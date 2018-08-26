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
import org.marre.mime.MimeHeaderParameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Markus Eriksson
 * @version $Id$
 */
public final class WspUtil {
  private static final Map<String, Integer> WSP_HEADERS = new HashMap<>();
  private static final Map<String, Integer> WSP_CONTENT_TYPES = new HashMap<>();
  private static final Map<String, Integer> WSP_PARAMETERS = new HashMap<>();
  private static final Map<String, Integer> WSP_PUSHAPP_TYPES = new HashMap<>();

  /* https://www.ietf.org/rfc/rfc2616.txt */

  private static byte[] RFC_EXPECT_TOKEN = {
      0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
      21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 34, 40, 41, 44, 47, 58, 59,
      60, 61, 62, 63, 64, 91, 92, 93, 123, 125, 127
  };

  /* Maps a header id to a well known id */

  private static final int[] WELL_KNOWN_HEADER_ID_WSP_11 = {
      0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
      0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
      0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1
  };

  private static final int[] WELL_KNOWN_HEADER_ID_WSP_12 = {
      0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
      0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
      0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F,
      0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, -1, -1, -1, -1, -1, -1, -1, -1,
      -1
  };

  private static final int[] WELL_KNOWN_HEADER_ID_WSP_13 = {
      0x00, 0x3B, 0x3C, 0x03, 0x04, 0x05, 0x06, 0x07, 0x3D, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
      0x3e, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
      0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F,
      0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3F, 0x40, 0x41, 0x42, 0x43,
      -1
  };

  private static final int[] WELL_KNOWN_HEADER_ID_WSP_14 = {
      0x00, 0x3B, 0x3C, 0x03, 0x04, 0x05, 0x06, 0x07, 0x47, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
      0x3e, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
      0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x45, 0x2F,
      0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x44, 0x38, 0x39, 0x3A, 0x3F, 0x40, 0x41, 0x42, 0x43,
      0x46
  };

  /* Maps a parameter id to a well known id */

  private static final int[] WELL_KNOWN_PARAMETER_ID_WSP_11 = {
      0x00, 0x01, 0x02, 0x03, 0x05, 0x06, 0x07, 0x08, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1
  };

  private static final int[] WELL_KNOWN_PARAMETER_ID_WSP_12 = {
      0x00, 0x01, 0x02, 0x03, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1
  };

  private static final int[] WELL_KNOWN_PARAMETER_ID_WSP_13 = {
      0x00, 0x01, 0x02, 0x03, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10,
      -1, -1, -1, -1, -1, -1
  };

  private static final int[] WELL_KNOWN_PARAMETER_ID_WSP_14 = {
      0x00, 0x01, 0x02, 0x03, 0x17, 0x18, 0x07, 0x08, 0x09, 0x19, 0x1A, 0x1B, 0x1C, 0x0E, 0x1D, 0x10,
      0x11, 0x12, 0x13, 0x14, 0x15, 0x16
  };

  /* Maps a content type id to a well known content type id */

  private static final int[] WELL_KNOWN_CONTENT_TYPE_ID_WSP_11 = {
      0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10,
      0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20, 0x21,
      0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
  };

  private static final int[] WELL_KNOWN_CONTENT_TYPE_ID_WSP_12 = {
      0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10,
      0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20, 0x21,
      0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31, 0x32, 0x33, 0x34, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
  };

  private static final int[] WELL_KNOWN_CONTENT_TYPE_ID_WSP_13 = {
      0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10,
      0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20, 0x21,
      0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36,
      -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
  };

  private static final int[] WELL_KNOWN_CONTENT_TYPE_ID_WSP_14 = {
      0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10,
      0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20, 0x21,
      0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36,
      0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
  };

  private static final int[] WELL_KNOWN_CONTENT_TYPE_ID_WSP_15 = {
      0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10,
      0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20, 0x21,
      0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36,
      0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F,
      0x40, 0x41, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B
  };

  /* Maps a well known parameter id to a encode parameter type */

  private static final int[] PARAMETER_TYPES = {
      WspConstants.WSP_PARAMETER_TYPE_Q_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_WELL_KNOWN_CHARSET,
      WspConstants.WSP_PARAMETER_TYPE_VERSION_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_INTEGER_VALUE,
      -1,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
      WspConstants.WSP_PARAMETER_TYPE_FIELD_NAME,
      WspConstants.WSP_PARAMETER_TYPE_SHORT_INTEGER,

      WspConstants.WSP_PARAMETER_TYPE_CONSTRAINED_ENCODING,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_STRING,

      WspConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
      WspConstants.WSP_PARAMETER_TYPE_DELTA_SECONDS_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
      WspConstants.WSP_PARAMETER_TYPE_NO_VALUE,

      WspConstants.WSP_PARAMETER_TYPE_SHORT_INTEGER,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_DATE_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_DATE_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_DATE_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_INTEGER_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
      WspConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
  };

