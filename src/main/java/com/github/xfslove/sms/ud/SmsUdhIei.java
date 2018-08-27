package com.github.xfslove.sms.ud;

/**
 * https://en.wikipedia.org/wiki/User_Data_Header
 * <p>
 * Collection of known SMS UDH Identity Element Identifier.
 */
public enum SmsUdhIei {
  /**
   * Concatenated short messages, 8-bit reference number.
   */
  CONCATENATED_8BIT((byte) 0x00),
  /**
   * Special SMS Message Indication.
   */
  SPECIAL_MESSAGE((byte) 0x01),
  /**
   * Application port addressing scheme, 8 bit address.
   */
  APP_PORT_8BIT((byte) 0x04),
  /**
   * Application port addressing scheme, 16 bit address.
   */
  APP_PORT_16BIT((byte) 0x05),
  /**
   * SMSC Control Parameters.
   */
  SMSC_CONTROL_PARAMS((byte) 0x06),
  /**
   * UDH Source Indicator.
   */
  UDH_SOURCE_INDICATOR((byte) 0x07),
  /**
   * Concatenated short message, 16-bit reference number.
   */
  CONCATENATED_16BIT((byte) 0x08),
  /**
   * Wireless Control Message Protocol.
   */
  WCMP((byte) 0x09),
  /**
   * RFC 822 E-Mail Header.
   */
  RFC822_EMAIL_HEADER((byte) 0x20),
  /**
   * Hyperlink format element.
   */
  HYPERLINK_FORMAT((byte) 0x21),
  /**
   * Reply Address Element
   */
  REPLY_ADDRESS((byte) 0x22),
  /**
   * Enhanced Voice Mail Information
   */
  ENHANCED_VOICE_MAIL((byte) 0x23),
  /**
   * National Language Single Shift
   */
  NATIONAL_LANGUAGE_SINGLE_SHIFT((byte) 0x24),
  /**
   * National Language Locking Shift
   */
  NATIONAL_LANGUAGE_LOCKING_SHIFT((byte) 0x25);

  private final byte value;

  SmsUdhIei(byte value) {
    this.value = value;
  }

  /**
   * Convert a UDH IEI value into an SmsUdhIei object.
   *
   * @param value The UDH IEI value as specified in the GSM spec.
   * @return one of the statically defined SmsNpi or a new SmsNpi if unknown.
   */
  public static SmsUdhIei parse(byte value) {
    switch (value) {
      case 0x00:
        return CONCATENATED_8BIT;
      case 0x01:
        return SPECIAL_MESSAGE;
      case 0x04:
        return APP_PORT_8BIT;
      case 0x05:
        return APP_PORT_16BIT;
      case 0x06:
        return SMSC_CONTROL_PARAMS;
      case 0x07:
        return UDH_SOURCE_INDICATOR;
      case 0x08:
        return CONCATENATED_16BIT;
      case 0x09:
        return WCMP;
      case 0x20:
        return RFC822_EMAIL_HEADER;
      case 0x21:
        return HYPERLINK_FORMAT;
      case 0x22:
        return REPLY_ADDRESS;
      case 0x23:
        return ENHANCED_VOICE_MAIL;
      case 0x24:
        return NATIONAL_LANGUAGE_SINGLE_SHIFT;
      case 0x25:
        return NATIONAL_LANGUAGE_LOCKING_SHIFT;
      default:
        throw new IllegalArgumentException("unsupported user data header");
    }
  }

  /**
   * Returns the UDH IEI value as specified in the GSM spec.
   */
  public byte getValue() {
    return value;
  }
}
