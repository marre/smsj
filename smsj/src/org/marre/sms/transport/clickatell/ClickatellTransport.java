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
package org.marre.sms.transport.clickatell;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.marre.sms.SmsAddress;
import org.marre.sms.SmsConcatMessage;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsDcsUtil;
import org.marre.sms.SmsException;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsPdu;
import org.marre.sms.SmsPduUtil;
import org.marre.sms.SmsUdhElement;
import org.marre.sms.SmsUdhUtil;
import org.marre.sms.SmsUserData;
import org.marre.sms.transport.SmsTransport;
import org.marre.util.StringUtil;

/**
 * An SmsTransport that sends the SMS with clickatell over HTTP.
 * <p>
 * It is developed to use the "Clickatell HTTP API v. 1.63".
 * <p>
 * Known limitations: <br>- Impossible to set the sending address (might work
 * with some networks) <br>- Cannot send 8-Bit messages without an UDH <br>-
 * Doesn't support a complete DCS. Only UCS2, 7bit, 8bit and SMS class 0 or 1.
 * <br>- Cannot set validity period (not done yet) <br>- Doesn't acknowledge
 * the TON or NPI, everything is sent as NPI_ISDN_TELEPHONE and
 * TON_INTERNATIONAL. <br>
 * <p>
 * Support matrix: <table border="1">
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
 * <td>UDH and <br>
 * concatenation</td>
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
    private static Log logger = LogFactory.getLog(ClickatellTransport.class);

    private String myUsername;
    private String myPassword;
    private String myApiId;
    private String mySessionId;

    /** Required feature "Text". Set by default. */
    public static final int FEAT_TEXT = 0x0001;
    /** Required feature "8-bit messaging". Set by default. */
    public static final int FEAT_8BIT = 0x0002;
    /** Required feature "udh (binary)". Set by default. */
    public static final int FEAT_UDH  = 0x0004;
    /** Required feature "ucs2/unicode". Set by default. */
    public static final int FEAT_UCS2 = 0x0008;
    /** Required feature "alpha originator (sender id)". */
    public static final int FEAT_ALPHA = 0x0010;
    /** Required feature "numeric originator (sender id)". */
    public static final int FEAT_NUMBER = 0x0020;
    /** Required feature "reply to an mt message with a numeric sender id". */
    public static final int FEAT_REPLY = 0x0040;
    /** Required feature "Flash messaging". */
    public static final int FEAT_FLASH = 0x0200;
    /** Required feature "Delivery acknowledgements". */
    public static final int FEAT_DELIVACK = 0x2000;
    /** Required feature "Concatenation". Set by default. */
    public static final int FEAT_CONCAT = 0x4000;
    /** The default required features as explained in HTTP API v224. */
    public static final int FEAT_DEFAULT = 0x400F;
    
    /**
     * Sends a request to clickatell.
     * 
     * @param theRequest
     *            Request URL to send
     * @return An array of responses (sessionid or msgid)
     * @throws ClickatellException
     */
    private String[] sendRequest(String theRequest) throws ClickatellException
    {
        String response = null;
        MessageFormat responseFormat = new MessageFormat("{0}: {1}");

        List idList = new LinkedList();

        //
        // Send request to clickatell
        //
        try
        {
            logger.info("sendRequest: Sending : " + theRequest);
            URL requestURL = new URL(theRequest);

            // Connect
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(requestURL.openStream()));

            // Read response
            while ((response = responseReader.readLine()) != null)
            {
                // Parse response
                Object[] objs = responseFormat.parse(response);
                if ("ERR".equalsIgnoreCase((String) objs[0]))
                {
                    MessageFormat errorFormat = new MessageFormat("{0}: {1}, {2}");
                    Object[] errObjs = errorFormat.parse(response);
                    
                    // Error message...
                    String errorNo = (String) errObjs[1];
                    String description = (String) errObjs[2];
                    throw new ClickatellException("Clickatell error. Error " + errorNo + ", " + description, 
                                                  Integer.parseInt(errorNo));
                }
                else
                {
                    logger.info("sendRequest: Got ID : " + ((String) objs[1]));
                    idList.add((String) objs[1]);
                }
            }
            responseReader.close();
        }
        catch (ParseException ex)
        {
            throw new ClickatellException("Unexpected response from Clickatell. : " + response,
                    ClickatellException.ERROR_UNKNOWN);
        }
        catch (MalformedURLException ex)
        {
            throw new ClickatellException(ex.getMessage(), ClickatellException.ERROR_UNKNOWN);
        }
        catch (IOException ex)
        {
            throw new ClickatellException(ex.getMessage(), ClickatellException.ERROR_UNKNOWN);
        }

        return (String[]) idList.toArray(new String[0]);
    }

    private String[] sendRequestWithRetry(String requestString)
        throws SmsException
    {
        String[] msgIds;
        
        // Send request to clickatell
        try
        {
            msgIds = sendRequest(requestString);
        }
        catch (ClickatellException ex)
        {
            switch (ex.getErrId())
            {
            // 858141 : Clickatell is not sending "Session id expired"
            // they are using "Authentication failed" instead
            case ClickatellException.ERROR_AUTH_FAILED:
            case ClickatellException.ERROR_SESSION_ID_EXPIRED:
                // Try to get a new session id
                connect();
    
                // Retry the request...
                // OK, this is a bit ugly...
                try
                {
                    msgIds = sendRequest(requestString);
                }
                catch (ClickatellException ex2)
                {
                    throw new SmsException(ex2.getMessage());
                }
                break;
    
            case ClickatellException.ERROR_UNKNOWN:
            default:
                throw new SmsException(ex.getMessage());
            }
        }
        
        return msgIds;
    }
    
    /**
     * Initializes the transport.
     * <p>
     * It expects the following properties in theProps param:
     * 
     * <pre>
     *       smsj.clickatell.username - clickatell username
     *       smsj.clickatell.password - clickatell password
     *       smsj.clickatell.apiid    - clickatell apiid
     * </pre>
     * 
     * @param theProps
     *            Properties to initialize the library
     * @throws SmsException
     *             If not given the needed params
     */
    public void init(Properties theProps) throws SmsException
    {
        myUsername = theProps.getProperty("smsj.clickatell.username");
        myPassword = theProps.getProperty("smsj.clickatell.password");
        myApiId = theProps.getProperty("smsj.clickatell.apiid");

        if ((myUsername == null) || (myPassword == null) || (myApiId == null)) 
        { 
            throw new SmsException("Incomplete login information for clickatell"); 
        }
    }

    /**
     * Sends an auth command to clickatell to get an session id that can be used
     * later.
     * 
     * @throws SmsException
     *             If we fail to authenticate to clickatell or if we fail to
     *             connect.
     */
    public void connect() throws SmsException
    {
        String[] response = null;
        String requestString;
        
        requestString  = "http://api.clickatell.com/http/auth?api_id=" + myApiId;
        requestString += "&user=" + myUsername;
        requestString += "&password=" + myPassword;
        
        try
        {
            response = sendRequest(requestString);
        }
        catch (ClickatellException ex)
        {
            throw new SmsException(ex.getMessage());
        }

        mySessionId = response[0];
    }

    /**
     * 
     */
    private String buildSendRequest(SmsUserData ud, byte[] udhData, SmsAddress dest, SmsAddress sender)
        throws SmsException
    {
        String requestString;
        int reqFeat = 0;
        
        requestString  = "http://api.clickatell.com/http/sendmsg?session_id=" + mySessionId;
        requestString += "&to=" + dest.getAddress();

        if (SmsUdhUtil.isConcat(ud, udhData))
        {
            requestString += "&concat=3";
            reqFeat |= FEAT_CONCAT;
        }
        
        if (sender != null)
        {
            requestString += "&from=" + sender.getAddress();
            reqFeat |= (sender.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC) ? FEAT_ALPHA : FEAT_NUMBER;  
        }
        
        // CLASS_0 message?
        if (SmsDcsUtil.getMessageClass(ud.getDcs()) == SmsConstants.MSG_CLASS_0)
        {
            requestString += "&msg_type=SMS_FLASH";
            reqFeat |= FEAT_FLASH;
        }
        
        //
        // Generate request URL
        //
        if ( (udhData == null) || (udhData.length == 0) )
        {                        
            //
            // Message without UDH
            //
            switch (SmsDcsUtil.getAlphabet(ud.getDcs()))
            {
            case SmsConstants.ALPHABET_8BIT:
                throw new SmsException("Clickatell API cannot send 8 bit encoded messages without UDH");

            case SmsConstants.ALPHABET_UCS2:
                String udStr = StringUtil.bytesToHexString(ud.getData());
                requestString += "&unicode=1";
                requestString += "&text=" + udStr;
                reqFeat |= FEAT_UCS2;
                break;

            case SmsConstants.ALPHABET_GSM:
                String msg = SmsPduUtil.readSeptets(ud.getData(), ud.getLength());            
                requestString += "&text=" + URLEncoder.encode(msg);
                reqFeat |= FEAT_TEXT;
                break;

            default:
                throw new SmsException("Unsupported data coding scheme");
            }
        }
        else
        {
            String udStr;
            String udhStr;
            
            //
            // Message Contains UDH
            //
            switch (SmsDcsUtil.getAlphabet(ud.getDcs()))
            {
            case SmsConstants.ALPHABET_8BIT:
                udStr = StringUtil.bytesToHexString(ud.getData());
                udhStr = StringUtil.bytesToHexString(udhData);                
                requestString += "&udh=" + udhStr;
                requestString += "&text=" + udStr;                
                reqFeat |= FEAT_UDH | FEAT_8BIT;
                break;

            case SmsConstants.ALPHABET_UCS2:
                udStr = StringUtil.bytesToHexString(ud.getData());
                udhStr = StringUtil.bytesToHexString(udhData);
                requestString += "&unicode=1";
                requestString += "&udh=" + udhStr;
                requestString += "&text=" + udStr;
                reqFeat |= FEAT_UDH | FEAT_UCS2;
                break;

            case SmsConstants.ALPHABET_GSM:
                throw new SmsException("Clickatell API cannot send 7 bit encoded messages with UDH");

            default:
                throw new SmsException("Unsupported data coding scheme");
            }
        }

        // Add the req_feat parameter
        requestString += "&req_feat=" + reqFeat;
        
        return requestString;
    }
    
    /**
     * More effective sending of SMS.
     * 
     * @param theMsg
     * @param theDestination
     * @param theSender
     * @throws SmsException
     */
    private String[] sendConcatMessage(SmsConcatMessage theMsg, SmsAddress theDestination, SmsAddress theSender)
        throws SmsException
    {
        SmsUserData userData = theMsg.getUserData();
        SmsUdhElement[] udhElements = theMsg.getUdhElements();
        byte[] udhData = SmsUdhUtil.toByteArray(udhElements);

        String requestString = buildSendRequest(userData, udhData, theDestination, theSender);
        return sendRequestWithRetry(requestString);
    }

    /**
     * Sends an sendmsg command to clickatell.
     * 
     * @param thePdu
     * @param theDcs
     * @param theDestination
     * @param theSender
     * @throws SmsException
     *             If clickatell sends an error message, unexpected response or
     *             if we fail to connect.
     */
    private String send(SmsPdu thePdu, SmsAddress theDestination, SmsAddress theSender) throws SmsException
    {
        SmsUserData userData = thePdu.getUserData();
        byte[] udhData = thePdu.getUserDataHeaders();
        
        String requestString = buildSendRequest(userData, udhData, theDestination, theSender);

        return sendRequestWithRetry(requestString)[0];
    }

    /**
     * Sends an SMS Message.
     * 
     * @param theMessage
     * @param theDestination
     * @param theSender
     * @throws SmsException
     * @return Message ids
     */
    public String[] send(SmsMessage theMessage, SmsAddress theDestination, SmsAddress theSender) throws SmsException
    {
        String[] msgIds;
        
        if (theDestination.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC) 
        { 
            throw new SmsException("Cannot sent SMS to an ALPHANUMERIC address"); 
        }

        if (mySessionId == null) 
        { 
            throw new SmsException("Must connect before sending"); 
        }
        
        
        if (theMessage instanceof SmsConcatMessage)
        {
            msgIds = sendConcatMessage((SmsConcatMessage) theMessage, theDestination, theSender);
        }
        else
        {
            SmsPdu[] msgPdu = theMessage.getPdus();
            msgIds = new String[msgPdu.length];

            for (int i = 0; i < msgPdu.length; i++)
            {
                msgIds[i] = send(msgPdu[i], theDestination, theSender);
            }
        }
        
        return msgIds;
    }

    /**
     * Disconnect from clickatell.
     * <p>
     * Not needed for the clickatell API
     * 
     * @throws SmsException
     *             Never
     */
    public void disconnect() throws SmsException
    {
        // The clickatell HTTP API is connection less
    }
}