  static {
    // WSP 1.1
    WSP_HEADERS.put("accept", WspConstants.HEADER_ACCEPT);
    WSP_HEADERS.put("accept-charset", WspConstants.HEADER_ACCEPT_CHARSET);
    WSP_HEADERS.put("accept-encoding", WspConstants.HEADER_ACCEPT_ENCODING);
    WSP_HEADERS.put("accept-language", WspConstants.HEADER_ACCEPT_LANGUAGE);
    WSP_HEADERS.put("accept-ranges", WspConstants.HEADER_ACCEPT_RANGES);
    WSP_HEADERS.put("age", WspConstants.HEADER_AGE);
    WSP_HEADERS.put("allow", WspConstants.HEADER_ALLOW);
    WSP_HEADERS.put("authorization", WspConstants.HEADER_AUTHORIZATION);
    WSP_HEADERS.put("cache-control", WspConstants.HEADER_CACHE_CONTROL);
    WSP_HEADERS.put("connection", WspConstants.HEADER_CONNECTION);
    WSP_HEADERS.put("content-base", WspConstants.HEADER_CONTENT_BASE);
    WSP_HEADERS.put("content-encoding", WspConstants.HEADER_CONTENT_ENCODING);
    WSP_HEADERS.put("content-language", WspConstants.HEADER_CONTENT_LANGUAGE);
    WSP_HEADERS.put("content-length", WspConstants.HEADER_CONTENT_LENGTH);
    WSP_HEADERS.put("content-location", WspConstants.HEADER_CONTENT_LOCATION);
    WSP_HEADERS.put("content-md5", WspConstants.HEADER_CONTENT_MD5);
    WSP_HEADERS.put("content-range", WspConstants.HEADER_CONTENT_RANGE);
    WSP_HEADERS.put("content-type", WspConstants.HEADER_CONTENT_TYPE);
    WSP_HEADERS.put("date", WspConstants.HEADER_DATE);
    WSP_HEADERS.put("etag", WspConstants.HEADER_ETAG);
    WSP_HEADERS.put("expires", WspConstants.HEADER_EXPIRES);
    WSP_HEADERS.put("from", WspConstants.HEADER_FROM);
    WSP_HEADERS.put("host", WspConstants.HEADER_HOST);
    WSP_HEADERS.put("if-modified-since", WspConstants.HEADER_IF_MODIFIED_SINCE);
    WSP_HEADERS.put("if-match", WspConstants.HEADER_IF_MATCH);
    WSP_HEADERS.put("if-none-match", WspConstants.HEADER_IF_NONE_MATCH);
    WSP_HEADERS.put("if-range", WspConstants.HEADER_IF_RANGE);
    WSP_HEADERS.put("if-unmodified-since", WspConstants.HEADER_IF_UNMODIFIED_SINCE);
    WSP_HEADERS.put("location", WspConstants.HEADER_LOCATION);
    WSP_HEADERS.put("last-modified", WspConstants.HEADER_LAST_MODIFIED);
    WSP_HEADERS.put("max-forwards", WspConstants.HEADER_MAX_FORWARDS);
    WSP_HEADERS.put("pragma", WspConstants.HEADER_PRAGMA);
    WSP_HEADERS.put("proxy-authenticate", WspConstants.HEADER_PROXY_AUTHENTICATE);
    WSP_HEADERS.put("proxy-authorization", WspConstants.HEADER_PROXY_AUTHORIZATION);
    WSP_HEADERS.put("public", WspConstants.HEADER_PUBLIC);
    WSP_HEADERS.put("range", WspConstants.HEADER_RANGE);
    WSP_HEADERS.put("referer", WspConstants.HEADER_REFERER);
    WSP_HEADERS.put("retry-after", WspConstants.HEADER_RETRY_AFTER);
    WSP_HEADERS.put("server", WspConstants.HEADER_SERVER);
    WSP_HEADERS.put("transfer-encoding", WspConstants.HEADER_TRANSFER_ENCODING);
    WSP_HEADERS.put("upgrade", WspConstants.HEADER_UPGRADE);
    WSP_HEADERS.put("user-agent", WspConstants.HEADER_USER_AGENT);
    WSP_HEADERS.put("vary", WspConstants.HEADER_VARY);
    WSP_HEADERS.put("via", WspConstants.HEADER_VIA);
    WSP_HEADERS.put("warning", WspConstants.HEADER_WARNING);
    WSP_HEADERS.put("www-authenticate", WspConstants.HEADER_WWW_AUTHENTICATE);
    WSP_HEADERS.put("content-disposition", WspConstants.HEADER_CONTENT_DISPOSITION);

    // WSP 1.2
    WSP_HEADERS.put("accept", WspConstants.HEADER_ACCEPT);
    WSP_HEADERS.put("x-wap-application-id", WspConstants.HEADER_X_WAP_APPLICATION_ID);
    WSP_HEADERS.put("x-wap-content-uri", WspConstants.HEADER_X_WAP_CONTENT_URI);
    WSP_HEADERS.put("x-wap-initiator-uri", WspConstants.HEADER_X_WAP_INITIATOR_URI);
    WSP_HEADERS.put("bearer-indication", WspConstants.HEADER_BEARER_INDICATION);
    WSP_HEADERS.put("accept-application", WspConstants.HEADER_ACCEPT_APPLICATION);
    WSP_HEADERS.put("push-flag", WspConstants.HEADER_PUSH_FLAG);
    WSP_HEADERS.put("profile", WspConstants.HEADER_PROFILE);
    WSP_HEADERS.put("profile-diff", WspConstants.HEADER_PROFILE_DIFF);
    WSP_HEADERS.put("profile-warning", WspConstants.HEADER_PROFILE_WARNING);

    // WSP 1.3
    WSP_HEADERS.put("expect", WspConstants.HEADER_EXPECT);
    WSP_HEADERS.put("te", WspConstants.HEADER_TE);
    WSP_HEADERS.put("trailer", WspConstants.HEADER_TRAILER);
    WSP_HEADERS.put("accept-charset", WspConstants.HEADER_ACCEPT_CHARSET);
    WSP_HEADERS.put("accept-encoding", WspConstants.HEADER_ACCEPT_ENCODING);
    WSP_HEADERS.put("cache-control", WspConstants.HEADER_CACHE_CONTROL);
    WSP_HEADERS.put("content-range", WspConstants.HEADER_CONTENT_RANGE);
    WSP_HEADERS.put("x-wap-tod", WspConstants.HEADER_X_WAP_TOD);
    WSP_HEADERS.put("content-id", WspConstants.HEADER_CONTENT_ID);
    WSP_HEADERS.put("set-cookie", WspConstants.HEADER_SET_COOKIE);
    WSP_HEADERS.put("cookie", WspConstants.HEADER_COOKIE);
    WSP_HEADERS.put("encoding-version", WspConstants.HEADER_ENCODING_VERSION);

    // WSP 1.4
    WSP_HEADERS.put("profile-warning", WspConstants.HEADER_PROFILE_WARNING);
    WSP_HEADERS.put("content-disposition", WspConstants.HEADER_CONTENT_DISPOSITION);
    WSP_HEADERS.put("x-wap-security", WspConstants.HEADER_X_WAP_SECURITY);
    WSP_HEADERS.put("cache-control", WspConstants.HEADER_CACHE_CONTROL);

    // http://www.wapforum.org/wina/wsp-content-type.htm
    // WSP 1.1
    WSP_CONTENT_TYPES.put("*/*", 0x00);
    WSP_CONTENT_TYPES.put("text/*", 0x01);
    WSP_CONTENT_TYPES.put("text/html", 0x02);
    WSP_CONTENT_TYPES.put("text/plain", 0x03);
    WSP_CONTENT_TYPES.put("text/x-hdml", 0x04);
    WSP_CONTENT_TYPES.put("text/x-ttml", 0x05);
    WSP_CONTENT_TYPES.put("text/x-vCalendar", 0x06);
    WSP_CONTENT_TYPES.put("text/x-vCard", 0x07);
    WSP_CONTENT_TYPES.put("text/vnd.wap.wml", 0x08);
    WSP_CONTENT_TYPES.put("text/vnd.wap.wmlscript", 0x09);
    WSP_CONTENT_TYPES.put("text/vnd.wap.wta-event", 0x0A);
    WSP_CONTENT_TYPES.put("multipart/*", 0x0B);
    WSP_CONTENT_TYPES.put("multipart/mixed", 0x0C);
    WSP_CONTENT_TYPES.put("multipart/form-data", 0x0D);
    WSP_CONTENT_TYPES.put("multipart/byteranges", 0x0E);
    WSP_CONTENT_TYPES.put("multipart/alternative", 0x0F);
    WSP_CONTENT_TYPES.put("application/*", 0x10);
    WSP_CONTENT_TYPES.put("application/java-vm", 0x11);
    WSP_CONTENT_TYPES.put("application/x-www-form-urlencoded", 0x12);
    WSP_CONTENT_TYPES.put("application/x-hdmlc", 0x13);
    WSP_CONTENT_TYPES.put("application/vnd.wap.wmlc", 0x14);
    WSP_CONTENT_TYPES.put("application/vnd.wap.wmlscriptc", 0x15);
    WSP_CONTENT_TYPES.put("application/vnd.wap.wta-eventc", 0x16);
    WSP_CONTENT_TYPES.put("application/vnd.wap.uaprof", 0x17);
    WSP_CONTENT_TYPES.put("application/vnd.wap.wtls-ca-certificate", 0x18);
    WSP_CONTENT_TYPES.put("application/vnd.wap.wtls-user-certificate", 0x19);
    WSP_CONTENT_TYPES.put("application/x-x509-ca-cert", 0x1A);
    WSP_CONTENT_TYPES.put("application/x-x509-user-cert", 0x1B);
    WSP_CONTENT_TYPES.put("image/*", 0x1C);
    WSP_CONTENT_TYPES.put("image/gif", 0x1D);
    WSP_CONTENT_TYPES.put("image/jpeg", 0x1E);
    WSP_CONTENT_TYPES.put("image/tiff", 0x1F);
    WSP_CONTENT_TYPES.put("image/png", 0x20);
    WSP_CONTENT_TYPES.put("image/vnd.wap.wbmp", 0x21);
    WSP_CONTENT_TYPES.put("application/vnd.wap.multipart.*", 0x22);
    WSP_CONTENT_TYPES.put("application/vnd.wap.multipart.mixed", 0x23);
    WSP_CONTENT_TYPES.put("application/vnd.wap.multipart.form-data", 0x24);
    WSP_CONTENT_TYPES.put("application/vnd.wap.multipart.byteranges", 0x25);
    WSP_CONTENT_TYPES.put("application/vnd.wap.multipart.alternative", 0x26);
    WSP_CONTENT_TYPES.put("application/xml", 0x27);
    WSP_CONTENT_TYPES.put("text/xml", 0x28);
    WSP_CONTENT_TYPES.put("application/vnd.wap.wbxml", 0x29);
    WSP_CONTENT_TYPES.put("application/x-x968-cross-cert", 0x2A);
    WSP_CONTENT_TYPES.put("application/x-x968-ca-cert", 0x2B);
    WSP_CONTENT_TYPES.put("application/x-x968-user-cert", 0x2C);
    WSP_CONTENT_TYPES.put("text/vnd.wap.si", 0x2D);

    // WSP 1.2
    WSP_CONTENT_TYPES.put("application/vnd.wap.sic", 0x2E);
    WSP_CONTENT_TYPES.put("text/vnd.wap.sl", 0x2F);
    WSP_CONTENT_TYPES.put("application/vnd.wap.slc", 0x30);
    WSP_CONTENT_TYPES.put("text/vnd.wap.co", 0x31);
    WSP_CONTENT_TYPES.put("application/vnd.wap.coc", 0x32);
    WSP_CONTENT_TYPES.put("application/vnd.wap.multipart.related", 0x33);
    WSP_CONTENT_TYPES.put("application/vnd.wap.sia", 0x34);

    // WSP 1.3
    WSP_CONTENT_TYPES.put("text/vnd.wap.connectivity-xml", 0x35);
    WSP_CONTENT_TYPES.put("application/vnd.wap.connectivity-wbxml", 0x36);

    // WSP 1.4
    WSP_CONTENT_TYPES.put("application/pkcs7-mime", 0x37);
    WSP_CONTENT_TYPES.put("application/vnd.wap.hashed-certificate", 0x38);
    WSP_CONTENT_TYPES.put("application/vnd.wap.signed-certificate", 0x39);
    WSP_CONTENT_TYPES.put("application/vnd.wap.cert-response", 0x3A);
    WSP_CONTENT_TYPES.put("application/xhtml+xml", 0x3B);
    WSP_CONTENT_TYPES.put("application/wml+xml", 0x3C);
    WSP_CONTENT_TYPES.put("text/css", 0x3D);
    WSP_CONTENT_TYPES.put("application/vnd.wap.mms-message", 0x3E);
    WSP_CONTENT_TYPES.put("application/vnd.wap.rollover-certificate", 0x3F);

    // WSP 1.5
    WSP_CONTENT_TYPES.put("application/vnd.wap.locc+wbxml", 0x40);
    WSP_CONTENT_TYPES.put("application/vnd.wap.loc+xml", 0x41);
    WSP_CONTENT_TYPES.put("application/vnd.syncml.dm+wbxml", 0x42);
    WSP_CONTENT_TYPES.put("application/vnd.syncml.dm+xml", 0x43);
    WSP_CONTENT_TYPES.put("application/vnd.syncml.notification", 0x44);
    WSP_CONTENT_TYPES.put("application/vnd.wap.xhtml+xml", 0x45);
    WSP_CONTENT_TYPES.put("application/vnd.wv.csp.cir", 0x46);
    WSP_CONTENT_TYPES.put("application/vnd.oma.dd+xml", 0x47);
    WSP_CONTENT_TYPES.put("application/vnd.oma.drm.message", 0x48);
    WSP_CONTENT_TYPES.put("application/vnd.oma.drm.content", 0x49);
    WSP_CONTENT_TYPES.put("application/vnd.oma.drm.rights+xml", 0x4A);
    WSP_CONTENT_TYPES.put("application/vnd.oma.drm.rights+wbxml", 0x4B);

    // WSP 1.1
    WSP_PARAMETERS.put("q", WspConstants.PARAMETER_Q);
    WSP_PARAMETERS.put("charset", WspConstants.PARAMETER_CHARSET);
    WSP_PARAMETERS.put("level", WspConstants.PARAMETER_LEVEL);
    WSP_PARAMETERS.put("type", WspConstants.PARAMETER_TYPE);
    WSP_PARAMETERS.put("name", WspConstants.PARAMETER_NAME);
    WSP_PARAMETERS.put("filename", WspConstants.PARAMETER_FILENAME);
    WSP_PARAMETERS.put("differences", WspConstants.PARAMETER_DIFFERENCES);
    WSP_PARAMETERS.put("padding", WspConstants.PARAMETER_PADDING);

    // WSP 1.2
    WSP_PARAMETERS.put("type", WspConstants.PARAMETER_TYPE_MULTIPART_RELATED);
    WSP_PARAMETERS.put("start", WspConstants.PARAMETER_START_MULTIPART_RELATED);
    WSP_PARAMETERS.put("start-info", WspConstants.PARAMETER_START_INFO_MULTIPART_RELATED);

    // WSP 1.3
    WSP_PARAMETERS.put("comment", WspConstants.PARAMETER_COMMENT);
    WSP_PARAMETERS.put("domain", WspConstants.PARAMETER_DOMAIN);
    WSP_PARAMETERS.put("max-age", WspConstants.PARAMETER_MAX_AGE);
    WSP_PARAMETERS.put("path", WspConstants.PARAMETER_PATH);
    WSP_PARAMETERS.put("secure", WspConstants.PARAMETER_SECURE);

    // WSP 1.4
    WSP_PARAMETERS.put("sec", WspConstants.PARAMETER_SEC_CONNECTIVITY);
    WSP_PARAMETERS.put("mac", WspConstants.PARAMETER_MAC_CONNECTIVITY);
    WSP_PARAMETERS.put("creation-date", WspConstants.PARAMETER_CREATION_DATE);
    WSP_PARAMETERS.put("modification-date", WspConstants.PARAMETER_MODIFICATION_DATE);
    WSP_PARAMETERS.put("read-date", WspConstants.PARAMETER_READ_DATE);
    WSP_PARAMETERS.put("size", WspConstants.PARAMETER_SIZE);
    WSP_PARAMETERS.put("name", WspConstants.PARAMETER_NAME);
    WSP_PARAMETERS.put("filename", WspConstants.PARAMETER_FILENAME);
    WSP_PARAMETERS.put("start", WspConstants.PARAMETER_START_MULTIPART_RELATED);
    WSP_PARAMETERS.put("start-info", WspConstants.PARAMETER_START_INFO_MULTIPART_RELATED);
    WSP_PARAMETERS.put("comment", WspConstants.PARAMETER_COMMENT);
    WSP_PARAMETERS.put("domain", WspConstants.PARAMETER_DOMAIN);
    WSP_PARAMETERS.put("path", WspConstants.PARAMETER_PATH);

    // http://www.wapforum.org/wina/push-app-id.htm
    WSP_PUSHAPP_TYPES.put("x-wap-application:*", 0x00);
    WSP_PUSHAPP_TYPES.put("x-wap-application:push.sia", 0x01);
    WSP_PUSHAPP_TYPES.put("x-wap-application:wml.ua", 0x02);
    WSP_PUSHAPP_TYPES.put("x-wap-application:wta.ua", 0x03);
    WSP_PUSHAPP_TYPES.put("x-wap-application:mms.ua", 0x04);
    WSP_PUSHAPP_TYPES.put("x-wap-application:push.syncml", 0x05);
    WSP_PUSHAPP_TYPES.put("x-wap-application:loc.ua", 0x06);
    WSP_PUSHAPP_TYPES.put("x-wap-application:syncml.dm", 0x07);
    WSP_PUSHAPP_TYPES.put("x-wap-application:drm.ua", 0x08);
    WSP_PUSHAPP_TYPES.put("x-wap-application:emn.ua", 0x09);
    WSP_PUSHAPP_TYPES.put("x-wap-application:wv.ua", 0x0A);

    WSP_PUSHAPP_TYPES.put("x-wap-microsoft:localcontent.ua", 0x8000);
    WSP_PUSHAPP_TYPES.put("x-wap-microsoft:imclient.ua ", 0x8001);
    WSP_PUSHAPP_TYPES.put("x-wap-docomo:imode.mail.ua ", 0x8002);
    WSP_PUSHAPP_TYPES.put("x-wap-docomo:imode.mr.ua", 0x8003);
    WSP_PUSHAPP_TYPES.put("x-wap-docomo:imode.mf.ua", 0x8004);
    WSP_PUSHAPP_TYPES.put("x-motorola:location.ua ", 0x8005);
    WSP_PUSHAPP_TYPES.put("x-motorola:now.ua", 0x8006);
    WSP_PUSHAPP_TYPES.put("x-motorola:otaprov.ua", 0x8007);
    WSP_PUSHAPP_TYPES.put("x-motorola:browser.ua", 0x8008);
    WSP_PUSHAPP_TYPES.put("x-motorola:splash.ua", 0x8009);
    WSP_PUSHAPP_TYPES.put("x-wap-nai:mvsw.command ", 0x800B);
    WSP_PUSHAPP_TYPES.put("x-wap-openwave:iota.ua", 0x8010);
  }

