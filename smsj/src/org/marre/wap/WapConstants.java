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
package org.marre.wap;

public class WapConstants
{
    /**
     * WINA "well-known" push app id
     * http://www.wapforum.org/wina/push-app-id.htm
     */
    public static final String[] PUSH_APP_IDS = {
            "x-wap-application:*",
            "x-wap-application:push.sia",
            "x-wap-application:wml.ua",
            "x-wap-application:wta.ua",
            "x-wap-application:mms.ua",
            "x-wap-application:push.syncml",
            "x-wap-application:loc.ua",
            "x-wap-application:syncml.dm",
            "x-wap-application:drm.ua",
    };

    /**
     * WINA "well-known" content types
     * http://www.wapforum.org/wina/wsp-content-type.htm
     */
    public static final String[] CONTENT_TYPES = {
            "*/*",
            "text/*",
            "text/html",
            "text/plain",
            "text/x-hdml",
            "text/x-ttml",
            "text/x-vCalendar",
            "text/x-vCard",
            "text/vnd.wap.wml",
            "text/vnd.wap.wmlscript",
            "text/vnd.wap.wta-event",
            "multipart/*",
            "multipart/mixed",
            "multipart/form-data",
            "multipart/byterantes",
            "multipart/alternative",
            "application/*",
            "application/java-vm",
            "application/x-www-form-urlencoded",
            "application/x-hdmlc",
            "application/vnd.wap.wmlc",
            "application/vnd.wap.wmlscriptc",
            "application/vnd.wap.wta-eventc",
            "application/vnd.wap.uaprof",
            "application/vnd.wap.wtls-ca-certificate",
            "application/vnd.wap.wtls-user-certificate",
            "application/x-x509-ca-cert",
            "application/x-x509-user-cert",
            "image/*",
            "image/gif",
            "image/jpeg",
            "image/tiff",
            "image/png",
            "image/vnd.wap.wbmp",
            "application/vnd.wap.multipart.*",
            "application/vnd.wap.multipart.mixed",
            "application/vnd.wap.multipart.form-data",
            "application/vnd.wap.multipart.byteranges",
            "application/vnd.wap.multipart.alternative",
            "application/xml",
            "text/xml",
            "application/vnd.wap.wbxml",
            "application/x-x968-cross-cert",
            "application/x-x968-ca-cert",
            "application/x-x968-user-cert",
            "text/vnd.wap.si",
            "application/vnd.wap.sic",
            "text/vnd.wap.sl",
            "application/vnd.wap.slc",
            "text/vnd.wap.co",
            "application/vnd.wap.coc",
            "application/vnd.wap.multipart.related",
            "application/vnd.wap.sia",
            "text/vnd.wap.connectivity-xml",
            "application/vnd.wap.connectivity-wbxml",
            "application/pkcs7-mime",
            "application/vnd.wap.hashed-certificate",
            "application/vnd.wap.signed-certificate",
            "application/vnd.wap.cert-response",
            "application/xhtml+xml",
            "application/wml+xml",
            "text/css",
            "application/vnd.wap.mms-message",
            "application/vnd.wap.rollover-certificate",
            "application/vnd.wap.locc+wbxml",
            "application/vnd.wap.loc+xml",
            "application/vnd.syncml.dm+wbxml",
            "application/vnd.syncml.dm+xml",
            "application/vnd.syncml.notification",
            "application/vnd.wap.xhtml+xml",
        };

    public static final int PDU_TYPE_PUSH = 0x06;

    /*
     * IANA assigned charset values
     * http://www.iana.org/assignments/character-sets
     */
    public static final int MIB_ENUM_UTF_8 = 106;

    public static final int WSP_TYPE_NO_VALUE = 0x01;
    public static final int WSP_TYPE_TEXT_VALUE = 0x02;
    public static final int WSP_TYPE_INTEGER_VALUE = 0x03;
    public static final int WSP_TYPE_DATE_VALUE = 0x04;
    public static final int WSP_TYPE_DELTA_SECONDS_VALUE = 0x05;
    public static final int WSP_TYPE_Q_VALUE = 0x06;
    public static final int WSP_TYPE_VERSION_VALUE = 0x07;
    public static final int WSP_TYPE_URI_VALUE = 0x08;

    public static final int WSP_TYPE_TEXT_STRING = 0x09;
    public static final int WSP_TYPE_WELL_KNOWN_CHARSET = 0x0A;
    public static final int WSP_TYPE_FIELD_NAME = 0x0B;
    public static final int WSP_TYPE_SHORT_INTEGER = 0x0C;
    public static final int WSP_TYPE_CONSTRAINED_ENCODING = 0x0D;

