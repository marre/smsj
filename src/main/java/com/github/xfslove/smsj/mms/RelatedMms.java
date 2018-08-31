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
package com.github.xfslove.smsj.mms;

import com.github.xfslove.smsj.mime.MimeBodyPart;
import com.github.xfslove.smsj.mime.MimeMultipartRelated;
import com.github.xfslove.smsj.mime.WapMimeEncoder;
import com.github.xfslove.smsj.mms.smil.*;
import com.github.xfslove.smsj.util.StringUtil;
import com.github.xfslove.smsj.wsp.WspEncodingVersion;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Created by hanwen on 2018/8/20.
 */
public class RelatedMms extends MimeMultipartRelated {

  private final WspEncodingVersion wspEncodingVersion;

  private WspEncodingVersion version = WspEncodingVersion.VERSION_1_0;

  private String transactionId;

  private String from;

  private String subject;

  private Date date = new Date();

  private Smil smil = new Smil();

  public RelatedMms() {
    wspEncodingVersion = WspEncodingVersion.VERSION_1_3;
  }

  public RelatedMms(WspEncodingVersion wspEncodingVersion) {
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

  public void setVersion(WspEncodingVersion version) {
    this.version = version;
  }

  public void setHeight(int height) {
    this.smil.setHeight(height);
  }

  public void setWidth(int width) {
    this.smil.setWidth(width);
  }

  public void addRegion(SmilRegion region) {
    this.smil.addRegion(region);
  }

  public void addPar(SmilPar par) {
    this.smil.addPar(par);
  }

  public void writeTo(OutputStream out) throws Exception {

    // Add headers
    if (transactionId == null || transactionId.length() == 0) {
      transactionId = StringUtil.randString(MmsConstants.DEFAULT_TRANSACTION_ID_LENGTH);
    }
    MmsHeaderEncoder.writeHeaderXMmsMessageType(out, MmsConstants.X_MMS_MESSAGE_TYPE_ID_M_RETRIEVE_CONF);
    MmsHeaderEncoder.writeHeaderXMmsTransactionId(out, transactionId);
    MmsHeaderEncoder.writeHeaderXMmsMmsVersion(out, version);

    if (subject != null && subject.length() > 0) {
      MmsHeaderEncoder.writeHeaderSubject(out, subject);
    }

    if (from != null && from.length() > 0) {
      MmsHeaderEncoder.writeHeaderFrom(out, from);
    }

    if (date != null) {
      MmsHeaderEncoder.writeHeaderDate(out, date);
    }

    // Add content-type
    MmsHeaderEncoder.writeHeaderContentType(wspEncodingVersion, out, getContentType());

    // Add content

    setStartBodyPart(createSmilPart());

    WapMimeEncoder.writeBody(wspEncodingVersion, out, this);

  }

  @Override
  public void addBodyPart(MimeBodyPart bodyPart) {
    SmilPar par = ((LinkedList<SmilPar>) smil.getParList()).getLast();
    SmilMedia media;

    String cid = cid();
    bodyPart.setContentId(cid);
    String contentType = bodyPart.getContentType().getName().toLowerCase();
    if (isTextType(contentType)) {
      media = new SmilMedia.Text(cid);
    } else if (isImageType(contentType)) {
      media = new SmilMedia.Image(cid);
    } else if (isAudioType(contentType)) {
      media = new SmilMedia.Audio(cid);
    } else if (isVideoType(contentType)) {
      media = new SmilMedia.Video(cid);
    } else {
      throw new RuntimeException("unsupported content-type");
    }
    par.addMedia(media);

    super.addBodyPart(bodyPart);
  }

  /**
   * add with smil region
   *
   * @param bodyPart  bodyPart
   * @param refRegion SmilRegion#id
   */
  public void addBodyPart(MimeBodyPart bodyPart, String refRegion) {
    SmilPar par = ((LinkedList<SmilPar>) smil.getParList()).getLast();
    SmilMedia media;

    String cid = cid();
    bodyPart.setContentId(cid);
    String contentType = bodyPart.getContentType().getName().toLowerCase();
    if (isTextType(contentType)) {
      media = new SmilMedia.Text(cid);
      ((SmilMedia.Text) media).setRef(refRegion);
    } else if (isImageType(contentType)) {
      media = new SmilMedia.Image(cid);
      ((SmilMedia.Image) media).setRef(refRegion);
    } else if (isAudioType(contentType)) {
      media = new SmilMedia.Audio(cid);
      ((SmilMedia.Audio) media).setRef(refRegion);
    } else if (isVideoType(contentType)) {
      media = new SmilMedia.Video(cid);
      ((SmilMedia.Video) media).setRef(refRegion);
    } else {
      throw new RuntimeException("unsupported content-type");
    }
    par.addMedia(media);

    super.addBodyPart(bodyPart);

  }

  public MimeBodyPart createSmilPart() throws Exception {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream();
         SmilWriter writer = new SmilWriter()) {
      writer.writeTo(os);

      MimeBodyPart smilPart = new MimeBodyPart(os.toByteArray(), smil.getContentType());
      // FIXME: Should perhaps set CID to something useful?
      smilPart.setContentId("0000");
      return smilPart;
    }
  }

  public boolean isTextType(String contentType) {
    return null != contentType && contentType.startsWith("text/");
  }

  public boolean isImageType(String contentType) {
    return null != contentType && contentType.startsWith("image/");
  }

  public boolean isAudioType(String contentType) {
    return null != contentType && contentType.startsWith("audio/");
  }

  public boolean isVideoType(String contentType) {
    return null != contentType && contentType.startsWith("video/");
  }

  public String cid() {
    return "cid:" + UUID.randomUUID().toString().replaceAll("-", "");
  }
}
