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

import org.apache.commons.logging.*;

import org.marre.sms.SmsConstants;
import org.marre.sms.SmsConcatMessage;
import org.marre.sms.SmsUdhElement;
import org.marre.sms.util.SmsUdhUtil;

/**
 * Nokia Group Graphic (CLI) message
 * <p>
 * <b>Note!</b> I haven't been able to verify that this class works since
 * I don't have access to a phone that can handle Group Graphic.
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaGroupGraphic extends SmsConcatMessage
{
    private static Log myLog = LogFactory.getLog(NokiaGroupGraphic.class);

    /**
     * Creates a group graphic SMS message
     *
     * @param theOtaBitmap An OtaBitmap object representing the
     * image to send
     */
    public NokiaGroupGraphic(OtaBitmap theOtaBitmap)
    {
        this(theOtaBitmap.getBytes());
    }

    /**
     * Creates a group graphic SMS message
     * <p>
     * The given byte array must be in the Nokia OTA image format.
     *
     * @param theOtaImage The ota image as a byte-array
     */
    public NokiaGroupGraphic(byte[] theOtaImage)
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);
        setContent(theOtaImage);
    }

    private void setContent(byte[] theOtaBitmap)
    {
        SmsUdhElement[] udhElements = new SmsUdhElement[1];
        ByteArrayOutputStream baos = new ByteArrayOutputStream(140);

        // Port
        udhElements[0] = SmsUdhUtil.get16BitApplicationPortUdh(SmsConstants.PORT_NOKIA_CLI_LOGO, 0);

        // Payload
        try
        {
            // Type?
            baos.write(0x30);
            // bitmap
            baos.write(theOtaBitmap);

            baos.close();
        }
        catch (IOException ex)
        {
            // Should not happen!
            myLog.fatal("Failed to write to ByteArrayOutputStream", ex);
        }

        // Let SmsConcatMessage build the pdus...
        setContent(udhElements, baos.toByteArray(), baos.size());
    }
}

