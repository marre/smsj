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
import java.awt.*;
import java.awt.image.*;

import org.marre.sms.*;
import org.marre.sms.transport.*;
import org.marre.sms.util.*;
import org.marre.sms.nokia.*;

public class TestSms
{
    public static void testSeptets()
        throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String testString = "                ";

        SmsPduUtil.writeSeptets(baos, testString);
        baos.close();

        String copyString = SmsPduUtil.readSeptets(new ByteArrayInputStream(baos.toByteArray()), testString.length());

        System.out.println("Original : " + testString);
        System.out.println("Septets  : " + SmsPduUtil.bytesToHexString(baos.toByteArray()));
        System.out.println("Copy     : " + copyString);
    }

    public static void testGsmTransport()
        throws Exception
    {
        Properties props = new Properties();
        SmsTransport transport = SmsTransportManager.getTransport("org.marre.sms.transport.gsm.GsmTransport", props);

        // FIXME: The second message is truncated!
        SmsMessage msg = new SmsTextMessage("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678", SmsConstants.ALPHABET_GSM);
//        SmsMessage msg = new SmsTextMessage("1234567890123456789012", SmsConstants.TEXT_ALPHABET_UCS2);

        SmsAddress sender = new SmsAddress("+1234567890");
        SmsAddress reciever = new SmsAddress("+9876543210");

        transport.send(msg.getPdus(), reciever, sender);
    }

    public static void testAddress()
        throws Exception
    {
        new SmsAddress("123123123");
    }

    public static void testArrayCopy()
    {
        String a = "88888888888888888888888888888888888888888888";
        byte[] src = SmsPduUtil.hexStringToBytes(a);
        byte[] dest = new byte[a.length()/2 + 1];

        SmsPduUtil.arrayCopy(src, 0, dest, 0, 8, src.length*8);

        System.out.println(a);
        System.out.println(SmsPduUtil.bytesToHexString(dest));
    }

    public static void testOtaBitmap()
        throws Exception
    {
        BufferedImage img = new BufferedImage(72, 14, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = img.createGraphics();

//        graphics.drawLine(3,3,10,10);
//        graphics.drawOval(1, 1, 12, 12);
//        graphics.draw(new Rectangle(0, 0, 10, 12));
        graphics.drawString("Anna", 0, 12);
//        graphics.drawRect(0, 0, 10, 10);

        graphics.dispose();
        graphics = null;

        OtaBitmap bitmap = new OtaBitmap(img);

        FileOutputStream fos = new FileOutputStream("c:\\temp\\fil.otb");
        fos.write(bitmap.getBytes());
        fos.close();

        System.out.println(SmsPduUtil.bytesToHexString(bitmap.getBytes()));
    }

    public static void main(String[] args)
        throws Exception
    {
        testOtaBitmap();
//        testSeptets();
//        testGsmTransport();
//        testAddress();
//        testArrayCopy();
    }
}
