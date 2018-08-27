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
package com.github.xfslove.sms.dcs;

import java.io.Serializable;

/**
 * Represents a SMS DCS (Data Coding Scheme).
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class SmsDcs implements Serializable {

  /**
   * The encoded dcs.
   */
  private final byte dcs;

  private final DcsGroup group;

  private final SmsAlphabet alphabet;

  private final SmsMsgClass messageClass;

  private final SmsWaitingInfo waitingInfo;

  public SmsDcs(byte dcs, DcsGroup group, SmsAlphabet alphabet, SmsMsgClass messageClass, SmsWaitingInfo waitingInfo) {
    this.dcs = dcs;
    this.group = group;
    this.alphabet = alphabet;
    this.messageClass = messageClass;
    this.waitingInfo = waitingInfo;
  }

  public SmsMsgClass getMessageClass() {
    return messageClass;
  }

  public SmsWaitingInfo getWaitingInfo() {
    return waitingInfo;
  }

  public DcsGroup getGroup() {
    return group;
  }

  public SmsAlphabet getAlphabet() {
    return alphabet;
  }

  /**
   * Returns the encoded dcs.
   *
   * @return The dcs.
   */
  public byte getValue() {
    return dcs;
  }

  /**
   * Builds a general-data-coding dcs.
   *
   * @param group    The dcs group, {@link DcsGroup#GENERAL_DATA_CODING} or {@link DcsGroup#MARKED_FOR_AUTOMATIC_DELETION}
   * @param alphabet The alphabet.
   * @param msgClass The message class.
   * @return A valid general data coding DCS.
   */
  public static SmsDcs general(DcsGroup group, SmsAlphabet alphabet, SmsMsgClass msgClass) {
    byte dcs = 0x00;

    switch (group) {
      case MARKED_FOR_AUTOMATIC_DELETION:
        dcs |= 0x40;
      default:
    }

    // Bits 3 and 2 indicate the alphabet being used, as follows :
    // Bit3 Bit2 Alphabet:
    //    0   0  Default alphabet
    //    0   1  8 bit data
    //    1   0  UCS2 (16bit) [10]
    //    1   1  Reserved
    switch (alphabet) {
      case GSM:
        dcs |= 0x00;
        break;
      case LATIN1:
        dcs |= 0x04;
        break;
      case UCS2:
        dcs |= 0x08;
        break;
      case RESERVED:
        dcs |= 0x0C;
        break;
      default:
    }

    switch (msgClass) {
      case CLASS_0:
        dcs |= 0x10;
        break;
      case CLASS_1:
        dcs |= 0x11;
        break;
      case CLASS_2:
        dcs |= 0x12;
        break;
      case CLASS_3:
        dcs |= 0x13;
        break;
      default:
    }

    return new SmsDcs(dcs, group, alphabet, msgClass, null);
  }

  /**
   * Builds a message-waiting dcs.
   *
   * @param group          The dcs group, {@link DcsGroup#MESSAGE_WAITING_DISCARD} or {@link DcsGroup#MESSAGE_WAITING_STORE_GSM} or {@link DcsGroup#MESSAGE_WAITING_STORE_UCS2}
   * @param smsWaitingInfo waiting info
   * @return A valid general data coding DCS.
   */
  public static SmsDcs waitingInfo(DcsGroup group, SmsWaitingInfo smsWaitingInfo) {
    byte dcs = 0x00;

    SmsAlphabet alphabet = null;

    // Bits 5 and 4 indicate the alphabet being used, as follows :
    // Bit5 Bit4 Alphabet:
    //    0   1  Default alphabet
    //    1   0  UCS2 (16bit) [10]
    switch (group) {
      case MESSAGE_WAITING_DISCARD:
        dcs |= 0xC0;
        break;
      case MESSAGE_WAITING_STORE_GSM: {
        alphabet = SmsAlphabet.GSM;
        dcs |= 0xD0;
        break;
      }
      case MESSAGE_WAITING_STORE_UCS2: {
        alphabet = SmsAlphabet.UCS2;
        dcs |= 0xE0;
        break;
      }
      default:
    }

    // Bit 3
    // 1 Set Indication Active
    switch (smsWaitingInfo) {
      case VOICE:
        dcs |= 0x08;
        break;
      case FAX:
        dcs |= 0x09;
        break;
      case EMAIL:
        dcs |= 0x0A;
        break;
      case OTHER:
        dcs |= 0x0B;
        break;
      default:
    }

    return new SmsDcs(dcs, group, alphabet, null, smsWaitingInfo);
  }

  /**
   * Builds a {@link DcsGroup#DATA_CODING_MESSAGE} dcs.
   *
   * @param alphabet alphabet
   * @param msgClass message class
   * @return A valid general data coding DCS.
   */
  public static SmsDcs dataCoding(SmsAlphabet alphabet, SmsMsgClass msgClass) {
    byte dcs = (byte) 0xF0;

    switch (alphabet) {
      case LATIN1:
        dcs |= 0x04;
        break;
      default:
    }

    switch (msgClass) {
      case CLASS_0:
        dcs |= 0x00;
        break;
      case CLASS_1:
        dcs |= 0x01;
        break;
      case CLASS_2:
        dcs |= 0x02;
        break;
      case CLASS_3:
        dcs |= 0x03;
        break;
      default:
    }

    return new SmsDcs(dcs, DcsGroup.DATA_CODING_MESSAGE, alphabet, msgClass, null);
  }

}
