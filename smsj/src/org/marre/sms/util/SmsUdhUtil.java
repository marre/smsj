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

import org.marre.sms.SmsUdhElement;
import org.marre.sms.SmsConstants;

/**
 * Toolkit class for SmsUdhElement objects
 * <p>
 * This class doesn't contain helper methods for all types of UDHI.
 *
 * @author Markus Eriksson
 */
public class SmsUdhUtil
{
    private SmsUdhUtil()
    {
    }

    /**
     * Creates a "8Bit concatenated" UDH element using UDH_IEI_CONCATENATED_8BIT
     * <p>
     * This can be used to create a concatenated SMS.
     *
     * @param theRefNr The reference number of this SMS, must be the same in
     * all SMS. Max 255.
     * @param theTotSms Total number of SMS. Max 255.
     * @param theSeqNr Sequence number. Max 255.
     * @return A SmsUdhElement
     */
    public static final SmsUdhElement get8BitConcatUdh(int theRefNr, int theTotSms, int theSeqNr)
    {
        byte[] udh = new byte[3];

        udh[0] = (byte) (theRefNr  & 0xff);
        udh[1] = (byte) (theTotSms & 0xff);
        udh[2] = (byte) (theSeqNr  & 0xff);

        return new SmsUdhElement(SmsConstants.UDH_IEI_CONCATENATED_8BIT, udh);
    }

    /**
     * Creates a "Message waiting" UDH element using UDH_IEI_SPECIAL_MESSAGE
     * <p>
     * If more than one type of message is required to be indicated within
     * one SMS message, then multiple "Message waiting" UDH elements must
     * be used.
     * <p>
     * <b>Special handling in concatenated messages:</b><br>
     * <i>
     * "In the case where this IEI is to be used in a concatenated SM then the
     * IEI, its associated IEI length and IEI data shall be contained in the
     * first segment of the concatenated SM. The IEI, its associated IEI length
     * and IEI data should also be contained in every subsequent segment of the
     * concatenated SM although this is not mandatory. However, in the case
     * where these elements are not contained in every subsequent segment of
     * the concatenated SM and where an out of sequence segment delivery
     * occurs or where the first segment is not delivered then processing
     * difficulties may arise at the receiving entity which may result in
     * the concatenated SM being totally or partially discarded."
     * </i>
     *
     * @param storeMsg Set to true if the message should be stored
     * @param theMsgType Message type, may be one of MESSAGE_WAITING_VOICE,
     * MESSAGE_WAITING_FAX, MESSAGE_WAITING_EMAIL or MESSAGE_WAITING_OTHER.
     * @param theMsgCount Number of messages waiting for retrieval. Max 255
     * messages. The value 255 shall be taken to mean 255 or greater.
     * @return A SmsUdhElement
     */
    public static final SmsUdhElement getMessageWaitingUdh(boolean storeMsg, int theMsgType, int theMsgCount)
    {
        byte[] udh = new byte[2];

        udh[0] = (byte) (theMsgType  & 0x7f);
        if ( storeMsg )
        {
            udh[0] |= (byte) (0x80);
        }
        udh[1] = (byte) (theMsgCount & 0xff);

        return new SmsUdhElement(SmsConstants.UDH_IEI_SPECIAL_MESSAGE, udh);
    }

    /**
     * Creates a "8 bit Application Port Adressing" UDH element
     * using UDH_IEI_APP_PORT_8BIT
     * <p>
     * Note! Only values between 240 and 255 are usable, the rest of the port
     * numbers are marked as reserved.
     * <p>
     * <b>Special handling in concatenated messages:</b><br>
     * <i>
     * In the case where this IE is to be used in a concatenated SM then the
     * IEI, its associated IEI length and IEI data shall be contained in the
     * first segment of the concatenated SM. The IEI, its associated IEI length
     * and IEI data shall also be contained in every subsequent segment of the
     * concatenated SM.
     * </i>
     * @param theDestPort Destination port
     * @param theOrigPort Source port
     * @return A SmsUdhElement
     */
    public static final SmsUdhElement get8BitApplicationPortUdh(int theDestPort, int theOrigPort)
    {
        byte[] udh = new byte[2];

        udh[0] = (byte) (theDestPort & 0xff);
        udh[1] = (byte) (theOrigPort & 0xff);

        return new SmsUdhElement(SmsConstants.UDH_IEI_APP_PORT_8BIT, udh);
    }

