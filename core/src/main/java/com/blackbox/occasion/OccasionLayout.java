package com.blackbox.occasion;

import com.blackbox.BBPersistentObject;
import com.blackbox.media.MediaMetaData;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


/**
 * @author A.J. Wright
 */
@XmlRootElement
public class OccasionLayout extends BBPersistentObject implements Serializable {


    public static enum LayoutType {
        // don't change order
        PORTRAIT,
        LANDSCAPE, // doesn't exist in UI
        IMAGE_ONLY,
        VIDEO_ONLY,
        VIDEO_AND_IMAGE,
        VIDEO // doesn't exist in UI
    }

    public static enum DateFormat {
        YYYYMMDD,
        MMDDYYYY,
        DDMMYYYY
    }

    private String font; // body font
    private String backgroundColor;
    private MediaMetaData transiantImage;
    private MediaMetaData transiantVideo;
    private String imageGuid;
    private String videoGuid;
    private LayoutType layoutType;
    private String textAlign;
    private String textColor;
    private DateFormat dataFormat;
    private boolean showHeading;
    private String headerFont;
    private Integer headerSize;
    private Integer bodySize;
    private String boxcolor;


    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getImageGuid() {
        return imageGuid;
    }

    public void setImageGuid(String imageGuid) {
        this.imageGuid = imageGuid;
    }

    public String getVideoGuid() {
        return videoGuid;
    }

    public void setVideoGuid(String videoGuid) {
        this.videoGuid = videoGuid;
    }

    public MediaMetaData getTransiantImage() {
        return transiantImage;
    }

    public void setTransiantImage(MediaMetaData transiantImage) {
        this.transiantImage = transiantImage;
    }

    public MediaMetaData getTransiantVideo() {
        return transiantVideo;
    }

    public void setTransiantVideo(MediaMetaData transiantVideo) {
        this.transiantVideo = transiantVideo;
    }

    /*@ManyToOne
    public MediaMetaData getImage() {
        return image;
    }

    public void setImage(MediaMetaData image) {
        this.image = image;
    }

    @ManyToOne
    public MediaMetaData getVideo() {
        return video;
    }

    public void setVideo(MediaMetaData video) {
        this.video = video;
    }
    */

    public LayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = layoutType;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    public DateFormat getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(DateFormat dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OccasionLayout that = (OccasionLayout) o;

        if (showHeading != that.showHeading) return false;
        if (backgroundColor != null ? !backgroundColor.equals(that.backgroundColor) : that.backgroundColor != null)
            return false;
        if (bodySize != null ? !bodySize.equals(that.bodySize) : that.bodySize != null) return false;
        if (boxcolor != null ? !boxcolor.equals(that.boxcolor) : that.boxcolor != null) return false;
        if (dataFormat != that.dataFormat) return false;
        if (font != null ? !font.equals(that.font) : that.font != null) return false;
        if (headerFont != null ? !headerFont.equals(that.headerFont) : that.headerFont != null) return false;
        if (headerSize != null ? !headerSize.equals(that.headerSize) : that.headerSize != null) return false;
        if (imageGuid != null ? !imageGuid.equals(that.imageGuid) : that.imageGuid != null) return false;
        if (layoutType != that.layoutType) return false;
        if (textAlign != null ? !textAlign.equals(that.textAlign) : that.textAlign != null) return false;
        if (textColor != null ? !textColor.equals(that.textColor) : that.textColor != null) return false;
        if (transiantImage != null ? !transiantImage.equals(that.transiantImage) : that.transiantImage != null)
            return false;
        if (transiantVideo != null ? !transiantVideo.equals(that.transiantVideo) : that.transiantVideo != null)
            return false;
        //noinspection RedundantIfStatement
        if (videoGuid != null ? !videoGuid.equals(that.videoGuid) : that.videoGuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (font != null ? font.hashCode() : 0);
        result = 31 * result + (backgroundColor != null ? backgroundColor.hashCode() : 0);
        result = 31 * result + (transiantImage != null ? transiantImage.hashCode() : 0);
        result = 31 * result + (transiantVideo != null ? transiantVideo.hashCode() : 0);
        result = 31 * result + (imageGuid != null ? imageGuid.hashCode() : 0);
        result = 31 * result + (videoGuid != null ? videoGuid.hashCode() : 0);
        result = 31 * result + (layoutType != null ? layoutType.hashCode() : 0);
        result = 31 * result + (textAlign != null ? textAlign.hashCode() : 0);
        result = 31 * result + (textColor != null ? textColor.hashCode() : 0);
        result = 31 * result + (dataFormat != null ? dataFormat.hashCode() : 0);
        result = 31 * result + (showHeading ? 1 : 0);
        result = 31 * result + (headerFont != null ? headerFont.hashCode() : 0);
        result = 31 * result + (headerSize != null ? headerSize.hashCode() : 0);
        result = 31 * result + (bodySize != null ? bodySize.hashCode() : 0);
        result = 31 * result + (boxcolor != null ? boxcolor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OccasionLayout{" +
                "font='" + font + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", transiantImage=" + transiantImage +
                ", transiantVideo=" + transiantVideo +
                ", imageGuid='" + imageGuid + '\'' +
                ", videoGuid='" + videoGuid + '\'' +
                ", layoutType=" + layoutType +
                ", textAlign='" + textAlign + '\'' +
                ", textColor='" + textColor + '\'' +
                ", dataFormat=" + dataFormat +
                ", showHeading=" + showHeading +
                ", headerFont='" + headerFont + '\'' +
                ", headerSize=" + headerSize +
                ", bodySize=" + bodySize +
                ", boxcolor='" + boxcolor + '\'' +
                '}';
    }

    public boolean isShowHeading() {
        return showHeading;
    }

    public void setShowHeading(boolean showHeading) {
        this.showHeading = showHeading;
    }

    public String getHeaderFont() {
        return headerFont;
    }

    public void setHeaderFont(String headerFont) {
        this.headerFont = headerFont;
    }

    public Integer getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(Integer headerSize) {
        this.headerSize = headerSize;
    }

    public Integer getBodySize() {
        return bodySize;
    }

    public void setBodySize(Integer bodySize) {
        this.bodySize = bodySize;
    }

    public String getBoxcolor() {
        return boxcolor;
    }

    public void setBoxcolor(String boxcolor) {
        this.boxcolor = boxcolor;
    }
}
