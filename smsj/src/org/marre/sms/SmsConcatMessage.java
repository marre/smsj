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

import java.util.*;
import org.marre.sms.util.*;

/**
 * Baseclass for messages that needs to be concatenated.
 * <p>
 * - Only usable for messages that uses the same UDH fields for all
 * message parts.<br>
 * - Can currently only handle 8-bit messages.
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public class SmsConcatMessage extends SmsAbstractMessage
{
    private static final Random myRnd = new Random();

    /** Length of myUd, can be in octets or septets */
    protected int myUdLength = 0;

    protected SmsUdhElement[] myUdhElements = null;
    protected byte myUd[] = null;

    protected SmsConcatMessage(byte theDcs)
    {
        setDataCodingScheme(theDcs);
    }

    public SmsConcatMessage(byte theDcs, SmsUdhElement[] theUdhElements, byte[] theUd, int theUdLength)
    {
        setDataCodingScheme(theDcs);
        setContent(theUdhElements, theUd, theUdLength);
    }

    /**
     * Set content of this message
     * @param theUdhElements
     * @param theUd
     * @param theUdLength
     */
    public void setContent(SmsUdhElement[] theUdhElements, byte[] theUd, int theUdLength)
    {
        myUdhElements = theUdhElements;
        myUd = theUd;
        myUdLength = theUdLength;
    }

    /**
     * Returns the whole UD
     *
     * @return the UD
     */
    public byte[] getUd()
    {
        return myUd;
    }

    /**
     * Returns the whole udh as a byte array
     *
     * @return the UDH
     */
    public byte[] getUdh()
    {
        return null;
    }

    /**
     * Returns the udh elements
     *
     * @return the UDH as SmsUdhElements
     */
    public SmsUdhElement[] getUdhElements()
    {
        return myUdhElements;
    }

    private SmsPdu[] createOctalPdus(int theMaxBytes)
    {
        int nMaxChars;
        int nMaxConcatChars;
        SmsPdu smsPdus[] = null;

        // 8-bit concat header is 6 bytes...
        nMaxConcatChars = theMaxBytes - 6;
        nMaxChars = nMaxConcatChars;

        if (myUdLength <= nMaxChars)
        {
            smsPdus = new SmsPdu[] { new SmsPdu(myUdhElements, myUd, myUdLength) };
        }
        else
        {
            int refno = myRnd.nextInt(256);

            // Calculate number of SMS needed
            int nSms = myUdLength / nMaxConcatChars;
            if ( (myUdLength % nMaxConcatChars) > 0 )
            {
                nSms += 1;
            }
            smsPdus = new SmsPdu[nSms];

            // Calculate number of UDHI
            SmsUdhElement[] pduUdhElements = null;
            if (myUdhElements == null)
            {
                pduUdhElements = new SmsUdhElement[1];
            }
            else
            {
                pduUdhElements = new SmsUdhElement[myUdhElements.length + 1];
            }

            // Copy the UDH headers
            for (int j=0; j < myUdhElements.length; j++)
            {
                // Leave position pduUdhElements[0] for the concat UDHI
                pduUdhElements[j + 1] = myUdhElements[j];
            }

            // Create pdus
            for (int i=0; i < nSms; i++)
            {
                byte pduUd[];
                int udBytes;
                int udLength;
                int udOffset;

                // Create concat header
                pduUdhElements[0] = SmsUdhUtil.get8BitConcatUdh(refno, nSms, i + 1);

                // Create
                // Must concatenate messages
                // Calc pdu length
                udOffset = nMaxChars * i;
                udBytes = myUd.length - udOffset;
                if (udBytes > nMaxChars)
                {
                    udBytes = nMaxChars;
                }
                udLength = udBytes;

                pduUd = new byte[udBytes];
                SmsPduUtil.arrayCopy(myUd, udOffset, pduUd, 0, udBytes);
                smsPdus[i] = new SmsPdu(pduUdhElements, pduUd, udLength);
            }
        }
        return smsPdus;
    }

    private SmsPdu[] createUnicodePdus(int theMaxBytes)
    {
        int nMaxChars;
        int nMaxConcatChars;
        byte pduUd[] = null;

        // 8-bit concat header is 6 bytes...
        nMaxConcatChars = (theMaxBytes - 6) / 2;
        nMaxChars = theMaxBytes / 2;

        if (myUdLength <= theMaxBytes)
        {
            return new SmsPdu[] { new SmsPdu(myUdhElements, myUd, myUdLength) };
        }

        // Must concatenate messages
        return null;
    }

    private SmsPdu[] createSeptetPdus(int theMaxBytes)
    {
        int nMaxChars;
        int nMaxConcatChars;
        byte pduUd[] = null;

        // 8-bit concat header is 6 bytes...
        nMaxConcatChars = ((theMaxBytes - 6) * 8) / 7;
        nMaxChars = (theMaxBytes * 8) / 7;

        if (myUdLength <= nMaxChars)
        {
            return new SmsPdu[] { new SmsPdu(myUdhElements, myUd, myUdLength) };
        }

        // Must concatenate messages
        return null;
    }
