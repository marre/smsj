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

import java.util.Properties;

import org.marre.sms.SmsPdu;
import org.marre.sms.SmsAddress;
import org.marre.sms.SmsException;

/**
 * Interface for a SMS transport
 * <p>
 * This interface is used for all smsj transports. A good example of
 * implementation of this interface is the GsmTransport or the ClickatellTransport.
 * <p>
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public interface SmsTransport
{
    /**
     * Initializes the transport
     * <p>
     * Initializes the transport with the given properties.
     *
     * @param theProps Properties
     * @throws SmsException
     */
    public void init(Properties theProps) throws SmsException;

    /**
     * Connects to the SMSC (or phone, or service, or...)
     *
     * @throws SmsException
     */
    public void connect() throws SmsException;

    /**
     * Sends a single SMS to the given destination
     *
     * @param thePdu The PDU to send
     * @param theDestination Destination address
     * @param theSender Sender
     * @throws SmsException
     */
    public void send(SmsPdu thePdu, SmsAddress theDestination, SmsAddress theSender) throws SmsException;

    /**
     * Sends an array of SMS to the reciever.
     * <p>
     * Often implemented as a for loop calling the other send() method
     *
     * @param thePdus The array of SmsPdus to send
     * @param theDestination Destination address
     * @param theSender Sender
     * @throws SmsException
     */
    public void send(SmsPdu thePdus[], SmsAddress theDestination, SmsAddress theSender) throws SmsException;

    /**
     * Sends a "ping" to the SMSC (or phone, or service, or...)
     * <p>
     * Used to keep the connection alive
     *
     * @throws SmsException
     */
    public void ping() throws SmsException;

    /**
     * Disconnects from the SMSC (or phone, or service, or...)
     *
     * @throws SmsException
     */
    public void disconnect() throws SmsException;
}
