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
package org.marre.wap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.marre.mime.MimeHeader;
import org.marre.util.StringUtil;
import org.marre.wap.util.WspUtil;

/**
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class MmsHeaderEncoder
{
    private MmsHeaderEncoder()
    {
    }
    
    public static void writeHeader(OutputStream theOs, MimeHeader theHeader)
        throws IOException
    {
        String name = theHeader.getName();
        int wellKnownHeaderId = StringUtil.findString(MmsConstants.HEADER_NAMES, name.toLowerCase());

        switch (wellKnownHeaderId)
        {
            case MmsConstants.HEADER_ID_BCC:
				MmsHeaderEncoder.writeHeaderBcc(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_CC:
				MmsHeaderEncoder.writeHeaderCc(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_X_MMS_CONTENT_LOCATION:
                MmsHeaderEncoder.writeHeaderContentLocation(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_CONTENT_TYPE:
                MmsHeaderEncoder.writeHeaderContentType(theOs, theHeader);
                break;
            case MmsConstants.HEADER_ID_DATE:
                break;
            case MmsConstants.HEADER_ID_X_MMS_DELIVERY_REPORT:
                break;
            case MmsConstants.HEADER_ID_X_MMS_DELIVERY_TIME:
                break;
            case MmsConstants.HEADER_ID_X_MMS_EXPIRY:
                break;
            case MmsConstants.HEADER_ID_FROM:
				MmsHeaderEncoder.writeHeaderFrom(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_X_MMS_MESSAGE_CLASS:
				MmsHeaderEncoder.writeHeaderXMmsMessageClass(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_MESSAGE_ID:
                break;
            case MmsConstants.HEADER_ID_X_MMS_MESSAGE_TYPE:
            	MmsHeaderEncoder.writeHeaderXMmsMessageType(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_X_MMS_MMS_VERSION:
				MmsHeaderEncoder.writeHeaderXMmsMmsVersion(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_X_MMS_MESSAGE_SIZE:
                break;
            case MmsConstants.HEADER_ID_X_MMS_PRIORITY:
				MmsHeaderEncoder.writeHeaderXMmsPriority(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_X_MMS_READ_REPLY:
				MmsHeaderEncoder.writeHeaderXMmsReadReply(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_X_MMS_REPORT_ALLOWED:
                break;
            case MmsConstants.HEADER_ID_X_MMS_RESPONSE_STATUS:
                break;
            case MmsConstants.HEADER_ID_X_MMS_RESPONSE_TEXT:
                break;
            case MmsConstants.HEADER_ID_X_MMS_SENDER_VISIBILITY:
				MmsHeaderEncoder.writeHeaderXMmsSenderVisibility(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_X_MMS_STATUS:
				MmsHeaderEncoder.writeHeaderXMmsStatus(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_SUBJECT:
            	MmsHeaderEncoder.writeHeaderSubject(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_TO:
				MmsHeaderEncoder.writeHeaderTo(theOs, theHeader.getValue());
                break;
            case MmsConstants.HEADER_ID_X_MMS_TRANSACTION_ID:
            	MmsHeaderEncoder.writeHeaderXMmsTransactionId(theOs, theHeader.getValue());
                break;

        default:
            // Application-header
            WspUtil.writeTokenText(theOs, theHeader.getName());
            WspUtil.writeTextString(theOs, theHeader.getValue());
            break;
        }
    }

	/**
     * Writes a wsp encoded content-location header as specified in
     * WAP-230-WSP-20010705-a.pdf.
     *
     * @param theOs
     * @param theContentLocation
     * @throws IOException
     */
    public static void writeHeaderContentLocation(OutputStream theOs, String theContentLocation)
        throws IOException
    {
        // TODO: Verify
        WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_X_MMS_CONTENT_LOCATION);
        WspUtil.writeTextString(theOs, theContentLocation);
    }

    public static void writeHeaderContentType(OutputStream theOs, String theContentType)
        throws IOException
    {
        WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_CONTENT_TYPE);
        WspUtil.writeContentType(theOs, theContentType);
    }

    public static void writeHeaderContentType(OutputStream theOs, MimeHeader theContentType)
        throws IOException
    {
        WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_CONTENT_TYPE);
        WspUtil.writeContentType(theOs, theContentType);
    }
    
	public static void writeEncodedStringValue(OutputStream baos, String theFrom)
		throws IOException 
	{
		// TODO: Charset...
		WspUtil.writeTextString(baos, theFrom);
	}
		
	public static void writeHeaderXMmsMessageType(OutputStream theOs, String theMessageType)
		throws IOException 
	{
		int messageTypeId = StringUtil.findString(MmsConstants.X_MMS_MESSAGE_TYPE_NAMES, theMessageType.toLowerCase());
		
		WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_X_MMS_MESSAGE_TYPE);
		WspUtil.writeShortInteger(theOs, messageTypeId);
	}
	
	public static void writeHeaderXMmsTransactionId(OutputStream theOs, String theTransactionId) 
		throws IOException 
	{
		WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_X_MMS_TRANSACTION_ID);
		WspUtil.writeTextString(theOs, theTransactionId);
	}
	
	public static void writeHeaderXMmsMmsVersion(OutputStream theOs, String theVersion) 
		throws IOException 
	{
		WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_X_MMS_MMS_VERSION);
		// TODO: Add correct version encoding
		WspUtil.writeShortInteger(theOs, 0x10);
	}
	
	public static void writeHeaderFrom(OutputStream theOs, String theFrom) 
		throws IOException 
	{
		WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_FROM);
		
		if (theFrom == null)
		{
			WspUtil.writeValueLength(theOs, 1);
			WspUtil.writeShortInteger(theOs, MmsConstants.FROM_INSERT_ADDRESS);
		}
		else
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			// Write data to baos
			WspUtil.writeShortInteger(baos, MmsConstants.FROM_ADDRESS_PRESENT);
			MmsHeaderEncoder.writeEncodedStringValue(baos, theFrom);
			baos.close();
			
			WspUtil.writeValueLength(theOs, baos.size());
			theOs.write(baos.toByteArray());
		}		
	}

	public static void writeHeaderSubject(OutputStream theOs, String theSubject)
		throws IOException 
	{
		WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_SUBJECT);
		MmsHeaderEncoder.writeEncodedStringValue(theOs, theSubject);
	}
	
	public static void writeHeaderTo(OutputStream theOs, String theRecipient)
		throws IOException 
	{
		WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_TO);
		MmsHeaderEncoder.writeEncodedStringValue(theOs, theRecipient);
	}
	
	public static void writeHeaderCc(OutputStream theOs, String theRecipient)
		throws IOException 
	{
		WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_CC);
		MmsHeaderEncoder.writeEncodedStringValue(theOs, theRecipient);
	}
	
	public static void writeHeaderBcc(OutputStream theOs, String theRecipient)
		throws IOException 
	{
		WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_BCC);
		MmsHeaderEncoder.writeEncodedStringValue(theOs, theRecipient);
	}
	
	public static void writeHeaderXMmsReadReply(OutputStream theOs, String theReadReply)
		throws IOException 
	{
		int readReplyId = StringUtil.findString(MmsConstants.X_MMS_READ_REPLY_NAMES, theReadReply.toLowerCase());
		
		if (readReplyId != -1)
		{
			WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_X_MMS_READ_REPLY);
			WspUtil.writeShortInteger(theOs, readReplyId);
		}
	}
	
	public static void writeHeaderXMmsPriority(OutputStream theOs, String thePriority)
		throws IOException 
	{
		int priorityId = StringUtil.findString(MmsConstants.X_MMS_PRIORITY_NAMES, thePriority.toLowerCase());

		if (priorityId != -1)
		{		
			WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_X_MMS_PRIORITY);
			WspUtil.writeShortInteger(theOs, priorityId);
		}
	}	

	public static void writeHeaderXMmsStatus(OutputStream theOs, String theStatus)
		throws IOException 
	{
		int statusId = StringUtil.findString(MmsConstants.X_MMS_STATUS_NAMES, theStatus.toLowerCase());

		if (statusId != -1)
		{		
			WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_X_MMS_STATUS);
			WspUtil.writeShortInteger(theOs, statusId);
		}
	}	
		
	public static void writeHeaderXMmsMessageClass(OutputStream theOs, String theMessageClass)
		throws IOException 
	{
		int messageClassId = StringUtil.findString(MmsConstants.X_MMS_MESSAGE_CLASS_NAMES, theMessageClass.toLowerCase());

		if (messageClassId != -1)
		{		
			WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_X_MMS_MESSAGE_CLASS);
			WspUtil.writeShortInteger(theOs, messageClassId);
		}
	}	
		
	public static void writeHeaderXMmsSenderVisibility(OutputStream theOs, String theVisibility)
		throws IOException 
	{
		int visibilityId = StringUtil.findString(MmsConstants.X_MMS_SENDER_VISIBILITY_NAMES, theVisibility.toLowerCase());

		if (visibilityId != -1)
		{		
			WspUtil.writeShortInteger(theOs, MmsConstants.HEADER_ID_X_MMS_SENDER_VISIBILITY);
			WspUtil.writeShortInteger(theOs, visibilityId);
		}
	}	
}
