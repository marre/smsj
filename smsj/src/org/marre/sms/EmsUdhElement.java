package org.marre.sms;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Title:         
 * Description:
 * Copyright:     Copyright (c) 2003
 * Company:       iModel Music
 * @author        
 * 
 * $Id$
 *
 * History
 * ----------------------------------------------------------------------------
 *
 * $Log$
 * Revision 1.1  2004/04/26 10:21:08  wammy
 * *** empty log message ***
 *
 */

/**
 * @author lincoln
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EmsUdhElement extends SmsUdhElement {

	int myPos;
	
	/**
	 * @param theUdhIei
	 * @param theUdhIeiData
	 */
	public EmsUdhElement(int theUdhIei, byte[] theUdhIeiData, int thePos) {
		super(theUdhIei, theUdhIeiData);
		myPos = thePos;
	}
	
	/**
		* Returns the total length of this UDH element.
		* <p>
		* The length is including the UDH data length and the UDH "header" and element 
		* position (2 bytes)
		* 
		* @return the length
		*/
	 public int getTotalLength()
	 {
			 return myUdhIeiData.length + 3;
	 }
	
	/**
	 * Return the UDH element including the UDH "header" and position (three bytes)
	 *
	 * @return Data
	 */
	public byte[] getData()
	{
			byte[] allData = new byte[myUdhIeiData.length + 3];

			allData[0] = (byte) (myUdhIei & 0xff);
			allData[1] = (byte) ((myUdhIeiData.length + 1) & 0xff);
			allData[2] = (byte) (myPos & 0xff);
			System.arraycopy(myUdhIeiData, 0, allData, 3, myUdhIeiData.length);

			return allData;
	}

	/**
	 * Writes the UDH element including UDH "header" to the given stream
	 *
	 * @param os Stream to write to
	 * @throws IOException
	 */
	public void writeTo(OutputStream os)
			throws IOException
	{
			os.write(myUdhIei);
			os.write(myUdhIeiData.length + 1); // + 1 FOR POSITION
			os.write(myPos);
			os.write(myUdhIeiData);
	}
	/**
	 * @return
	 */
	public int getPosition() {
		return myPos;
	}

	/**
	 * @param i
	 */
	public void setPosition(int thePosition) {
		myPos = thePosition;
	}

}
