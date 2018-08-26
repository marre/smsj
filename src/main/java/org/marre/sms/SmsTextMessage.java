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
package org.marre.sms;

import org.marre.sms.charset.Gsm7BitCharsetProvider;
import org.marre.sms.dcs.DcsGroup;
import org.marre.sms.dcs.SmsAlphabet;
import org.marre.sms.dcs.SmsDcs;
import org.marre.sms.dcs.SmsMsgClass;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Represents a text message.
 * <p>
 * The text can be sent in unicode (max 70 chars/SMS), 8-bit (max 140 chars/SMS)
 * or GSM encoding (max 160 chars/SMS).
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class SmsTextMessage extends SmsConcatMessage {

  private String text;

  private SmsDcs dcs;

  /**
   * Creates an SmsTextMessage with the given dcs.
   *
   * @param msg The message
   * @param dcs The data coding scheme
   */
  public SmsTextMessage(String msg, SmsDcs dcs) {
    setText(msg, dcs);
  }

  /**
   * Creates an SmsTextMessage with the given alphabet and message class.
   *
   * @param msg          The message
   * @param alphabet     The alphabet
   * @param messageClass The messageclass
   */
  public SmsTextMessage(String msg, SmsAlphabet alphabet, SmsMsgClass messageClass) {
    this(msg, SmsDcs.general(DcsGroup.GENERAL_DATA_CODING, alphabet, messageClass));
  }

  /**
   * Creates an SmsTextMessage with default 7Bit GSM Alphabet
   *
   * @param msg The message
   */
  public SmsTextMessage(String msg) {
    this(msg, SmsAlphabet.GSM, SmsMsgClass.CLASS_1);
  }

  /**
   * Returns the text message.
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the text.
   *
   * @param text
   */
  public void setText(String text) {
    if (text == null) {
      throw new IllegalArgumentException("Text cannot be null, use an empty string instead.");
    }

    this.text = text;
  }

  /**
   * Sets the text.
   *
   * @param text
   */
  public void setText(String text, SmsDcs dcs) {
    // Check input for null
    if (text == null) {
      throw new IllegalArgumentException("text cannot be null, use an empty string instead.");
    }

    if (dcs == null) {
      throw new IllegalArgumentException("dcs cannot be null.");
    }

    this.text = text;
    this.dcs = dcs;
  }

  /**
   * Returns the dcs.
   */
  public SmsDcs getDcs() {
    return dcs;
  }

  /**
   * Returns the user data.
   *
   * @return user data
   */
  @Override
  public SmsUserData getUserData() {
    SmsUserData ud;

    byte[] bytes;
    switch (dcs.getAlphabet()) {
      case GSM:
        try {
          bytes = text.getBytes(Gsm7BitCharsetProvider.CHARSET_NAME);
          ud = new SmsUserData(bytes, bytes.length, dcs);
        } catch (UnsupportedEncodingException e) {
          throw new RuntimeException(e);
        }
        break;

      case LATIN1:
        bytes = text.getBytes(StandardCharsets.ISO_8859_1);
        ud = new SmsUserData(bytes, bytes.length, dcs);
        break;

      case UCS2:
        bytes = text.getBytes(StandardCharsets.UTF_16BE);
        ud = new SmsUserData(bytes, bytes.length, dcs);
        break;

      default:
        return null;
    }

    return ud;
  }

  /**
   * Returns null.
   */
  @Override
  public SmsUdhElement[] getUdhElements() {
    return null;
  }
}
