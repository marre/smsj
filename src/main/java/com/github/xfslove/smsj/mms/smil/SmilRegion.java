package com.github.xfslove.smsj.mms.smil;

import java.io.Serializable;

/**
 * Created by hanwen on 2018/8/23.
 */
public class SmilRegion implements Serializable {

  public static final String TEXT = "Text";

  public static final String IMAGE = "Image";

  private final String id;

  /**
   * percentage
   */
  private int width = 100;

  /**
   * percentage
   */
  private int height = 100;

  /**
   * percentage
   */
  private int left;

  /**
   * percentage
   */
  private int top;

  private Fit fit = Fit.MEET;

  /**
   * @param id is {@link SmilRegion#TEXT} or {@link SmilRegion#IMAGE}
   */
  public SmilRegion(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getLeft() {
    return left;
  }

  public void setLeft(int left) {
    this.left = left;
  }

  public int getTop() {
    return top;
  }

  public void setTop(int top) {
    this.top = top;
  }

  public Fit getFit() {
    return fit;
  }

  public void setFit(Fit fit) {
    this.fit = fit;
  }

  public enum Fit {
    /**
     * smil2.0
     */
    HIDDEN,
    MEET,
    FILL,
    SCROLL
  }
}
