/*
    SMS Library for the Java platform
    Copyright (C) 2002  Markus Eriksson
    Portions Copyright (C) 2002 Boris von Loesch

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