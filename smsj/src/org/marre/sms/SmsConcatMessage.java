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
import java.io.*;
import org.apache.commons.logging.*;
import org.marre.sms.util.*;

/**
 * Baseclass for messages that needs to be concatenated.
 * <p>
 * - Only usable for messages that uses the same UDH fields for all
 * message parts.<br>
 * - This class could be better written. There are several parts that are copy-
 * pasted.<br>
 * - The septet coding could be a bit optimized.<br>
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class SmsConcatMessage extends SmsAbstractMessage
{
    private static final Random myRnd = new Random();
    private static Log myLog = LogFactory.getLog(SmsConcatMessage.class);

    /** Length of myUd, can be in octets or septets */
    protected int myUdLength = 0;

    protected SmsUdhElement[] myUdhElements = null;
    protected byte myUd[] = null;

    /**
     * Creates an empty SmsConcatMessage
     */
    protected SmsConcatMessage()
    {
    }

    /**
     * Creates an SmsConcatMessage with the given DCS
     *
     * @param theDcs Data Coding Scheme to use
     */
    protected SmsConcatMessage(byte theDcs)
    {
        setDataCodingScheme(theDcs);
    }

    /**
     * Creates an SmsConcatMessage
     * <p>
     * theUdhElements should not contain any concat udh elements. These will
     * be added by SmsConcatMessage if needed.
     *
     * @param theDcs Data Coding Scheme to use
     * @param theUdhElements The UDH elements to use on all messages
     * @param theUd User Data
     * @param theUdLength The length of the User Data. Can be in septets or
     * octets depending on the DCS
     */
    public SmsConcatMessage(byte theDcs, SmsUdhElement[] theUdhElements, byte[] theUd, int theUdLength)
    {
        setDataCodingScheme(theDcs);
        setContent(theUdhElements, theUd, theUdLength);
    }

    /**
     * Set content of this message
     *
     * @param theUdhElements The UDH elements to use on all messages
     * @param theUd User Data
     * @param theUdLength The length of the User Data. Can be in septets or
     * octets depending on the DCS
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
    public byte[] getUserData()
    {
        return myUd;
    }

    /**
     * Returns the length of the user data field
     * <p>
     * This can be in characters or byte depending on the message (DCS).
     * If message is 7 bit coded the length is given in septets.
     * If 8bit or UCS2 the length is in octets.
     *
     * @return The length
     */
    public int getUserDataLength()
    {
        return myUdLength;
    }

    /**
     * Returns the whole udh as a byte array.
     * <p>
     * The returned UDH is the same as specified when the message was created.
     * No concat headers are added.
     *
     * @return the UDH elements as a byte array.
     */
    public byte[] getUserDataHeaders()
    {
        if ( myUdhElements == null)
        {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);

        try
        {
            for(int i=0; i < myUdhElements.length; i++)
            {
                myUdhElements[i].writeTo(baos);
            }
        }
        catch (IOException ioe)
        {
            // Shouldn't happen.
            myLog.fatal("Failed to write to ByteArrayOutputStream", ioe);
        }

        return baos.toByteArray();
    }

    /**
     * Returns the udh elements
     * <p>
     * The returned UDH is the same as specified when the message was created.
     * No concat headers are added.
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
        nMaxChars = theMaxBytes;

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

                // Copy the UDH headers
                for (int j=0; j < myUdhElements.length; j++)
                {
                    // Leave position pduUdhElements[0] for the concat UDHI
                    pduUdhElements[j + 1] = myUdhElements[j];
                }
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
                udOffset = nMaxConcatChars * i;
                udBytes = myUd.length - udOffset;
                if (udBytes > nMaxConcatChars)
                {
                    udBytes = nMaxConcatChars;
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
        SmsPdu smsPdus[] = null;

        // 8-bit concat header is 6 bytes...
        nMaxConcatChars = (theMaxBytes - 6) / 2;
        nMaxChars = theMaxBytes / 2;

        if (myUdLength <= nMaxChars)
        {
            smsPdus = new SmsPdu[] { new SmsPdu(myUdhElements, myUd, myUdLength) };
        }
        else
        {
            int refno = myRnd.nextInt(256);

            // Calculate number of SMS needed
            int nSms = (myUdLength/2) / nMaxConcatChars;
            if ( ((myUdLength/2) % nMaxConcatChars) > 0 )
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

                // Copy the UDH headers
                for (int j=0; j < myUdhElements.length; j++)
                {
                    // Leave position pduUdhElements[0] for the concat UDHI
                    pduUdhElements[j + 1] = myUdhElements[j];
                }
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
                udOffset = nMaxConcatChars * i;
                udLength = (myUdLength/2) - udOffset;
                if (udLength > nMaxConcatChars)
                {
                    udLength = nMaxConcatChars;
                }
                udBytes = udLength * 2;

                pduUd = new byte[udBytes];
                SmsPduUtil.arrayCopy(myUd, udOffset * 2, pduUd, 0, udBytes);
                smsPdus[i] = new SmsPdu(pduUdhElements, pduUd, udBytes);
            }
        }
        return smsPdus;
    }

    private SmsPdu[] createSeptetPdus(int theMaxBytes)
    {
        int nMaxChars;
        int nMaxConcatChars;
        SmsPdu smsPdus[] = null;

        // 8-bit concat header is 6 bytes...
        nMaxConcatChars = ((theMaxBytes - 6) * 8) / 7;
        nMaxChars = (theMaxBytes * 8) / 7;

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

                // Copy the UDH headers
                for (int j=0; j < myUdhElements.length; j++)
                {
                    // Leave position pduUdhElements[0] for the concat UDHI
                    pduUdhElements[j + 1] = myUdhElements[j];
                }
            }

            // Convert septets into a string...
            String msg = SmsPduUtil.readSeptets(myUd, myUdLength);

            // Create pdus
            for (int i=0; i < nSms; i++)
            {
                byte pduUd[];
                int udOffset;
                int udLength;

                // Create concat header
                pduUdhElements[0] = SmsUdhUtil.get8BitConcatUdh(refno, nSms, i + 1);

                // Create
                // Must concatenate messages
                // Calc pdu length
                udOffset = nMaxConcatChars * i;
                udLength = myUdLength - udOffset;
                if (udLength > nMaxConcatChars)
                {
                    udLength = nMaxConcatChars;
                }

                pduUd = SmsPduUtil.getSeptets(msg.substring(udOffset, udOffset + udLength));
                smsPdus[i] = new SmsPdu(pduUdhElements, pduUd, udLength);
            }
        }
        return smsPdus;
    }

    /**
     * Converts this message into SmsPdu:s
     * <p>
     * If the message is too long to fit in one SmsPdu the message is divided
     * into many SmsPdu:s with a 8-bit concat pdu UDH element.
     * 
     * @return Returns the message as SmsPdu:s
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

