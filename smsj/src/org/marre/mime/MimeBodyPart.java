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
package org.marre.mime;

import java.io.*;
import java.util.*;

public class MimeBodyPart
{
    protected byte[] myContent = null;
    protected MimeMultipart myMultipart = null;

    protected Map myHeaders;

    public MimeBodyPart()
    {
        myHeaders = new HashMap();
    }

    // HEADERS
    public void setHeader(String theHeaderName, String theHeaderValue)
    {
        myHeaders.put(theHeaderName, theHeaderValue);
    }

    public void delHeader(String theHeaderName)
    {
        myHeaders.remove(theHeaderName);
    }

    public String getHeader(String theHeaderName)
    {
        return (String) myHeaders.get(theHeaderName);
    }

    public List getHeaders()
    {
        return null;
    }

    // CONTENT
    public void setContent(byte[] theContent, String theContentType)
    {
        myContent = new byte[theContent.length];
        System.arraycopy(theContent, 0, myContent, 0, theContent.length);
    }

    public void setContent(MimeMultipart theMultipart)
    {
        myMultipart = theMultipart;
    }

    public byte[] getContent()
    {
        return myContent;
    }

    public int getSize()
    {
        return 0;
    }
}

