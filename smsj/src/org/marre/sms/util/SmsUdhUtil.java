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
package org.marre.sms.util;

import org.marre.sms.SmsUdhElement;
import org.marre.sms.SmsConstants;

public class SmsUdhUtil
{
    private SmsUdhUtil()
    {
    }

    public static final SmsUdhElement getConcatUdh(int theRefNr, int theTotSms, int theSeqNr)
    {
        byte[] udh = new byte[3];

        udh[0] = (byte) (theRefNr  & 0xff);
        udh[1] = (byte) (theTotSms & 0xff);
        udh[2] = (byte) (theSeqNr  & 0xff);

        return new SmsUdhElement(SmsConstants.UDH_IEI_CONCATENATED_8BIT, udh);
    }
}
