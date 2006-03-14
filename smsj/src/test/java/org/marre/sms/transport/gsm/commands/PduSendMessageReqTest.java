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
package org.marre.sms.transport.gsm.commands;

import junit.framework.TestCase;

import org.marre.sms.transport.gsm.GsmComm;
import org.marre.sms.transport.gsm.MockSerialComm;
import org.marre.util.StringUtil;

public class PduSendMessageReqTest extends TestCase
{
    public void testSuccessfulCMGS() throws Exception
    {
        GsmComm comm = new MockSerialComm(new String[]{
                "> ", 
                "+CMGS: 97",
                "",
                "OK"});
        
        PduSendMessageReq req = new PduSendMessageReq(StringUtil.hexStringToBytes("41000C919333289868390000A0050003B5020140201008040281623010080402814020190C040281402010680603814020100804A2C1402010080402816A30100804028140201B0C040281402010E80603814020100804C2C140201008040281723010080402814031180C0402814020502C060381402010081493C1402010080402C56630100804028140311A0C040281402050AC06038140"));
        
        req.send(comm);
    }
}
