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
 * The Initial Developer of the Original Code is Boris von Loesch.
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s): Markus Eriksson
 *
 * ***** END LICENSE BLOCK ***** */
package com.github.xfslove.wap.nokia;

import com.github.xfslove.xml.XmlAttribute;
import com.github.xfslove.xml.XmlWriter;

import java.io.IOException;
import java.io.Serializable;


public class NokiaOtaBookmark implements Serializable {

  private final String name;

  private final String url;

  /**
   * Creates a Nokia Ota Browser Settings Bookmark
   *
   * @param name the name of the bookmark (max 50 chars)
   * @param url  the URL of the bookmark (max 255 chars)
   */
  public NokiaOtaBookmark(String name, String url) {
    this.name = name;
    this.url = url;
  }

  public void writeXmlTo(XmlWriter xmlWriter) throws IOException {
    // <CHARACTERISTIC TYPE="BOOKMARK">
    xmlWriter.addStartElement("CHARACTERISTIC", new XmlAttribute[]{new XmlAttribute("TYPE", "BOOKMARK")});

    // <PARM NAME="NAME" VALUE="name"/>
    xmlWriter.addEmptyElement("PARM", new XmlAttribute[]{
        new XmlAttribute("NAME", "NAME"),
        new XmlAttribute("VALUE", name)});
    // <PARM NAME="URL" VALUE="url"/>
    xmlWriter.addEmptyElement("PARM", new XmlAttribute[]{
        new XmlAttribute("NAME", "URL"),
        new XmlAttribute("VALUE", url)});

    // </CHARACTERISTIC>
    xmlWriter.addEndElement();
  }
}
