/*
    SMS Library for the Java platform
    Copyright (C) 2002  Markus Eriksson
    Portions Copyright (C) 2002  Boris von Loesch

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
package org.marre.sms.transport.gsm;

import java.util.*;
import java.io.*;
import javax.comm.*;

import org.apache.commons.logging.*;

import org.marre.sms.util.SmsPduUtil;
import org.marre.sms.util.SmsDcsUtil;
import org.marre.sms.transport.SmsTransport;
import org.marre.sms.SmsPdu;
import org.marre.sms.SmsAddress;
import org.marre.sms.SmsMessage;
import org.marre.sms.SmsException;
import org.marre.sms.SmsConstants;

/**
 * An SmsTransport that sends the SMS from an GSM phone that is attached
 * to the serial port.
 * <p>
 * This transport has the following settable parameters:
 * <br>
 * <pre>
 * <b>sms.gsm.serialport</b> - Serial port where the GSM phone is located. Ex: "COM1"
 * <b>sms.gsm.bitrate</b> - Bits per second
 * <b>sms.gsm.bit</b> - Databits
 * <b>sms.gsm.parity</b> - Parity (N, E, O, M, S)
 * <b>sms.gsm.stopbits</b> - Stopbits (1, 1.5, 2)
 * <b>sms.gsm.flowcontrol</b> - FlowControl (XONXOFF, RTSCTS, NONE)
 * <b>sms.gsm.initcommands</b> - Initialization commands, seperated by ;
 * <b>
 * </pre>
 * <p>
 * <i>This transport cannot set the sending "address" to anything else
 * than the sending phone's phonenumber.</i>
 *
 * @todo Validity period
 *
 * @author Markus Eriksson, Boris von Loesch
 * @version $Id$
 */
public class GsmTransport implements SmsTransport, SerialPortEventListener
{
    static Log myLog = LogFactory.getLog(GsmTransport.class);

    private String mySerialPortName;
    private SerialPort mySerialPort;
    private OutputStream myOutStream;
    private InputStream myInStream;
    private byte[] myReadBuffer = new byte[1500]; // Buffer for serial input
    private int myBufferOffset = 0;              // serialEvent
    private Object myPortStatusLock = new Object();
    private int myPortStatus = MSG_OK;
    private String myPortMessage = "";
    private String[] myInitCmds;

    private int myBitRate, myBit, myStopbits, myParity, myFlowControl;

    private static String LINEFEED="\r";

    private static final int MSG_OK = 1;
    private static final int MSG_WAIT = 2;
    private static final int MSG_ERROR = 3;
    private static final int MSG_WMSG = 4;
    private static final int TRIES = 5;

    private static final int NOT_OK_NOTHING = 0;
    private static final int NOT_OK_RETRY = 1;
    private static final int NOT_OK_WAIT = 2;
    private static final int NOT_OK_WMSG = 3;

    public GsmTransport()
    {
    }

