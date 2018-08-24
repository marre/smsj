package org.marre.sms.charset;

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
  private static Map<String, Byte> defaultEncodeMap = new HashMap<>();
  private static Map<Byte, String> defaultDecodeMap = new HashMap<>();
  private static Map<String, Byte> extEncodeMap = new HashMap<>();
  private static Map<Byte, String> extDecodeMap = new HashMap<>();


  // static section that populates the encode and decode HashMap objects
  static {
    // default alphabet
    defaultEncodeMap.put("@", (byte) 0x00);
    defaultEncodeMap.put("£", (byte) 0x01);
    defaultEncodeMap.put("$", (byte) 0x02);
    defaultEncodeMap.put("¥", (byte) 0x03);
    defaultEncodeMap.put("è", (byte) 0x04);
    defaultEncodeMap.put("é", (byte) 0x05);
    defaultEncodeMap.put("ù", (byte) 0x06);
    defaultEncodeMap.put("ì", (byte) 0x07);
    defaultEncodeMap.put("ò", (byte) 0x08);
    defaultEncodeMap.put("Ç", (byte) 0x09);
    defaultEncodeMap.put("\n", (byte) 0x0a);
    defaultEncodeMap.put("Ø", (byte) 0x0b);
    defaultEncodeMap.put("ø", (byte) 0x0c);
    defaultEncodeMap.put("\r", (byte) 0x0d);
    defaultEncodeMap.put("Å", (byte) 0x0e);
    defaultEncodeMap.put("å", (byte) 0x0f);
    defaultEncodeMap.put("\u0394", (byte) 0x10);
    defaultEncodeMap.put("_", (byte) 0x11);
    defaultEncodeMap.put("\u03A6", (byte) 0x12);
    defaultEncodeMap.put("\u0393", (byte) 0x13);
    defaultEncodeMap.put("\u039B", (byte) 0x14);
    defaultEncodeMap.put("\u03A9", (byte) 0x15);
    defaultEncodeMap.put("\u03A0", (byte) 0x16);
    defaultEncodeMap.put("\u03A8", (byte) 0x17);
    defaultEncodeMap.put("\u03A3", (byte) 0x18);
    defaultEncodeMap.put("\u0398", (byte) 0x19);
    defaultEncodeMap.put("\u039E", (byte) 0x1a);
    // 27 is Escape character
    defaultEncodeMap.put("\u001B", (byte) 0x1b);
    defaultEncodeMap.put("Æ", (byte) 0x1c);
    defaultEncodeMap.put("æ", (byte) 0x1d);
    defaultEncodeMap.put("ß", (byte) 0x1e);
    defaultEncodeMap.put("É", (byte) 0x1f);
    defaultEncodeMap.put("\u0020", (byte) 0x20);
    defaultEncodeMap.put("!", (byte) 0x21);
    defaultEncodeMap.put("\"", (byte) 0x22);
    defaultEncodeMap.put("#", (byte) 0x23);
    defaultEncodeMap.put("¤", (byte) 0x24);
    defaultEncodeMap.put("%", (byte) 0x25);
    defaultEncodeMap.put("&", (byte) 0x26);
    defaultEncodeMap.put("'", (byte) 0x27);
    defaultEncodeMap.put("(", (byte) 0x28);
    defaultEncodeMap.put(")", (byte) 0x29);
    defaultEncodeMap.put("*", (byte) 0x2a);
    defaultEncodeMap.put("+", (byte) 0x2b);
    defaultEncodeMap.put(",", (byte) 0x2c);
    defaultEncodeMap.put("-", (byte) 0x2d);
    defaultEncodeMap.put(".", (byte) 0x2e);
    defaultEncodeMap.put("/", (byte) 0x2f);
    defaultEncodeMap.put("0", (byte) 0x30);
    defaultEncodeMap.put("1", (byte) 0x31);
    defaultEncodeMap.put("2", (byte) 0x32);
    defaultEncodeMap.put("3", (byte) 0x33);
    defaultEncodeMap.put("4", (byte) 0x34);
    defaultEncodeMap.put("5", (byte) 0x35);
    defaultEncodeMap.put("6", (byte) 0x36);
    defaultEncodeMap.put("7", (byte) 0x37);
    defaultEncodeMap.put("8", (byte) 0x38);
    defaultEncodeMap.put("9", (byte) 0x39);
    defaultEncodeMap.put(":", (byte) 0x3a);
    defaultEncodeMap.put(";", (byte) 0x3b);
    defaultEncodeMap.put("<", (byte) 0x3c);
    defaultEncodeMap.put("=", (byte) 0x3d);
    defaultEncodeMap.put(">", (byte) 0x3e);
    defaultEncodeMap.put("?", (byte) 0x3f);
    defaultEncodeMap.put("¡", (byte) 0x40);
    defaultEncodeMap.put("A", (byte) 0x41);
    defaultEncodeMap.put("B", (byte) 0x42);
    defaultEncodeMap.put("C", (byte) 0x43);
    defaultEncodeMap.put("D", (byte) 0x44);
    defaultEncodeMap.put("E", (byte) 0x45);
    defaultEncodeMap.put("F", (byte) 0x46);
    defaultEncodeMap.put("G", (byte) 0x47);
    defaultEncodeMap.put("H", (byte) 0x48);
    defaultEncodeMap.put("I", (byte) 0x49);
    defaultEncodeMap.put("J", (byte) 0x4a);
    defaultEncodeMap.put("K", (byte) 0x4b);
    defaultEncodeMap.put("L", (byte) 0x4c);
    defaultEncodeMap.put("M", (byte) 0x4d);
    defaultEncodeMap.put("N", (byte) 0x4e);
    defaultEncodeMap.put("O", (byte) 0x4f);
    defaultEncodeMap.put("P", (byte) 0x50);
    defaultEncodeMap.put("Q", (byte) 0x51);
    defaultEncodeMap.put("R", (byte) 0x52);
    defaultEncodeMap.put("S", (byte) 0x53);
    defaultEncodeMap.put("T", (byte) 0x54);
    defaultEncodeMap.put("U", (byte) 0x55);
    defaultEncodeMap.put("V", (byte) 0x56);
    defaultEncodeMap.put("W", (byte) 0x57);
    defaultEncodeMap.put("X", (byte) 0x58);
    defaultEncodeMap.put("Y", (byte) 0x59);
    defaultEncodeMap.put("Z", (byte) 0x5a);
    defaultEncodeMap.put("Ä", (byte) 0x5b);
    defaultEncodeMap.put("Ö", (byte) 0x5c);
    defaultEncodeMap.put("Ñ", (byte) 0x5d);
    defaultEncodeMap.put("Ü", (byte) 0x5e);
    defaultEncodeMap.put("§", (byte) 0x5f);
    defaultEncodeMap.put("¿", (byte) 0x60);
    defaultEncodeMap.put("a", (byte) 0x61);
    defaultEncodeMap.put("b", (byte) 0x62);
    defaultEncodeMap.put("c", (byte) 0x63);
    defaultEncodeMap.put("d", (byte) 0x64);
    defaultEncodeMap.put("e", (byte) 0x65);
    defaultEncodeMap.put("f", (byte) 0x66);
    defaultEncodeMap.put("g", (byte) 0x67);
    defaultEncodeMap.put("h", (byte) 0x68);
    defaultEncodeMap.put("i", (byte) 0x69);
    defaultEncodeMap.put("j", (byte) 0x6a);
    defaultEncodeMap.put("k", (byte) 0x6b);
    defaultEncodeMap.put("l", (byte) 0x6c);
    defaultEncodeMap.put("m", (byte) 0x6d);
    defaultEncodeMap.put("n", (byte) 0x6e);
    defaultEncodeMap.put("o", (byte) 0x6f);
    defaultEncodeMap.put("p", (byte) 0x70);
    defaultEncodeMap.put("q", (byte) 0x71);
    defaultEncodeMap.put("r", (byte) 0x72);
    defaultEncodeMap.put("s", (byte) 0x73);
    defaultEncodeMap.put("t", (byte) 0x74);
    defaultEncodeMap.put("u", (byte) 0x75);
    defaultEncodeMap.put("v", (byte) 0x76);
    defaultEncodeMap.put("w", (byte) 0x77);
    defaultEncodeMap.put("x", (byte) 0x78);
    defaultEncodeMap.put("y", (byte) 0x79);
    defaultEncodeMap.put("z", (byte) 0x7a);
    defaultEncodeMap.put("ä", (byte) 0x7b);
    defaultEncodeMap.put("ö", (byte) 0x7c);
    defaultEncodeMap.put("ñ", (byte) 0x7d);
    defaultEncodeMap.put("ü", (byte) 0x7e);
    defaultEncodeMap.put("à", (byte) 0x7f);

    defaultDecodeMap.put((byte) 0x00, "@");
    defaultDecodeMap.put((byte) 0x01, "£");
    defaultDecodeMap.put((byte) 0x02, "$");
    defaultDecodeMap.put((byte) 0x03, "¥");
    defaultDecodeMap.put((byte) 0x04, "è");
    defaultDecodeMap.put((byte) 0x05, "é");
    defaultDecodeMap.put((byte) 0x06, "ù");
    defaultDecodeMap.put((byte) 0x07, "ì");
    defaultDecodeMap.put((byte) 0x08, "ò");
    defaultDecodeMap.put((byte) 0x09, "Ç");
    defaultDecodeMap.put((byte) 0x0a, "\n");
    defaultDecodeMap.put((byte) 0x0b, "Ø");
    defaultDecodeMap.put((byte) 0x0c, "ø");
    defaultDecodeMap.put((byte) 0x0d, "\r");
    defaultDecodeMap.put((byte) 0x0e, "Å");
    defaultDecodeMap.put((byte) 0x0f, "å");
    defaultDecodeMap.put((byte) 0x10, "\u0394");
    defaultDecodeMap.put((byte) 0x11, "_");
    defaultDecodeMap.put((byte) 0x12, "\u03A6");
    defaultDecodeMap.put((byte) 0x13, "\u0393");
    defaultDecodeMap.put((byte) 0x14, "\u039B");
    defaultDecodeMap.put((byte) 0x15, "\u03A9");
    defaultDecodeMap.put((byte) 0x16, "\u03A0");
    defaultDecodeMap.put((byte) 0x17, "\u03A8");
    defaultDecodeMap.put((byte) 0x18, "\u03A3");
    defaultDecodeMap.put((byte) 0x19, "\u0398");
    defaultDecodeMap.put((byte) 0x1a, "\u039E");
    // 27 is Escape character
    defaultDecodeMap.put((byte) 0x1b, "\u001B");
    defaultDecodeMap.put((byte) 0x1c, "Æ");
    defaultDecodeMap.put((byte) 0x1d, "æ");
    defaultDecodeMap.put((byte) 0x1e, "ß");
    defaultDecodeMap.put((byte) 0x1f, "É");
    defaultDecodeMap.put((byte) 0x20, "\u0020");
    defaultDecodeMap.put((byte) 0x21, "!");
    defaultDecodeMap.put((byte) 0x22, "\"");
    defaultDecodeMap.put((byte) 0x23, "#");
    defaultDecodeMap.put((byte) 0x24, "¤");
    defaultDecodeMap.put((byte) 0x25, "%");
    defaultDecodeMap.put((byte) 0x26, "&");
    defaultDecodeMap.put((byte) 0x27, "'");
    defaultDecodeMap.put((byte) 0x28, "(");
    defaultDecodeMap.put((byte) 0x29, ")");
    defaultDecodeMap.put((byte) 0x2a, "*");
    defaultDecodeMap.put((byte) 0x2b, "+");
    defaultDecodeMap.put((byte) 0x2c, ",");
    defaultDecodeMap.put((byte) 0x2d, "-");
    defaultDecodeMap.put((byte) 0x2e, ".");
    defaultDecodeMap.put((byte) 0x2f, "/");
    defaultDecodeMap.put((byte) 0x30, "0");
    defaultDecodeMap.put((byte) 0x31, "1");
    defaultDecodeMap.put((byte) 0x32, "2");
    defaultDecodeMap.put((byte) 0x33, "3");
    defaultDecodeMap.put((byte) 0x34, "4");
    defaultDecodeMap.put((byte) 0x35, "5");
    defaultDecodeMap.put((byte) 0x36, "6");
    defaultDecodeMap.put((byte) 0x37, "7");
    defaultDecodeMap.put((byte) 0x38, "8");
    defaultDecodeMap.put((byte) 0x39, "9");
    defaultDecodeMap.put((byte) 0x3a, ":");
    defaultDecodeMap.put((byte) 0x3b, ";");
    defaultDecodeMap.put((byte) 0x3c, "<");
    defaultDecodeMap.put((byte) 0x3d, "=");
    defaultDecodeMap.put((byte) 0x3e, ">");
    defaultDecodeMap.put((byte) 0x3f, "?");
    defaultDecodeMap.put((byte) 0x40, "¡");
    defaultDecodeMap.put((byte) 0x41, "A");
    defaultDecodeMap.put((byte) 0x42, "B");
    defaultDecodeMap.put((byte) 0x43, "C");
    defaultDecodeMap.put((byte) 0x44, "D");
    defaultDecodeMap.put((byte) 0x45, "E");
    defaultDecodeMap.put((byte) 0x46, "F");
    defaultDecodeMap.put((byte) 0x47, "G");
    defaultDecodeMap.put((byte) 0x48, "H");
    defaultDecodeMap.put((byte) 0x49, "I");
    defaultDecodeMap.put((byte) 0x4a, "J");
    defaultDecodeMap.put((byte) 0x4b, "K");
    defaultDecodeMap.put((byte) 0x4c, "L");
    defaultDecodeMap.put((byte) 0x4d, "M");
    defaultDecodeMap.put((byte) 0x4e, "N");
    defaultDecodeMap.put((byte) 0x4f, "O");
    defaultDecodeMap.put((byte) 0x50, "P");
    defaultDecodeMap.put((byte) 0x51, "Q");
    defaultDecodeMap.put((byte) 0x52, "R");
    defaultDecodeMap.put((byte) 0x53, "S");
    defaultDecodeMap.put((byte) 0x54, "T");
    defaultDecodeMap.put((byte) 0x55, "U");
    defaultDecodeMap.put((byte) 0x56, "V");
    defaultDecodeMap.put((byte) 0x57, "W");
    defaultDecodeMap.put((byte) 0x58, "X");
    defaultDecodeMap.put((byte) 0x59, "Y");
    defaultDecodeMap.put((byte) 0x5a, "Z");
    defaultDecodeMap.put((byte) 0x5b, "Ä");
    defaultDecodeMap.put((byte) 0x5c, "Ö");
    defaultDecodeMap.put((byte) 0x5d, "Ñ");
    defaultDecodeMap.put((byte) 0x5e, "Ü");
    defaultDecodeMap.put((byte) 0x5f, "§");
    defaultDecodeMap.put((byte) 0x60, "¿");
    defaultDecodeMap.put((byte) 0x61, "a");
    defaultDecodeMap.put((byte) 0x62, "b");
    defaultDecodeMap.put((byte) 0x63, "c");
    defaultDecodeMap.put((byte) 0x64, "d");
    defaultDecodeMap.put((byte) 0x65, "e");
    defaultDecodeMap.put((byte) 0x66, "f");
    defaultDecodeMap.put((byte) 0x67, "g");
    defaultDecodeMap.put((byte) 0x68, "h");
    defaultDecodeMap.put((byte) 0x69, "i");
    defaultDecodeMap.put((byte) 0x6a, "j");
    defaultDecodeMap.put((byte) 0x6b, "k");
    defaultDecodeMap.put((byte) 0x6c, "l");
    defaultDecodeMap.put((byte) 0x6d, "m");
    defaultDecodeMap.put((byte) 0x6e, "n");
    defaultDecodeMap.put((byte) 0x6f, "o");
    defaultDecodeMap.put((byte) 0x70, "p");
    defaultDecodeMap.put((byte) 0x71, "q");
    defaultDecodeMap.put((byte) 0x72, "r");
    defaultDecodeMap.put((byte) 0x73, "s");
    defaultDecodeMap.put((byte) 0x74, "t");
    defaultDecodeMap.put((byte) 0x75, "u");
    defaultDecodeMap.put((byte) 0x76, "v");
    defaultDecodeMap.put((byte) 0x77, "w");
    defaultDecodeMap.put((byte) 0x78, "x");
    defaultDecodeMap.put((byte) 0x79, "y");
    defaultDecodeMap.put((byte) 0x7a, "z");
    defaultDecodeMap.put((byte) 0x7b, "ä");
    defaultDecodeMap.put((byte) 0x7c, "ö");
    defaultDecodeMap.put((byte) 0x7d, "ñ");
    defaultDecodeMap.put((byte) 0x7e, "ü");
    defaultDecodeMap.put((byte) 0x7f, "à");

    // extended alphabet
    extEncodeMap.put("\n", (byte) 0x0a);
    extEncodeMap.put("^", (byte) 0x14);
    // reserved for future extensions
    extEncodeMap.put(" ", (byte) 0x1b);
    extEncodeMap.put("{", (byte) 0x28);
    extEncodeMap.put("}", (byte) 0x29);
    extEncodeMap.put("\\", (byte) 0x2f);
    extEncodeMap.put("[", (byte) 0x3c);
    extEncodeMap.put("~", (byte) 0x3d);
    extEncodeMap.put("]", (byte) 0x3e);
    extEncodeMap.put("|", (byte) 0x40);
    extEncodeMap.put("€", (byte) 0x65);

    extDecodeMap.put((byte) 0x0a, "\n");
    extDecodeMap.put((byte) 0x14, "^");
    // reserved for future extensions
    extDecodeMap.put((byte) 0x1b, " ");
    extDecodeMap.put((byte) 0x28, "{");
    extDecodeMap.put((byte) 0x29, "}");
    extDecodeMap.put((byte) 0x2f, "\\");
    extDecodeMap.put((byte) 0x3c, "[");
    extDecodeMap.put((byte) 0x3d, "~");
    extDecodeMap.put((byte) 0x3e, "]");
    extDecodeMap.put((byte) 0x40, "|");
    extDecodeMap.put((byte) 0x65, "€");
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
        Byte b = defaultEncodeMap.get("" + ch);
        if (b != null) {
          bb.put(b);
        } else {
          // check extended alphabet
          b = extEncodeMap.get("" + ch);
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
        String s = defaultDecodeMap.get(b);
        if (s != null) {
          char ch = s.charAt(0);
          if (ch != '\u001B') {
            cb.put(ch);
          } else {
            // check the extended alphabet
            if (bb.hasRemaining()) {
              b = bb.get();
              s = extDecodeMap.get(b);
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