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

import java.io.*;

/**
 * Represents an SMS pdu
 * <p>
 * A SMS pdu consists of a user data header (UDH) and the actual content often
 * called user data (UD).
 * 
 * @author Markus Eriksson
 * @version $Id$
 */

public class SmsPdu
{
    protected SmsUdhElement[] myUdhElements;
    protected SmsUserData myUd;

    /**
     * Creates an empty SMS pdu object
     */
    public SmsPdu()
    {
    }

    /**
     * Creates an SMS pdu object.
     * 
     * @param theUdhIeis
     *            The UDH elements
     * @param theUd
     *            The content
     * @param theUdLength
     *            The length of the content. Can be in octets or septets
     *            depending on the DCS
     */
    public SmsPdu(SmsUdhElement[] theUdhIeis, byte[] theUd, int theUdLength, SmsDcs theDataCodingScheme)
    {
        setUserDataHeaders(theUdhIeis);
        setUserData(theUd, theUdLength, theDataCodingScheme);
    }

    /**
     * Creates an SMS pdu object.
     * 
     * @param theUdhIeis
     *            The UDH elements
     * @param theUd
     *            The content
     */
    public SmsPdu(SmsUdhElement[] theUdhIeis, SmsUserData theUd)
    {
        setUserDataHeaders(theUdhIeis);
        setUserData(theUd);
    }
    
    /**
     * Sets the UDH field
     * 
     * @param theUdhElements
     *            The UDH elements
     */
    public void setUserDataHeaders(SmsUdhElement[] theUdhElements)
    {
        if (theUdhElements != null)
        {
            myUdhElements = new SmsUdhElement[theUdhElements.length];

            System.arraycopy(theUdhElements, 0, myUdhElements, 0, theUdhElements.length);
        }
        else
        {
            myUdhElements = null;
        }
    }

    /**
     * Returns the user data headers
     * 
     * @return A byte array representing the UDH fields or null if there aren't
     *         any UDH
     */
    public byte[] getUserDataHeaders()
    {
        if (myUdhElements == null)
        {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);

        baos.write((byte) SmsUdhUtil.getTotalSize(myUdhElements));

        try
        {
            for (int i = 0; i < myUdhElements.length; i++)
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
     *            The content
     * @param theUdLength
     *            The length, can be in septets or octets depending on the DCS
     * @param theDataCodingScheme
     *            The data coding scheme
     */
    public void setUserData(byte[] theUd, int theUdLength, SmsDcs theDataCodingScheme)
    {
        myUd = new SmsUserData(theUd, theUdLength, theDataCodingScheme);
    }

    /**
     * Sets the user data field of the message.
     * 
     * @param theUd
     *            The content
     */
    public void setUserData(SmsUserData theUd)
    {
        myUd = theUd;
    }
    
    /**
     * Returns the user data part of the message.
     * 
     * @return UD field
     */
    public SmsUserData getUserData()
    {
        return myUd;
    }
    
    /**
     * Returns the dcs.
     * 
     * @return dcs
     */
    public SmsDcs getDcs()
    {
        return myUd.getDcs();
    }
}
