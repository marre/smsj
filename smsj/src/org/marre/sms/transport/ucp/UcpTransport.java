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
package org.marre.sms.transport.ucp;

import java.util.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import org.marre.util.StringUtil;
import org.marre.sms.util.SmsPduUtil;
import org.marre.sms.util.SmsDcsUtil;
import org.marre.sms.transport.SmsTransport;
import org.marre.sms.SmsPdu;
import org.marre.sms.SmsAddress;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsException;
import org.marre.sms.SmsConstants;

/**
 * An SmsTransport that sends the SMS through an UCP SMSC
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class UcpTransport implements SmsTransport
{
	private Socket s = null;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	private BufferedReader stdIn = null;	
    
    public UcpTransport()
    {
    }

    public void init(Properties theProps) throws SmsException
    {
    	//Normaly we sould get the Data from the Props, but temporaly hardCoded
    	String server = "213.61.220.27";
    	int port = 1500;
    	try {
		    s = new Socket(server, port);
		    out = new DataOutputStream(s.getOutputStream());
		    in = new DataInputStream(s.getInputStream());
		} catch (UnknownHostException e) {
			throw new SmsException("Unknown host");
		} catch (IOException e) {
		    System.err.println
			("SendSMS.send: Cannot open TCP/IP connection to: ".concat
			     (server).concat(":").concat(String.valueOf(port)));
			throw new SmsException("Cannot connect to Server");
		} catch (Exception e) {
		    System.err.println
			("SendSMS.send: Cannot open TCP/IP connection to: ".concat
			     (server).concat(":").concat(String.valueOf(port)));
			throw new SmsException("Cannot connect to Server");
		}
		System.out.println("Connected sucessfully to the target Host");
    }

    public void connect() throws SmsException
    {
    	//Logging into the Remote Host via UCP 60;
		byte [] message = buildLogin("","");
		System.out.println("SMSC response: " + sendUcp(message));
    }

    public void send(SmsMessage theMessage, SmsAddress theDestination, SmsAddress theSender)
        throws SmsException
    {
        SmsPdu msgPdu[] = null;

        if (theDestination.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC)
        {
            throw new SmsException("Cannot sent SMS to ALPHANUMERIC address");
        }

        msgPdu = theMessage.getPdus();
        for(int i=0; i < msgPdu.length; i++)
        {
            byte dcs = theMessage.getDataCodingScheme();
            boolean moreToSend = (i < (msgPdu.length - 1));
            byte[] submitCmd = buildSubmit(dcs, msgPdu[i], moreToSend,
theDestination, theSender);
			System.out.println("SMSC response: " + sendUcp(submitCmd));
        }
    }

    /**
     * Building the Login Stream
     * @author Lorenz Barth
     * @throws SmsException
     * @param userid, pwd
     *
     */
    public byte[] buildLogin(String userid, String pwd) throws SmsException {
		StringUtil stu = new StringUtil();
    	UCPSeries60 ucplogin = new UCPSeries60(UCPSeries60.OP_OPEN_SESSION);
    	ucplogin.setTRN(0x01);
    	ucplogin.setField(ucplogin.FIELD_OAdC,"u0000854");
		ucplogin.setField(ucplogin.FIELD_OTON,"6");
		ucplogin.setField(ucplogin.FIELD_ONPI,"5");
		ucplogin.setField(ucplogin.FIELD_STYP,"1");
		ucplogin.setField(ucplogin.FIELD_VERS,"0100");
		ucplogin.setField(ucplogin.FIELD_PWD,stu.encodeInIRA("cjSBtqk7"));				
    	return ucplogin.getCommand();	
    }
    public byte[] buildSubmit(byte dcs, SmsPdu pdu, boolean moreToSend, SmsAddress dest, SmsAddress sender)
        throws SmsException
    {
        UcpSeries50 ucpSubmit = new UcpSeries50(UcpSeries50.OP_SUBMIT_SHORT_MESSAGE);

        byte[] udh = pdu.getUserDataHeaders();
        boolean isSeptets = (SmsDcsUtil.getAlphabet(dcs) == SmsConstants.ALPHABET_GSM);
        int udBits;

        // FIXME: TRN
        ucpSubmit.setTRN(0x01);

        // OTOA = Originator Type Of Address (1139 = OadC is set to NPI
        // telephone and TON international, 5039 The OAdC contains an
        // alphanumeric address)
        // OAdC = Address code originator
        if (dest.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC)
        {
            String addr = dest.getAddress();
            byte septets[] = SmsPduUtil.getSeptets(addr);
            ucpSubmit.setField(UcpSeries50.FIELD_OTOA, "5039");
            ucpSubmit.setField(UcpSeries50.FIELD_OAdC, StringUtil.bytesToHexString(septets));
        }
        else
        {
            ucpSubmit.setField(UcpSeries50.FIELD_OTOA, "1139");
            ucpSubmit.setField(UcpSeries50.FIELD_OAdC, dest.getAddress());
        }

        // AdC = Address code recipient for the SM
        ucpSubmit.setField(UcpSeries50.FIELD_AdC, sender.getAddress());

        // MT <= 4
        ucpSubmit.setField(UcpSeries50.FIELD_MT, "4");

        // XSer = Extra Services
        ucpSubmit.clearXSer();
        ucpSubmit.addXSer(UcpSeries50.XSER_TYPE_DCS, dcs);
        if (udh != null)
        {
            ucpSubmit.addXSer(UcpSeries50.XSER_TYPE_UDH, udh);
        }

        // NB = Number of bits in TMsg
        udBits = pdu.getUserDataLength() * ((isSeptets) ? 7 : 8);
        ucpSubmit.setField(UcpSeries50.FIELD_NB, StringUtil.intToString(udBits, 4));

        // TMsg = Msg in hex
        ucpSubmit.setField(UcpSeries50.FIELD_Msg, StringUtil.bytesToHexString(pdu.getUserData()));

        // MMS = More messages to send
        if (moreToSend)
        {
            ucpSubmit.setField(UcpSeries50.FIELD_MMS, "1");
        }

        return ucpSubmit.getCommand();
    }

    public void ping()
        throws SmsException
    {
    }

	/**
	 * Closing Socket and Streams
	 * 
	 * @author Lorenz Barth
	 * @throws SmsException
	 * 
	 */
    public void disconnect() throws SmsException {
    	try {
			out.close();
			in.close();
			s.close();
    	}catch (Exception e) {
			e.printStackTrace();
			throw new SmsException(e.getMessage());
		}
    }

    /**
     * This method is sending the Data to over the existing Connection and recives
     * the answer, the Answer is returned as a String
     * @author Lorenz Barth
     * @throws SmsException
     * @param byte[] of Data
     */
    public String sendUcp(byte[] data) throws SmsException {
		if(!s.isConnected() || out!=null || in!=null) {
			throw new SmsException("Please Connect first");
		}
    	System.out.println("SMSC send: " + new String(data,0,data.length));
    	StringBuffer strBuf;
		try {
			out.write(data);
			out.flush();
			byte[] b = new byte[1];
			if ((b[0] = in.readByte()) != 2) {
				System.out.println("SendSMS.send: The SMSC sends a bad reply");
				throw new SmsException("The SMSC sends a bad reply");
			}
			strBuf = new StringBuffer();
			while ((b[0] = in.readByte()) != 3) {
				strBuf.append(new String(b));
			}
		} catch (IOException e) {
			throw new SmsException(e.getMessage());
		} catch (SmsException e) {
			throw new SmsException(e.getMessage());
		}
		// Return the String
    	return strBuf.toString();
    }
}

