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

import java.util.*;
import java.io.*;

/**
 * Various functions to encode and decode strings
 *
 * @author Markus Eriksson
 */
public class StringUtil
{
    /**
     * This class isn't intended to be instantiated
     */
    private StringUtil()
    {
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
}

