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
    public UcpTransport()
    {
    }

    public void init(Properties theProps) throws SmsException
    {
    }

    public void connect() throws SmsException
    {
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
        }
    }

    /**
     *
     */
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

    public void disconnect()
        throws SmsException
    {
    }
}

