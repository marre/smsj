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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.marre.mime.MimeHeader;
import org.marre.mime.MimeHeaderParam;

/**
 * 
 * @author Markus Eriksson
 * @version $Id$
 */
public final class WspUtil
{
    private static Map myWspHeaders;
    private static Map myWspContentTypes;
    private static Map myWspParameters;
    private static Map myWspPushAppTypes;
    
    /* Maps a header id to a well known id */
    private static final int[] WELL_KNOWN_HEADER_ID_WSP_11 = {
        0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
        0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
        0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, -1,
        -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
        -1
    };
            
    private static final int[] WELL_KNOWN_HEADER_ID_WSP_12 = {
        0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
        0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
        0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F,
        0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  
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
        0x00, 0x01, 0x02, 0x03, 0x05, 0x06, 0x07, 0x08, -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
        -1,   -1,   -1,   -1,   -1,   -1
    };
            
    private static final int[] WELL_KNOWN_PARAMETER_ID_WSP_12 = {
        0x00, 0x01, 0x02, 0x03, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, -1,   -1,   -1,   -1,   -1,
        -1,   -1,   -1,   -1,   -1,   -1
    };
    
    private static final int[] WELL_KNOWN_PARAMETER_ID_WSP_13 = {
        0x00, 0x01, 0x02, 0x03, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10,
        -1,   -1,   -1,   -1,   -1,   -1
    };
    
    private static final int[] WELL_KNOWN_PARAMETER_ID_WSP_14 = {
        0x00, 0x01, 0x02, 0x03, 0x17, 0x18, 0x07, 0x08, 0x09, 0x19, 0x1A, 0x1B, 0x1C, 0x0E, 0x1D, 0x10,
        0x11, 0x12, 0x13, 0x14, 0x15, 0x16
    };
    
    /* Maps a well known parameter id to a parameter type */
    private static final int[] PARAMETER_TYPES = {
        WapConstants.WSP_PARAMETER_TYPE_Q_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_WELL_KNOWN_CHARSET,
        WapConstants.WSP_PARAMETER_TYPE_VERSION_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_INTEGER_VALUE,
        -1,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
        WapConstants.WSP_PARAMETER_TYPE_FIELD_NAME,
        WapConstants.WSP_PARAMETER_TYPE_SHORT_INTEGER,
        
        WapConstants.WSP_PARAMETER_TYPE_CONSTRAINED_ENCODING,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
        
        WapConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
        WapConstants.WSP_PARAMETER_TYPE_DELTA_SECONDS_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_STRING,
        WapConstants.WSP_PARAMETER_TYPE_NO_VALUE,
        
        WapConstants.WSP_PARAMETER_TYPE_SHORT_INTEGER,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_DATE_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_DATE_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_DATE_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_INTEGER_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
        WapConstants.WSP_PARAMETER_TYPE_TEXT_VALUE,
    };
    
