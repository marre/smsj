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
package org.marre;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.marre.sms.SmsAddress;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsException;
import org.marre.sms.SmsTextMessage;
import org.marre.sms.SmsTransport;
import org.marre.sms.SmsTransportManager;
import org.marre.wap.nokia.NokiaOtaBrowserSettings;
import org.marre.wap.push.SmsWapPushMessage;
import org.marre.wap.push.WapSIPush;

/**
 * High level API to the smsj library
 * <p>
 * If you only need to send some basic SMS messages than you only have to use
 * this API. Ex:
 * 
 * <pre>
 * try
 * {
 *     // Send SMS with clickatell
 *     SmsSender smsSender = SmsSender.getClickatellSender(&quot;username&quot;, &quot;password&quot;, &quot;apiid&quot;);
 *     String msg = &quot;A sample SMS.&quot;;
 *     // International number to reciever without leading &quot;+&quot;
 *     String reciever = &quot;+464545425463&quot;;
 *     // Number of sender (not supported on all transports)
 *     String sender = &quot;+46534534535&quot;;
 *     smsSender.send(&quot;A sample SMS.&quot;, reciever, sender);
 * }
 * catch (SmsException ex)
 * {
 *     ex.printStackTrace();
 * }
 * </pre>
 * 
 * @author Markus Eriksson
 * @version $Id$
 */
public class SmsSender
{
    protected SmsTransport myTransport;

    /**
     * Creates a SmsSender object by using the given transport and properties.
     * <p>
     * You can also use getClickatellSender(...) to create a SmsSender object
     * 
     * @param theTransport
     *            Classname of the SmsTransport class
     * @param theProps
     *            Properties to initialize the transport with
     * @throws SmsException
     */
    public SmsSender(String theTransport, Properties theProps) throws SmsException
    {
        myTransport = SmsTransportManager.getTransport(theTransport, theProps);
        myTransport.connect();
    }

    /**
     * Convenience method to create a SmsSender object that knows how to send
     * messages with the Clickatell service.
     * 
     * @param theUsername
     *            Clickatell username
     * @param thePassword
     *            Clickatell password
     * @param theApiId
     *            Clickatell api-id
     * @return A SmsSender object that uses the ClickatellTransport to send
     *         messages
     * @throws SmsException
     */
    public static SmsSender getClickatellSender(String theUsername, String thePassword, String theApiId)
            throws SmsException
    {
        Properties props = new Properties();

        props.setProperty("smsj.clickatell.username", theUsername);
        props.setProperty("smsj.clickatell.password", thePassword);
        props.setProperty("smsj.clickatell.apiid", theApiId);

        return new SmsSender("org.marre.sms.transport.clickatell.ClickatellTransport", props);
    }

    /**
     * Convenience method to create a SmsSender object that knows how to send
     * messages with the Clickatell service.
     * 
     * @param thePropsFilename
     *            Filename of a properties file containing properties for the
     *            clickatell transport.
     * @return A SmsSender object that uses the ClickatellTransport to send
     *         messages
     * @throws SmsException
     */
    public static SmsSender getClickatellSender(String thePropsFilename) throws SmsException
    {
        Properties props = new Properties();

        try
        {
            props.load(new FileInputStream(thePropsFilename));
        }
        catch (IOException ex)
        {
            throw new SmsException("Couldn't load clickatell properties from file : " + thePropsFilename);
        }

        return new SmsSender("org.marre.sms.transport.clickatell.ClickatellTransport", props);
    }

    /**
     * Convenience method to create a SmsSender object that knows how to send
     * messages with a GSM phone attached to the serial port on your computer.
     * 
     * @param theSerialPort
     *            Serial port where your phone is located. Ex "COM1:"
     * @return A SmsSender object that uses the GsmTranport to send messages
     * @throws SmsException
     */
    public static SmsSender getGsmSender(String theSerialPort) throws SmsException
    {
        Properties props = new Properties();

        props.setProperty("sms.gsm.serialport", theSerialPort);

        return new SmsSender("org.marre.sms.transport.gsm.GsmTransport", props);
    }