    public static final String PARAMETER_NAMES[] = {
        "q",
        "charset",
        "level",
        "type",
        null,
        null,             // "name"         Deprecated in encoding v1.4
        null,             // "filename"     Deprecated in encoding v1.4
        "differences",
        "padding",
        "type",
        null,             // "start"        Deprecated in encoding v1.4
        null,             // "start-info"   Deprecated in encoding v1.4
        null,             // "comment"      Deprecated in encoding v1.4
        null,             // "domain"       Deprecated in encoding v1.4
        "max-age",
        null,             // "path"         Deprecated in encoding v1.4

        "secure",
        "sec",
        "mac",
        "creation-date",
        "modification-date",
        "read-date",
        "size",
        "name",
        "filename",
        "start",
        "start-info",
        "comment",
        "domain",
        "path",
    };

    public static final int PARAMETER_TYPES[] = {
        WSP_TYPE_Q_VALUE,
        WSP_TYPE_WELL_KNOWN_CHARSET,
        WSP_TYPE_VERSION_VALUE,
        WSP_TYPE_INTEGER_VALUE,
        -1,
        WSP_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        WSP_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        WSP_TYPE_FIELD_NAME,
        WSP_TYPE_SHORT_INTEGER,
        WSP_TYPE_CONSTRAINED_ENCODING,
        WSP_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        WSP_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        WSP_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        WSP_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        WSP_TYPE_DELTA_SECONDS_VALUE,
        WSP_TYPE_TEXT_STRING, // Deprecated in encoding v1.4

        WSP_TYPE_NO_VALUE,
        WSP_TYPE_SHORT_INTEGER,
        WSP_TYPE_TEXT_VALUE,
        WSP_TYPE_DATE_VALUE,
        WSP_TYPE_DATE_VALUE,
        WSP_TYPE_DATE_VALUE,
        WSP_TYPE_INTEGER_VALUE,
        WSP_TYPE_TEXT_VALUE,
        WSP_TYPE_TEXT_VALUE,
        WSP_TYPE_TEXT_VALUE,
        WSP_TYPE_TEXT_VALUE,
        WSP_TYPE_TEXT_VALUE,
        WSP_TYPE_TEXT_VALUE,
        WSP_TYPE_TEXT_VALUE,
    };
    
    public static final int HEADER_ID_ACCEPT = 0x00;
//    public static final int HEADER_ID_ACCEPT_CHARSET = 0x01;
//    public static final int HEADER_ID_ACCEPT_ENCODING = 0x02;
    public static final int HEADER_ID_ACCEPT_LANGUAGE = 0x03;
    public static final int HEADER_ID_ACCEPT_RANGES = 0x04;
    public static final int HEADER_ID_AGE = 0x05;
    public static final int HEADER_ID_ALLOW = 0x06;
    public static final int HEADER_ID_AUTHORIZATION = 0x07;
//    public static final int HEADER_ID_CACHE_CONTROL = 0x08;
    public static final int HEADER_ID_CONNECTION = 0x09;
    public static final int HEADER_ID_CONTENT_BASE = 0x0A;
    public static final int HEADER_ID_CONTENT_ENCODING = 0x0B;
    public static final int HEADER_ID_CONTENT_LANGUAGE = 0x0C;
    public static final int HEADER_ID_CONTENT_LENGTH = 0x0D;
    public static final int HEADER_ID_CONTENT_LOCATION = 0x0E;
    public static final int HEADER_ID_CONTENT_MD5 = 0x0F;
    
//    public static final int HEADER_ID_CONTENT_RANGE = 0x10;
    public static final int HEADER_ID_CONTENT_TYPE = 0x11;
    public static final int HEADER_ID_DATE = 0x12;
    public static final int HEADER_ID_ETAG = 0x13;
    public static final int HEADER_ID_EXPIRES = 0x14;
    public static final int HEADER_ID_FROM = 0x15;
    public static final int HEADER_ID_HOST = 0x16;
    public static final int HEADER_ID_IF_MODIFIED_SINCE = 0x17;
    public static final int HEADER_ID_IF_MATCH = 0x18;
    public static final int HEADER_ID_IF_NONE_MATCH = 0x19;
    public static final int HEADER_ID_IF_RANGE = 0x1A;
    public static final int HEADER_ID_IF_UNMODIFIED_SINCE = 0x1B;
    public static final int HEADER_ID_LAST_MODIFIED = 0x1C;
    public static final int HEADER_ID_LOCATION = 0x1D;
    public static final int HEADER_ID_MAX_FORWARDS = 0x1E;
    public static final int HEADER_ID_PRAGMA = 0x1F;
    
