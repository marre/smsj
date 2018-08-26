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
 *   Boris von Loesch
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Various functions to encode and decode strings
 *
 * @author Markus Eriksson
 */
public final class SmsPduUtil {

  /**
   * This class isn't intended to be instantiated
   */
  private SmsPduUtil() {
  }

  /**
   * octal -> septets
   *
   * @param octal octal array
   * @return
   */
  public static byte[] octal2septets(byte[] octal) {
    int septetCount = (8 * octal.length) / 7;
    byte[] septets = new byte[septetCount];
    for (int newIndex = septets.length - 1; newIndex >= 0; --newIndex) {
      for (int bit = 6; bit >= 0; --bit) {
        int oldBitIndex = ((newIndex * 7) + bit);
        if ((octal[oldBitIndex >>> 3] & (1 << (oldBitIndex & 7))) != 0) {
          septets[newIndex] |= (1 << bit);
        }
      }
    }

    return septets;
  }

  /**
   * septets -> octal
   *
   * @param septets septets array
   * @return
   */
  public static byte[] septets2octal(byte[] septets) {
    int octetLength = (int) Math.ceil(((septets.length * 7)) / 8.0);
    byte[] octets = new byte[octetLength];
    for (int i = 0; i < septets.length; i++) {
      for (int j = 0; j < 7; j++) {
        if ((septets[i] & (1 << j)) != 0) {
          int bitIndex = (i * 7) + j;
          octets[bitIndex >>> 3] |= 1 << (bitIndex & 7);
        }
      }
    }

    return octets;
  }

  /**
   * Writes the given phonenumber to the stream (BCD coded)
   *
   * @param os     Stream to write to
   * @param number Number to convert
   * @throws IOException when failing to write to os
   */
  public static void writeBcdNumber(OutputStream os, String number) throws IOException {
    int bcd = 0x00;
    int n = 0;

    // First convert to a "half octet" value
    for (int i = 0; i < number.length(); i++) {
      switch (number.charAt(i)) {
        case '0':
          bcd |= 0x00;
          break;
        case '1':
          bcd |= 0x10;
          break;
        case '2':
          bcd |= 0x20;
          break;
        case '3':
          bcd |= 0x30;
          break;
        case '4':
          bcd |= 0x40;
          break;
        case '5':
          bcd |= 0x50;
          break;
        case '6':
          bcd |= 0x60;
          break;
        case '7':
          bcd |= 0x70;
          break;
        case '8':
          bcd |= 0x80;
          break;
        case '9':
          bcd |= 0x90;
          break;
        case '*':
          bcd |= 0xA0;
          break;
        case '#':
          bcd |= 0xB0;
          break;
        case 'a':
          bcd |= 0xC0;
          break;
        case 'b':
          bcd |= 0xE0;
          break;
        default:
      }

      n++;

      if (n == 2) {
        os.write(bcd);
        n = 0;
        bcd = 0x00;
      } else {
        bcd >>= 4;
      }
    }

    if (n == 1) {
      bcd |= 0xF0;
      os.write(bcd);
    }
  }

  /**
   * Converts bytes to BCD format
   *
   * @param is     The byte InputStream
   * @param length how many
   * @return Decoded number
   */
  public static String readBcdNumber(InputStream is, int length) throws IOException {
    byte[] arr = new byte[length];
    is.read(arr, 0, length);
    return readBcdNumber(arr, 0, length);
  }

  /**
   * Converts bytes to BCD format
   *
   * @param data   bytearray
   * @param length how many
   * @param offset
   * @return Decoded number
   */
  public static String readBcdNumber(byte[] data, int offset, int length) {
    StringBuilder out = new StringBuilder();
    for (int i = offset; i < offset + length; i++) {
      int arrb = data[i];
      if ((data[i] & 15) <= 9) {
        out.append(data[i] & 15);
      }
      if ((data[i] & 15) == 0xA) {
        out.append("*");
      }
      if ((data[i] & 15) == 0xB) {
        out.append("#");
      }
      arrb = (arrb >>> 4);
      if ((arrb & 15) <= 9) {
        out.append(arrb & 15);
      }
      if ((arrb & 15) == 0xA) {
        out.append("*");
      }
      if ((arrb & 15) == 0xB) {
        out.append("#");
      }
    }
    return out.toString();
  }
}
