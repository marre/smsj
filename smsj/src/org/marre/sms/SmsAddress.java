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
package org.marre.sms;

import java.io.ByteArrayOutputStream;

/**
 * Represents an phonenumber in SMSj.
 * <p>
 * The address can be a phonenumber (+463482422) or alphanumeric
 * ('SmsService'). Not all networks and transports supports alphanumeric
 * sending id.
 * <p>
 * Max address length is <br>
 * - 20 digits (excluding any initial '+') or<br>
 * - 11 alphanumeric chars (if TON == TON_ALPHANUMERIC).
 * <p>
 * Look in SmsConstants for definitions of TON and NPI.
 *
 * @author Markus Eriksson
 * @version $Id$
 */
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

            // Trim '+' from address
            if (myAddress.charAt(0) == '+')
            {
                myAddress = myAddress.substring(1);
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

    /**
     * Returns the address
     *
     * @return The address
     */
    public String getAddress()
    {
        return myAddress;
    }

    /**
     * Returns the TON field
     * <p>
     * See SmsConstants for definitions of different TON:s
     *
     * @return The TON
     */
    public int getTypeOfNumber()
    {
        return myTon;
    }

    /**
     * Returns the NPI field
     * <p>
     * See SmsConstants for definitions of different TON:s
     *
     * @return The NPI
     */
    public int getNumberingPlanIdentification()
    {
        return myNpi;
    }
}

