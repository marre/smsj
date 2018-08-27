package com.github.xfslove.sms.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.HashMap;
import java.util.Map;

/**
 * A Charset implementation for Gsm 7-bit default and extended character set
 * See GSM 03.38
 * copy from https://github.com/OpenSmpp/opensmpp
 *
 * @author hanwen
 * @date 2018/8/24
 */
public class Gsm7BitCharset extends Charset {

  /**
   * HashMap's used for encoding and decoding
   */
  private static final Map<String, Byte> DEFAULT_ENCODE_MAP = new HashMap<>();
  private static final Map<Byte, String> DEFAULT_DECODE_MAP = new HashMap<>();
  private static final Map<String, Byte> EXT_ENCODE_MAP = new HashMap<>();
  private static final Map<Byte, String> EXT_DECODE_MAP = new HashMap<>();


  // static section that populates the encode and decode HashMap objects
  static {
    // default alphabet
    DEFAULT_ENCODE_MAP.put("@", (byte) 0x00);
    DEFAULT_ENCODE_MAP.put("£", (byte) 0x01);
    DEFAULT_ENCODE_MAP.put("$", (byte) 0x02);
    DEFAULT_ENCODE_MAP.put("¥", (byte) 0x03);
    DEFAULT_ENCODE_MAP.put("è", (byte) 0x04);
    DEFAULT_ENCODE_MAP.put("é", (byte) 0x05);
    DEFAULT_ENCODE_MAP.put("ù", (byte) 0x06);
    DEFAULT_ENCODE_MAP.put("ì", (byte) 0x07);
    DEFAULT_ENCODE_MAP.put("ò", (byte) 0x08);
    DEFAULT_ENCODE_MAP.put("Ç", (byte) 0x09);
    DEFAULT_ENCODE_MAP.put("\n", (byte) 0x0a);
    DEFAULT_ENCODE_MAP.put("Ø", (byte) 0x0b);
    DEFAULT_ENCODE_MAP.put("ø", (byte) 0x0c);
    DEFAULT_ENCODE_MAP.put("\r", (byte) 0x0d);
    DEFAULT_ENCODE_MAP.put("Å", (byte) 0x0e);
    DEFAULT_ENCODE_MAP.put("å", (byte) 0x0f);
    DEFAULT_ENCODE_MAP.put("\u0394", (byte) 0x10);
    DEFAULT_ENCODE_MAP.put("_", (byte) 0x11);
    DEFAULT_ENCODE_MAP.put("\u03A6", (byte) 0x12);
    DEFAULT_ENCODE_MAP.put("\u0393", (byte) 0x13);
    DEFAULT_ENCODE_MAP.put("\u039B", (byte) 0x14);
    DEFAULT_ENCODE_MAP.put("\u03A9", (byte) 0x15);
    DEFAULT_ENCODE_MAP.put("\u03A0", (byte) 0x16);
    DEFAULT_ENCODE_MAP.put("\u03A8", (byte) 0x17);
    DEFAULT_ENCODE_MAP.put("\u03A3", (byte) 0x18);
    DEFAULT_ENCODE_MAP.put("\u0398", (byte) 0x19);
    DEFAULT_ENCODE_MAP.put("\u039E", (byte) 0x1a);
    // 27 is Escape character
    DEFAULT_ENCODE_MAP.put("\u001B", (byte) 0x1b);
    DEFAULT_ENCODE_MAP.put("Æ", (byte) 0x1c);
    DEFAULT_ENCODE_MAP.put("æ", (byte) 0x1d);
    DEFAULT_ENCODE_MAP.put("ß", (byte) 0x1e);
    DEFAULT_ENCODE_MAP.put("É", (byte) 0x1f);
    DEFAULT_ENCODE_MAP.put("\u0020", (byte) 0x20);
    DEFAULT_ENCODE_MAP.put("!", (byte) 0x21);
    DEFAULT_ENCODE_MAP.put("\"", (byte) 0x22);
    DEFAULT_ENCODE_MAP.put("#", (byte) 0x23);
    DEFAULT_ENCODE_MAP.put("¤", (byte) 0x24);
    DEFAULT_ENCODE_MAP.put("%", (byte) 0x25);
    DEFAULT_ENCODE_MAP.put("&", (byte) 0x26);
    DEFAULT_ENCODE_MAP.put("'", (byte) 0x27);
    DEFAULT_ENCODE_MAP.put("(", (byte) 0x28);
    DEFAULT_ENCODE_MAP.put(")", (byte) 0x29);
    DEFAULT_ENCODE_MAP.put("*", (byte) 0x2a);
    DEFAULT_ENCODE_MAP.put("+", (byte) 0x2b);
    DEFAULT_ENCODE_MAP.put(",", (byte) 0x2c);
    DEFAULT_ENCODE_MAP.put("-", (byte) 0x2d);
    DEFAULT_ENCODE_MAP.put(".", (byte) 0x2e);
    DEFAULT_ENCODE_MAP.put("/", (byte) 0x2f);
    DEFAULT_ENCODE_MAP.put("0", (byte) 0x30);
    DEFAULT_ENCODE_MAP.put("1", (byte) 0x31);
    DEFAULT_ENCODE_MAP.put("2", (byte) 0x32);
    DEFAULT_ENCODE_MAP.put("3", (byte) 0x33);
    DEFAULT_ENCODE_MAP.put("4", (byte) 0x34);
    DEFAULT_ENCODE_MAP.put("5", (byte) 0x35);
    DEFAULT_ENCODE_MAP.put("6", (byte) 0x36);
    DEFAULT_ENCODE_MAP.put("7", (byte) 0x37);
    DEFAULT_ENCODE_MAP.put("8", (byte) 0x38);
    DEFAULT_ENCODE_MAP.put("9", (byte) 0x39);
    DEFAULT_ENCODE_MAP.put(":", (byte) 0x3a);
    DEFAULT_ENCODE_MAP.put(";", (byte) 0x3b);
    DEFAULT_ENCODE_MAP.put("<", (byte) 0x3c);
    DEFAULT_ENCODE_MAP.put("=", (byte) 0x3d);
    DEFAULT_ENCODE_MAP.put(">", (byte) 0x3e);
    DEFAULT_ENCODE_MAP.put("?", (byte) 0x3f);
    DEFAULT_ENCODE_MAP.put("¡", (byte) 0x40);
    DEFAULT_ENCODE_MAP.put("A", (byte) 0x41);
    DEFAULT_ENCODE_MAP.put("B", (byte) 0x42);
    DEFAULT_ENCODE_MAP.put("C", (byte) 0x43);
    DEFAULT_ENCODE_MAP.put("D", (byte) 0x44);
    DEFAULT_ENCODE_MAP.put("E", (byte) 0x45);
    DEFAULT_ENCODE_MAP.put("F", (byte) 0x46);
    DEFAULT_ENCODE_MAP.put("G", (byte) 0x47);
    DEFAULT_ENCODE_MAP.put("H", (byte) 0x48);
    DEFAULT_ENCODE_MAP.put("I", (byte) 0x49);
    DEFAULT_ENCODE_MAP.put("J", (byte) 0x4a);
    DEFAULT_ENCODE_MAP.put("K", (byte) 0x4b);
    DEFAULT_ENCODE_MAP.put("L", (byte) 0x4c);
    DEFAULT_ENCODE_MAP.put("M", (byte) 0x4d);
    DEFAULT_ENCODE_MAP.put("N", (byte) 0x4e);
    DEFAULT_ENCODE_MAP.put("O", (byte) 0x4f);
    DEFAULT_ENCODE_MAP.put("P", (byte) 0x50);
    DEFAULT_ENCODE_MAP.put("Q", (byte) 0x51);
    DEFAULT_ENCODE_MAP.put("R", (byte) 0x52);
    DEFAULT_ENCODE_MAP.put("S", (byte) 0x53);
    DEFAULT_ENCODE_MAP.put("T", (byte) 0x54);
    DEFAULT_ENCODE_MAP.put("U", (byte) 0x55);
    DEFAULT_ENCODE_MAP.put("V", (byte) 0x56);
    DEFAULT_ENCODE_MAP.put("W", (byte) 0x57);
    DEFAULT_ENCODE_MAP.put("X", (byte) 0x58);
    DEFAULT_ENCODE_MAP.put("Y", (byte) 0x59);
    DEFAULT_ENCODE_MAP.put("Z", (byte) 0x5a);
    DEFAULT_ENCODE_MAP.put("Ä", (byte) 0x5b);
    DEFAULT_ENCODE_MAP.put("Ö", (byte) 0x5c);
    DEFAULT_ENCODE_MAP.put("Ñ", (byte) 0x5d);
    DEFAULT_ENCODE_MAP.put("Ü", (byte) 0x5e);
    DEFAULT_ENCODE_MAP.put("§", (byte) 0x5f);
    DEFAULT_ENCODE_MAP.put("¿", (byte) 0x60);
    DEFAULT_ENCODE_MAP.put("a", (byte) 0x61);
    DEFAULT_ENCODE_MAP.put("b", (byte) 0x62);
    DEFAULT_ENCODE_MAP.put("c", (byte) 0x63);
    DEFAULT_ENCODE_MAP.put("d", (byte) 0x64);
    DEFAULT_ENCODE_MAP.put("e", (byte) 0x65);
    DEFAULT_ENCODE_MAP.put("f", (byte) 0x66);
    DEFAULT_ENCODE_MAP.put("g", (byte) 0x67);
    DEFAULT_ENCODE_MAP.put("h", (byte) 0x68);
    DEFAULT_ENCODE_MAP.put("i", (byte) 0x69);
    DEFAULT_ENCODE_MAP.put("j", (byte) 0x6a);
    DEFAULT_ENCODE_MAP.put("k", (byte) 0x6b);
    DEFAULT_ENCODE_MAP.put("l", (byte) 0x6c);
    DEFAULT_ENCODE_MAP.put("m", (byte) 0x6d);
    DEFAULT_ENCODE_MAP.put("n", (byte) 0x6e);
    DEFAULT_ENCODE_MAP.put("o", (byte) 0x6f);
    DEFAULT_ENCODE_MAP.put("p", (byte) 0x70);
    DEFAULT_ENCODE_MAP.put("q", (byte) 0x71);
    DEFAULT_ENCODE_MAP.put("r", (byte) 0x72);
    DEFAULT_ENCODE_MAP.put("s", (byte) 0x73);
    DEFAULT_ENCODE_MAP.put("t", (byte) 0x74);
    DEFAULT_ENCODE_MAP.put("u", (byte) 0x75);
    DEFAULT_ENCODE_MAP.put("v", (byte) 0x76);
    DEFAULT_ENCODE_MAP.put("w", (byte) 0x77);
    DEFAULT_ENCODE_MAP.put("x", (byte) 0x78);
    DEFAULT_ENCODE_MAP.put("y", (byte) 0x79);
    DEFAULT_ENCODE_MAP.put("z", (byte) 0x7a);
    DEFAULT_ENCODE_MAP.put("ä", (byte) 0x7b);
    DEFAULT_ENCODE_MAP.put("ö", (byte) 0x7c);
    DEFAULT_ENCODE_MAP.put("ñ", (byte) 0x7d);
    DEFAULT_ENCODE_MAP.put("ü", (byte) 0x7e);
    DEFAULT_ENCODE_MAP.put("à", (byte) 0x7f);

    DEFAULT_DECODE_MAP.put((byte) 0x00, "@");
    DEFAULT_DECODE_MAP.put((byte) 0x01, "£");
    DEFAULT_DECODE_MAP.put((byte) 0x02, "$");
    DEFAULT_DECODE_MAP.put((byte) 0x03, "¥");
    DEFAULT_DECODE_MAP.put((byte) 0x04, "è");
    DEFAULT_DECODE_MAP.put((byte) 0x05, "é");
    DEFAULT_DECODE_MAP.put((byte) 0x06, "ù");
    DEFAULT_DECODE_MAP.put((byte) 0x07, "ì");
    DEFAULT_DECODE_MAP.put((byte) 0x08, "ò");
    DEFAULT_DECODE_MAP.put((byte) 0x09, "Ç");
    DEFAULT_DECODE_MAP.put((byte) 0x0a, "\n");
    DEFAULT_DECODE_MAP.put((byte) 0x0b, "Ø");
    DEFAULT_DECODE_MAP.put((byte) 0x0c, "ø");
    DEFAULT_DECODE_MAP.put((byte) 0x0d, "\r");
    DEFAULT_DECODE_MAP.put((byte) 0x0e, "Å");
    DEFAULT_DECODE_MAP.put((byte) 0x0f, "å");
    DEFAULT_DECODE_MAP.put((byte) 0x10, "\u0394");
    DEFAULT_DECODE_MAP.put((byte) 0x11, "_");
    DEFAULT_DECODE_MAP.put((byte) 0x12, "\u03A6");
    DEFAULT_DECODE_MAP.put((byte) 0x13, "\u0393");
    DEFAULT_DECODE_MAP.put((byte) 0x14, "\u039B");
    DEFAULT_DECODE_MAP.put((byte) 0x15, "\u03A9");
    DEFAULT_DECODE_MAP.put((byte) 0x16, "\u03A0");
    DEFAULT_DECODE_MAP.put((byte) 0x17, "\u03A8");
    DEFAULT_DECODE_MAP.put((byte) 0x18, "\u03A3");
    DEFAULT_DECODE_MAP.put((byte) 0x19, "\u0398");
    DEFAULT_DECODE_MAP.put((byte) 0x1a, "\u039E");
    // 27 is Escape character
    DEFAULT_DECODE_MAP.put((byte) 0x1b, "\u001B");
    DEFAULT_DECODE_MAP.put((byte) 0x1c, "Æ");
    DEFAULT_DECODE_MAP.put((byte) 0x1d, "æ");
    DEFAULT_DECODE_MAP.put((byte) 0x1e, "ß");
    DEFAULT_DECODE_MAP.put((byte) 0x1f, "É");
    DEFAULT_DECODE_MAP.put((byte) 0x20, "\u0020");
    DEFAULT_DECODE_MAP.put((byte) 0x21, "!");
    DEFAULT_DECODE_MAP.put((byte) 0x22, "\"");
    DEFAULT_DECODE_MAP.put((byte) 0x23, "#");
    DEFAULT_DECODE_MAP.put((byte) 0x24, "¤");
    DEFAULT_DECODE_MAP.put((byte) 0x25, "%");
    DEFAULT_DECODE_MAP.put((byte) 0x26, "&");
    DEFAULT_DECODE_MAP.put((byte) 0x27, "'");
    DEFAULT_DECODE_MAP.put((byte) 0x28, "(");
    DEFAULT_DECODE_MAP.put((byte) 0x29, ")");
    DEFAULT_DECODE_MAP.put((byte) 0x2a, "*");
    DEFAULT_DECODE_MAP.put((byte) 0x2b, "+");
    DEFAULT_DECODE_MAP.put((byte) 0x2c, ",");
    DEFAULT_DECODE_MAP.put((byte) 0x2d, "-");
    DEFAULT_DECODE_MAP.put((byte) 0x2e, ".");
    DEFAULT_DECODE_MAP.put((byte) 0x2f, "/");
    DEFAULT_DECODE_MAP.put((byte) 0x30, "0");
    DEFAULT_DECODE_MAP.put((byte) 0x31, "1");
    DEFAULT_DECODE_MAP.put((byte) 0x32, "2");
    DEFAULT_DECODE_MAP.put((byte) 0x33, "3");
    DEFAULT_DECODE_MAP.put((byte) 0x34, "4");
    DEFAULT_DECODE_MAP.put((byte) 0x35, "5");
    DEFAULT_DECODE_MAP.put((byte) 0x36, "6");
    DEFAULT_DECODE_MAP.put((byte) 0x37, "7");
    DEFAULT_DECODE_MAP.put((byte) 0x38, "8");
    DEFAULT_DECODE_MAP.put((byte) 0x39, "9");
    DEFAULT_DECODE_MAP.put((byte) 0x3a, ":");
    DEFAULT_DECODE_MAP.put((byte) 0x3b, ";");
    DEFAULT_DECODE_MAP.put((byte) 0x3c, "<");
    DEFAULT_DECODE_MAP.put((byte) 0x3d, "=");
    DEFAULT_DECODE_MAP.put((byte) 0x3e, ">");
    DEFAULT_DECODE_MAP.put((byte) 0x3f, "?");
    DEFAULT_DECODE_MAP.put((byte) 0x40, "¡");
    DEFAULT_DECODE_MAP.put((byte) 0x41, "A");
    DEFAULT_DECODE_MAP.put((byte) 0x42, "B");
    DEFAULT_DECODE_MAP.put((byte) 0x43, "C");
    DEFAULT_DECODE_MAP.put((byte) 0x44, "D");
    DEFAULT_DECODE_MAP.put((byte) 0x45, "E");
    DEFAULT_DECODE_MAP.put((byte) 0x46, "F");
    DEFAULT_DECODE_MAP.put((byte) 0x47, "G");
    DEFAULT_DECODE_MAP.put((byte) 0x48, "H");
    DEFAULT_DECODE_MAP.put((byte) 0x49, "I");
    DEFAULT_DECODE_MAP.put((byte) 0x4a, "J");
    DEFAULT_DECODE_MAP.put((byte) 0x4b, "K");
    DEFAULT_DECODE_MAP.put((byte) 0x4c, "L");
    DEFAULT_DECODE_MAP.put((byte) 0x4d, "M");
    DEFAULT_DECODE_MAP.put((byte) 0x4e, "N");
    DEFAULT_DECODE_MAP.put((byte) 0x4f, "O");
    DEFAULT_DECODE_MAP.put((byte) 0x50, "P");
    DEFAULT_DECODE_MAP.put((byte) 0x51, "Q");
    DEFAULT_DECODE_MAP.put((byte) 0x52, "R");
    DEFAULT_DECODE_MAP.put((byte) 0x53, "S");
    DEFAULT_DECODE_MAP.put((byte) 0x54, "T");
    DEFAULT_DECODE_MAP.put((byte) 0x55, "U");
    DEFAULT_DECODE_MAP.put((byte) 0x56, "V");
    DEFAULT_DECODE_MAP.put((byte) 0x57, "W");
    DEFAULT_DECODE_MAP.put((byte) 0x58, "X");
    DEFAULT_DECODE_MAP.put((byte) 0x59, "Y");
    DEFAULT_DECODE_MAP.put((byte) 0x5a, "Z");
    DEFAULT_DECODE_MAP.put((byte) 0x5b, "Ä");
    DEFAULT_DECODE_MAP.put((byte) 0x5c, "Ö");
    DEFAULT_DECODE_MAP.put((byte) 0x5d, "Ñ");
    DEFAULT_DECODE_MAP.put((byte) 0x5e, "Ü");
    DEFAULT_DECODE_MAP.put((byte) 0x5f, "§");
    DEFAULT_DECODE_MAP.put((byte) 0x60, "¿");
    DEFAULT_DECODE_MAP.put((byte) 0x61, "a");
    DEFAULT_DECODE_MAP.put((byte) 0x62, "b");
    DEFAULT_DECODE_MAP.put((byte) 0x63, "c");
    DEFAULT_DECODE_MAP.put((byte) 0x64, "d");
    DEFAULT_DECODE_MAP.put((byte) 0x65, "e");
    DEFAULT_DECODE_MAP.put((byte) 0x66, "f");
    DEFAULT_DECODE_MAP.put((byte) 0x67, "g");
    DEFAULT_DECODE_MAP.put((byte) 0x68, "h");
    DEFAULT_DECODE_MAP.put((byte) 0x69, "i");
    DEFAULT_DECODE_MAP.put((byte) 0x6a, "j");
    DEFAULT_DECODE_MAP.put((byte) 0x6b, "k");
    DEFAULT_DECODE_MAP.put((byte) 0x6c, "l");
    DEFAULT_DECODE_MAP.put((byte) 0x6d, "m");
    DEFAULT_DECODE_MAP.put((byte) 0x6e, "n");
    DEFAULT_DECODE_MAP.put((byte) 0x6f, "o");
    DEFAULT_DECODE_MAP.put((byte) 0x70, "p");
    DEFAULT_DECODE_MAP.put((byte) 0x71, "q");
    DEFAULT_DECODE_MAP.put((byte) 0x72, "r");
    DEFAULT_DECODE_MAP.put((byte) 0x73, "s");
    DEFAULT_DECODE_MAP.put((byte) 0x74, "t");
    DEFAULT_DECODE_MAP.put((byte) 0x75, "u");
    DEFAULT_DECODE_MAP.put((byte) 0x76, "v");
    DEFAULT_DECODE_MAP.put((byte) 0x77, "w");
    DEFAULT_DECODE_MAP.put((byte) 0x78, "x");
    DEFAULT_DECODE_MAP.put((byte) 0x79, "y");
    DEFAULT_DECODE_MAP.put((byte) 0x7a, "z");
    DEFAULT_DECODE_MAP.put((byte) 0x7b, "ä");
    DEFAULT_DECODE_MAP.put((byte) 0x7c, "ö");
    DEFAULT_DECODE_MAP.put((byte) 0x7d, "ñ");
    DEFAULT_DECODE_MAP.put((byte) 0x7e, "ü");
    DEFAULT_DECODE_MAP.put((byte) 0x7f, "à");

    // extended alphabet
    EXT_ENCODE_MAP.put("\n", (byte) 0x0a);
    EXT_ENCODE_MAP.put("^", (byte) 0x14);
    // reserved for future extensions
    EXT_ENCODE_MAP.put(" ", (byte) 0x1b);
    EXT_ENCODE_MAP.put("{", (byte) 0x28);
    EXT_ENCODE_MAP.put("}", (byte) 0x29);
    EXT_ENCODE_MAP.put("\\", (byte) 0x2f);
    EXT_ENCODE_MAP.put("[", (byte) 0x3c);
    EXT_ENCODE_MAP.put("~", (byte) 0x3d);
    EXT_ENCODE_MAP.put("]", (byte) 0x3e);
    EXT_ENCODE_MAP.put("|", (byte) 0x40);
    EXT_ENCODE_MAP.put("€", (byte) 0x65);

    EXT_DECODE_MAP.put((byte) 0x0a, "\n");
    EXT_DECODE_MAP.put((byte) 0x14, "^");
    // reserved for future extensions
    EXT_DECODE_MAP.put((byte) 0x1b, " ");
    EXT_DECODE_MAP.put((byte) 0x28, "{");
    EXT_DECODE_MAP.put((byte) 0x29, "}");
    EXT_DECODE_MAP.put((byte) 0x2f, "\\");
    EXT_DECODE_MAP.put((byte) 0x3c, "[");
    EXT_DECODE_MAP.put((byte) 0x3d, "~");
    EXT_DECODE_MAP.put((byte) 0x3e, "]");
    EXT_DECODE_MAP.put((byte) 0x40, "|");
    EXT_DECODE_MAP.put((byte) 0x65, "€");
  }