    public void init(Properties theProps) throws SmsException
    {
        mySerialPortName=theProps.getProperty("sms.gsm.serialport", "COM2");

        CommPortIdentifier portId = null;
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();

        /* find the requested port */
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            /* Check for serial port */
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(mySerialPortName)) {
                    try {
                        mySerialPort = (SerialPort) portId.open(GsmTransport.class.getName(), 3000);
                    } catch (PortInUseException e) {
                        myLog.error("Port "+mySerialPortName+" already open.", e);
                       throw new SmsException("Port "+mySerialPortName+" already open.");
                    }
                }
            }
        }

        /* port not found */
        if(mySerialPort == null) {
            myLog.error("Port " + mySerialPortName + " does not exist.");
            throw new SmsException("Port " + mySerialPortName + " does not exist.");
        }

        /* Open streams to the port */
        try
        {
            myOutStream = mySerialPort.getOutputStream();
            myInStream = mySerialPort.getInputStream();
        }
        catch (IOException e)
        {
            mySerialPort.close();
            myLog.error("Cannot open streams", e);
            throw new SmsException("Cannot open streams.");
        }

        /* Configure port */
        myBitRate = Integer.valueOf(theProps.getProperty("sms.gsm.bitrate", "19200")).intValue();

        myBit=SerialPort.DATABITS_8;
        switch (Integer.valueOf(theProps.getProperty("sms.gsm.bit", "8")).intValue()){
            case 5: myBit=SerialPort.DATABITS_5; break;
            case 6: myBit=SerialPort.DATABITS_6; break;
            case 7: myBit=SerialPort.DATABITS_7; break;
            case 8: myBit=SerialPort.DATABITS_8; break;
        }

        myStopbits=SerialPort.STOPBITS_1;
        String st = theProps.getProperty("sms.gsm.stopbits", "1");
        if (st.equals("1")) myStopbits=SerialPort.STOPBITS_1;
        else if (st.equals("1.5")) myStopbits=SerialPort.STOPBITS_1_5;
        else if (st.equals("2")) myStopbits=SerialPort.STOPBITS_2;

        myParity=SerialPort.PARITY_NONE;
        st = theProps.getProperty("sms.gsm.parity", "NONE").toUpperCase();
        if (st.equals("NONE")) myParity=SerialPort.PARITY_NONE;
        else if (st.equals("EVEN")) myParity=SerialPort.PARITY_EVEN;
        else if (st.equals("ODD")) myParity=SerialPort.PARITY_ODD;
        else if (st.equals("MARK")) myParity=SerialPort.PARITY_MARK;
        else if (st.equals("SPACE")) myParity=SerialPort.PARITY_SPACE;


        myFlowControl=SerialPort.FLOWCONTROL_RTSCTS_IN|SerialPort.FLOWCONTROL_RTSCTS_OUT;
        st = theProps.getProperty("sms.gsm.flowcontrol", "NONE").toUpperCase();
        if (st.equals("RTSCTS")) myFlowControl=SerialPort.FLOWCONTROL_RTSCTS_IN|SerialPort.FLOWCONTROL_RTSCTS_OUT;
        else if (st.equals("XONXOFF")) myFlowControl=SerialPort.FLOWCONTROL_XONXOFF_IN|SerialPort.FLOWCONTROL_XONXOFF_OUT;
        else if (st.equals("NONE")) myFlowControl=mySerialPort.FLOWCONTROL_NONE;

        StringTokenizer stk = new StringTokenizer(theProps.getProperty("sms.gsm.initcommands", "AT+CMGF=0"));
        myInitCmds = new String[stk.countTokens()];
        for (int i=0; stk.hasMoreTokens(); i++)
        {
            myInitCmds[i] = stk.nextToken();
        }
    }

    /**
     * Initializes the communication with the GSM phone.
     *
     * @throws SmsException Thrown when the transport fails to communicate
     * with the GSM phone
     */
    public void connect() throws SmsException
    {
      try {
        mySerialPort.setSerialPortParams(myBitRate, myBit, myStopbits, myParity);
        mySerialPort.setFlowControlMode(myFlowControl);
        mySerialPort.enableReceiveTimeout(3000);
        mySerialPort.addEventListener(this);
        }
        catch (UnsupportedCommOperationException e) {
            mySerialPort.close();
            myLog.error("Cannot initialize the port", e);
            throw new SmsException("Cannot initialize the port");
        }
        catch (TooManyListenersException e){
            myLog.error("Failed to add listener to serial port", e);
            throw new SmsException ("Failed to add listener to serial port");
        }
        /* Start a thread for handling comminication with the terminal */

        /*Add handler for serial events*/
        mySerialPort.notifyOnDataAvailable(true);

        // Send AT to GSM phone
        try
        {
            ping();
        }
        catch (SmsException e)
        {
            myLog.error("Cannot connect the GSM phone");
            throw new SmsException("Cannot connect the GSM phone");
        }

        //Can I send sms via the gsm phone?
        try
        {
            sendCmd("AT+CSMS=0", NOT_OK_RETRY);
            if (myPortMessage.indexOf("+CMS ERROR")>=0)
            {
                // This will be catched below...
                throw new SmsException("");
            }
        }
        catch (SmsException e)
        {
            myLog.error("GSM phone cannot send short messages");
            throw new SmsException("GSM phone cannot send short messages");
        }

        //Init Commands
        try
        {
            for (int i=0; i<myInitCmds.length;i++)
            {
                sendCmd (myInitCmds[i], NOT_OK_RETRY);
            }
        }
        catch (SmsException e)
        {
            myLog.error("Error while initializing the GSM phone");
            throw new SmsException("Error while initializing the GSM phone");
        }
    }

    /**
     * Sends the SMS message to the given recipients.
     * <p>
     * Note: The sending address is ignored for the GSM transport.
     *
     * @param theMessage The message to send
     * @param theDestination The reciever
     * @param theSender The sending address, ignored
     * @throws SmsException Thrown if we fail to send the SMS
     */
    public void send(SmsMessage theMessage, SmsAddress theDestination, SmsAddress theSender)
        throws SmsException
    {
        SmsPdu msgPdu[] = null;

        if (theDestination.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC)
        {
            throw new SmsException("Cannot sent SMS to an ALPHANUMERIC address");
        }

        msgPdu = theMessage.getPdus();

        for(int i=0; i < msgPdu.length; i++)
        {
            byte dcs = theMessage.getDataCodingScheme();

            if (theDestination.getTypeOfNumber() == SmsConstants.TON_ALPHANUMERIC)
            {
                throw new SmsException("Cannot sent SMS to an ALPHANUMERIC address");
            }

            if (SmsDcsUtil.getAlphabet(dcs) == SmsConstants.ALPHABET_GSM)
            {
                sendSeptetEncodedPdu(msgPdu[i], dcs, theDestination, theSender);
            }
            else
            {
                sendOctetEncodedPdu(msgPdu[i], dcs, theDestination, theSender);
            }
        }
    }

    private void sendOctetEncodedPdu(SmsPdu thePdu, byte theDcs, SmsAddress theDestination, SmsAddress theSender)
        throws SmsException
    {
        byte ud[] = thePdu.getUserData();
        byte udh[] = thePdu.getUserDataHeaders();

        ByteArrayOutputStream baos = new ByteArrayOutputStream(200);
        try
        {
            int nUdBytes = thePdu.getUserDataLength();
            int nUdhBytes = (udh == null) ? 0 : udh.length;
            // +1 For the UDH Length
            int tpUdl = nUdBytes + nUdhBytes + 1;

            // Use default SMSC
            baos.write(0x00);

            // UDH?
            if( nUdhBytes == 0 )
            {
                // TP-Message-Type-Indicator = SUBMIT
                // TP-Reject-Duplicates = ON
                // TP-Validity-Period-Format = No field
                // TP-Status-Report-Request = No
                // TP-User-Data-Header = No
                // TP-Reply-Path = No
                baos.write(0x01);
            }
            else
            {
                // TP-Message-Type-Indicator = SUBMIT
                // TP-Reject-Duplicates = ON
                // TP-Validity-Period-Format = No field
                // TP-Status-Report-Request = No
                // TP-User-Data-Header = Yes
                // TP-Reply-Path = No
                baos.write(0x41);
            }

            // TP-Message-Reference
            // Leave to 0x00, MS will set it
            baos.write(0x00);

            // 2-12 octets
            // TP-DA
            // - 1:st octet - length of address (4 bits)
            // - 2:nd octet
            //   - myBit 7 - always 1
            //   - myBit 4-6 - TON
            //   - myBit 0-3 - NPI
            // - n octets - BCD
            writeDestinationAddress(baos, theDestination);

            // TP-PID
            baos.write(0x00);

            // TP-DCS
            baos.write(theDcs);

            // 1 octet/ 7 octets
            // TP-VP - Optional

            // UDH?
            if( nUdhBytes == 0 )
            {
                // 1 Integer
                // TP-UDL
                // UDL includes the length of UDH
                baos.write(nUdBytes);

                // n octets
                // TP-UD
                baos.write(ud);
            }
            else
            {
                // The whole UD PDU without the header length byte
                byte fullUd[] = new byte[nUdBytes + nUdhBytes];

                // TP-UDL includes the length of UDH
                // +1 is for the size header...
                baos.write(nUdBytes + nUdhBytes + 1);

                // User Data header length in octets
                baos.write(nUdhBytes);

                // TP-UDH
                System.arraycopy(udh, 0, fullUd, 0, nUdhBytes);
                // TP-UD
                System.arraycopy(ud, 0, fullUd, nUdhBytes, nUdBytes);

                baos.write(fullUd);
            }
            baos.close();
        }
        catch (IOException ex)
        {
            myLog.error(ex.getMessage(), ex);
            throw new SmsException(ex.getMessage());
        }

        myLog.debug("PDU : " + SmsPduUtil.bytesToHexString(baos.toByteArray()));
        myLog.debug("Length : " + baos.size());

        sendStream(baos);
    }

    private void sendSeptetEncodedPdu(SmsPdu thePdu, byte theDcs, SmsAddress theDestination, SmsAddress theSender)
        throws SmsException
    {
        byte ud[] = thePdu.getUserData();
        byte udh[] = thePdu.getUserDataHeaders();

        int nUdSeptets = thePdu.getUserDataLength();
        int nUdBits = 0;

        int nUdhBytes = (udh == null) ? 0 : udh.length;

        // UDH + UDHL
        int nUdhBits = 0;

        // UD + UDH + UDHL
        int nTotalBytes = 0;
        int nTotalBits = 0;
        int nTotalSeptets = 0;

        int nFillBits = 0;

        ByteArrayOutputStream baos = new ByteArrayOutputStream(161);

        try
        {
            // Use default SMSC
            baos.write(0x00);

            // UDH?
            if( nUdhBytes == 0 )
            {
                // TP-Message-Type-Indicator = SUBMIT
                // TP-Reject-Duplicates = ON
                // TP-Validity-Period-Format = No field
                // TP-Status-Report-Request = No
                // TP-User-Data-Header = No
                // TP-Reply-Path = No
                baos.write(0x01);
            }
            else
            {
                // +1 is for the UDHL
                nUdhBits = (nUdhBytes + 1) * 8;

                nFillBits = 7 - (nUdhBits % 7);

                // TP-Message-Type-Indicator = SUBMIT
                // TP-Reject-Duplicates = ON
                // TP-Validity-Period-Format = No field
                // TP-Status-Report-Request = No
                // TP-User-Data-Header = Yes
                // TP-Reply-Path = No
                baos.write(0x41);
            }

            nUdBits = nUdSeptets * 7;

            nTotalBits = nUdSeptets * 7 + nFillBits + nUdhBits;
            nTotalSeptets = nTotalBits / 7;

            nTotalBytes = nTotalBits / 8;
            if (nTotalBits % 8 > 0)
            {
                nTotalBytes += 1;
            }

            // TP-Message-Reference
            // Leave to 0x00, MS will set it
            baos.write(0x00);

            // 2-12 octets
            // TP-DA
            // - 1:st octet - length of address (4 bits)
            // - 2:nd octet
            //   - myBit 7 - always 1
            //   - myBit 4-6 - TON
            //   - myBit 0-3 - NPI
            // - n octets - BCD
            writeDestinationAddress(baos, theDestination);

            // TP-PID
            baos.write(0x00);

            // TP-DCS
            // UCS, septets, language, SMS class...
            baos.write(theDcs);

            // TP-VP - Optional
            // Probably not needed

            // UDH?
            if( (udh == null) || (udh.length == 0) )
            {
                // TP-UDL
                // UDL includes the length of UDH
                baos.write(nUdSeptets);

                // TP-UD
                baos.write(ud);
            }
            else
            {
                // The whole UD PDU
                byte fullUd[] = new byte[nTotalBytes - 1];

                // TP-UDL
                // UDL includes the length of the UDHL
                baos.write(nTotalSeptets);

                // User Data header length
                // In octets minus eventual fill bits
                baos.write(nUdhBytes);

                // TP-UDH
                System.arraycopy(udh, 0, fullUd, 0, nUdhBytes);

                // TP-UD
                SmsPduUtil.arrayCopy(ud, 0, fullUd, nUdhBytes, nFillBits, nUdBits);

                baos.write(fullUd);
            }
            baos.close();
        }
        catch (IOException ex)
        {
            myLog.error(ex.getMessage(), ex);
            throw new SmsException(ex.getMessage());
        }

        myLog.debug("PDU : " + SmsPduUtil.bytesToHexString(baos.toByteArray()));
        myLog.debug("Length : " + baos.size());

        sendStream(baos);
    }

    private void sendStream(ByteArrayOutputStream baos)
        throws SmsException
    {
        //Wake up
        sendCmd("AT", NOT_OK_RETRY);
        //Send message
        sendCmd("AT+CMGS=" + (baos.size() - 1), NOT_OK_WAIT);
        sendCmd(SmsPduUtil.bytesToHexString(baos.toByteArray()) + "\032", NOT_OK_WAIT);
        myLog.debug(myPortMessage);
    }

    private int sendCmd(String cmd, int waitStatus)
        throws SmsException
    {
        int ret=MSG_OK;
        int i=0;
        //Send Command, until response is not WAIT
        if (waitStatus == NOT_OK_RETRY)
        {
          while(((ret=sendCmd(cmd))==MSG_WAIT)&&(i++<TRIES));
        }
        else if (waitStatus==NOT_OK_WAIT)
        {
          sendCmd(cmd);
          while((myPortStatus==MSG_WAIT)&&(i++<TRIES))
          try
          {
            Thread.sleep(500);
          }
          catch (InterruptedException e)
          {
            myLog.error("Thread.sleep interrupted", e);
          }
          ret = myPortStatus;
        }
        else
        {
            ret = sendCmd(cmd);
        }
        if (i==TRIES+1)
        {
            myLog.error("Communication to GSM phone failed.\n Command: " + cmd);
            throw new SmsException("Communication to GSM phone failed.");
        }
        if (ret==MSG_ERROR)
        {
            myLog.error("Error while executing command: " + cmd);
            throw new SmsException("Error while executing command: " + cmd);
        }

        return ret;
    }

    private int sendCmd (String cmd)
    {
        synchronized(myPortStatusLock) {
            myPortStatus = MSG_WAIT;
            try {
                myOutStream.write((cmd + LINEFEED).getBytes());
            } catch (IOException e) {
                myLog.error("Fehler", e);
            }

            /* wait for response from device */
            try {
                myPortStatusLock.wait(1500); // millis
            } catch (InterruptedException e) {
                myLog.error("Fehler", e);
            }

            if(myPortStatus != MSG_OK) {
                myLog.debug("port not ok "+myPortStatus+","+cmd);
            }
        }
        return myPortStatus;
    }


    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
