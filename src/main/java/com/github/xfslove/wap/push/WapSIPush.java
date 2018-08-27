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
package com.github.xfslove.wap.push;

import com.github.xfslove.wap.wbxml.WbxmlWriter;
import com.github.xfslove.wap.wbxml.WbxmlDocument;
import com.github.xfslove.xml.XmlAttribute;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WapSIPush implements WbxmlDocument {

  public static final String WBXML_CONTENT_TYPE = "application/vnd.wap.sic";

  public static final String[] SI_TAG_TOKENS = {
      // 05
      "si",
      // 06
      "indication",
      // 07
      "info",
      // 08
      "item"
  };

  public static final String[] SI_ATTR_START_TOKENS = {
      // 05
      "action=signal-none",
      // 06
      "action=signal-low",
      // 07
      "action=signal-medium",
      // 08
      "action=signal-high",
      // 09
      "action=delete",
      // 0A
      "created",
      // 0B
      "href",
      // 0C
      "href=http://",
      // 0D
      "href=http://www.",
      // 0E
      "href=https://",
      // 0F
      "href=https://www.",
      // 10
      "si-expires",
      // 11
      "si-id",
      // 12
      "class"
  };

  public static final String[] SI_ATTR_VALUE_TOKENS = {
      // 85
      ".com/",
      // 86
      ".edu/",
      // 87
      ".net/",
      // 88
      ".org/"
  };

  private String uri;

  private String id;

  /**
   * Example: 1999-04-30T06:40:00Z means 6.40 in the morning UTC on the
   * 30th of April 1999.
   */
  private Date createdDate;
  private Date expiresDate;

  private String action;

  private String message;

  public WapSIPush(String uri, String message) {
    this.uri = uri;
    this.message = message;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getCreated() {
    return createdDate;
  }

  public void setCreated(Date created) {
    createdDate = created;
  }

  public Date getExpires() {
    return expiresDate;
  }

  public void setExpires(Date expires) {
    expiresDate = (Date) expires.clone();
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public void writeXmlTo(OutputStream os) throws Exception {
    try (WbxmlWriter writer = new WbxmlWriter(SI_TAG_TOKENS, SI_ATTR_START_TOKENS, SI_ATTR_VALUE_TOKENS)) {
      writer.setDoctype("-//WAPFORUM//DTD SI 1.0//EN");

      writer.addStartElement("si");
      List<XmlAttribute> attrs = new ArrayList<>();
      attrs.add(new XmlAttribute("href", uri));
      if (id != null && id.length() > 0) {
        attrs.add(new XmlAttribute("si-id", id));
      }
      if (createdDate != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        attrs.add(new XmlAttribute("si-expires", sdf.format(createdDate)));
      }
      if (expiresDate != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        attrs.add(new XmlAttribute("created", sdf.format(expiresDate)));
      }
      if (action != null && action.length() > 0) {
        attrs.add(new XmlAttribute("action", action));
      }
      writer.addStartElement("indication", attrs.toArray(new XmlAttribute[0]));
      writer.addCharacters(message);
      writer.addEndElement();
      writer.addEndElement();

      writer.writeTo(os);
    }
  }

  @Override
  public String getContentType() {
    return WBXML_CONTENT_TYPE;
  }
}