  /**
   * Constructor for the Gsm7Bit charset.  Call the superclass
   * constructor to pass along the name(s) we'll be known by.
   * Then save a reference to the delegate Charset.
   */
  protected Gsm7BitCharset(String canonical, String[] aliases) {
    super(canonical, aliases);
  }

  // ----------------------------------------------------------

  /**
   * Called by users of this Charset to obtain an encoder.
   * This implementation instantiates an instance of a private class
   * (defined below) and passes it an encoder from the base Charset.
   */
  @Override
  public CharsetEncoder newEncoder() {
    return new Gsm7BitEncoder(this);
  }

  /**
   * Called by users of this Charset to obtain a decoder.
   * This implementation instantiates an instance of a private class
   * (defined below) and passes it a decoder from the base Charset.
   */
  @Override
  public CharsetDecoder newDecoder() {
    return new Gsm7BitDecoder(this);
  }

  /**
   * This method must be implemented by concrete Charsets.  We always
   * say no, which is safe.
   */
  @Override
  public boolean contains(Charset cs) {
    return (false);
  }

  /**
   * The encoder implementation for the Gsm7Bit Charset.
   * This class, and the matching decoder class below, should also
   * override the "impl" methods, such as implOnMalformedInput() and
   * make passthrough calls to the baseEncoder object.  That is left
   * as an exercise for the hacker.
   */
  private class Gsm7BitEncoder extends CharsetEncoder {

