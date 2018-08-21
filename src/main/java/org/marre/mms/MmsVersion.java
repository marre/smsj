package org.marre.mms;

/**
 * Created by hanwen on 2018/8/21.
 */
public enum MmsVersion {
  /**
   * mms version 1.0
   */
  VERSION_1_0((byte) 0x10),
  /**
   * mms version 1.1
   */
  VERSION_1_1((byte) 0x11),
  /**
   * mms version 1.2
   */
  VERSION_1_2((byte) 0x12),
  /**
   * mms version 1.3
   */
  VERSION_1_3((byte) 0x13);

  private final byte value;

  MmsVersion(byte value) {
    this.value = value;
  }

  public byte getValue() {
    return value;
  }
}
