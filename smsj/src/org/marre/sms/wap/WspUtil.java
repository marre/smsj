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
package org.marre.sms.wap;

import java.io.*;

public class WspUtil
{
    private WspUtil()
    {
    }

    /**
     * Writes a "Uintvar" in wsp format to the given output stream.
     *
     * @param theOs Stream to write to
     * @param theValue Value to write
     */
    private void writeUintvar(OutputStream theOs, long theValue)
        throws IOException
    {
        int nOctets = 0;
        while ((theValue >> (7*nOctets)) > 0)
        {
            nOctets++;
        }

        for (int i=nOctets; i > 0; i--)
        {
            byte octet = (byte)(theValue >> (7*(i-1)));
            byte byteValue = (byte) ((byte)octet & (byte)0x7f);
            if (i > 1)
            {
                byteValue = (byte) (byteValue | (byte)0x80);
            }
            theOs.write(byteValue);
        }
    }

    /**
     * Writes a "long integer" in wsp format to the given output stream.
     *
     * @param theOs Stream to write to
     * @param theValue Value to write
     */
    private void writeLongInteger(OutputStream theOs, long theValue)
        throws IOException
    {
        int nOctets = 0;
        while ((theValue >> (8*nOctets)) > 0)
        {
            nOctets++;
        }
        theOs.write((byte)nOctets);

        for (int i=nOctets; i > 0; i--)
        {
            byte octet = (byte)(theValue >> (8*(i-1)));
            byte byteValue = (byte) ((byte)octet & (byte)(0xff));
            theOs.write(byteValue);
        }
    }

    /**
     * Writes an "encoded string" in pdu format to the given output stream.
     * It currently only handles ASCII chars, but should be extended to
     * work with other charsets.
     *
     * @param theOs Stream to write to
     * @param theStr Text to write
     */
    private void writeEncodedString(OutputStream theOs, String theStr)
        throws IOException
    {
        // TODO: Add support for other charsets
        theOs.write(theStr.getBytes("US-ASCII"));
        theOs.write((byte)0x00);
    }

    /**
     * Finds an WINA assigned number for the given contenttype.
     *
     * @param theContentType
     * @return WINA assigned number if found, otherwise -1
     */
    public static final int findContentType(String theContentType)
    {
        if (theContentType == null)
        {
            return -1;
        }

        for (int i=0; i < WapConstants.CONTENT_TYPES.length; i++)
        {
            if (WapConstants.CONTENT_TYPES[i].equalsIgnoreCase(theContentType))
            {
                // Found it
                return i;
            }
        }

        // Not found
        return -1;
    }

    /**
     * Returns a contenttype from a WINA assigned content type number
     *
     * @param theContentType
     * @return Content type or null if not found
     */
    public static final String findContentType(int theContentType)
    {
        try
        {
            return WapConstants.CONTENT_TYPES[theContentType];
        }
        catch (IndexOutOfBoundsException ex)
        {
            return null;
        }
    }
}