    /**
     * Creates a "16 bit Application Port Adressing" UDH element
     * using UDH_IEI_APP_PORT_16BIT
     * <p>
     * Note! Only values between 0 and 16999 are usable, the rest of the port
     * numbers are marked as reserved.
     * <p>
     * <b>Special handling in concatenated messages:</b><br>
     * <i>
     * In the case where this IE is to be used in a concatenated SM then the
     * IEI, its associated IEI length and IEI data shall be contained in the
     * first segment of the concatenated SM. The IEI, its associated IEI length
     * and IEI data shall also be contained in every subsequent segment of the
     * concatenated SM.
     * </i>
     * @param theDestPort Destination port
     * @param theOrigPort Source port
     * @return A SmsUdhElement
     */
    public static final SmsUdhElement get16BitApplicationPortUdh(int theDestPort, int theOrigPort)
    {
        byte[] udh = new byte[4];

        udh[0] = (byte) (theDestPort & 0xff);
        udh[1] = (byte) ((theDestPort >> 8) & 0xff);
        udh[2] = (byte) (theOrigPort & 0xff);
        udh[3] = (byte) ((theOrigPort >> 8) & 0xff);

        return new SmsUdhElement(SmsConstants.UDH_IEI_APP_PORT_16BIT, udh);
    }

    /**
     * Creates a "16Bit concatenated" UDH element using UDH_IEI_CONCATENATED_16BIT
     * <p>
     * This can be used to create a concatenated SMS.
     *
     * @param theRefNr The reference number of this SMS, must be the same in
     * all SMS. Max 65536
     * @param theTotSms Total number of SMS. Max 255
     * @param theSeqNr Sequence number. Max 255
     * @return A SmsUdhElement
     */
    public static final SmsUdhElement get16BitConcatUdh(int theRefNr, int theTotSms, int theSeqNr)
    {
        byte[] udh = new byte[4];

        udh[0] = (byte) (theRefNr & 0xff);
        udh[1] = (byte) ((theRefNr >> 8) & 0xff);
        udh[2] = (byte) (theTotSms & 0xff);
        udh[3] = (byte) (theSeqNr  & 0xff);

        return new SmsUdhElement(SmsConstants.UDH_IEI_CONCATENATED_16BIT, udh);
    }

    /**
     * Creates a "EMS Text Formatting" UDH element.
     *
     * @param theStartPos Start position of the text formatting. This position
     * is relative to the start of the UD field of the PDU.
     * @param theFormatLen The number of character to format. If 0 it sets the
     * default text formatting.
     * @param theAlignment Can be any of EMS_TEXT_ALIGN_*
     * @param theFontSize Can be any of EMS_TEXT_SIZE_*
     * @param theStyle Can be any of EMS_TEXT_STYLE_*
     * @param theForegroundColor Can be any of EMS_TEXT_COLOR_*
     * @param theBackgroundColor Can be any of EMS_TEXT_COLOR_*
     * @return A SmsUdhElement
     */
    public static final SmsUdhElement getEmsTextFormattingUdh(int theStartPos, int theFormatLen,
        byte theAlignment, byte theFontSize, byte theStyle, byte theForegroundColor, byte theBackgroundColor)
    {
        byte[] udh = new byte[4];

        udh[0] = (byte) (theStartPos & 0xff);
        udh[1] = (byte) (theFormatLen & 0xff);
        udh[2] = (byte) (( (theAlignment & 0x03) | (theFontSize & 0x0C) | (theStyle & 0xF0) ) & 0xff);
        udh[3] = (byte) (( (theForegroundColor & 0x0f) | (((theBackgroundColor & 0x0f) << 4) & 0xf0) ) & 0xff);

        return new SmsUdhElement(SmsConstants.UDH_IEI_EMS_TEXT_FORMATTING, udh);
    }
}
