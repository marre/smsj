package org.marre.sms.util;

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
        reset();
    }

    public synchronized byte[] taoByteArray()
        throws IOException
    {
        flushByte();
        return super.toByteArray();
    }

    public synchronized void reset()
    {
        super.reset();
        myBitOffset = 0;
        myBuffer = 0x00;
    }

    public synchronized void flushByte()
    {
        if (myBitOffset > 0)
        {
            super.write(myBuffer);
            reset();
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
        myBuffer <<= 1;
        myBuffer |= (bit & 0x01);
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

