package org.marre.sms.ems;

import org.marre.sms.SmsConcatMessage;
import org.marre.sms.SmsConstants;
import org.marre.sms.SmsUdhElement;
import org.marre.sms.SmsUdhUtil;
import org.marre.sms.nokia.OtaBitmap;

/**
 * Title: Description: Copyright: Copyright (c) 2003 Company: iModel Music
 * 
 * @author
 * 
 * $Id$
 * 
 * History
 * ----------------------------------------------------------------------------
 * 
 * $Log$
 * Revision 1.3  2004/11/18 21:34:54  c95men
 * Removed SmsAbstractMessage
 *
 * Revision 1.2  2004/11/02 17:59:58  c95men
 * Major restructuring.
 *
 * - Minimize coupling between sms, mms and wap code. Moved some classes to another package.
 * - Redesigned SmsConcatMessage
 * - Redesigned wap push
 * - Added junit tests to some of the classes.
 * - Using maven to build.
 * - Using eclipse as IDE
 * Revision 1.1 2004/04/26 10:52:33 wammy ***
 * empty log message ***
 *  
 */

public class EmsVariablePicture /* extends SmsAbstractMessage */
{

    //private byte[] theOtaBitmap = null;
    //int width = 0;
    //int height = 0;
    protected OtaBitmap myOtaBitmap;

    /**
     *  
     */
    public EmsVariablePicture()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param theRingTone
     */
    public EmsVariablePicture(byte[] otaBitmapData)
    {
//        setDataCodingScheme(SmsConstants.DCS_DEFAULT_8BIT);
        myOtaBitmap = new OtaBitmap(otaBitmapData);
    }

    /**
     * 
     * @param theRingTone
     */
    public EmsVariablePicture(OtaBitmap otaBitmap)
    {
//        setDataCodingScheme(SmsConstants.DCS_DEFAULT_8BIT);
        this.myOtaBitmap = otaBitmap;
        setContent();
    }

    /**
     * 
     * @param theRingTone
     */
    public void setContent()
    {
/*        
        setContent(new SmsUdhElement[]{
                SmsUdhUtil.getEmsVariablePictureUdh(myOtaBitmap.getImageData(), 
                                                    myOtaBitmap.getWidth(), 
                                                    myOtaBitmap.getHeight(), 0), }, 
                "\n".getBytes(), 
                1);
*/                
    }
}
