/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
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
 * ***** END LICENSE BLOCK ***** */
package com.github.xfslove.sms.nokia;

import com.github.xfslove.sms.SmsPduUtil;
import com.github.xfslove.sms.SmsPort;
import com.github.xfslove.sms.SmsPortAddressedMessage;
import com.github.xfslove.sms.ud.SmsUserData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Nokia Operator Logo message
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaOperatorLogo extends SmsPortAddressedMessage {

  /**
   * If set to true it will make the message two bytes shorter to make it
   * possible to fit a 72x14 pixel image in one SMS instead of two. <br>
   * <b>Note! </b> This will probably only work on Nokia phones...
   */
  private boolean discardNokiaHeaders;

  /**
   * The ota image as a byte array
   */
  private final byte[] bitmapData;

  /**
   * GSM Mobile Country Code
   */
  private final int mcc;

  /**
   * GSM Mobile Network Code
   */
  private final int mnc;

  /**
   * Creates a Nokia Operator Logo message
   *
   * @param otaBitmap bitmap
   * @param mcc       GSM Mobile Country Code
   * @param mnc       GSM Mobile Network Code
   */
  public NokiaOperatorLogo(OtaBitmap otaBitmap, int mcc, int mnc) {
    this(otaBitmap.getBytes(), mcc, mnc);
  }

  /**
   * Creates a Nokia Operator Logo message
   *
   * @param bitmapData The ota image as a byte array
   * @param mcc        GSM Mobile Country Code
   * @param mnc        GSM Mobile Network Code
   */
  public NokiaOperatorLogo(byte[] bitmapData, int mcc, int mnc) {
    super(SmsPort.NOKIA_OPERATOR_LOGO, SmsPort.ZERO);

    this.bitmapData = bitmapData;
    this.mcc = mcc;
    this.mnc = mnc;
  }

  /**
   * Creates a Nokia Operator Logo message
   *
   * @param bitmapData     The ota image as a byte array
   * @param mcc            GSM Mobile Country Code
   * @param mnc            GSM Mobile Network Code
   * @param discardHeaders discard header
   */
  public NokiaOperatorLogo(byte[] bitmapData, int mcc, int mnc, boolean discardHeaders) {
    super(SmsPort.NOKIA_OPERATOR_LOGO, SmsPort.ZERO);

    discardNokiaHeaders = discardHeaders;
    this.bitmapData = bitmapData;
    this.mcc = mcc;
    this.mnc = mnc;
  }

  @Override
  public SmsUserData getUserData() {

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream(140)) {
      if (!discardNokiaHeaders) {
        // Header??
        baos.write(0x30);
      }

      // mcc
      SmsPduUtil.writeBcdNumber(baos, "" + mcc);
      // mnc
      if (mnc < 10) {
        SmsPduUtil.writeBcdNumber(baos, "0" + mnc);
      } else {
        SmsPduUtil.writeBcdNumber(baos, "" + mnc);
      }

      if (!discardNokiaHeaders) {
        // Start of content?
        baos.write(0x0A);
      }
      // bitmap
      baos.write(bitmapData);

      return new SmsUserData(baos.toByteArray());
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

  }
}
