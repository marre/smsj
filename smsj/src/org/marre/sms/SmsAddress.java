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

import java.io.ByteArrayOutputStream;

public class SmsAddress
{
    private int myTon = SmsConstants.TON_INTERNATIONAL;
    private int myNpi = SmsConstants.NPI_ISDN_TELEPHONE;

    private String myAddress = null;

    public SmsAddress(String theAddress, int theTon, int theNpi)
    {
        myAddress = theAddress;
        myTon = theTon;
        myNpi = theNpi;
    }

    public SmsAddress(String theAddress)
    {
        this(theAddress, SmsConstants.TON_INTERNATIONAL, SmsConstants.NPI_ISDN_TELEPHONE);
    }

    public String getAddress()
    {
        return myAddress;
    }

    public int getTypeOfNumber()
    {
        return myTon;
    }

    public int getNumberingPlanIdentification()
    {
        return myNpi;
    }
}
