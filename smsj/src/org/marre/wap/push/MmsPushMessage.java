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

import org.marre.util.StringUtil;
import org.marre.wap.WapConstants;
import org.marre.wap.mms.MmsConstants;
import org.marre.wap.mms.MmsHeaderEncoder;

/**
 * MMS notification message.
 * 
 * @author Lincoln Spiteri
 * @version $Id$
 */
public class MmsPushMessage extends SmsWapPushMessage
{
    private static final int DEFAULT_TRANSACTION_ID_LENGTH = 5;
    private static final long DEFAULT_EXPIRY = 3 * 24 * 60 * 60; // 3 days

    protected String myContentLocation;
    protected String myFrom;
    protected String mySubject;
    protected int myMessageClassId = MmsConstants.X_MMS_MESSAGE_CLASS_ID_PERSONAL;
    protected String myTransactionId;
    protected long mySize;
    protected long myExpiry;

    public MmsPushMessage(String theContentLocation)
    {
        super();

        myContentLocation = theContentLocation;
        myTransactionId = StringUtil.randString(DEFAULT_TRANSACTION_ID_LENGTH);
        myExpiry = DEFAULT_EXPIRY;
    }

    public void createMmsPush()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try
        {
            // X-Mms-Message-Type (m-notification-ind)
            MmsHeaderEncoder.writeHeaderXMmsMessageType(baos, MmsConstants.X_MMS_MESSAGE_TYPE_ID_M_NOTIFICATION_IND);
            MmsHeaderEncoder.writeHeaderXMmsTransactionId(baos, myTransactionId);
            MmsHeaderEncoder.writeHeaderXMmsMmsVersion(baos, MmsConstants.X_MMS_MMS_VERSION_ID_1_0);

            if (myFrom.length() > 0)
            {
                MmsHeaderEncoder.writeHeaderFrom(baos, myFrom);
            }

            if (mySubject.length() > 0)
            {
                MmsHeaderEncoder.writeHeaderSubject(baos, mySubject);
            }

            MmsHeaderEncoder.writeHeaderXMmsMessageClass(baos, myMessageClassId);
            MmsHeaderEncoder.writeHeaderXMmsMessageSize(baos, mySize);
            MmsHeaderEncoder.writeHeaderXMmsExpiryRelative(baos, myExpiry);
            MmsHeaderEncoder.writeHeaderContentLocation(baos, myContentLocation);

            baos.close();
        }
        catch (IOException e)
        {
            // Should not happen, we are writing to an ByteArray
        }

        // TODO: This is just plain wrong...
/*        
        createMessage(WapConstants.WSP_ENCODING_VERSION_1_2, baos.toByteArray(), 
                      "application/vnd.wap.mms-message", "x-wap-application:mms.ua", null);
*/                      
    }

    public void setMessageClass(int messageClassId)
    {
        myMessageClassId = messageClassId;
    }

    public void setSize(long theSize)
    {
        mySize = theSize;
    }

    public void setSubject(String theSubject)
    {
        mySubject = theSubject;
    }

    public void setExpiry(int i)
    {
        myExpiry = i;
    }

    public void setFrom(String string)
    {
        myFrom = string;
    }

    public void setTransactionId(String transactionId)
    {
        myTransactionId = transactionId;
    }
}
