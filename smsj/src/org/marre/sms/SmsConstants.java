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

public class SmsConstants
{
    /**
     * Type-Of-Number - Unknown.
     * "Unknown" is used when the user or network has no a priori information
     * about the numbering plan. In this case, the Address-Value field is
     * organized according to the network dialling plan, e.g. prefix or escape
     * digits might be present.
     */
    public static final byte TON_UNKNOWN = 0x00;
    /**
     * Type-Of-Number - International.
     * The international format shall be accepted also when the message is
     * destined to a recipient in the same country as the MSC or as the SGSN.
     * <p>
     * Most common.
     */
    public static final byte TON_INTERNATIONAL = 0x01;
    /**
     * Type-Of-Number - National.
     * Prefix or escape digits shall not be included.
     */
    public static final byte TON_NATIONAL = 0x02;
    /**
     * Type-Of-Number - Network specific.
     * "Network specific number" is used to indicate administration/service
     * number specific to the serving network, e.g. used to access an operator.
     */
    public static final byte TON_NETWORK_SPECIFIC = 0x03;
    /**
     * Type-Of-Number - Subscriber.
     * "Subscriber number" is used when a specific short number representation
     * is stored in one or more SCs as part of a higher layer application.
     * (Note that "Subscriber number" shall only be used in connection with the
     *  proper PID referring to this application).
     */
    public static final byte TON_SUBSCRIBER = 0x04;
    /**
     * Type-Of-Number - Alphanumeric.
     * Number must be coded according to 3GPP TS 23.038 GSM 7-bit default
     * alphabet.
     * <p>
     * NPI Must be set to UNKNOWN.
     */
    public static final byte TON_ALPHANUMERIC = 0x05;
    /**
     * Type-Of-Number - Abbreviated.
     */
    public static final byte TON_ABBREVIATED = 0x06;

    /**
     * Numbering-Plan-Identification - Unknown
     */
    public static final byte NPI_UNKNOWN = 0x00;
    /**
     * Numbering-Plan-Identification - ISDN/Telephone.
     * (E.164 /E.163)
     * <p>
     * Most common.
     */
    public static final byte NPI_ISDN_TELEPHONE = 0x01;
    /**
     * Numbering-Plan-Identification - Data Numbering Plan
     * (X.121)
     */
    public static final byte NPI_DATA = 0x03;
    /**
     * Numbering-Plan-Identification - Telex
     */
    public static final byte NPI_TELEX = 0x04;
    /**
     * Numbering-Plan-Identification - National
     */
    public static final byte NPI_NATIONAL = 0x08;
    /**
     * Numbering-Plan-Identification - Private
     */
    public static final byte NPI_PRIVATE = 0x09;
    /**
     * Numbering-Plan-Identification - Unknown
     */
    public static final byte NPI_ERMES = 0x10;

    /** Concatenated short messages, 8-bit reference number */
    public static final byte UDH_IEI_CONCATENATED_8BIT = 0x00;
    /** Special SMS Message Indication */
    public static final byte UDH_IEI_SPECIAL_MESSAGE = 0x01;
    /** Application port addressing scheme, 8 bit address */
    public static final byte UDH_IEI_APP_PORT_8BIT = 0x04;
    /** Application port addressing scheme, 16 bit address */
    public static final byte UDH_IEI_APP_PORT_16BIT = 0x05;
    /** SMSC Control Parameters */
    public static final byte UDH_IEI_SMSC_CONTROL_PARAMS = 0x06;
    /** UDH Source Indicator */
    public static final byte UDH_IEI_UDH_SOURCE_INDICATOR = 0x07;
    /** Concatenated short message, 16-bit reference number */
    public static final byte UDH_IEI_CONCATENATED_16BIT = 0x08;
    /** Wireless Control Message Protocol */
    public static final byte UDH_IEI_WCMP = 0x09;

