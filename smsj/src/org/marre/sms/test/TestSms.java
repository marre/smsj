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
package org.marre.sms.test;

import java.io.*;
import java.util.*;

import org.marre.sms.*;
import org.marre.sms.transport.*;
import org.marre.sms.util.*;

public class TestSms
{
    public static void testSeptets()
        throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String testString = "Testing 123456 ÅÄÖ";

        SmsPduUtil.writeSeptets(baos, testString);
        baos.close();

        String copyString = SmsPduUtil.readSeptets(new ByteArrayInputStream(baos.toByteArray()), testString.length());

        System.out.println("Original : " + testString);
        System.out.println("Copy     : " + copyString);
    }

    public static void testGsmTransport()
        throws Exception
    {
        Properties props = new Properties();
        SmsTransport transport = SmsTransportManager.getTransport("org.marre.sms.transport.gsm.GsmTransport", props);

        SmsMessage msg = new SmsTextMessage("Can this really work? I'm impressed.", SmsTextMessage.TEXT_ALPHABET_ISO_LATIN_1);

        SmsAddress sender = new SmsAddress("+1234567890");
        SmsAddress reciever = new SmsAddress("+9876543210");

        transport.send(msg.getPdus(), reciever, sender);
    }

    public static void main(String[] args)
        throws Exception
    {
        testSeptets();
        testGsmTransport();
    }
}
