package com.github.xfslove.smsj.mms.smil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hanwen
 * created at 2018/8/23.
 */
public class SmilPar implements Serializable {

  private Integer dur;

  private List<SmilMedia> mediaList = new ArrayList<>();

  public List<SmilMedia> getMediaList() {
    return Collections.unmodifiableList(mediaList);
  }

  public void addMedia(SmilMedia media) {
    this.mediaList.add(media);
  }

  public Integer getDur() {
    return dur;
  }

  public void setDur(Integer dur) {
    this.dur = dur;
  }
}
