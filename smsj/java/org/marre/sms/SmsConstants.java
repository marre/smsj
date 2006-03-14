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
 * Contains various SMS related constants.
 * 
 * @author Markus Eriksson
 * @version $Id$
 */
public final class SmsConstants
{
    /**
     * Type-Of-Number - Unknown. "Unknown" is used when the user or network has
     * no a priori information about the numbering plan. In this case, the
     * Address-Value field is organized according to the network dialling plan,
     * e.g. prefix or escape digits might be present.
     */
    public static final byte TON_UNKNOWN = 0x00;
    /**
     * Type-Of-Number - International. The international format shall be
     * accepted also when the message is destined to a recipient in the same
     * country as the MSC or as the SGSN.
     * <p>
     * Most common.
     */
    public static final byte TON_INTERNATIONAL = 0x01;
    /**
     * Type-Of-Number - National. Prefix or escape digits shall not be included.
     */
    public static final byte TON_NATIONAL = 0x02;
    /**
     * Type-Of-Number - Network specific. "Network specific number" is used to
     * indicate administration/service number specific to the serving network,
     * e.g. used to access an operator.
     */
    public static final byte TON_NETWORK_SPECIFIC = 0x03;
    /**
     * Type-Of-Number - Subscriber. "Subscriber number" is used when a specific
     * short number representation is stored in one or more SCs as part of a
     * higher layer application. (Note that "Subscriber number" shall only be
     * used in connection with the proper PID referring to this application).
     */
    public static final byte TON_SUBSCRIBER = 0x04;
    /**
     * Type-Of-Number - Alphanumeric. Number must be coded according to 3GPP TS
     * 23.038 GSM 7-bit default alphabet.
     * <p>
     * NPI Must be set to UNKNOWN.
     */
    public static final byte TON_ALPHANUMERIC = 0x05;
    /**
     * Type-Of-Number - Abbreviated.
     */
    public static final byte TON_ABBREVIATED = 0x06;

    /**
     * Numbering-Plan-Identification - Unknown.
     */
    public static final byte NPI_UNKNOWN = 0x00;
    /**
     * Numbering-Plan-Identification - ISDN/Telephone. (E.164 /E.163)
     * <p>
     * Most common.
     */
    public static final byte NPI_ISDN_TELEPHONE = 0x01;
    /**
     * Numbering-Plan-Identification - Data Numbering Plan (X.121)
     */
    public static final byte NPI_DATA = 0x03;
    /**
     * Numbering-Plan-Identification - Telex.
     */
    public static final byte NPI_TELEX = 0x04;
    /**
     * Numbering-Plan-Identification - National.
     */
    public static final byte NPI_NATIONAL = 0x08;
    /**
     * Numbering-Plan-Identification - Private.
     */
    public static final byte NPI_PRIVATE = 0x09;
    /**
     * Numbering-Plan-Identification - Unknown.
     */
    public static final byte NPI_ERMES = 0x10;

    /** Concatenated short messages, 8-bit reference number. */
    public static final byte UDH_IEI_CONCATENATED_8BIT = 0x00;
    /** Special SMS Message Indication. */
    public static final byte UDH_IEI_SPECIAL_MESSAGE = 0x01;
    /** Application port addressing scheme, 8 bit address. */
    public static final byte UDH_IEI_APP_PORT_8BIT = 0x04;
    /** Application port addressing scheme, 16 bit address. */
    public static final byte UDH_IEI_APP_PORT_16BIT = 0x05;
    /** SMSC Control Parameters. */
    public static final byte UDH_IEI_SMSC_CONTROL_PARAMS = 0x06;
    /** UDH Source Indicator. */
    public static final byte UDH_IEI_UDH_SOURCE_INDICATOR = 0x07;
    /** Concatenated short message, 16-bit reference number. */
    public static final byte UDH_IEI_CONCATENATED_16BIT = 0x08;
    /** Wireless Control Message Protocol. */
    public static final byte UDH_IEI_WCMP = 0x09;

