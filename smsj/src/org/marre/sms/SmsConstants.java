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
package org.marre.sms;

public class SmsConstants
{
    public static final byte TON_UNKNOWN = 0x00;
    public static final byte TON_INTERNATIONAL = 0x01;
    public static final byte TON_NATIONAL = 0x02;
    public static final byte TON_NETWORK_SPECIFIC = 0x03;
    public static final byte TON_SUBSCRIBER_NUMBER = 0x04;
    public static final byte TON_ALPHANUMERIC = 0x05;
    public static final byte TON_ABBREVIATED = 0x06;

    public static final byte NPI_UNKNOWN = 0x00;
    public static final byte NPI_ISDN_TELEPHONE = 0x01;
    public static final byte NPI_DATA = 0x03;
    public static final byte NPI_TELEX = 0x04;
    public static final byte NPI_NATIONAL = 0x08;
    public static final byte NPI_PRIVATE = 0x09;
    public static final byte NPI_ERMES = 0x10;

    public static final byte MSG_CLASS_0 = 0x00;
    public static final byte MSG_CLASS_1 = 0x01;
    public static final byte MSG_CLASS_2 = 0x02;
    public static final byte MSG_CLASS_3 = 0x03;
    public static final byte MSG_CLASS_UNKNOWN = 0x04;

    private SmsConstants()
    {
    }
}
