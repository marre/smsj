/*
 * Created on 25-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.marre.sms.ems;

import java.util.Random;
import java.util.StringTokenizer;

import org.marre.sms.SmsConcatMessage;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsPdu;
import org.marre.sms.SmsPduUtil;
import org.marre.sms.SmsUdhElement;
import org.marre.sms.SmsUdhUtil;
import org.marre.util.StringUtil;

/**
 * @author Lincoln Spiteri
 */
public class EmsIMelody implements SmsMessage
{
    public static final int DATA_LENGTH = 60;

    protected byte[] myData;
    protected boolean myConcat;
    protected String myText;

    /**
     *  
     */
    public EmsIMelody()
    {
    }

    /**
     * 
     * @param theRingTone
     */
    public EmsIMelody(byte[] theRingTone)
    {
        //setContent(theRingTone);
        myData = theRingTone;

        //if (data.length > 127){
        //  concat = true;
        //}
    }

    /**
     * 
     * @param theRingTone
     */
    public void setContent(byte[] theRingTone)
    {
        // Must add \n due to mBlox bug
//        setContent(new SmsUdhElement[]{SmsUdhUtil.getEmsUserDefinedSoundUdh(theRingTone, 0)}, "\n".getBytes(), 1);
    }

    /**
     * 
     * @param data
     * @return
     */
    private byte[] getImelodyBody(byte[] data)
    {
        String melody = "";

        boolean isData = false;
        StringTokenizer tokenizer = new StringTokenizer(new String(data), "\n");

        while (tokenizer.hasMoreTokens())
        {
            String temp = tokenizer.nextToken();

            if (temp.startsWith("END:"))
            {
                isData = false;
            }

            if (temp.startsWith("MELODY:"))
            {
                isData = true;
            }

            if (isData)
            {
                temp = temp.substring(7); // remove the MELODY: part from the
                // string
                melody = melody.concat(temp.trim());
            }
        }

        return melody.getBytes();
    }

    /**
     *  
     */
    public SmsPdu[] getPdus()
    {
        SmsPdu[] smsPdus = null;

        //if (concat){
        smsPdus = createEmsPdus();
        //}
        //else {
        //return super.getPdus();
        //}

        return smsPdus;
    }

    /**
     * 
     * @param theMaxBytes
     * @return
     */
    private SmsPdu[] createEmsPdus()
    {
        //Calculate the number of parts required
        //Headers: 45 + 13 + 9 = 67
        //Available tone octets: 61 (Full) 124 (short)
        //Length of full headers for imelody.
        byte[] rtBody = this.getImelodyBody(myData);
        int rtBodyLen = rtBody.length;

        int nSms;

        if (myConcat)
        {

            nSms = rtBodyLen / DATA_LENGTH;

            if ((rtBodyLen % DATA_LENGTH) > 0)
            {
                nSms++;
            }
        }
        else
        {
            nSms = 1;
        }

        SmsPdu[] smsPdus = new SmsPdu[nSms];

        Random myRnd = new Random();
        int refno = myRnd.nextInt(256);

        // Calculate number of UDHI
        SmsUdhElement[] pduUdhElements = null;

        // Create pdus
        for (int i = 0; i < nSms; i++)
        {
            int offset = i * DATA_LENGTH;

            //Get the iMelody for the current segment
            byte[] imelodyFrag = getImelodyFragment(rtBody, offset);

            // Create UDH header
            if (i == 0 && myConcat)
            {
                pduUdhElements = new SmsUdhElement[3];
                pduUdhElements[0] = SmsUdhUtil.get8BitConcatUdh(refno, nSms, i + 1);
                pduUdhElements[1] = SmsUdhUtil.getEmsUserPromptIndicatorUdh(nSms);
                pduUdhElements[2] = SmsUdhUtil.getEmsUserDefinedSoundUdh(imelodyFrag, 0);
            }
            else
            {
                pduUdhElements = new SmsUdhElement[2];
                pduUdhElements[0] = SmsUdhUtil.get8BitConcatUdh(refno, nSms, i + 1);
                pduUdhElements[1] = SmsUdhUtil.getEmsUserDefinedSoundUdh(imelodyFrag, 0);
            }

            //pduUd = new byte[udBytes];
            //SmsPduUtil.arrayCopy(myUd, udOffset, pduUd, 0, udBytes);
            smsPdus[i] = new SmsPdu(pduUdhElements, "\n".getBytes(), 1, SmsConstants.DCS_DEFAULT_8BIT);
        }

        return smsPdus;
    }

    /**
     * 
     * @param args
     */
    private byte[] getImelodyFragment(byte[] data, int offset)
    {

        int fragSize = 0;

        if ((data.length - offset) < DATA_LENGTH)
        {
            fragSize = data.length - offset;
        }
        else
        {
            fragSize = DATA_LENGTH;
        }

        StringBuffer melody = new StringBuffer();
        byte[] fragmentData = new byte[fragSize];

        SmsPduUtil.arrayCopy(data, offset, fragmentData, 0, fragSize);

        melody.append("BEGIN:IMELODY\r\n");
        melody.append("VERSION:1.2\r\n");
        melody.append("FORMAT:CLASS1.0\r\n");
        melody.append("MELODY:");
        melody.append(new String(fragmentData));
        melody.append("\r\n");
        melody.append("END:IMELODY\r\n");

        return melody.toString().getBytes();
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        System.out.println("Testing EmsImelody");

        String data = "BEGIN:IMELODY\r\n"
                    + "VERSION:1.2\r\n"
                    + "FORMAT:CLASS1.0\r\n"
                    + "MELODY:*3f3f3f3#c1#d3#d3#d3c1r3f3f3f3#c3#f3#f3#f3f3*4#c3#c3#c3*3#a1*3f3f3f3#c1#d3#d3#d3c1r3f3f3f3#c3#f3#f3#f3f3*4#c3#c3#c3*3#a1\r\n"
                    + "END:IMELODY\r\n";

        //String data =
        // "424547494E3A494D454C4F44590D0A56455253494F4E3A312E300D0A464F524D41543A434C415353312E300D0A4D454C4F44593A563133723072322A3267332A3367332A326733562D6733562B2A3367332A3267335631352361335631332A332361336333562B2A346333562D2A3267335631352A3367330D0A205631332A32673367332A3367330D0A454E443A494D454C4F44590D0A";
        //byte bytes[] = StringUtil.hexStringToBytes(data);

        EmsIMelody imelody = new EmsIMelody(data.getBytes());

        SmsPdu[] pdus = imelody.getPdus();

        for (int i = 0; i < pdus.length; i++)
        {
            byte[] udh = pdus[i].getUserDataHeaders();
//            byte[] ud = pdus[i].getUserData();
            System.out.println("UDH: " + StringUtil.bytesToHexString(udh));
            //System.out.println("UD: " + StringUtil.bytesToHexString(ud));
        }

        //System.out.println(StringUtil.bytesToHexString(b));
    }

    /**
     * @return
     */
    public String getText()
    {
        return myText;
    }

    /**
     * @param string
     */
    public void setText(String string)
    {
        myText = string;
    }

    /**
     * @return
     */
    public boolean isConcat()
    {
        return myConcat;
    }

    /**
     * @param b
     */
    public void setConcat(boolean b)
    {
        myConcat = b;
    }

}
