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
package org.marre.sms.transport.gsm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

/**
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class SerialComm
{
    private static Log logger = LogFactory.getLog(SerialComm.class);

    private static final int DEFAULT_BIT_RATE = 19200;
    private static String APP_NAME = "SMSJ";
    
    private SerialPort mySerialPort;
    private OutputStream myOutStream;
    private InputStream myInStream;

    private String myPortName;
    private int myBitRate;
    private int myDataBits; 
    private int myStopBits;
    private int myParity;
    private int myFlowControl;

    public SerialComm(String thePortName)
    {
        myPortName = thePortName;
        myBitRate = DEFAULT_BIT_RATE;
        myDataBits = SerialPort.DATABITS_8;
        myStopBits = SerialPort.STOPBITS_1;
        myParity = SerialPort.PARITY_NONE;
        myFlowControl = SerialPort.FLOWCONTROL_NONE;
    }

    public SerialPort openSerialPort(String portName)
        throws PortInUseException
    {
        SerialPort serialPort = null;
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();

        // find the requested port
        while (portList.hasMoreElements()) 
        {
            CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();

            // Check for serial port
            if ( (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) &&
                 (portId.getName().equals(portName)) ) 
            {
                return (SerialPort) portId.open(APP_NAME, 3000);
            }
        }
        
        return null;
    }

    public void open() 
        throws IOException, PortInUseException
    {
        mySerialPort = openSerialPort(myPortName);
        if (mySerialPort == null)
        {
            throw new IOException("Failed to open port");
        }
        
        try
        {
            myOutStream = mySerialPort.getOutputStream();
            myInStream = mySerialPort.getInputStream();
        
            mySerialPort.setSerialPortParams(myBitRate, myDataBits, myStopBits, myParity);
            mySerialPort.setFlowControlMode(myFlowControl);
        } 
        catch (UnsupportedCommOperationException e)
        {
            mySerialPort.close();
            mySerialPort = null;
            throw new IOException(e.getMessage());
        } 
    }

    public void close()
        throws IOException
    {
        if (myOutStream != null)
        {
            myOutStream.close();
            myOutStream = null;
        }
        
        if (myInStream != null)
        {
            myInStream.close();
            myInStream = null;            
        }
        
        if (mySerialPort != null)
        {        
            mySerialPort.close();
            mySerialPort = null; 
        }
    }

    public void send(String data) 
        throws IOException
    {
        logger.info(">> " + data);
        
        myOutStream.write(data.getBytes());
        myOutStream.write("\r".getBytes());
    }

    public String readLine() 
        throws IOException
    {        
        StringBuffer buffer = new StringBuffer(256);
        int ch;

        while (true)
        {
            ch = myInStream.read();
            if ( (ch == -1) ||
                 (ch == '\r') )
            {
                break;
            }
            
            if ( ch == '\n' )
            {
                continue;
            }
            
            buffer.append((char) ch);
            
            // Special case : continue data response
            if ( ch == '>')
            {
                break;
            }
        }

        String row = buffer.toString();
        
        // LOG
        logger.info("<< " + row);

        return row;
    }
    
    public void setBitRate(String theBitRate)
    {
        if      ("110".equals(theBitRate))    myBitRate = 110;
        else if ("134".equals(theBitRate))    myBitRate = 134;
        else if ("150".equals(theBitRate))    myBitRate = 150;
        else if ("300".equals(theBitRate))    myBitRate = 300;
        else if ("600".equals(theBitRate))    myBitRate = 600;
        else if ("1200".equals(theBitRate))   myBitRate = 1200;
        else if ("2400".equals(theBitRate))   myBitRate = 2400;
        else if ("4800".equals(theBitRate))   myBitRate = 4800;
        else if ("9600".equals(theBitRate))   myBitRate = 9600;
        else if ("14400".equals(theBitRate))  myBitRate = 14400;
        else if ("19200".equals(theBitRate))  myBitRate = 19200;
        else if ("38400".equals(theBitRate))  myBitRate = 38400;
        else if ("57600".equals(theBitRate))  myBitRate = 57600;
        else if ("115200".equals(theBitRate)) myBitRate = 115200;
        else if ("128000".equals(theBitRate)) myBitRate = 128000;
        else                                  myBitRate = DEFAULT_BIT_RATE;        
    }
    
    public void setDataBits(String theDataBits)
    {
        if      ("5".equals(theDataBits)) myDataBits = SerialPort.DATABITS_5;
        else if ("6".equals(theDataBits)) myDataBits = SerialPort.DATABITS_6;
        else if ("7".equals(theDataBits)) myDataBits = SerialPort.DATABITS_7;
        else if ("8".equals(theDataBits)) myDataBits = SerialPort.DATABITS_8;
        else                              myDataBits = SerialPort.DATABITS_8;
    }

    public void setFlowControl(String theFlowControl)
    {
        if      ("RTSCTS".equals(theFlowControl))  myFlowControl = SerialPort.FLOWCONTROL_RTSCTS_IN  | SerialPort.FLOWCONTROL_RTSCTS_OUT;
        else if ("XONXOFF".equals(theFlowControl)) myFlowControl = SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT;
        else if ("NONE".equals(theFlowControl))    myFlowControl = SerialPort.FLOWCONTROL_NONE;
        else                                       myFlowControl = SerialPort.FLOWCONTROL_NONE;
    }

    public void setParity(String theParity)
    {
        if      ("NONE".equals(theParity))  myParity = SerialPort.PARITY_NONE;
        else if ("EVEN".equals(theParity))  myParity = SerialPort.PARITY_EVEN;
        else if ("ODD".equals(theParity))   myParity = SerialPort.PARITY_ODD;
        else if ("MARK".equals(theParity))  myParity = SerialPort.PARITY_MARK;
        else if ("SPACE".equals(theParity)) myParity = SerialPort.PARITY_SPACE;
        else                                myParity = SerialPort.PARITY_NONE;
    }

    public void setStopBits(String theStopBits)
    {
        if      ("1".equals(theStopBits))   myStopBits = SerialPort.STOPBITS_1;
        else if ("1.5".equals(theStopBits)) myStopBits = SerialPort.STOPBITS_1_5;
        else if ("2".equals(theStopBits))   myStopBits = SerialPort.STOPBITS_2;
        else                                myStopBits = SerialPort.STOPBITS_1;
    }
}