    /** Text Formatting (EMS) */
    public static final byte UDH_IEI_EMS_TEXT_FORMATTING = 0x0A;
    /** Predefined Sound (EMS) */
    public static final byte UDH_IEI_EMS_PREDEFINED_SOUND = 0x0B;
    /** User Defined Sound (iMelody max 128 bytes) (EMS) */
    public static final byte UDH_IEI_EMS_USER_DEFINED_SOUND = 0x0C;
    /** Predefined Animation (EMS) */
    public static final byte UDH_IEI_EMS_PREDEFINED_ANIMATION = 0x0D;
    /** Large Animation (16*16 times 4 = 32*4 =128 bytes) (EMS) */
    public static final byte UDH_IEI_EMS_LARGE_ANIMATION = 0x0E;
    /** Small Animation (8*8 times 4 = 8*4 =32 bytes) (EMS) */
    public static final byte UDH_IEI_EMS_SMALL_ANIMATION = 0x0F;
    /** Large Picture (32*32 = 128 bytes) (EMS) */
    public static final byte UDH_IEI_EMS_LARGE_PICTURE = 0x10;
    /** Small Picture (16*16 = 32 bytes) (EMS) */
    public static final byte UDH_IEI_EMS_SMALL_PICTURE = 0x11;
    /** Variable Picture (EMS) */
    public static final byte UDH_IEI_EMS_VARIABLE_PICTURE = 0x12;
    /** User prompt indicator (EMS) */
    public static final byte UDH_IEI_EMS_USER_PROMPT = 0x13;
    /** Extended Object (EMS) */
    public static final byte UDH_IEI_EMS_EXTENDED_OBJECT = 0x14;
    /** Reused Extended Object (EMS) */
    public static final byte UDH_IEI_EMS_REUSED_EXTENDED_OBJECT = 0x15;
    /** Compression Control (EMS) */
    public static final byte UDH_IEI_EMS_COMPRESSION_CONTROL = 0x16;

    /** RFC 822 E-Mail Header */
    public static final byte UDH_IEI_RFC822_EMAIL_HEADER = 0x20;
    /** Hyperlink format element */
    public static final byte UDH_IEI_HYPERLINK_FORMAT = 0x21;

    /* PORT NUMBERS */

    /** WAP Push */
    public static final int PORT_WAP_PUSH = 2948;

    /** Nokia Internet access configuration data */
    public static final int PORT_NOKIA_IAC = 5503;
    /** Nokia Ring Tone */
    public static final int PORT_NOKIA_RING_TONE = 5505;
    /** Nokia Operator Logo */
    public static final int PORT_NOKIA_OPERATOR_LOGO = 5506;
    /** Nokia Calling Line Identification Logo */
    public static final int PORT_NOKIA_CLI_LOGO = 5507;
    /** Nokia Email notification */
    public static final int PORT_NOKIA_EMAIL_NOTIFICATION = 5512;
    /** Nokia Multipart Message */
    public static final int PORT_NOKIA_MULTIPART_MESSAGE = 5514;

    /** WAP connectionless session service */
    public static final int PORT_WAP_WSP = 9200;
    /** WAP session service */
    public static final int PORT_WAP_WSP_WTP = 9201;

    /** WAP vCard */
    public static final int PORT_WAP_VCARD = 9204;
    /** WAP vCalendar */
    public static final int PORT_WAP_VCALENDAR = 9205;

    /**
     * As defined in GSM 03.38. It contains all characters needed for most
     * Western European languages. It also contains upper case Greek characters.
     */
    public static final int TEXT_ALPHABET_GSM = 0;
    /**
     * ISO 8859-1 (ISO Latin-1)
     */
    public static final int TEXT_ALPHABET_8BIT = 1;
    /**
     * Unicode UCS-2
     */
    public static final int TEXT_ALPHABET_UCS2 = 2;

    public static final byte MSG_CLASS_0 = 0x00;
    public static final byte MSG_CLASS_1 = 0x01;
    public static final byte MSG_CLASS_2 = 0x02;
    public static final byte MSG_CLASS_3 = 0x03;
    public static final byte MSG_CLASS_UNKNOWN = 0x04;

    private SmsConstants()
    {
    }
}
