package com.blackbox.server.util;

import com.blackbox.foundation.media.MediaMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.activation.MimetypesFileTypeMap;
import java.io.InputStream;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
public final class MediaPublishUtil {

    private static final Logger logger = LoggerFactory.getLogger(MediaPublishUtil.class);

    private static MimetypesFileTypeMap mimeMap;
    static {
        try {
            InputStream in = MediaPublishUtil.class.getResourceAsStream("/bbrmimetypes.properties");
            mimeMap = new MimetypesFileTypeMap(in);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static class MediaInfo implements Serializable {
        private ArtifactType type;
        private String mimeType;

        public MediaInfo(ArtifactType type, String mimeType) {
            this.type = type;
            this.mimeType = mimeType;
        }

        public ArtifactType getType() {
            return type;
        }

        public void setType(ArtifactType type) {
            this.type = type;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }
    }


    private MediaPublishUtil() {
    }

    public static String buildThumbnailUniqueName(String name) {
        StringBuilder builder = new StringBuilder();
        String[] strings = name.split("\\.");

        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];

            builder.append(string);

            // if we are one before the last token add -thumbnail
            if (i + 2 == strings.length) builder.append("-thumbnail");

            if (i + 1 != strings.length) builder.append(".");
        }

        return builder.toString();
    }

    public static boolean isStreamMedia(MediaMetaData md) {
        return md.isAcknowledged() && !md.isLibrary() && !md.isLoosePic() && !md.isProfile() && !md.isAvatar();
    }

    public static MediaInfo loadMediaInfo(String filename) {
        String mimeType = mimeMap.getContentType(filename);
        return new MediaInfo(typeFromMime(mimeType), mimeType);
    }

    public static ArtifactType typeFromMime(String mime) {
        if (mime.contains("image")) {
            return ArtifactType.IMAGE;
        }

        if (mime.contains("video")) {
            return ArtifactType.VIDEO;
        }

        if (mime.contains("text")) {
            return ArtifactType.TEXT;
        }

        if (mime.contains("audio")) {
            return ArtifactType.AUDIO;
        }

        throw new IllegalArgumentException("Unsupported mime type" + mime);
    }

}
