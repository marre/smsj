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

import junit.framework.TestCase;
import org.marre.wsp.WspConstants;
import org.marre.wsp.WspEncodingVersion;
import org.marre.wsp.WspUtil;

/**
 * 
 * @author Markus
 * @version $Id$
 */

public class WspUtilTest extends TestCase
{
    public void testGetHeaderType() 
    {
        // Just test some of the headers
        assertEquals(WspConstants.HEADER_ACCEPT,                WspUtil.getHeaderType("accept"));
        assertEquals(WspConstants.HEADER_CONTENT_LENGTH,        WspUtil.getHeaderType("content-length"));
        assertEquals(WspConstants.HEADER_IF_UNMODIFIED_SINCE,   WspUtil.getHeaderType("if-unmodified-since"));
        assertEquals(WspConstants.HEADER_PROFILE_DIFF,          WspUtil.getHeaderType("profile-diff"));
        assertEquals(WspConstants.HEADER_CONTENT_DISPOSITION,   WspUtil.getHeaderType("content-disposition"));

        // Some bogus headers
        assertEquals(-1, WspUtil.getHeaderType("x-d-h-h-totally-unknown-header"));
        assertEquals(-1, WspUtil.getHeaderType(null));
        assertEquals(-1, WspUtil.getHeaderType(""));
        assertEquals(-1, WspUtil.getHeaderType("accep"));
        assertEquals(-1, WspUtil.getHeaderType("content-len"));
    }
    
    public void testGetWellKnownHeaderId() 
    {
        // Just test some of the headers
        assertEquals(0x00, WspUtil.getWellKnownHeaderId(WspEncodingVersion.VERSION_1_1, WspConstants.HEADER_ACCEPT));
        assertEquals(0x0D, WspUtil.getWellKnownHeaderId(WspEncodingVersion.VERSION_1_1, WspConstants.HEADER_CONTENT_LENGTH));
        assertEquals(0x1B, WspUtil.getWellKnownHeaderId(WspEncodingVersion.VERSION_1_1, WspConstants.HEADER_IF_UNMODIFIED_SINCE));
        assertEquals(0x2E, WspUtil.getWellKnownHeaderId(WspEncodingVersion.VERSION_1_1, WspConstants.HEADER_CONTENT_DISPOSITION));
        assertEquals(0x36, WspUtil.getWellKnownHeaderId(WspEncodingVersion.VERSION_1_2, WspConstants.HEADER_PROFILE_DIFF));

        // Unknown 1.1 headers
        assertEquals(-1, WspUtil.getWellKnownHeaderId(WspEncodingVersion.VERSION_1_1, WspConstants.HEADER_PROFILE_DIFF));
        
        // Test cache-control
        assertEquals(0x08, WspUtil.getWellKnownHeaderId(WspEncodingVersion.VERSION_1_1, WspConstants.HEADER_CACHE_CONTROL));
        assertEquals(0x08, WspUtil.getWellKnownHeaderId(WspEncodingVersion.VERSION_1_2, WspConstants.HEADER_CACHE_CONTROL));
        assertEquals(0x3D, WspUtil.getWellKnownHeaderId(WspEncodingVersion.VERSION_1_3, WspConstants.HEADER_CACHE_CONTROL));
        assertEquals(0x47, WspUtil.getWellKnownHeaderId(WspEncodingVersion.VERSION_1_4, WspConstants.HEADER_CACHE_CONTROL));
    }
    
    public void testGetWellKnownContentType() 
    {
        // Just test some of the headers
        assertEquals(0x00, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_1, WspUtil.getContentType("*/*")));
        assertEquals(0x2D, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_1, WspUtil.getContentType("text/vnd.wap.si")));
        assertEquals(0x40, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_5, WspUtil.getContentType("application/vnd.wap.locc+wbxml")));

        // Unknown 1.1 headers
        assertEquals(-1, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_1, WspUtil.getContentType("some/strange-content-type")));
        
        // Test differen encoding versions
        assertEquals(-1, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_1, WspUtil.getContentType("application/vnd.wap.locc+wbxml")));
        assertEquals(-1, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_2, WspUtil.getContentType("application/vnd.wap.locc+wbxml")));
        assertEquals(-1, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_3, WspUtil.getContentType("application/vnd.wap.locc+wbxml")));
        assertEquals(-1, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_4, WspUtil.getContentType("application/vnd.wap.locc+wbxml")));
        assertEquals(0x40, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_5, WspUtil.getContentType("application/vnd.wap.locc+wbxml")));
        
        assertEquals(-1, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_1, WspUtil.getContentType("application/vnd.wap.sic")));
        assertEquals(0x2E, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_2, WspUtil.getContentType("application/vnd.wap.sic")));
        assertEquals(0x2E, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_3, WspUtil.getContentType("application/vnd.wap.sic")));
        assertEquals(0x2E, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_4, WspUtil.getContentType("application/vnd.wap.sic")));
        assertEquals(0x2E, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_5, WspUtil.getContentType("application/vnd.wap.sic")));
        
        assertEquals(-1, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_1, WspUtil.getContentType("application/vnd.wap.sia")));
        assertEquals(0x34, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_2, WspUtil.getContentType("application/vnd.wap.sia")));
        assertEquals(0x34, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_3, WspUtil.getContentType("application/vnd.wap.sia")));
        assertEquals(0x34, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_4, WspUtil.getContentType("application/vnd.wap.sia")));
        assertEquals(0x34, WspUtil.getWellKnownContentTypeId(WspEncodingVersion.VERSION_1_5, WspUtil.getContentType("application/vnd.wap.sia")));
    }        
}
