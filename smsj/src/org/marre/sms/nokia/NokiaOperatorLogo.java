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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.marre.sms.SmsConcatMessage;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsPduUtil;
import org.marre.sms.SmsUdhElement;
import org.marre.sms.SmsUdhUtil;
import org.marre.sms.SmsUserData;

/**
 * Nokia Operator Logo message
 * 
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaOperatorLogo extends SmsConcatMessage
{
    /**
     * If set to true it will make the message two bytes shorter to make it
     * possible to fit a 72x14 pixel image in one SMS instead of two. <br>
     * <b>Note! </b> This will probably only work on Nokia phones...
     */
    protected boolean myDiscardNokiaHeaders;
    
    /** The ota image as a byte array */
    protected byte[] myBitmap;
    
    /** GSM Mobile Country Code */
    protected int myMcc;
    
    /** GSM Mobile Network Code */
    protected int myMnc;

    /**
     * Creates a Nokia Operator Logo message
     * 
     * @param theOtaBitmap
     * @param theMcc
     *            GSM Mobile Country Code
     * @param theMnc
     *            GSM Mobile Network Code
     */
    public NokiaOperatorLogo(OtaBitmap theOtaBitmap, int theMcc, int theMnc)
    {
        this(theOtaBitmap.getBytes(), theMcc, theMnc);
    }

    /**
     * Creates a Nokia Operator Logo message
     * 
     * @param theOtaImage
     *            The ota image as a byte array
     * @param theMcc
     *            GSM Mobile Country Code
     * @param theMnc
     *            GSM Mobile Network Code
     */
    public NokiaOperatorLogo(byte[] theOtaImage, int theMcc, int theMnc)
    {
        myBitmap = theOtaImage;
        myMcc = theMcc;
        myMnc = theMnc;
    }

    /**
     * Creates a Nokia Operator Logo message
     * 
     * @param theOtaImage
     *            The ota image as a byte array
     * @param theMcc
     *            GSM Mobile Country Code
     * @param theMnc
     *            GSM Mobile Network Code
     */
    public NokiaOperatorLogo(byte[] theOtaImage, int theMcc, int theMnc, boolean discardHeaders)
    {
        myDiscardNokiaHeaders = discardHeaders;
        myBitmap = theOtaImage;
        myMcc = theMcc;
        myMnc = theMnc;
    }

    /**
     * Creates a Nokia Operator Logo message
     * 
     * @param theBitmap
     * @param theOperatorMccMnc
     *            Operator defined in org.marre.sms.util.GsmOperators
     */
    public NokiaOperatorLogo(OtaBitmap theBitmap, int[] theOperatorMccMnc)
    {
        this(theBitmap, theOperatorMccMnc[0], theOperatorMccMnc[1]);
    }

    /**
     * Creates a Nokia Operator Logo message
     * 
     * @param theOtaImage
     *            The ota image as a byte array
     * @param theOperatorMccMnc
     */
    public NokiaOperatorLogo(byte[] theOtaImage, int[] theOperatorMccMnc)
    {
        this(theOtaImage, theOperatorMccMnc[0], theOperatorMccMnc[1]);
    }

    public SmsUserData getUserData()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(140);

        try
        {
            if (!myDiscardNokiaHeaders)
            {
                // Header??
                baos.write(0x30);
            }

            // mcc
            SmsPduUtil.writeBcdNumber(baos, "" + myMcc);
            // mnc
            if (myMnc < 10)
            {
                SmsPduUtil.writeBcdNumber(baos, "0" + myMnc);
            }
            else
            {
                SmsPduUtil.writeBcdNumber(baos, "" + myMnc);
            }

            if (!myDiscardNokiaHeaders)
            {
                // Start of content?
                baos.write(0x0A);
            }
            // bitmap
            baos.write(myBitmap);

            baos.close();
        }
        catch (IOException ex)
        {
            // Should not happen!
        }

        return new SmsUserData(baos.toByteArray());
    }

    public SmsUdhElement[] getUdhElements()
    {
        return new SmsUdhElement[] { SmsUdhUtil.get16BitApplicationPortUdh(SmsConstants.PORT_NOKIA_OPERATOR_LOGO, 0) };
    }
}
