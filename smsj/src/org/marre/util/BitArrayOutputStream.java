/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package org.marre.util;

import java.io.*;

/**
 *
 */
public class BitArrayOutputStream extends ByteArrayOutputStream
{
    private int myBitOffset;
    private int myBuffer;

    /**
     * Default constructor.
     */
    public BitArrayOutputStream(int size)
    {
        super(size);
        resetBitCounter();
    }

    public synchronized byte[] toByteArray()
    {
        flushByte();
        return super.toByteArray();
    }

    private synchronized void resetBitCounter()
    {
        myBitOffset = 0;
        myBuffer = 0x00;
    }

    public synchronized void reset()
    {
        super.reset();
        resetBitCounter();
    }

    public synchronized void flushByte()
    {
        if (myBitOffset > 0)
        {
            super.write(myBuffer);
            resetBitCounter();
        }
    }

    public synchronized void writeBits( byte[] data, int nBits )
    {
        for(int i=0; nBits > 0; i++)
        {
            writeBits(data[i], Math.min(nBits, 8));
            nBits -= 8;
        }
    }

    public synchronized void writeBits( int data, int nBits )
    {
        while (nBits > 0)
        {
            writeBit(data & 0x01);
            data >>= 1;
            nBits--;
        }
    }

    public synchronized void writeBit( int bit )
    {
        myBuffer |= ((bit & 0x01) << myBitOffset);
        myBitOffset++;

        if (myBitOffset == 8)
        {
            flushByte();
        }
    }

    public synchronized void write(int data)
    {
        writeBits(data, 8);
    }

    public synchronized void write(byte[] data)
    {
        writeBits(data, 8 * data.length);
    }

    public synchronized void write(byte[] data, int off, int len)
    {
        throw new RuntimeException("Not supported yet");
    }

    public synchronized void close()
        throws IOException
    {
        flushByte();
        super.close();
    }
}