    static {
        // WSP 1.1
        myWspHeaders = new HashMap();
        myWspHeaders.put("accept",               new Integer(WapConstants.HEADER_ACCEPT));
        myWspHeaders.put("accept-charset",       new Integer(WapConstants.HEADER_ACCEPT_CHARSET));
        myWspHeaders.put("accept-encoding",      new Integer(WapConstants.HEADER_ACCEPT_ENCODING));
        myWspHeaders.put("accept-language",      new Integer(WapConstants.HEADER_ACCEPT_LANGUAGE));
        myWspHeaders.put("accept-ranges",        new Integer(WapConstants.HEADER_ACCEPT_RANGES));
        myWspHeaders.put("age",                  new Integer(WapConstants.HEADER_AGE));
        myWspHeaders.put("allow",                new Integer(WapConstants.HEADER_ALLOW));
        myWspHeaders.put("authorization",        new Integer(WapConstants.HEADER_AUTHORIZATION));
        myWspHeaders.put("cache-control",        new Integer(WapConstants.HEADER_CACHE_CONTROL));
        myWspHeaders.put("connection",           new Integer(WapConstants.HEADER_CONNECTION));
        myWspHeaders.put("content-base",         new Integer(WapConstants.HEADER_CONTENT_BASE));
        myWspHeaders.put("content-encoding",     new Integer(WapConstants.HEADER_CONTENT_ENCODING));
        myWspHeaders.put("content-language",     new Integer(WapConstants.HEADER_CONTENT_LANGUAGE));
        myWspHeaders.put("content-length",       new Integer(WapConstants.HEADER_CONTENT_LENGTH));
        myWspHeaders.put("content-location",     new Integer(WapConstants.HEADER_CONTENT_LOCATION));
        myWspHeaders.put("content-md5",          new Integer(WapConstants.HEADER_CONTENT_MD5));
        myWspHeaders.put("content-range",        new Integer(WapConstants.HEADER_CONTENT_RANGE));
        myWspHeaders.put("content-type",         new Integer(WapConstants.HEADER_CONTENT_TYPE));
        myWspHeaders.put("date",                 new Integer(WapConstants.HEADER_DATE));
        myWspHeaders.put("etag",                 new Integer(WapConstants.HEADER_ETAG));
        myWspHeaders.put("expires",              new Integer(WapConstants.HEADER_EXPIRES));
        myWspHeaders.put("from",                 new Integer(WapConstants.HEADER_FROM));
        myWspHeaders.put("host",                 new Integer(WapConstants.HEADER_HOST));
        myWspHeaders.put("if-modified-since",    new Integer(WapConstants.HEADER_IF_MODIFIED_SINCE));
        myWspHeaders.put("if-match",             new Integer(WapConstants.HEADER_IF_MATCH));
        myWspHeaders.put("if-none-match",        new Integer(WapConstants.HEADER_IF_NONE_MATCH));
        myWspHeaders.put("if-range",             new Integer(WapConstants.HEADER_IF_RANGE));
        myWspHeaders.put("if-unmodified-since",  new Integer(WapConstants.HEADER_IF_UNMODIFIED_SINCE));
        myWspHeaders.put("location",             new Integer(WapConstants.HEADER_LOCATION));
        myWspHeaders.put("last-modified",        new Integer(WapConstants.HEADER_LAST_MODIFIED));
        myWspHeaders.put("max-forwards",         new Integer(WapConstants.HEADER_MAX_FORWARDS));
        myWspHeaders.put("pragma",               new Integer(WapConstants.HEADER_PRAGMA));
        myWspHeaders.put("proxy-authenticate",   new Integer(WapConstants.HEADER_PROXY_AUTHENTICATE));
        myWspHeaders.put("proxy-authorization",  new Integer(WapConstants.HEADER_PROXY_AUTHORIZATION));
        myWspHeaders.put("public",               new Integer(WapConstants.HEADER_PUBLIC));
        myWspHeaders.put("range",                new Integer(WapConstants.HEADER_RANGE));
        myWspHeaders.put("referer",              new Integer(WapConstants.HEADER_REFERER));
        myWspHeaders.put("retry-after",          new Integer(WapConstants.HEADER_RETRY_AFTER));
        myWspHeaders.put("server",               new Integer(WapConstants.HEADER_SERVER));
        myWspHeaders.put("transfer-encoding",    new Integer(WapConstants.HEADER_TRANSFER_ENCODING));
        myWspHeaders.put("upgrade",              new Integer(WapConstants.HEADER_UPGRADE));
        myWspHeaders.put("user-agent",           new Integer(WapConstants.HEADER_USER_AGENT));
        myWspHeaders.put("vary",                 new Integer(WapConstants.HEADER_VARY));
        myWspHeaders.put("via",                  new Integer(WapConstants.HEADER_VIA));
        myWspHeaders.put("warning",              new Integer(WapConstants.HEADER_WARNING));
        myWspHeaders.put("www-authenticate",     new Integer(WapConstants.HEADER_WWW_AUTHENTICATE));
        myWspHeaders.put("content-disposition",  new Integer(WapConstants.HEADER_CONTENT_DISPOSITION));
        
        // WSP 1.2
        myWspHeaders.put("accept",               new Integer(WapConstants.HEADER_ACCEPT));
        myWspHeaders.put("x-wap-application-id", new Integer(WapConstants.HEADER_X_WAP_APPLICATION_ID));
        myWspHeaders.put("x-wap-content-uri",    new Integer(WapConstants.HEADER_X_WAP_CONTENT_URI));
        myWspHeaders.put("x-wap-initiator-uri",  new Integer(WapConstants.HEADER_X_WAP_INITIATOR_URI));
        myWspHeaders.put("bearer-indication",    new Integer(WapConstants.HEADER_BEARER_INDICATION));
        myWspHeaders.put("accept-application",   new Integer(WapConstants.HEADER_ACCEPT_APPLICATION));
        myWspHeaders.put("push-flag",            new Integer(WapConstants.HEADER_PUSH_FLAG));
        myWspHeaders.put("profile",              new Integer(WapConstants.HEADER_PROFILE));
        myWspHeaders.put("profile-diff",         new Integer(WapConstants.HEADER_PROFILE_DIFF));
        myWspHeaders.put("profile-warning",      new Integer(WapConstants.HEADER_PROFILE_WARNING));
        
        // WSP 1.3
        myWspHeaders.put("expect",               new Integer(WapConstants.HEADER_EXPECT));
        myWspHeaders.put("te",                   new Integer(WapConstants.HEADER_TE));
        myWspHeaders.put("trailer",              new Integer(WapConstants.HEADER_TRAILER));
        myWspHeaders.put("accept-charset",       new Integer(WapConstants.HEADER_ACCEPT_CHARSET));
        myWspHeaders.put("accept-encoding",      new Integer(WapConstants.HEADER_ACCEPT_ENCODING));
        myWspHeaders.put("cache-control",        new Integer(WapConstants.HEADER_CACHE_CONTROL));
        myWspHeaders.put("content-range",        new Integer(WapConstants.HEADER_CONTENT_RANGE));
        myWspHeaders.put("x-wap-tod",            new Integer(WapConstants.HEADER_X_WAP_TOD));
        myWspHeaders.put("content-id",           new Integer(WapConstants.HEADER_CONTENT_ID));
        myWspHeaders.put("set-cookie",           new Integer(WapConstants.HEADER_SET_COOKIE));
        myWspHeaders.put("cookie",               new Integer(WapConstants.HEADER_COOKIE));
        myWspHeaders.put("encoding-version",     new Integer(WapConstants.HEADER_ENCODING_VERSION));
        
        // WSP 1.4
        myWspHeaders.put("profile-warning",      new Integer(WapConstants.HEADER_PROFILE_WARNING));
        myWspHeaders.put("content-disposition",  new Integer(WapConstants.HEADER_CONTENT_DISPOSITION));
        myWspHeaders.put("x-wap-security",       new Integer(WapConstants.HEADER_X_WAP_SECURITY));
        myWspHeaders.put("cache-control",        new Integer(WapConstants.HEADER_CACHE_CONTROL));
        
        // http://www.wapforum.org/wina/wsp-content-type.htm
        // WSP 1.1
        myWspContentTypes = new HashMap();        
        myWspContentTypes.put("*/*",                                            new Integer(0x00));
        myWspContentTypes.put("text/*",                                         new Integer(0x01));
        myWspContentTypes.put("text/html",                                      new Integer(0x02));
        myWspContentTypes.put("text/plain",                                     new Integer(0x03));
        myWspContentTypes.put("text/x-hdml",                                    new Integer(0x04));
        myWspContentTypes.put("text/x-ttml",                                    new Integer(0x05));
        myWspContentTypes.put("text/x-vCalendar",                               new Integer(0x06));
        myWspContentTypes.put("text/x-vCard",                                   new Integer(0x07));
        myWspContentTypes.put("text/vnd.wap.wml",                               new Integer(0x08));
        myWspContentTypes.put("text/vnd.wap.wmlscript",                         new Integer(0x09));
        myWspContentTypes.put("text/vnd.wap.wta-event",                         new Integer(0x0A));
        myWspContentTypes.put("multipart/*",                                    new Integer(0x0B));
        myWspContentTypes.put("multipart/mixed",                                new Integer(0x0C));
        myWspContentTypes.put("multipart/form-data",                            new Integer(0x0D));
        myWspContentTypes.put("multipart/byteranges",                           new Integer(0x0E));
        myWspContentTypes.put("multipart/alternative",                          new Integer(0x0F));
        myWspContentTypes.put("application/*",                                  new Integer(0x10));
        myWspContentTypes.put("application/java-vm",                            new Integer(0x11));
        myWspContentTypes.put("application/x-www-form-urlencoded",              new Integer(0x12));
        myWspContentTypes.put("application/x-hdmlc",                            new Integer(0x13));
        myWspContentTypes.put("application/vnd.wap.wmlc",                       new Integer(0x14));
        myWspContentTypes.put("application/vnd.wap.wmlscriptc",                 new Integer(0x15));
        myWspContentTypes.put("application/vnd.wap.wta-eventc",                 new Integer(0x16));
        myWspContentTypes.put("application/vnd.wap.uaprof",                     new Integer(0x17));
        myWspContentTypes.put("application/vnd.wap.wtls-ca-certificate",        new Integer(0x18));
        myWspContentTypes.put("application/vnd.wap.wtls-user-certificate",      new Integer(0x19));
        myWspContentTypes.put("application/x-x509-ca-cert",                     new Integer(0x1A));
        myWspContentTypes.put("application/x-x509-user-cert",                   new Integer(0x1B));
        myWspContentTypes.put("image/*",                                        new Integer(0x1C));
        myWspContentTypes.put("image/gif",                                      new Integer(0x1D));
        myWspContentTypes.put("image/jpeg",                                     new Integer(0x1E));
        myWspContentTypes.put("image/tiff",                                     new Integer(0x1F));
        myWspContentTypes.put("image/png",                                      new Integer(0x20));
        myWspContentTypes.put("image/vnd.wap.wbmp",                             new Integer(0x21));
        myWspContentTypes.put("application/vnd.wap.multipart.*",                new Integer(0x22));
        myWspContentTypes.put("application/vnd.wap.multipart.mixed",            new Integer(0x23));
        myWspContentTypes.put("application/vnd.wap.multipart.form-data",        new Integer(0x24));
        myWspContentTypes.put("application/vnd.wap.multipart.byteranges",       new Integer(0x25));
        myWspContentTypes.put("application/vnd.wap.multipart.alternative",      new Integer(0x26));
        myWspContentTypes.put("application/xml",                                new Integer(0x27));
        myWspContentTypes.put("text/xml",                                       new Integer(0x28));
        myWspContentTypes.put("application/vnd.wap.wbxml",                      new Integer(0x29));
        myWspContentTypes.put("application/x-x968-cross-cert",                  new Integer(0x2A));
        myWspContentTypes.put("application/x-x968-ca-cert",                     new Integer(0x2B));
        myWspContentTypes.put("application/x-x968-user-cert",                   new Integer(0x2C));
        myWspContentTypes.put("text/vnd.wap.si",                                new Integer(0x2D));

        // WSP 1.2
        myWspContentTypes.put("application/vnd.wap.sic",                        new Integer(0x2E));
        myWspContentTypes.put("text/vnd.wap.sl",                                new Integer(0x2F));
        myWspContentTypes.put("application/vnd.wap.slc",                        new Integer(0x30));
        myWspContentTypes.put("text/vnd.wap.co",                                new Integer(0x31));
        myWspContentTypes.put("application/vnd.wap.coc",                        new Integer(0x32));
        myWspContentTypes.put("application/vnd.wap.multipart.related",          new Integer(0x33));
        myWspContentTypes.put("application/vnd.wap.sia",                        new Integer(0x34));
                
        // WSP 1.3
        myWspContentTypes.put("text/vnd.wap.connectivity-xml",                  new Integer(0x35));
        myWspContentTypes.put("application/vnd.wap.connectivity-wbxml",         new Integer(0x36));
        
        // WSP 1.4
        myWspContentTypes.put("application/pkcs7-mime",                         new Integer(0x37));
        myWspContentTypes.put("application/vnd.wap.hashed-certificate",         new Integer(0x38));
        myWspContentTypes.put("application/vnd.wap.signed-certificate",         new Integer(0x39));
        myWspContentTypes.put("application/vnd.wap.cert-response",              new Integer(0x3A));
        myWspContentTypes.put("application/xhtml+xml",                          new Integer(0x3B));
        myWspContentTypes.put("application/wml+xml",                            new Integer(0x3C));
        myWspContentTypes.put("text/css",                                       new Integer(0x3D));
        myWspContentTypes.put("application/vnd.wap.mms-message",                new Integer(0x3E));
        myWspContentTypes.put("application/vnd.wap.rollover-certificate",       new Integer(0x3F));
        
        // WSP 1.5
        myWspContentTypes.put("application/vnd.wap.locc+wbxml",                 new Integer(0x40));
        myWspContentTypes.put("application/vnd.wap.loc+xml",                    new Integer(0x41));
        myWspContentTypes.put("application/vnd.syncml.dm+wbxml",                new Integer(0x42));
        myWspContentTypes.put("application/vnd.syncml.dm+xml",                  new Integer(0x43));
        myWspContentTypes.put("application/vnd.syncml.notification",            new Integer(0x44));
        myWspContentTypes.put("application/vnd.wap.xhtml+xml",                  new Integer(0x45));
        myWspContentTypes.put("application/vnd.wv.csp.cir",                     new Integer(0x46));
        myWspContentTypes.put("application/vnd.oma.dd+xml",                     new Integer(0x47));
        myWspContentTypes.put("application/vnd.oma.drm.message",                new Integer(0x48));
        myWspContentTypes.put("application/vnd.oma.drm.content",                new Integer(0x49));
        myWspContentTypes.put("application/vnd.oma.drm.rights+xml",             new Integer(0x4A));
        myWspContentTypes.put("application/vnd.oma.drm.rights+wbxml",           new Integer(0x4B));
        
        // WSP 1.1
        myWspParameters = new HashMap();
        myWspParameters.put("q",                    new Integer(WapConstants.PARAMETER_Q));
        myWspParameters.put("charset",              new Integer(WapConstants.PARAMETER_CHARSET));
        myWspParameters.put("level",                new Integer(WapConstants.PARAMETER_LEVEL));
        myWspParameters.put("type",                 new Integer(WapConstants.PARAMETER_TYPE));
        myWspParameters.put("name",                 new Integer(WapConstants.PARAMETER_NAME));
        myWspParameters.put("filename",             new Integer(WapConstants.PARAMETER_FILENAME));
        myWspParameters.put("differences",          new Integer(WapConstants.PARAMETER_DIFFERENCES));
        myWspParameters.put("padding",              new Integer(WapConstants.PARAMETER_PADDING));
            
        // WSP 1.2
        myWspParameters.put("type",                 new Integer(WapConstants.PARAMETER_TYPE_MULTIPART_RELATED));
        myWspParameters.put("start",                new Integer(WapConstants.PARAMETER_START_MULTIPART_RELATED));
        myWspParameters.put("start-info",           new Integer(WapConstants.PARAMETER_START_INFO_MULTIPART_RELATED));
            
        // WSP 1.3
        myWspParameters.put("comment",              new Integer(WapConstants.PARAMETER_COMMENT));
        myWspParameters.put("domain",               new Integer(WapConstants.PARAMETER_DOMAIN));
        myWspParameters.put("max-age",              new Integer(WapConstants.PARAMETER_MAX_AGE));
        myWspParameters.put("path",                 new Integer(WapConstants.PARAMETER_PATH));
        myWspParameters.put("secure",               new Integer(WapConstants.PARAMETER_SECURE));
            
        // WSP 1.4
        myWspParameters.put("sec",                  new Integer(WapConstants.PARAMETER_SEC_CONNECTIVITY));
        myWspParameters.put("mac",                  new Integer(WapConstants.PARAMETER_MAC_CONNECTIVITY));
        myWspParameters.put("creation-date",        new Integer(WapConstants.PARAMETER_CREATION_DATE));
        myWspParameters.put("modification-date",    new Integer(WapConstants.PARAMETER_MODIFICATION_DATE));
        myWspParameters.put("read-date",            new Integer(WapConstants.PARAMETER_READ_DATE));
        myWspParameters.put("size",                 new Integer(WapConstants.PARAMETER_SIZE));
        myWspParameters.put("name",                 new Integer(WapConstants.PARAMETER_NAME));
        myWspParameters.put("filename",             new Integer(WapConstants.PARAMETER_FILENAME));
        myWspParameters.put("start",                new Integer(WapConstants.PARAMETER_START_MULTIPART_RELATED));
        myWspParameters.put("start-info",           new Integer(WapConstants.PARAMETER_START_INFO_MULTIPART_RELATED));
        myWspParameters.put("comment",              new Integer(WapConstants.PARAMETER_COMMENT));
        myWspParameters.put("domain",               new Integer(WapConstants.PARAMETER_DOMAIN));
        myWspParameters.put("path",                 new Integer(WapConstants.PARAMETER_PATH));
        
        // http://www.wapforum.org/wina/push-app-id.htm
        myWspPushAppTypes = new HashMap();
        myWspPushAppTypes.put("x-wap-application:*",            new Integer(0x00));
        myWspPushAppTypes.put("x-wap-application:push.sia",     new Integer(0x01));
        myWspPushAppTypes.put("x-wap-application:wml.ua",       new Integer(0x02));
        myWspPushAppTypes.put("x-wap-application:wta.ua",       new Integer(0x03));
        myWspPushAppTypes.put("x-wap-application:mms.ua",       new Integer(0x04));
        myWspPushAppTypes.put("x-wap-application:push.syncml",  new Integer(0x05));
        myWspPushAppTypes.put("x-wap-application:loc.ua",       new Integer(0x06));
        myWspPushAppTypes.put("x-wap-application:syncml.dm",    new Integer(0x07));
        myWspPushAppTypes.put("x-wap-application:drm.ua",       new Integer(0x08));
        myWspPushAppTypes.put("x-wap-application:emn.ua",       new Integer(0x09));
        myWspPushAppTypes.put("x-wap-application:wv.ua",        new Integer(0x0A));
        
        myWspPushAppTypes.put("x-wap-microsoft:localcontent.ua",    new Integer(0x8000));
        myWspPushAppTypes.put("x-wap-microsoft:imclient.ua ",       new Integer(0x8001));
        myWspPushAppTypes.put("x-wap-docomo:imode.mail.ua ",        new Integer(0x8002));
        myWspPushAppTypes.put("x-wap-docomo:imode.mr.ua",           new Integer(0x8003));
        myWspPushAppTypes.put("x-wap-docomo:imode.mf.ua",           new Integer(0x8004));
        myWspPushAppTypes.put("x-motorola:location.ua ",            new Integer(0x8005));
        myWspPushAppTypes.put("x-motorola:now.ua",                  new Integer(0x8006));
        myWspPushAppTypes.put("x-motorola:otaprov.ua",              new Integer(0x8007));
        myWspPushAppTypes.put("x-motorola:browser.ua",              new Integer(0x8008));
        myWspPushAppTypes.put("x-motorola:splash.ua",               new Integer(0x8009));
        myWspPushAppTypes.put("x-wap-nai:mvsw.command ",            new Integer(0x800B));
        myWspPushAppTypes.put("x-wap-openwave:iota.ua",             new Integer(0x8010));
    }
    
