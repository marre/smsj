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

import java.io.IOException;
import java.io.OutputStream;

import org.marre.mime.MimeHeader;
import org.marre.util.StringUtil;
import org.marre.wap.util.WspUtil;

/**
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class WspHeaderEncoder 
{
	private WspHeaderEncoder()
	{
	}

	public static void writeHeader(OutputStream theOs, MimeHeader theHeader)
	    throws IOException
	{
	    String name = theHeader.getName();
	    int wellKnownHeaderId = StringUtil.findString(WapConstants.HEADER_NAMES, name.toLowerCase());
	
	    switch (wellKnownHeaderId)
	    {
	    case WapConstants.HEADER_ID_ACCEPT:
	        break;
	    case WapConstants.HEADER_ID_ACCEPT_APPLICATION:
	        break;
	    case WapConstants.HEADER_ID_ACCEPT_CHARSET:
	        break;
	    case WapConstants.HEADER_ID_ACCEPT_ENCODING:
	        break;
	    case WapConstants.HEADER_ID_ACCEPT_LANGUAGE:
	        break;
	    case WapConstants.HEADER_ID_ACCEPT_RANGES:
	        break;
	    case WapConstants.HEADER_ID_AGE:
	        break;
	    case WapConstants.HEADER_ID_ALLOW:
	        break;
	    case WapConstants.HEADER_ID_AUTHORIZATION:
	        break;
	    case WapConstants.HEADER_ID_BEARER_INDICATION:
	        break;
	    case WapConstants.HEADER_ID_CACHE_CONTROL:
	        break;
	    case WapConstants.HEADER_ID_CONNECTION:
	        break;
	    case WapConstants.HEADER_ID_CONTENT_BASE:
	        break;
	    case WapConstants.HEADER_ID_CONTENT_DISPOSITION:
	        break;
	    case WapConstants.HEADER_ID_CONTENT_ID:
	        writeHeaderContentID(theOs, theHeader.getValue());
	        break;
	    case WapConstants.HEADER_ID_CONTENT_LANGUAGE:
	        break;
	    case WapConstants.HEADER_ID_CONTENT_LENGTH:
	        break;
	    case WapConstants.HEADER_ID_CONTENT_LOCATION:
	        writeHeaderContentLocation(theOs, theHeader.getValue());
	        break;
	    case WapConstants.HEADER_ID_CONTENT_MD5:
	        break;
	    case WapConstants.HEADER_ID_CONTENT_RANGE:
	        break;
	    case WapConstants.HEADER_ID_CONTENT_TYPE:
	        writeHeaderContentType(theOs, theHeader);
	        break;
	    case WapConstants.HEADER_ID_COOKIE:
	        break;
	    case WapConstants.HEADER_ID_DATE:
	        break;
	    case WapConstants.HEADER_ID_ENCODING_VERSION:
	        break;
	    case WapConstants.HEADER_ID_ETAG:
	        break;
	    case WapConstants.HEADER_ID_EXPECT:
	        break;
	    case WapConstants.HEADER_ID_EXPIRES:
	        break;
	    case WapConstants.HEADER_ID_FROM:
	        break;
	    case WapConstants.HEADER_ID_HOST:
	        break;
	    case WapConstants.HEADER_ID_IF_MATCH:
	        break;
	    case WapConstants.HEADER_ID_IF_MODIFIED_SINCE:
	        break;
	    case WapConstants.HEADER_ID_IF_NONE_MATCH:
	        break;
	    case WapConstants.HEADER_ID_IF_RANGE:
	        break;
	    case WapConstants.HEADER_ID_IF_UNMODIFIED_SINCE:
	        break;
	    case WapConstants.HEADER_ID_LAST_MODIFIED:
	        break;
	    case WapConstants.HEADER_ID_LOCATION:
	        break;
	    case WapConstants.HEADER_ID_MAX_FORWARDS:
	        break;
	    case WapConstants.HEADER_ID_PRAGMA:
	        break;
	    case WapConstants.HEADER_ID_PROFILE:
	        break;
	    case WapConstants.HEADER_ID_PROFILE_DIFF:
	        break;
	    case WapConstants.HEADER_ID_PROFILE_WARNING:
	        break;
	    case WapConstants.HEADER_ID_PROXY_AUTHENTICATE:
	        break;
	    case WapConstants.HEADER_ID_PROXY_AUTHORIZATION:
	        break;
	    case WapConstants.HEADER_ID_PUBLIC:
	        break;
	    case WapConstants.HEADER_ID_PUSH_FLAG:
	        break;
	    case WapConstants.HEADER_ID_RANGE:
	        break;
	    case WapConstants.HEADER_ID_REFERER:
	        break;
	    case WapConstants.HEADER_ID_RETRY_AFTER:
	        break;
	    case WapConstants.HEADER_ID_SERVER:
	        break;
	    case WapConstants.HEADER_ID_SET_COOKIE:
	        break;
	    case WapConstants.HEADER_ID_TE:
	        break;
	    case WapConstants.HEADER_ID_TRAILER:
	        break;
	    case WapConstants.HEADER_ID_TRANSFER_ENCODING:
	        break;
	    case WapConstants.HEADER_ID_UPGRADE:
	        break;
	    case WapConstants.HEADER_ID_USER_AGENT:
	        break;
	    case WapConstants.HEADER_ID_VARY:
	        break;
	    case WapConstants.HEADER_ID_VIA:
	        break;
	    case WapConstants.HEADER_ID_WARNING:
	        break;
	    case WapConstants.HEADER_ID_WWW_AUTHENTICATE:
	        break;
	    case WapConstants.HEADER_ID_X_WAP_APPLICATION_ID:
	        writeHeaderXWapApplicationId(theOs, theHeader.getValue());
	        break;
	    case WapConstants.HEADER_ID_X_WAP_CONTENT_URI:
	        break;
	    case WapConstants.HEADER_ID_X_WAP_INITIATOR_URI:
	        break;
	    case WapConstants.HEADER_ID_X_WAP_SECURITY:
	        break;
	    case WapConstants.HEADER_ID_X_WAP_TOD:
	        break;
	
	    default:
	        // Custom header
	        WspUtil.writeTokenText(theOs, theHeader.getName());
	        WspUtil.writeTextString(theOs, theHeader.getValue());
	        break;            
	    }
	}
	
	/**
	 * Writes a wsp encoded content-id header as specified in
	 * WAP-230-WSP-20010705-a.pdf.
	 *
	 * @param theOs
	 * @param theContentLocation
	 * @throws IOException
	 */
	public static void writeHeaderContentID(OutputStream theOs, String theContentId)
	    throws IOException
	{
	    // TODO: Verify
	    WspUtil.writeShortInteger(theOs, WapConstants.HEADER_ID_CONTENT_ID);
	    WspUtil.writeQuotedString(theOs, theContentId);
	}
	
	/**
	 * Writes a wsp encoded content-location header as specified in
	 * WAP-230-WSP-20010705-a.pdf.
	 *
	 * @param theOs
	 * @param theContentLocation
	 * @throws IOException
	 */
	public static void writeHeaderContentLocation(OutputStream theOs, String theContentLocation)
	    throws IOException
	{
	    // TODO: Verify
	    WspUtil.writeShortInteger(theOs, WapConstants.HEADER_ID_CONTENT_LOCATION);
	    WspUtil.writeTextString(theOs, theContentLocation);
	}
	
	public static void writeHeaderContentType(OutputStream theOs, String theContentType)
	    throws IOException
	{
	    WspUtil.writeShortInteger(theOs, WapConstants.HEADER_ID_CONTENT_TYPE);
	    WspUtil.writeContentType(theOs, theContentType);
	}
	
	public static void writeHeaderContentType(OutputStream theOs, MimeHeader theContentType)
	    throws IOException
	{
	    WspUtil.writeShortInteger(theOs, WapConstants.HEADER_ID_CONTENT_TYPE);
	    WspUtil.writeContentType(theOs, theContentType);
	}
	
	/**
	 * Writes a wsp encoded X-Wap-Application-Id header as specified in
	 * WAP-230-WSP-20010705-a.pdf.
	 *
	 * @param theOs
	 * @param theAppId
	 * @throws IOException
	 */
	public static void writeHeaderXWapApplicationId(OutputStream theOs, String theAppId)
	    throws IOException
	{
	    int wellKnownAppId = StringUtil.findString(WapConstants.PUSH_APP_IDS, theAppId.toLowerCase());
	
	    WspUtil.writeShortInteger(theOs, WapConstants.HEADER_ID_X_WAP_APPLICATION_ID);
	
	    if (wellKnownAppId == -1)
	    {
	        WspUtil.writeTextString(theOs, theAppId);
	    }
	    else
	    {
	        WspUtil.writeInteger(theOs, wellKnownAppId);
	    }
	}
}
