package org.marre.mms;

import org.marre.xml.XmlDocument;
import org.marre.xml.XmlWriter;

import java.io.IOException;

/**
 * Created by hanwen on 2018/8/20.
 */
public class SmilDocument implements XmlDocument {

  public static final String SMIL_CONTENT_TYPE = "application/smil";

  @Override
  public String getContentType() {
    return SMIL_CONTENT_TYPE;
  }

  @Override
  public void writeXmlTo(XmlWriter xmlWriter) throws IOException {

  }
}
