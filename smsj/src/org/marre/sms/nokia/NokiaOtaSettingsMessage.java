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
package org.marre.sms.nokia;

import java.io.*;

import org.marre.sms.*;
import org.marre.sms.util.*;
import org.marre.wap.*;
import org.marre.wap.util.*;
import org.marre.mime.*;

/**
 *  Nokia OTA Settings Message (based on 7.0 spec)
 * 
 * @author Fabio Corneti
 * @version
 */

public class NokiaOtaSettingsMessage extends SmsConcatMessage {
    
    private byte[] myOtaSettingsMsg;
    
    //Liquidterm: this class will change to a
    protected NokiaOtaSettingsMessage() {
        super(SmsConstants.DCS_DEFAULT_8BIT);
    }
    
    /** 
     * Creates a Generic Nokia OTA Settings message 
     *
     * @param theOtaBrowserSettingsMsg byte array containing the OTA message (WSP
     * Encoded)
     * 
     * @param theDestPort integer containing the destination WDP port
     * @param theOrigPort integer containing the origin WDP port
     */
    public NokiaOtaSettingsMessage(byte[] theOtaSettingsMsg, int theDestPort, 
        int theOrigPort) 
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);
        myOtaSettingsMsg = theOtaSettingsMsg;
        
        setContent(
            new SmsUdhElement[] {
                SmsUdhUtil.get16BitApplicationPortUdh(theDestPort, theOrigPort)
            },
            myOtaSettingsMsg,
            myOtaSettingsMsg.length); 
    }
    
    protected void createMessage(byte[] theOtaSettingsWbxml, MimeContentType theContentType,
        int theDestPort, int theOrigPort) 
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try
        {
            // WSP HEADER

            // TID - Transaction ID
            // FIXME: Should perhaps set TID to something useful?
            WspUtil.writeUint8(baos, 0x01);

            // Type
            WspUtil.writeUint8(baos, WapConstants.PDU_TYPE_PUSH);

            // Create headers first
            ByteArrayOutputStream headers = new ByteArrayOutputStream();

            // Content-type
            WspUtil.writeContentType(headers, theContentType);
            
            headers.close();

            // Headers created, write headers lenght and headers to baos

            // HeadersLen - Length of Content-type and Headers
            WspUtil.writeUintvar(baos, headers.size());

            // Headers
            baos.write(headers.toByteArray());

            // Data
            baos.write(theOtaSettingsWbxml);

            // Done
            baos.close();
        }
        catch (IOException ex)
        {
            // Shouldn't happen
        }

        myOtaSettingsMsg = baos.toByteArray();

        setContent(
            new SmsUdhElement[] {
                SmsUdhUtil.get16BitApplicationPortUdh(theDestPort, theOrigPort)
            },
            myOtaSettingsMsg,
            myOtaSettingsMsg.length);
    }
}
