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
 *   Boris von Loesch
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
package org.marre.sms.util;

import java.util.*;
import java.io.*;
/**
 * Contains MCC and MNC definitions for various GSM operators.
 * <p>
 * TODO: Test this!
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public final class GsmOperators
{
    private static Properties myMccMncProp = new Properties();

    /**
     * Returns the Mcc and Mnc number for the given operator
     * 
     * @param mccmncProp The property file
     * @param country the countrycode for the country (e.g. "se", "fi")
     * @param operator the receivers number in international format (e.g. +49172..)
     * @return
     */
    public static int[] getMCC_MNC(Properties mccmncProp, String country, String operator)
    {
        int[] ret = {0,0};
        
        String mccStr = mccmncProp.getProperty(country + "." + operator + ".mcc");
        String mncStr = mccmncProp.getProperty(country + "." + operator + ".mnc");

        if ((mccStr != null) && (mncStr != null))
        {
            ret[0] = Integer.valueOf(mccStr).intValue();
            ret[1] = Integer.valueOf(mncStr).intValue();
        }

        return ret;
    }

    /**
     * Returns the Mcc and Mnc number for the given operator.
     * The property file is loaded as resource mccmnc.prop
     * 
     * @param country the receivers number in international format (e.g. +49172..)
     * @param operator the receivers number in international format (e.g. +49172..)
     * @return
     */
    public static int[] getMCC_MNC(String country, String operator)
    {
        return getMCC_MNC(myMccMncProp, country, operator);
    }

    /**
     * Builds a default mcc mnc list that is used if it failed to
     * load the "mccmnc.prop" file.
     */    
    private static void defaultMccMnc()
    {
        myMccMncProp.setProperty("se.telia.mcc",       "240");        
        myMccMncProp.setProperty("se.telia.mnc",       "1");        
        myMccMncProp.setProperty("se.comviq.mcc",      "240");        
        myMccMncProp.setProperty("se.comviq.mnc",      "7");        
        myMccMncProp.setProperty("se.europolitan.mcc", "240");        
        myMccMncProp.setProperty("se.europolitan.mnc", "8");   
        myMccMncProp.setProperty("se.vodafone.mcc",    "240");        
        myMccMncProp.setProperty("se.vodafone.mnc",    "8");   
        
        myMccMncProp.setProperty("fi.radiolinja.mcc",  "244");        
        myMccMncProp.setProperty("fi.radiolinja.mnc",  "5");        
        myMccMncProp.setProperty("fi.sonera.mcc",      "244");        
        myMccMncProp.setProperty("fi.sonera.mnc",      "91");
        
        myMccMncProp.setProperty("de.tmobil.mcc",      "262");        
        myMccMncProp.setProperty("de.tmobil.mnc",      "1");        
        myMccMncProp.setProperty("de.vodafone.mcc",    "262");        
        myMccMncProp.setProperty("de.vodafone.mnc",    "1");
                        
        myMccMncProp.setProperty("it.tim.mcc",         "222");        
        myMccMncProp.setProperty("it.tim.mnc",         "1");
        myMccMncProp.setProperty("it.vodafone.mcc",    "222");        
        myMccMncProp.setProperty("it.vodafone.mnc",    "10");
        myMccMncProp.setProperty("it.wind.mcc",        "222");        
        myMccMncProp.setProperty("it.wind.mnc",        "88");
        myMccMncProp.setProperty("it.blu.mcc",         "222");        
        myMccMncProp.setProperty("it.blu.mnc",         "98");
    }

    private static void readMccMncFromResource(String resourceName)
    {
        ClassLoader cl = GsmOperators.class.getClassLoader();
        InputStream in = cl.getResourceAsStream(resourceName);
        
        if (in != null)
        {
            try
            {
                myMccMncProp.load(in);
            }
            catch (IOException ex)
            {
                // TODO: Log this

                // Load default values
                defaultMccMnc();
            }
        }
    }
    
    static 
    {
        readMccMncFromResource("mccmnc.prop");
    }
}