    private WspUtil()
    {
    }

    /**
     * Converts a header name to a header type (WapConstants.HEADER_*).
     * 
     * The header name to be found must be in lower case (for performance reasons).
     * 
     * @param headerName The name of the header.
     * @return The header type, or -1 if not found.
     */
    public static int getHeaderType(String headerName)
    {
        Integer headerType = (Integer) myWspHeaders.get(headerName);
        
        return (headerType != null) ? (headerType.intValue()) : (-1);
    }
   
    /**
     * Converts a header type (WapConstants.HEADER_*) to a well known header id.
     * 
     * @param wspEncodingVersion The requested wsp encoding version
     * @param headerType The header type
     * @return A well known header id or -1 if not found.
     */
    public static int getWellKnownHeaderId(byte wspEncodingVersion, int headerType)
    {
        int wellKnownHeaderId;

        switch (wspEncodingVersion)
        {
        case WapConstants.WSP_ENCODING_VERSION_1_1:
            wellKnownHeaderId = WELL_KNOWN_HEADER_ID_WSP_11[headerType];
            break;
        case WapConstants.WSP_ENCODING_VERSION_1_2:
            wellKnownHeaderId = WELL_KNOWN_HEADER_ID_WSP_12[headerType];
            break;
            
        case WapConstants.WSP_ENCODING_VERSION_1_3:
            wellKnownHeaderId = WELL_KNOWN_HEADER_ID_WSP_13[headerType];
            break;
            
        case WapConstants.WSP_ENCODING_VERSION_1_4:
        case WapConstants.WSP_ENCODING_VERSION_1_5:
            wellKnownHeaderId = WELL_KNOWN_HEADER_ID_WSP_14[headerType];
            break;
        
        default:
            wellKnownHeaderId = -1;
        }

        return wellKnownHeaderId;
    }
    
