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
package org.marre.wap.wbxml;

import org.marre.util.StringUtil;
import org.marre.wsp.WspConstants;
import org.marre.wsp.WspUtil;
import org.marre.xml.XmlAttribute;
import org.marre.xml.XmlWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hanwen
 */
public class WbxmlWriter implements XmlWriter, AutoCloseable {

  private final Map<String, Integer> stringTable = new HashMap<>();

  private final ByteArrayOutputStream stringTableBuf = new ByteArrayOutputStream();

  private final ByteArrayOutputStream wbxmlBody = new ByteArrayOutputStream();

  private String[] tagTokens;

  private String[] attrStartTokens;

  private String[] attrValueTokens;

  private String publicId;

  public WbxmlWriter(String[] tagTokens, String[] attrStartTokens, String[] attrValueTokens) {
    setTagTokens(tagTokens);
    setAttrStartTokens(attrStartTokens);
    setAttrValueTokens(attrValueTokens);
  }

  /**
   * Writes the wbxml to stream.
   *
   * @param os
   * @throws IOException
   */
  @Override
  public void writeTo(OutputStream os) throws IOException {
    // WBXML v 0.1
    WspUtil.writeUint8(os, 0x01);
    // Public ID
    writePublicIdentifier(os, publicId);
    // Charset - "UTF-8"
    WspUtil.writeUintvar(os, WspConstants.MIB_ENUM_UTF_8);
    // String table
    writeStringTable(os);

    // Write body
    wbxmlBody.writeTo(os);
  }

  /////// XmlWriter

  @Override
  public void setDoctype(String publicID) {
    this.publicId = publicID;
  }

  @Override
  public void addStartElement(String tag) throws IOException {
    int tagIndex = StringUtil.findString(tagTokens, tag);
    if (tagIndex >= 0) {
      // Known tag
      // Tag token table starts at #5
      tagIndex += 0x05;
      wbxmlBody.write(WbxmlConstants.TOKEN_KNOWN_C | tagIndex);
    } else {
      // Unknown. Add as literal
      wbxmlBody.write(WbxmlConstants.TOKEN_LITERAL_C);
      writeStrT(wbxmlBody, tag);
    }
  }

  @Override
  public void addStartElement(String tag, XmlAttribute[] attribs) throws IOException {
    int tagIndex = StringUtil.findString(tagTokens, tag);
    if (tagIndex >= 0) {
      // Known tag
      // Tag token table starts at #5
      tagIndex += 0x05;
      wbxmlBody.write(WbxmlConstants.TOKEN_KNOWN_AC | tagIndex);
    } else if (tag != null) {
      // Unknown. Add as literal (Liquidterm: only if not null)
      wbxmlBody.write(WbxmlConstants.TOKEN_LITERAL_AC);
      writeStrT(wbxmlBody, tag);
    }

    // Write attributes
    writeAttributes(wbxmlBody, attribs);
  }

  @Override
  public void addEmptyElement(String tag) throws IOException {
    int tagIndex = StringUtil.findString(tagTokens, tag);
    if (tagIndex >= 0) {
      // Known tag
      // Tag token table starts at #5
      tagIndex += 0x05;
      wbxmlBody.write(WbxmlConstants.TOKEN_KNOWN | tagIndex);
    } else if (tag != null) {
      // Unknown. Add as literal (Liquidterm: if not null)
      wbxmlBody.write(WbxmlConstants.TOKEN_LITERAL);
      writeStrT(wbxmlBody, tag);
    }
  }

  @Override
  public void addEmptyElement(String tag, XmlAttribute[] attribs) throws IOException {
    int tagIndex = StringUtil.findString(tagTokens, tag);

    if (tagIndex >= 0) {
      // Known tag
      // Tag token table starts at #5
      tagIndex += 0x05;
      wbxmlBody.write(WbxmlConstants.TOKEN_KNOWN_A | tagIndex);
    } else {
      // Unknown. Add as literal
      wbxmlBody.write(WbxmlConstants.TOKEN_LITERAL_A);
      writeStrT(wbxmlBody, tag);
    }

    // Add attributes
    writeAttributes(wbxmlBody, attribs);
  }

  @Override
  public void addEndElement() {
    wbxmlBody.write(WbxmlConstants.TOKEN_END);
  }

  @Override
  public void addCharacters(String str) throws IOException {
    wbxmlBody.write(WbxmlConstants.TOKEN_STR_I);
    writeStrI(wbxmlBody, str);
  }

  // WBXML specific stuff

  public void addOpaqueData(byte[] buff) throws IOException {
    addOpaqueData(buff, 0, buff.length);
  }

