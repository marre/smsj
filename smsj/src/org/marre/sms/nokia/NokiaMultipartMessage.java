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

import java.io.*;
import java.util.LinkedList;

import org.marre.sms.util.SmsUdhUtil;
import org.marre.sms.*;

/**
 * Baseclass for Nokia Multipart Messages
 * <p>
 * Baseclass for messages that rely on the Nokia Multipart Messages
 *
 * @author Markus Eriksson
 * @version $Id$
 */
abstract class NokiaMultipartMessage extends SmsConcatMessage
{
    private boolean isDirty = true;

    private LinkedList myParts = new LinkedList();

    /**
     * Creates a Nokia Multipart Message
     */
    protected NokiaMultipartMessage()
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);
    }

    /**
     * Adds a part to this multipart message
     *
     * @param theItemType Type
     * @param data Content
     */
    protected void addMultipart(byte theItemType, byte[] data)
    {
        myParts.add(new NokiaPart(theItemType, data));
        isDirty = true;
    }

    /**
     * Removes all parts from the message
     */
    protected void clear()
    {
        myParts.clear();
        isDirty = true;
    }

    /**
     * Builds the pdus for this message
     * <p>
     * It relies on SmsConcat message to actually build the different PDU:s
     *
     */
    protected void buildPdus()
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
        }

        // Let SmsConcatMessage build the pdus...
        setContent(udhElements, baos.toByteArray(), baos.size());
    }

    /**
     * Overridden to rebuild the message if dirty
     */
    public SmsPdu[] getPdus()
    {
        if (isDirty)
        {
            buildPdus();
            isDirty = false;
        }

        return super.getPdus();
    }

    /**
     * Overridden to rebuild the message if dirty
     */
    public int getUserDataLength()
    {
        if (isDirty)
        {
            buildPdus();
            isDirty = false;
        }

        return super.getUserDataLength();
    }

    /**
     * Overridden to rebuild the message if dirty
     */
    public SmsUdhElement[] getUdhElements()
    {
        if (isDirty)
        {
            buildPdus();
            isDirty = false;
        }

        return super.getUdhElements();
    }

    /**
     * Overridden to rebuild the message if dirty
     */
    public byte[] getUserData()
    {
        if (isDirty)
        {
            buildPdus();
            isDirty = false;
        }

        return super.getUserData();
    }

    /**
     * Overridden to rebuild the message if dirty
     */
    public byte[] getUserDataHeaders()
    {
        if (isDirty)
        {
            buildPdus();
            isDirty = false;
        }

        return super.getUserDataHeaders();
    }
}
