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
package org.marre.mms;


import org.marre.mime.*;
import org.marre.mms.util.MmsUtil;

public class MmsMessage 
{

	public static final boolean MULTIPART_RELATED = true;
	public static final boolean MULTIPART_MIXED = false;
	
	boolean type;
	
	String from;
	String to;
	String cc;
	String subject;
	String messageClass;
	String priority;
	String deliveryReport;
	
	MimeMultipart messageBody;
	
	public MmsMessage()
	{
		messageClass = "personal";
	}
		
	public void setMessage(MimeMultipart messageBody)
	{
		if (messageBody == null)
		{
			return;
		}
		
		if (messageBody instanceof MimeMultipartRelated)
		{
			type = MULTIPART_RELATED;			
		}
				
		this.messageBody = messageBody;
	}
	
	public MimeMultipart getMessage()
	{
		return messageBody;
	}
	
	public boolean isMultipartRelated()
	{
		if (type == MULTIPART_RELATED)
		{
			return true;
		}
		
		return false;
	}

	public boolean isMultipartMixed()
	{
		if (type == MULTIPART_MIXED)
		{
			return true;
		}
		
		return false;
	}
				
	public static void main(String[] args){
		
		MimeBodyPart smilBodyPart = MmsUtil.createSmilBodyPart("test_smil".getBytes(), "0000");
		MimeBodyPart textBodyPart = MmsUtil.createTextBodyPart("test_text".getBytes(), "test.txt");
		
		MimeMultipartRelated mmr = new MimeMultipartRelated();
		mmr.addBodyPart(smilBodyPart);
		mmr.addBodyPart(textBodyPart);
		
		MmsMessage mms = new MmsMessage();
		mms.setMessage(mmr);		
	}

	public String getCc() 
	{
		return cc;
	}

	public String getDeliveryReport() 
	{
		return deliveryReport;
	}

	public String getFrom() 
	{
		return from;
	}

	public MimeMultipart getMessageBody() 
	{
		return messageBody;
	}

	public String getMessageClass() 
	{
		return messageClass;
	}

	public String getPriority() 
	{
		return priority;
	}

	public String getSubject() 
	{
		return subject;
	}

	public String getTo() 
	{
		return to;
	}

	public void setCc(String string) 
	{
		cc = string;
	}

	public void setDeliveryReport(String string) 
	{
		deliveryReport = string;
	}

	public void setFrom(String string) 
	{
		from = string;
	}

	public void setMessageBody(MimeMultipart multipart) 
	{
		messageBody = multipart;
	}

	public void setMessageClass(String string) 
	{
		messageClass = string;
	}

	public void setPriority(String string) 
	{
		priority = string;
	}

	public void setSubject(String string) 
	{
		subject = string;
	}

	public void setTo(String string) 
	{
		to = string;
	}
}
