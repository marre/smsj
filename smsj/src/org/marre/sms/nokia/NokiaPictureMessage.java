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

    /**
     *
     * @param theBitmap
     * @param theMsg
     */
    public NokiaPictureMessage(OtaBitmap theBitmap, String theMsg)
    {
        this(theBitmap, theMsg, false);
    }

    /**
     *
     * @param theMsg
     * @param theBitmap
     */
    public NokiaPictureMessage(String theMsg, OtaBitmap theBitmap)
    {
        this(theMsg, theBitmap, false);
    }

    /**
     *
     * @param theBitmap
     * @param theMsg
     * @param asUnicode
     */
    public NokiaPictureMessage(OtaBitmap theBitmap, String theMsg, boolean asUnicode)
    {
        addBitmap(theBitmap);
        addText(theMsg, asUnicode);
    }

    /**
     *
     * @param theMsg
     * @param theBitmap
     * @param asUnicode
     */
    public NokiaPictureMessage(String theMsg, OtaBitmap theBitmap, boolean asUnicode)
    {
        addText(theMsg, asUnicode);
        addBitmap(theBitmap);
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
