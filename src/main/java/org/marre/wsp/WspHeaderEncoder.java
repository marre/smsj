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
package org.marre.wsp;

import org.marre.mime.MimeHeader;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Markus Eriksson
 * @version $Id$
 */
public final class WspHeaderEncoder {

  private WspHeaderEncoder() {
    // Static class
  }

  /**
   * there is only some useful encoder
   *
   * @param wspEncodingVersion
   * @param os
   * @param header
   * @throws IOException
   */
  public static void writeHeader(WspEncodingVersion wspEncodingVersion, OutputStream os, MimeHeader header) throws IOException {
    String headerName = header.getName().toLowerCase();
    int headerType = WspUtil.getHeaderType(headerName);

    switch (headerType) {
      case WspConstants.HEADER_ACCEPT:
        break;
      case WspConstants.HEADER_ACCEPT_APPLICATION:
        break;
      case WspConstants.HEADER_ACCEPT_CHARSET:
        break;
      case WspConstants.HEADER_ACCEPT_ENCODING:
        break;
      case WspConstants.HEADER_ACCEPT_LANGUAGE:
        break;
      case WspConstants.HEADER_ACCEPT_RANGES:
        break;
      case WspConstants.HEADER_AGE:
        break;
      case WspConstants.HEADER_ALLOW:
        break;
      case WspConstants.HEADER_AUTHORIZATION:
        break;
      case WspConstants.HEADER_BEARER_INDICATION:
        break;
      case WspConstants.HEADER_CACHE_CONTROL:
        break;
      case WspConstants.HEADER_CONNECTION:
        break;
      case WspConstants.HEADER_CONTENT_BASE:
        break;
      case WspConstants.HEADER_CONTENT_DISPOSITION:
        break;
      case WspConstants.HEADER_CONTENT_ID:
        writeHeaderContentID(wspEncodingVersion, os, header.getValue());
        break;
      case WspConstants.HEADER_CONTENT_LANGUAGE:
        break;
      case WspConstants.HEADER_CONTENT_LENGTH:
        break;
      case WspConstants.HEADER_CONTENT_LOCATION:
        writeHeaderContentLocation(wspEncodingVersion, os, header.getValue());
        break;
      case WspConstants.HEADER_CONTENT_MD5:
        break;
      case WspConstants.HEADER_CONTENT_RANGE:
        break;
      case WspConstants.HEADER_CONTENT_TYPE:
        writeHeaderContentType(wspEncodingVersion, os, header);
        break;
      case WspConstants.HEADER_COOKIE:
        break;
      case WspConstants.HEADER_DATE:
        break;
      case WspConstants.HEADER_ENCODING_VERSION:
        break;
      case WspConstants.HEADER_ETAG:
        break;
      case WspConstants.HEADER_EXPECT:
        break;
      case WspConstants.HEADER_EXPIRES:
        break;
      case WspConstants.HEADER_FROM:
        break;
      case WspConstants.HEADER_HOST:
        break;
      case WspConstants.HEADER_IF_MATCH:
        break;
      case WspConstants.HEADER_IF_MODIFIED_SINCE:
        break;
      case WspConstants.HEADER_IF_NONE_MATCH:
        break;
      case WspConstants.HEADER_IF_RANGE:
        break;
      case WspConstants.HEADER_IF_UNMODIFIED_SINCE:
        break;
      case WspConstants.HEADER_LAST_MODIFIED:
        break;
      case WspConstants.HEADER_LOCATION:
        break;
      case WspConstants.HEADER_MAX_FORWARDS:
        break;
      case WspConstants.HEADER_PRAGMA:
        break;
      case WspConstants.HEADER_PROFILE:
        break;
      case WspConstants.HEADER_PROFILE_DIFF:
        break;
      case WspConstants.HEADER_PROFILE_WARNING:
        break;
      case WspConstants.HEADER_PROXY_AUTHENTICATE:
        break;
      case WspConstants.HEADER_PROXY_AUTHORIZATION:
        break;
      case WspConstants.HEADER_PUBLIC:
        break;
      case WspConstants.HEADER_PUSH_FLAG:
        break;
      case WspConstants.HEADER_RANGE:
        break;
      case WspConstants.HEADER_REFERER:
        break;
      case WspConstants.HEADER_RETRY_AFTER:
        break;
      case WspConstants.HEADER_SERVER:
        break;
      case WspConstants.HEADER_SET_COOKIE:
        break;
      case WspConstants.HEADER_TE:
        break;
      case WspConstants.HEADER_TRAILER:
        break;
      case WspConstants.HEADER_TRANSFER_ENCODING:
        break;
      case WspConstants.HEADER_UPGRADE:
        break;
      case WspConstants.HEADER_USER_AGENT:
        break;
      case WspConstants.HEADER_VARY:
        break;
      case WspConstants.HEADER_VIA:
        break;
      case WspConstants.HEADER_WARNING:
        break;
      case WspConstants.HEADER_WWW_AUTHENTICATE:
        break;
      case WspConstants.HEADER_X_WAP_APPLICATION_ID:
        writeHeaderXWapApplicationId(wspEncodingVersion, os, header.getValue());
        break;
      case WspConstants.HEADER_X_WAP_CONTENT_URI:
        break;
      case WspConstants.HEADER_X_WAP_INITIATOR_URI:
        break;
      case WspConstants.HEADER_X_WAP_SECURITY:
        break;
      case WspConstants.HEADER_X_WAP_TOD:
        break;

      default:
        // Custom header
        writeCustomHeader(os, header.getName(), header.getValue());
        break;
    }
  }

