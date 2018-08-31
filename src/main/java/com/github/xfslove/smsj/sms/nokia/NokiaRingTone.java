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
package com.github.xfslove.smsj.sms.nokia;

import com.github.xfslove.smsj.sms.SmsPort;
import com.github.xfslove.smsj.sms.SmsPortAddressedMessage;
import com.github.xfslove.smsj.sms.ud.SmsUserData;

/**
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaRingTone extends SmsPortAddressedMessage {

  private final byte[] ringToneData;

  /**
   * Creates a ring tone
   *
   * @param ringToneData The ring tone in nokia binary format
   */
  public NokiaRingTone(byte[] ringToneData) {
    super(SmsPort.NOKIA_RING_TONE, SmsPort.ZERO);

    this.ringToneData = ringToneData;
  }

  @Override
  public SmsUserData getUserData() {
    return new SmsUserData(ringToneData);
  }
}
