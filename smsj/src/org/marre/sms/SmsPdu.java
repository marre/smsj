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
import java.util.*;

/**
 * Represents an SMS pdu
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public class SmsPdu
{
    /** Length of myUd, can be in octets or septets */
    protected int myUdLength = 0;

    protected SmsUdhElement[] myUdhElements = null;
    protected byte myUd[] = null;

    /**
     * Creates an empty SMS pdu object
     */
    public SmsPdu()
    {
    }

    /**
     * Creates an SMS pdu object
     *
     * @param theUdhIeis
     * @param theUd
     * @param theUdLength
     * @param theDcs
     */
    public SmsPdu(SmsUdhElement[] theUdhIeis, byte[] theUd, int theUdLength)
    {
        setUserDataHeaders(theUdhIeis);
        setUserData(theUd, theUdLength);
    }

    /**
     * Sets the UDH field
     *
     * @param theUdhElements
     */
    public void setUserDataHeaders(SmsUdhElement[] theUdhElements)
    {
        myUdhElements = theUdhElements;
    }

    /**
     * Returns the user data headers
     *
     * @return A byte array representing the UDH fields or null if there aren't
     * any UDH
     */
    public byte[] getUserDataHeaders()
    {
        if ( myUdhElements == null)
        {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);

        try
        {
            for(int i=0; i < myUdhElements.length; i++)
            {
                myUdhElements[i].writeTo(baos);
            }
        }
        catch (IOException ioe)
        {
            // Shouldn't happen.
            throw new RuntimeException("Failed to write to ByteArrayOutputStream");
        }

        return baos.toByteArray();
    }

    /**
     * Sets the user data field of the message.
     *
     * @param theUd
     * @param theUdLength
     * @param theDcs
     */
    public void setUserData(byte[] theUd, int theUdLength)
    {
        myUd = theUd;
        myUdLength = theUdLength;
    }

    /**
     * Returns the user data part of the message.
     *
     * @return UD field
     */
    public byte[] getUserData()
    {
        return myUd;
    }

    /**
     * Returns the length of the user data field
     * <p>
     * This can be in characters or byte depending on the message (DCS).
     * If message is 7 bit coded the length is given in septets.
     * If 8bit or UCS2 the length is in octets.
     *
     * @return The length
     */
    public int getUserDataLength()
    {
        return myUdLength;
    }
}