  public static void writeCustomHeader(OutputStream os, String name, String value) throws IOException {
    WspUtil.writeTokenText(os, name);
    WspUtil.writeTextString(os, value);
  }

  /**
   * Writes a wsp encoded content-id header as specified in
   * WAP-230-WSP-20010705-a.pdf.
   * <p>
   * Content-ID is introduced in encoding version 1.3.
   */
  public static void writeHeaderContentID(WspEncodingVersion wspEncodingVersion, OutputStream os, String contentId) throws IOException {
    int headerId = WspUtil.getWellKnownHeaderId(wspEncodingVersion, WspConstants.HEADER_CONTENT_ID);
    if (headerId != -1) {
      WspUtil.writeShortInteger(os, headerId);
      WspUtil.writeQuotedString(os, contentId);
    } else {
      WspHeaderEncoder.writeCustomHeader(os, "Content-ID", contentId);
    }
  }

  /**
   * Writes a wsp encoded content-location header as specified in
   * WAP-230-WSP-20010705-a.pdf.
   */
  public static void writeHeaderContentLocation(WspEncodingVersion wspEncodingVersion, OutputStream os, String contentLocation) throws IOException {
    int headerId = WspUtil.getWellKnownHeaderId(wspEncodingVersion, WspConstants.HEADER_CONTENT_LOCATION);
    WspUtil.writeShortInteger(os, headerId);
    try {
      WspUtil.writeTextString(os, new URI(contentLocation).toASCIIString());
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("wrong content location");
    }
  }

  public static void writeHeaderContentType(WspEncodingVersion wspEncodingVersion, OutputStream os, String contentType) throws IOException {
    int headerId = WspUtil.getWellKnownHeaderId(wspEncodingVersion, WspConstants.HEADER_CONTENT_TYPE);
    WspUtil.writeShortInteger(os, headerId);
    WspUtil.writeContentType(wspEncodingVersion, os, contentType);
  }

  public static void writeHeaderContentType(WspEncodingVersion wspEncodingVersion, OutputStream os, MimeHeader contentType) throws IOException {
    int headerId = WspUtil.getWellKnownHeaderId(wspEncodingVersion, WspConstants.HEADER_CONTENT_TYPE);
    WspUtil.writeShortInteger(os, headerId);
    WspUtil.writeContentType(wspEncodingVersion, os, contentType);
  }

  /**
   * Writes a wsp encoded X-Wap-Application-Id header as specified in
   * WAP-230-WSP-20010705-a.pdf.
   * <p>
   * X-Wap-Application-Id is introduced in encoding version 1.2.
   */
  public static void writeHeaderXWapApplicationId(WspEncodingVersion wspEncodingVersion, OutputStream os, String appId) throws IOException {
    int wellKnownAppId = WspUtil.getWellKnownPushAppId(appId.toLowerCase());

    int headerId = WspUtil.getWellKnownHeaderId(wspEncodingVersion, WspConstants.HEADER_X_WAP_APPLICATION_ID);
    if (headerId != -1) {
      WspUtil.writeShortInteger(os, headerId);
      if (wellKnownAppId == -1) {
        WspUtil.writeTextString(os, appId);
      } else {
        WspUtil.writeInteger(os, wellKnownAppId);
      }
    } else {
      writeCustomHeader(os, "X-Wap-Application-Id", appId);
    }
  }
}
