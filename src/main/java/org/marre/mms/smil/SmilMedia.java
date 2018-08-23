package org.marre.mms.smil;

/**
 * Created by hanwen on 2018/8/23.
 */
public interface SmilMedia {

  String getSrc();

  /**
   * reference SmilRegion
   *
   * @return
   */
  String getRef();


  class Image implements SmilMedia {

    private final String src;

    private String ref;

    public Image(String src) {
      this.src = src;
    }

    public void setRef(String ref) {
      this.ref = ref;
    }

    @Override
    public String getSrc() {
      return src;
    }

    @Override
    public String getRef() {
      return ref;
    }
  }

  class Audio implements SmilMedia {

    private final String src;

    private String ref;

    public Audio(String src) {
      this.src = src;
    }

    public void setRef(String ref) {
      this.ref = ref;
    }

    @Override
    public String getSrc() {
      return src;
    }

    @Override
    public String getRef() {
      return ref;
    }
  }

  class Video implements SmilMedia {

    private final String src;

    private String ref;

    public Video(String src) {
      this.src = src;
    }

    public void setRef(String ref) {
      this.ref = ref;
    }

    @Override
    public String getSrc() {
      return src;
    }

    @Override
    public String getRef() {
      return ref;
    }
  }

  class Text implements SmilMedia {

    private final String src;

    private String ref;

    public Text(String src) {
      this.src = src;
    }

    public void setRef(String ref) {
      this.ref = ref;
    }

    @Override
    public String getSrc() {
      return src;
    }

    @Override
    public String getRef() {
      return ref;
    }
  }
}
