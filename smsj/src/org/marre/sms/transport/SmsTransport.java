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
package org.marre.sms.transport;

import java.util.Properties;

import org.marre.sms.SmsMessage;
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
     * Sends an SmsMessage to the given destination
     *
     * @param theMessage The Message to send
     * @param theDestination Destination address
     * @param theSender Sender
     * @throws SmsException
     */
    public void send(SmsMessage theMessage, SmsAddress theDestination, SmsAddress theSender) throws SmsException;

    /**
     * Disconnects from the SMSC (or phone, or service, or...)
     *
     * @throws SmsException
     */
    public void disconnect() throws SmsException;
}
