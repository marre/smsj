/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
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
 * ***** END LICENSE BLOCK ***** */
package org.marre.sms.nokia;

import java.io.UnsupportedEncodingException;
import org.apache.commons.logging.*;

/**
 * Nokia Downloadable Profile
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaDownloadableProfile extends NokiaMultipartMessage
{
    private static Log myLog = LogFactory.getLog(NokiaDownloadableProfile.class);

    private String myProfileName = null;
    private byte[] myScreenSaver = null;
    private byte[] myRingingTone = null;

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

    public void setRingingTone(byte[] theRingingTone)
    {
        myRingingTone = theRingingTone;
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

    private void addRingingTone(byte[] theRingingTone)
    {
        addMultipart(NokiaPart.ITEM_RINGTONE, theRingingTone);
    }

    protected void buildPdus()
    {
        // Reset message
        clear();

        // Create message
        if (myProfileName != null)
        {
            addProfileName(myProfileName);
        }

        if (myScreenSaver != null)
        {
            addScreenSaver(myScreenSaver);
        }

        if (myRingingTone != null)
        {
            addRingingTone(myRingingTone);
        }

        super.buildPdus();
    }
}

