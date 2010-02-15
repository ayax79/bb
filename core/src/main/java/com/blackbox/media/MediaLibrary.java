package com.blackbox.media;

import com.blackbox.BaseEntity;
import com.blackbox.EntityReference;
import com.blackbox.Utils;
import com.blackbox.social.NetworkTypeEnum;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static com.blackbox.EntityType.MEDIA_LIBRARY;

/**
 * Defines a collection of media that belongs to a specific entity. This is useful for implemented things like albums.
 */
@XmlRootElement(name = "mediaLibrary")
public class MediaLibrary extends BaseEntity {
    private static final long serialVersionUID = 1907881509395016077L;

    public static enum MediaLibraryType {
        // order matters, don't reorder
        IMAGE,
        VIDEO,
        CORK_BOARD
    }

    private String description;
    private List<MediaMetaData> media;
    private EntityReference owner;
    private MediaLibraryType type = MediaLibraryType.IMAGE;
    private NetworkTypeEnum networkTypeEnum = NetworkTypeEnum.WORLD;
    private boolean hide = false;

    public MediaLibrary() {
        super(MEDIA_LIBRARY);
    }

    public List<MediaMetaData> getMedia() {
        return media;
    }

    public void setMedia(List<MediaMetaData> media) {
        this.media = media;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NetworkTypeEnum getNetworkTypeEnum() {
        return networkTypeEnum;
    }

    public void setNetworkTypeEnum(NetworkTypeEnum networkTypeEnum) {
        this.networkTypeEnum = networkTypeEnum;
    }

    public EntityReference getOwner() {
        return owner;
    }

    public void setOwner(EntityReference owner) {
        this.owner = owner;
    }

    public MediaLibraryType getType() {
        return type;
    }

    public void setType(MediaLibraryType type) {
        this.type = type;
    }

    public static MediaLibrary createMediaLibrary() {
        MediaLibrary ml = new MediaLibrary();
        Utils.applyGuid(ml);
        return ml;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean isHide) {
        this.hide = isHide;
    }

    @Override
    public String toString() {
        return "MediaLibrary{" +
                "description='" + description + '\'' +
                ", media=" + media +
                ", owner=" + owner +
                ", type=" + type +
                "} " + super.toString();
    }
}
