package org.marre.sms.ems;

import org.marre.sms.SmsConcatMessage;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsUdhElement;
import org.marre.sms.nokia.OtaBitmap;
import org.marre.sms.util.SmsUdhUtil;

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
 * Revision 1.1  2004/04/26 10:52:33  wammy
 * *** empty log message ***
 *
 */


public class EmsVariablePicture extends SmsConcatMessage {

	//private byte[] theOtaBitmap = null;
	//int width = 0;
	//int height = 0;
	OtaBitmap otaBitmap = null;
	
	/**
	 * 
	 */
	public EmsVariablePicture() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param theRingTone
	 */
	public EmsVariablePicture(byte[] otaBitmapData) 
	{
		super(SmsConstants.DCS_DEFAULT_8BIT);
		otaBitmap = new OtaBitmap(otaBitmapData);
	}

	/**
	 * 
	 * @param theRingTone
	 */
	public EmsVariablePicture(OtaBitmap otaBitmap) 
	{
		super(SmsConstants.DCS_DEFAULT_8BIT);
		this.otaBitmap = otaBitmap;
		setContent();
	}

	/**
	 * 
	 * @param theRingTone
	 */
	public void setContent()
	{		
		setContent(
				new SmsUdhElement[] {
						SmsUdhUtil.getEmsVariablePictureUdh(otaBitmap, 0)
				},
				"\n".getBytes(),
				1);
	}
}
