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

import org.marre.sms.SmsUserData;
/**
 * Nokia Downloadable Profile
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class NokiaDownloadableProfile extends NokiaMultipartMessage
{
    private String myProfileName;
    private byte[] myScreenSaver;
    private byte[] myRingingTone;

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

    private void addProfileName(String theProfileName)
    {
    }

    public SmsUserData getUserData()
    {
        // Reset message
        clear();

        // Create message
        if (myProfileName != null)
        {
            try
            {
                addMultipart(NokiaPart.ITEM_PROFILE_NAME, myProfileName.getBytes("UTF-16BE"));
            }
            catch (UnsupportedEncodingException ex)
            {
                throw new RuntimeException(ex.getMessage());
            }
        }

        if (myScreenSaver != null)
        {
            addMultipart(NokiaPart.ITEM_SCREEN_SAVER, myScreenSaver);
        }

        if (myRingingTone != null)
        {
            addMultipart(NokiaPart.ITEM_RINGTONE, myRingingTone);
        }

        return super.getUserData();
    }
}