  private WspUtil() {
  }

  /**
   * Converts a header name to a header type (WspConstants.HEADER_*).
   * <p>
   * The header name to be found must be in lower case (for performance reasons).
   *
   * @param headerName The name of the header.
   * @return The header type, or -1 if not found.
   */
  public static int getHeaderType(String headerName) {
    Integer headerType = WSP_HEADERS.get(headerName);

    return headerType != null ? headerType : -1;
  }

  /**
   * Converts a header type (WspConstants.HEADER_*) to a well known header id.
   *
   * @param wspEncodingVersion The requested wsp encoding version
   * @param headerType         The header type
   * @return A well known header id or -1 if not found.
   */
  public static int getWellKnownHeaderId(WspEncodingVersion wspEncodingVersion, int headerType) {
    int wellKnownHeaderId;

    switch (wspEncodingVersion) {
      case VERSION_1_1:
        wellKnownHeaderId = WELL_KNOWN_HEADER_ID_WSP_11[headerType];
        break;
      case VERSION_1_2:
        wellKnownHeaderId = WELL_KNOWN_HEADER_ID_WSP_12[headerType];
        break;

      case VERSION_1_3:
        wellKnownHeaderId = WELL_KNOWN_HEADER_ID_WSP_13[headerType];
        break;

      case VERSION_1_4:
      case VERSION_1_5:
        wellKnownHeaderId = WELL_KNOWN_HEADER_ID_WSP_14[headerType];
        break;

      default:
        wellKnownHeaderId = -1;
    }

    return wellKnownHeaderId;
  }

