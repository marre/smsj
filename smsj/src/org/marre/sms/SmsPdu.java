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

public class SmsPdu
{
    protected SmsDcs myDcs = null;

    /** Length of myUd, can be in octets or septets */
    protected int myUdLength = 0;

    protected SmsUdhIei[] myUdhElements = null;
    protected byte myUd[] = null;

    public SmsPdu()
    {
    }

    public SmsPdu(SmsUdhIei[] theUdhIeis, byte[] theUd, int theUdLength, SmsDcs theDcs)
    {
        setUserDataHeaders(theUdhIeis);
        setUserData(theUd, theUdLength, theDcs);
    }

    public void setUserDataHeaders(SmsUdhIei[] theUdhElements)
    {
        myUdhElements = theUdhElements;
    }

    public byte[] getUserDataHeaders()
    {
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

    public void setUserData(byte[] theUd, int theUdLength, SmsDcs theDcs)
    {
        myUd = theUd;
        myUdLength = theUdLength;
        myDcs = theDcs;
    }

    public byte[] getUserData()
    {
        return myUd;
    }

    public int getUserDataLength()
    {
        return myUdLength;
    }

    public SmsDcs getDataCodingScheme()
    {
        return myDcs;
    }
}