    /**
     * Constructor, call the superclass constructor with the
     * Charset object and the encodings sizes from the
     * delegate encoder.
     */
    Gsm7BitEncoder(Charset cs) {
      super(cs, 1, 2);
    }

    /**
     * Implementation of the encoding loop.
     */
    @Override
    protected CoderResult encodeLoop(CharBuffer cb, ByteBuffer bb) {
      CoderResult cr = CoderResult.UNDERFLOW;

      while (cb.hasRemaining()) {
        if (!bb.hasRemaining()) {
          cr = CoderResult.OVERFLOW;
          break;
        }
        char ch = cb.get();

        // first check the default alphabet
        Byte b = DEFAULT_ENCODE_MAP.get("" + ch);
        if (b != null) {
          bb.put(b);
        } else {
          // check extended alphabet
          b = EXT_ENCODE_MAP.get("" + ch);
          if (b != null) {
            // since the extended character set takes two bytes
            // we have to check that there is enough space left
            if (bb.remaining() < 2) {
              // go back one step
              cb.position(cb.position() - 1);
              cr = CoderResult.OVERFLOW;
              break;
            }
            // all ok, add it to the buffer
            bb.put((byte) 0x1b);
            bb.put(b);
          } else {
            // no match found, send a ?
            b = 0x3F;
            bb.put(b);
          }
        }
      }
      return cr;
    }
  }

