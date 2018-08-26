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

import org.marre.sms.dcs.SmsAlphabet;
import org.marre.sms.ud.SmsUdhElement;
import org.marre.sms.ud.SmsUdhIei;
import org.marre.sms.ud.SmsUdhUtil;
import org.marre.sms.ud.SmsUserData;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Baseclass for messages that needs to be concatenated.
 * <p>- Only usable for messages that uses the same UDH fields for all message
 * parts. <br>- This class could be better written. There are several parts
 * that are copy- pasted. <br>- The septet coding could be a bit optimized.
 * <br>
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public abstract class SmsConcatMessage implements SmsMessage {

  public static final int MAX_OCTAL_LENGTH = 140;

  /**
   * Field 1 (1 octet): Length of User Data Header
   * Field 2 (1 octet): Information Element Identifier, {@link SmsUdhIei#CONCATENATED_8BIT}
   * Field 3 (1 octet): Length of the header, excluding the first two fields; equal to 03
   * Field 4 (1 octet): 00-FF, CSMS reference number, must be same for all the SMS parts in the CSMS
   * Field 5 (1 octet): 00-FF, total number of parts. The value shall remain constant for every short message which makes up the concatenated short message. If the value is zero then the receiving entity shall ignore the whole information element
   * Field 6 (1 octet): 00-FF, this part's number in the sequence. The value shall start at 1 and increment for every short message which makes up the concatenated short message. If the value is zero or greater than the value in Field 5 then the receiving entity shall ignore the whole information element.
   */
  public static final int CONCAT_FIELD_LENGTH = 6;

  /**
   * refno rand 0-255
   */
  private static final Random RND = ThreadLocalRandom.current();

  /**
   * Creates an empty SmsConcatMessage.
   */
  protected SmsConcatMessage() {
    // Empty
  }

  /**
   * Returns the whole UD
   *
   * @return the UD
   */
  public abstract SmsUserData getUserData();

  /**
   * Returns the udh elements
   * <p>
   * The returned UDH is the same as specified when the message was created.
   * No concat headers are added.
   *
   * @return the UDH as SmsUdhElements
   */
  public abstract SmsUdhElement[] getUdhElements();

  /**
   * Converts this message into SmsPdu:s
   * <p>
   * If the message is too long to fit in one SmsPdu the message is divided
   * into many SmsPdu:s with a 8-bit concat pdu UDH element.
   *
   * @return Returns the message as SmsPdu:s
   */
  @Override
  public SmsPdu[] getPdus() {
    SmsUserData ud = getUserData();
    SmsUdhElement[] udhElements = getUdhElements();

    SmsAlphabet alphabet = ud.getDcs().getAlphabet();

    int maxLength = SmsAlphabet.GSM.equals(alphabet) ? (MAX_OCTAL_LENGTH * 8) / 7 : MAX_OCTAL_LENGTH;

    // single pdu
    if (ud.getLength() <= maxLength - SmsUdhUtil.getTotalSize(udhElements)) {
      return new SmsPdu[]{new SmsPdu(udhElements, ud)};
    }

    return createMultiPdus(udhElements, ud, maxLength - CONCAT_FIELD_LENGTH);
  }

  private SmsPdu[] createMultiPdus(SmsUdhElement[] udhElements, SmsUserData ud, int nMaxConcatChars) {
    int refno = RND.nextInt(0xFF);

    // Calculate number of SMS needed
    int nSms = ud.getLength() / nMaxConcatChars;
    if ((ud.getLength() % nMaxConcatChars) > 0) {
      nSms += 1;
    }
    SmsPdu[] smsPdus = new SmsPdu[nSms];

    // Calculate number of UDHI
    // add concat header...
    SmsUdhElement[] pduUdhElements;
    if (udhElements == null) {
      pduUdhElements = new SmsUdhElement[1];
    } else {
      pduUdhElements = new SmsUdhElement[udhElements.length + 1];

      // Copy the UDH headers
      System.arraycopy(udhElements, 0, pduUdhElements, 1, udhElements.length);
    }

    // Create pdus
    for (int i = 0; i < nSms; i++) {
      // Create concat header
      pduUdhElements[0] = SmsUdhUtil.get8BitConcatUdh(refno, nSms, i + 1);

      // Create
      // Must concatenate messages
      // Calc pdu length
      int udOffset = nMaxConcatChars * i;
      int udBytes = ud.getLength() - udOffset;
      if (udBytes > nMaxConcatChars) {
        udBytes = nMaxConcatChars;
      }
      int udLength = udBytes;

      byte[] pduUd = new byte[udBytes];
      System.arraycopy(ud.getData(), udOffset, pduUd, 0, udBytes);
      smsPdus[i] = new SmsPdu(pduUdhElements, pduUd, udLength, ud.getDcs());
    }
    return smsPdus;
  }
}
