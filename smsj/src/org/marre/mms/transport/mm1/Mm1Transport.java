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
package org.marre.mms.transport.mm1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.marre.mime.MimeBodyPart;
import org.marre.mms.MmsException;
import org.marre.mms.MmsHeaders;
import org.marre.mms.transport.MmsTransport;
import org.marre.util.IOUtil;
import org.marre.util.StringUtil;

/**
 * Sends mms using the mm1 protocol. 
 * 
 * @author Markus Eriksson
 * @version $Id$
 */
public class Mm1Transport implements MmsTransport
{
    private static Log logger = LogFactory.getLog(Mm1Transport.class);

    /** 
     * Content type for a mms message. 
     */
    public static final String CONTENT_TYPE_WAP_MMS_MESSAGE = "application/vnd.wap.mms-message";

    /**
     * URL for the proxy gateway
     */
    private String myMmsProxyGatewayAddress;

    /**
     * @see org.marre.mms.transport.MmsTransport#init(java.util.Properties)
     */
    public void init(Properties theProps) throws MmsException
    {
        myMmsProxyGatewayAddress = theProps.getProperty("smsj.mm1.proxygateway");

        if (myMmsProxyGatewayAddress == null)
        {
            throw new MmsException("smsj.mm1.proxygateway not set");
        }
    }

    /**
     * The mm1 protocol is connection less so this method is not used.
     * @see org.marre.mms.transport.MmsTransport#connect()
     */
    public void connect() throws IOException
    {
        // Empty
    }

    /**
     * Sends the mms.
     * @throws MmsException if fails to send the message. 
     * 
     * @see org.marre.mms.transport.MmsTransport#send(org.marre.mime.MimeBodyPart,
     *      org.marre.mms.MmsHeaders)
     *      
     * @param theMessage message to send
     * @param theHeaders headers for the message
     */
    public void send(MimeBodyPart theMessage, MmsHeaders theHeaders) throws MmsException, IOException
    {
        // POST data to the MMSC
        
        // First create the data so we can find out how large it is
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Mm1Encoder.writeMessageToStream(baos, theMessage, theHeaders);
        baos.close();
        
        if (logger.isDebugEnabled())
        {
            String str = StringUtil.bytesToHexString(baos.toByteArray());
            logger.debug("request [" + str + "]");
        }
        
        URL url = new URL(myMmsProxyGatewayAddress);
        HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
        
        urlConn.addRequestProperty("Content-Length", "" + baos.size());
        urlConn.addRequestProperty("Content-Type", CONTENT_TYPE_WAP_MMS_MESSAGE);

        urlConn.setDoOutput(true);
        urlConn.setDoInput(true);
        urlConn.setAllowUserInteraction(false);
        
        // Send the data
        OutputStream out = urlConn.getOutputStream();
        baos.writeTo(out);
        out.flush();
        out.close();
        
        baos.reset();
        baos = new ByteArrayOutputStream();
        
        // Read the response
        InputStream response = urlConn.getInputStream();
        
        int responsecode = urlConn.getResponseCode();
        logger.debug("HTTP response code : " + responsecode);
        
        IOUtil.copy(response, baos);
        baos.close();
        
        if (logger.isDebugEnabled())
        {
            String str = StringUtil.bytesToHexString(baos.toByteArray());
            logger.debug("response [" + str + "]");
        }
        // TODO: Parse the response
    }

    /**
     * The mm1 protocol is connection less so this method is not used.
     * @see org.marre.mms.transport.MmsTransport#disconnect()
     */
    public void disconnect() throws IOException
    {
        // Empty
    }
}