    /**
     * Converts a content type to a WINA "well-known" content type id.
     * 
     * http://www.wapforum.org/wina/wsp-content-type.htm
     * 
     * @param wspEncodingVersion The requested wsp encoding version
     * @param contentType The content type
     * @return A well known content type id or -1 if not found.
     */
    public static int getWellKnownContentTypeId(byte wspEncodingVersion, String contentType)
    {
        Integer contentTypeIdInt = (Integer) myWspContentTypes.get(contentType);        

        if (contentTypeIdInt == null)
        {
            return -1;
        }
        
        int wellKnownContentTypeId = contentTypeIdInt.intValue();
        
        if ( (wspEncodingVersion >= WapConstants.WSP_ENCODING_VERSION_1_1) && (wellKnownContentTypeId <= 0x2D) )
        {
            return wellKnownContentTypeId;
        }
        else if ( (wspEncodingVersion >= WapConstants.WSP_ENCODING_VERSION_1_2) && (wellKnownContentTypeId <= 0x34) )
        {
            return wellKnownContentTypeId;
        }
        else if ( (wspEncodingVersion >= WapConstants.WSP_ENCODING_VERSION_1_3) && (wellKnownContentTypeId <= 0x36) )
        {
            return wellKnownContentTypeId;
        }
        else if ( (wspEncodingVersion >= WapConstants.WSP_ENCODING_VERSION_1_4) && (wellKnownContentTypeId <= 0x3F) )
        {
            return wellKnownContentTypeId;
        }
        else if ( (wspEncodingVersion >= WapConstants.WSP_ENCODING_VERSION_1_5) && (wellKnownContentTypeId <= 0x4B) )
        {
            return wellKnownContentTypeId;
        }
        else
        {
            return -1;
        }
    }
    