    /**
     * Convenience method to create a SmsSender object that knows how to send
     * messages with a UCP SMSC.
     * 
     * @param theIpHost
     *            A string with the ip address or host name of the SMSC
     * @param theIpPort
     *            An integer with the ip port on which the SMSC listens
     * @param password
     * @return A SmsSender object that uses the UcpTranport to send messages
     * @throws SmsException
     */
    public static SmsSender getUcpSender(String theIpHost, int theIpPort) throws SmsException
    {
        //Liquidterm: strict input checking is done in the UcpTransport class
        Properties props = new Properties();
        props.setProperty("smsj.ucp.ip.host", theIpHost);
        props.setProperty("smsj.ucp.ip.port", Integer.toString(theIpPort));
        return new SmsSender("org.marre.sms.transport.ucp.UcpTransport", props);
    }

    /**
     * Convenience method to create a SmsSender object that knows how to send
     * messages with a UCP SMSC.
     * 
     * @param theIpHost
     *            A string with the ip address or host name of the SMSC
     * @param theIpPort
     *            An integer with the ip port on which the SMSC listens
     * @param theUCP60Uid
     *            A string containing the UCP60 userid
     * @param theUCP60Pwd
     *            A string containing the UCP60 password
     * @return A SmsSender object that uses the UcpTranport to send messages
     * @throws SmsException
     */
    public static SmsSender getUcpSender(String theIpHost, int theIpPort, String theUCP60Uid, String theUCP60Pwd)
            throws SmsException
    {
        //Liquidterm: strict input checking is done in the UcpTransport class
        Properties props = new Properties();
        props.setProperty("smsj.ucp.ip.host", theIpHost);
        props.setProperty("smsj.ucp.ip.port", Integer.toString(theIpPort));
        props.setProperty("smsj.ucp.ucp60.uid", theUCP60Uid);
        props.setProperty("smsj.ucp.ucp60.password", theUCP60Pwd);
        return new SmsSender("org.marre.sms.transport.ucp.UcpTransport", props);
    }

    /**
     * Convenience method to create a SmsSender object that knows how to send
     * messages with a UCP SMSC.
     * 
     * @param thePropsFilename
     *            A string containt a filename with the serialized Properties
     *            object for the transport
     * @return A SmsSender object that uses the UcpTranport to send messages
     * @throws SmsException
     */
    public static SmsSender getUcpSender(String thePropsFilename) throws SmsException
    {
        //Liquidterm: strict input checking is done in the UcpTransport class
        Properties props = new Properties();
        try
        {
            props.load(new FileInputStream(thePropsFilename));
        }
        catch (IOException ex)
        {
            throw new SmsException("Couldn't load UCP transport properties " + "from file : " + thePropsFilename);
        }
        return new SmsSender("org.marre.sms.transport.ucp.UcpTransport", props);
    }

    /**
     * Convenience method to create a SmsSender object that knows how to send
     * messages via PSWinComm
     * 
     * @param theUsername
     *            PsWinComm username
     * @param thePassword
     *            PsWinComm password
     * @return A SmsSender object that uses the PsWinXmlTransport to send
     *         messages
     * @throws SmsException
     */
    public static SmsSender getPsWinCommXmlSender(String theUsername, String thePassword) throws SmsException
    {
        Properties props = new Properties();

        props.setProperty("smsj.pswincom.username", theUsername);
        props.setProperty("smsj.pswincom.password", thePassword);

        return new SmsSender("org.marre.sms.transport.pswincom.PsWinXmlTransport", props);
    }

    /**
     * Convenience method to create a SmsSender object that knows how to send
     * messages via PSWinComm
     * 
     * @param thePropsFilename
     *            Filename of a properties file containing properties for the
     *            pswincomm transport.
     * @return A SmsSender object that uses the PsWinXmlTransport to send
     *         messages
     * @throws SmsException
     */
    public static SmsSender getPsWinCommXmlSender(String thePropsFilename) throws SmsException
    {
        Properties props = new Properties();

        try
        {
            props.load(new FileInputStream(thePropsFilename));
        }
        catch (IOException ex)
        {
            throw new SmsException("Couldn't load clickatell properties from file : " + thePropsFilename);
        }

        return new SmsSender("org.marre.sms.transport.pswincom.PsWinXmlTransport", props);
    }

    /**
     * Sends an ordinary SMS to the given recipient.
     * <p>- No limit on the number of concatenated SMS that this message will
     * use. <br>- Will send the message with the GSM charset (Max 160
     * chars/SMS) <br>
     * 
     * @param theMsg
     *            Message to send
     * @param theDest
     *            Destination number (international format without leading +)
     *            Ex. 44546754235
     * @param theSender
     *            Destination number (international format without leading +).
     *            Can also be an alphanumerical string. Ex "SMSJ". (not
     *            supported by all transports).
     * @throws SmsException
     */
    public void sendTextSms(String theMsg, String theDest, String theSender) throws SmsException
    {
        sendTextSms(theMsg, theDest, theSender, SmsConstants.ALPHABET_GSM);
    }

