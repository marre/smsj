/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is "SMS Library for the Java platform".
 *
 * The Initial Developer of the Original Code is Markus Eriksson.
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package org.marre.sms.transport.pswincom;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.marre.sms.SmsAddress;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsDcs;
import org.marre.sms.SmsException;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsPdu;
import org.marre.sms.SmsPduUtil;
import org.marre.sms.SmsTextMessage;
import org.marre.sms.SmsUserData;
import org.marre.sms.transport.SmsTransport;
import org.marre.sms.transport.clickatell.ClickatellTransport;
import org.marre.util.IOUtil;
import org.marre.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * Simple transport for the pswin xml protocol.
 * 
 * See http://www.pswin.com/ for more information.
 * 
 * <pre>
 * Available properties:
 * smsj.pswincom.username
 * smsj.pswincom.password
 * smsj.pswincom.server - server address (default is "sms.pswin.com")
 * smsj.pswincom.port - port (default is "1111")
 * </pre>
 *  
 * @author Markus
 * @version $Id$
 */
public class PsWinXmlTransport implements SmsTransport
{
    private static Logger log_ = LoggerFactory.getLogger(PsWinXmlTransport.class);
    
    private String username_;
    private String password_;
    
    private String server_;
    private int port_;

    /**
     * Initializes the pswin transport.
     * 
     * @see org.marre.sms.transport.SmsTransport#init(java.util.Properties)
     */
    public void init(Properties props) throws SmsException
    {
        username_ = props.getProperty("smsj.pswincom.username");
        password_ = props.getProperty("smsj.pswincom.password");
        server_ = props.getProperty("smsj.pswincom.server", "sms.pswin.com");
        port_ = Integer.parseInt(props.getProperty("smsj.pswincom.port", "1111"));

        log_.debug("init() : username = " + username_);
        log_.debug("init() : password = " + password_);
        log_.debug("init() : server = " + server_);
        log_.debug("init() : port = " + port_);
        
        if ((username_ == null) || (password_ == null))
        {
            throw new SmsException("Incomplete login information for pswincom");
        }
    }

    private void addMsg(StringWriter xmlStringWriter, SmsPdu smsPdu, SmsAddress dest, SmsAddress sender) 
        throws SmsException
    {
        SmsUserData userData = smsPdu.getUserData();
        
        // <MSG>
        xmlStringWriter.write("<MSG>\r\n");
        // <RCPREQ>Y</RCPREQ>
        xmlStringWriter.write("<RCPREQ>Y</RCPREQ>\r\n");

        switch (smsPdu.getDcs().getAlphabet())
        {
        case SmsDcs.ALPHABET_UCS2:
            // <OP>9</OP>
            xmlStringWriter.write("<OP>9</OP>\r\n");
            // <TEXT>hex-text</TEXT>
            xmlStringWriter.write("<TEXT>");
            xmlStringWriter.write(StringUtil.bytesToHexString(userData.getData()));
            xmlStringWriter.write("</TEXT>\r\n");
            break;

        case SmsDcs.ALPHABET_GSM:
            // <TEXT>txt</TEXT>
            xmlStringWriter.write("<TEXT>");
            xmlStringWriter.write(SmsPduUtil.readSeptets(userData.getData(), userData.getLength()));
            xmlStringWriter.write("</TEXT>\r\n");
            break;

        case SmsDcs.ALPHABET_8BIT:
            // <OP>8</OP>
            xmlStringWriter.write("<OP>8</OP>\r\n");
            // <TEXT>udh-and-ud</TEXT>
            xmlStringWriter.write("<TEXT>");
            xmlStringWriter.write(StringUtil.bytesToHexString(smsPdu.getUserDataHeaders())
                    + StringUtil.bytesToHexString(userData.getData()));
            xmlStringWriter.write("</TEXT>\r\n");
            break;

        default:
            throw new SmsException("Unsupported alphabet");
        }

        // <RCV>434343434</RCV>
        xmlStringWriter.write("<RCV>");
        xmlStringWriter.write(dest.getAddress());
        xmlStringWriter.write("</RCV>\r\n");

        if (sender != null)
        {
            // <SND>434344</SND>
            xmlStringWriter.write("<SND>");
            xmlStringWriter.write(sender.getAddress());
            xmlStringWriter.write("</SND>\r\n");
        }

        if (smsPdu.getDcs().getMessageClass() == SmsDcs.MSG_CLASS_0)
        {
            // <CLASS>0</CLASS>
            xmlStringWriter.write("<CLASS>");
            xmlStringWriter.write("0");
            xmlStringWriter.write("</CLASS>\r\n");
        }

        // </MSG>
        xmlStringWriter.write("</MSG>\r\n");
    }

