/*
    SMS Library for the Java platform
    Copyright (C) 2002  Markus Eriksson

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
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
 *       String reciever = "464545425463";
 *       // Number of sender (not supported on all transports)
 *       String sender = "46534534535";
 *       smsSender.send("A sample SMS.", "+467056565", "+4646464646");
 *   }
 *   catch (SmsException ex)
 *   {
 *       ex.printStackTrace();
 *   }
 * </pre>
 *
 * @author Markus Eriksson
 * @version 1.0
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
    SmsSender(String theTransport, Properties theProps)
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
     * Sends an ordinary SMS to the given recipient.
     * <p>
     * - No limit on the number of concatenated SMS that this message will use.<br>
     * - Will send the message with the GSM charset (~160 chars/SMS)<br>
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
        sendTextSms(theMsg, theDest, theSender, -1, SmsConstants.ALPHABET_GSM);
    }

    /**
     * Sends an ordinary SMS to the given recipient.
     * <p>
     * - Will send the message with the GSM charset (~160 chars/SMS)<br>
     *
     * @param theMsg Message to send
     * @param theDest Destination number (international format without leading +)
     * Ex. 44546754235
     * @param theSender Destination number (international format without leading +).
     * Can also be an alphanumerical string. Ex "SMSJ". (not supported by all
     * transports).
     * @param theMaxSms Will not send message if the resulting message is longer
     * than this number of SMS
     * @throws SmsException
     */
    public void sendTextSms(String theMsg, String theDest, String theSender,  int theMaxSms)
        throws SmsException
    {
        sendTextSms(theMsg, theDest, theSender, theMaxSms, SmsConstants.ALPHABET_GSM);
    }

    /**
     * Sends an ordinary SMS to the given recipient.
     * <p>
     * - No limit on the number of concatenated SMS that this message will use.<br>
     * - Will send the message with the UCS2 charset (~70 chars/SMS)<br>
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
        sendTextSms(theMsg, theDest, theSender, -1, SmsConstants.ALPHABET_UCS2);
    }

    /**
     * Sends an ordinary SMS to the given recipient.
     * <p>
     * - Will send the message with the UCS2 charset (~70 chars/SMS)<br>
     *
     * @param theMsg Message to send
     * @param theDest Destination number (international format without leading +)
     * Ex. 44546754235
     * @param theSender Destination number (international format without leading +).
     * Can also be an alphanumerical string. Ex "SMSJ". (not supported by all
     * transports).
     * @param theMaxSms Will not send message if the resulting message is longer
     * than this number of SMS
     * @throws SmsException
     */
    public void sendUnicodeTextSms(String theMsg, String theDest, String theSender, int theMaxSms)
        throws SmsException
    {
        sendTextSms(theMsg, theDest, theSender, theMaxSms, SmsConstants.ALPHABET_UCS2);
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
    private void sendTextSms(String theMsg, String theDest, String theSender, int theMaxSms, int theAlphabet)
        throws SmsException
    {
        SmsPdu msgPdus[];
        SmsTextMessage textMessage = new SmsTextMessage(theMsg, theAlphabet);

        msgPdus = textMessage.getPdus();

        if (    (theMaxSms != -1)
             && (msgPdus.length > theMaxSms) )
        {
            throw new SmsException("The given message is to long to fit in " + theMaxSms + " SMS.");
        }

        // FIXME: Currently it is a bit stupid and always reconnects...
        myTransport.connect();
        myTransport.send(msgPdus, new SmsAddress(theDest), new SmsAddress(theSender));
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