    /** Text Formatting (EMS). */
    public static final byte UDH_IEI_EMS_TEXT_FORMATTING = 0x0A;
    /** Predefined Sound (EMS). */
    public static final byte UDH_IEI_EMS_PREDEFINED_SOUND = 0x0B;
    /** User Defined Sound (iMelody max 128 bytes) (EMS). */
    public static final byte UDH_IEI_EMS_USER_DEFINED_SOUND = 0x0C;
    /** Predefined Animation (EMS). */
    public static final byte UDH_IEI_EMS_PREDEFINED_ANIMATION = 0x0D;
    /** Large Animation (16*16 times 4 = 32*4 =128 bytes) (EMS). */
    public static final byte UDH_IEI_EMS_LARGE_ANIMATION = 0x0E;
    /** Small Animation (8*8 times 4 = 8*4 =32 bytes) (EMS). */
    public static final byte UDH_IEI_EMS_SMALL_ANIMATION = 0x0F;
    /** Large Picture (32*32 = 128 bytes) (EMS). */
    public static final byte UDH_IEI_EMS_LARGE_PICTURE = 0x10;
    /** Small Picture (16*16 = 32 bytes) (EMS). */
    public static final byte UDH_IEI_EMS_SMALL_PICTURE = 0x11;
    /** Variable Picture (EMS). */
    public static final byte UDH_IEI_EMS_VARIABLE_PICTURE = 0x12;
    /** User prompt indicator (EMS). */
    public static final byte UDH_IEI_EMS_USER_PROMPT = 0x13;
    /** Extended Object (EMS). */
    public static final byte UDH_IEI_EMS_EXTENDED_OBJECT = 0x14;
    /** Reused Extended Object (EMS). */
    public static final byte UDH_IEI_EMS_REUSED_EXTENDED_OBJECT = 0x15;
    /** Compression Control (EMS). */
    public static final byte UDH_IEI_EMS_COMPRESSION_CONTROL = 0x16;

    /** RFC 822 E-Mail Header. */
    public static final byte UDH_IEI_RFC822_EMAIL_HEADER = 0x20;
    /** Hyperlink format element. */
    public static final byte UDH_IEI_HYPERLINK_FORMAT = 0x21;

    /* MESSAGE INDICATION TYPES FOR UDH_IEI_SPECIAL_MESSAGE */

    /** UDH_IEI_SPECIAL_MESSAGE type "Voice Message Waiting". */
    public static final int UISM_MSG_WAITING_VOICE = 0x00;
    /** UDH_IEI_SPECIAL_MESSAGE type "Fax Message Waiting". */
    public static final int UISM_MSG_WAITING_FAX = 0x01;
    /** UDH_IEI_SPECIAL_MESSAGE type "Electronic Mail Message Waiting". */
    public static final int UISM_MSG_WAITING_EMAIL = 0x02;
    /**
     * UDH_IEI_SPECIAL_MESSAGE type "Other Message Waiting". (see 3GPP TS 23.038
     * [9] for definition of 'other')"
     */
    public static final int UISM_MSG_WAITING_OTHER = 0x03;

    /* PORT NUMBERS */

    /** WAP Push. */
    public static final int PORT_WAP_PUSH = 2948;

    /** Nokia Internet access configuration data. */
    public static final int PORT_NOKIA_IAC = 5503;
    /** Nokia Ring Tone. */
    public static final int PORT_NOKIA_RING_TONE = 5505;
    /** Nokia Operator Logo. */
    public static final int PORT_NOKIA_OPERATOR_LOGO = 5506;
    /** Nokia Calling Line Identification Logo. */
    public static final int PORT_NOKIA_CLI_LOGO = 5507;
    /** Nokia Email notification. */
    public static final int PORT_NOKIA_EMAIL_NOTIFICATION = 5512;
    /** Nokia Multipart Message. */
    public static final int PORT_NOKIA_MULTIPART_MESSAGE = 5514;

