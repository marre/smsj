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
package org.marre.sms.transport.gsm;

import java.util.*;
import java.io.*;

import org.marre.sms.util.SmsPduUtil;
import org.marre.sms.transport.SmsTransport;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsAddress;
import org.marre.sms.SmsException;
import org.marre.sms.SmsConstants;

public class GsmTransport implements SmsTransport
{
    public void init(Properties theProps)
        throws SmsException
    {
    }

    public void login(String theUsername, String thePassword)
        throws SmsException
    {
        // Connect serial port
    }

    public void sendMessage(SmsMessage theMsg, SmsAddress theDestination, SmsAddress theSender)
        throws SmsException
    {
        byte ud[] = theMsg.getUserData();
        int udLength = theMsg.getUserDataLength();
        byte udh[] = theMsg.getUserDataHeader();

        ByteArrayOutputStream baos = new ByteArrayOutputStream(160);

        try
        {
            // Use default SMSC
            baos.write(0x00);

            if( (udh == null) || (udh.length == 0) )
            {
                // TP-Message-Type-Indicator = SUBMIT
                // TP-Reject-Duplicates = ON
                // TP-Validity-Period-Format = No field
                // TP-Status-Report-Request = No
                // TP-User-Data-Header = No
                // TP-Reply-Path = No
                baos.write(0x01);
            }
            else
            {
                // TP-Message-Type-Indicator = SUBMIT
                // TP-Reject-Duplicates = ON
                // TP-Validity-Period-Format = No field
                // TP-Status-Report-Request = No
                // TP-User-Data-Header = Yes
                // TP-Reply-Path = No
                baos.write(0x41);
            }

            // TP-Message-Reference
            // Leave to 0x00, MS will set it
            baos.write(0x00);

            // 2-12 octets
            // TP-DA
            // - 1:st octet - length of address (4 bits)
            // - 2:nd octet
            //   - bit 7 - always 1
            //   - bit 4-6 - TON
            //   - bit 0-3 - NPI
            // - n octets - BCD
            // 1010 - "*"
            // 1011 - "#"
            // 1100 - "a"
            // 1101 - "b"
            // 1110 - "c"
            writeDestinationAddress(baos, theDestination);

            // 1 octet
            // TP-PID
            // Probably 0x00
            baos.write(0x00);

            // 1 Integer
            // TP-DCS
            // UCS, septets, language, SMS class...
            baos.write(0x00);

            // 1 octet/ 7 octets
            // TP-VP - Optional
            // Probably not needed

            if( (udh == null) || (udh.length == 0) )
            {
                // 1 Integer
                // TP-UDL
                // UDL includes the length of UDH
                baos.write(udLength);

                // n octets
                // TP-UD
                baos.write(ud);
            }
            else
            {
                // FIXME user data header length when encoded with 7 bit...

                // 1 Integer
                // TP-UDL
                // UDL includes the length of UDH
                baos.write(udLength + udh.length);

                // User Data header length
                baos.write(udh.length);

                // n octets
                // TP-UDH
                baos.write(udh);

                // n octets
                // TP-UD
                baos.write(ud);
            }
            baos.close();
        }
        catch (IOException ex)
        {
            throw new SmsException(ex.getMessage());
        }
        System.out.println("PDU : " + SmsPduUtil.bytesToHexString(baos.toByteArray()));
        System.out.println("Length : " + baos.size());
    }

    public void ping()
        throws SmsException
    {
        // PONG
    }

    public void logout()
        throws SmsException
    {
        // disconnect
    }

    private void writeDestinationAddress(OutputStream theOs, SmsAddress theDestination)
        throws SmsException
    {
        String address = theDestination.getAddress();
        int ton = theDestination.getTypeOfNumber();
        int npi = theDestination.getNumberingPlanIdentification();

        try
        {
            if (ton == SmsConstants.TON_ALPHANUMERIC)
            {
                throw new SmsException("Cannot sent SMS to an ALPHANUMERIC address");
            }
            else
            {
                // trim leading + from address
                if (address.charAt(0) == '+')
                {
                    address = address.substring(1);
                }

                // Length in semi octets
                theOs.write(address.length());

                // Type Of Address
                theOs.write(0x80 | ton << 4 | npi);

                // BCD encode
                SmsPduUtil.writeBcdNumber(theOs, address);
            }
        }
        catch (IOException ex)
        {
            throw new SmsException(ex.getMessage());
        }
    }
}
