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
package org.marre.sms;

import java.util.Properties;
import java.io.*;

import org.marre.sms.transport.SmsTransport;
import org.marre.sms.transport.SmsTransportManager;

/**
 * High level API to the smsj library
 * <p>
 * If you only need to send some basic SMS messages than you only have
 * to use this API.
 * Ex:
 * <pre>
 *   try
 *   {
 *       // Send SMS with clickatell
 *       SmsSender smsSender = SmsSender.getClickatellSender("username", "password", "apiid");
 *       String msg = "A sample SMS.";
 *       // International number to reciever without leading "+"
 *       String reciever = "+464545425463";
 *       // Number of sender (not supported on all transports)
 *       String sender = "+46534534535";
 *       smsSender.send("A sample SMS.", reciever, sender);
 *   }
 *   catch (SmsException ex)
 *   {
 *       ex.printStackTrace();
 *   }
 * </pre>
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class SmsSender
{
    SmsTransport myTransport = null;

    /**
     * Creates a SmsSender object by using the given transport and properties.
     * <p>
     * You can also use getClickatellSender(...) to create a SmsSender object
     *
     * @param theTransport Classname of the SmsTransport class
     * @param theProps Properties to initialize the transport with
     * @throws SmsException
     */
    public SmsSender(String theTransport, Properties theProps)
        throws SmsException
    {
        myTransport = SmsTransportManager.getTransport(theTransport, theProps);
    }

    /**
     * Convenience method to create a SmsSender object that knows how to send
     * messages with the Clickatell service.
     *
     * @param theUsername Clickatell username
     * @param thePassword Clickatell password
     * @param theApiId Clickatell api-id
     * @return A SmsSender object that uses the ClickatellTransport to send
     * messages
     * @throws SmsException
     */
    static public SmsSender getClickatellSender(String theUsername, String thePassword, String theApiId)
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
     * @param thePropsFilename Filename of a properties file containing
     * properties for the clickatell transport.
     * @return A SmsSender object that uses the ClickatellTransport to send
     * messages
     * @throws SmsException
     */
    static public SmsSender getClickatellSender(String thePropsFilename)
        throws SmsException
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
     * @param theSerialPort Serial port where your phone is located. Ex "COM1:"
     * @return A SmsSender object that uses the GsmTranport to send
     * messages
     * @throws SmsException
     */
    static public SmsSender getGsmSender(String theSerialPort)
        throws SmsException
    {
        Properties props = new Properties();

        props.setProperty("sms.gsm.serialport", theSerialPort);

        return new SmsSender("org.marre.sms.transport.gsm.GsmTransport", props);
    }

    /**
     * Sends an ordinary SMS to the given recipient.
     * <p>
     * - No limit on the number of concatenated SMS that this message will use.<br>
     * - Will send the message with the GSM charset (Max 160 chars/SMS)<br>
     *
     * @param theMsg Message to send
     * @param theDest Destination number (international format without leading +)
     * Ex. 44546754235
     * @param theSender Destination number (international format without leading +).
     * Can also be an alphanumerical string. Ex "SMSJ". (not supported by all
     * transports).
     * @throws SmsException
     */
    public void sendTextSms(String theMsg, String theDest, String theSender)
        throws SmsException
    {
        sendTextSms(theMsg, theDest, theSender, SmsConstants.ALPHABET_GSM);
    }

    /**
     * Sends an ordinary SMS to the given recipient.
     * <p>
     * - No limit on the number of concatenated SMS that this message will use.<br>
     * - Will send the message with the UCS2 charset (MAX 70 chars/SMS)<br>
     *
     * @param theMsg Message to send
     * @param theDest Destination number (international format without leading +)
     * Ex. 44546754235
     * @param theSender Destination number (international format without leading +).
     * Can also be an alphanumerical string. Ex "SMSJ". (not supported by all
     * transports).
     * @throws SmsException
     */
    public void sendUnicodeTextSms(String theMsg, String theDest, String theSender)
        throws SmsException
    {
        sendTextSms(theMsg, theDest, theSender, SmsConstants.ALPHABET_UCS2);
    }

    /**
     * Used internally to actually send the message
     *
     * @param theMsg Message
     * @param theDest Dest phonenumber
     * @param theSender Sending address
     * @param theMaxSms Max no of SMS (Set to -1 if "unlimited")
     * @param theAlphabet Alphabet to use. Can be any of SmsConstants.ALPHABET_*
     * @throws SmsException
     */
    private void sendTextSms(String theMsg, String theDest, String theSender, int theAlphabet)
        throws SmsException
    {
        SmsTextMessage textMessage = new SmsTextMessage(theMsg, theAlphabet);

        // FIXME: Currently it is a bit stupid and always reconnects...
        myTransport.connect();
        myTransport.send(textMessage, new SmsAddress(theDest), new SmsAddress(theSender));
        myTransport.disconnect();
    }

    /**
     * Call this when you are done with the SmsSender object.
     * <p>
     * It will free any resources that we have used.
     * @throws SmsException
     */
    public void close()
        throws SmsException
    {
        if (myTransport != null)
        {
            myTransport.disconnect();
            myTransport = null;
        }
    }

    // Probably never called. But good to have if the caller forget to close()
    protected void finalize()
    {
        // Disconnect transport if the caller forget to close us
        try { close(); } catch (SmsException ex) { }
    }
}
