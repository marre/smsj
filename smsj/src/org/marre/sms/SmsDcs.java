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
 * Represents a SMS DCS (Data Coding Scheme).
 *
 * @version $Id$
 * @author Markus Eriksson
 */
public class SmsDcs
{
    /**
     * Alphabet as defined in GSM 03.38. It contains all characters needed for most
     * Western European languages. It also contains upper case Greek characters.
     */
    public static final int ALPHABET_GSM = 0;
    /** ISO 8859-1 (ISO Latin-1). */
    public static final int ALPHABET_8BIT = 1;
    /** Unicode UCS-2. */
    public static final int ALPHABET_UCS2 = 2;
    /** Reserved. */
    public static final int ALPHABET_RESERVED = 3;
    /** Unknown. */
    public static final int ALPHABET_UNKNOWN = 4;

    /** Class 0 SMS. Sometimes called FLASH message. */
    public static final int MSG_CLASS_0 = 0;
    /** Class 1 SMS. Default meaning: ME-specific. */
    public static final int MSG_CLASS_1 = 1;
    /** Class 2 SMS, SIM specific message. */
    public static final int MSG_CLASS_2 = 2;
    /** Class 3 SMS. Default meaning: TE specific (See GSM TS 07.05). */
    public static final int MSG_CLASS_3 = 3;
    /** Message with no specific message class (Often handled as an class 1 SMS). */
    public static final int MSG_CLASS_UNKNOWN = 4;

    /** DCS general data coding indication group. 00xxxxxx. */
    public static final int GROUP_GENERAL_DATA_CODING = 0;
    /** DCS message waiting indication group: discard message. 1100xxxx. */
    public static final int GROUP_MESSAGE_WAITING_DISCARD = 1;
    /** DCS message waiting indication group: store message (gsm). 1101xxxx. */
    public static final int GROUP_MESSAGE_WAITING_STORE_GSM = 2;
    /** DCS message waiting indication group: store message (ucs2). 1110xxxx. */
    public static final int GROUP_MESSAGE_WAITING_STORE_UCS2 = 3;
    /** DCS data coding/message class: 1111xxxx. */
    public static final int GROUP_DATA_CODING_MESSAGE = 4;
    /** DCS group unknown. */
    public static final int GROUP_UNKNOWN = 5;
    
    /** Message waiting indication type - voicemail. */
    public static final int DCS_MSG_WAITING_VOICEMAIL = 0;
    /** Message waiting indication type - fax. */
    public static final int DCS_MSG_WAITING_FAX = 1;
    /** Message waiting indication type - email. */
    public static final int DCS_MSG_WAITING_EMAIL = 2;
    /** Message waiting indication type - other. Should not be used. */
    public static final int DCS_MSG_WAITING_OTHER = 3;
          
    /** The encoded dcs. */
    protected byte dcs_;
    
    /**
     * Creates a specific DCS.
     * 
     * @param dcs The dcs.
     */
    public SmsDcs(byte dcs)
    {
        dcs_ = dcs;
    }
    
    /**
     * Returns the encoded dcs.
     * 
     * @return The dcs.
     */
    public byte getValue()
    {
        return dcs_;
    }
    
    /**
     * Builds a general-data-coding dcs.
     * 
     * @param alphabet The alphabet. Possible values are ALPHABET_GSM, ALPHABET_8BIT, ALPHABET_UCS2 and ALPHABET_RESERVED. 
     * @param messageClass The message class. Possible values are MSG_CLASS_0, MSG_CLASS_1, MSG_CLASS_2 and MSG_CLASS_3.
     * 
     * @return A valid general data coding DCS.
     */
    public static SmsDcs getGeneralDataCodingDcs(int alphabet, int messageClass)
    {
        byte dcs = 0x00;
        
        // Bits 3 and 2 indicate the alphabet being used, as follows :
        // Bit3 Bit2 Alphabet:
        //    0   0  Default alphabet
        //    0   1  8 bit data
        //    1   0  UCS2 (16bit) [10]
        //    1   1  Reserved
        switch (alphabet)
        {
        case ALPHABET_GSM:      dcs |= 0x00; break; 
        case ALPHABET_8BIT:     dcs |= 0x04; break;
        case ALPHABET_UCS2:     dcs |= 0x08; break;
        case ALPHABET_RESERVED: dcs |= 0x0C; break;
        
        case ALPHABET_UNKNOWN:
        default:
            throw new IllegalArgumentException("Invalid alphabet");
        }
        
        switch (messageClass)
        {
        case MSG_CLASS_0:          dcs |= 0x10; break; 
        case MSG_CLASS_1:          dcs |= 0x11; break;
        case MSG_CLASS_2:          dcs |= 0x12; break;
        case MSG_CLASS_3:          dcs |= 0x13; break;
        case MSG_CLASS_UNKNOWN:    dcs |= 0x00; break;
            
        default:
            throw new IllegalArgumentException("Invalid message class");
        }
                
        return new SmsDcs(dcs);
    }
    
