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
package org.marre.sms.transport;

import java.util.*;
import java.lang.reflect.*;

import org.marre.sms.*;

/**
 * smsj Transport Manager
 * <p>
 * Contains methods to manage the different transports. Currently it only
 * contains a method to dynamically instantiate and initialize an SmsTransport
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public class SmsTransportManager
{
    private SmsTransportManager()
    {
    }

    /**
     * Dynamically instantiates and initializes an SmsTransport
     *
     * @param theClassname Full classname of the transport.
     * Ex. "org.marre.sms.transport.gsm.GsmTransport"
     * @param theProps Properties to initialize the transport with.
     * @return Returns an initialized SmsTransport object
     * @throws SmsException If we failed to load the requested transport
     */
    public static final SmsTransport getTransport(String theClassname, Properties theProps)
        throws SmsException
    {
        SmsTransport transport = null;

        try
        {
            // Try to find and instanciate the transport
            Class clazz = Class.forName(theClassname);
            Object obj = clazz.newInstance();

            // No promblems so far. Initialize the transport
            transport = (SmsTransport)obj;
            transport.init(theProps);

            return (SmsTransport) obj;
        }
        catch (ClassCastException ex)
        {
            throw new SmsException(theClassname + "is not an SmsTransport.");
        }
        catch (ClassNotFoundException ex)
        {
            throw new SmsException("Couldn't find " + theClassname + ". Please check your classpath.");
        }
        catch (Exception ex)
        {
            throw new SmsException("Couldn't load " + theClassname + ".");
        }
    }
}
