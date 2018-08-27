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
package com.github.xfslove.sms.nokia;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.Serializable;

/**
 * Nokia OTA Bitmap format
 * <p>
 * This class can currently only handle non-animated B/W OTA Bitmaps.
 * <p>
 * Format is:
 *
 * <pre>
 *                     Octet 1 -&gt; 0 : Not sure what this is
 *                     Octet 2 -&gt; &lt;width&gt; : Width of image
 *                     Octet 3 -&gt; &lt;height&gt; : Height of image
 *                     Octet 4 -&gt; 1 : Number of colors?? B/W == 1?
 *                     Octet 5-n -&gt; &lt;imgdata&gt; : Image data 1 bit for each pixel
 * </pre>
 * <p>
 * I have only verified this class with BufferedImages of type TYPE_INT_ARGB
 *
 * @author Markus Eriksson
 */
public class OtaBitmap implements Serializable {

  private int width;
  private int height;

  private byte[] otaImgData;

  /**
   * Initialise with a raw Ota Bitmap
   *
   * @param otaBitmapData data
   */
  public OtaBitmap(byte[] otaBitmapData) {
    if (otaBitmapData != null) {
      //Read info field read until no more fields left bit 7 is 0
      //assume just 1 for now
      int infoField = otaBitmapData[0];
      width = otaBitmapData[1];
      height = otaBitmapData[2];
      int depth = otaBitmapData[3];

      int length = otaBitmapData.length - 4;
      otaImgData = new byte[length];

      System.arraycopy(otaBitmapData, 4, otaImgData, 0, length);
    }
  }

  /**
   * Creates an OtaBitmap object from an BufferedImage.
   * <p>
   * Every pixel that is not white will be converted to black.
   *
   * @param img Image to convert.
   */
  public OtaBitmap(BufferedImage img) {
    int bitOffset = 0;
    int data = 0;
    int nByte = 0;
    int nTotalBytes = 0;
    Raster raster = img.getData();

    width = img.getWidth();
    height = img.getHeight();

    nTotalBytes = (width * height) / 8;
    if (((width * height) % 8) > 0) {
      nTotalBytes++;
    }

    otaImgData = new byte[nTotalBytes];

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int color = img.getRGB(x, y);

        if (color != 0) {
          data |= ((1 << (7 - bitOffset)) & 0xff);
        }

        bitOffset++;

        if (bitOffset >= 8) {
          otaImgData[nByte] = (byte) (data & 0xff);

          bitOffset = 0;
          data = 0x00;
          nByte++;
        }
      }
    }

    if (bitOffset > 0) {
      otaImgData[nByte] = (byte) (data & 0xff);
    }
  }

  /**
   * Returns the created image data (not including image header)
   *
   * @return Image data
   */
  public byte[] getImageData() {
    return otaImgData;
  }

  /**
   * Returns the encoded OtaBitmap
   *
   * @return An encoded OtaBitmap
   */
  public byte[] getBytes() {
    byte[] otaBitmap = new byte[otaImgData.length + 4];

    // Not sure what this is
    otaBitmap[0] = 0;
    otaBitmap[1] = (byte) (width & 0xff);
    otaBitmap[2] = (byte) (height & 0xff);
    // Number of colors
    otaBitmap[3] = 1;

    // Add image data
    System.arraycopy(otaImgData, 0, otaBitmap, 4, otaImgData.length);

    return otaBitmap;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public void setHeight(int i) {
    height = i;
  }

  public void setWidth(int i) {
    width = i;
  }

}