    /**
     * Converts a parameter name to a parameter type (WapConstants.PARAMETER_*).
     * 
     * The header name to be found must be in lower case (for performance reasons).
     * 
     * @param parameterName The name of the parameter.
     * @return The parameter type, or -1 if not found.
     */
    public static int getParameterType(String parameterName)
    {
        Integer parameterType = (Integer) myWspParameters.get(parameterName);
        
        return (parameterType != null) ? (parameterType.intValue()) : (-1);
    }

    /**
     * Converts a parameter name to a parameter type (WapConstants.WSP_PARAMETER_TYPE_*).
     * 
     * @param wellKnownParameterId The well known parameter id to lookup.
     * @return The parameter type, or -1 if not found.
     */
    public static int getWspParameterType(int wellKnownParameterId)
    {
        return PARAMETER_TYPES[wellKnownParameterId];
    }
    
    /**
     * Converts a parameter type (WapConstants.PARAMETER_*) to a well known parameter id.
     * 
     * @param wspEncodingVersion The requested wsp encoding version
     * @param parameterType The header type
     * @return A well known parameter id or -1 if not found.
     */
    public static int getWellKnownParameterId(byte wspEncodingVersion, int parameterType)
    {
        int wellKnownParameterId = -1;

        if (parameterType >= 0)
        {
            switch (wspEncodingVersion)
            {
            case WapConstants.WSP_ENCODING_VERSION_1_1:
                wellKnownParameterId = WELL_KNOWN_PARAMETER_ID_WSP_11[parameterType];
                break;
            case WapConstants.WSP_ENCODING_VERSION_1_2:
                wellKnownParameterId = WELL_KNOWN_PARAMETER_ID_WSP_12[parameterType];
                break;
                
            case WapConstants.WSP_ENCODING_VERSION_1_3:
                wellKnownParameterId = WELL_KNOWN_PARAMETER_ID_WSP_13[parameterType];
                break;
                
            case WapConstants.WSP_ENCODING_VERSION_1_4:
            case WapConstants.WSP_ENCODING_VERSION_1_5:
                wellKnownParameterId = WELL_KNOWN_PARAMETER_ID_WSP_14[parameterType];
                break;
            
            default:
            }
        }
        
        return wellKnownParameterId;
    }
    
