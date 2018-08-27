package com.github.xfslove.mms.smil;

import com.github.xfslove.xml.XmlAttribute;
import com.github.xfslove.xml.XmlDocument;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * one smil looks like
 * <pre>
 *
 *   &lt;smil&gt;
 *     &lt;head&gt;
 *       &lt;root-layout/&gt;
 *       &lt;region id="Text"/&gt;
 *       &lt;region id="Image"/&gt;
 *     &lt;/head&gt;
 *     &lt;body&gt;
 *       &lt;text src="cid:1" region="Text"/&gt;
 *       &lt;img src="cid:2" region="Image"/&gt;
 *     &lt;/body&gt;
 *   &lt;/smil&gt;
 *
 * </pre>
 *
 * Created by hanwen on 2018/8/22.
 */
public class Smil implements XmlDocument {

  public static final String SMIL_CONTENT_TYPE = "application/smil";

  private int height;

  private int width;

  private List<SmilRegion> regionList = new LinkedList<>();

  private List<SmilPar> parList = new LinkedList<>();

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

  public void addRegion(SmilRegion region) {
    this.regionList.add(region);
  }

  public List<SmilPar> getParList() {
    return Collections.unmodifiableList(parList);
  }

  public void addPar(SmilPar par) {
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
                new XmlAttribute("id", region.getId()), new XmlAttribute("width", region.getWidth() + "%"),
                new XmlAttribute("height", region.getHeight() + "%"), new XmlAttribute("left", region.getLeft() + "%"),
                new XmlAttribute("top", region.getTop() + "%"), new XmlAttribute("fit", region.getFit().name().toLowerCase())
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
