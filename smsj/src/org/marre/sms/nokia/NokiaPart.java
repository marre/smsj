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

/**
 * Used in NokiaMultipartMessages
 *
 * @author Markus Eriksson
 * @version $Id$
 */
class NokiaPart
{
    static final byte ITEM_TEXT_ISO_8859_1 = 0x00;
    static final byte ITEM_TEXT_UNICODE = 0x01;
    static final byte ITEM_OTA_BITMAP = 0x02;
    static final byte ITEM_RINGTONE = 0x03;
    static final byte ITEM_PROFILE_NAME = 0x04;
    static final byte ITEM_SCREEN_SAVER = 0x06;

    private byte myItemType;
    private byte[] myData;

    /**
     *
     * @param theItemType
     * @param data
     */
    NokiaPart(byte theItemType, byte[] data)
    {
        myItemType = theItemType;
        myData = data;
    }

    /**
     *
     * @return
     */
    byte getItemType()
    {
        return myItemType;
    }

    /**
     *
     * @return
     */
    byte[] getData()
    {
        return myData;
    }
}
