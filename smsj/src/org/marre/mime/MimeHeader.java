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
import java.io.Serializable;

public class MimeHeader implements Serializable
{
    protected String myHeaderName;
    protected String myHeaderValue;

    protected List myParams;

    protected MimeHeader()
    {
    }

    public MimeHeader(String theName, String theValue)
    {
        myHeaderName = theName;
        myHeaderValue = theValue;
        myParams = new LinkedList();
    }

    public void setValue(String theValue)
    {
        myHeaderValue = theValue;
    }

    public String getName()
    {
        return myHeaderName;
    }

    public String getValue()
    {
        return myHeaderValue;
    }

    public void setParam(String theName, String theValue)
    {
        // Remove parameter if it already exists...
        removeParam(theName);
        
        // Add new...
        myParams.add(new MimeHeaderParam(theName, theValue));
    }

    public MimeHeaderParam getParam(String theName)
    {
        Iterator iter = myParams.iterator();
        while (iter.hasNext())
        {
            MimeHeaderParam param = (MimeHeaderParam) iter.next();
            if (param.getName().equalsIgnoreCase(theName))
            {
                return param;
            }
        }
        
        // Not found
        return null;
    }
    
    public void removeParam(String theName)
    {
        MimeHeaderParam param = getParam(theName);
        
        if (param != null)
        {
            myParams.remove(param);
        }
    }

    public List getAllParams()
    {
        return myParams;
    }

    public int getParamCount()
    {
        return myParams.size();
    }

    public MimeHeaderParam getParam(int theIndex)
    {
        return (MimeHeaderParam) myParams.get(theIndex);
    }
}