  /**
   * Converts a content type to a WINA "well-known" content type id.
   * <p>
   * http://www.wapforum.org/wina/wsp-content-type.htm
   *
   * @param wspEncodingVersion The requested wsp encoding version
   * @param contentType        The content type
   * @return A well known content type id or -1 if not found.
   */
  public static int getWellKnownContentTypeId(WspEncodingVersion wspEncodingVersion, int contentType) {
    int wellKnownContentTypeId = -1;

    if (contentType >= 0) {
      switch (wspEncodingVersion) {
        case VERSION_1_1:
          wellKnownContentTypeId = WELL_KNOWN_CONTENT_TYPE_ID_WSP_11[contentType];
          break;

        case VERSION_1_2:
          wellKnownContentTypeId = WELL_KNOWN_CONTENT_TYPE_ID_WSP_12[contentType];
          break;

        case VERSION_1_3:
          wellKnownContentTypeId = WELL_KNOWN_CONTENT_TYPE_ID_WSP_13[contentType];
          break;

        case VERSION_1_4:
          wellKnownContentTypeId = WELL_KNOWN_CONTENT_TYPE_ID_WSP_14[contentType];
          break;

        case VERSION_1_5:
          wellKnownContentTypeId = WELL_KNOWN_CONTENT_TYPE_ID_WSP_15[contentType];
          break;

        default:
      }
    }

    return wellKnownContentTypeId;
  }

