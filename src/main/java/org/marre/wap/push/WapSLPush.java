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
package org.marre.wap.push;

import org.marre.wap.wbxml.WbxmlDocument;
import org.marre.wap.wbxml.WbxmlWriter;
import org.marre.xml.XmlAttribute;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a WAP Service Loading Push message.
 *
 * @author Markus
 * @version $Id$
 */
public class WapSLPush implements WbxmlDocument {
  /**
   * WBXML content type
   */
  public static final String WBXML_CONTENT_TYPE = "application/vnd.wap.slc";

  /**
   * Action, execute-low
   */
  public static final String ACTION_EXECUTE_LOW = "execute-low";
  /**
   * Action, execute-high
   */
  public static final String ACTION_EXECUTE_HIGH = "execute-high";
  /**
   * Action, execute-cache
   */
  public static final String ACTION_EXECUTE_CACHE = "execute-cache";

  /**
   * WBXML tag tokens for wap sl push.
   */
  public static final String[] SL_TAG_TOKENS = {
      // 05
      "sl"
  };

  /**
   * WBXML attr start tokens for wap sl push.
   */
  public static final String[] SL_ATTR_START_TOKENS = {
      // 05
      "action=execute-low",
      // 06
      "action=execute-high",
      // 07
      "action=cache",
      // 08
      "href",
      // 09
      "href=http://",
      // 0A
      "href=http://www.",
      // 0B
      "href=https://",
      // 0C
      "href=https://www."
  };

  /**
   * WBXML attr value tokens for wap sl push.
   */
  public static final String[] SL_ATTR_VALUE_TOKENS = {
      // 85
      ".com/",
      // 86
      ".edu/",
      // 87
      ".net/",
      // 88
      ".org/"
  };


  /**
   * The uri.
   */
  private String uri;
  /**
   * The action.
   */
  private String action;

  /**
   * Constructor.
   *
   * @param uri
   */
  public WapSLPush(String uri) {
    this.uri = uri;
  }

  /**
   * Returns the URI.
   *
   * @return
   */
  public String getUri() {
    return uri;
  }

  /**
   * Sets the URI.
   *
   * @param uri
   */
  public void setUri(String uri) {
    this.uri = uri;
  }

  /**
   * Retrieves the current set action.
   *
   * @return Action or null if not set.
   */
  public String getAction() {
    return action;
  }

  /**
   * Set the action.
   *
   * @param action Can be ACTION_EXECUTE_LOW, ACTION_EXECUTE_HIGH or ACTION_EXECUTE_CACHE.
   */
  public void setAction(String action) {
    if (!(ACTION_EXECUTE_CACHE.equals(action) ||
        ACTION_EXECUTE_HIGH.equals(action) ||
        ACTION_EXECUTE_LOW.equals(action))) {
      throw new IllegalArgumentException("Action can only be execute-high, execute-low or cache.");
    }
    this.action = action;
  }

  /**
   * Writes the xml document to the given writer.
   *
   * @param os
   */
  @Override
  public void writeXmlTo(OutputStream os) throws Exception {
    try (WbxmlWriter writer = new WbxmlWriter(SL_TAG_TOKENS, SL_ATTR_START_TOKENS, SL_ATTR_VALUE_TOKENS)) {
      writer.setDoctype("-//WAPFORUM//DTD SL 1.0//EN");

      List<XmlAttribute> attrs = new ArrayList<>();
      attrs.add(new XmlAttribute("href", uri));
      if (action != null && action.length() > 0) {
        attrs.add(new XmlAttribute("action", action));
      }

      writer.addEmptyElement("sl", attrs.toArray(new XmlAttribute[0]));
      writer.writeTo(os);
    }
  }

  /**
   * Returns the text content type.
   *
   * @return Content type
   */
  @Override
  public String getContentType() {
    return WBXML_CONTENT_TYPE;
  }
}
