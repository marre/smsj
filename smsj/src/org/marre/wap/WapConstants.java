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
    public static final int HEADER_ID_CONTENT_LOCATION = 0x0e;
    public static final int HEADER_ID_X_WAP_APPLICATION_ID = 0x2f;
    public static final int HEADER_ID_CONTENT_ID = 0x40;

    /*
     * IANA assigned charset values
     * http://www.iana.org/assignments/character-sets
     */
    public static final int MIB_ENUM_UTF_8 = 106;

    public static final int PARAMETER_TYPE_NO_VALUE = 0x01;
    public static final int PARAMETER_TYPE_TEXT_VALUE = 0x02;
    public static final int PARAMETER_TYPE_INTEGER_VALUE = 0x03;
    public static final int PARAMETER_TYPE_DATE_VALUE = 0x04;
    public static final int PARAMETER_TYPE_DELTA_SECONDS_VALUE = 0x05;
    public static final int PARAMETER_TYPE_Q_VALUE = 0x06;
    public static final int PARAMETER_TYPE_VERSION_VALUE = 0x07;
    public static final int PARAMETER_TYPE_URI_VALUE = 0x08;

    public static final int PARAMETER_TYPE_TEXT_STRING = 0x09;
    public static final int PARAMETER_TYPE_WELL_KNOWN_CHARSET = 0x0A;
    public static final int PARAMETER_TYPE_FIELD_NAME = 0x0B;
    public static final int PARAMETER_TYPE_SHORT_INTEGER = 0x0C;
    public static final int PARAMETER_TYPE_CONSTRAINED_ENCODING = 0x0D;

    public static final String PARAMETER_NAMES[] = {
        "q",
        "charset",
        "level",
        "type",
        null,
        "name",           // Deprecated in encoding v1.4
        "filename",       // Deprecated in encoding v1.4
        "differences",
        "padding",
        "type",
        "start",          // Deprecated in encoding v1.4
        "start-info",     // Deprecated in encoding v1.4
        "comment",        // Deprecated in encoding v1.4
        "domain",         // Deprecated in encoding v1.4
        "max-age",
        "path",           // Deprecated in encoding v1.4

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
        PARAMETER_TYPE_Q_VALUE,
        PARAMETER_TYPE_WELL_KNOWN_CHARSET,
        PARAMETER_TYPE_VERSION_VALUE,
        PARAMETER_TYPE_INTEGER_VALUE,
        -1,
        PARAMETER_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        PARAMETER_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        PARAMETER_TYPE_FIELD_NAME,
        PARAMETER_TYPE_SHORT_INTEGER,
        PARAMETER_TYPE_CONSTRAINED_ENCODING,
        PARAMETER_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        PARAMETER_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        PARAMETER_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        PARAMETER_TYPE_TEXT_STRING, // Deprecated in encoding v1.4
        PARAMETER_TYPE_DELTA_SECONDS_VALUE,
        PARAMETER_TYPE_TEXT_STRING, // Deprecated in encoding v1.4

        PARAMETER_TYPE_NO_VALUE,
        PARAMETER_TYPE_SHORT_INTEGER,
        PARAMETER_TYPE_TEXT_VALUE,
        PARAMETER_TYPE_DATE_VALUE,
        PARAMETER_TYPE_DATE_VALUE,
        PARAMETER_TYPE_DATE_VALUE,
        PARAMETER_TYPE_INTEGER_VALUE,
        PARAMETER_TYPE_TEXT_VALUE,
        PARAMETER_TYPE_TEXT_VALUE,
        PARAMETER_TYPE_TEXT_VALUE,
        PARAMETER_TYPE_TEXT_VALUE,
        PARAMETER_TYPE_TEXT_VALUE,
        PARAMETER_TYPE_TEXT_VALUE,
        PARAMETER_TYPE_TEXT_VALUE,
    };
}
