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
import java.awt.image.BufferedImage;

import org.apache.commons.logging.*;

import org.marre.sms.SmsConstants;
import org.marre.sms.SmsConcatMessage;
import org.marre.sms.SmsUdhElement;
import org.marre.sms.util.SmsPduUtil;
import org.marre.sms.util.SmsUdhUtil;

/**
 * Nokia Operator Logo message
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaOperatorLogo extends SmsConcatMessage
{
    static Log myLog = LogFactory.getLog(NokiaGroupGraphic.class);

    /**
     *
     * @param theOtaBitmap
     * @param theMcc GSM Mobile Country Code
     * @param theMnc GSM Mobile Network Code
     */
    public NokiaOperatorLogo(OtaBitmap theOtaBitmap, int theMcc, int theMnc)
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);
        setContent(theOtaBitmap.getBytes(), theMcc, theMnc);
    }

    /**
     *
     * @param theImg
     * @param theMcc GSM Mobile Country Code
     * @param theMnc GSM Mobile Network Code
     */
    public NokiaOperatorLogo(BufferedImage theImg, int theMcc, int theMnc)
    {
        this(new OtaBitmap(theImg), theMcc, theMnc);
    }

    /**
     *
     * @param theOtaImage The ota image as an hexstring
     * @param theMcc
     * @param theMnc
     */
    public NokiaOperatorLogo(String theOtaImage, int theMcc, int theMnc)
    {
        super(SmsConstants.DCS_DEFAULT_8BIT);
        setContent(SmsPduUtil.hexStringToBytes(theOtaImage), theMcc, theMnc);
    }

    /**
     *
     * @param theBitmap
     * @param theOperatorMccMnc Operator defined in org.marre.sms.util.GsmOperators
     */
    public NokiaOperatorLogo(OtaBitmap theBitmap, int[] theOperatorMccMnc)
    {
        this(theBitmap, theOperatorMccMnc[0], theOperatorMccMnc[1]);
    }

    /**
     *
     * @param theImg
     * @param theOperatorMccMnc Operator defined in org.marre.sms.util.GsmOperators
     */
    public NokiaOperatorLogo(BufferedImage theImg, int[] theOperatorMccMnc)
    {
        this(new OtaBitmap(theImg), theOperatorMccMnc);
    }

    /**
     *
     * @param theOtaImage The ota image as an hex string
     * @param theOperatorMccMnc
     */
    public NokiaOperatorLogo(String theOtaImage, int[] theOperatorMccMnc)
    {
        this(theOtaImage, theOperatorMccMnc[0], theOperatorMccMnc[1]);
    }

    private void setContent(byte[] theOtaBitmap, int theMcc, int theMnc)
    {
        SmsUdhElement[] udhElements = new SmsUdhElement[1];
        ByteArrayOutputStream baos = new ByteArrayOutputStream(140);

        // Port
        udhElements[0] = SmsUdhUtil.get16BitApplicationPortUdh(SmsConstants.PORT_NOKIA_OPERATOR_LOGO, 0);

        // Payload

        try
        {
            // Version ("0" - ISO-88591)
            baos.write(0x30);
            // mcc
            SmsPduUtil.writeBcdNumber(baos, "" + theMcc);
            // mnc
            SmsPduUtil.writeBcdNumber(baos, "" + theMnc);
            // <lf>
            baos.write(0x0A);
            // bitmap
            baos.write(theOtaBitmap);

            baos.close();
        }
        catch (IOException ex)
        {
            // Should not happen!
            myLog.fatal("Failed to write to ByteArrayOutputStream", ex);
        }

        setContent(udhElements, baos.toByteArray(), baos.size());
    }
}
