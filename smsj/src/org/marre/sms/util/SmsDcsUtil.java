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
