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
package org.marre.xml.wbxml;

import java.io.*;
import java.util.*;

import org.marre.xml.*;

import org.marre.wap.util.WspUtil;
import org.marre.wap.WapConstants;

public class WbxmlWriter implements XmlWriter
{
    private OutputStream myOs = null;

    private Map myStringTable = null;
    private ByteArrayOutputStream myStringTableBuf = null;

    private ByteArrayOutputStream myWbxmlBody = null;

    private String myTagTokens[] = null;
    private String myAttrStartTokens[] = null;
    private String myAttrValueTokens[] = null;

    private String myPublicID = null;

    public WbxmlWriter(OutputStream os)
    {
        myOs = os;

        myWbxmlBody = new ByteArrayOutputStream();

        myStringTable = new HashMap();
        myStringTableBuf = new ByteArrayOutputStream();
    }

    /**
     * Writes the wbxml to stream
     *
     * @throws SAXException
     */
    public void close()
        throws IOException
    {
        // WBXML v 0.1
        WspUtil.writeUint8(myOs, 0x01);
        // Public ID
        writePublicIdentifier(myOs, myPublicID);
        // Charset - "UTF-8"
        WspUtil.writeUintvar(myOs, WapConstants.MIB_ENUM_UTF_8);
        // String table
        writeStringTable(myOs);

        // Flush
        myOs.flush();

        // Write body
        myWbxmlBody.close();
        myWbxmlBody.writeTo(myOs);

        myOs.flush();
        myOs.close();
        myOs = null;
    }

    /////// XmlWriter

    public void setDoctype(String name, String systemURI)
    {
        // Not sure what to do here
    }

    public void setDoctype(String name, String publicID, String publicURI)
    {
        myPublicID = publicID;
    }

    public void addStartElement(String tag)
        throws IOException
    {
        int tagIndex = WspUtil.findString(myTagTokens, tag);
        if (tagIndex >= 0)
        {
            // Known tag
            tagIndex += 0x05; // Tag token table starts at #5
            myWbxmlBody.write(WbxmlConstants.TOKEN_KNOWN_C | tagIndex);
        }
        else
        {
            // Unknown. Add as literal
            myWbxmlBody.write(WbxmlConstants.TOKEN_LITERAL_C);
            writeStrT(myWbxmlBody, tag);
        }
    }

    public void addStartElement(String tag, XmlAttribute[] attribs)
        throws IOException
    {
        int tagIndex = WspUtil.findString(myTagTokens, tag);
        if (tagIndex >= 0)
        {
            // Known tag
            tagIndex += 0x05; // Tag token table starts at #5
            myWbxmlBody.write(WbxmlConstants.TOKEN_KNOWN_AC | tagIndex);
        }
        else
        {
            // Unknown. Add as literal
            myWbxmlBody.write(WbxmlConstants.TOKEN_LITERAL_AC);
            writeStrT(myWbxmlBody, tag);
        }

        // Write attributes
        writeAttributes(myWbxmlBody, attribs);
    }

    public void addEmptyElement(String tag)
        throws IOException
    {
        int tagIndex = WspUtil.findString(myTagTokens, tag);
        if (tagIndex >= 0)
        {
            // Known tag
            tagIndex += 0x05; // Tag token table starts at #5
            myWbxmlBody.write(WbxmlConstants.TOKEN_KNOWN | tagIndex);
        }
        else
        {
            // Unknown. Add as literal
            myWbxmlBody.write(WbxmlConstants.TOKEN_LITERAL);
            writeStrT(myWbxmlBody, tag);
        }
    }

    public void addEmptyElement(String tag, XmlAttribute[] attribs)
        throws IOException
    {
        int tagIndex = WspUtil.findString(myTagTokens, tag);

        if (tagIndex >= 0)
        {
            // Known tag
            tagIndex += 0x05; // Tag token table starts at #5
            myWbxmlBody.write(WbxmlConstants.TOKEN_KNOWN_A | tagIndex);
        }
        else
        {
            // Unknown. Add as literal
            myWbxmlBody.write(WbxmlConstants.TOKEN_LITERAL_A);
            writeStrT(myWbxmlBody, tag);
        }

        // Add attributes
        writeAttributes(myWbxmlBody, attribs);
    }

    public void addEndTag()
        throws IOException
    {
        myWbxmlBody.write(WbxmlConstants.TOKEN_END);
    }

    public void addCharacters(char[] ch, int start, int length)
        throws IOException
    {
        addCharacters(new String(ch, start, length));
    }

    public void addCharacters(String str)
        throws IOException
    {
        myWbxmlBody.write(WbxmlConstants.TOKEN_STR_I);
        writeStrI(myWbxmlBody, str);
    }

    // WBXML specific stuff

    public void addOpaqueData(byte[] buff)
        throws IOException
    {
        addOpaqueData(buff, 0, buff.length);
    }

