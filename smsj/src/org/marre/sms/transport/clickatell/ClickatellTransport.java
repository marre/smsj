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
import org.marre.sms.util.SmsPduUtil;

/**
 * An SmsTransport that sends the SMS with clickatell over HTTP.
 * <p>
 * It is developed to use the "Clickatell HTTP API v. 1.63"
 * <p>
 * Known limitations:<br>
 * - Impossible to set the sending address<br>
 * - Cannot send 8-Bit messages without an UDH<br>
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
     *
     * @param theProps
     * @throws SmsException
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
     * Command:
     * http://api.clickatell.com/http/auth?api_id=xxxx&user=xxxx&password=xxxx
     *
     * Response:
     * OK: session id
     * or
     * ERR: Error number
     *
     * @throws SmsException
     */
    public void connect() throws SmsException
    {
        String requestString = MessageFormat.format(
                "http://api.clickatell.com/http/auth?api_id={0}&user={1}&password={2}",
                new Object[] { myApiId, myUsername, myPassword });

        MessageFormat responseFormat = new MessageFormat("{0}: {1}");

        try
        {
            URL requestURL = new URL(requestString);

            // Connect
            BufferedReader responseReader =
                new BufferedReader(new InputStreamReader(requestURL.openStream()));

            // Read response
            String response = responseReader.readLine();

            // Parse response
            Object[] objs = responseFormat.parse(response);

            if ( "OK".equalsIgnoreCase((String)objs[0]) )
            {
                // Store session id
                mySessionId = (String)objs[1];
            }
            else if ( "ERR".equalsIgnoreCase((String)objs[0]) )
            {
                String clickatellError = (String) objs[1];
                throw new SmsException("Clickatell error. Error " + clickatellError);
            }
            else
            {
                throw new SmsException("Unexpected answer from Clickatell. : " + response);
            }
        }
        catch (ParseException ex)
        {
            throw new SmsException(ex.getMessage());
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
     * Command:
     * http://api.clickatell.com/http/sendmsg?session_id=xxxx&
     *
     * Response Single Message:
     * ID: apiMsgId
     * Response Multiple Messages:
     * ID: apiMsgId To: xxxxxx
     * ID: apiMsgId To: xxxxxx
     * or
     * ERR: Error number
     *
     * @param thePdu
     * @param theDestination
     * @param theSender
     * @throws SmsException
     */
    public void send(SmsPdu thePdu, SmsAddress theDestination, SmsAddress theSender) throws SmsException
    {
        MessageFormat responseFormat = new MessageFormat("{0}: {1}");
        String requestString;
        String ud;
        String udh;

        if (theDestination.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC)
        {
            throw new SmsException("Cannot sent SMS to an ALPHANUMERIC address");
        }

        if (mySessionId == null)
        {
            throw new SmsException("Must connect before sending");
        }

        if (thePdu.getUserDataHeaders() == null)
        {
            switch (thePdu.getDataCodingScheme())
            {
            case SmsConstants.DCS_DEFAULT_8BIT:
                throw new SmsException("Clickatell API cannot send 8 bit encoded messages without UDH");

            case SmsConstants.DCS_DEFAULT_UCS2:
                ud = SmsPduUtil.bytesToHexString(thePdu.getUserData());
                requestString = MessageFormat.format(
                    "http://api.clickatell.com/http/sendmsg?session_id={0}&to={1}&from={2}&text={3}&unicode=1",
                    new Object[] { mySessionId, theDestination.getAddress(), theSender.getAddress(), ud });
                break;

            case SmsConstants.DCS_DEFAULT_7BIT:
                String msg = SmsPduUtil.readSeptets(thePdu.getUserData(), thePdu.getUserDataLength());
                msg = URLEncoder.encode(msg);
                requestString = MessageFormat.format(
                    "http://api.clickatell.com/http/sendmsg?session_id={0}&to={1}&from={2}&text={3}",
                    new Object[] { mySessionId, theDestination.getAddress(), theSender.getAddress(), msg });
                break;

            default:
                throw new SmsException("Unsupported data coding scheme");
            }
        }
        else
        {
            switch (thePdu.getDataCodingScheme())
            {
            case SmsConstants.DCS_DEFAULT_8BIT:
                ud = SmsPduUtil.bytesToHexString(thePdu.getUserData());
                udh = SmsPduUtil.bytesToHexString(thePdu.getUserDataHeaders());
                requestString = MessageFormat.format(
                    "http://api.clickatell.com/http/sendmsg?session_id={0}&to={1}&from={2}&udh={3}&text={4}",
                    new Object[] { mySessionId, theDestination.getAddress(), theSender.getAddress(), udh, ud });
                break;

            case SmsConstants.DCS_DEFAULT_UCS2:
                ud = SmsPduUtil.bytesToHexString(thePdu.getUserData());
                udh = SmsPduUtil.bytesToHexString(thePdu.getUserDataHeaders());
                requestString = MessageFormat.format(
                    "http://api.clickatell.com/http/sendmsg?session_id={0}&to={1}&from={2}&udh={3}&text={4}&unicode=1",
                    new Object[] { mySessionId, theDestination.getAddress(), theSender.getAddress(), udh, ud });
                break;

            case SmsConstants.DCS_DEFAULT_7BIT:
                String msg = SmsPduUtil.readSeptets(thePdu.getUserData(), thePdu.getUserDataLength());
                msg = URLEncoder.encode(msg);
                udh = SmsPduUtil.bytesToHexString(thePdu.getUserDataHeaders());
                requestString = MessageFormat.format(
                    "http://api.clickatell.com/http/sendmsg?session_id={0}&to={1}&from={2}&udh={3}&text={4}&msg_type=SMS_TEXT",
                    new Object[] { mySessionId, theDestination.getAddress(), theSender.getAddress(), udh, msg });
                break;

            default:
                throw new SmsException("Unsupported data coding scheme");
            }
        }

        try
        {
            URL requestURL = new URL(requestString);

            // Connect
            BufferedReader responseReader =
                new BufferedReader(new InputStreamReader(requestURL.openStream()));

            // Read response
            String response;
            while ( (response = responseReader.readLine()) != null)
            {
                System.out.println(response);
            }
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

    public void send(SmsPdu[] thePdus, SmsAddress theDestination, SmsAddress theSender) throws SmsException
    {
        if (theDestination.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC)
        {
            throw new SmsException("Cannot sent SMS to an ALPHANUMERIC address");
        }

        for(int i=0; i < thePdus.length; i++)
        {
            send(thePdus[i], theDestination, theSender);
        }
    }

    /**
     * Command:
     * http://api.clickatell.com/http/ping?session_id=xxx
     * Response:
     * OK:
     * or
     * ERR: Error number
     *
     * @throws SmsException
     */
    public void ping() throws SmsException
    {
        /**@todo: Implement this org.marre.sms.transport.SmsTransport method*/
        throw new java.lang.UnsupportedOperationException("Method ping() not yet implemented.");
    }

    public void disconnect() throws SmsException
    {
    }
}