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

    private SmsPdu[] createPdus(int theMaxLength, int theMaxConcatenatedLength)
    {
        SmsPdu smsPdus[] = null;

        if (myUdLength <= theMaxLength)
        {
            smsPdus = new SmsPdu[] { new SmsPdu(myUdhElements, myUd, myUdLength) };
        }
        else
        {
            int refno = myRnd.nextInt(256);
            int nSms = myUdLength / theMaxConcatenatedLength;
            if ( (myUdLength % theMaxConcatenatedLength) > 0 )
            {
                nSms += 1;
            }

            smsPdus = new SmsPdu[nSms];

            for(int i=0; i < nSms; i++)
            {
                int msgOffset = theMaxConcatenatedLength * i;
                SmsUdhElement[] pduUdhElements = new SmsUdhElement[myUdhElements.length + 1];

                // Add a concat header
                pduUdhElements[0] = SmsUdhUtil.get8BitConcatUdh(refno, nSms, i);
                for (int j=0; j < myUdhElements.length; j++)
                {
                    pduUdhElements[j+1] = myUdhElements[j];
                }

                smsPdus[i] = createPdu(pduUdhElements, msgOffset, theMaxConcatenatedLength);
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
            /** @todo FIXME */
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

    public SmsPdu[] getPdus()
    {
        SmsPdu[] smsPdus = null;
        int udhLength = SmsUdhUtil.getUdhLength(myUdhElements);
        int concatUdhLength = 6;

        switch(SmsDcsUtil.getAlphabet(myDcs))
        {
        case SmsConstants.ALPHABET_GSM:
            smsPdus = createPdus(160, 153);
            break;
        case SmsConstants.ALPHABET_8BIT:
            smsPdus = createPdus(140 - udhLength, 140 - udhLength - concatUdhLength);
            break;
        case SmsConstants.ALPHABET_UCS2:
//            smsPdus = createPdus(70, 67);
            smsPdus = createPdus(140 - udhLength, 140 - udhLength - concatUdhLength);
            break;
        }

        return smsPdus;
    }
}
