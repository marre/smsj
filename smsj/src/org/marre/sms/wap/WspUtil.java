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

/**
 *
 * @author Markus Eriksson
 * @version @version $Id$
 */
public class WspUtil
{
    private WspUtil()
    {
    }

    /**
     * Writes a "uint8" in wsp format to the given output stream.
     *
     * @param theOs Stream to write to
     * @param theValue Value to write
     */
    public static void writeUint8(OutputStream theOs, int theValue)
        throws IOException
    {
        theOs.write(theValue);
    }

    /**
     * Writes a "Uintvar" in wsp format to the given output stream.
     *
     * @param theOs Stream to write to
     * @param theValue Value to write
     */
    public static void writeUintvar(OutputStream theOs, long theValue)
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
    public static void writeLongInteger(OutputStream theOs, long theValue)
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
     * Writes an "integer" in wsp format to the given output stream.
     *
     * @param theOs
     * @param theValue
     */
    public static void writeInteger(OutputStream theOs, long theValue)
        throws IOException
    {
        if (theValue < 128)
        {
            writeShortInteger(theOs, (int)theValue);
        }
        else
        {
            writeLongInteger(theOs, theValue);
        }
    }

    /**
     * Writes a "short integer" in wsp format to the given output stream.
     *
     * @param theOs Stream to write to
     * @param theValue Value to write
     */
    public static void writeShortInteger(OutputStream theOs, int theValue)
        throws IOException
    {
        theOs.write((byte) (theValue | (byte)0x80));
    }

    /**
     * Writes an "extension media" in pdu format to the given output stream.
     * It currently only handles ASCII chars, but should be extended to
     * work with other charsets.
     *
     * @param theOs Stream to write to
     * @param theStr Text to write
     */
    public static void writeExtensionMedia(OutputStream theOs, String theStr)
        throws IOException
    {
        theOs.write(theStr.getBytes("ISO-8859-1"));
        theOs.write((byte)0x00);
    }

    public static void writeTextString(OutputStream theOs, String theStr)
        throws IOException
    {
        /*
        Text-string = [Quote] *TEXT End-of-string
        ; If the first character in the TEXT is in the range of 128-255, a Quote character must precede it.
        ; Otherwise the Quote character must be omitted. The Quote is not part of the contents.
        Quote = <Octet 127>
        End-of-string = <Octet 0>
        */

        byte strBytes[] = theStr.getBytes("ISO-8859-1");

        if ( (strBytes[0] & 0x80) > 0x00 )
        {
            theOs.write(0x7f);
        }

        theOs.write(strBytes);
        theOs.write(0x00);
    }

    /**
     * Writes a wsp encoded content-type as specified in
     * WAP-230-WSP-20010705-a.pdf.
     *
     * @param theOs
     * @param theContentType
     * @throws IOException
     */
    public static void writeContentType(OutputStream theOs, String theContentType)
        throws IOException
    {
        int wellKnownContentType = findContentType(theContentType);

        if (wellKnownContentType == -1)
        {
            writeExtensionMedia(theOs, theContentType);
        }
        else
        {
            writeShortInteger(theOs, wellKnownContentType);
        }
    }

    /**
     * Writes a wsp encoded X-Wap-Application-Id header as specified in
     * WAP-230-WSP-20010705-a.pdf.
     *
     * @param theOs
     * @param theAppId
     * @throws IOException
     */
    public static void writeWapApplicationId(OutputStream theOs, String theAppId)
        throws IOException
    {
        int wellKnownAppId = findPushAppId(theAppId);

        WspUtil.writeShortInteger(theOs, WapConstants.HEADER_ID_X_WAP_APPLICATION_ID);

        if (wellKnownAppId == -1)
        {
            writeTextString(theOs, theAppId);
        }
        else
        {
            writeInteger(theOs, wellKnownAppId);
        }
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

    /**
     * Finds an WINA "well known" number for the given push app id
     *
     * @param theContentType
     * @return WINA assigned number if found, otherwise -1
     */
    public static final int findPushAppId(String thePushAppId)
    {
        if (thePushAppId == null)
        {
            return -1;
        }

        for (int i=0; i < WapConstants.PUSH_APP_IDS.length; i++)
        {
            if (WapConstants.PUSH_APP_IDS[i].equalsIgnoreCase(thePushAppId))
            {
                // Found it
                return i;
            }
        }

        // Not found
        return -1;
    }

    /**
     * Returns a push app id from a WINA "well known" number
     *
     * @param theContentType
     * @return Content type or null if not found
     */
    public static final String findPushAppId(int thePushAppId)
    {
        try
        {
            return WapConstants.PUSH_APP_IDS[thePushAppId];
        }
        catch (IndexOutOfBoundsException ex)
        {
            return null;
        }
    }

}