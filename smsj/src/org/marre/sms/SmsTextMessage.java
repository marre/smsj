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
import java.util.Random;

import org.marre.sms.util.SmsPduUtil;
import org.marre.sms.util.SmsUdhUtil;

/**
 * SmsTextMessage
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public class SmsTextMessage extends SmsConcatMessage
{
    public SmsTextMessage(String theMsg, int theAlphabet, byte theMessageClass)
    {
        this(theMsg, theAlphabet);
        int dcs = getDataCodingScheme() | 0x10 | theMessageClass;
        setDataCodingScheme((byte)(dcs & 0xff));
    }
 
    public SmsTextMessage(String theMsg, int theAlphabet)
    {
        try
        {
            switch (theAlphabet)
            {
            case SmsConstants.ALPHABET_GSM:
                // 7-bit encoding, No message class, No compression
                setDataCodingScheme(SmsConstants.DCS_DEFAULT_7BIT);
                setContent(null, SmsPduUtil.getSeptets(theMsg), theMsg.length());
                break;
            case SmsConstants.ALPHABET_8BIT:
                // 8bit data encoding, No message class, No compression
                setDataCodingScheme(SmsConstants.DCS_DEFAULT_8BIT);
                setContent(null, theMsg.getBytes("ISO-8859-1"), theMsg.length());
                break;
            case SmsConstants.ALPHABET_UCS2:
                // 16 bit UCS2 encoding, No message class, No compression
                setDataCodingScheme(SmsConstants.DCS_DEFAULT_UCS2);
                setContent(null, theMsg.getBytes("UTF-16BE"), theMsg.length() * 2);
                break;
            }
        }
        catch (UnsupportedEncodingException ex)
        {
            // Shouldn't happen. According to the javadoc documentation
            // for JDK 1.3.1 the "UTF-16BE" and "ISO-8859-1" encoding
            // are standard...
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Creates an SmsTextMessage with default 7Bit GSM Alphabet
     *
     * @param theMsg The message
     */
    public SmsTextMessage(String theMsg)
    {
        this(theMsg, SmsConstants.ALPHABET_GSM);
    }
}
