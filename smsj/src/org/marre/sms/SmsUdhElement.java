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
package org.marre.sms;

import java.io.*;

/**
 * Represents an User Data Header Element
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class SmsUdhElement
{
    private int myUdhIei = 0;
    private byte[] myUdhIeiData = null;

    /**
     * Creates an SmsUdhElement
     *
     * @param theUdhIei
     * @param theUdhIeiData
     */
    public SmsUdhElement(int theUdhIei, byte[] theUdhIeiData)
    {
        myUdhIei = theUdhIei;
        myUdhIeiData = theUdhIeiData;
    }

    /**
     * Returns the total length of this UDH element.
     * <p>
     * The length is including the UDH data length and the UDH "header" (2 bytes)
     * @return the length
     */
    public int getTotalLength()
    {
        return myUdhIeiData.length + 2;
    }

    /**
     * Returns the length of the UDH iei data
     * <p>
     * The length returned is only the length of the data
     * @return Length of data
     */
    public int getUdhIeiDataLength()
    {
        return myUdhIeiData.length;
    }

    /**
     * Returns the Udh Iei Data excluding the UDH "header"
     * @return Data
     */
    public byte[] getUdhIeiData()
    {
        return myUdhIeiData;
    }

    /**
     * Return the UDH element including the UDH "header" (two bytes)
     *
     * @return Data
     */
    public byte[] getData()
    {
        byte[] allData = new byte[myUdhIeiData.length + 2];

        allData[0] = (byte) (myUdhIei & 0xff);
        allData[1] = (byte) (myUdhIeiData.length & 0xff);
        System.arraycopy(myUdhIeiData, 0, allData, 2, myUdhIeiData.length);

        return allData;
    }

    /**
     * Writes the UDH element including UDH "header" to the given stream
     *
     * @param os Stream to write to
     * @throws IOException
     */
    public void writeTo(OutputStream os)
        throws IOException
    {
        os.write(myUdhIei);
        os.write(myUdhIeiData.length);
        os.write(myUdhIeiData);
    }
}
