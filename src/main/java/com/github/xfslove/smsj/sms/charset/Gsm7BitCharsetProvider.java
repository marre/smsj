package com.github.xfslove.smsj.sms.charset;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Collections;
import java.util.Iterator;

/**
 * This is a CharsetProvider for the GSM 7-Bit character set. It is named
 * X-Gsm7Bit since it's not registered in the IANA registry
 * <p>
 * To activate this CharsetProvider, it's necessary to add a file to
 * the classpath of the JVM runtime at the following location:
 * META-INF/services/java.nio.charsets.spi.CharsetProvider
 * <p>
 * That file must contain a line with the fully qualified name of
 * this class on a line by itself:
 * org.smpp.charset.Gsm7BitCharsetProvider
 * <p>
 * See the javadoc page for java.nio.charsets.spi.CharsetProvider
 * for full details.
 * <p>
 * copy from https://github.com/OpenSmpp/opensmpp
 *
 * @author hanwen
 * created at 2018/8/24
 */
public class Gsm7BitCharsetProvider extends CharsetProvider {

  /**
   * A handle to the Charset object
   */
  private Charset gsm7Bit;

  /**
   * Constructor, instantiate a Charset object and save the reference.
   */
  public Gsm7BitCharsetProvider() {
    super();
    this.gsm7Bit = new Gsm7BitCharset(Gsm7BitCharset.CHARSET_NAME, null);
  }

  /**
   * Called by Charset static methods to find a particular named
   * Charset.  If it's the name of this charset (we don't have
   * any aliases) then return the Rot13 Charset, else return null.
   */
  @Override
  public Charset charsetForName(String charsetName) {
    if (charsetName.equalsIgnoreCase(Gsm7BitCharset.CHARSET_NAME)) {
      return gsm7Bit;
    }
    return null;
  }

  /**
   * Return an Iterator over the set of Charset objects we provide.
   *
   * @return An Iterator object containing references to all the
   * Charset objects provided by this class.
   */
  @Override
  public Iterator<Charset> charsets() {
    return Collections.singleton(gsm7Bit).iterator();
  }
}
/*
 * $Log: not supported by cvs2svn $
 */