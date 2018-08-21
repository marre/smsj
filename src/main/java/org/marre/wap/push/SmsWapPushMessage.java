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
package org.marre.wap.push;

import org.marre.mime.MimeBodyPart;
import org.marre.mime.MimeContentType;
import org.marre.sms.SmsPort;
import org.marre.sms.SmsPortAddressedMessage;
import org.marre.sms.SmsUserData;
import org.marre.mime.WapMimeEncoder;
import org.marre.wap.wbxml.WbxmlDocument;
import org.marre.wsp.WspConstants;
import org.marre.wsp.WspEncodingVersion;
import org.marre.wsp.WspUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Connectionless WAP push message with SMS as bearer.
 *
 * It supports the "content-type" and "X-Wap-Application-Id" headers.
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public class SmsWapPushMessage extends SmsPortAddressedMessage
{
    protected WspEncodingVersion wspEncodingVersion_ = WspEncodingVersion.VERSION_1_2;
    protected MimeBodyPart pushMsg_;
        
    protected SmsWapPushMessage()
    {
        super(SmsPort.WAP_PUSH, SmsPort.WAP_WSP);
    }
    
    public SmsWapPushMessage(MimeBodyPart pushMsg)
    {
        this();
        
        pushMsg_ = pushMsg;
    }
    
    public SmsWapPushMessage(WbxmlDocument pushMsg, MimeContentType contentType)
    {
        this();
        
        // The current wbxml encoder can only output utf-8
        contentType.setParam("charset", "utf-8");
        pushMsg_ = new MimeBodyPart(buildPushMessage(pushMsg), contentType);
    }
    
    public SmsWapPushMessage(WbxmlDocument pushMsg, String contentType)
    {
        this(pushMsg, new MimeContentType(contentType));
    }
    
    public SmsWapPushMessage(WbxmlDocument pushMsg)
    {
        this(pushMsg, pushMsg.getWbxmlContentType());
    }
     
    public SmsWapPushMessage(byte[] pushMsg, MimeContentType contentType)
    {
        this();
        
        pushMsg_ = new MimeBodyPart(pushMsg, contentType);
    }
        
    public SmsWapPushMessage(byte[] pushMsg, String contentType)
    {
        this();
        
        pushMsg_ = new MimeBodyPart(pushMsg, contentType);
    }
    
    protected byte[] buildPushMessage(WbxmlDocument pushMsg)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try
        {
            // Data
            pushMsg.writeXmlTo(pushMsg.getWbxmlWriter(baos));

            // Done
            baos.close();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }

        return baos.toByteArray();
    }
    
    public void setWspEncodingVersion(WspEncodingVersion wspEncodingVersion)
    {
        wspEncodingVersion_ = wspEncodingVersion;
    }

    @Override
    public SmsUserData getUserData()
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
            WspUtil.writeUint8(baos, WspConstants.PDU_TYPE_PUSH);

            //
            // WAP PUSH FIELDS
            //

            // Create headers first
            ByteArrayOutputStream headers = new ByteArrayOutputStream();
            
            // Content-type
            WapMimeEncoder.writeContentType(wspEncodingVersion_, headers, pushMsg_);

            // WAP-HEADERS
            WapMimeEncoder.writeHeaders(wspEncodingVersion_, headers, pushMsg_);
                        
            // Done with the headers...
            headers.close();

            // Headers created, write headers lenght and headers to baos

            // HeadersLen - Length of Content-type and Headers
            WspUtil.writeUintvar(baos, headers.size());

            // Headers
            baos.write(headers.toByteArray());

            // Data
            WapMimeEncoder.writeBody(wspEncodingVersion_, baos, pushMsg_);

            // Done
            baos.close();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }

        return new SmsUserData(baos.toByteArray());
    }

    public void setXWapApplicationId(String appId)
    {
        pushMsg_.addHeader("X-Wap-Application-Id", appId);
    }
    
    public void setXWapContentURI(String contentUri)
    {
        pushMsg_.addHeader("X-Wap-Content-URI", contentUri);
    }

    public void setXWapInitiatorURI(String initiatorUri)
    {
        pushMsg_.addHeader("X-Wap-Initiator-URI", initiatorUri);
    }
 }
