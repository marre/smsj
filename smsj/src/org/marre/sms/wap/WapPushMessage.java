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
package org.marre.sms.wap;

import java.io.*;

import org.marre.wap.*;
import org.marre.wap.util.*;
import org.marre.mime.MimeBodyPart;
import org.marre.mime.MimeContentType;
import org.marre.mime.encoder.wap.WapMimeEncoder;

import org.marre.sms.*;
import org.marre.sms.util.*;

/**
 * Connectionless WAP push message with SMS as bearer.
 * <p>
 * It will support the setting of "content-type" and "X-Wap-Application-Id",
 * no other headers will be supported.
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public class WapPushMessage extends SmsConcatMessage
{
    private byte[] myPushMsg;

    protected WapPushMessage()
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);
    }

    /**
     * Sends a CL WAP push message OTA with SMS.
     *
     * @param thePushMsg The push message (Must be WSP encoded)
     */
    public WapPushMessage(byte[] thePushMsg)
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);
        
        myPushMsg = thePushMsg;
        
        setContent(
            new SmsUdhElement[] {
                SmsUdhUtil.get16BitApplicationPortUdh(SmsConstants.PORT_WAP_PUSH,
                                                      SmsConstants.PORT_WAP_WSP)
            },
            myPushMsg,
            myPushMsg.length);        
    }

    /**
     * Creates a CL WAP push message OTA with SMS.
     *
     * @param thePushMsg The push message
     * @param theContentType Content-type of the push
     * @param theAppId WAP Push Application ID
     * @param theContentLocation Content-Location URI
     */
    public WapPushMessage(byte[] thePushMsg, MimeContentType theContentType, String theAppId, String theContentLocation)
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);
        createMessage(thePushMsg, theContentType, theAppId, theContentLocation);
    }
    
    /**
     * Creates a CL WAP push message OTA with SMS.
     *
     * @param thePushMsg The push message
     * @param theContentType Content-type of the push
     * @param theAppId WAP Push Application ID
     * @param theContentLocation Content-Location URI
     */
    public WapPushMessage(byte[] thePushMsg, String theContentType, String theAppId, String theContentLocation)
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);
        createMessage(thePushMsg, theContentType, theAppId, theContentLocation);
    }
    
    public WapPushMessage(MimeBodyPart thePushMsg)
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);
        createMessage(thePushMsg);
    }
    
    protected void createMessage(byte[] thePushMsg, String theContentType, String theAppId, String theContentLocation)
    {
        this.createMessage(thePushMsg, new MimeContentType(theContentType), theAppId, theContentLocation);
    }
    
    protected void createMessage(byte[] thePushMsg, MimeContentType theContentType, String theAppId, String theContentLocation)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try
        {
            //
            // WSP HEADER
            //

            // TID - Transaction ID
            // FIXME: Should perhaps set TID to something useful?
            WspUtil.writeUint8(baos, 0x00);

            // Type
            WspUtil.writeUint8(baos, WapConstants.PDU_TYPE_PUSH);

            //
            // WAP PUSH FIELDS
            //

            // Create headers first
            ByteArrayOutputStream headers = new ByteArrayOutputStream();

            // Content-type
            WspUtil.writeContentType(headers, theContentType);

            // WAP-HEADERS
            // There could be more wap headers, but we currently only use
            // the Application ID and content location

            // App ID
            if( theAppId != null)
            {
                WspHeaderEncoder.writeHeaderXWapApplicationId(headers, theAppId);
            }
            
            // Content Location
            if( theContentLocation != null)
            {
                WspHeaderEncoder.writeHeaderContentLocation(headers, theContentLocation);
            }
            
            // Done with the headers...
            headers.close();

            // Headers created, write headers lenght and headers to baos

            // HeadersLen - Length of Content-type and Headers
            WspUtil.writeUintvar(baos, headers.size());

            // Headers
            baos.write(headers.toByteArray());

            // Data
            baos.write(thePushMsg);

            // Done
            baos.close();
        }
        catch (IOException ex)
        {
            // Shouldn't happen
        }

        myPushMsg = baos.toByteArray();

        setContent(
            new SmsUdhElement[] {
                SmsUdhUtil.get16BitApplicationPortUdh(SmsConstants.PORT_WAP_PUSH,
                                                      SmsConstants.PORT_WAP_WSP)
            },
            myPushMsg,
            myPushMsg.length);
    }
    
    protected void createMessage(MimeBodyPart thePushMsg)
    {
        WapMimeEncoder wapMimeEncoder = new WapMimeEncoder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try
        {
            //
            // WSP HEADER
            //

            // TID - Transaction ID
            // FIXME: Should perhaps set TID to something useful?
            WspUtil.writeUint8(baos, 0x00);

            // Type
            WspUtil.writeUint8(baos, WapConstants.PDU_TYPE_PUSH);

            //
            // WAP PUSH FIELDS
            //

            // Create headers first
            ByteArrayOutputStream headers = new ByteArrayOutputStream();

            // Content-type
            wapMimeEncoder.writeContentType(headers, thePushMsg);

            // WAP-HEADERS
            wapMimeEncoder.writeHeaders(headers, thePushMsg);

            // Done with the headers...
            headers.close();

            // Headers created, write headers lenght and headers to baos

            // HeadersLen - Length of Content-type and Headers
            WspUtil.writeUintvar(baos, headers.size());

            // Headers
            baos.write(headers.toByteArray());

            // Data
            wapMimeEncoder.writeData(baos, thePushMsg);

            // Done
            baos.close();
        }
        catch (IOException ex)
        {
            // Shouldn't happen
        }

        myPushMsg = baos.toByteArray();

        setContent(
            new SmsUdhElement[] {
                SmsUdhUtil.get16BitApplicationPortUdh(SmsConstants.PORT_WAP_PUSH,
                                                      SmsConstants.PORT_WAP_WSP)
            },
            myPushMsg,
            myPushMsg.length);
    }
    
    /**
     * Returns the wsp encoded wap push message without any SMS headers
     * <p>
     * Mostly used for debugging...
     *
     * @return
     */
    public byte[] getPushMsg()
    {
        return myPushMsg;
    }
}