    public static final int HEADER_ID_PROXY_AUTHENTICATE = 0x20;
    public static final int HEADER_ID_PROXY_AUTHORIZATION = 0x21;
    public static final int HEADER_ID_PUBLIC = 0x22;
    public static final int HEADER_ID_RANGE = 0x23;
    public static final int HEADER_ID_REFERER = 0x24;
    public static final int HEADER_ID_RETRY_AFTER = 0x25;
    public static final int HEADER_ID_SERVER = 0x26;
    public static final int HEADER_ID_TRANSFER_ENCODING = 0x27;
    public static final int HEADER_ID_UPGRADE = 0x28;
    public static final int HEADER_ID_USER_AGENT = 0x29;
    public static final int HEADER_ID_VARY = 0x2A;
    public static final int HEADER_ID_VIA = 0x2B;
    public static final int HEADER_ID_WARNING = 0x2C;
    public static final int HEADER_ID_WWW_AUTHENTICATE = 0x2D;
//    public static final int HEADER_ID_CONTENT_DISPOSITION = 0x2E;
    public static final int HEADER_ID_X_WAP_APPLICATION_ID = 0x2F;
    
    public static final int HEADER_ID_X_WAP_CONTENT_URI = 0x30;
    public static final int HEADER_ID_X_WAP_INITIATOR_URI = 0x31;
    public static final int HEADER_ID_ACCEPT_APPLICATION = 0x32;
    public static final int HEADER_ID_BEARER_INDICATION = 0x33;
    public static final int HEADER_ID_PUSH_FLAG = 0x34;
    public static final int HEADER_ID_PROFILE = 0x35;
    public static final int HEADER_ID_PROFILE_DIFF = 0x36;
//    public static final int HEADER_ID_PROFILE_WARNING = 0x37;
    public static final int HEADER_ID_EXPECT = 0x38;
    public static final int HEADER_ID_TE = 0x39;
    public static final int HEADER_ID_TRAILER = 0x3A;
    public static final int HEADER_ID_ACCEPT_CHARSET = 0x3B;
    public static final int HEADER_ID_ACCEPT_ENCODING = 0x3C;
//    public static final int HEADER_ID_CACHE_CONTROL = 0x3D;
    public static final int HEADER_ID_CONTENT_RANGE = 0x3E;
    public static final int HEADER_ID_X_WAP_TOD = 0x3F;
    
    public static final int HEADER_ID_CONTENT_ID = 0x40;
    public static final int HEADER_ID_SET_COOKIE = 0x41;
    public static final int HEADER_ID_COOKIE = 0x42;
    public static final int HEADER_ID_ENCODING_VERSION = 0x43;
    public static final int HEADER_ID_PROFILE_WARNING = 0x44;
    public static final int HEADER_ID_CONTENT_DISPOSITION = 0x45;
    public static final int HEADER_ID_X_WAP_SECURITY = 0x46;
    public static final int HEADER_ID_CACHE_CONTROL = 0x47;
    

    public static final String HEADER_NAMES[] = {
        "accept",
        "accept-charset",       // Deprecated
        "accept-encoding",      // Deprecated
        "accept-language",
        "accept-ranges",
        "age",
        "allow",
        "authorization",
        "cache-control",        // Deprecated
        "connection",
        "content-base",         // Deprecated
        "content-encoding",
        "content-language",
        "content-length",
        "content-location",
        "content-md5",
        "content-range",        // Deprecated
        "content-type",
        "date",
        "etag",
        "expires",
        "from",
        "host",
        "if-modified-since",
        "if-match",
        "if-none-match",
        "if-range",
        "if-unmodified-since",
        "location",
        "last-modified",
        "max-forwards",
        "pragma",
        "proxy-authenticate",
        "proxy-authorization",
        "public",
        "range",
        "referer",
        "retry-after",
        "server",
        "transfer-encoding",
        "upgrade",
        "user-agent",
        "vary",
        "via",
        "warning",
        "www-authenticate",
        "content-disposition",  // Deprecated
        "x-wap-application-id",
        "x-wap-content-uri",
        "x-wap-initiator-uri",
        "bearer-indication",
        "push-flag",
        "profile",
        "profile-diff",
        "profile-warning",      // Deprecated
        "expect",
        "te",
        "trailer",
        "accept-charset",
        "accept-encoding",
        "cache-control",        // Deprecated
        "content-range",
        "x-wap-tod",
        "content-id",
        "set-cookie",
        "cookie",
        "encoding-version",
        "profile-warning",
        "content-disposition",
        "x-wap-security",
        "cache-control",
    };    
}