/*
    private SmsPdu[] createPdus(int theMaxBytes)
    {
        SmsPdu smsPdus[] = null;
        if (myUdLength <= nMaxChars)
        {
            smsPdus = new SmsPdu[] { new SmsPdu(myUdhElements, myUd, myUdLength) };
        }
        else
        {
            int refno = myRnd.nextInt(256);
            int nSms = myUdLength / nMaxConcatChars;
            if ( (myUdLength % nMaxConcatChars) > 0 )
            {
                nSms += 1;
            }

            smsPdus = new SmsPdu[nSms];

            for(int i=0; i < nSms; i++)
            {
                int msgStart = ((nMaxConcatChars * i) * nBitsPerChar) / 8;
                int msgBitOffset = ((nMaxConcatChars * i) * nBitsPerChar) % 8;
                int msgLength = nMaxConcatChars;

                if (msgLength > (udLength - (nMaxConcatChars * i)))
                {
                    msgLength = udLength - (
                }

                int msgBitLength = msgLength * nBitsPerChar;
                int msgByteLength = msgBitLength / 8;

                if (msgBitLength % 8 > 0)
                {
                    msgByteLength += 1;
                }

                byte pduUd[] = null;

                SmsUdhElement[] pduUdhElements = new SmsUdhElement[myUdhElements.length + 1];

                // Add a concat header
                pduUdhElements[0] = SmsUdhUtil.get8BitConcatUdh(refno, nSms, i + 1);
                for (int j=0; j < myUdhElements.length; j++)
                {
                    pduUdhElements[j + 1] = myUdhElements[j];
                }

                smsPdus[i] = new SmsPdu(pduUdhElements, ud, udLength);
                createPdu(pduUdhElements, msgOffset, maxConcatLength);
            }
        }

        return smsPdus;
    }

    private SmsPdu createPdu(SmsUdhElement[] theUdhElements, int theOffset, int theMaxLength)
    {
        byte pduUd[] = null;
        int udBytes;
        int udLength = 0;

        // Copy data to pduUd
        switch (SmsDcsUtil.getAlphabet(getDataCodingScheme()))
        {
        case SmsConstants.ALPHABET_GSM:
            break;
        case SmsConstants.ALPHABET_8BIT:
        case SmsConstants.ALPHABET_UCS2:
            // Calc pdu length
            udBytes = myUd.length - theOffset;
            if (udBytes > theMaxLength)
            {
                udBytes = theMaxLength;
            }
            udLength = udBytes;
            // Copy
            pduUd = new byte[udBytes];
            SmsPduUtil.arrayCopy(myUd, theOffset, pduUd, 0, udBytes);
            break;
        }

        return new SmsPdu(theUdhElements, pduUd, udLength);
    }
*/
    public SmsPdu[] getPdus()
    {
        SmsPdu[] smsPdus = null;
        int udhLength = SmsUdhUtil.getUdhLength(myUdhElements);
        int nBytesLeft = 140 - udhLength;

        switch(SmsDcsUtil.getAlphabet(myDcs))
        {
        case SmsConstants.ALPHABET_GSM:
            smsPdus = createSeptetPdus(nBytesLeft);
            break;
        case SmsConstants.ALPHABET_UCS2:
            smsPdus = createUnicodePdus(nBytesLeft);
            break;
        case SmsConstants.ALPHABET_8BIT:
        default:
            smsPdus = createOctalPdus(nBytesLeft);
            break;
        }

        return smsPdus;
    }
}
