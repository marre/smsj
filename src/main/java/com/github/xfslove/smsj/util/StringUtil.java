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
package com.github.xfslove.smsj.util;

import com.github.xfslove.smsj.sms.charset.Gsm7BitCharset;
import com.github.xfslove.smsj.sms.charset.Gsm7BitCharsetProvider;
import com.github.xfslove.smsj.sms.dcs.SmsAlphabet;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Various functions to encode and decode strings.
 *
 * @author Markus Eriksson
 */
public final class StringUtil {

  private static final char[] RANDOM_CHARS = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();

  private static final Random RND = ThreadLocalRandom.current();

  /**
   * This class isn't intended to be instantiated.
   */
  private StringUtil() {
  }

  /**
   * @param stringTable to search
   * @param text        search text
   * @return index of text, -1 not found
   */
  public static int findString(String[] stringTable, String text) {
    if (stringTable != null) {
      for (int i = 0; i < stringTable.length; i++) {
        if ((stringTable[i] != null) && (stringTable[i].equals(text))) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Converts a byte array to a string with hex values.
   *
   * @param data Data to convert
   * @return the encoded string
   */
  public static String bytesToHexString(byte[] data) {
    StringBuilder hexStrBuff = new StringBuilder(data.length * 2);

    for (byte aData : data) {
      String hexByteStr = Integer.toHexString(aData & 0xff).toUpperCase();
      if (hexByteStr.length() == 1) {
        hexStrBuff.append("0");
      }
      hexStrBuff.append(hexByteStr);
    }

    return hexStrBuff.toString();
  }

  /**
   * Converts a byte to a string with hex values.
   *
   * @param data Byte to convert
   * @return the encoded string
   */
  public static String byteToHexString(byte data) {
    StringBuilder hexStrBuff = new StringBuilder(2);

    String hexByteStr = Integer.toHexString(data & 0xff).toUpperCase();
    if (hexByteStr.length() == 1) {
      hexStrBuff.append("0");
    }
    hexStrBuff.append(hexByteStr);

    return hexStrBuff.toString();
  }

  /**
   * Converts a string of hex characters to a byte array.
   *
   * @param hexString The hex string to read
   * @return the resulting byte array
   */
  public static byte[] hexStringToBytes(String hexString) {
    byte[] data = new byte[hexString.length() / 2];

    for (int i = 0; i < data.length; i++) {
      String a = hexString.substring(i * 2, i * 2 + 2);
      data[i] = (byte) Integer.parseInt(a, 16);
    }

    return data;
  }

  /**
   * Method intToString.
   * <p>
   * Converst an integer to nChars characters
   *
   * @param value  Integer value
   * @param nChars Number of chars to represent the "value"
   * @return String The string representing "value"
   */
  public static String intToString(int value, int nChars) {
    String strValue = Integer.toString(value);
    StringBuilder strBuf = new StringBuilder(nChars);

    for (int i = strValue.length(); i < nChars; i++) {
      strBuf.append('0');
    }
    strBuf.append(strValue);

    return strBuf.toString();
  }

  /**
   * Generates a random string of the given length.
   * <p>
   * "abcdefghijklmnopqrstuvwxyz1234567890"
   *
   * @param length length
   * @return A random string
   */
  public static String randString(int length) {
    StringBuilder sb = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      sb.append(RANDOM_CHARS[RND.nextInt(RANDOM_CHARS.length)]);
    }

    return sb.toString();
  }

  /**
   * Generates a string of the given alphabet.
   *
   * @param bytes    bytes
   * @param alphabet alphabet
   * @param reserved reserved alphabet charset
   * @return string value
   */
  public static String getString(byte[] bytes, SmsAlphabet alphabet, Charset reserved) {
    if (bytes == null || bytes.length == 0) {
      return null;
    }
    switch (alphabet) {
      case GSM:
        Charset charset = new Gsm7BitCharsetProvider().charsetForName(Gsm7BitCharset.CHARSET_NAME);
        return new String(bytes, charset);
      case LATIN1:
        return new String(bytes, StandardCharsets.ISO_8859_1);
      case UCS2:
        return new String(bytes, StandardCharsets.UTF_16BE);
      case RESERVED:
        return new String(bytes, reserved);
      default:
        return null;
    }
  }
}