  // --------------------------------------------------------

  /**
   * The decoder implementation for the Gsm 7Bit Charset.
   */
  private class Gsm7BitDecoder extends CharsetDecoder {

    /**
     * Constructor, call the superclass constructor with the
     * Charset object and pass alon the chars/byte values
     * from the delegate decoder.
     */
    Gsm7BitDecoder(Charset cs) {
      super(cs, 1, 1);
    }

    /**
     * Implementation of the decoding loop.
     */
    @Override
    protected CoderResult decodeLoop(ByteBuffer bb, CharBuffer cb) {
      CoderResult cr = CoderResult.UNDERFLOW;

      while (bb.hasRemaining()) {
        if (!cb.hasRemaining()) {
          cr = CoderResult.OVERFLOW;
          break;
        }
        byte b = bb.get();

        // first check the default alphabet
        String s = DEFAULT_DECODE_MAP.get(b);
        if (s != null) {
          char ch = s.charAt(0);
          if (ch != '\u001B') {
            cb.put(ch);
          } else {
            // check the extended alphabet
            if (bb.hasRemaining()) {
              b = bb.get();
              s = EXT_DECODE_MAP.get(b);
              if (s != null) {
                ch = s.charAt(0);
                cb.put(ch);
              } else {
                cb.put('?');
              }
            }
          }
        } else {
          cb.put('?');
        }
      }
      return cr;
    }
  }
}

/*
 * $Log: not supported by cvs2svn $
 * Revision 1.2  2006/03/09 16:24:14  sverkera
 * Removed compiler and javadoc warnings
 *
 * Revision 1.1  2003/09/30 09:02:09  sverkera
 * Added implementation for GSM 7Bit charset
 *
 */