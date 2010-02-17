package com.blackbox.foundation.media;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CorkboardPublish implements Serializable {

    private CorkboardImage corkboardImage;
    private MediaMetaData mediaMetaData;

    private byte[] data;

    public CorkboardPublish(CorkboardImage corkboardImage, MediaMetaData media, byte[] data) {
        this.corkboardImage = corkboardImage;
        this.mediaMetaData = media;
        this.data = data;
    }

    public CorkboardPublish() {
    }

    public CorkboardImage getCorkboardImage() {
        return corkboardImage;
    }

    public void setCorkboardImage(CorkboardImage corkboardImage) {
        this.corkboardImage = corkboardImage;
    }

    public MediaMetaData getMediaMetaData() {
        return mediaMetaData;
    }

    public void setMediaMetaData(MediaMetaData mediaMetaData) {
        this.mediaMetaData = mediaMetaData;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