    private void addTextMsg(StringWriter xmlStringWriter, SmsTextMessage msg, SmsAddress dest, SmsAddress sender) 
        throws SmsException
    {
        SmsUserData userData = msg.getUserData();

        // <MSG>
        xmlStringWriter.write("<MSG>\r\n");

        switch (userData.getDcs().getAlphabet())
        {
        case SmsDcs.ALPHABET_UCS2:
            // <OP>9</OP>
            xmlStringWriter.write("<OP>9</OP>\r\n");
            // <TEXT>hex-text</TEXT>
            xmlStringWriter.write("<TEXT>");
            xmlStringWriter.write(StringUtil.bytesToHexString(userData.getData()));
            xmlStringWriter.write("</TEXT>\r\n");
            break;

        case SmsDcs.ALPHABET_GSM:
            // <TEXT>txt</TEXT>
            xmlStringWriter.write("<TEXT>");
            xmlStringWriter.write(msg.getText());
            xmlStringWriter.write("</TEXT>\r\n");
            break;

        default:
            throw new SmsException("Unsupported alphabet");
        }

        // <RCV>434343434</RCV>
        xmlStringWriter.write("<RCV>");
        xmlStringWriter.write(dest.getAddress());
        xmlStringWriter.write("</RCV>\r\n");

        if (sender != null)
        {
            // <SND>434344</SND>
            xmlStringWriter.write("<SND>");
            xmlStringWriter.write(sender.getAddress());
            xmlStringWriter.write("</SND>\r\n");
        }

        if (userData.getDcs().getMessageClass() == SmsDcs.MSG_CLASS_0)
        {
            // <CLASS>0</CLASS>
            xmlStringWriter.write("<CLASS>");
            xmlStringWriter.write("0");
            xmlStringWriter.write("</CLASS>\r\n");
        }

        // </MSG>
        xmlStringWriter.write("</MSG>\r\n");
    }

    /**
     * Creates a pswin xml document and writes it to the given outputstream.
     * 
     * @param os
     * @param msg
     * @param dest
     * @param sender
     * @throws IOException
     * @throws SmsException
     */
    private void writeXmlTo(OutputStream os, SmsMessage msg, 
                              SmsAddress dest, SmsAddress sender)
            throws IOException, SmsException
    {
        StringWriter xmlWriter = new StringWriter(1024);

        // <?xml version="1.0"?>
        // <SESSION>
        // <CLIENT>$myUsername</CLIENT>
        // <PW>$myPassword</PW>
        // <MSGLST>
        xmlWriter.write("<?xml version=\"1.0\"?>\r\n");
        xmlWriter.write("<SESSION>\r\n");
        xmlWriter.write("<CLIENT>" + username_ + "</CLIENT>\r\n");
        xmlWriter.write("<PW>" + password_ + "</PW>\r\n");
        xmlWriter.write("<MSGLST>\r\n");

        // <MSG>...</MSG>
        if (msg instanceof SmsTextMessage)
        {
            addTextMsg(xmlWriter, (SmsTextMessage) msg, dest, sender);
        }
        else
        {
            SmsPdu[] msgPdu = msg.getPdus();
            for (int i = 0; i < msgPdu.length; i++)
            {
                addMsg(xmlWriter, msgPdu[i], dest, sender);
            }
        }

        // </MSGLST>
        // </SESSION>
        xmlWriter.write("</MSGLST>\r\n");
        xmlWriter.write("</SESSION>\r\n");

        // Finally write XML to stream
        String xmlDoc = xmlWriter.toString();
        os.write(xmlDoc.getBytes());
    }

    /**
     * Sends the given xml request to pswin for processing.
     * 
     * @param xmlReq
     * @throws IOException
     * @throws SmsException
     */
    private void sendReqToPsWinCom(byte[] xmlReq) throws IOException, SmsException
    {
        Socket xmlSocket = new Socket(server_, port_);

        // Send request
        OutputStream os = xmlSocket.getOutputStream();
        os.write(xmlReq);

        // Get response
        InputStream is = xmlSocket.getInputStream();
        
        // Parse response
        PsWinXmlResponseParser responseParser = new PsWinXmlResponseParser(is);
        responseParser.parse();

        // Verify that we could logon correctly
        if (! responseParser.getLogon().equals("OK"))
        {
            throw new SmsException("Failed to send message: " + responseParser.getReason());
        }
    }

    /**
     * Send.
     * 
     * @param msg 
     * @param dest 
     * @param sender 
     * @return Internal message id. 
     * @throws SmsException 
     * @throws IOException 
     * 
     * @see org.marre.sms.transport.SmsTransport#send()
     */
    public String send(SmsMessage msg, SmsAddress dest, SmsAddress sender) throws SmsException, IOException
    {
        byte[] xmlReq;
        
        if (dest.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC)
        {
            throw new SmsException("Cannot sent SMS to ALPHANUMERIC address");
        }

        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);

            // Create xml document
            writeXmlTo(baos, msg, dest, sender);
            baos.close();
            
            xmlReq = baos.toByteArray();
        }
        catch (IOException ex)
        {
            throw new SmsException("Failed to build xml request", ex);
        }

        // Send req
        sendReqToPsWinCom(xmlReq);
        
        // TODO: Return an internal message id
        return null;
    }

    /**
     * Connect.
     * 
     * @see org.marre.sms.transport.SmsTransport#connect()
     */
    public void connect()
    {
        // Empty
    }
    
    /**
     * Disconnect.
     * 
     * @see org.marre.sms.transport.SmsTransport#disconnect()
     */
    public void disconnect()
    {
        // Empty
    }

    /**
     * Ping.
     * 
     * @see org.marre.sms.transport.SmsTransport#ping()
     */
    public void ping()
    {
        // Empty
    }
}
