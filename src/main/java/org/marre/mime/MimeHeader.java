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
package org.marre.mime;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MimeHeader implements Serializable {

  private final String name;

  private String value;

  private final List<MimeHeaderParameter> params = new LinkedList<>();

  public MimeHeader(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public void setValue(String theValue) {
    value = theValue;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public void setParam(String theName, String theValue) {
    // Remove parameter if it already exists...
    removeParameter(theName);

    // Add new...
    params.add(new MimeHeaderParameter(theName, theValue));
  }

  public MimeHeaderParameter getParameter(String theName) {
    for (MimeHeaderParameter param : params) {
      if (param.getName().equalsIgnoreCase(theName)) {
        return param;
      }
    }

    // Not found
    return null;
  }

  public void removeParameter(String theName) {
    MimeHeaderParameter param = getParameter(theName);
    if (param != null) {
      params.remove(param);
    }
  }

  public Collection<MimeHeaderParameter> getParameters() {
    return Collections.unmodifiableCollection(params);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(name).append("=").append(value);

    for (MimeHeaderParameter param : params) {
      sb.append("; ").append(param.getName()).append("=").append(param.getValue());
    }

    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MimeHeader)) return false;

    MimeHeader that = (MimeHeader) o;

    if (!name.equals(that.name)) return false;
    if (!value.equals(that.value)) return false;
    if (!params.equals(that.params)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + value.hashCode();
    result = 31 * result + params.hashCode();
    return result;
  }
}
