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

import java.io.*;
import java.util.Random;

import org.marre.sms.util.SmsPduUtil;
import org.marre.sms.util.SmsUdhUtil;

/**
 * SmsTextMessag
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public class SmsTextMessage implements SmsMessage
{
    private static final Random myRnd = new Random();

    private String myMsg = null;
    private int myAlphabet = SmsConstants.ALPHABET_GSM;

    public SmsTextMessage(String theMsg, int theAlphabet)
    {
        myMsg = theMsg;
        myAlphabet = theAlphabet;
    }

    public SmsTextMessage(String theMsg)
    {
        this(theMsg, SmsConstants.ALPHABET_GSM);
    }

    public SmsPdu[] getPdus()
    {
        SmsPdu[] smsPdus = null;

        switch(myAlphabet)
        {
        case SmsConstants.ALPHABET_GSM:
            smsPdus = getPdus(160, 153);
            break;
        case SmsConstants.ALPHABET_8BIT:
            smsPdus = getPdus(140, 134);
            break;
        case SmsConstants.ALPHABET_UCS2:
            smsPdus = getPdus(70, 67);
            break;
        }

        return smsPdus;
    }

    private SmsPdu[] getPdus(int maxChars, int maxConcatenatedChars)
    {
        SmsPdu smsPdus[] = null;
        int msgLength = myMsg.length();

        if (msgLength <= maxChars)
        {
            SmsPdu smsPdu = new SmsPdu();
            setUserData(smsPdu, myMsg);
            smsPdus = new SmsPdu[] { smsPdu };
        }
        else
        {
            int refno = myRnd.nextInt(256);
            int nSms = msgLength / maxConcatenatedChars;
            if ( (msgLength % maxConcatenatedChars) > 0 )
            {
                nSms += 1;
            }

            smsPdus = new SmsPdu[nSms];

            for(int i=0; i < nSms; i++)
            {
                int msgStart = maxConcatenatedChars * i;
                int msgEnd = msgStart + maxConcatenatedChars;

                smsPdus[i] = new SmsPdu();
                if (msgEnd > msgLength)
                {
                    msgEnd = msgLength;
                }
                setUserData(smsPdus[i], myMsg.substring(msgStart, msgEnd));

                // Set user data header
                smsPdus[i].setUserDataHeaders(
                            new SmsUdhElement[] {
                                SmsUdhUtil.getConcatUdh(refno, nSms, i)
                            });
            }
        }

        return smsPdus;
    }

    private void setUserData(SmsPdu thePdu, String theMsg)
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(140);
            SmsDcs dcs = null;
            // udLength can be in septets or octets depending on alphabet
            int udLength = 0;

            switch (myAlphabet)
            {
            case SmsConstants.ALPHABET_GSM:
                SmsPduUtil.writeSeptets(baos, theMsg);
                // 7-bit encoding, No message class, No compression
                dcs = SmsDcs.DEFAULT_7BIT_DCS;
                udLength = theMsg.length();
                break;
            case SmsConstants.ALPHABET_8BIT:
                baos.write(theMsg.getBytes("ISO-8859-1"));
                // 8bit data encoding, No message class, No compression
                dcs = SmsDcs.DEFAULT_8BIT_DCS;
                udLength = theMsg.length();
                break;
            case SmsConstants.ALPHABET_UCS2:
                baos.write(theMsg.getBytes("UTF-16BE"));
                // 16 bit UCS2 encoding, No message class, No compression
                dcs = SmsDcs.DEFAULT_UCS2_DCS;
                udLength = theMsg.length() * 2;
                break;
            }

            baos.close();

            thePdu.setUserData(baos.toByteArray(), udLength, dcs);
        }
        catch (UnsupportedEncodingException ex)
        {
            // Shouldn't happen. According to the javadoc documentation
            // for JDK 1.3.1 the "UTF-16BE" and "ISO-8859-1" encoding
            // are standard...
            throw new RuntimeException(ex.getMessage());
        }
        catch (IOException ex)
        {
            // Shouldnt really happen. We were writing to an internal
            // stream.
            throw new RuntimeException(ex.getMessage());
        }
    }
}
