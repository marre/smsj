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
package org.marre.sms.siemens;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import org.marre.sms.SmsDcs;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsPdu;
import org.marre.util.StringUtil;

/**
 * 
 * @author Raphael Borg Ellul Vincenti
 * @version $Id$
 */
public class SiemensOtaMessage implements SmsMessage
{
    protected int myVersion = 1;
    protected String myName = "";
    protected String myType = "";
    protected long myReferenceId;
    protected int myDataSize;
    protected int myNumberOfPackets;
    protected byte[] myContent;

    /**
     * Creates an SMS containing a Bitmap or a Ringtone
     * 
     * @param theMessage
     *            The content in bytes
     */
    public SiemensOtaMessage(String name, String type, byte[] content)
    {
        myName = name;
        myType = type;
        myContent = content;

        init();
    }

    /**
     * Creates an SMS containing a Bitmap or a Ringtone
     * 
     * @param theMessage
     *            The content in bytes
     */
    public SiemensOtaMessage(int version, String name, String type, byte[] content)
    {
        this.myVersion = version;
        this.myName = name;
        this.myType = type;
        this.myContent = content;

        init();

    }

    /**
     * 
     *  
     */
    private void init()
    {
        this.myReferenceId = (new Random()).nextLong();
        this.myDataSize = 140 - 22 - myName.length() - myType.length();
        this.myNumberOfPackets = (int) Math.ceil((float) myContent.length / (float) myDataSize);
    }

    /**
     * Returns the userdata header for an SMS
     * 
     * @return
     */
    private byte[] getHeader(int actPacketNumber)
    {
        byte[] header = new byte[22 + myName.length() + myType.length()];

        // Identifier: "//SEO"
        header[0] = (byte) 0x2f;
        header[1] = (byte) 0x2f;
        header[2] = (byte) 0x53;
        header[3] = (byte) 0x45;
        header[4] = (byte) 0x4f;

        // Version
        header[5] = (byte) (myVersion & 0xff);

        // DataSize
        header[6] = (byte) (myDataSize & 0xff);
        header[7] = (byte) ((myDataSize >> 8) & 0xff);

        // ReferenceId
        header[8] = (byte) (myReferenceId & 0xff);
        header[9] = (byte) ((myReferenceId >> 8) & 0xff);
        header[10] = (byte) ((myReferenceId >> 16) & 0xff);
        header[11] = (byte) ((myReferenceId >> 24) & 0xff);

        // ActPacketNumber
        header[12] = (byte) (actPacketNumber & 0xff);
        header[13] = (byte) ((actPacketNumber >> 8) & 0xff);

        // NumberOfPackets
        header[14] = (byte) (myNumberOfPackets & 0xff);
        header[15] = (byte) ((myNumberOfPackets >> 8) & 0xff);

        // ObjectSize
        header[16] = (byte) (myContent.length & 0xff);
        header[17] = (byte) ((myContent.length >> 8) & 0xff);
        header[18] = (byte) ((myContent.length >> 16) & 0xff);
        header[19] = (byte) ((myContent.length >> 24) & 0xff);

        // ObjectType (Pascal String)
        header[20] = (byte) (myType.length() & 0xff);
        header[21] = (byte) (myType.charAt(0) & 0xff);
        header[22] = (byte) (myType.charAt(1) & 0xff);
        header[23] = (byte) (myType.charAt(2) & 0xff);

        // ObjectName (Pascal String)
        header[24] = (byte) (myName.length() & 0xff);
        for (int i = 0; i < myName.length(); i++)
        {
            header[25 + i] = (byte) ((myName.charAt(i)) & 0xff);
        }

        return header;
    }

    /**
     * Converts this message into SmsPdu:s
     * <p>
     * If the message is too long to fit in one SmsPdu the message is divided
     * into many SmsPdu:s with a different packet number
     * 
     * @return Returns the message as SmsPdu:s
     */
    public SmsPdu[] getPdus()
    {
        SmsPdu[] smsPdus = new SmsPdu[myNumberOfPackets];
        SmsDcs dcs = SmsDcs.getGeneralDataCodingDcs(SmsDcs.ALPHABET_8BIT, SmsDcs.MSG_CLASS_1);
        for (int i = 0; i < myNumberOfPackets; i++)
        {

            byte[] pdu = new byte[140];
            byte[] header = getHeader(i + 1);

            System.arraycopy(header, 0, pdu, 0, header.length);
            int offset = myDataSize * i;

            // Handle last SMS
            if (i == (myNumberOfPackets - 1))
            {
                myDataSize = myContent.length % myDataSize;
            }

            System.arraycopy(myContent, offset, pdu, header.length, myDataSize);

            SmsPdu smsPdu = new SmsPdu(null, pdu, pdu.length, dcs);
            smsPdus[i] = smsPdu;
        }

        return smsPdus;
    }

    /*
     *  
     */
    public static byte[] loadFromFile(File file) throws IOException
    {
        int n;
        int nread = 0;
        int len = (int) file.length();

        FileInputStream fin = new FileInputStream(file);

        byte[] content = new byte[len];

        while (nread < len)
        {

            if ((n = fin.read(content, nread, len - nread)) == -1)
            {
                throw new IOException("Error loading Compound from file");
            }
            nread += n;
        }

        return content;
    }

    public static void main(String[] args)
    {
        String name = "Operator.bmp";
        String type = "bmp";

        byte[] data = null;

        try
        {
            data = loadFromFile(new File(args[0]));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        SiemensOtaMessage message = new SiemensOtaMessage(22518750, name, type, data);

        SmsPdu[] pdus = message.getPdus();

        System.out.println("Data   : " + StringUtil.bytesToHexString(data));

        for (int i = 0; i < pdus.length; i++)
        {
            System.out.println("UD     : " + StringUtil.bytesToHexString(pdus[i].getUserData().getData()));
        }
    }

}
