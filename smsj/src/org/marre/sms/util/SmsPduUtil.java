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

import java.util.*;
import java.io.*;

// TODO: GSM charset

/**
 * Various functions to encode and decode strings
 *
 * @author Markus Eriksson
 */
public class SmsPduUtil
{
    /**
     * This class isn't intended to be instantiated
     */
    private SmsPduUtil()
    {
    }

    /**
     * Pack the given string into septets.
     *
     * @todo Convert to GSM charset
     *
     * @param theOs Write the septets into this stream
     * @param theMsg The message to encode
     * @throws IOException Thrown when failing to write to theOs
     */
    public static void writeSeptets(OutputStream theOs, String theMsg)
        throws IOException
    {
        int data = 0;
        int nBits = 0;

        for(int i=0; i < theMsg.length(); i++)
        {
            char ch = (char)(theMsg.charAt(i) & 0x7f);
            data |= (ch << nBits);
            nBits += 7;

            while(nBits >= 8)
            {
                // Write full octet
                char octet = (char) (data & 0xff);
                theOs.write(octet);
                data >>>= 8;
                nBits -= 8;
            } // while
        } // for

        // Write remaining byte
        if( (nBits > 0) )
        {
            theOs.write(data);
        }
    }

    /**
     * Decodes a 7-bit encoded string from the stream
     *
     * @todo GSM charset
     *
     * @param theIs The stream to read from
     * @param theLength Number of decoded chars to read from the stream
     * @return The decoded string
     * @throws IOException when failing to read from theIs
     */
    public static String readSeptets(InputStream theIs, int theLength)
        throws IOException
    {
        StringBuffer msg = new StringBuffer(160);

        int rest = 0;
        int restBits = 0;

        while(msg.length() < theLength)
        {
            char ch;
            int octetBits = 8;
            int octet = theIs.read();

            if (octet == -1)
            {
                throw new IOException("Unexpected end of stream");
            }

            octet <<= restBits;
            octet |= rest;
            octetBits += restBits;

            msg.append((char) (octet & 0x7f));
            octet >>>= 7;
            octetBits -= 7;

            rest = octet;
            restBits = octetBits;

            if(restBits >= 7)
            {
                msg.append((char)(rest & 0x7f));

                rest >>>= 7;
                restBits -= 7;
            }
        } // for

        return msg.toString();
    }

    /**
     * Writes the given phonenumber to the stream (BCD coded)
     *
     * @param theOs Stream to write to
     * @param theNumber Number to convert
     * @throws IOException when failing to write to theOs
     */
    public static void writeBcdNumber(OutputStream theOs, String theNumber)
        throws IOException
    {
        int bcd = 0x00;
        int n = 0;

        // First convert to a "half octet" value
        for(int i=0; i < theNumber.length(); i++)
        {
            switch(theNumber.charAt(i))
            {
            case '0': bcd |= 0x00; break;
            case '1': bcd |= 0x01; break;
            case '2': bcd |= 0x02; break;
            case '3': bcd |= 0x03; break;
            case '4': bcd |= 0x04; break;
            case '5': bcd |= 0x05; break;
            case '6': bcd |= 0x06; break;
            case '7': bcd |= 0x07; break;
            case '8': bcd |= 0x08; break;
            case '9': bcd |= 0x09; break;
            case '*': bcd |= 0x0A; break;
            case '#': bcd |= 0x0B; break;
            case 'a': bcd |= 0x0C; break;
            case 'b': bcd |= 0x0E; break;
            }

            n++;

            if (n == 2)
            {
                theOs.write(bcd);
                n = 0;
                bcd = 0x00;
            }
            else
            {
                bcd <<= 4;
            }
        }

        if (n == 1)
        {
            bcd |= 0xF0;
            theOs.write(bcd);
        }
    }

    /**
     * Not implemented yet
     *
     * @todo Implement
     *
     * @param theIs
     * @param theLength
     * @return Decoded number
     */
    public static String readBcdNumber(InputStream theIs, int theLength)
    {
        return null;
    }

    /**
     * Conversts a byte array to a string with hex values.
     *
     * @param theData Data to convert
     * @return the encoded string
     */
    public static String bytesToHexString(byte[] theData)
    {
        StringBuffer hexStrBuff = new StringBuffer(theData.length*2);

        for(int i=0; i < theData.length; i++)
        {
            String hexByteStr = Integer.toHexString(theData[i] & 0xff).toUpperCase();
            if (hexByteStr.length() == 1)
            {
                hexStrBuff.append("0");
            }
            hexStrBuff.append(hexByteStr);
        }

        return hexStrBuff.toString();
    }

    /**
     * Converts a string of hex characters to a byte array
     *
     * @param theHexString The hex string to read
     * @return the resulting byte array
     */
    public static byte[] hexStringToBytes(String theHexString)
    {
        byte data[] = new byte[theHexString.length()/2];

        for(int i=0; i < data.length; i++)
        {
            String a = theHexString.substring(i*2, i*2+2);
            data[i] = (byte)Integer.parseInt(a, 16);
        }

        return data;
    }
}
