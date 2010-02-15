package com.blackbox.server.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.yestech.lib.util.Pair;
import static com.blackbox.server.util.MediaPublishUtil.isStreamMedia;
import com.blackbox.media.MediaMetaData;
import org.yestech.publish.objectmodel.ArtifactType;

import java.io.File;
import java.net.URL;

/**
 * @author A.J. Wright
 */
public class MediaPublishUtilTest {
    @Test
    public void buildThumbnailUniqueNameTest() {
        Pair<String,String> pair = new Pair<String,String>("lskdjflsdfkj", "sdlfkjsdflkj.jpg");

        String result = MediaPublishUtil.buildThumbnailUniqueName(pair.getSecond());
        assertEquals("sdlfkjsdflkj-thumbnail.jpg", result);
    }


    @Test
    public void testIsStreamMediaWithVirtualGift() {
        MediaMetaData vc = new MediaMetaData();
        vc.setAcknowledged(false);
        assertFalse(isStreamMedia(vc));

        vc.setAcknowledged(true);
        assertTrue(isStreamMedia(vc));
    }

    @Test
    public void testLoadMediaInfo() {
        URL resource = getClass().getResource("/daisy.jpg");
        File file = new File(resource.getFile());
        MediaPublishUtil.MediaInfo info = MediaPublishUtil.loadMediaInfo(file.getName());
        assertEquals("image/jpeg", info.getMimeType());
        assertEquals(ArtifactType.IMAGE, info.getType());
    }

    @Test
    public void testMimeTypeForMp4() {
        MediaPublishUtil.MediaInfo mediaInfo = MediaPublishUtil.loadMediaInfo("foo.mp4");
        assertEquals("video/mp4", mediaInfo.getMimeType());
        assertEquals(ArtifactType.VIDEO, mediaInfo.getType());
    }

}
