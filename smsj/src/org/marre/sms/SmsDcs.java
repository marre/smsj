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

/**
 * Represents SMS Data Coding Scheme
 *
 * <b>Note:</b> It doesn't correctly handle Message Waiting indication
 * messages yet.
 *
 * @author Markus Eriksson
 */
public class SmsDcs
{
    public static final SmsDcs DEFAULT_7BIT_DCS = new SmsDcs((byte)0x00);
    public static final SmsDcs DEFAULT_8BIT_DCS = new SmsDcs((byte)0x04);
    public static final SmsDcs DEFAULT_UCS2_DCS = new SmsDcs((byte)0x08);

    private byte myDcs;

    public SmsDcs(byte theDcs)
    {
        myDcs = theDcs;
    }

    public byte getDcs()
    {
        return myDcs;
    }

    /**
     * Get the message class
     *
     * @return Returns the message class. See SmsConstants for valid message classes.
     */
    public int getMessageClass()
    {
        if (myDcs == 0x00)
        {
            return SmsConstants.MSG_CLASS_UNKNOWN;
        }

        // General Data Coding Indication
        if ( (myDcs & 0xC0) == 0)
        {
            // No message class meaning
            if ( (myDcs & 0x08) == 0 )
            {
                return SmsConstants.MSG_CLASS_UNKNOWN;
            }

            switch (myDcs & 0x03)
            {
            case 0x00: return SmsConstants.MSG_CLASS_0;
            case 0x01: return SmsConstants.MSG_CLASS_1;
            case 0x02: return SmsConstants.MSG_CLASS_2;
            case 0x03: return SmsConstants.MSG_CLASS_3;
            }
        }

        // Data coding/message class
        if ( (myDcs & 0xF0) == 0xF0)
        {
            switch (myDcs & 0x03)
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
     * @return Returns the alphabet. See SmsConstants for valid alphabets.
     */
    public int getAlphabet()
    {
        if (myDcs == 0x00)
        {
            return SmsConstants.ALPHABET_GSM;
        }

        // General Data Coding Indication
        if ( (myDcs & 0xC0) == 0)
        {
            switch (myDcs & 0x0C)
            {
            case 0x00: return SmsConstants.ALPHABET_GSM;
            case 0x04: return SmsConstants.ALPHABET_8BIT;
            case 0x08: return SmsConstants.ALPHABET_UCS2;
            case 0x0C: return SmsConstants.ALPHABET_RESERVED;
            }
        }

        // Data coding/message class
        if ( (myDcs & 0xF0) == 0xF0)
        {
            switch (myDcs & 0x02)
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
    public boolean isCompressed()
    {
        // General Data Coding Indication, Compressed
        return ((myDcs & 0xE0) == 0x20);
    }
}