  /**
   * @param contentTypeName The name of content type
   * @return The content type, or -1 if not found.
   */
  public static int getContentType(String contentTypeName) {
    Integer contentType = WSP_CONTENT_TYPES.get(contentTypeName);

    return contentType != null ? contentType : -1;
  }

  /**
   * Converts a parameter name to a parameter type (WspConstants.PARAMETER_*).
   * <p>
   * The header name to be found must be in lower case (for performance reasons).
   *
   * @param parameterName The name of the parameter.
   * @return The parameter type, or -1 if not found.
   */
  public static int getParameterType(String parameterName) {
    Integer parameterType = WSP_PARAMETERS.get(parameterName);

    return parameterType != null ? parameterType : -1;
  }

  /**
   * Converts a parameter name to a parameter type (WspConstants.WSP_PARAMETER_TYPE_*).
   *
   * @param wellKnownParameterId The well known parameter id to lookup.
   * @return The parameter type, or -1 if not found.
   */
  public static int getWspParameterType(int wellKnownParameterId) {
    return PARAMETER_TYPES[wellKnownParameterId];
  }

  /**
   * Converts a parameter type (WspConstants.PARAMETER_*) to a well known parameter id.
   *
   * @param wspEncodingVersion The requested wsp encoding version
   * @param parameterType      The header type
   * @return A well known parameter id or -1 if not found.
   */
  public static int getWellKnownParameterId(WspEncodingVersion wspEncodingVersion, int parameterType) {
    int wellKnownParameterId = -1;

    if (parameterType >= 0) {
      switch (wspEncodingVersion) {
        case VERSION_1_1:
          wellKnownParameterId = WELL_KNOWN_PARAMETER_ID_WSP_11[parameterType];
          break;
        case VERSION_1_2:
          wellKnownParameterId = WELL_KNOWN_PARAMETER_ID_WSP_12[parameterType];
          break;

        case VERSION_1_3:
          wellKnownParameterId = WELL_KNOWN_PARAMETER_ID_WSP_13[parameterType];
          break;

        case VERSION_1_4:
        case VERSION_1_5:
          wellKnownParameterId = WELL_KNOWN_PARAMETER_ID_WSP_14[parameterType];
          break;

        default:
      }
    }

    return wellKnownParameterId;
  }

