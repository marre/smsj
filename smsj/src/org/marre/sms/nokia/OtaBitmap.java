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

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.OutputStream;
import java.io.IOException;

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
