/*
    SMS Library for the Java platform
    Copyright (C) 2002  Markus Eriksson

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.marre.sms.nokia;

import org.apache.commons.logging.*;

import java.io.UnsupportedEncodingException;

/**
 * Nokia Picture message
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaPictureMessage extends NokiaMultipartMessage
{
    private static Log myLog = LogFactory.getLog(NokiaPictureMessage.class);

    public static final int PICTURE_ABOVE_TEXT = 1;
    public static final int PICTURE_BELOW_TEXT = 2;

    /**
     *
     * @param theBitmap
     * @param theMsg
     */
    public NokiaPictureMessage(OtaBitmap theBitmap, String theMsg, int theOrientation)
    {
        this(theBitmap, theMsg, theOrientation, false);
    }

    /**
     *
     * @param theBitmap
     * @param theMsg
     */
    public NokiaPictureMessage(byte[] theBitmap, String theMsg, int theOrientation)
    {
        this(theBitmap, theMsg, theOrientation, false);
    }

    /**
     *
     * @param theBitmap
     * @param theMsg
     * @param asUnicode
     */
    public NokiaPictureMessage(OtaBitmap theBitmap, String theMsg, int theOrientation, boolean asUnicode)
    {
        this(theBitmap.getBytes(), theMsg, theOrientation, asUnicode);
    }

    /**
     *
     * @param theBitmap
     * @param theMsg
     * @param asUnicode
     */
    public NokiaPictureMessage(byte[] theBitmap, String theMsg, int theOrientation, boolean asUnicode)
    {
        if (theOrientation == PICTURE_ABOVE_TEXT)
        {
            addBitmap(theBitmap);
            addText(theMsg, asUnicode);
        }
        else
        {
            addText(theMsg, asUnicode);
            addBitmap(theBitmap);
        }
    }

    /**
     *
     * @param theBitmap
     */
    private void addBitmap(byte[] theBitmap)
    {
        addMultipart(NokiaPart.ITEM_OTA_BITMAP, theBitmap);
    }

    /**
     *
     * @param theBitmap
     */
    private void addBitmap(OtaBitmap theBitmap)
    {
        addMultipart(NokiaPart.ITEM_OTA_BITMAP, theBitmap.getBytes());
    }

    /**
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
            myLog.fatal("Shouldn't happen, 'UTF-16BE' and 'ISO-8859-1' is in the standard", ex);
        }
    }
}
