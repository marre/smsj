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

import java.io.*;
import org.marre.sms.util.SmsPduUtil;

public class SmsTextMessage extends SmsMessage
{
    public static final int TEXT_ALPHABET_GSM = 0;
    public static final int TEXT_ALPHABET_UCS2 = 1;

    public SmsTextMessage(String theMsg, int theAlphabet)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(140);
            int userDataLength = 0;

            switch (theAlphabet)
            {
            case TEXT_ALPHABET_GSM:
                SmsPduUtil.writeSeptets(baos, theMsg);
                userDataLength = theMsg.length();
                // 7-bit encoding, No message class, No compression
                setDataCodingScheme((byte)0x00);
                break;
            case TEXT_ALPHABET_UCS2:
                baos.write(theMsg.getBytes("UTF-16BE"));
                userDataLength = theMsg.length() * 2;
                // 16 bit UCS2 encoding, No message class, No compression
                setDataCodingScheme((byte)0x08);
                break;
            }

            baos.close();
            setUserData(myUd = baos.toByteArray(), userDataLength);
        }
        catch (UnsupportedEncodingException ex)
        {
            // Shouldn't happen. According to the javadoc documentation
            // for JDK 1.3.1 the "UTF-16BE" is standard...
            throw new RuntimeException(ex.getMessage());
        }
        catch (IOException ex)
        {
            // Shouldnt really happen. We were writing to an internal
            // stream.
            throw new RuntimeException(ex.getMessage());
        }
    }

    public SmsTextMessage(String theMsg)
    {
        this(theMsg, TEXT_ALPHABET_GSM);
    }
}
