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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.marre.mime.MimeBodyPart;
import org.marre.mime.MimeContentType;
import org.marre.sms.SmsConcatMessage;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsUdhElement;
import org.marre.sms.SmsUdhUtil;
import org.marre.sms.SmsUserData;
import org.marre.wap.WapConstants;
import org.marre.wap.WapMimeEncoder;
import org.marre.wap.WspHeaderEncoder;
import org.marre.wap.WspUtil;
import org.marre.wap.wbxml.WbxmlDocument;
import org.marre.xml.TextXmlWriter;
import org.marre.xml.XmlWriter;

/**
 * Connectionless WAP push message with SMS as bearer.
 *
 * It supports the "content-type" and "X-Wap-Application-Id" headers.
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public class SmsWapPushMessage extends SmsConcatMessage
{
    protected int myDstPort = SmsConstants.PORT_WAP_PUSH;
    protected int mySrcPort = SmsConstants.PORT_WAP_WSP;
    protected byte myWspEncodingVersion = WapConstants.WSP_ENCODING_VERSION_1_2;
    protected MimeBodyPart myPushMsg;
        
    protected SmsWapPushMessage()
    {
    }
    
    public SmsWapPushMessage(MimeBodyPart thePushMsg)
    {
        myPushMsg = thePushMsg;
    }
    
    public SmsWapPushMessage(WbxmlDocument thePushMsg, MimeContentType theContentType)
    {
        // The current wbxml encoder can only output utf-8
        theContentType.setParam("charset", "utf-8");
        myPushMsg = new MimeBodyPart(buildPushMessage(thePushMsg), theContentType);
    }
    
    public SmsWapPushMessage(WbxmlDocument thePushMsg, String theContentType)
    {
        MimeContentType ct = new MimeContentType(theContentType);
        // The current wbxml encoder can only output utf-8
        ct.setParam("charset", "utf-8");
        myPushMsg = new MimeBodyPart(buildPushMessage(thePushMsg), ct);
    }
    
    public SmsWapPushMessage(WbxmlDocument thePushMsg)
    {
        this(thePushMsg, thePushMsg.getWbxmlContentType());
    }
     
    public SmsWapPushMessage(byte[] thePushMsg, MimeContentType theContentType)
    {
        myPushMsg = new MimeBodyPart(thePushMsg, theContentType);
    }
        
    public SmsWapPushMessage(byte[] thePushMsg, String theContentType)
    {
        myPushMsg = new MimeBodyPart(thePushMsg, theContentType);
    }
    
    protected byte[] buildPushMessage(WbxmlDocument thePushMsg)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try
        {
            // Data
            thePushMsg.writeXmlTo(thePushMsg.getWbxmlWriter(baos));

            // Done
            baos.close();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }

        return baos.toByteArray();
    }
    
    public void setWspEncodingVersion(byte wspEncodingVersion)
    {
        myWspEncodingVersion = wspEncodingVersion;
    }
    
    public void setPushPorts(int srcPort, int dstPort)
    {
        mySrcPort = srcPort;
        myDstPort = dstPort;
    }
    
    public SmsUserData getUserData()
    {
        WapMimeEncoder wapMimeEncoder = new WapMimeEncoder(WapConstants.WSP_ENCODING_VERSION_1_2);
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
            wapMimeEncoder.writeContentType(headers, myPushMsg);

            // WAP-HEADERS
            wapMimeEncoder.writeHeaders(headers, myPushMsg);
                        
            // Done with the headers...
            headers.close();

            // Headers created, write headers lenght and headers to baos

            // HeadersLen - Length of Content-type and Headers
            WspUtil.writeUintvar(baos, headers.size());

            // Headers
            baos.write(headers.toByteArray());

            // Data
            wapMimeEncoder.writeBody(baos, myPushMsg);

            // Done
            baos.close();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage());
        }

        return new SmsUserData(baos.toByteArray());
    }

    public SmsUdhElement[] getUdhElements()
    {
        return new SmsUdhElement[] { SmsUdhUtil.get16BitApplicationPortUdh(myDstPort, mySrcPort) }; 
    }
    
    public void setXWapApplicationId(String appId)
    {
        myPushMsg.addHeader("X-Wap-Application-Id", appId);
    }
    
    public void setXWapContentURI(String contentUri)
    {
        myPushMsg.addHeader("X-Wap-Content-URI", contentUri);
    }

    public void setXWapInitiatorURI(String initiatorUri)
    {
        myPushMsg.addHeader("X-Wap-Initiator-URI", initiatorUri);
    }
 }
