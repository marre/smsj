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

  public static void writeApplicationHeader(OutputStream os, String name, String value) throws IOException {
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
      WspHeaderEncoder.writeApplicationHeader(os, "Content-ID", contentId);
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
      writeApplicationHeader(os, "X-Wap-Application-Id", appId);
    }
  }
}
