package com.blackbox.media;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({MediaMetaData.class})
public class MediaPublish<T extends MediaMetaData> implements Serializable {

    private T mediaMetaData;

    private byte[] data;

    public MediaPublish() {
    }

    public MediaPublish(T mediaMetaData, byte[] data) {
        this.mediaMetaData = mediaMetaData;
        this.data = data;
    }

    public T getMediaMetaData() {
        return mediaMetaData;
    }

    public void setMediaMetaData(T mediaMetaData) {
        this.mediaMetaData = mediaMetaData;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
