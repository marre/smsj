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
package org.marre.sms;

import org.marre.sms.transport.gsm.GsmEncoder;
import org.marre.util.StringUtil;

import junit.framework.TestCase;

/**
 * 
 * @author Markus
 * @version $Id$
 */

public class SmsPduUtilTest extends TestCase
{
    public void testSeptetEncoderBitLength() throws SmsException
    {
        byte[] srcData = StringUtil.hexStringToBytes("FFFFFFFFFFFFFFFF");
        byte[] dstData;
        
        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 0, 64);
        assertEquals("FFFFFFFFFFFFFFFF", StringUtil.bytesToHexString(dstData));

        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 0, 63);
        assertEquals("FFFFFFFFFFFFFF7F", StringUtil.bytesToHexString(dstData));
        
        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 0, 62);
        assertEquals("FFFFFFFFFFFFFF3F", StringUtil.bytesToHexString(dstData));                
        
        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 0, 61);
        assertEquals("FFFFFFFFFFFFFF1F", StringUtil.bytesToHexString(dstData));                
        
        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 0, 60);
        assertEquals("FFFFFFFFFFFFFF0F", StringUtil.bytesToHexString(dstData));                
        
        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 0, 59);
        assertEquals("FFFFFFFFFFFFFF07", StringUtil.bytesToHexString(dstData));                
        
        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 0, 58);
        assertEquals("FFFFFFFFFFFFFF03", StringUtil.bytesToHexString(dstData));                
        
        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 0, 57);
        assertEquals("FFFFFFFFFFFFFF01", StringUtil.bytesToHexString(dstData));                
        
        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 0, 56);
        assertEquals("FFFFFFFFFFFFFF00", StringUtil.bytesToHexString(dstData));
    }
    
    public void testSeptetEncoderDstStart() throws SmsException
    {
        byte[] srcData = StringUtil.hexStringToBytes("FFFF");
        byte[] dstData;
        
        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 0, 8);
        assertEquals("FF00", StringUtil.bytesToHexString(dstData));
        
        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 1, 8);
        assertEquals("FE01", StringUtil.bytesToHexString(dstData));

        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 2, 8);
        assertEquals("FC03", StringUtil.bytesToHexString(dstData));

        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 3, 8);
        assertEquals("F807", StringUtil.bytesToHexString(dstData));

        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 4, 8);
        assertEquals("F00F", StringUtil.bytesToHexString(dstData));

        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 5, 8);
        assertEquals("E01F", StringUtil.bytesToHexString(dstData));

        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 6, 8);
        assertEquals("C03F", StringUtil.bytesToHexString(dstData));

        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 7, 8);
        assertEquals("807F", StringUtil.bytesToHexString(dstData));
        
        dstData = new byte[srcData.length];
        SmsPduUtil.arrayCopy(srcData, 0, dstData, 0, 8, 8);
        assertEquals("00FF", StringUtil.bytesToHexString(dstData));                
    }    
    
}
