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
package org.marre.util;

import java.text.StringCharacterIterator;
import java.util.*;
import java.io.*;

/**
 * Various functions to encode and decode strings
 *
 * @author Markus Eriksson
 */
public class StringUtil
{
	private Hashtable IRA_Uni = new Hashtable(127, 1.0F);
    private Hashtable Uni_IRA = new Hashtable(127, 1.0F);
    /**
     * This class isn't intended to be instantiated
     */
    public StringUtil()
    {
    	crearTablaConversiones();
    }

    /**
     *
     * @param stringTable
     * @param text
     * @return
     */
    public static int findString(String stringTable[], String text)
    {
        if (stringTable != null)
        {
            for(int i=0; i < stringTable.length; i++)
            {
                if (stringTable[i].equals(text))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Converts a byte array to a string with hex values.
     *
     * @param theData Data to convert
     * @return the encoded string
     */
    public static String bytesToHexString(byte[] theData)
    {
        StringBuffer hexStrBuff = new StringBuffer(theData.length*2);

        for(int i=0; i < theData.length; i++)
        {
            String hexByteStr = Integer.toHexString(theData[i] & 0xff).toUpperCase();
            if (hexByteStr.length() == 1)
            {
                hexStrBuff.append("0");
            }
            hexStrBuff.append(hexByteStr);
        }

        return hexStrBuff.toString();
    }

    /**
     * Converts a byte to a string with hex values.
     *
     * @param theByte Byte to convert
     * @return the encoded string
     */
    public static String byteToHexString(byte theByte)
    {
        StringBuffer hexStrBuff = new StringBuffer(2);

        String hexByteStr = Integer.toHexString(theByte & 0xff).toUpperCase();
        if (hexByteStr.length() == 1)
        {
            hexStrBuff.append("0");
        }
        hexStrBuff.append(hexByteStr);

        return hexStrBuff.toString();
    }

    /**
     * Converts a string of hex characters to a byte array
     *
     * @param theHexString The hex string to read
     * @return the resulting byte array
     */
    public static byte[] hexStringToBytes(String theHexString)
    {
        byte data[] = new byte[theHexString.length()/2];

        for(int i=0; i < data.length; i++)
        {
            String a = theHexString.substring(i*2, i*2+2);
            data[i] = (byte)Integer.parseInt(a, 16);
        }

        return data;
    }

    public static String intToString(int value, int nChars)
    {
        String strValue = Integer.toString(value);
        StringBuffer strBuf = new StringBuffer(nChars);

        for (int i = strValue.length(); i < nChars; i++)
        {
            strBuf.append('0');
        }
        strBuf.append(strValue);

        return strBuf.toString();
    }
    public String encodeInIRA(String parString) {
		parString = unicode2IRA(parString);
		StringCharacterIterator sci = new StringCharacterIterator(parString);
		String bits = "";
		for (char c = sci.first(); c != '\uffff'; c = sci.next()) {
		    StringBuffer bin = new StringBuffer("00000000");
		    String binValue = Integer.toBinaryString(c);
		    bin.replace(8 - binValue.length(), 8, binValue);
		    binValue = bin.toString();
		    bits = binValue.concat(bits);
		}
		StringBuffer salida = new StringBuffer();
		for (int x = bits.length() / 8 - 1; x >= 0; x--) {
		    StringBuffer hex = new StringBuffer("00");
		    String cadenaHex
			= Integer.toHexString
			      (Integer.valueOf
				   (bits.substring(8 * x, 8 * (x + 1)), 2).intValue())
			      .toUpperCase();
		    hex.replace(2 - cadenaHex.length(), 2, cadenaHex);
		    salida.append(hex);
		}
		return salida.toString();
    }
    public String unicode2IRA(String s) {
	if (s == null)
	    return null;
	StringCharacterIterator sci = new StringCharacterIterator(s);
	StringBuffer sb = new StringBuffer(s.length());
	for (char c = sci.first(); c != '\uffff'; c = sci.next()) {
	    if (c == '\u0080')
		sb.append('\033').append('e');
	    else if (c == '\t')
		sb.append(' ');
	    else if (c == '[')
		sb.append('\033').append('e');
	    else if (c == '{')
		sb.append('\033').append('(');
	    else if (c == ']')
		sb.append('\033').append('>');
	    else if (c == '}')
		sb.append('\033').append(')');
	    else if (c == '^')
		sb.append('\033').append('\024');
	    else if (c == '\\')
		sb.append('\033').append('/');
	    else if (c == '|')
		sb.append('\033').append('@');
	    else if (c == '~')
		sb.append('\033').append('=');
	    else if (c == '\u00e1')
		sb.append('a');
	    else if (c == '\u00ed')
		sb.append('i');
	    else if (c == '\u00f3')
		sb.append('o');
	    else if (c == '\u00fa')
		sb.append('u');
	    else if (c == '\u00c1')
		sb.append('A');
	    else if (c == '\u00c9')
		sb.append('E');
	    else if (c == '\u00cd')
		sb.append('I');
	    else if (c == '\u00d3')
		sb.append('O');
	    else if (c == '\u00da')
		sb.append('U');
	    else {
		Character car = new Character(c);
		Character car1 = (Character) Uni_IRA.get(car);
		if (car1 == null)
		    car1 = new Character('.');
		sb.append(car1);
	    }
	}
	return sb.toString();
    }
     private void sustituirEnTablas(int i, char c) {
	Character car = new Character((char) i);
	Character car1 = new Character(c);
	IRA_Uni.put(car, car1);
	Uni_IRA.put(car1, car);
    }
    private void crearTablaConversiones() {
	for (int i = 0; i <= 127; i++) {
	    Character character = new Character((char) i);
	    Uni_IRA.put(character, character);
	    IRA_Uni.put(character, character);
	}
	sustituirEnTablas(0, '@');
	sustituirEnTablas(1, '\u00a3');
	sustituirEnTablas(2, '$');
	sustituirEnTablas(3, '\u00a5');
	sustituirEnTablas(4, '\u00e8');
	sustituirEnTablas(5, '\u00e9');
	sustituirEnTablas(6, '\u00f9');
	sustituirEnTablas(7, '\u00ec');
	sustituirEnTablas(8, '\u00f2');
	sustituirEnTablas(9, '\u00c7');
	sustituirEnTablas(11, '\u00d8');
	sustituirEnTablas(12, '\u00f8');
	sustituirEnTablas(14, '\u00c5');
	sustituirEnTablas(15, '\u00e5');
	sustituirEnTablas(16, '\u0394');
	sustituirEnTablas(17, '_');
	sustituirEnTablas(18, '\u03a6');
	sustituirEnTablas(19, '\u0393');
	sustituirEnTablas(20, '\u039b');
	sustituirEnTablas(21, '\u03a9');
	sustituirEnTablas(22, '\u03a0');
	sustituirEnTablas(23, '\u03a8');
	sustituirEnTablas(24, '\u03a3');
	sustituirEnTablas(25, '\u0398');
	sustituirEnTablas(26, '\u039e');
	sustituirEnTablas(28, '\u00c6');
	sustituirEnTablas(29, '\u00e6');
	sustituirEnTablas(30, '\u00df');
	sustituirEnTablas(31, '\u00c9');
	sustituirEnTablas(36, '\u00a4');
	sustituirEnTablas(64, '\u00a1');
	sustituirEnTablas(91, '\u00c4');
	sustituirEnTablas(92, '\u00d6');
	sustituirEnTablas(93, '\u00d1');
	sustituirEnTablas(94, '\u00dc');
	sustituirEnTablas(95, '\u00a7');
	sustituirEnTablas(96, '\u00bf');
	sustituirEnTablas(123, '\u00e4');
	sustituirEnTablas(124, '\u00f6');
	sustituirEnTablas(125, '\u00f1');
	sustituirEnTablas(126, '\u00fc');
	sustituirEnTablas(127, '\u00e0');
    }
}

