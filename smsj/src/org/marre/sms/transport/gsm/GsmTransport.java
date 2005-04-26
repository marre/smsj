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
 *   Boris von Loesch
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
package org.marre.sms.transport.gsm;

import java.io.IOException;
import java.util.Properties;

import javax.comm.PortInUseException;

import org.marre.sms.SmsAddress;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsException;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsPdu;
import org.marre.sms.transport.SmsTransport;
import org.marre.util.StringUtil;

/**
 * An SmsTransport that sends the SMS from an GSM phone that is attached
 * to the serial port.
 * <p>
 * This transport has the following settable parameters:
 * <br>
 * <pre>
 * <b>sms.gsm.serialport</b> - Serial port where the GSM phone is located. Ex: "COM1"
 * <b>sms.gsm.bitrate</b> - Bits per second
 * <b>sms.gsm.bit</b> - Databits
 * <b>sms.gsm.parity</b> - Parity (NONE, EVEN, ODD, MARK, SPACE)
 * <b>sms.gsm.stopbits</b> - Stopbits (1, 1.5, 2)
 * <b>sms.gsm.flowcontrol</b> - FlowControl (XONXOFF, RTSCTS, NONE)
 * <b>
 * </pre>
 * <p>
 * <i>This transport cannot set the sending "address" to anything else
 * than the sending phone's phonenumber.</i>
 *
 * @todo Validity period
 *
 * @author Markus Eriksson, Boris von Loesch
 * @version $Id$
 */
public class GsmTransport implements SmsTransport
{
    private static final int RESPONSE_OK = 1;
    private static final int RESPONSE_ERROR = 2;
    private static final int RESPONSE_EMPTY_LINE = 4;
    private static final int RESPONSE_TEXT = 8;
    private static final int RESPONSE_CONTINUE = 16;
    
    private SerialComm mySerialComm = null;

    public GsmTransport()
    {        
    }

    /**
     * Initializes this transport.
     * 
     * @param theProps 
     */
    public void init(Properties theProps) throws SmsException
    {
        String portName = theProps.getProperty("sms.gsm.serialport", "COM1");
        
        mySerialComm = new SerialComm(portName);

        mySerialComm.setBitRate(theProps.getProperty("sms.gsm.bitrate", "19200"));
        mySerialComm.setDataBits(theProps.getProperty("sms.gsm.bit", "8"));
        mySerialComm.setStopBits(theProps.getProperty("sms.gsm.stopbits", "8"));
        mySerialComm.setParity(theProps.getProperty("sms.gsm.parity", "NONE"));
        mySerialComm.setFlowControl(theProps.getProperty("sms.gsm.flowcontrol", "NONE"));
    }
    
    /**
     * Initializes the communication with the GSM phone.
     * 
     * @throws SmsException Thrown when the transport fails to communicate
     * with the GSM phone
     */
    public void connect() 
        throws SmsException, IOException
    {
        try
        {
            mySerialComm.open();
            
            // Can I send sms via the gsm phone?
            mySerialComm.send("AT+CSMS=0");
            if (readResponse(RESPONSE_OK) != RESPONSE_OK)
            {
                throw new SmsException("AT+CSMS=0 failed");
            }
            
            // Init
            mySerialComm.send("AT+CMGF=0");
            if (readResponse(RESPONSE_OK) != RESPONSE_OK)
            {
                throw new SmsException("AT+CMGF=0 failed");            
            }
        }
        catch (PortInUseException e)
        {
            throw new SmsException(e);
        }
    }

    /**
     * Sends the SMS message to the given recipients.
     * 
     * Note: The sending address is ignored for the GSM transport.
     *
     * @param theMessage The message to send
     * @param theDestination The reciever
     * @param theSender The sending address, ignored
     * @throws SmsException Thrown if we fail to send the SMS
     */
    public String[] send(SmsMessage theMessage, SmsAddress theDestination, SmsAddress theSender) throws SmsException, IOException
    {
        if (theDestination.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC)
        {
            throw new SmsException("Cannot sent SMS to an ALPHANUMERIC address");
        }

        SmsPdu[] msgPdu = theMessage.getPdus();
        for(int i=0; i < msgPdu.length; i++)
        {
            byte[] data = GsmEncoder.encodePdu(msgPdu[i], theDestination, theSender); 
            sendSms(data);
        }
        
        // TODO: Return a real message id
        return new String[msgPdu.length];
    }

    private void sendSms(byte[] theBuff)
        throws SmsException, IOException
    {
        String response;

        mySerialComm.send("AT+CMGS=" + (theBuff.length - 1));
        if (readResponse(RESPONSE_CONTINUE) != RESPONSE_CONTINUE)
        {
            throw new SmsException("AT+CMGS=length failed");            
        }

        mySerialComm.send(StringUtil.bytesToHexString(theBuff) + "\032");
        if (readResponse(RESPONSE_OK) != RESPONSE_OK)
        {
            throw new SmsException("AT+CMGS data failed");            
        }
    }

    /**
     * Sends a "AT" command to keep the connection alive.
     *
     * @throws SmsException
     */
    public void ping()
        throws SmsException, IOException
    {
        mySerialComm.send("AT");
    }

    /**
     * Closes the serial connection to the phone.
     * 
     * @throws SmsException
     */
    public void disconnect()
        throws SmsException, IOException
    {
        mySerialComm.close();
    }

    private int readResponse(int endOnMask)
        throws IOException
    {
        int status = 0;
        
        // Always stop on error
        endOnMask |= RESPONSE_ERROR;
        
        while (true)
        {
            String response = mySerialComm.readLine();
        
            status = analyseResponse(response);
            if ((status & endOnMask) != 0)
            {
                break;
            }
        }
        
        return status;
    }
    
    private int analyseResponse(String response)
    {
        if (response.startsWith("OK"))
        {
            return RESPONSE_OK;
        }
        else if (response.startsWith("ERROR"))
        {
            return RESPONSE_ERROR;
        }
        else if (response.equals(">"))
        {
            return RESPONSE_CONTINUE;
        } 
        else if (response.length() == 0)
        {
            return RESPONSE_EMPTY_LINE;
        }
        
        return RESPONSE_TEXT; 
    }
}
