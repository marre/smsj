/*
    SMS Library for the Java platform
    Copyright (C) 2002  Markus Eriksson

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
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
    public static final int HEADER_ID_X_WAP_APPLICATION_ID = 0x2f;
}
