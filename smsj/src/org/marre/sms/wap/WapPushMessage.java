/*
    SMS Library for the Java platform
    Copyright (C) 2002  Markus Eriksson

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.marre.sms.wap;

import java.io.*;

import org.apache.commons.logging.*;

import org.marre.wap.*;
import org.marre.wap.util.*;

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
    static Log myLog = LogFactory.getLog(WapPushMessage.class);

    private byte[] myPushMsg;

    /**
     * Sends a CL WAP push message OTA with SMS.
     *
     * @param thePushMsg The push message
     */
    public WapPushMessage(byte[] thePushMsg)
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);

        myPushMsg = thePushMsg;
    }

    /**
     * Creates a CL WAP push message OTA with SMS.
     *
     * @param thePushMsg The push message
     * @param theContentType Content-type of the push
     * @param theAppId WAP Push Application ID
     */
    public WapPushMessage(byte[] thePushMsg, String theContentType, String theAppId)
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);

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
            // the Application ID

            // App ID
            if( theAppId != null)
            {
                WspUtil.writeWapApplicationId(headers, theAppId);
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
            myLog.error("Failed to write to bytearrayoutputstream", ex);
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
     * Sends a CL WAP push message OTA with SMS.
     *
     * @param thePushMsg The push message
     * @param theContentType Content-type of the push
     */
    public WapPushMessage(byte[] thePushMsg, String theContentType)
    {
        this(thePushMsg, theContentType, null);
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
