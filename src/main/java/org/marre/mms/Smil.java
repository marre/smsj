package org.marre.mms;

import org.marre.xml.XmlDocument;

import java.io.OutputStream;

/**
 * Created by hanwen on 2018/8/22.
 */
public class Smil implements XmlDocument {

  public static final String SMIL_CONTENT_TYPE = "application/smil";

  @Override
  public String getContentType() {
    return SMIL_CONTENT_TYPE;
  }

  @Override
  public void writeXmlTo(OutputStream os) throws Exception {

    try (SmilWriter writer = new SmilWriter()) {
      writer.writeTo(os);
    }

  }
}