    /**
     * Converts a push app to a WINA "well-known" push app id.
     * 
     * http://www.wapforum.org/wina/push-app-id.htm
     * 
     * @param pushApp The push app
     * @return A well known push app id or -1 if not found.
     */
    public static int getWellKnownPushAppId(String pushApp)
    {
        Integer pushAppIdInt = (Integer) myWspPushAppTypes.get(pushApp);        

        if (pushAppIdInt == null)
        {
            return -1;
        }
        
        return pushAppIdInt.intValue();
    }
    
    /**
     * Writes a "uint8" in wsp format to the given output stream.
     * 
     * @param theOs
     *            Stream to write to
     * @param theValue
     *            Value to write
     */
    public static void writeUint8(OutputStream theOs, int theValue) throws IOException
    {
        theOs.write(theValue);
    }

    /**
     * Writes a "Uintvar" in wsp format to the given output stream.
     * 
     * @param theOs
     *            Stream to write to
     * @param theValue
     *            Value to write
     */
    public static void writeUintvar(OutputStream theOs, long theValue) throws IOException
    {
        int nOctets = 1;
        while ((theValue >> (7 * nOctets)) > 0)
        {
            nOctets++;
        }

        for (int i = nOctets; i > 0; i--)
        {
            byte octet = (byte) (theValue >> (7 * (i - 1)));
            byte byteValue = (byte) ((byte) octet & (byte) 0x7f);
            if (i > 1)
            {
                byteValue = (byte) (byteValue | (byte) 0x80);
            }
            theOs.write(byteValue);
        }
    }

