package org.marre.sms;

/**
 * https://en.wikipedia.org/wiki/Data_Coding_Scheme#SMS_Data_Coding_Scheme
 * <p>
 * DCS Coding Groups
 */
public enum DcsGroup {
  /**
   * DCS general data coding indication group. 0000xxxx-0011xxxx.
   */
  GENERAL_DATA_CODING,

  /**
   * DCS message waiting indication group: discard message. 1100xxxx.
   */
  MESSAGE_WAITING_DISCARD,

  /**
   * DCS message waiting indication group: store message (gsm). 1101xxxx.
   */
  MESSAGE_WAITING_STORE_GSM,
  /**
   * DCS message waiting indication group: store message (ucs2). 1110xxxx.
   */
  MESSAGE_WAITING_STORE_UCS2,
  /**
   * DCS data coding/message class: 1111xxxx.
   */
  DATA_CODING_MESSAGE,

  /**
   * DCS message marked for automatic deletion group: 0100xxxx-0111xxxx.
   */
  MARKED_FOR_AUTOMATIC_DELETION,

  /**
   * DCS reserved coding groups:  1000xxxx-1011xxxx.
   */
  RESERVED
}