    /**
     * Decodes the given dcs and returns the alphabet.
     *
     * <pre>
     * Return value can be one of:
     * - ALPHABET_GSM
     * - ALPHABET_8BIT
     * - ALPHABET_UCS2
     * - ALPHABET_RESERVED
     * - ALPHABET_UNKNOWN
     * </pre>
     * 
     * @return Returns the alphabet.
     */
    public int getAlphabet()
    {
        switch (getGroup())
        {
        case GROUP_GENERAL_DATA_CODING:
            // General Data Coding Indication
            if (dcs_ == 0x00)
            {
                return ALPHABET_GSM;
            }
            
            switch (dcs_ & 0x0C)
            {
            case 0x00: return ALPHABET_GSM;
            case 0x04: return ALPHABET_8BIT;
            case 0x08: return ALPHABET_UCS2;
            case 0x0C: return ALPHABET_RESERVED;
            default:   return ALPHABET_UNKNOWN;
            }
            
        case GROUP_MESSAGE_WAITING_STORE_GSM:
            return ALPHABET_GSM;
        
        case GROUP_MESSAGE_WAITING_STORE_UCS2:
            return ALPHABET_UCS2;

        case GROUP_DATA_CODING_MESSAGE:
            switch (dcs_ & 0x04)
            {
            case 0x00: return ALPHABET_GSM;
            case 0x04: return ALPHABET_8BIT;
            default:   return ALPHABET_UNKNOWN;
            }
        
        default:
            return ALPHABET_UNKNOWN;
        }                
    }
    
    /**
     * What group (type of message) is the given dcs.
     * 
     * @param theDcs the dcs to test
     * @return Any of the GROUP_ constants.
     */
    public int getGroup()
    {
        if ((dcs_ & 0xC0) == 0x00) 
        {
            return GROUP_GENERAL_DATA_CODING;
        }
        
        switch ((dcs_ & 0xF0))
        {
        case 0xC0: return GROUP_MESSAGE_WAITING_DISCARD;
        case 0xD0: return GROUP_MESSAGE_WAITING_STORE_GSM;
        case 0xE0: return GROUP_MESSAGE_WAITING_STORE_UCS2;
        case 0xF0: return GROUP_DATA_CODING_MESSAGE;
        default:   return GROUP_UNKNOWN;
        }
    }
    
    /**
     * Get the message class.
     *
     * <pre>
     * Return value can be one of:
     * - MSG_CLASS_UNKNOWN
     * - MSG_CLASS_0
     * - MSG_CLASS_1
     * - MSG_CLASS_2
     * - MSG_CLASS_3
     * </pre>
     *
     * @return Returns the message class.
     */
    public int getMessageClass()
    {
        switch (getGroup())
        {
        case GROUP_GENERAL_DATA_CODING:
            // General Data Coding Indication
            if (dcs_ == 0x00)
            {
                return MSG_CLASS_UNKNOWN;
            }
            
            switch (dcs_ & 0x13)
            {
            case 0x10: return MSG_CLASS_0;
            case 0x11: return MSG_CLASS_1;
            case 0x12: return MSG_CLASS_2;
            case 0x13: return MSG_CLASS_3;
            default:   return MSG_CLASS_UNKNOWN;
            }
            
        case GROUP_DATA_CODING_MESSAGE:
            // Data coding/message class
            switch (dcs_ & 0x03)
            {
            case 0x00: return MSG_CLASS_0;
            case 0x01: return MSG_CLASS_1;
            case 0x02: return MSG_CLASS_2;
            case 0x03: return MSG_CLASS_3;
            default:   return MSG_CLASS_UNKNOWN;
            }
            
        default:
            return MSG_CLASS_UNKNOWN;
        }
    }
}