  /**
   * Converts a push app to a WINA "well-known" push app id.
   * <p>
   * http://www.wapforum.org/wina/push-app-id.htm
   *
   * @param pushApp The push app
   * @return A well known push app id or -1 if not found.
   */
  public static int getWellKnownPushAppId(String pushApp) {
    Integer pushAppIdInt = WSP_PUSHAPP_TYPES.get(pushApp);

    if (pushAppIdInt == null) {
      return -1;
    }

    return pushAppIdInt;
  }

  /**
   * Writes a "uint8" in wsp format to the given output stream.
   *
   * @param theOs    Stream to write to
   * @param theValue Value to write
   */
  public static void writeUint8(OutputStream theOs, int theValue) throws IOException {
    theOs.write(theValue);
  }

  /**
   * Writes a "Uintvar" in wsp format to the given output stream.
   *
   * @param theOs    Stream to write to
   * @param theValue Value to write
   */
  public static void writeUintvar(OutputStream theOs, long theValue) throws IOException {
    int nOctets = 1;
    while ((theValue >> (7 * nOctets)) > 0) {
      nOctets++;
    }

    for (int i = nOctets; i > 0; i--) {
      byte octet = (byte) (theValue >> (7 * (i - 1)));
      byte byteValue = (byte) (octet & (byte) 0x7f);
      if (i > 1) {
        byteValue = (byte) (byteValue | (byte) 0x80);
      }
      theOs.write(byteValue);
    }
  }

  /**
   * Writes a "long integer" in wsp format to the given output stream.
   *
   * @param theOs    Stream to write to
   * @param theValue Value to write
   */
  public static void writeLongInteger(OutputStream theOs, long theValue) throws IOException {
    int nOctets = 0;
    while ((theValue >> (8 * nOctets)) > 0) {
      nOctets++;
    }
    theOs.write((byte) nOctets);

    for (int i = nOctets; i > 0; i--) {
      byte octet = (byte) (theValue >> (8 * (i - 1)));
      byte byteValue = (byte) (octet & (byte) (0xff));
      theOs.write(byteValue);
    }
  }

  /**
   * Writes an "integer" in wsp format to the given output stream.
   *
   * @param theOs    Stream to write to
   * @param theValue Value to write
   */
  public static void writeInteger(OutputStream theOs, long theValue) throws IOException {
    if (theValue < 128) {
      writeShortInteger(theOs, (int) theValue);
    } else {
      writeLongInteger(theOs, theValue);
    }
  }

  /**
   * Writes a "short integer" in wsp format to the given output stream.
   *
   * @param theOs    Stream to write to
   * @param theValue Value to write
   */
  public static void writeShortInteger(OutputStream theOs, int theValue) throws IOException {
    theOs.write((byte) (theValue | (byte) 0x80));
  }

  public static void writeValueLength(OutputStream theOs, long theValue) throws IOException {
    // ShortLength | (Length-quote Length)

    if (theValue <= 30) {
      // Short-length
      theOs.write((int) theValue);
    } else {
      // Length-quote == Octet 31
      theOs.write(31);
      writeUintvar(theOs, theValue);
    }
  }

  /**
   * Writes an "extension media" in pdu format to the given output stream.
   *
   * @param theOs  Stream to write to
   * @param theStr Text to write
   */
  public static void writeExtensionMedia(OutputStream theOs, String theStr) throws IOException {
    theOs.write(theStr.getBytes(StandardCharsets.ISO_8859_1));
    theOs.write((byte) 0x00);
  }

  public static void writeTextString(OutputStream theOs, String theStr) throws IOException {
    /*
     * Text-string = [Quote] *TEXT End-of-string ; If the first character in
     * the TEXT is in the range of 128-255, a Quote character must precede
     * it. ; Otherwise the Quote character must be omitted. The Quote is not
     * part of the contents. Quote = <Octet 127> End-of-string = <Octet 0>
     */

    byte[] strBytes = theStr.getBytes(StandardCharsets.ISO_8859_1);

    if ((strBytes[0] & 0x80) > 0x00) {
      theOs.write(127);
    }

    theOs.write(strBytes);
    theOs.write(0x00);
  }

