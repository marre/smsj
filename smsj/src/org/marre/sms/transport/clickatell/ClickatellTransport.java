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
package org.marre.sms.transport.clickatell;

import java.io.*;
import java.net.*;
import java.text.*;

import org.marre.sms.transport.SmsTransport;
import java.util.Properties;
import org.marre.sms.*;
import org.marre.sms.util.*;

/**
 * An SmsTransport that sends the SMS with clickatell over HTTP.
 * <p>
 * It is developed to use the "Clickatell HTTP API v. 1.63".
 * <p>
 * Known limitations:<br>
 * - Impossible to set the sending address (might work with some networks)<br>
 * - Cannot send 8-Bit messages without an UDH<br>
 * - Doesn't support a complete DCS. Only UCS2, 7bit, 8bit and
 *   SMS class 0 or 1.<br>
 * - Cannot set validity period (not done yet)<br>
 * - Doesn't acknowledge the TON or NPI, everything is sent as NPI_ISDN_TELEPHONE
 * and TON_INTERNATIONAL.<br>
 * <p>
 * Support matrix:
 * <table border="1">
 * <tr>
 * <td></td>
 * <td>7-bit</td>
 * <td>8-bit</td>
 * <td>UCS2</td>
 * </tr>
 * <tr>
 * <td>CLASS 0</td>
 * <td>Yes</td>
 * <td>Yes, with UDH present</td>
 * <td>Yes</td>
 * </tr>
 * <tr>
 * <td>CLASS 1</td>
 * <td>Yes</td>
 * <td>Yes, with UDH present</td>
 * <td>Yes</td>
 * </tr>
 * <tr>
 * <td>UDH and<br>concatenation</td>
 * <td>No</td>
 * <td>Yes</td>
 * <td>Yes</td>
 * </tr>
 * <tr>
 * <td>ass</td>
 * </tr>
 * </table>
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public class ClickatellTransport implements SmsTransport
{
    private String myUsername = null;
    private String myPassword = null;
    private String myApiId = null;
    private String mySessionId = null;

    /**
     * Initializes the transport
     * <p>
     * It expects the following properties in theProps param:
     * <pre>
     * smsj.clickatell.username - clickatell username
     * smsj.clickatell.password - clickatell password
     * smsj.clickatell.apiid    - clickatell apiid
     * </pre>
     * @param theProps Properties to initialize the library
     * @throws SmsException If not given the needed params
     */
    public void init(Properties theProps) throws SmsException
    {
        myUsername = theProps.getProperty("smsj.clickatell.username");
        myPassword = theProps.getProperty("smsj.clickatell.password");
        myApiId = theProps.getProperty("smsj.clickatell.apiid");

        if (    (myUsername == null)
             || (myPassword == null)
             || (myApiId == null) )
        {
            throw new SmsException("Incomplete login information for clickatell");
        }
    }

    /**
     * Sends an auth command to clickatell to get an session id that
     * can be used later.
     *
     * @throws SmsException If we fail to authenticate to clickatell or if
     * we fail to connect.
     */
    public void connect() throws SmsException
    {
        String response = null;
        MessageFormat responseFormat = new MessageFormat("{0}: {1}");
        String requestString = MessageFormat.format(
                "http://api.clickatell.com/http/auth?api_id={0}&user={1}&password={2}",
                new Object[] { myApiId, myUsername, myPassword });

        try
        {
            URL requestURL = new URL(requestString);

            // Connect
            BufferedReader responseReader =
                new BufferedReader(new InputStreamReader(requestURL.openStream()));

            // Read response
            response = responseReader.readLine();
            responseReader.close();

            // Parse response
            Object[] objs = responseFormat.parse(response);

            if ( "OK".equalsIgnoreCase((String)objs[0]) )
            {
                // Store session id
                mySessionId = (String)objs[1];
            }
            else
            {
                // ERR
                String errorMsg = (String) objs[1];
                throw new SmsException("Clickatell error. Error " + errorMsg);
            }
        }
        catch (ParseException ex)
        {
            throw new SmsException("Unexpected response from Clickatell. : " + response);
        }
        catch (MalformedURLException ex)
        {
            throw new SmsException(ex.getMessage());
        }
        catch (IOException ex)
        {
            throw new SmsException(ex.getMessage());
        }
    }

    /**
     * Sends an sendmsg command to clickatell
     *
     * @param thePdu
     * @param theDcs
     * @param theDestination
     * @param theSender
     * @throws SmsException If clickatell sends an error message, unexpected
     * response or if  we fail to connect.
     */
    public void send(SmsPdu thePdu, byte theDcs, SmsAddress theDestination, SmsAddress theSender) throws SmsException
    {
        String response = null;
        MessageFormat responseFormat = new MessageFormat("{0}: {1}");
        String requestString;
        String ud;
        byte udhData[];
        String udh;

        if (theDestination.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC)
        {
            throw new SmsException("Cannot sent SMS to an ALPHANUMERIC address");
        }

        if (mySessionId == null)
        {
            throw new SmsException("Must connect before sending");
        }

        //
        // Generate request URL
        //
        if (thePdu.getUserDataHeaders() == null)
        {
            //
            // Message without UDH
            //
            switch (SmsDcsUtil.getAlphabet(theDcs))
            {
            case SmsConstants.DCS_DEFAULT_8BIT:
                throw new SmsException("Clickatell API cannot send 8 bit encoded messages without UDH");

            case SmsConstants.DCS_DEFAULT_UCS2:
                ud = SmsPduUtil.bytesToHexString(thePdu.getUserData());
                requestString = MessageFormat.format(
                    "http://api.clickatell.com/http/sendmsg?session_id={0}&to={1}&from={2}&text={3}&unicode=1&concat=0",
                    new Object[] { mySessionId, theDestination.getAddress(), theSender.getAddress(), ud });
                break;

            case SmsConstants.DCS_DEFAULT_7BIT:
                String msg = SmsPduUtil.readSeptets(thePdu.getUserData(), thePdu.getUserDataLength());
                msg = URLEncoder.encode(msg);
                requestString = MessageFormat.format(
                    "http://api.clickatell.com/http/sendmsg?session_id={0}&to={1}&from={2}&text={3}&concat=0",
                    new Object[] { mySessionId, theDestination.getAddress(), theSender.getAddress(), msg });
                break;

            default:
                throw new SmsException("Unsupported data coding scheme");
            }
        }
        else
        {
            //
            // Message Contains UDH
            //
            switch (theDcs)
            {
            case SmsConstants.DCS_DEFAULT_8BIT:
                ud = SmsPduUtil.bytesToHexString(thePdu.getUserData());
                udhData = thePdu.getUserDataHeaders();
                // Add length of udh
                udh = SmsPduUtil.bytesToHexString(new byte[] {(byte) (udhData.length & 0xff)});
                udh += SmsPduUtil.bytesToHexString(udhData);
                requestString = MessageFormat.format(
                    "http://api.clickatell.com/http/sendmsg?session_id={0}&to={1}&from={2}&udh={3}&text={4}&concat=0",
                    new Object[] { mySessionId, theDestination.getAddress(), theSender.getAddress(), udh, ud });
                break;

            case SmsConstants.DCS_DEFAULT_UCS2:
                ud = SmsPduUtil.bytesToHexString(thePdu.getUserData());
                udhData = thePdu.getUserDataHeaders();
                // Add length of udh
                udh = SmsPduUtil.bytesToHexString(new byte[] {(byte) (udhData.length & 0xff)});
                udh += SmsPduUtil.bytesToHexString(udhData);
                requestString = MessageFormat.format(
                    "http://api.clickatell.com/http/sendmsg?session_id={0}&to={1}&from={2}&udh={3}&text={4}&unicode=1&concat=0",
                    new Object[] { mySessionId, theDestination.getAddress(), theSender.getAddress(), udh, ud });
                break;

            case SmsConstants.DCS_DEFAULT_7BIT:
                throw new SmsException("Clickatell API cannot send 7 bit encoded messages with UDH");

            default:
                throw new SmsException("Unsupported data coding scheme");
            }
        }

        // CLASS_0 message?
        if (SmsDcsUtil.getMessageClass(theDcs) == SmsConstants.MSG_CLASS_0)
        {
            requestString += "&msg_type=SMS_FLASH";
        }

        //
        // Send request to clickatell
        //
        try
        {
            URL requestURL = new URL(requestString);

            // Connect
            BufferedReader responseReader =
                new BufferedReader(new InputStreamReader(requestURL.openStream()));

            // Read response
            response = responseReader.readLine();
            responseReader.close();

            // Parse response
            Object[] objs = responseFormat.parse(response);

            if ( "ID".equalsIgnoreCase((String)objs[0]) )
            {
                // Could do something with this, we just ignore it.
                String msgId = (String)objs[1];
            }
            else
            {
                // ERR
                String errorMsg = (String) objs[1];
                throw new SmsException("Clickatell error. Error " + errorMsg);
            }
        }
        catch (ParseException ex)
        {
            throw new SmsException("Unexpected response from Clickatell. : " + response);
        }
        catch (MalformedURLException ex)
        {
            throw new SmsException(ex.getMessage());
        }
        catch (IOException ex)
        {
            throw new SmsException(ex.getMessage());
        }
    }

    /**
     * Sends many SMS
     *
     * @todo If all messages are 7bit encoded we should check if the only UDH
     * field is the concatenation UDH. We should then send it as one message
     * to clickatell...
     *
     * @param theMessage
     * @param theDestination
     * @param theSender
     * @throws SmsException
     */
    public void send(SmsMessage theMessage, SmsAddress theDestination, SmsAddress theSender)
        throws SmsException
    {
        SmsPdu msgPdu[] = null;

        if (theDestination.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC)
        {
            throw new SmsException("Cannot sent SMS to an ALPHANUMERIC address");
        }

        msgPdu = theMessage.getPdus();

        for(int i=0; i < msgPdu.length; i++)
        {
            send(msgPdu[i], theMessage.getDataCodingScheme(), theDestination, theSender);
        }
    }

    /**
     * Sends a ping command to clickatell.
     * <p>
     * Currently not implemented.
     * <pre>
     * Command:
     * http://api.clickatell.com/http/ping?session_id=xxx
     * Response:
     * OK:
     * or
     * ERR: Error number
     * </pre>
     *
     * @throws SmsException
     */
    public void ping() throws SmsException
    {
        /**@todo: Implement this org.marre.sms.transport.SmsTransport method*/
        throw new java.lang.UnsupportedOperationException("Method ping() not yet implemented.");
    }

    /**
     * Disconnect from clickatell
     * <p>
     * Not needed for the clickatell API
     *
     * @throws SmsException Never
     */
    public void disconnect() throws SmsException
    {
    }
}