    public void addOpaqueData(byte[] buff, int off, int len)
        throws IOException
    {
        myWbxmlBody.write(WbxmlConstants.TOKEN_OPAQ);
        WspUtil.writeUintvar(myWbxmlBody, buff.length);
        myWbxmlBody.write(buff, off, len);
    }

    /**
     * Sets the tag tokens
     *
     * @param theTagTokens first element in this array defines tag #5
     */
    public void setTagTokens(String [] theTagTokens)
    {
        myTagTokens = new String[theTagTokens.length];
        System.arraycopy(theTagTokens, 0, myTagTokens, 0, theTagTokens.length);
    }

    /**
     * Sets the attribute start tokens
     *
     * @param theAttrStrartTokens first element in this array defines attribute #85
     */
    public void setAttrStartTokens(String [] theAttrStrartTokens)
    {
        myAttrStartTokens = new String[theAttrStrartTokens.length];
        System.arraycopy(theAttrStrartTokens, 0, myAttrStartTokens, 0, theAttrStrartTokens.length);
    }

    /**
     * Sets the attribute value tokens
     *
     * @param theAttrStrartTokens first element in this array defines attribute #05
     */
    public void setAttrValueTokens(String [] theAttrValueTokens)
    {
        myAttrValueTokens = new String[theAttrValueTokens.length];
        System.arraycopy(theAttrValueTokens, 0, myAttrValueTokens, 0, theAttrValueTokens.length);
    }

    /////////////////////////////////////////////////////////

    private void writePublicIdentifier(OutputStream os, String publicID)
        throws IOException
    {
        if (publicID == null)
        {
            // "Unknown or missing public identifier."
            WspUtil.writeUintvar(os, 0x01);
        }
        else
        {
            int idx = WspUtil.findString(WbxmlConstants.KNOWN_PUBLIC_DOCTYPES, publicID);
            if (idx != -1)
            {
                // Known ID 
                idx += 2; // Skip 0 and 1
                WspUtil.writeUintvar(os, idx);
            }
            else
            {
                // Unknown ID, add string
                WspUtil.writeUintvar(os, 0x00); // String reference following
                writeStrT(os, publicID);
            }
        }
    }

    private void writeStrI(OutputStream theOs, String str)
        throws IOException
    {
        theOs.write(str.getBytes("UTF-8"));
        theOs.write(0x00);
    }

    private void writeStrT(OutputStream theOs, String str)
        throws IOException
    {
        Integer index = (Integer) myStringTable.get(str);

        if (index == null)
        {
            index = new Integer(myStringTableBuf.size());
            myStringTable.put(str, index);
            writeStrI(myStringTableBuf, str);
        }

        WspUtil.writeUintvar(theOs, index.intValue());
    }

    private void writeStringTable(OutputStream theOs)
        throws IOException
    {
        // Write length of string table
        WspUtil.writeUintvar(theOs, myStringTableBuf.size());
        // Write string table
        myStringTableBuf.writeTo(theOs);
    }

    // FIXME: Unsure how to do this stuff with the attributes
    // more efficient...
    private void writeAttributes(OutputStream os, XmlAttribute[] attribs)
        throws IOException
    {
        int idx;

        for (int i = 0; i < attribs.length; i++)
        {
            // TYPE
            idx = WspUtil.findString(myAttrStartTokens, attribs[i].getType());
            if (idx >= 0)
            {
                idx += 0x05; // Attr start token table starts at #5
                myWbxmlBody.write(idx);
            }
            else
            {
                myWbxmlBody.write(WbxmlConstants.TOKEN_LITERAL);
                writeStrT(myWbxmlBody, attribs[i].getType());
            }

            // VALUE
            idx = WspUtil.findString(myAttrValueTokens, attribs[i].getValue());
            if(idx >= 0) {
                idx += 0x85; // Attr value token table starts at 85
                myWbxmlBody.write (idx);
            } else {
                myWbxmlBody.write(WbxmlConstants.TOKEN_STR_I);
                writeStrI(myWbxmlBody, attribs[i].getValue());
            }
        }

        // End of attributes
        myWbxmlBody.write(WbxmlConstants.TOKEN_END);
    }

    public static void main(String argv[])
        throws Exception
    {
        XmlWriter handler = new WbxmlWriter(new FileOutputStream("demo.wbxml"));

        handler.addStartElement("element", new XmlAttribute[] { new XmlAttribute("type1", "value1") });
        handler.addCharacters("Some text");
        handler.addEmptyElement("empty", new XmlAttribute[] { new XmlAttribute("type2", "value2") });
        handler.addStartElement("element", new XmlAttribute[] { new XmlAttribute("type3", "value3"), new XmlAttribute("type4", "value4") });
        handler.addEmptyElement("empty", new XmlAttribute[] { new XmlAttribute("type2", "value2") });
        handler.addEmptyElement("empty", new XmlAttribute[] { new XmlAttribute("type2", "value2") });
        handler.addEndTag();
        handler.addEndTag();

        handler.close();
    }
}
