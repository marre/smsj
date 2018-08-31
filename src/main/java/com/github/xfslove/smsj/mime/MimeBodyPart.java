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
package com.github.xfslove.smsj.mime;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a MIME body part.
 * <p>
 * A body part can contain headers and a body.
 *
 * @author Markus Eriksson
 * @version $Id$
 */
public class MimeBodyPart implements Serializable {

  private byte[] body;

  private MimeContentType contentType;

  private final List<MimeHeader> headers = new LinkedList<>();

  /**
   * Creates a new empty MimeBodyPart.
   */
  public MimeBodyPart() {
  }

  /**
   * Creates a new empty MimeBodyPart.
   *
   * @param body        body
   * @param contentType content type
   */
  public MimeBodyPart(byte[] body, MimeContentType contentType) {
    setContent(body, contentType);
  }

  /**
   * Creates a new empty MimeBodyPart.
   *
   * @param body        body
   * @param contentType content type
   */
  public MimeBodyPart(byte[] body, String contentType) {
    setContent(body, contentType);
  }

  MimeBodyPart(String contentType) {
    this.contentType = new MimeContentType(contentType);
  }

  /**
   * Adds a mime header to this body part.
   *
   * @param header The header to add
   */
  public void addHeader(MimeHeader header) {
    headers.add(header);
  }

  /**
   * Adds a header to this body part.
   *
   * @param headerName  The name of the header
   * @param headerValue The value
   */
  public void addHeader(String headerName, String headerValue) {
    MimeHeader header = getHeader(headerName);
    if (header != null) {
      headers.remove(header);
    }
    addHeader(new MimeHeader(headerName, headerValue));
  }

  /**
   * @return Retrieves all headers.
   */
  public Collection<MimeHeader> getHeaders() {
    return Collections.unmodifiableCollection(headers);
  }

  /**
   * Retrieves a header with the given name.
   *
   * @param headerName The name of the header to find
   * @return The header, or null if not found
   */
  public MimeHeader getHeader(String headerName) {
    for (MimeHeader header : headers) {
      if (header.getName().equalsIgnoreCase(headerName)) {
        return header;
      }
    }

    return null;
  }

  /**
   * Sets the main content of this body part.
   *
   * @param content     The main content
   * @param contentType The content-type of the content
   */
  public void setContent(byte[] content, String contentType) {
    body = new byte[content.length];
    System.arraycopy(content, 0, body, 0, content.length);
    this.contentType = new MimeContentType(contentType);
  }

  /**
   * Sets the main content of this body part.
   *
   * @param content     The main content
   * @param contentType The content type
   */
  public void setContent(byte[] content, MimeContentType contentType) {
    body = new byte[content.length];
    System.arraycopy(content, 0, body, 0, content.length);
    this.contentType = contentType;
  }

  /**
   * Sets the "Content-Id" header.
   *
   * @param contentId The content-id
   */
  public void setContentId(String contentId) {
    String escapedContentId = contentId
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;");

    addHeader("Content-ID", "<" + escapedContentId + ">");
  }

  /**
   * Sets the "Content-Location" header.
   *
   * @param contentLocation The content-location
   */
  public void setContentLocation(String contentLocation) {
    addHeader("Content-Location", contentLocation);
  }

  /**
   * Returns the content of this body part.
   *
   * @return The content
   */
  public byte[] getBody() {
    byte[] bodyCopy = null;

    if (body != null) {
      bodyCopy = new byte[body.length];
      System.arraycopy(body, 0, bodyCopy, 0, body.length);
    }

    return bodyCopy;
  }

  /**
   * Returns the size of the body in this body part.
   *
   * @return The size of the body
   */
  public int getBodySize() {
    return body.length;
  }

  /**
   * Returns the content type.
   *
   * @return The content type
   */
  public MimeContentType getContentType() {
    return contentType;
  }
}
