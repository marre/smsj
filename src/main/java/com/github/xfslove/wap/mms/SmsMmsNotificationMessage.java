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
package com.github.xfslove.wap.mms;

import com.github.xfslove.mms.MmsConstants;
import com.github.xfslove.mms.MmsHeaderEncoder;
import com.github.xfslove.mime.MimeBodyPart;
import com.github.xfslove.sms.ud.SmsUserData;
import com.github.xfslove.util.StringUtil;
import com.github.xfslove.wap.push.SmsWapPushMessage;
import com.github.xfslove.wsp.WspEncodingVersion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Simple MMS notification message sent over Sms.
 *
 * @version $Id$
 */
public class SmsMmsNotificationMessage extends SmsWapPushMessage {

  private static final long DEFAULT_EXPIRY = 3 * 24 * 60 * 60;

  private WspEncodingVersion version = WspEncodingVersion.VERSION_1_0;

  private String transactionId;

  private String from;

  private String subject;

  private int messageClassId = MmsConstants.X_MMS_MESSAGE_CLASS_ID_PERSONAL;

  private final long size;

  private long expiry = DEFAULT_EXPIRY;

  private final String contentLocation;

  public SmsMmsNotificationMessage(String contentLocation, long size) {
    this.contentLocation = contentLocation;
    this.size = size;
  }

  protected void writeNotificationTo(OutputStream os) throws IOException {
    // X-Mms-Message-Type (m-notification-ind)
    if (transactionId == null || transactionId.length() == 0) {
      transactionId = StringUtil.randString(MmsConstants.DEFAULT_TRANSACTION_ID_LENGTH);
    }
    MmsHeaderEncoder.writeHeaderXMmsMessageType(os, MmsConstants.X_MMS_MESSAGE_TYPE_ID_M_NOTIFICATION_IND);
    MmsHeaderEncoder.writeHeaderXMmsTransactionId(os, transactionId);
    MmsHeaderEncoder.writeHeaderXMmsMmsVersion(os, version);

    if (from != null && from.length() > 0) {
      MmsHeaderEncoder.writeHeaderFrom(os, from);
    }

    if (subject != null && subject.length() > 0) {
      MmsHeaderEncoder.writeHeaderSubject(os, subject);
    }

    MmsHeaderEncoder.writeHeaderXMmsMessageClass(os, messageClassId);
    MmsHeaderEncoder.writeHeaderXMmsMessageSize(os, size);
    MmsHeaderEncoder.writeHeaderXMmsExpiryRelative(os, expiry);
    MmsHeaderEncoder.writeHeaderContentLocation(os, contentLocation);
  }

  public void setMessageClass(int messageClassId) {
    this.messageClassId = messageClassId;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public void setExpiry(int expiry) {
    this.expiry = expiry;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  @Override
  public SmsUserData getUserData() {

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      writeNotificationTo(baos);
      pushMsg = new MimeBodyPart(baos.toByteArray(), "application/vnd.wap.mms-message");
      setXWapApplicationId("x-wap-application:mms.ua");

      return super.getUserData();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

  }
}
