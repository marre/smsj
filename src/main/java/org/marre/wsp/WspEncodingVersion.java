package org.marre.wsp;

public enum WspEncodingVersion {
  /**
   * wsp version 1.0
   */
  VERSION_1_0(1, 0),
  /**
   * wsp version 1.1
   */
  VERSION_1_1(1, 1),
  /**
   * wsp version 1.2
   */
  VERSION_1_2(1, 2),
  /**
   * wsp version 1.3
   */
  VERSION_1_3(1, 3),
  /**
   * wsp version 1.4
   */
  VERSION_1_4(1, 4),
  /**
   * wsp version 1.5
   */
  VERSION_1_5(1, 5);

  private int major;

  private int minor;

  WspEncodingVersion(int major, int minor) {
    this.major = major;
    this.minor = minor;
  }

  public static WspEncodingVersion parse(String version) {
    switch (version) {
      case "1.0":
        return VERSION_1_0;
      case "1.1":
        return VERSION_1_1;
      case "1.2":
        return VERSION_1_2;
      case "1.3":
        return VERSION_1_3;
      case "1.4":
        return VERSION_1_4;
      case "1.5":
        return VERSION_1_5;
      default:
        throw new IllegalArgumentException("unsupported version");
    }
  }

  public String stringValue() {
    return major + "." + minor;
  }

  public byte byteValue() {
    return (byte) ((major << 4) | minor);
  }

}
