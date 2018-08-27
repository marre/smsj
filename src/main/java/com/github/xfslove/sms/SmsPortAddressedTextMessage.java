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
package com.github.xfslove.sms;

import com.github.xfslove.sms.dcs.SmsAlphabet;
import com.github.xfslove.sms.dcs.SmsDcs;
import com.github.xfslove.sms.dcs.SmsMsgClass;
import com.github.xfslove.sms.ud.SmsUserData;

/**
 * A port addressed message that delegates the text part to SmsTextMessage.
 *
 * @author Markus
 * @version $Id$
 */
public class SmsPortAddressedTextMessage extends SmsPortAddressedMessage {

  /**
   * The text message part.
   */
  protected final SmsTextMessage smsTextMessage;

  /**
   * Creates a new SmsPortAddressedTextMessage with default 6Bit GSM Alphabet.
   *
   * @param destPort dest port
   * @param origPort orig port
   * @param msg      msg
   */
  public SmsPortAddressedTextMessage(SmsPort destPort, SmsPort origPort, String msg) {
    super(destPort, origPort);
    smsTextMessage = new SmsTextMessage(msg);
  }

  /**
   * Creates a new SmsPortAddressedTextMessage with the given alphabet and message class.
   *
   * @param destPort     dest port
   * @param origPort     orig port
   * @param msg          msg
   * @param alphabet     alphabet
   * @param messageClass msg class
   */
  public SmsPortAddressedTextMessage(SmsPort destPort, SmsPort origPort, String msg, SmsAlphabet alphabet, SmsMsgClass messageClass) {
    super(destPort, origPort);
    smsTextMessage = new SmsTextMessage(msg, alphabet, messageClass);
  }

  /**
   * Creates a SmsPortAddressedTextMessage with the given dcs.
   *
   * @param destPort dest port
   * @param origPort orig port
   * @param msg      msg
   * @param dcs      dcs
   */
  public SmsPortAddressedTextMessage(SmsPort destPort, SmsPort origPort, String msg, SmsDcs dcs) {
    super(destPort, origPort);
    smsTextMessage = new SmsTextMessage(msg, dcs);
  }

  @Override
  public SmsUserData getUserData() {
    return smsTextMessage.getUserData();
  }

  /**
   * @return the text message.
   */
  public String getText() {
    return smsTextMessage.getText();
  }

  /**
   * @param text the text.
   */
  public void setText(String text) {
    smsTextMessage.setText(text);
  }

  /**
   * @param text the text.
   * @param dcs  the dcs.
   */
  public void setText(String text, SmsDcs dcs) {
    smsTextMessage.setText(text, dcs);
  }

  /**
   * @return the dcs.
   */
  public SmsDcs getDcs() {
    return smsTextMessage.getDcs();
  }
}
