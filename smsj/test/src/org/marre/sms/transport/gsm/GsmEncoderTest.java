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
package org.marre.sms.transport.gsm;

import org.marre.sms.SmsAddress;
import org.marre.sms.SmsException;
import org.marre.sms.SmsPdu;
import org.marre.sms.SmsTextMessage;
import org.marre.sms.SmsUdhElement;
import org.marre.sms.SmsUdhUtil;
import org.marre.util.StringUtil;

import junit.framework.TestCase;

/**
 * 
 * @author Markus Eriksson
 * @version $Id$
 */

public class GsmEncoderTest extends TestCase
{
    public void testSeptetEncoder() throws SmsException
    {
        SmsTextMessage textMsg;
        SmsPdu[] smsPdus;
        byte[] data;
     
        // 160 chars should fit within one SMS
        textMsg = new SmsTextMessage("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
        smsPdus = textMsg.getPdus();
        assertEquals(1, smsPdus.length);
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("000100039121F30000A0B0986C46ABD96EB81C2C269BD16AB61B2E078BC966B49AED86CBC162B219AD66BBE172B0986C46ABD96EB81C2C269BD16AB61B2E078BC966B49AED86CBC162B219AD66BBE172B0986C46ABD96EB81C2C269BD16AB61B2E078BC966B49AED86CBC162B219AD66BBE172B0986C46ABD96EB81C2C269BD16AB61B2E078BC966B49AED86CBC162B219AD66BBE172",
                     StringUtil.bytesToHexString(data));
        
        // 0 chars
        textMsg = new SmsTextMessage("");
        smsPdus = textMsg.getPdus();
        assertEquals(1, smsPdus.length);
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("000100039121F3000000",
                     StringUtil.bytesToHexString(data));        
    }    
}