    /**
     * Writes a "long integer" in wsp format to the given output stream.
     * 
     * @param theOs
     *            Stream to write to
     * @param theValue
     *            Value to write
     */
    public static void writeLongInteger(OutputStream theOs, long theValue) throws IOException
    {
        int nOctets = 0;
        while ((theValue >> (8 * nOctets)) > 0)
        {
            nOctets++;
        }
        theOs.write((byte) nOctets);

        for (int i = nOctets; i > 0; i--)
        {
            byte octet = (byte) (theValue >> (8 * (i - 1)));
            byte byteValue = (byte) ((byte) octet & (byte) (0xff));
            theOs.write(byteValue);
        }
    }

    /**
     * Writes an "integer" in wsp format to the given output stream.
     * 
     * @param theOs
     * @param theValue
     */
    public static void writeInteger(OutputStream theOs, long theValue) throws IOException
    {
        if (theValue < 128)
        {
            writeShortInteger(theOs, (int) theValue);
        }
        else
        {
            writeLongInteger(theOs, theValue);
        }
    }

    /**
     * Writes a "short integer" in wsp format to the given output stream.
     * 
     * @param theOs
     *            Stream to write to
     * @param theValue
     *            Value to write
     */
    public static void writeShortInteger(OutputStream theOs, int theValue) throws IOException
    {
        theOs.write((byte) (theValue | (byte) 0x80));
    }

    public static void writeValueLength(OutputStream theOs, long theValue) throws IOException
    {
        // ShortLength | (Length-quote Length)

        if (theValue <= 30)
        {
            // Short-length
            theOs.write((int) theValue);
        }
        else
        {
            // Length-quote == Octet 31
            theOs.write(31);
            writeUintvar(theOs, theValue);
        }
    }

    /**
     * Writes an "extension media" in pdu format to the given output stream. It
     * currently only handles ASCII chars, but should be extended to work with
     * other charsets.
     * 
     * @param theOs
     *            Stream to write to
     * @param theStr
     *            Text to write
     */
    public static void writeExtensionMedia(OutputStream theOs, String theStr) throws IOException
    {
        theOs.write(theStr.getBytes("ISO-8859-1"));
        theOs.write((byte) 0x00);
    }

    public static void writeTextString(OutputStream theOs, String theStr) throws IOException
    {
        /*
         * Text-string = [Quote] *TEXT End-of-string ; If the first character in
         * the TEXT is in the range of 128-255, a Quote character must precede
         * it. ; Otherwise the Quote character must be omitted. The Quote is not
         * part of the contents. Quote = <Octet 127> End-of-string = <Octet 0>
         */

        byte[] strBytes = theStr.getBytes("ISO-8859-1");

        if ((strBytes[0] & 0x80) > 0x00)
        {
            theOs.write(0x7f);
        }

        theOs.write(strBytes);
        theOs.write(0x00);
    }

    public static void writeQuotedString(OutputStream theOs, String theStr) throws IOException
    {
        /*
         * Quoted-string = <Octet 34> *TEXT End-of-string ;The TEXT encodes an
         * RFC2616 Quoted-string with the enclosing quotation-marks <"> removed
         */

        // <Octet 34>
        theOs.write(34);

        theOs.write(theStr.getBytes("ISO-8859-1"));
        theOs.write(0x00);
    }

    public static void writeTokenText(OutputStream theOs, String theStr) throws IOException
    {
        /*
         * Token-Text = Token End-of-string
         */
        // TODO: Token => RFC2616
        theOs.write(theStr.getBytes("ISO-8859-1"));
        theOs.write(0x00);
    }

