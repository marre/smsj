/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is "SMS Library for the Java platform".
 *
 * The Initial Developer of the Original Code is Markus Eriksson.
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package org.marre.wap.util;

import java.io.*;

import org.marre.wap.*;

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
