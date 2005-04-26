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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Properties;

import org.marre.sms.SmsAddress;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsDcsUtil;
import org.marre.sms.SmsException;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsPdu;
import org.marre.sms.SmsPduUtil;
import org.marre.sms.SmsTextMessage;
import org.marre.sms.SmsUserData;
import org.marre.sms.transport.SmsTransport;
import org.marre.util.StringUtil;

public class PsWinXmlTransport implements SmsTransport
{
    private String myUsername;
    private String myPassword;

    public void init(Properties theProps) throws SmsException
    {
        myUsername = theProps.getProperty("smsj.pswincom.username");
        myPassword = theProps.getProperty("smsj.pswincom.password");

        if ((myUsername == null) || (myPassword == null))
        {
            throw new SmsException("Incomplete login information for pswincom");
        }
    }

    public void connect() throws SmsException, IOException
    {
    }

    protected void addMsg(StringWriter theXmlWriter, SmsPdu thePdu, SmsAddress theDestination,
            SmsAddress theSender) throws SmsException
    {
        SmsUserData userData = thePdu.getUserData();
        
        // <MSG>
        theXmlWriter.write("<MSG>\r\n");

        switch (SmsDcsUtil.getAlphabet(thePdu.getDcs()))
        {
        case SmsConstants.ALPHABET_UCS2:
            // <OP>9</OP>
            theXmlWriter.write("<OP>9</OP>\r\n");
            // <TEXT>hex-text</TEXT>
            theXmlWriter.write("<TEXT>");
            theXmlWriter.write(StringUtil.bytesToHexString(userData.getData()));
            theXmlWriter.write("</TEXT>\r\n");
            break;

        case SmsConstants.ALPHABET_GSM:
            // <TEXT>txt</TEXT>
            theXmlWriter.write("<TEXT>");
            theXmlWriter.write(SmsPduUtil.readSeptets(userData.getData(), userData.getLength()));
            theXmlWriter.write("</TEXT>\r\n");
            break;

        case SmsConstants.ALPHABET_8BIT:
            // <OP>8</OP>
            theXmlWriter.write("<OP>8</OP>\r\n");
            // <TEXT>udh-and-ud</TEXT>
            theXmlWriter.write("<TEXT>");
            theXmlWriter.write(StringUtil.bytesToHexString(thePdu.getUserDataHeaders())
                    + StringUtil.bytesToHexString(userData.getData()));
            theXmlWriter.write("</TEXT>\r\n");
            break;

        default:
            throw new SmsException("Unsupported alphabet");
        }

        // <RCV>434343434</RCV>
        theXmlWriter.write("<RCV>");
        theXmlWriter.write(theDestination.getAddress());
        theXmlWriter.write("</RCV>\r\n");

        if (theSender != null)
        {
            // <SND>434344</SND>
            theXmlWriter.write("<SND>");
            theXmlWriter.write(theSender.getAddress());
            theXmlWriter.write("</SND>\r\n");
        }

        if (SmsDcsUtil.getMessageClass(thePdu.getDcs()) == SmsConstants.MSG_CLASS_0)
        {
            // <CLASS>0</CLASS>
            theXmlWriter.write("<CLASS>");
            theXmlWriter.write("0");
            theXmlWriter.write("</CLASS>\r\n");
        }

        // </MSG>
        theXmlWriter.write("</MSG>\r\n");
    }

