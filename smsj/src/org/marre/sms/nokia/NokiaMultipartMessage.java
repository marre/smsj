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

import java.io.*;
import java.util.LinkedList;
import java.awt.image.BufferedImage;

import org.apache.commons.logging.*;

import org.marre.sms.SmsConstants;
import org.marre.sms.SmsConcatMessage;
import org.marre.sms.SmsUdhElement;
import org.marre.sms.SmsPdu;
import org.marre.sms.util.SmsUdhUtil;

/**
 * Nokia Multipart Message format
 *
 * @autor Markus Eriksson
 * @version $Id$
 */
abstract class NokiaMultipartMessage extends SmsConcatMessage
{
    private static Log myLog = LogFactory.getLog(NokiaMultipartMessage.class);

    private LinkedList myParts = new LinkedList();

    /**
     *
     */
    protected NokiaMultipartMessage()
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);
    }

    /**
     *
     * @param theItemType
     * @param data
     */
    protected void addMultipart(byte theItemType, byte[] data)
    {
        myParts.add(new NokiaPart(theItemType, data));
    }

    /**
     *
     * @return
     */
    public SmsPdu[] getPdus()
    {
        SmsUdhElement[] udhElements = new SmsUdhElement[1];
        ByteArrayOutputStream baos = new ByteArrayOutputStream(140);

        // Port
        udhElements[0] = SmsUdhUtil.get16BitApplicationPortUdh(SmsConstants.PORT_NOKIA_MULTIPART_MESSAGE, 0);

        // Payload

        try
        {
            // Header or something...
            baos.write(0x30);

            // Loop through all multiparts and add them
            for(int i=0; i < myParts.size(); i++)
            {
                NokiaPart part = (NokiaPart) myParts.get(i);
                byte[] data = part.getData();

                // Type - 1 octet
                baos.write(part.getItemType());
                // Length - 2 octets
                baos.write((byte)((data.length >> 8) & 0xff));
                baos.write((byte)(data.length & 0xff));
                // Data - n octets
                baos.write(data);
            }

            baos.close();
        }
        catch (IOException ex)
        {
            // Should not happen!
            myLog.fatal("Failed to write to ByteArrayOutputStream!", ex);
        }

        // Let SmsConcatMessage build the pdus...
        setContent(udhElements, baos.toByteArray(), baos.size());
        return super.getPdus();
    }
}
