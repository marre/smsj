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
package org.marre.sms.transport.clickatell;

import org.marre.sms.transport.SmsTransport;
import java.util.Properties;
import org.marre.sms.SmsPdu;
import org.marre.sms.SmsAddress;
import org.marre.sms.SmsException;

/**
 * An SmsTransport that sends the SMS with clickatell over HTTP.
 * <p>
 * It is developed to use the "Clickatell HTTP API v. 1.63"
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public class ClickatellTransport implements SmsTransport
{
    public void init(Properties theProps) throws SmsException
    {
        throw new java.lang.UnsupportedOperationException("Method init() not yet implemented.");
    }

    /**
     * Command:
     * http://api.clickatell.com/http/auth?api_id=xxxx&user=xxxx&password=xxxx
     *
     * Response:
     * OK: session id
     * or
     * ERR: Error number
     *
     * @throws SmsException
     */
    public void connect() throws SmsException
    {
        // http://api.clickatell.com/http/auth?api_id=xxxx&user=xxxx&password=xxxx
        throw new java.lang.UnsupportedOperationException("Method connect() not yet implemented.");
    }

    /**
     * Command:
     * http://api.clickatell.com/http/sendmsg?session_id=xxxx&
     *
     * Response Single Message:
     * ID: apiMsgId
     * Response Multiple Messages:
     * ID: apiMsgId To: xxxxxx
     * ID: apiMsgId To: xxxxxx
     * or
     * ERR: Error number
     *
     * @param thePdu
     * @param theDestination
     * @param theSender
     * @throws SmsException
     */
    public void send(SmsPdu thePdu, SmsAddress theDestination, SmsAddress theSender) throws SmsException
    {
        /**@todo: Implement this org.marre.sms.transport.SmsTransport method*/
        throw new java.lang.UnsupportedOperationException("Method send() not yet implemented.");
    }

    public void send(SmsPdu[] thePdus, SmsAddress theDestination, SmsAddress theSender) throws SmsException
    {
        /**@todo: Implement this org.marre.sms.transport.SmsTransport method*/
        throw new java.lang.UnsupportedOperationException("Method send() not yet implemented.");
    }

    /**
     * Command:
     * http://api.clickatell.com/http/ping?session_id=xxx
     * Response:
     * OK:
     * or
     * ERR: Error number
     *
     * @throws SmsException
     */
    public void ping() throws SmsException
    {
        /**@todo: Implement this org.marre.sms.transport.SmsTransport method*/
        throw new java.lang.UnsupportedOperationException("Method ping() not yet implemented.");
    }

    public void disconnect() throws SmsException
    {
        /**@todo: Implement this org.marre.sms.transport.SmsTransport method*/
        throw new java.lang.UnsupportedOperationException("Method disconnect() not yet implemented.");
    }
}