  public static void writeQuotedString(OutputStream theOs, String theStr) throws IOException {
    /*
     * Quoted-string = <Octet 34> *TEXT End-of-string ;The TEXT encodes an
     * RFC2616 Quoted-string with the enclosing quotation-marks <"> removed
     */

    // <Octet 34>
    theOs.write(34);

    theOs.write(theStr.getBytes(StandardCharsets.ISO_8859_1));
    theOs.write(0x00);
  }

  public static void writeTokenText(OutputStream theOs, String theStr) throws IOException {
    /*
     * Token-Text = Token End-of-string
     *
     */

    byte[] strBytes = theStr.getBytes(StandardCharsets.ISO_8859_1);

    byte[] sorted = Arrays.copyOf(strBytes, strBytes.length);
    Arrays.sort(sorted);

    for (byte expect : RFC_EXPECT_TOKEN) {
      int index = Arrays.binarySearch(sorted, expect);
      if (index >= 0) {
        throw new IllegalArgumentException("token text should expect character: " + expect);
      }
    }

    theOs.write(strBytes);
    theOs.write(0x00);
  }

  public static void writeTextValue(OutputStream theOs, String theStr) throws IOException {
    /*
     * No-value | Token-text | Quoted-string
     */

    if (theStr == null || theStr.length() == 0) {
      writeNoValue(theOs);
      return;
    }

    try {
      writeTokenText(theOs, theStr);
    } catch (IllegalArgumentException e) {
      // eat
      writeQuotedString(theOs, theStr);
    }
  }

  public static void writeNoValue(OutputStream theOs) throws IOException {
    theOs.write(0x00);
  }

  /**
   * Writes a wsp encoded content-type as specified in
   * WAP-230-WSP-20010705-a.pdf.
   * <p>
   * Uses the "constrained media" format. <br>
   * Note! This method can only be used on simple content types (like
   * "text/plain" or "image/gif"). If a more complex content-type is needed
   * (like "image/gif; start=cid; parameter=value;") you must use the
   * MimeContentType class.
   *
   * @param theOs
   * @param theContentType
   * @throws IOException
   */
  public static void writeContentType(WspEncodingVersion wspEncodingVersion, OutputStream theOs, String theContentType) throws IOException {
    int contentType = getContentType(theContentType.toLowerCase());
    int wellKnownContentType = WspUtil.getWellKnownContentTypeId(wspEncodingVersion, contentType);

    if (wellKnownContentType == -1) {
      writeExtensionMedia(theOs, theContentType);
    } else {
      writeShortInteger(theOs, wellKnownContentType);
    }
  }