    /**
     * Sends an ordinary SMS to the given recipient.
     * <p>- No limit on the number of concatenated SMS that this message will
     * use. <br>- Will send the message with the UCS2 charset (MAX 70
     * chars/SMS) <br>
     * 
     * @param theMsg
     *            Message to send
     * @param theDest
     *            Destination number (international format without leading +)
     *            Ex. 44546754235
     * @param theSender
     *            Destination number (international format without leading +).
     *            Can also be an alphanumerical string. Ex "SMSJ". (not
     *            supported by all transports).
     * @throws SmsException
     */
    public void sendUnicodeTextSms(String theMsg, String theDest, String theSender) throws SmsException
    {
        sendTextSms(theMsg, theDest, theSender, SmsConstants.ALPHABET_UCS2);
    }

    /**
     * Used internally to actually send the message
     * 
     * @param theMsg
     *            Message
     * @param theDest
     *            Dest phonenumber
     * @param theSender
     *            Sending address
     * @param theMaxSms
     *            Max no of SMS (Set to -1 if "unlimited")
     * @param theAlphabet
     *            Alphabet to use. Can be any of SmsConstants.ALPHABET_*
     * @throws SmsException
     */
    private void sendTextSms(String theMsg, String theDest, String theSender, int theAlphabet) throws SmsException
    {
        SmsTextMessage textMessage = new SmsTextMessage(theMsg, theAlphabet);

        myTransport.send(textMessage, new SmsAddress(theDest), new SmsAddress(theSender));
    }

   /**
    *
    * Sends an OTA Bookmark (Nokia specification) to the given recipient
    * 
    * @param theTitle String with the title of the bookmark
    * @param theUri String with the url referenced by the bookmark
    * @param theDest String with the recipient number (international format
    * without leading +)
    * @param theSender String with the sender number. Can also be an
    * alphanumerical string (not supported by all transports).
    *
    * @throws SmsException
    */
   public void sendNokiaBookmark(String theTitle, String theUri, String theDest, String theSender) throws SmsException 
   {
       NokiaOtaBrowserSettings browserSettings = new NokiaOtaBrowserSettings();
       browserSettings.addBookmark(theTitle, theUri);
       
       SmsWapPushMessage wapPushMessage = new SmsWapPushMessage(browserSettings);
       wapPushMessage.setPushPorts(49154, SmsConstants.PORT_OTA_SETTINGS_BROWSER);
       
       SmsAddress sender = new SmsAddress(theSender);
       SmsAddress reciever = new SmsAddress(theDest);
       
       myTransport.send(wapPushMessage, reciever, sender);
   }
    
  /**
   *
   * Sends a Wap Push SI containing to the given recipient
   * 
   * @param theMessage String with the description of the service
   * @param theUri String with the url referenced by the SI
   * @param theDest String with the recipient number
   * @param theSender String with the sender number. Can also be an
   * alphanumerical string (not supported by all transports).
   *
   * @throws SmsException
   */
  public void sendWapSiPushMsg(String theUri, String theMessage, String theDest, String theSender) throws SmsException 
  {
      //Liquidterm: add some URI checking
      //add checking on url and message length
      WapSIPush siPush = new WapSIPush(theUri, theMessage);
      
      SmsWapPushMessage wapPushMessage = new SmsWapPushMessage(siPush);
      
      SmsAddress sender = new SmsAddress(theSender);
      SmsAddress reciever = new SmsAddress(theDest);
      
      myTransport.send(wapPushMessage, reciever, sender);
  }
   
    /**
     * Call this when you are done with the SmsSender object.
     * <p>
     * It will free any resources that we have used.
     * 
     * @throws SmsException
     */
    public void close() throws SmsException
    {
        if (myTransport != null)
        {
            myTransport.disconnect();
            myTransport = null;
        }
    }

    // Probably never called. But good to have if the caller forget to close()
    protected void finalize() throws Throwable
    {
        // Disconnect transport if the caller forget to close us
        try
        {
            close();
        }
        catch (SmsException ex)
        {
        }
        
        super.finalize();
    }
}
