package org.marre.mms;

import org.marre.xml.XmlAttribute;
import org.marre.xml.XmlDocument;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hanwen on 2018/8/22.
 */
public class Smil implements XmlDocument {

  public static final String SMIL_CONTENT_TYPE = "application/smil";

  private int height;

  private int width;

  private List<SmilRegion> regionList = new ArrayList<>();

  private List<SmilPar> parList = new ArrayList<>();

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public List<SmilRegion> getRegionList() {
    return Collections.unmodifiableList(regionList);
  }

  public void setRegion(SmilRegion region) {
    this.regionList.add(region);
  }

  public List<SmilPar> getParList() {
    return Collections.unmodifiableList(parList);
  }

  public void setPar(SmilPar par) {
    this.parList.add(par);
  }

  @Override
  public String getContentType() {
    return SMIL_CONTENT_TYPE;
  }

  @Override
  public void writeXmlTo(OutputStream os) throws Exception {
    try (SmilWriter writer = new SmilWriter()) {

      writer.addStartElement("smil");

      // head
      writer.addStartElement("head");

      writer.addEmptyElement("root-layout",
          new XmlAttribute[]{
              new XmlAttribute("height", String.valueOf(height)), new XmlAttribute("width", String.valueOf(width))
          });

      for (SmilRegion region : regionList) {
        writer.addEmptyElement("region",
            new XmlAttribute[]{
                new XmlAttribute("id", region.getId()), new XmlAttribute("width", region.getWeight() + "%"),
                new XmlAttribute("height", region.getHeight() + "%"), new XmlAttribute("left", region.getLeft() + "%"),
                new XmlAttribute("top", region.getRight() + "%"), new XmlAttribute("fit", region.getFit().name().toLowerCase())
            });
      }
      writer.addEndElement();

      // body
      writer.addStartElement("body");

      for (SmilPar par : parList) {
        if (par.getDur() != null) {
          writer.addStartElement("par");
        } else {
          writer.addStartElement("par", new XmlAttribute[]{new XmlAttribute("dur", par.getDur() + "ms")});
        }

        for (SmilMedia media : par.getMediaList()) {
          List<XmlAttribute> attrs = new ArrayList<>();
          attrs.add(new XmlAttribute("src", media.getSrc()));
          if (media.getRef() != null && media.getRef().length() > 0) {
            attrs.add(new XmlAttribute("region", media.getRef()));
          }
          if (media instanceof SmilMedia.Text) {
            writer.addEmptyElement("text", attrs.toArray(new XmlAttribute[0]));
          } else if (media instanceof SmilMedia.Image) {
            writer.addEmptyElement("img", attrs.toArray(new XmlAttribute[0]));
          } else if (media instanceof SmilMedia.Audio) {
            writer.addEmptyElement("audio", attrs.toArray(new XmlAttribute[0]));
          } else if (media instanceof SmilMedia.Video) {
            writer.addEmptyElement("video", attrs.toArray(new XmlAttribute[0]));
          }
        }

        writer.addEndElement();
      }
      writer.addEndElement();

      writer.addEndElement();
      writer.writeTo(os);
    }
  }
}