    private void addTextMsg(StringWriter theXmlWriter, SmsTextMessage theMessage, SmsAddress theDestination,
            SmsAddress theSender) throws SmsException
    {
        // 70 UCS
        // 160 GSM
        
        SmsUserData userData = theMessage.getUserData();

        // <MSG>
        theXmlWriter.write("<MSG>\r\n");

        switch (SmsDcsUtil.getAlphabet(userData.getDcs()))
        {
        case SmsConstants.ALPHABET_UCS2:
            // <OP>9</OP>
            theXmlWriter.write("<OP>9</OP>\r\n");
            // <TEXT>hex-text</TEXT>
            theXmlWriter.write("<TEXT>");
            theXmlWriter.write(StringUtil.bytesToHexString(userData.getData()));
            theXmlWriter.write("</TEXT>\r\n");
            break;

        case SmsConstants.ALPHABET_GSM:
            // <TEXT>txt</TEXT>
            theXmlWriter.write("<TEXT>");
            theXmlWriter.write(theMessage.getText());
            theXmlWriter.write("</TEXT>\r\n");
            break;

        default:
            throw new SmsException("Unsupported alphabet");
        }

        // <RCV>434343434</RCV>
        theXmlWriter.write("<RCV>");
        theXmlWriter.write(theDestination.getAddress());
        theXmlWriter.write("</RCV>\r\n");

        if (theSender != null)
        {
            // <SND>434344</SND>
            theXmlWriter.write("<SND>");
            theXmlWriter.write(theSender.getAddress());
            theXmlWriter.write("</SND>\r\n");
        }

        if (SmsDcsUtil.getMessageClass(userData.getDcs()) == SmsConstants.MSG_CLASS_0)
        {
            // <CLASS>0</CLASS>
            theXmlWriter.write("<CLASS>");
            theXmlWriter.write("0");
            theXmlWriter.write("</CLASS>\r\n");
        }

        // </MSG>
        theXmlWriter.write("</MSG>\r\n");
    }

    protected void writeXmlTo(OutputStream theOs, SmsMessage theMessage, 
                              SmsAddress theDestination, SmsAddress theSender)
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
        xmlWriter.write("<CLIENT>" + myUsername + "</CLIENT>\r\n");
        xmlWriter.write("<PW>" + myPassword + "</PW>\r\n");
        xmlWriter.write("<MSGLST>\r\n");

        // <MSG>...</MSG>
        if (theMessage instanceof SmsTextMessage)
        {
            addTextMsg(xmlWriter, (SmsTextMessage) theMessage, theDestination, theSender);
        }
        else
        {
            SmsPdu[] msgPdu = theMessage.getPdus();
            for (int i = 0; i < msgPdu.length; i++)
            {
                addMsg(xmlWriter, msgPdu[i], theDestination, theSender);
            }
        }

        // </MSGLST>
        // </SESSION>
        xmlWriter.write("</MSGLST>\r\n");
        xmlWriter.write("</SESSION>\r\n");

        // Finally write XML to stream
        String xmlDoc = xmlWriter.toString();
        theOs.write(xmlDoc.getBytes());
    }

    protected String[] sendReqToPsWinCom(byte[] theXmlReq) throws IOException
    {
        Socket xmlSocket = new Socket("sms.pswin.com", 1111);

        // Send request
        OutputStream os = xmlSocket.getOutputStream();
        os.write(theXmlReq);

        // TODO: parse response
        InputStream is = xmlSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String resp;
        while ((resp = reader.readLine()) != null)
        {
            System.err.println(resp);
        }
        
        return null;
    }

    public String[] send(SmsMessage theMessage, SmsAddress theDestination, SmsAddress theSender) throws SmsException, IOException
    {
        String[] msgIds;
        byte[] xmlReq;
        
        if (theDestination.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC)
        {
            throw new SmsException("Cannot sent SMS to ALPHANUMERIC address");
        }

        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);

            // Create xml document
            writeXmlTo(baos, theMessage, theDestination, theSender);
            baos.close();
            
            xmlReq = baos.toByteArray();
        }
        catch (IOException ex)
        {
            throw new SmsException("Failed to build xml request", ex);
        }

        // Send req
        msgIds = sendReqToPsWinCom(xmlReq);
        
        return msgIds;
    }

    public void disconnect() throws SmsException, IOException
    {
    }

    public void ping() throws SmsException, IOException
    {
        // TODO Auto-generated method stub
        
    }
}
