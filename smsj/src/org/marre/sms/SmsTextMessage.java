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

import java.io.*;

/**
 * Represents a text message.
 * <p>
 * The text can be sent in unicode (max 70 chars/SMS), 8-bit (max 140 chars/SMS)
 * or GSM encoding (max 160 chars/SMS).
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class SmsTextMessage extends SmsConcatMessage
{
    protected String myText;
    protected int myAlphabet;
    
    /**
     * Creates an SmsTextMessage with the given alphabet and message class.
     * <p>
     * theAlphabet can be any of:<br>
     * - SmsConstants.ALPHABET_GSM<br>
     * - SmsConstants.ALPHABET_8BIT<br>
     * - SmsConstants.ALPHABET_UCS2<br>
     * <p>
     * theMessageClass can be any of:<br>
     * - SmsConstants.MSG_CLASS_0 (Often called a FLASH message)<br>
     * - SmsConstants.MSG_CLASS_1<br>
     * - SmsConstants.MSG_CLASS_2<br>
     * - SmsConstants.MSG_CLASS_3<br>
     *
     * @param theMsg The message
     * @param theAlphabet The alphabet
     * @param theMessageClass The messageclass
     */
    public SmsTextMessage(String theMsg, int theAlphabet, byte theMessageClass)
    {
        this(theMsg, theAlphabet);
        int dcs = getDataCodingScheme() | 0x10 | theMessageClass;
        setDataCodingScheme((byte) (dcs & 0xff));
    }

    /**
     * Creates an SmsTextMessage with the given alphabet
     * <p>
     * theAlphabet can be any of:<br>
     * - SmsConstants.ALPHABET_GSM<br>
     * - SmsConstants.ALPHABET_8BIT<br>
     * - SmsConstants.ALPHABET_UCS2<br>
     *
     * @param theMsg The message
     * @param theAlphabet The alphabet
     */
    public SmsTextMessage(String theMsg, int theAlphabet)
    {
        myText = theMsg;
        myAlphabet = theAlphabet;
    }

    /**
     * Creates an SmsTextMessage with default 7Bit GSM Alphabet
     *
     * @param theMsg The message
     */
    public SmsTextMessage(String theMsg)
    {
        this(theMsg, SmsConstants.ALPHABET_GSM);
    }
    
    /**
     * Returns the text message. 
     */
    public String getText()
    {
        return myText;
    }

    /* (non-Javadoc)
     * @see org.marre.sms.SmsConcatMessage#getUserData()
     */
    public SmsUserData getUserData()
    {
        SmsUserData ud;
        
        try
        {
            switch (myAlphabet)
            {
            case SmsConstants.ALPHABET_GSM:
                // 7-bit encoding, No message class, No compression
                setDataCodingScheme(SmsConstants.DCS_DEFAULT_7BIT);
                ud = new SmsUserData(SmsPduUtil.getSeptets(myText), myText.length());
                break;
                
            case SmsConstants.ALPHABET_8BIT:
                // 8bit data encoding, No message class, No compression
                setDataCodingScheme(SmsConstants.DCS_DEFAULT_8BIT);
                ud = new SmsUserData(myText.getBytes("ISO-8859-1"), myText.length());
                break;
                
            case SmsConstants.ALPHABET_UCS2:
                // 16 bit UCS2 encoding, No message class, No compression
                setDataCodingScheme(SmsConstants.DCS_DEFAULT_UCS2);
                ud = new SmsUserData(myText.getBytes("UTF-16BE"), myText.length() * 2);
                break;
                
            default:
                ud = null;
                break;
            }
        }
        catch (UnsupportedEncodingException ex)
        {
            // Shouldn't happen. According to the javadoc documentation
            // for JDK 1.3.1 the "UTF-16BE" and "ISO-8859-1" encoding
            // are standard...
            throw new RuntimeException(ex.getMessage());
        }
        
        return ud;
    }

    public SmsUdhElement[] getUdhElements()
    {
        return null;
    }
}
