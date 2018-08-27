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
package com.github.xfslove.mms;

import com.github.xfslove.mime.MimeHeader;
import com.github.xfslove.wsp.WspConstants;
import com.github.xfslove.wsp.WspEncodingVersion;
import com.github.xfslove.wsp.WspUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * wap-209-mmsencapsulation-20020105-a
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public final class MmsHeaderEncoder {
  private MmsHeaderEncoder() {
  }

  /**
   * Writes a wsp encoded content-location header as specified in
   * WAP-230-WSP-20010705-a.pdf.
   *
   * @param os
   * @param contentLocation
   * @throws IOException
   */
  public static void writeHeaderContentLocation(OutputStream os, String contentLocation) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_CONTENT_LOCATION);

    try {
      WspUtil.writeTextString(os, new URI(contentLocation).toASCIIString());
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("wrong content location");
    }
  }

  public static void writeHeaderContentType(WspEncodingVersion wspEncodingVersion, OutputStream os, String contentType) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_CONTENT_TYPE);
    WspUtil.writeContentType(wspEncodingVersion, os, contentType);
  }

  public static void writeHeaderContentType(WspEncodingVersion wspEncodingVersion, OutputStream os, MimeHeader contentType) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_CONTENT_TYPE);
    WspUtil.writeContentType(wspEncodingVersion, os, contentType);
  }

  public static void writeHeaderXMmsMessageType(OutputStream os, int messageTypeId) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_MESSAGE_TYPE);
    WspUtil.writeShortInteger(os, messageTypeId);
  }

  public static void writeHeaderXMmsTransactionId(OutputStream os, String transactionId) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_TRANSACTION_ID);
    WspUtil.writeTextString(os, transactionId);
  }

  public static void writeHeaderXMmsMmsVersion(OutputStream os, WspEncodingVersion versionId) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_MMS_VERSION);
    WspUtil.writeShortInteger(os, versionId.byteValue());
  }

  public static void writeHeaderDate(OutputStream os, Date date) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_DATE);
    long time = date.getTime();
    WspUtil.writeLongInteger(os, time);
  }

  public static void writeHeaderFrom(OutputStream os, String from) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_FROM);

    if (from == null || from.length() == 0) {
      WspUtil.writeValueLength(os, 1);
      WspUtil.writeShortInteger(os, MmsConstants.FROM_INSERT_ADDRESS);
    } else {
      try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

        // Write data to baos
        WspUtil.writeShortInteger(baos, MmsConstants.FROM_ADDRESS_PRESENT);
        writeEncodedStringValue(baos, from);

        WspUtil.writeValueLength(os, baos.size());
        os.write(baos.toByteArray());
      }
    }
  }

  public static void writeHeaderSubject(OutputStream os, String subject) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_SUBJECT);
    writeEncodedStringValue(os, subject);
  }

  public static void writeHeaderTo(OutputStream os, String receiver) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_TO);
    writeEncodedStringValue(os, receiver);
  }

  public static void writeHeaderCc(OutputStream os, String receiver) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_CC);
    writeEncodedStringValue(os, receiver);
  }

  public static void writeHeaderBcc(OutputStream os, String receiver) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_BCC);
    writeEncodedStringValue(os, receiver);
  }

  public static void writeHeaderXMmsReadReply(OutputStream os, int readReplyId) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_READ_REPLY);
    WspUtil.writeShortInteger(os, readReplyId);
  }

  public static void writeHeaderXMmsPriority(OutputStream os, int priorityId) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_PRIORITY);
    WspUtil.writeShortInteger(os, priorityId);
  }

  public static void writeHeaderXMmsStatus(OutputStream os, int status) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_STATUS);
    WspUtil.writeShortInteger(os, status);
  }

  public static void writeHeaderXMmsMessageClass(OutputStream os, int messageClassId) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_MESSAGE_CLASS);
    WspUtil.writeShortInteger(os, messageClassId);
  }

  public static void writeHeaderXMmsMessageSize(OutputStream os, long messageSize) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_MESSAGE_SIZE);
    WspUtil.writeLongInteger(os, messageSize);
  }

  public static void writeHeaderXMmsExpiryAbsolute(OutputStream os, long expiry) throws IOException {
    // Expiry-value = Value-length (Absolute-token Date-value |
    // Relative-token Delta-seconds-value)
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

      // (Absolute-token Date-value)
      WspUtil.writeShortInteger(baos, MmsConstants.ABSOLUTE_TOKEN);
      WspUtil.writeLongInteger(baos, expiry);

      WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_EXPIRY);
      WspUtil.writeValueLength(os, baos.size());
      os.write(baos.toByteArray());
    }
  }

  public static void writeHeaderXMmsExpiryAbsolute(OutputStream os, Date expiry) throws IOException {
    writeHeaderXMmsExpiryAbsolute(os, expiry.getTime());
  }

  public static void writeHeaderXMmsExpiryRelative(OutputStream os, long expiry) throws IOException {
    // Expiry-value = Value-length (Absolute-token Date-value |
    // Relative-token Delta-seconds-value)
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

      WspUtil.writeShortInteger(baos, MmsConstants.RELATIVE_TOKEN);
      WspUtil.writeLongInteger(baos, expiry);

      WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_EXPIRY);
      WspUtil.writeValueLength(os, baos.size());
      os.write(baos.toByteArray());
    }
  }

  public static void writeHeaderXMmsSenderVisibility(OutputStream os, int visibilityId) throws IOException {
    WspUtil.writeShortInteger(os, MmsConstants.HEADER_ID_X_MMS_SENDER_VISIBILITY);
    WspUtil.writeShortInteger(os, visibilityId);
  }


  public static void writeEncodedStringValue(OutputStream os, String stringValue) throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      WspUtil.writeShortInteger(bos, WspConstants.MIB_ENUM_UTF_8);
      WspUtil.writeTextString(bos, stringValue);

      WspUtil.writeValueLength(os, bos.size());
      os.write(bos.toByteArray());
    }
  }
}
