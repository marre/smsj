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
package org.marre.wap.nokia;

import java.io.*;

import org.marre.xml.*;
import org.marre.xml.wbxml.*;

//Liquidterm: evaluate the addition of a WBXML document class from which derive
//various kind of WBXML stuff

/**
 * Contains Nokia OTABookmark structure
 *
 * @author Fabio Corneti
 */
public class OtaBookmark
{
    private String myTitle = null;
    private String myUri = null;

    /**
     * Creates a Nokia OTA Bookmark object
     *
     * @param theTitle String containing the title of the bookmark
     * @param theUri String containing the URI of the bookmark
     */
    public OtaBookmark(String theTitle, String theUri)
    {
        myTitle = theTitle;
        myUri = theUri;
    }

    public static final WbxmlWriter getWbxmlWriter()
    {
        WbxmlWriter writer = new WbxmlWriter();

        writer.setTagTokens(OtaConstants.OTA_TAG_TOKENS);
        writer.setAttrStartTokens(OtaConstants.OTA_ATTR_START_TOKENS);
        writer.setAttrValueTokens(OtaConstants.OTA_ATTR_VALUE_TOKENS);

        return writer;
    }

    public void writeTo(XmlWriter writer, OutputStream os)
        throws IOException
    {
        writer.reset();

        writer.setDoctype(null);

        writer.addStartElement("CHARACTERISTIC-LIST");
        writer.addStartElement("CHARACTERISTIC", new XmlAttribute[] {
            new XmlAttribute("TYPE", "BOOKMARK") });
        writer.addEmptyElement("PARM", new XmlAttribute[] {
            new XmlAttribute("NAME", "NAME"), new XmlAttribute("VALUE",myTitle)});
        writer.addEmptyElement("PARM", new XmlAttribute[] {
            new XmlAttribute("NAME", "URL"), new XmlAttribute("VALUE",myUri)});
        writer.addEndTag();
        writer.addEndTag();

        writer.writeTo(os);
    }
    
    /**
     * Returns a byte array containing the Wbxml of the bookmark
     *
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try
        {
            this.writeTo(OtaBookmark.getWbxmlWriter(), baos);
            baos.close();
        }
        catch (IOException e)
        {
            // Shouldn't happen
        }

        return baos.toByteArray();
    }
    
    public static void main(String argv[])
        throws Exception
    {
        OtaBookmark nbk = new OtaBookmark("http://wap.google.com/", "Google!");
        FileOutputStream fos = new FileOutputStream("/home/liquidterm/bookmark.wbxml");
        System.out.println(fos);
        nbk.writeTo(getWbxmlWriter(), fos);
    }

    public String getUri()
    {
        return myUri;
    }

    public void setUri(String theUri)
    {
        this.myUri = theUri;
    }

    public String getTitle()
    {
        return myTitle;
    }

    public void setTitle(String theTitle)
    {
        this.myTitle = theTitle;
    }

}
