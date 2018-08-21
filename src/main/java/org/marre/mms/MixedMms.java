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
package org.marre.mms;

import org.marre.mime.MimeMultipartMixed;
import org.marre.mime.WapMimeEncoder;
import org.marre.util.StringUtil;
import org.marre.wsp.WspEncodingVersion;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by hanwen on 2018/8/20.
 */
public class MixedMms extends MimeMultipartMixed {

  private final WspEncodingVersion wspEncodingVersion;

  private String transactionId;

  private String from;

  private String subject;

  private Date date = new Date();

  public MixedMms() {
    wspEncodingVersion = WspEncodingVersion.VERSION_1_2;
  }

  public MixedMms(WspEncodingVersion wspEncodingVersion) {
    this.wspEncodingVersion = wspEncodingVersion;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void writeMessage(OutputStream out) throws IOException {
    // Add headers
    writeHeaders(out);

    // Add content-type
    MmsHeaderEncoder.writeHeaderContentType(wspEncodingVersion, out, getContentType());

    // Add content
    WapMimeEncoder.writeBody(wspEncodingVersion, out, this);
  }

  private void writeHeaders(OutputStream out) throws IOException {
    if (transactionId == null || transactionId.length() == 0) {
      transactionId = StringUtil.randString(MmsConstants.DEFAULT_TRANSACTION_ID_LENGTH);
    }
    MmsHeaderEncoder.writeHeaderXMmsMessageType(out, MmsConstants.X_MMS_MESSAGE_TYPE_ID_M_RETRIEVE_CONF);
    MmsHeaderEncoder.writeHeaderXMmsTransactionId(out, transactionId);
    MmsHeaderEncoder.writeHeaderXMmsMmsVersion(out, WspEncodingVersion.VERSION_1_0);

    if (subject != null && subject.length() > 0) {
      MmsHeaderEncoder.writeHeaderSubject(out, subject);
    }

    if (from != null && from.length() > 0) {
      MmsHeaderEncoder.writeHeaderFrom(out, from);
    }

    if (date != null) {
      MmsHeaderEncoder.writeHeaderDate(out, date);
    }
  }
}
