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

public class SmsPdu
{
    protected SmsDcs myDcs = null;

    /** Length of myUd, can be in octets or septets */
    protected int myUdLength = 0;

    protected byte myUd[] = null;
    protected byte myUdh[] = null;

    public SmsPdu()
    {
    }

    public SmsPdu(byte[] theUdh, byte[] theUd, int theUdLength, SmsDcs theDcs)
    {
        setUserDataHeader(theUdh);
        setUserData(theUd, theUdLength, theDcs);
    }

    public void setUserDataHeader(byte[] theUdh)
    {
        myUdh = theUdh;
    }

    public byte[] getUserDataHeader()
    {
        return myUdh;
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
