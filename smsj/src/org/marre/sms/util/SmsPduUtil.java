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

public class SmsPduUtil
{
    private SmsPduUtil()
    {
    }

    public static void writeSeptets(OutputStream theOs, String theMsg)
        throws IOException
    {
        int bb = 0, bblen = 0;

        for(int i=0; i < theMsg.length(); i++)
        {
            char c = (char)(theMsg.charAt(i) & 0x7f);
            bb |= (c << bblen); // insert c to bb.
            bblen += 7;

            while(bblen >= 8)
            {
                // we have a full octet.
                char o = (char) (bb & 0xff); // take 8 bits.
                theOs.write(o);
                bb >>>= 8;
                bblen -= 8;
            } // while
        } // for

        // Write remaining byte
        if( (bblen > 0) )
        {
            theOs.write(bb);
        }
    }

    public static String readSeptets(InputStream theIs, int theLength)
        throws IOException
    {
        StringBuffer msg = new StringBuffer(160);

        int r=0, rlen=0; // ints are 32 bit long.

        // assumes even number of chars in octet string.
        while(msg.length() < theLength)
        {
            char c;
            int o;
            int olen;

            o = theIs.read();
            if (o == -1)
            {
                throw new IOException("Unexpected end of stream");
            }

            olen = 8;

            if(rlen >= 7)
            {
                // take a full char off remainder.
                c = (char) (r & 0x7f);
                r >>>= 7;
                rlen -= 7;
                msg.append(c);

                if (msg.length() == theLength)
                {
                    break;
                }

            }

            o <<= rlen; // push remainding bits from r to o.
            o |= r;
            olen += rlen;

            c = (char) (o & 0x7f); // get first 7 bits from o.
            o >>>= 7;
            olen -= 7;

            r = o; // put remainding bits from o to r.
            rlen = olen;

            msg.append(c);
        } // for

        return msg.toString();
    }

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

    public static String readBcdNumber(InputStream theIs, int theLength)
    {
        return null;
    }

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
