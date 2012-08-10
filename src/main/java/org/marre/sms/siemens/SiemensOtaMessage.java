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

import org.marre.sms.*;
import org.marre.util.StringUtil;

/**
 * 
 * @author Raphael Borg Ellul Vincenti
 * @version $Id$
 */
public class SiemensOtaMessage implements SmsMessage
{
    protected int version_ = 1;
    protected String name_ = "";
    protected String type_ = "";
    protected long referenceId_;
    protected int dataSize_;
    protected int numberOfPackets_;
    protected byte[] content_;

    /**
     * Creates an SMS containing a Bitmap or a Ringtone
     * 
     * @param content
     *            The content in bytes
     */
    public SiemensOtaMessage(String name, String type, byte[] content)
    {
        name_ = name;
        type_ = type;
        content_ = content;

        init();
    }

    /**
     * Creates an SMS containing a Bitmap or a Ringtone
     * 
     * @param content
     *            The content in bytes
     */
    public SiemensOtaMessage(int version, String name, String type, byte[] content)
    {
        this.version_ = version;
        this.name_ = name;
        this.type_ = type;
        this.content_ = content;

        init();

    }

    /**
     * 
     *  
     */
    private void init()
    {
        this.referenceId_ = (new Random()).nextLong();
        this.dataSize_ = 140 - 22 - name_.length() - type_.length();
        this.numberOfPackets_ = (int) Math.ceil((float) content_.length / (float) dataSize_);
    }

    /**
     * Returns the userdata header for an SMS
     * 
     * @return
     */
    private byte[] getHeader(int actPacketNumber)
    {
        byte[] header = new byte[22 + name_.length() + type_.length()];

        // Identifier: "//SEO"
        header[0] = (byte) 0x2f;
        header[1] = (byte) 0x2f;
        header[2] = (byte) 0x53;
        header[3] = (byte) 0x45;
        header[4] = (byte) 0x4f;

        // Version
        header[5] = (byte) (version_ & 0xff);

        // DataSize
        header[6] = (byte) (dataSize_ & 0xff);
        header[7] = (byte) ((dataSize_ >> 8) & 0xff);

        // ReferenceId
        header[8] = (byte) (referenceId_ & 0xff);
        header[9] = (byte) ((referenceId_ >> 8) & 0xff);
        header[10] = (byte) ((referenceId_ >> 16) & 0xff);
        header[11] = (byte) ((referenceId_ >> 24) & 0xff);

        // ActPacketNumber
        header[12] = (byte) (actPacketNumber & 0xff);
        header[13] = (byte) ((actPacketNumber >> 8) & 0xff);

        // NumberOfPackets
        header[14] = (byte) (numberOfPackets_ & 0xff);
        header[15] = (byte) ((numberOfPackets_ >> 8) & 0xff);

        // ObjectSize
        header[16] = (byte) (content_.length & 0xff);
        header[17] = (byte) ((content_.length >> 8) & 0xff);
        header[18] = (byte) ((content_.length >> 16) & 0xff);
        header[19] = (byte) ((content_.length >> 24) & 0xff);

        // ObjectType (Pascal String)
        header[20] = (byte) (type_.length() & 0xff);
        header[21] = (byte) (type_.charAt(0) & 0xff);
        header[22] = (byte) (type_.charAt(1) & 0xff);
        header[23] = (byte) (type_.charAt(2) & 0xff);

        // ObjectName (Pascal String)
        header[24] = (byte) (name_.length() & 0xff);
        for (int i = 0; i < name_.length(); i++)
        {
            header[25 + i] = (byte) ((name_.charAt(i)) & 0xff);
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
        SmsPdu[] smsPdus = new SmsPdu[numberOfPackets_];
        SmsDcs dcs = SmsDcs.getGeneralDataCodingDcs(SmsAlphabet.LATIN1, SmsMsgClass.CLASS_1);
        for (int i = 0; i < numberOfPackets_; i++)
        {

            byte[] pdu = new byte[140];
            byte[] header = getHeader(i + 1);

            System.arraycopy(header, 0, pdu, 0, header.length);
            int offset = dataSize_ * i;

            // Handle last SMS
            if (i == (numberOfPackets_ - 1))
            {
                dataSize_ = content_.length % dataSize_;
            }

            System.arraycopy(content_, offset, pdu, header.length, dataSize_);

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
