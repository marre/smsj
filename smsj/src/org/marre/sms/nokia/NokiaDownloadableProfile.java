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
package org.marre.sms.nokia;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.*;

import org.marre.sms.SmsPdu;

/**
 * Nokia Downloadable Profile
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaDownloadableProfile extends NokiaMultipartMessage
{
    private static Log myLog = LogFactory.getLog(NokiaPictureMessage.class);

    private String myProfileName = null;
    private byte[] myScreenSaver = null;
    
    public NokiaDownloadableProfile()
    {
    }

    public NokiaDownloadableProfile(String theProfileName)
    {
        setProfileName(theProfileName);
    }

    public void setScreenSaver(byte[] theBitmap)
    {
        myScreenSaver = theBitmap;
    }

    public void setScreenSaver(OtaBitmap theBitmap)
    {
        myScreenSaver = theBitmap.getBytes();
    }

    public void setProfileName(String theProfileName)
    {
        myProfileName = theProfileName;
    }

    private void addScreenSaver(byte[] theBitmap)
    {
        addMultipart(NokiaPart.ITEM_SCREEN_SAVER, theBitmap);
    }

    private void addProfileName(String theProfileName)
    {
        try
        {
            addMultipart(NokiaPart.ITEM_PROFILE_NAME, theProfileName.getBytes("UTF-16BE"));
        }
        catch (UnsupportedEncodingException ex)
        {
            myLog.fatal("Shouldn't happen, 'UTF-16BE' is in the standard", ex);
        }
    }

    public SmsPdu[] getPdus()
    {
        if (myProfileName != null)
        {
            addProfileName(myProfileName);
        }

        if (myScreenSaver != null)
        {
            addScreenSaver(myScreenSaver);
        }

        return super.getPdus();
    }
}

