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
 * <b>Note!</b> This is not a complete list.
 *
 * @author Markus Eriksson
 * @version 1.0
 */
public final class GsmOperators
{
    // SE - TELIA
    public static final int   SE_TELIA_MCC = 240;
    public static final int   SE_TELIA_MNC = 1;
    public static final int[] SE_TELIA_MCC_MNC = {SE_TELIA_MCC, SE_TELIA_MNC};

    // SE - COMVIQ
    public static final int   SE_COMVIQ_MCC = 240;
    public static final int   SE_COMVIQ_MNC = 7;
    public static final int[] SE_COMVIQ_MCC_MNC = {SE_COMVIQ_MCC, SE_COMVIQ_MNC};

    // SE - EUROPOLITAN
    public static final int   SE_EUROPOLITAN_MCC = 240;
    public static final int   SE_EUROPOLITAN_MNC = 8;
    public static final int[] SE_EUROPOLITAN_MCC_MNC = {SE_EUROPOLITAN_MCC, SE_EUROPOLITAN_MNC};

    // FI - RADIOLINJA
    public static final int   FI_RADIOLINJA_MCC = 244;
    public static final int   FI_RADIOLINJA_MNC = 5;
    public static final int[] FI_RADIOLINJA_MCC_MNC = {FI_RADIOLINJA_MCC, FI_RADIOLINJA_MNC};

    // FI - SONERA
    public static final int   FI_SONERA_MCC = 244;
    public static final int   FI_SONERA_MNC = 91;
    public static final int[] FI_SONERA_MCC_MNC = {FI_SONERA_MCC, FI_SONERA_MNC};

	// GER - T-Mobil D1
    public static final int   GER_TMOBIL_MCC = 262;
    public static final int   GER_TMOBIL_MNC = 01;
    public static final int[] GER_TMOBIL_MCC_MNC = {GER_TMOBIL_MCC, GER_TMOBIL_MNC};

	// GER - Vodafone D2
    public static final int   GER_VODAFONED2_MCC = 262;
    public static final int   GER_VODAFONED2_MNC = 01;
    public static final int[] GER_VODAFONED2_MCC_MNC = {GER_VODAFONED2_MCC, GER_VODAFONED2_MNC};




    /**
     * Returns the Mcc and Mnc number for the given number, if the area code is in the property file
     * @param mccmncProp  The property file
     * @param intReceiverNr the receivers number in international format (e.g. +49172..)
     * @return
     */
    public static int[] getMCC_MNC(Properties mccmncProp, String intReceiverNr)
    {
        //Check the first seven or less charakters
        int i=7;
        String operator="";
        int[] ret = {0,0};
        //Find operator
        while ((i>2)&&(operator.equals("")))
        {
            operator=mccmncProp.getProperty(intReceiverNr.substring(0, i), "");
            i--;
        }
        //Find MCC and MNC
        if (!operator.equals(""))
        {
            String mccmnc=mccmncProp.getProperty(operator, "0,0");
            ret[0]=Integer.valueOf(mccmnc.substring(0,mccmnc.indexOf(","))).intValue();
            ret[1]=Integer.valueOf(mccmnc.substring(mccmnc.indexOf(",")+1)).intValue();
        }
        return ret;
    }

    /**
     * Returns the Mcc and Mnc number for the given number, if the area code is in the property file
     * @param propFileName  The property filename
     * @param intReceiverNr the receivers number in international format (e.g. +49172..)
     * @return
     */
    public static int[] getMCC_MNC(String propFileName, String intReceiverNr)
      throws IOException
    {
        Properties prop = new Properties();
        prop.load(new FileInputStream(propFileName));
        return getMCC_MNC(prop, intReceiverNr);
    }

    /**
     * Returns the Mcc and Mnc number for the given number, if the area code is in the property file.
     * The property file is loaded as resource mccmnc.prop
     * @param intReceiverNr the receivers number in international format (e.g. +49172..)
     * @return
     */
    public static int[] getMCC_MNC(String intReceiverNr)
      throws IOException
    {
        Properties prop = new Properties();
        ClassLoader cl = GsmOperators.class.getClassLoader();
        InputStream inst = cl.getResourceAsStream("mccmnc.prop");
        if (inst==null)
        {
            throw new FileNotFoundException ("Could not load resource mccmnc.prop");
        }
        prop.load(inst);
        return getMCC_MNC(prop, intReceiverNr);
    }
}