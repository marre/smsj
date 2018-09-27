package com.github.xfslove.smsj.mms.smil;

import com.github.xfslove.smsj.xml.XmlAttribute;
import com.github.xfslove.smsj.xml.XmlWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

/**
 * @author hanwen
 * created at 2018/8/22.
 */
public class SmilWriter implements XmlWriter, AutoCloseable {

  private final ByteArrayOutputStream smilBody = new ByteArrayOutputStream();

  private final Stack<String> stringBuf = new Stack<>();

  @Override
  public void writeTo(OutputStream os) throws IOException {

    smilBody.writeTo(os);
  }

  @Override
  public void setDoctype(String publicID) {
    // nothing
  }

  @Override
  public void addStartElement(String tag) throws IOException {

    smilBody.write('<');
    smilBody.write(tag.getBytes(StandardCharsets.UTF_8));
    smilBody.write('>');
    stringBuf.push(tag);
  }

  @Override
  public void addStartElement(String tag, XmlAttribute[] attribs) throws IOException {

    smilBody.write('<');
    smilBody.write(tag.getBytes(StandardCharsets.UTF_8));
    writeAttributes(smilBody, attribs);
    smilBody.write('>');
    stringBuf.push(tag);
  }

  @Override
  public void addEmptyElement(String tag) throws IOException {

    smilBody.write('<');
    smilBody.write(tag.getBytes(StandardCharsets.UTF_8));
    smilBody.write('/');
    smilBody.write('>');
  }

  @Override
  public void addEmptyElement(String tag, XmlAttribute[] attribs) throws IOException {

    smilBody.write('<');
    smilBody.write(tag.getBytes(StandardCharsets.UTF_8));
    writeAttributes(smilBody, attribs);
    smilBody.write('/');
    smilBody.write('>');
  }

  @Override
  public void addEndElement() throws IOException {
    smilBody.write('<');
    smilBody.write('/');
    smilBody.write(stringBuf.pop().getBytes(StandardCharsets.UTF_8));
    smilBody.write('>');
  }

  @Override
  public void addCharacters(String str) throws IOException {
    // nothing
  }

  private void writeAttributes(OutputStream os, XmlAttribute[] attribs) throws IOException {
    os.write(' ');
    for (int i = 0; i < attribs.length; i++) {
      XmlAttribute attr = attribs[i];
      os.write(attr.getType().getBytes(StandardCharsets.UTF_8));
      os.write('=');
      os.write('\"');
      os.write(attr.getValue().getBytes(StandardCharsets.UTF_8));
      os.write('\"');
      if (i != attribs.length - 1) {
        os.write(' ');
      }
    }
  }

  @Override
  public void close() throws Exception {
    smilBody.close();
  }
}
