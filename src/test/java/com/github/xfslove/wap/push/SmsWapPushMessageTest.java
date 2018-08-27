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
package com.github.xfslove.wap.push;

import com.github.xfslove.sms.SmsPdu;
import com.github.xfslove.sms.SmsPort;
import com.github.xfslove.sms.ud.SmsUserData;
import com.github.xfslove.util.StringUtil;
import junit.framework.TestCase;

import com.github.xfslove.wap.nokia.NokiaOtaBrowserSettings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 
 * @author Markus Eriksson
 * @version $Id$
 */
public class SmsWapPushMessageTest extends TestCase
{
    public void testOtaBrowserBookmark() throws IOException {
        NokiaOtaBrowserSettings browserSettings = new NokiaOtaBrowserSettings();
        
        // Add a bookmark
        browserSettings.addBookmark("Wap", "http://wap.dk");
        
        // Check contents
        SmsWapPushMessage push = new SmsWapPushMessage(browserSettings);
        // Set ports
        push.setPorts(SmsPort.OTA_SETTINGS_BROWSER, new SmsPort(49154));
        
        SmsUserData userData = push.getUserData();
        
        assertEquals("00062C1F2A6170706C69636174696F6E2F782D7761702D70726F762E62726F777365722D73657474696E67730081EA01016A0045C67F0187151103576170000187171103687474703A2F2F7761702E646B000101", 
                     StringUtil.bytesToHexString(userData.getData()));
        
        // Check SMS segmentation
        SmsPdu[] pdus = push.getPdus();
        
        // The message should fit within one message
        assertEquals(1, pdus.length);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        pdus[0].writeUDHTo(os);

        assertEquals("060504C34FC002",
                StringUtil.bytesToHexString(os.toByteArray()));
        assertEquals("00062C1F2A6170706C69636174696F6E2F782D7761702D70726F762E62726F777365722D73657474696E67730081EA01016A0045C67F0187151103576170000187171103687474703A2F2F7761702E646B000101", 
                StringUtil.bytesToHexString(pdus[0].getUserData().getData()));
    }
    
    public void testSiPush()
    {
        WapSIPush wapSIPush = new WapSIPush("http://www.xyz.com/email/123/abc.wml", "You have 4 new e-mails");
        
        // Check contents
        SmsWapPushMessage push = new SmsWapPushMessage(wapSIPush);
        
        SmsUserData userData = push.getUserData();

/*        
        assertEquals("00062C1F2A6170706C69636174696F6E2F782D7761702D70726F762E62726F777365722D73657474696E67730081EA01016A0045C67F0187151103576170000187171103687474703A2F2F7761702E646B000101", 
                     StringUtil.bytesToHexString(userData.getData()));
*/                     
    }
}
