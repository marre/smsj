/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
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
 * ***** END LICENSE BLOCK ***** */
package org.marre.sms.nokia;

import java.io.UnsupportedEncodingException;

/**
 * Nokia Picture message
 * <p>
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaPictureMessage extends NokiaMultipartMessage
{
    /**
     * Creates a Nokia Picture Message
     *
     * @param theBitmap
     * @param theMsg
     */
    public NokiaPictureMessage(OtaBitmap theBitmap, String theMsg)
    {
        this(theBitmap, theMsg, false);
    }

    /**
     * Creates a Nokia Picture Message
     *
     * @param theBitmap
     * @param theMsg
     */
    public NokiaPictureMessage(byte[] theBitmap, String theMsg)
    {
        this(theBitmap, theMsg, false);
    }

    /**
     * Creates a Nokia Picture Message
     *
     * @param theBitmap
     * @param theMsg
     * @param asUnicode Set to true if text should be sent as unicode
     */
    public NokiaPictureMessage(OtaBitmap theBitmap, String theMsg, boolean asUnicode)
    {
        this(theBitmap.getBytes(), theMsg, asUnicode);
    }

    /**
     * Creates a Nokia Picture Message
     *
     * @param theBitmap
     * @param theMsg
     * @param asUnicode Set to true if text should be sent as unicode
     */
    public NokiaPictureMessage(byte[] theBitmap, String theMsg, boolean asUnicode)
    {
        addBitmap(theBitmap);
        addText(theMsg, asUnicode);
    }

    /**
     * Used internally to add the image
     *
     * @param theBitmap
     */
    private void addBitmap(byte[] theBitmap)
    {
        addMultipart(NokiaPart.ITEM_OTA_BITMAP, theBitmap);
    }

    /**
     * Used internally to add the image
     * @param theBitmap
     */
    private void addBitmap(OtaBitmap theBitmap)
    {
        addMultipart(NokiaPart.ITEM_OTA_BITMAP, theBitmap.getBytes());
    }

    /**
     * Used internally to add text
     *
     * @param theMsg
     * @param asUnicode
     */
    private void addText(String theMsg, boolean asUnicode)
    {
        try
        {
            if (asUnicode)
            {
                addMultipart(NokiaPart.ITEM_TEXT_UNICODE, theMsg.getBytes("UTF-16BE"));
            }
            else
            {
                addMultipart(NokiaPart.ITEM_TEXT_ISO_8859_1, theMsg.getBytes("ISO-8859-1"));
            }
        }
        catch (UnsupportedEncodingException ex)
        {
            //myLog.fatal("Shouldn't happen, 'UTF-16BE' and 'ISO-8859-1' are in the standard", ex);
        }
    }
}