/*          case SerialPortEvent.BI:System.err.println("Break interrupt");
          case SerialPortEvent.OE:System.err.println("Overrun error");
          case SerialPortEvent.FE:System.err.println("Framing error");
          case SerialPortEvent.PE:System.err.println("Parity error");
          case SerialPortEvent.CD:System.err.println("Carrier detect");
          case SerialPortEvent.CTS:System.err.println("Clear to send");
          case SerialPortEvent.DSR:System.err.println("Data set ready");
          case SerialPortEvent.RI:System.err.println("Ring ind.");*/
          case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;

          case SerialPortEvent.DATA_AVAILABLE:
            int n;
            try {
                while ( (n = myInStream.available()) > 0) {
                    n = myInStream.read(myReadBuffer, myBufferOffset, n);
                    myBufferOffset += n;
                    // linefeed+carriage return detected, line ready
                    if((myReadBuffer[myBufferOffset-1] == 10) &&
                       (myReadBuffer[myBufferOffset-2] == 13)) {
                        String sbuf = new String(myReadBuffer,0,myBufferOffset-2);
//                        myLog.debug(sbuf);
                        lineReceived(sbuf);
                        myBufferOffset = 0;
                    }
                    else if (myReadBuffer[myBufferOffset-2] == '>'){
                        String sbuf = new String(myReadBuffer,0,myBufferOffset);
                        lineReceived(sbuf);
                        myBufferOffset = 0;
                    }
                }
            } catch (IOException e) {
              e.printStackTrace();
            }
            break;
        }
    }

    private void lineReceived(String buffer) {
        String response;
        StringTokenizer st = new StringTokenizer(buffer, "\r\n");

        synchronized(myPortStatusLock) {
            //scan st line by line
            while (st.hasMoreTokens()) {
                response = st.nextToken().trim();
                if (response.equals("")) {
                    myPortStatus = MSG_OK;
                } else if (response.startsWith("OK")) {
                    myPortStatus = MSG_OK;
                } else if (response.startsWith(">")) {
                    myPortStatus = MSG_WMSG;
                } else if (response.startsWith("ERROR")) {
                    myPortStatus = MSG_ERROR;
                } else if (response.startsWith("+CME ERROR") || response.startsWith("+CMS ERROR")) {
                    myPortStatus = MSG_ERROR;
                }
            }
            //For later use
            myPortMessage=buffer;
            myPortStatusLock.notify();
        }
        return;
    }

    /**
     * Sends a "AT" command to keep the connection alive
     *
     * @throws SmsException
     */
    public void ping()
        throws SmsException
    {
        sendCmd("AT", NOT_OK_RETRY);
        // PONG
    }

    /**
     * Closes the serial connection to the phone
     *
     * @throws SmsException
     */
    public void disconnect()
        throws SmsException
    {
        mySerialPort.close();
    }

    /**
     * Writes a destination address to the given stream in the correct format
     *
     * @param theOs Stream to write to
     * @param theDestination Destination address to encode
     * @throws IOException Thrown if failing to write to the stream
     */
    private void writeDestinationAddress(OutputStream theOs, SmsAddress theDestination)
        throws IOException
    {
        String address = theDestination.getAddress();
        int ton = theDestination.getTypeOfNumber();
        int npi = theDestination.getNumberingPlanIdentification();

        // trim leading + from address
        if (address.charAt(0) == '+')
        {
            address = address.substring(1);
        }

        // Length in semi octets
        theOs.write(address.length());

        // Type Of Address
        theOs.write(0x80 | ton << 4 | npi);

        // BCD encode
        SmsPduUtil.writeBcdNumber(theOs, address);
    }
}
