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
package org.marre.sms;

/**
 * Various functions to decode and encode the DCS byte (Data Coding Scheme).
 *
 * @version $Id$
 * @author Markus Eriksson
 */
public final class SmsDcsUtil
{
    /**
     * Hide the constructor.
     */
    private SmsDcsUtil()
    {
        // Empty
    }
    
    /**
     * What group (type of message) is the given dcs.
     * 
     * @param theDcs the dcs to test
     * @return Any of the SmsConstants.DCS_GROUP_ constants.
     */
    public static int getGroup(byte theDcs)
    {
        if ((theDcs & 0xC0) == 0x00) 
        {
            return SmsConstants.DCS_GROUP_GENERAL_DATA_CODING;
        }
        
        switch ((theDcs & 0xF0))
        {
        case 0xC0: return SmsConstants.DCS_GROUP_MESSAGE_WAITING_DISCARD;
        case 0xD0: return SmsConstants.DCS_GROUP_MESSAGE_WAITING_STORE_GSM;
        case 0xE0: return SmsConstants.DCS_GROUP_MESSAGE_WAITING_STORE_UCS2;
        case 0xF0: return SmsConstants.DCS_GROUP_DATA_CODING_MESSAGE;
        default:   return SmsConstants.DCS_GROUP_UNKNOWN;
        }
    }
    
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
        switch (getGroup(theDcs))
        {
        case SmsConstants.DCS_GROUP_GENERAL_DATA_CODING:
            // General Data Coding Indication
            if (theDcs == 0x00)
            {
                return SmsConstants.MSG_CLASS_UNKNOWN;
            }
            
            switch (theDcs & 0x13)
            {
            case 0x10: return SmsConstants.MSG_CLASS_0;
            case 0x11: return SmsConstants.MSG_CLASS_1;
            case 0x12: return SmsConstants.MSG_CLASS_2;
            case 0x13: return SmsConstants.MSG_CLASS_3;
            default:   return SmsConstants.MSG_CLASS_UNKNOWN;
            }
            
        case SmsConstants.DCS_GROUP_DATA_CODING_MESSAGE:
            // Data coding/message class
            switch (theDcs & 0x03)
            {
            case 0x00: return SmsConstants.MSG_CLASS_0;
            case 0x01: return SmsConstants.MSG_CLASS_1;
            case 0x02: return SmsConstants.MSG_CLASS_2;
            case 0x03: return SmsConstants.MSG_CLASS_3;
            default:   return SmsConstants.MSG_CLASS_UNKNOWN;
            }
            
        default:
            return SmsConstants.MSG_CLASS_UNKNOWN;
        }
    }

    /**
     * Decodes the given dcs and returns the alphabet.
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
     * @param theDcs The dcs to decode. 
     * @return Returns the alphabet. See SmsConstants for valid alphabets.
     */
    public static int getAlphabet(byte theDcs)
    {
        switch (getGroup(theDcs))
        {
        case SmsConstants.DCS_GROUP_GENERAL_DATA_CODING:
            // General Data Coding Indication
            if (theDcs == 0x00)
            {
                return SmsConstants.ALPHABET_GSM;
            }
            
            switch (theDcs & 0x0C)
            {
            case 0x00: return SmsConstants.ALPHABET_GSM;
            case 0x04: return SmsConstants.ALPHABET_8BIT;
            case 0x08: return SmsConstants.ALPHABET_UCS2;
            case 0x0C: return SmsConstants.ALPHABET_RESERVED;
            default:   return SmsConstants.ALPHABET_UNKNOWN;
            }
            
        case SmsConstants.DCS_GROUP_MESSAGE_WAITING_STORE_GSM:
            return SmsConstants.ALPHABET_GSM;
        
        case SmsConstants.DCS_GROUP_MESSAGE_WAITING_STORE_UCS2:
            return SmsConstants.ALPHABET_UCS2;

        case SmsConstants.DCS_GROUP_DATA_CODING_MESSAGE:
            switch (theDcs & 0x04)
            {
            case 0x00: return SmsConstants.ALPHABET_GSM;
            case 0x04: return SmsConstants.ALPHABET_8BIT;
            default:   return SmsConstants.ALPHABET_UNKNOWN;
            }
        
        default:
            return SmsConstants.ALPHABET_UNKNOWN;
        }        
    }

    /**
     * Is the message compressed using the GSM standard compression algorithm.
     * 
     * See GSM TS 03.42 for further information.
     * 
     * @param theDcs The dcs to decode
     * @return true if compressed, false otherwise
     */
    public static boolean isCompressed(byte theDcs)
    {
        // General Data Coding Indication, Compressed
        return ((theDcs & 0xE0) == 0x20);
    }

    
    /**
     * Builds a general-data-coding dcs (uncompressed).
     * 
     * @param alphabet The alphabet. Possible values are SmsConstants.ALPHABET_GSM, SmsConstants.ALPHABET_8BIT and 
     *                 SmsConstants.ALPHABET_UCS2.
     * @param messageClass The message class. Possible values are SmsConstants.MSG_CLASS_0, SmsConstants.MSG_CLASS_1, 
     *                     SmsConstants.MSG_CLASS_2 and SmsConstants.MSG_CLASS_3.
     * @return A valid general data coding DCS.
     */
    public static byte getGeneralDataCodingDcs(int alphabet, byte messageClass)
    {
        return getGeneralDataCodingDcs(alphabet, messageClass, SmsConstants.DCS_COMPRESSION_OFF);
    }
    
    /**
     * Builds a general-data-coding dcs.
     * 
     * @param alphabet The alphabet. Possible values are SmsConstants.ALPHABET_GSM, SmsConstants.ALPHABET_8BIT, 
     *                 SmsConstants.ALPHABET_UCS2 and SmsConstants.ALPHABET_RESERVED. 
     * @param messageClass The message class. Possible values are SmsConstants.MSG_CLASS_0, SmsConstants.MSG_CLASS_1, 
     *                     SmsConstants.MSG_CLASS_2 and SmsConstants.MSG_CLASS_3.
     * @param compressed Sets the compressed bit. Possible values are SmsConstants.DCS_COMPRESSION_OFF and 
     *                   SmsConstants.DCS_COMPRESSION_ON.
     * @return A valid general data coding DCS.
     */
    public static byte getGeneralDataCodingDcs(int alphabet, byte messageClass, byte compressed)
    {
        byte dcs = 0x00;
        
        // Bit 5, if set to 0, indicates the text is uncompressed
        // Bit 5, if set to 1, indicates the text is compressed using the GSM standard compression
        // algorithm. (see GSM TS 03.42)
        switch (compressed)
        {
        case SmsConstants.DCS_COMPRESSION_ON:
            dcs |= 0x20;
            break;
        case SmsConstants.DCS_COMPRESSION_OFF:
            // Do nothing
            break;
        
        default:
            throw new IllegalArgumentException("Invalid value for compressed");
        }
        
        // Bits 3 and 2 indicate the alphabet being used, as follows :
        // Bit3 Bit2 Alphabet:
        //    0   0  Default alphabet
        //    0   1  8 bit data
        //    1   0  UCS2 (16bit) [10]
        //    1   1  Reserved
        switch (alphabet)
        {
        case SmsConstants.ALPHABET_GSM:      dcs |= 0x00; break; 
        case SmsConstants.ALPHABET_8BIT:     dcs |= 0x04; break;
        case SmsConstants.ALPHABET_UCS2:     dcs |= 0x08; break;
        case SmsConstants.ALPHABET_RESERVED: dcs |= 0x0C; break;
        
        case SmsConstants.ALPHABET_UNKNOWN:
        default:
            throw new IllegalArgumentException("Invalid alphabet");
        }
        
        switch (messageClass)
        {
        case SmsConstants.MSG_CLASS_0:          dcs |= 0x10; break; 
        case SmsConstants.MSG_CLASS_1:          dcs |= 0x11; break;
        case SmsConstants.MSG_CLASS_2:          dcs |= 0x12; break;
        case SmsConstants.MSG_CLASS_3:          dcs |= 0x13; break;
        case SmsConstants.MSG_CLASS_UNKNOWN:    dcs |= 0x00; break;
            
        default:
            throw new IllegalArgumentException("Invalid message class");
        }
                
        return dcs;
    }
 }