    /** WAP connectionless session service. */
    public static final int PORT_WAP_WSP = 9200;
    /** WAP session service. */
    public static final int PORT_WAP_WSP_WTP = 9201;

    /** WAP vCard. */
    public static final int PORT_WAP_VCARD = 9204;
    /** WAP vCalendar. */
    public static final int PORT_WAP_VCALENDAR = 9205;

    /** OTA Settings - Browser. */
    public static final int PORT_OTA_SETTINGS_BROWSER = 49999;

    /** OTA Settings - SyncML. */
    public static final int PORT_OTA_SETTINGS_SYNCML = 49996;

    /** EMS - Left aligned text. */
    public static final byte EMS_TEXT_ALIGN_LEFT = 0x00;
    /** EMS - Centered text. */
    public static final byte EMS_TEXT_ALIGN_CENTER = 0x01;
    /** EMS - Right aligned text. */
    public static final byte EMS_TEXT_ALIGN_RIGHT = 0x02;
    /** EMS - Default alignment (language dependent). */
    public static final byte EMS_TEXT_ALIGN_DEFAULT = 0x03;

    /** EMS - Normal size font. */
    public static final byte EMS_TEXT_SIZE_NORMAL = 0x00;
    /** EMS - Large size font. */
    public static final byte EMS_TEXT_SIZE_LARGE = 0x04;
    /** EMS - Small size font. */
    public static final byte EMS_TEXT_SIZE_SMALL = 0x08;

    /** EMS - Normal size font. */
    public static final byte EMS_TEXT_STYLE_BOLD = (byte) 0x10;
    /** EMS - Large size font. */
    public static final byte EMS_TEXT_STYLE_ITALIC = (byte) 0x20;
    /** EMS - Small size font. */
    public static final byte EMS_TEXT_STYLE_UNDERLINED = (byte) 0x40;
    /** EMS - Small size font. */
    public static final byte EMS_TEXT_STYLE_STRIKETHROUGH = (byte) 0x80;

    /** EMS - Color black. */
    public static final byte EMS_TEXT_COLOR_BLACK = 0x00;
    /** EMS - Color dark grey. */
    public static final byte EMS_TEXT_COLOR_DARK_GREY = 0x01;
    /** EMS - Color dark red. */
    public static final byte EMS_TEXT_COLOR_DARK_RED = 0x02;
    /** EMS - Color dark yellow. */
    public static final byte EMS_TEXT_COLOR_DARK_YELLOW = 0x03;
    /** EMS - Color dark green. */
    public static final byte EMS_TEXT_COLOR_DARK_GREEN = 0x04;
    /** EMS - Color dark cyan. */
    public static final byte EMS_TEXT_COLOR_DARK_CYAN = 0x05;
    /** EMS - Color dark blue. */
    public static final byte EMS_TEXT_COLOR_DARK_BLUE = 0x06;
    /** EMS - Color dark magenta. */
    public static final byte EMS_TEXT_COLOR_DARK_MAGENTA = 0x07;
    /** EMS - Color grey. */
    public static final byte EMS_TEXT_COLOR_GREY = 0x08;
    /** EMS - Color white. */
    public static final byte EMS_TEXT_COLOR_WHITE = 0x09;
    /** EMS - Color bright red. */
    public static final byte EMS_TEXT_COLOR_BRIGHT_RED = 0x0A;
    /** EMS - Color bright yellow. */
    public static final byte EMS_TEXT_COLOR_BRIGHT_YELLOW = 0x0B;
    /** EMS - Color bright green. */
    public static final byte EMS_TEXT_COLOR_BRIGHT_GREEN = 0x0C;
    /** EMS - Color bright cyan. */
    public static final byte EMS_TEXT_COLOR_BRIGHT_CYAN = 0x0D;
    /** EMS - Color bright blue. */
    public static final byte EMS_TEXT_COLOR_BRIGHT_BLUE = 0x0E;
    /** EMS - Color bright magenta. */
    public static final byte EMS_TEXT_COLOR_BRIGHT_MAGENTA = 0x0F;

