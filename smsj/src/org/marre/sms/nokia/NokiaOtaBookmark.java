/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
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
 * The Initial Developer of the Original Code is Boris von Loesch.
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s): Markus Eriksson
 *
 * ***** END LICENSE BLOCK ***** */
package org.marre.sms.nokia;

import java.io.*;

import org.marre.sms.*;
import org.marre.wap.nokia.*;
import org.marre.mime.*;

/**
 * Creates a Nokia Ota Settings bookmark message
 *
 * @author  liquidterm
 */
public class NokiaOtaBookmark extends NokiaOtaSettingsMessage {
    
    /**
     * Creates a Nokia Ota Settings bookmark message
     *
     * @param theTitle the title of the bookmark
     * @param theUri the URI of the bookmark
     */
    public NokiaOtaBookmark(String theTitle, String theUri) {
        OtaBookmark otaBookmark = new OtaBookmark(theTitle, theUri);
        byte[] otaBookmarkMsg = otaBookmark.toByteArray();
        createMessage(otaBookmarkMsg, new MimeContentType("application/x-wap-prov.browser-bookmarks"), 
            SmsConstants.PORT_OTA_SETTINGS_BROWSER, 0);
    }
}