  public void addOpaqueData(byte[] buff, int off, int len) throws IOException {
    wbxmlBody.write(WbxmlConstants.TOKEN_OPAQ);
    WspUtil.writeUintvar(wbxmlBody, buff.length);
    wbxmlBody.write(buff, off, len);
  }

  /**
   * Sets the tag tokens.
   *
   * @param tagTokens first element in this array defines tag #5
   */
  public void setTagTokens(String[] tagTokens) {
    if (tagTokens != null) {
      this.tagTokens = new String[tagTokens.length];
      System.arraycopy(tagTokens, 0, this.tagTokens, 0, tagTokens.length);
    } else {
      this.tagTokens = null;
    }
  }

  /**
   * Sets the attribute start tokens.
   *
   * @param attrStrartTokens first element in this array defines attribute #85
   */
  public void setAttrStartTokens(String[] attrStrartTokens) {
    if (attrStrartTokens != null) {
      attrStartTokens = new String[attrStrartTokens.length];
      System.arraycopy(attrStrartTokens, 0, attrStartTokens, 0, attrStrartTokens.length);
    } else {
      attrStartTokens = null;
    }
  }

  /**
   * Sets the attribute value tokens.
   *
   * @param attrValueTokens first element in this array defines attribute #05
   */
  public void setAttrValueTokens(String[] attrValueTokens) {
    if (attrValueTokens != null) {
      this.attrValueTokens = new String[attrValueTokens.length];
      System.arraycopy(attrValueTokens, 0, this.attrValueTokens, 0, attrValueTokens.length);
    } else {
      this.attrValueTokens = null;
    }
  }

  /////////////////////////////////////////////////////////

  private void writePublicIdentifier(OutputStream os, String publicId) throws IOException {
    if (publicId == null) {
      // "Unknown or missing public identifier."
      WspUtil.writeUintvar(os, 0x01);
    } else {
      int idx = StringUtil.findString(WbxmlConstants.KNOWN_PUBLIC_DOCTYPES, publicId);
      if (idx != -1) {
        // Known ID
        // Skip 0 and 1
        idx += 2;
        WspUtil.writeUintvar(os, idx);
      } else {
        // Unknown ID, add string
        // String reference following
        WspUtil.writeUintvar(os, 0x00);
        writeStrT(os, publicId);
      }
    }
  }

  private void writeStrI(OutputStream os, String str) throws IOException {
    //Liquidterm: protection against null values
    if (str != null && str.length() > 0) {
      os.write(str.getBytes(StandardCharsets.UTF_8));
      os.write(0x00);
    }
  }

  private void writeStrT(OutputStream os, String str) throws IOException {
    Integer index = stringTable.get(str);

    if (index == null) {
      index = stringTableBuf.size();
      stringTable.put(str, index);
      writeStrI(stringTableBuf, str);
    }

    WspUtil.writeUintvar(os, index);
  }

  private void writeStringTable(OutputStream os) throws IOException {
    // Write length of string table
    WspUtil.writeUintvar(os, stringTableBuf.size());
    // Write string table
    stringTableBuf.writeTo(os);
  }

  // more efficient...
  private void writeAttributes(OutputStream os, XmlAttribute[] attrs) throws IOException {
    int idx;

    for (XmlAttribute attr : attrs) {
      // TYPE=VALUE
      String typeValue = attr.getType() + "=" + attr.getValue();
      idx = StringUtil.findString(attrStartTokens, typeValue);
      if (idx >= 0) {
        // Found a matching type-value pair
        // Attr start token table starts at #5
        idx += 0x05;
        os.write(idx);
      } else {
        // Try with separate type and values

        // TYPE
        idx = StringUtil.findString(attrStartTokens, attr.getType());
        if (idx >= 0) {
          // Attr start token table starts at #5
          idx += 0x05;
          os.write(idx);
        } else {
          os.write(WbxmlConstants.TOKEN_LITERAL);
          writeStrT(os, attr.getType());
        }

        // VALUE
        String attrValue = attr.getValue();
        if (attrValue != null && attrValue.length() > 0) {
          idx = StringUtil.findString(attrValueTokens, attrValue);
          if (idx >= 0) {
            // Attr value token table starts at 85
            idx += 0x85;
            os.write(idx);
          } else {
            os.write(WbxmlConstants.TOKEN_STR_I);
            writeStrI(os, attrValue);
          }
        }
      }
    }

    // End of attributes
    os.write(WbxmlConstants.TOKEN_END);
  }

  @Override
  public void close() throws Exception {
    stringTableBuf.close();
    wbxmlBody.close();
  }
}
