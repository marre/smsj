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
package org.marre.wap.util;

import java.io.IOException;
import java.io.OutputStream;

import org.marre.mime.MimeHeader;
import org.marre.util.StringUtil;
import org.marre.wap.MmsConstants;

/**
 * @author Markus Eriksson
 */
public class MmsUtil
{
    private MmsUtil()
    {
    }
    
    public static void writeHeader(OutputStream theOs, MimeHeader theHeader)
        throws IOException
    {
        String name = theHeader.getName();
        int wellKnownHeaderId = StringUtil.findString(MmsConstants.HEADER_NAMES, name.toLowerCase());

        switch (wellKnownHeaderId)
        {
            case MmsConstants.HEADER_ID_BCC:
                break;
            case MmsConstants.HEADER_ID_CC:
                break;
            case MmsConstants.HEADER_ID_CONTENT_LOCATION:
                MmsUtil.writeHeaderContentLocation(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_CONTENT_TYPE:
                MmsUtil.writeHeaderContentType(theOs, theHeader);
                break;
            case MmsConstants.HEADER_ID_DATE:
                break;
            case MmsConstants.HEADER_ID_DELIVERY_REPORT:
                break;
            case MmsConstants.HEADER_ID_DELIVERY_TIME:
                break;
            case MmsConstants.HEADER_ID_EXPIRY:
                break;
            case MmsConstants.HEADER_ID_FROM:
                break;
            case MmsConstants.HEADER_ID_MESSAGE_CLASS:
                break;
            case MmsConstants.HEADER_ID_MESSAGE_ID:
                break;
            case MmsConstants.HEADER_ID_MESSAGE_TYPE:
                break;
            case MmsConstants.HEADER_ID_MMS_VERSION:
                break;
            case MmsConstants.HEADER_ID_MESSAGE_SIZE:
                break;
            case MmsConstants.HEADER_ID_PRIORITY:
                break;
            case MmsConstants.HEADER_ID_READ_REPLY:
                break;
            case MmsConstants.HEADER_ID_REPORT_ALLOWED:
                break;
            case MmsConstants.HEADER_ID_RESPONSE_STATUS:
                break;
            case MmsConstants.HEADER_ID_RESPONSE_TEXT:
                break;
            case MmsConstants.HEADER_ID_SENDER_VISIBILITY:
                break;
            case MmsConstants.HEADER_ID_STATUS:
                break;
            case MmsConstants.HEADER_ID_SUBJECT:
                break;
            case MmsConstants.HEADER_ID_TO:
                break;
            case MmsConstants.HEADER_ID_TRANSACITON_ID:
                break;

        default:
            // Application-header
            WspUtil.writeTokenText(theOs, theHeader.getName());
            WspUtil.writeTextString(theOs, theHeader.getValue());
            break;
        }
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
        WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_CONTENT_LOCATION);
        WspUtil.writeTextString(theOs, theContentLocation);
    }

    public static void writeHeaderContentType(OutputStream theOs, String theContentType)
        throws IOException
    {
        WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_CONTENT_TYPE);
        WspUtil.writeContentType(theOs, theContentType);
    }

    public static void writeHeaderContentType(OutputStream theOs, MimeHeader theContentType)
        throws IOException
    {
        WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_CONTENT_TYPE);
        WspUtil.writeContentType(theOs, theContentType);
    }    
}
