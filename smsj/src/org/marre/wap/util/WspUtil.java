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
import org.marre.mime.*;
import org.marre.util.*;

/**
 *
 * @author Markus Eriksson
 * @version $Id$
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
        int nOctets = 1;
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

    public static void writeValueLength(OutputStream theOs, long theValue)
        throws IOException
    {
        // ShortLength | (Length-quote Length)

        if (theValue <= 30)
        {
            // Short-length
            theOs.write((int)theValue);
        }
        else
        {
            // Length-quote == Octet 31
            theOs.write(31);
            writeUintvar(theOs, theValue);
        }
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

    public static void writeQuotedString(OutputStream theOs, String theStr)
        throws IOException
    {
        /*
        Quoted-string = <Octet 34> *TEXT End-of-string
        ;The TEXT encodes an RFC2616 Quoted-string with the enclosing quotation-marks <"> removed
        */

        // <Octet 34>
        theOs.write(34);

        theOs.write(theStr.getBytes("ISO-8859-1"));
        theOs.write(0x00);
    }

    public static void writeTokenText(OutputStream theOs, String theStr)
        throws IOException
    {
        /*
        Token-Text = Token End-of-string
        */
        // TODO: Token => RFC2616

        theOs.write(theStr.getBytes("ISO-8859-1"));
        theOs.write(0x00);
    }

    public static void writeTextValue(OutputStream theOs, String theStr)
        throws IOException
    {
        /*
        // No-value | Token-text | Quoted-string
        */
        // FIXME: Verify
        writeQuotedString(theOs, theStr);
    }

    /**
     * Writes a wsp encoded content-type as specified in
     * WAP-230-WSP-20010705-a.pdf.
     * <p>
     * Uses the "constrained media" format.<br>
     * Note! This method can only be used on simple content types (like
     * "text/plain" or "image/gif"). If a more complex content-type is needed
     * (like "image/gif; start=cid; parameter=value;") you must use the
     * MimeContentType class.
     *
     * @param theOs
     * @param theContentType
     * @throws IOException
     */
    public static void writeContentType(OutputStream theOs, String theContentType)
        throws IOException
    {
        int wellKnownContentType = StringUtil.findString(WapConstants.CONTENT_TYPES, theContentType.toLowerCase());

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
     * Writes a wsp encoded content-type as specified in
     * WAP-230-WSP-20010705-a.pdf.
     * <p>
     * This method automatically chooses the most compact way to represent
     * the given content type.
     *
     * @param theOs
     * @param theContentType
     * @throws IOException
     */
    public static void writeContentType(OutputStream theOs, MimeHeader theContentType)
        throws IOException
    {
        if (theContentType.getParamCount() == 0)
        {
            // Simple content type, use "constrained-media" format
            writeContentType(theOs, theContentType.getValue());
        }
        else
        {
            String theContentType1 = theContentType.getValue();
            // Complex, use "content-general-form"
            int wellKnownContentType = StringUtil.findString(WapConstants.CONTENT_TYPES, theContentType1.toLowerCase());

            // Create parameter byte array of
            // well-known-media (integer) or extension media
            // 0 or more parameters
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (wellKnownContentType == -1)
            {
                writeExtensionMedia(baos, theContentType.getValue());
            }
            else
            {
                // well-known-media (integer)
                writeInteger(baos, wellKnownContentType);
            }

            // Add Parameters
            for (int i=0; i < theContentType.getParamCount(); i++)
            {
                MimeHeaderParam headerParam = theContentType.getParam(i);
                writeParameter(baos, headerParam.getName(), headerParam.getValue());
            }
            baos.close();

            // Write to stream

            // content-general-form
            // value length
            writeValueLength(theOs, baos.size());
            // Write parameter byte array
            theOs.write(baos.toByteArray());
        }
    }

    public static void writeTypedValue(OutputStream os, int paramType, String value)
        throws IOException
    {
        switch (paramType)
        {
        // "Used to indicate that the parameter actually have no value,
        // eg, as the parameter "bar" in ";foo=xxx; bar; baz=xyzzy"."
        case WapConstants.WSP_TYPE_NO_VALUE:
            os.write(0x00);
            break;

        case WapConstants.WSP_TYPE_TEXT_VALUE:
            writeTextValue(os, value);
            break;

        case WapConstants.WSP_TYPE_INTEGER_VALUE:
            writeInteger(os, Long.parseLong(value));
            break;

        case WapConstants.WSP_TYPE_DATE_VALUE:
            // TODO: Implement
            /*
            ; The encoding of dates shall be done in number of seconds from
            ; 1970-01-01, 00:00:00 GMT.
            */
            writeTextString(os, value);
            break;

        case WapConstants.WSP_TYPE_DELTA_SECONDS_VALUE:
            // Integer-Value
            // TODO: Implement
            writeTextString(os, value);
            break;

        case WapConstants.WSP_TYPE_Q_VALUE:
            // TODO: Implement
            /*
            ; The encoding is the same as in Uintvar-integer, but with restricted size. When quality factor 0
            ; and quality factors with one or two decimal digits are encoded, they shall be multiplied by 100
            ; and incremented by one, so that they encode as a one-octet value in range 1-100,
            ; ie, 0.1 is encoded as 11 (0x0B) and 0.99 encoded as 100 (0x64). Three decimal quality
            ; factors shall be multiplied with 1000 and incremented by 100, and the result shall be encoded
            ; as a one-octet or two-octet uintvar, eg, 0.333 shall be encoded as 0x83 0x31.
            ; Quality factor 1 is the default value and shall never be sent.
            */
            writeTextString(os, value);
            break;

        case WapConstants.WSP_TYPE_VERSION_VALUE:
            // TODO: Implement
            /*
            ; The three most significant bits of the Short-integer value are interpreted to encode a major
            ; version number in the range 1-7, and the four least significant bits contain a minor version
            ; number in the range 0-14. If there is only a major version number, this is encoded by
            ; placing the value 15 in the four least significant bits. If the version to be encoded fits these
            ; constraints, a Short-integer must be used, otherwise a Text-string shall be used.
            */
            writeTextString(os, value);
            break;

        case WapConstants.WSP_TYPE_URI_VALUE:
            // Text-String
            // TODO: Verify
            /*
            ; URI value should be encoded per [RFC2616], but service user may use a different format.
            */
            writeTextString(os, value);
            break;

        case WapConstants.WSP_TYPE_TEXT_STRING:
            writeTextString(os, value);
            break;

        case WapConstants.WSP_TYPE_WELL_KNOWN_CHARSET:
            // Any-Charset | Integer-Value
            // ; Both are encoded using values from Character Set Assignments table in Assigned Numbers
            // TODO: Implement correctly. Currently we always say "UTF8"
            writeInteger(os, WapConstants.MIB_ENUM_UTF_8);
            break;

        case WapConstants.WSP_TYPE_FIELD_NAME:
            // Token-text | Well-known-field-name
            // TODO: Implement
            writeTextString(os, value);
            break;

        case WapConstants.WSP_TYPE_SHORT_INTEGER:
            writeShortInteger(os, Integer.parseInt(value));
            break;

        case WapConstants.WSP_TYPE_CONSTRAINED_ENCODING:
            // Constrained-Encoding == Content-type
            writeContentType(os, value);
            break;

        default:
            // TODO: Implement
            writeTextString(os, value);
            break;
        }
    }

    public static void writeParameter(OutputStream os, String name, String value)
        throws IOException
    {
        int wellKnownParameter = StringUtil.findString(WapConstants.PARAMETER_NAMES, name.toLowerCase());

        if (wellKnownParameter == -1)
        {
            // Untyped-parameter
            // Token-Text
            writeTokenText(os, name);

            // Untyped-value == Integer-Value | Text-value
            writeTextString(os, value);
        }
        else
        {
            // Typed-parameter

            // Well-known-parameter-token == Integer-value
            writeInteger(os, wellKnownParameter);
            // Typed-value
            writeTypedValue(os, WapConstants.PARAMETER_TYPES[wellKnownParameter], value);
        }
    }

    /**
     * Converts from a "multipart/" content type to "vnd.wap..." content type.
     * 
     * @param ct
     * @return
     */
    public static String convertMultipartContentType(String ct)
    {
        if (ct.equalsIgnoreCase("multipart/*"))
        {
            return "application/vnd.wap.multipart.*";
        }
        else if (ct.equalsIgnoreCase("multipart/mixed"))
        {
            return "application/vnd.wap.multipart.mixed";
        }
        else if (ct.equalsIgnoreCase("multipart/form-data"))
        {
            return "application/vnd.wap.multipart.form-data";
        }
        else if (ct.equalsIgnoreCase("multipart/byteranges"))
        {
            return "application/vnd.wap.multipart.byteranges";
        }
        else if (ct.equalsIgnoreCase("multipart/alternative"))
        {
            return "application/vnd.wap.multipart.alternative";
        }
        else if (ct.equalsIgnoreCase("multipart/related"))
        {
            return "application/vnd.wap.multipart.related";
        }
        
        return ct;
    }
}
