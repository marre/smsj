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
package org.marre.sms.util;

import org.marre.sms.SmsConstants;

/**
 * Various functions to decode and encode the DCS byte (Data Coding Scheme)
 *
 * @author Markus Eriksson
 */
public class SmsDcsUtil
{
    /**
     * Get the message class.
     *
     * <pre>
     * Return value can be one of:
     * - SmsConstants.MSG_CLASS_UNKNOWN
     * - SmsConstants.MSG_CLASS_0
     * - SmsConstants.MSG_CLASS_1
     * - SmsConstants.MSG_CLASS_2
     * - SmsConstants.MSG_CLASS_3
     * </pre>
     *
     * @return Returns the message class. See SmsConstants for valid message classes.
     */
    public static int getMessageClass(byte theDcs)
    {
        if (theDcs == 0x00)
        {
            return SmsConstants.MSG_CLASS_UNKNOWN;
        }

        // General Data Coding Indication
        if ( (theDcs & 0xC0) == 0)
        {
            // No message class meaning
            if ( (theDcs & 0x10) == 0 )
            {
                return SmsConstants.MSG_CLASS_UNKNOWN;
            }

            switch (theDcs & 0x03)
            {
            case 0x00: return SmsConstants.MSG_CLASS_0;
            case 0x01: return SmsConstants.MSG_CLASS_1;
            case 0x02: return SmsConstants.MSG_CLASS_2;
            case 0x03: return SmsConstants.MSG_CLASS_3;
            }
        }

        // Data coding/message class
        if ( (theDcs & 0xF0) == 0xF0)
        {
            switch (theDcs & 0x03)
            {
            case 0x00: return SmsConstants.MSG_CLASS_0;
            case 0x01: return SmsConstants.MSG_CLASS_1;
            case 0x02: return SmsConstants.MSG_CLASS_2;
            case 0x03: return SmsConstants.MSG_CLASS_3;
            }
        }

        return SmsConstants.MSG_CLASS_UNKNOWN;
    }

    /**
     * Get the alphabet
     *
     * <pre>
     * Return value can be one of:
     * - SmsConstants.ALPHABET_GSM
     * - SmsConstants.ALPHABET_8BIT
     * - SmsConstants.ALPHABET_UCS2
     * - SmsConstants.ALPHABET_RESERVED
     * - SmsConstants.ALPHABET_UNKNOWN
     * </pre>
     *
     * @return Returns the alphabet. See SmsConstants for valid alphabets.
     */
    public static int getAlphabet(byte theDcs)
    {
        if (theDcs == 0x00)
        {
            return SmsConstants.ALPHABET_GSM;
        }

        // General Data Coding Indication
        if ( (theDcs & 0xC0) == 0)
        {
            switch (theDcs & 0x0C)
            {
            case 0x00: return SmsConstants.ALPHABET_GSM;
            case 0x04: return SmsConstants.ALPHABET_8BIT;
            case 0x08: return SmsConstants.ALPHABET_UCS2;
            case 0x0C: return SmsConstants.ALPHABET_RESERVED;
            }
        }

        // Data coding/message class
        if ( (theDcs & 0xF0) == 0xF0)
        {
            switch (theDcs & 0x02)
            {
            case 0x00: return SmsConstants.ALPHABET_GSM;
            case 0x02: return SmsConstants.ALPHABET_8BIT;
            }
        }

        return SmsConstants.ALPHABET_UNKNOWN;
    }

    /**
     * Is the message compressed using the GSM standard compression algorithm?
     * <p>
     * See GSM TS 03.42 for further information
     *
     * @return true if compressed, false otherwise
     */
    public static boolean isCompressed(byte theDcs)
    {
        // General Data Coding Indication, Compressed
        return ((theDcs & 0xE0) == 0x20);
    }

    private SmsDcsUtil()
    {
    }
}
