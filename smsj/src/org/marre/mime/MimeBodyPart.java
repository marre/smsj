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
package org.marre.mime;

import java.util.*;

/**
 * Represents a MIME body part.
 * 
 * A body part can contain headers and a body.
 * 
 * @author Markus Eriksson
 * @version $Id$
 */
public class MimeBodyPart
{
    protected byte[] myBody;
    protected MimeContentType myContentType;

    protected List myHeaders;

    /**
     * Creates a new empty MimeBodyPart.
     */
    public MimeBodyPart()
    {
        myHeaders = new LinkedList();
    }

    /**
     * Creates a new empty MimeBodyPart.
     */
    public MimeBodyPart(byte[] body, MimeContentType contentType)
    {
        this();
        setContent(body, contentType);
    }
    
    /**
     * Creates a new empty MimeBodyPart.
     */
    public MimeBodyPart(byte[] body, String contentType)
    {
        this();
        setContent(body, contentType);
    }
    
    /**
     * Adds a mime header to this body part.
     * 
     * @param theHeader The header to add
     */
    public void addHeader(MimeHeader theHeader)
    {
        myHeaders.add(theHeader);
    }

    /**
     * Adds a eader to this body part.
     * 
     * @param theHeaderName The name of the header
     * @param theHeaderValue The value
     */
    public void addHeader(String theHeaderName, String theHeaderValue)
    {
        MimeHeader header = getHeader(theHeaderName);
        if (header != null)
        {
            myHeaders.remove(header);
            header = null;
        }
        addHeader(new MimeHeader(theHeaderName, theHeaderValue));
    }

    /**
     * Retrieves a header with the given index.
     * 
     * @param theIndex Index of header to retrieve
     * @return The header, or null if not found
     */
    public MimeHeader getHeader(int theIndex)
    {
        return (MimeHeader) myHeaders.get(theIndex);
    }

    /**
     * Retrieves a header with the given name.
     * 
     * @param headerName The name of the header to find
     * @return The header, or null if not found
     */
    public MimeHeader getHeader(String headerName)
    {
        Iterator iter = myHeaders.iterator();
        
        while (iter.hasNext())
        {
            MimeHeader header = (MimeHeader) iter.next();
            
            if (header.getName().equalsIgnoreCase(headerName))
            {
                return header;
            }
        }
        
        return null;
    }
    
    /**
     * Returns the number of headers.
     * 
     * @return The number of headers
     */
    public int getHeaderCount()
    {
        return myHeaders.size();
    }

    /**
     * Sets the main content of this body part.
     * 
     * @param theContent The main content
     * @param theContentType The content-type of the content
     */
    public void setContent(byte[] theContent, String theContentType)
    {
        myBody = new byte[theContent.length];
        System.arraycopy(theContent, 0, myBody, 0, theContent.length);
        myContentType = new MimeContentType(theContentType);
    }

    /**
     * Sets the main content of this body part.
     * 
     * @param theContent The main content
     * @param theContentType The content type
     */
    public void setContent(byte[] theContent, MimeContentType theContentType)
    {
        myBody = new byte[theContent.length];
        System.arraycopy(theContent, 0, myBody, 0, theContent.length);
        myContentType = theContentType;
    }
    
    /**
     * Sets the "Content-Id" header.
     * 
     * @param theContentId The content-id
     */
    public void setContentId(String theContentId)
    {
        addHeader("Content-Id", theContentId);
    }

    /**
     * Sets the "Content-Location" header.
     * 
     * @param theContentLocation The content-location
     */
    public void setContentLocation(String theContentLocation)
    {
        addHeader("Content-Location", theContentLocation);
    }
    
    /**
     * Returns the content of this body part.
     * 
     * @return The content
     */
    public byte[] getBody()
    {
        byte[] bodyCopy = null;
        
        if (myBody != null)
        {
            bodyCopy = new byte[myBody.length];
            System.arraycopy(myBody, 0, bodyCopy, 0, myBody.length);
        }
        
        return bodyCopy;
    }

    /**
     * Returns the size of the body in this body part.
     * 
     * @return The size of the body
     */
    public int getBodySize()
    {
        return myBody.length;
    }

    /**
     * Returns the content type.
     * 
     * @return The content type
     */
    public MimeContentType getContentType()
    {
        return myContentType;
    }
}
