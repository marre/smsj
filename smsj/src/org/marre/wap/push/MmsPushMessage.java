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
package org.marre.wap.push;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.marre.sms.wap.WapPushMessage;
import org.marre.wap.MmsConstants;
import org.marre.wap.MmsHeaderEncoder;

/**
 * MMS WAP Push message
 * <p>
 *
 * @author Lincoln Spiteri
 * @version $Id$
 */

public class MmsPushMessage extends WapPushMessage {

	String uri;
	String from;
	String to;
	String subject;
	String	messageClass;
	int transactionId;
	int size;
	int expiry;
	
	
	public MmsPushMessage(String uri) {
		
		super();
		this.uri = uri;
		subject = "";
		from = "";
		to = "";
		transactionId = 0;
		size = 0;
		expiry = 1000;
		messageClass = "personal";
	}

	public void createMmsPush()
	{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			try 
			{
				// X-Mms-Message-Type (m-notification-ind)
				MmsHeaderEncoder.writeHeaderXMmsMessageType(baos, "m-notification-ind");
				MmsHeaderEncoder.writeHeaderXMmsTransactionId(baos, String.valueOf(transactionId));
				MmsHeaderEncoder.writeHeaderXMmsMmsVersion(baos, "1.0");
				
				if (from.length() != 0)
				{
					MmsHeaderEncoder.writeHeaderFrom(baos, from);
				}
				
				if (subject.length() != 0)
				{
					MmsHeaderEncoder.writeHeaderSubject(baos, subject);
				}

				MmsHeaderEncoder.writeHeaderXMmsMessageClass(baos, messageClass);
				MmsHeaderEncoder.writeHeaderXMmsMessageSize(baos, size);
				MmsHeaderEncoder.writeHeaderXMmsExpiry(baos, expiry, MmsConstants.EXPIRY_TIME_RELATIVE);
				MmsHeaderEncoder.writeHeaderContentLocation(baos, uri);

				baos.close();
			}
			catch (IOException e)
			{
				// Should not happen	
			}
		
			createMessage(baos.toByteArray(), "application/vnd.wap.mms-message", "x-wap-application:mms.ua", null);
	}
	
	public String getMessageClass() 
	{
			return messageClass;
	}

	public int getSize() 
	{
			return size;
	}

	public String getSubject() 
	{
			return subject;
	}

	public String getTo() 
	{
			return to;
	}

	public String getUri() 
	{
			return uri;
	}

	public void setMessageClass(String s) 
	{
			messageClass = s;
	}

	public void setSize(int i) 
	{
			size = i;
	}

	public void setSubject(String string) 
	{
			subject = string;
	}

	public void setTo(String string) 
	{
			to = string;
	}

	public void setUri(String string) 
	{
			uri = string;
	}

	public int getExpiry() 
	{
			return expiry;
	}

	public String getFrom() 
	{
			return from;
	}

	public int getTransactionId() 
	{
			return transactionId;
	}

	public void setExpiry(int i) 
	{
			expiry = i;
	}

	public void setFrom(String string) 
	{
			from = string;
	}

	public void setTransactionId(int i) 
	{
			transactionId = i;
	}

}