    /** EMS - Predefined sound - Chimes high. */
    public static final byte EMS_PREDEFINED_SOUND_CHIMES_HIGH = 0x00;
    /** EMS - Predefined sound - Chimes low. */
    public static final byte EMS_PREDEFINED_SOUND_CHIMES_LOW = 0x01;
    /** EMS - Predefined sound - Ding. */
    public static final byte EMS_PREDEFINED_SOUND_DING = 0x02;
    /** EMS - Predefined sound - TaDa. */
    public static final byte EMS_PREDEFINED_SOUND_TADA = 0x03;
    /** EMS - Predefined sound - Notify. */
    public static final byte EMS_PREDEFINED_SOUND_NOTIFY = 0x04;
    /** EMS - Predefined sound - Drum. */
    public static final byte EMS_PREDEFINED_SOUND_DRUM = 0x05;
    /** EMS - Predefined sound - Claps. */
    public static final byte EMS_PREDEFINED_SOUND_CLAPS = 0x06;
    /** EMS - Predefined sound - FanFar. */
    public static final byte EMS_PREDEFINED_SOUND_FANFAR = 0x07;
    /** EMS - Predefined sound - Chord high. */
    public static final byte EMS_PREDEFINED_SOUND_CHORD_HIGH = 0x08;
    /** EMS - Predefined sound - Chord low. */
    public static final byte EMS_PREDEFINED_SOUND_CHORD_LOW = 0x09;

    /** EMS - Predefined animation - I am ironic, flirty. */
    public static final byte EMS_PREDEFINED_ANIM_IRONIC = 0x00;
    /** EMS - Predefined animation - I am glad. */
    public static final byte EMS_PREDEFINED_ANIM_GLAD = 0x01;
    /** EMS - Predefined animation - I am sceptic. */
    public static final byte EMS_PREDEFINED_ANIM_SCEPTIC = 0x02;
    /** EMS - Predefined animation - I am sad. */
    public static final byte EMS_PREDEFINED_ANIM_SAD = 0x03;
    /** EMS - Predefined animation - WOW. */
    public static final byte EMS_PREDEFINED_ANIM_WOW = 0x04;
    /** EMS - Predefined animation - I am crying. */
    public static final byte EMS_PREDEFINED_ANIM_CRYING = 0x05;
    /** EMS - Predefined animation - I am winking. */
    public static final byte EMS_PREDEFINED_ANIM_WINKING = 0x06;
    /** EMS - Predefined animation - I am lughing. */
    public static final byte EMS_PREDEFINED_ANIM_LAUGHING = 0x07;
    /** EMS - Predefined animation - I am indifferent. */
    public static final byte EMS_PREDEFINED_ANIM_INDIFFERENT = 0x08;
    /** EMS - Predefined animation - In love/Kissing. */
    public static final byte EMS_PREDEFINED_ANIM_LOVE = 0x09;
    /** EMS - Predefined animation - I am confused. */
    public static final byte EMS_PREDEFINED_ANIM_CONFUSED = 0x0A;
    /** EMS - Predefined animation - Tounge hanging out. */
    public static final byte EMS_PREDEFINED_ANIM_TOUNGE = 0x0B;
    /** EMS - Predefined animation - I am angry. */
    public static final byte EMS_PREDEFINED_ANIM_ANGRY = 0x0C;
    /** EMS - Predefined animation - Wearing glasses. */
    public static final byte EMS_PREDEFINED_ANIM_GLASSES = 0x0D;
    /** EMS - Predefined animation - Devil. */
    public static final byte EMS_PREDEFINED_ANIM_DEVIL = 0x0E;

    private SmsConstants()
    {
        // Empty
    }
}
