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
    private static final String ALLOWED_DIGITS = "+0123456789*#ab";

    private int myTon = SmsConstants.TON_INTERNATIONAL;
    private int myNpi = SmsConstants.NPI_ISDN_TELEPHONE;

    private String myAddress = null;

    /**
     * Creates an SmsAddress object.
     * <p>
     * This constructor tries to be intelligent by choosing the correct
     * NPI and TON from the given address.
     *
     * @param theAddress The address
     * @throws SmsException Thrown if the address is invalid
     */
    public SmsAddress(String theAddress)
        throws SmsException
    {
        int npi = SmsConstants.NPI_ISDN_TELEPHONE;
        int ton = SmsConstants.TON_INTERNATIONAL;

        for(int i=0; i < theAddress.length(); i++)
        {
            char ch = theAddress.charAt(i);
            if (ALLOWED_DIGITS.indexOf(ch) == -1)
            {
                ton = SmsConstants.TON_ALPHANUMERIC;
                npi = SmsConstants.NPI_UNKNOWN;
                break;
            }
        }

        init(theAddress, ton, npi);
    }

    /**
     * Creates an SmsAddress object.
     * <p>
     * Max address length is <br>
     * 20 digits (excluding any initial '+') or<br>
     * 11 alphanumeric chars (if TON == TON_ALPHANUMERIC).
     * <p>
     * If you choose TON_ALPHANUMERIC then the NPI will be set to NPI_UNKNOWN.
     *
     * @param theAddress The address
     * @param theTon The type of number
     * @param theNpi The number plan indication
     * @throws SmsException Thrown if the address is invalid
     */
    public SmsAddress(String theAddress, int theTon, int theNpi)
        throws SmsException
    {
        init(theAddress, theTon, theNpi);
    }

    private void init(String theAddress, int theTon, int theNpi)
        throws SmsException
    {
        int addressLength;

        if (theAddress == null)
        {
            throw new SmsException("Empty address.");
        }

        myTon = theTon;
        myAddress = theAddress.trim();
        addressLength = myAddress.length();

        if (addressLength == 0)
        {
            throw new SmsException("Empty address.");
        }

        if (theTon == SmsConstants.TON_ALPHANUMERIC)
        {
            myNpi = SmsConstants.NPI_UNKNOWN;

            if (theAddress.length() > 11)
            {
                throw new SmsException("Alphanumeric address can be at most 11 chars.");
            }
        }
        else
        {
            myNpi = theNpi;

            if (myAddress.charAt(0) == '+')
            {
                addressLength -= 1;
            }

            if (addressLength > 20)
            {
                throw new SmsException("Too long address, Max allowed is 20 digits (excluding any inital '+').");
            }

            for(int i=0; i < theAddress.length(); i++)
            {
                char ch = theAddress.charAt(i);
                if (ALLOWED_DIGITS.indexOf(ch) == -1)
                {
                    throw new SmsException("Invalid digit in address. '" + ch + "'.");
                }
            }
        }
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
