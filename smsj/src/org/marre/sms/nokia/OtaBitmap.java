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

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import org.marre.sms.util.SmsPduUtil;

/**
 * Nokia OTA Bitmap format
 * <p>
 * This class can currently only handle non-animated B/W OTA Bitmaps.
 * <p>
 * Format is:
 * <pre>
 * Octet 1 -> 0 : Not sure what this is
 * Octet 2 -> <width> : Width of image
 * Octet 3 -> <height> : Height of image
 * Octet 4 -> 1 : Number of colors?? B/W == 1?
 * Octet 5-n -> <imgdata> : Image data 1 bit for each pixel
 * </pre>
 * I have only verified this class with BufferedImages of type TYPE_INT_ARGB
 *
 * @author Markus Eriksson
 */
public class OtaBitmap
{
    private int myWidth = 0;
    private int myHeight = 0;

    private byte myOtaImgData[] = null;

    /**
     * Creates an OtaBitmap object from an BufferedImage.
     * <p>
     * Every pixel that is not white will be converted
     * to black.
     *
     * @param theImg Image to convert.
     */
    public OtaBitmap(BufferedImage theImg)
    {
        int bitOffset = 0;
        int data = 0;
        int nByte = 0;
        int nTotalBytes = 0;
        Raster raster = theImg.getData();

        myWidth = theImg.getWidth();
        myHeight = theImg.getHeight();

        nTotalBytes = (myWidth * myHeight) / 8;
        if ( ((myWidth * myHeight) % 8) > 0)
        {
            nTotalBytes++;
        }

        myOtaImgData = new byte[nTotalBytes];

        for (int y=0; y < myHeight; y++)
        {
            for (int x=0; x < myWidth; x++)
            {
                int color = theImg.getRGB(x, y);

                if (color != 0)
                {
                    data |= ((1 << (7-bitOffset)) & 0xff);
                }

                bitOffset++;

                if (bitOffset >= 8)
                {
                    myOtaImgData[nByte] = (byte)(data & 0xff);

                    bitOffset = 0;
                    data = 0x00;
                    nByte++;
                }
            }
        }

        if (bitOffset > 0)
        {
            myOtaImgData[nByte] = (byte)(data & 0xff);
        }
    }

    /**
     * Returns the created image data (not including image header)
     *
     * @return Image data
     */
    public byte[] getImageData()
    {
        return myOtaImgData;
    }

    /**
     * Returns the encoded OtaBitmap
     *
     * @return An encoded OtaBitmap
     */
    public byte[] getBytes()
    {
        byte[] otaBitmap = new byte[myOtaImgData.length + 4];

        otaBitmap[0] = 0; // Not sure what this is
        otaBitmap[1] = (byte) (myWidth & 0xff);
        otaBitmap[2] = (byte) (myHeight & 0xff);
        otaBitmap[3] = 1; // Number of colors

        // Add image data
        SmsPduUtil.arrayCopy(myOtaImgData, 0, otaBitmap, 4, 0, myOtaImgData.length * 8);

        return otaBitmap;
    }
}