  /**
   * Writes a wsp encoded content-type as specified in
   * WAP-230-WSP-20010705-a.pdf.
   * <p>
   * This method automatically chooses the most compact way to represent the
   * given content type.
   *
   * @param theOs
   * @param theContentType
   * @throws IOException
   */
  public static void writeContentType(WspEncodingVersion wspEncodingVersion, OutputStream theOs, MimeHeader theContentType) throws IOException {
    if (theContentType.getParameters().isEmpty()) {
      // Simple content type, use "constrained-media" format
      writeContentType(wspEncodingVersion, theOs, theContentType.getValue());
    } else {
      String theContentTypeStr = theContentType.getValue();
      // Complex, use "content-general-form"
      int contentType = getContentType(theContentTypeStr.toLowerCase());
      int wellKnownContentType = WspUtil.getWellKnownContentTypeId(wspEncodingVersion, contentType);

      // Create parameter byte array of
      // well-known-media (integer) or extension media
      // 0 or more parameters
      try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
        if (wellKnownContentType == -1) {
          writeExtensionMedia(baos, theContentType.getValue());
        } else {
          // well-known-media (integer)
          writeInteger(baos, wellKnownContentType);
        }

        // Add Parameters
        for (MimeHeaderParameter headerParam : theContentType.getParameters()) {
          writeParameter(wspEncodingVersion, baos, headerParam.getName(), headerParam.getValue());
        }
        // Write to stream

        // content-general-form
        // value length
        writeValueLength(theOs, baos.size());
        // Write parameter byte array
        theOs.write(baos.toByteArray());
      }
    }
  }

  public static void writeTypedValue(WspEncodingVersion wspEncodingVersion, OutputStream os, int wspParamType, String value) throws IOException {
    switch (wspParamType) {
      // "Used to indicate that the parameter actually have no value,
      // eg, as the parameter "bar" in ";foo=xxx; bar; baz=xyzzy"."
      case WspConstants.WSP_PARAMETER_TYPE_NO_VALUE:
        writeNoValue(os);
        break;

      case WspConstants.WSP_PARAMETER_TYPE_TEXT_VALUE:
        writeTextValue(os, value);
        break;

      case WspConstants.WSP_PARAMETER_TYPE_INTEGER_VALUE:
        writeInteger(os, Long.parseLong(value));
        break;

      case WspConstants.WSP_PARAMETER_TYPE_DATE_VALUE:
        /*
         * ; The encoding of dates shall be done in number of seconds from ;
         * 1970-01-01, 00:00:00 GMT.
         */
        long l = Long.valueOf(value);
        writeLongInteger(os, l);
        break;

      case WspConstants.WSP_PARAMETER_TYPE_DELTA_SECONDS_VALUE:
        // Integer-Value
        int i = Integer.valueOf(value);
        writeInteger(os, i);
        break;

      case WspConstants.WSP_PARAMETER_TYPE_Q_VALUE:
        /*
         * ; The encoding is the same as in Uintvar-integer, but with
         * restricted size. When quality factor 0 ; and quality factors with
         * one or two decimal digits are encoded, they shall be multiplied
         * by 100 ; and incremented by one, so that they encode as a
         * one-octet value in range 1-100, ; ie, 0.1 is encoded as 11 (0x0B)
         * and 0.99 encoded as 100 (0x64). Three decimal quality ; factors
         * shall be multiplied with 1000 and incremented by 100, and the
         * result shall be encoded ; as a one-octet or two-octet uintvar,
         * eg, 0.333 shall be encoded as 0x83 0x31. ; Quality factor 1 is
         * the default value and shall never be sent.
         */
        try {
          BigDecimal bd = new BigDecimal(value);
          BigDecimal remainder = bd.remainder(BigDecimal.ONE);
          if (remainder.scale() <= 2) {
            BigDecimal e = remainder.multiply(BigDecimal.valueOf(100)).plus();
            writeUintvar(os, e.longValue());
          } else if (remainder.scale() == 3) {
            BigDecimal e = remainder.multiply(BigDecimal.valueOf(1000)).add(BigDecimal.valueOf(100));
            writeUintvar(os, e.longValue());
          } else {
            writeTextValue(os, value);
          }
        } catch (NumberFormatException e) {
          // eat
          writeTextValue(os, value);
        }
        break;

      case WspConstants.WSP_PARAMETER_TYPE_VERSION_VALUE:
        /*
         * ; The three most significant bits of the Short-integer value are
         * interpreted to encode a major ; version number in the range 1-7,
         * and the four least significant bits contain a minor version ;
         * number in the range 0-14. If there is only a major version
         * number, this is encoded by ; placing the value 15 in the four
         * least significant bits. If the version to be encoded fits these ;
         * constraints, a Short-integer must be used, otherwise a
         * Text-string shall be used.
         */
        try {
          WspEncodingVersion version = WspEncodingVersion.parse(value);
          writeShortInteger(os, version.byteValue());
          break;
        } catch (IllegalArgumentException e) {
          // eat
          writeTextString(os, value);
        }
        break;

      case WspConstants.WSP_PARAMETER_TYPE_URI_VALUE:
        // Text-String
        /*
         * ; URI value should be encoded per [RFC2616], but service user may
         * use a different format.
         */
        try {
          writeTextString(os, new URI(value).toASCIIString());
        } catch (URISyntaxException e) {
          // eat
          writeTextValue(os, value);
        }
        break;

      case WspConstants.WSP_PARAMETER_TYPE_TEXT_STRING:
        writeTextString(os, value);
        break;

      case WspConstants.WSP_PARAMETER_TYPE_WELL_KNOWN_CHARSET:
        // Any-Charset | Integer-Value
        // ; Both are encoded using values from Character Set Assignments
        // table in Assigned Numbers
        // Currently we always say "UTF8"
        writeInteger(os, WspConstants.MIB_ENUM_UTF_8);
        break;

      case WspConstants.WSP_PARAMETER_TYPE_FIELD_NAME:
        // Token-text | Well-known-field-name

        int headerType = getHeaderType(value);
        int wellKnownHeaderId = getWellKnownHeaderId(wspEncodingVersion, headerType);
        if (wellKnownHeaderId == -1) {
          try {
            writeTokenText(os, value);
          } catch (IllegalArgumentException e) {
            // eat
            writeTextValue(os, value);
          }
        } else {
          writeShortInteger(os, wellKnownHeaderId);
        }
        break;

      case WspConstants.WSP_PARAMETER_TYPE_SHORT_INTEGER:
        writeShortInteger(os, Integer.parseInt(value));
        break;

      case WspConstants.WSP_PARAMETER_TYPE_CONSTRAINED_ENCODING:
        // Constrained-Encoding == Content-type
        writeContentType(wspEncodingVersion, os, value);
        break;

      default:
        writeTextValue(os, value);
    }
  }

  public static void writeParameter(WspEncodingVersion wspEncodingVersion, OutputStream os, String name, String value) throws IOException {
    String theNameStr = name.toLowerCase();
    int parameterType = WspUtil.getParameterType(theNameStr);
    int wellKnownParameter = WspUtil.getWellKnownParameterId(wspEncodingVersion, parameterType);

    if (wellKnownParameter == -1) {
      // Untyped-parameter
      // Token-Text
      writeTokenText(os, name);

      // Untyped-value == Integer-Value | Text-value
      try {
        long l = Long.valueOf(value);
        writeInteger(os, l);
      } catch (NumberFormatException e) {
        // eat
        writeTextString(os, value);
      }
    } else {
      // Typed-parameter

      // Well-known-parameter-token == Integer-value
      writeInteger(os, wellKnownParameter);
      // Typed-value
      writeTypedValue(wspEncodingVersion, os, getWspParameterType(wellKnownParameter), value);
    }
  }

  /**
   * Converts from a "multipart/" content type to "vnd.wap..." content type.
   *
   * @param ct
   * @return
   */
  public static String convertMultipartContentType(String ct) {
    switch (ct) {
      case "multipart/*":
        return "application/vnd.wap.multipart.*";
      case "multipart/mixed":
        return "application/vnd.wap.multipart.mixed";
      case "multipart/form-data":
        return "application/vnd.wap.multipart.form-data";
      case "multipart/byteranges":
        return "application/vnd.wap.multipart.byteranges";
      case "multipart/alternative":
        return "application/vnd.wap.multipart.alternative";
      case "multipart/related":
        return "application/vnd.wap.multipart.related";
      default:
        return ct;
    }
  }
}
