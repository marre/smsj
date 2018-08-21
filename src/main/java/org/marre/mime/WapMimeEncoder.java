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
package org.marre.mime;

import org.marre.wsp.WspConstants;
import org.marre.wsp.WspEncodingVersion;
import org.marre.wsp.WspHeaderEncoder;
import org.marre.wsp.WspUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * Converts mime documents to a wsp encoded stream.
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class WapMimeEncoder {

  /**
   * Writes an WSP encoded content type header to the given stream.
   * <p>
   * NOTE! It only writes an WSP encoded content-type to the stream. It does
   * not add the content type header id.
   *
   * @param os  The stream to write to
   * @param msg The message to get the content-type from
   * @throws IOException Thrown if we fail to write the content-type to the stream
   */
  public static void writeContentType(WspEncodingVersion wspEncodingVersion, OutputStream os, MimeBodyPart msg) throws IOException {
    if (msg instanceof MimeMultipart) {
      String ct = msg.getContentType().getValue();

      // Convert multipart headers...
      String newCt = WspUtil.convertMultipartContentType(ct);
      msg.getContentType().setValue(newCt);
    }

    WspUtil.writeContentType(wspEncodingVersion, os, msg.getContentType());
  }

  /**
   * Writes the headers of the message to the given stream.
   *
   * @param os  The stream to write to
   * @param msg The message to get the headers from
   * @throws IOException Thrown if we fail to write the headers to the stream
   */
  public static void writeHeaders(WspEncodingVersion wspEncodingVersion, OutputStream os, MimeBodyPart msg) throws IOException {
    for (MimeHeader header : msg.getHeaders()) {
      writeHeader(wspEncodingVersion, os, header);
    }
  }

  /**
   * Writes the body of the message to the given stream.
   *
   * @param os  The stream to write to
   * @param msg The message to get the data from
   * @throws IOException Thrown if we fail to write the body to the stream
   */
  public static void writeBody(WspEncodingVersion wspEncodingVersion, OutputStream os, MimeBodyPart msg) throws IOException {
    if (msg instanceof MimeMultipart) {

      writeMultipart(wspEncodingVersion, os, (MimeMultipart) msg);
    } else {
      os.write(msg.getBody());
    }
  }

  /**
   * Section 8.5.2 in WAP-230-WSP-20010705
   */
  private static void writeMultipart(WspEncodingVersion wspEncodingVersion, OutputStream os, MimeMultipart multipart) throws IOException {
    Collection<MimeBodyPart> bodyParts = multipart.getBodyParts();

    // nEntries
    WspUtil.writeUintvar(os, bodyParts.size());

    for (MimeBodyPart part : bodyParts) {
      try (ByteArrayOutputStream headers = new ByteArrayOutputStream();
           ByteArrayOutputStream content = new ByteArrayOutputStream()) {

        // Generate content-type + headers
        writeContentType(wspEncodingVersion, headers, part);
        writeHeaders(wspEncodingVersion, headers, part);
        // Done with the headers...

        // Generate content...
        writeBody(wspEncodingVersion, content, part);

        // Write data to the os

        // Length of the content type and headers combined
        WspUtil.writeUintvar(os, headers.size());
        // Length of the data (content)
        WspUtil.writeUintvar(os, content.size());
        // Content type + headers
        os.write(headers.toByteArray());
        // Data
        os.write(content.toByteArray());
      }
    }
  }

  private static void writeHeader(WspEncodingVersion wspEncodingVersion, OutputStream os, MimeHeader header) throws IOException {
    String headerName = header.getName().toLowerCase();
    int headerType = WspUtil.getHeaderType(headerName);
    switch (headerType) {
      // not handle currently
      case WspConstants.HEADER_ACCEPT:
      case WspConstants.HEADER_ACCEPT_APPLICATION:
      case WspConstants.HEADER_ACCEPT_CHARSET:
      case WspConstants.HEADER_ACCEPT_ENCODING:
      case WspConstants.HEADER_ACCEPT_LANGUAGE:
      case WspConstants.HEADER_ACCEPT_RANGES:
      case WspConstants.HEADER_AGE:
      case WspConstants.HEADER_ALLOW:
      case WspConstants.HEADER_AUTHORIZATION:
      case WspConstants.HEADER_BEARER_INDICATION:
      case WspConstants.HEADER_CACHE_CONTROL:
      case WspConstants.HEADER_CONNECTION:
      case WspConstants.HEADER_CONTENT_BASE:
      case WspConstants.HEADER_CONTENT_DISPOSITION:
      case WspConstants.HEADER_CONTENT_LANGUAGE:
      case WspConstants.HEADER_CONTENT_LENGTH:
      case WspConstants.HEADER_CONTENT_MD5:
      case WspConstants.HEADER_CONTENT_RANGE:
      case WspConstants.HEADER_COOKIE:
      case WspConstants.HEADER_DATE:
      case WspConstants.HEADER_ENCODING_VERSION:
      case WspConstants.HEADER_ETAG:
      case WspConstants.HEADER_EXPECT:
      case WspConstants.HEADER_EXPIRES:
      case WspConstants.HEADER_FROM:
      case WspConstants.HEADER_HOST:
      case WspConstants.HEADER_IF_MATCH:
      case WspConstants.HEADER_IF_MODIFIED_SINCE:
      case WspConstants.HEADER_IF_NONE_MATCH:
      case WspConstants.HEADER_IF_RANGE:
      case WspConstants.HEADER_IF_UNMODIFIED_SINCE:
      case WspConstants.HEADER_LAST_MODIFIED:
      case WspConstants.HEADER_LOCATION:
      case WspConstants.HEADER_MAX_FORWARDS:
      case WspConstants.HEADER_PRAGMA:
      case WspConstants.HEADER_PROFILE:
      case WspConstants.HEADER_PROFILE_DIFF:
      case WspConstants.HEADER_PROFILE_WARNING:
      case WspConstants.HEADER_PROXY_AUTHENTICATE:
      case WspConstants.HEADER_PROXY_AUTHORIZATION:
      case WspConstants.HEADER_PUBLIC:
      case WspConstants.HEADER_PUSH_FLAG:
      case WspConstants.HEADER_RANGE:
      case WspConstants.HEADER_REFERER:
      case WspConstants.HEADER_RETRY_AFTER:
      case WspConstants.HEADER_SERVER:
      case WspConstants.HEADER_SET_COOKIE:
      case WspConstants.HEADER_TE:
      case WspConstants.HEADER_TRAILER:
      case WspConstants.HEADER_TRANSFER_ENCODING:
      case WspConstants.HEADER_UPGRADE:
      case WspConstants.HEADER_USER_AGENT:
      case WspConstants.HEADER_VARY:
      case WspConstants.HEADER_VIA:
      case WspConstants.HEADER_WARNING:
      case WspConstants.HEADER_WWW_AUTHENTICATE:
      case WspConstants.HEADER_X_WAP_CONTENT_URI:
      case WspConstants.HEADER_X_WAP_INITIATOR_URI:
      case WspConstants.HEADER_X_WAP_SECURITY:
      case WspConstants.HEADER_X_WAP_TOD:
        break;

      case WspConstants.HEADER_CONTENT_ID:
        WspHeaderEncoder.writeHeaderContentId(wspEncodingVersion, os, header.getValue());
        break;
      case WspConstants.HEADER_CONTENT_LOCATION:
        WspHeaderEncoder.writeHeaderContentLocation(wspEncodingVersion, os, header.getValue());
        break;
      case WspConstants.HEADER_CONTENT_TYPE:
        WspHeaderEncoder.writeHeaderContentType(wspEncodingVersion, os, header);
        break;
      case WspConstants.HEADER_X_WAP_APPLICATION_ID:
        WspHeaderEncoder.writeHeaderXWapApplicationId(wspEncodingVersion, os, header.getValue());
        break;
      default:
        WspHeaderEncoder.writeApplicationHeader(os, header.getName(), header.getValue());
    }
  }
}
