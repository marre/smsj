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

import junit.framework.TestCase;

import org.marre.sms.SmsAddress;
import org.marre.sms.SmsDcs;
import org.marre.sms.SmsException;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsPdu;
import org.marre.sms.SmsTextMessage;
import org.marre.sms.SmsUdhElement;
import org.marre.sms.SmsUdhUtil;
import org.marre.util.StringUtil;

/**
 * 
 * @author Markus Eriksson
 * @version $Id$
 */

public class GsmEncoderTest extends TestCase
{    
    class UDH5Msg extends SmsTextMessage 
    {
        public UDH5Msg(String txt) 
        {
            super(txt);
        }
        
        public SmsUdhElement[] getUdhElements() 
        {
            return new SmsUdhElement[]{SmsUdhUtil.get8BitApplicationPortUdh(10,11)};
        }
    }
    
    public void testUdh5() throws SmsException
    {
        SmsMessage udhMsg;
        SmsPdu[] smsPdus;
        byte[] data;

        // 5 bytes UDH, 2 septets
        udhMsg = new UDH5Msg("01");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F30000080404020A0BC062",
                StringUtil.bytesToHexString(data));
        
        // 5 bytes UDH, 3 septets
        udhMsg = new UDH5Msg("012");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F30000090404020A0BC06232",
                StringUtil.bytesToHexString(data));
        
        // 5 bytes UDH, 4 septets
        udhMsg = new UDH5Msg("0123");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000A0404020A0BC062B219",
                StringUtil.bytesToHexString(data));
        
        // 5 bytes UDH, 5 septets
        udhMsg = new UDH5Msg("01234");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000B0404020A0BC062B2190D",
                StringUtil.bytesToHexString(data));
        
        // 5 bytes UDH, 6 septets
        udhMsg = new UDH5Msg("012345");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000C0404020A0BC062B219AD06",
                StringUtil.bytesToHexString(data));
        
        // 5 bytes UDH, 7 septets
        udhMsg = new UDH5Msg("0123456");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000D0404020A0BC062B219AD6603",
                StringUtil.bytesToHexString(data));
        
        // 5 bytes UDH, 8 septets
        udhMsg = new UDH5Msg("01234567");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000E0404020A0BC062B219AD66BB01",
                StringUtil.bytesToHexString(data));
        
        // 5 bytes UDH, 9 septets
        udhMsg = new UDH5Msg("012345678");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000F0404020A0BC062B219AD66BBE100",
                StringUtil.bytesToHexString(data));
    }
    
    class UDH6Msg extends SmsTextMessage 
    {
        public UDH6Msg(String txt) 
        {
            super(txt);
        }
        
        public SmsUdhElement[] getUdhElements() 
        {
            return new SmsUdhElement[]{SmsUdhUtil.get8BitConcatUdh(3, 2, 1)};
        }
    }
    
    public void testUdh6() throws SmsException
    {
        SmsMessage udhMsg;
        SmsPdu[] smsPdus;
        byte[] data;

        // 6 bytes UDH, 2 septets
        udhMsg = new UDH6Msg("01");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F30000090500030302016031",
                StringUtil.bytesToHexString(data));
        
        // 6 bytes UDH, 3 septets
        udhMsg = new UDH6Msg("012");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000A050003030201603119",
                StringUtil.bytesToHexString(data));
        
        // 6 bytes UDH, 4 septets
        udhMsg = new UDH6Msg("0123");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000B0500030302016031D90C",
                StringUtil.bytesToHexString(data));
        
        // 6 bytes UDH, 5 septets
        udhMsg = new UDH6Msg("01234");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000C0500030302016031D98C06",
                StringUtil.bytesToHexString(data));
        
        // 6 bytes UDH, 6 septets
        udhMsg = new UDH6Msg("012345");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000D0500030302016031D98C5603",
                StringUtil.bytesToHexString(data));
        
        // 6 bytes UDH, 7 septets
        udhMsg = new UDH6Msg("0123456");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000E0500030302016031D98C56B301",
                StringUtil.bytesToHexString(data));
        
        // 6 bytes UDH, 8 septets
        udhMsg = new UDH6Msg("01234567");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000F0500030302016031D98C56B3DD00",
                StringUtil.bytesToHexString(data));
        
        // 6 bytes UDH, 9 septets
        udhMsg = new UDH6Msg("012345678");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F30000100500030302016031D98C56B3DD70",
                StringUtil.bytesToHexString(data));
    }
    
    class UDH7Msg extends SmsTextMessage 
    {
        public UDH7Msg(String txt) 
        {
            super(txt);
        }
        
        public SmsUdhElement[] getUdhElements() 
        {
            return new SmsUdhElement[]{SmsUdhUtil.get16BitApplicationPortUdh(10,11)};
        }
    }
    
    public void testUdh7() throws SmsException
    {
        SmsMessage udhMsg;
        SmsPdu[] smsPdus;
        byte[] data;

        // 7 bytes UDH, 5 septets
        udhMsg = new UDH7Msg("01234");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000D060504000A000BB0986C4603",
                StringUtil.bytesToHexString(data));
        
        // 7 bytes UDH, 6 septets
        udhMsg = new UDH7Msg("012345");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000E060504000A000BB0986C46AB01",
                StringUtil.bytesToHexString(data));
        
        // 7 bytes UDH, 7 septets
        udhMsg = new UDH7Msg("0123456");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F300000F060504000A000BB0986C46ABD900",
                StringUtil.bytesToHexString(data));
        
        // 5 bytes UDH, 8 septets
        udhMsg = new UDH7Msg("01234567");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F3000010060504000A000BB0986C46ABD96E",
                StringUtil.bytesToHexString(data));
        
        // 5 bytes UDH, 9 septets
        udhMsg = new UDH7Msg("012345678");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F3000011060504000A000BB0986C46ABD96E38",
                StringUtil.bytesToHexString(data));

        // 5 bytes UDH, 10 septets
        udhMsg = new UDH7Msg("0123456789");
        smsPdus = udhMsg.getPdus();
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("4100039121F3000012060504000A000BB0986C46ABD96EB81C",
                StringUtil.bytesToHexString(data));        
    }
    
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
        assertEquals("0100039121F30000A0B0986C46ABD96EB81C2C269BD16AB61B2E078BC966B49AED86CBC162B219AD66BBE172B0986C46ABD96EB81C2C269BD16AB61B2E078BC966B49AED86CBC162B219AD66BBE172B0986C46ABD96EB81C2C269BD16AB61B2E078BC966B49AED86CBC162B219AD66BBE172B0986C46ABD96EB81C2C269BD16AB61B2E078BC966B49AED86CBC162B219AD66BBE172",
                     StringUtil.bytesToHexString(data));
        
        // 0 chars
        textMsg = new SmsTextMessage("");
        smsPdus = textMsg.getPdus();
        assertEquals(1, smsPdus.length);
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("0100039121F3000000",
                     StringUtil.bytesToHexString(data));        
    }
    
    public void testOctetEncoder() throws SmsException
    {
        SmsTextMessage msg;
        SmsPdu[] smsPdus;
        byte[] data;
     
        // 70 chars should fit within one SMS
        msg = new SmsTextMessage("0123456789012345678901234567890123456789012345678901234567890123456789", SmsDcs.ALPHABET_UCS2, SmsDcs.MSG_CLASS_UNKNOWN);
        smsPdus = msg.getPdus();
        assertEquals(1, smsPdus.length);
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("0100039121F300088C0030003100320033003400350036003700380039003000310032003300340035003600370038003900300031003200330034003500360037003800390030003100320033003400350036003700380039003000310032003300340035003600370038003900300031003200330034003500360037003800390030003100320033003400350036003700380039",
                     StringUtil.bytesToHexString(data));
                
        // 0 chars
        msg = new SmsTextMessage("", SmsDcs.ALPHABET_UCS2, SmsDcs.MSG_CLASS_UNKNOWN);
        smsPdus = msg.getPdus();
        assertEquals(1, smsPdus.length);
        data = GsmEncoder.encodePdu(smsPdus[0], new SmsAddress("123"), new SmsAddress("456"));
        assertEquals("0100039121F3000800",
                     StringUtil.bytesToHexString(data));        
    }    
}
