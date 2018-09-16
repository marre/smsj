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
package com.github.xfslove.smsj.sms;

import com.github.xfslove.smsj.sms.dcs.SmsDcs;
import com.github.xfslove.smsj.sms.ud.SmsUdhElement;
import com.github.xfslove.smsj.sms.ud.SmsUdhUtil;
import com.github.xfslove.smsj.sms.ud.SmsUserData;

import java.io.Serializable;

/**
 * Represents an SMS pdu
 * <p>
 * A SMS pdu consists of a user data header (UDH) and the actual content often
 * called user data (UD).
 *
 * @author Markus Eriksson
 * @version $Id$
 */

public class SmsPdu implements Serializable {
  private SmsUdhElement[] udhElements;

  private SmsUserData ud;

  /**
   * Creates an empty SMS pdu object
   */
  public SmsPdu() {
    // Empty
  }

  /**
   * Creates an SMS pdu object.
   *
   * @param udhElements The UDH elements
   * @param ud          The content
   * @param dcs         dcs
   */
  public SmsPdu(SmsUdhElement[] udhElements, byte[] ud, SmsDcs dcs) {
    setUserDataHeaders(udhElements);
    setUserData(ud, dcs);
  }

  /**
   * Creates an SMS pdu object.
   *
   * @param udhElements The UDH elements
   * @param ud          The content
   */
  public SmsPdu(SmsUdhElement[] udhElements, SmsUserData ud) {
    setUserDataHeaders(udhElements);
    setUserData(ud);
  }

  /**
   * Sets the UDH field
   *
   * @param udhElements The UDH elements
   */
  public void setUserDataHeaders(SmsUdhElement[] udhElements) {
    this.udhElements = udhElements;
  }

  /**
   * the UDHL + UDH fields or null if there aren't any udh
   *
   * @return udh bytes
   */
  public byte[] getUdhBytes() {
    if (udhElements == null || udhElements.length == 0) {
      return new byte[0];
    }

    int sizeOf = SmsUdhUtil.getSizeOf(udhElements);
    byte[] bytes = new byte[sizeOf + 1];
    bytes[0] = (byte) (sizeOf & 0xff);
    int offset = 1;
    for (SmsUdhElement udhElement : udhElements) {
      byte[] udh = udhElement.getData();
      System.arraycopy(udh, 0, bytes, offset, udh.length);
      offset += udh.length;
    }

    return bytes;
  }

  /**
   * @return ud bytes
   */
  public byte[] getUdBytes() {
    if (ud == null) {
      return new byte[0];
    }
    return ud.getData() == null ? new byte[0] : ud.getData();
  }

  /**
   * Sets the user data field of the message.
   *
   * @param ud  The content
   * @param dcs The data coding scheme
   */
  public void setUserData(byte[] ud, SmsDcs dcs) {
    this.ud = new SmsUserData(ud, dcs);
  }

  /**
   * Sets the user data field of the message.
   *
   * @param ud The content
   */
  public void setUserData(SmsUserData ud) {
    this.ud = ud;
  }

  /**
   * Returns the user data part of the message.
   *
   * @return UD field
   */
  public SmsUserData getUserData() {
    return ud;
  }

  /**
   * Returns the user data header part of the message.
   *
   * @return UDH field
   */
  public SmsUdhElement[] getUserDateHeaders() {
    return udhElements;
  }

  /**
   * Returns the dcs.
   *
   * @return dcs
   */
  public SmsDcs getDcs() {
    return ud.getDcs();
  }
}