    public static void writeTextValue(OutputStream theOs, String theStr) throws IOException
    {
        /*
         * // No-value | Token-text | Quoted-string
         */
        // FIXME: Verify
        writeQuotedString(theOs, theStr);
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
    public static void writeContentType(byte wspEncodingVersion, OutputStream theOs, String theContentType) throws IOException
    {
        int wellKnownContentType = WspUtil.getWellKnownContentTypeId(wspEncodingVersion, theContentType.toLowerCase());

        if (wellKnownContentType == -1)
        {
            writeValueLength(theOs, theContentType.length() + 1);
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
     * This method automatically chooses the most compact way to represent the
     * given content type.
     * 
     * @param theOs
     * @param theContentType
     * @throws IOException
     */
    public static void writeContentType(byte wspEncodingVersion, OutputStream theOs, MimeHeader theContentType) throws IOException
    {
        if (theContentType.getParamCount() == 0)
        {
            // Simple content type, use "constrained-media" format
            writeContentType(wspEncodingVersion, theOs, theContentType.getValue());
        }
        else
        {
            String theContentTypeStr = theContentType.getValue();
            // Complex, use "content-general-form"
            int wellKnownContentType = WspUtil.getWellKnownContentTypeId(wspEncodingVersion, theContentTypeStr.toLowerCase());

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
            for (int i = 0; i < theContentType.getParamCount(); i++)
            {
                MimeHeaderParam headerParam = theContentType.getParam(i);
                writeParameter(wspEncodingVersion, baos, headerParam.getName(), headerParam.getValue());
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

    public static void writeTypedValue(byte wspEncodingVersion, OutputStream os, int wspParamType, String value) throws IOException
    {
        switch (wspParamType)
        {
        // "Used to indicate that the parameter actually have no value,
        // eg, as the parameter "bar" in ";foo=xxx; bar; baz=xyzzy"."
        case WapConstants.WSP_PARAMETER_TYPE_NO_VALUE:
            os.write(0x00);
            break;

        case WapConstants.WSP_PARAMETER_TYPE_TEXT_VALUE:
            writeTextValue(os, value);
            break;

        case WapConstants.WSP_PARAMETER_TYPE_INTEGER_VALUE:
            writeInteger(os, Long.parseLong(value));
            break;

        case WapConstants.WSP_PARAMETER_TYPE_DATE_VALUE:
            /*
             * ; The encoding of dates shall be done in number of seconds from ;
             * 1970-01-01, 00:00:00 GMT.
             */
            Long l = Long.valueOf(value);
            writeLongInteger(os, l.longValue());
            break;

        case WapConstants.WSP_PARAMETER_TYPE_DELTA_SECONDS_VALUE:
            // Integer-Value
            Integer i = Integer.valueOf(value);
            writeInteger(os, i.intValue());
            break;

        case WapConstants.WSP_PARAMETER_TYPE_Q_VALUE:
            // TODO: Implement
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
            writeTextString(os, value);
            break;

        case WapConstants.WSP_PARAMETER_TYPE_VERSION_VALUE:
            // TODO: Implement
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
            writeTextString(os, value);
            break;

        case WapConstants.WSP_PARAMETER_TYPE_URI_VALUE:
            // Text-String
            // TODO: Verify
            /*
             * ; URI value should be encoded per [RFC2616], but service user may
             * use a different format.
             */
            writeTextString(os, value);
            break;

        case WapConstants.WSP_PARAMETER_TYPE_TEXT_STRING:
            writeTextString(os, value);
            break;

        case WapConstants.WSP_PARAMETER_TYPE_WELL_KNOWN_CHARSET:
            // Any-Charset | Integer-Value
            // ; Both are encoded using values from Character Set Assignments
            // table in Assigned Numbers
            // TODO: Implement correctly. Currently we always say "UTF8"
            writeInteger(os, WapConstants.MIB_ENUM_UTF_8);
            break;

        case WapConstants.WSP_PARAMETER_TYPE_FIELD_NAME:
            // Token-text | Well-known-field-name
            // TODO: Implement
            writeTextString(os, value);
            break;

        case WapConstants.WSP_PARAMETER_TYPE_SHORT_INTEGER:
            writeShortInteger(os, Integer.parseInt(value));
            break;

        case WapConstants.WSP_PARAMETER_TYPE_CONSTRAINED_ENCODING:
            // Constrained-Encoding == Content-type
            writeContentType(wspEncodingVersion, os, value);
            break;

        default:
            // TODO: Implement
            writeTextString(os, value);
            break;
        }
    }

    public static void writeParameter(byte wspEncodingVersion, OutputStream os, String name, String value) throws IOException
    {
        int parameterType = WspUtil.getParameterType(name);
        int wellKnownParameter = WspUtil.getWellKnownParameterId(wspEncodingVersion, parameterType);

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
            writeTypedValue(wspEncodingVersion, os, getWspParameterType(wellKnownParameter), value);
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
        else
        {
            return ct;
        }
    }
}
