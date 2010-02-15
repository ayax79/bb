package com.blackbox.server.occasion.aspect;

import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaMetaData;
import com.blackbox.occasion.Occasion;
import com.blackbox.occasion.OccasionLayout;
import static com.google.common.collect.Lists.newArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

/**
 * @author A.J. Wright
 */
@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class PopulateOccasionTransientsAspectUnitTest {

    @Mock IMediaManager mediaManager;

    @Test
    public void handleOccasionListTest() {

        PopulateOccasionTransientsAspect aspect = new PopulateOccasionTransientsAspect();
        aspect.setMediaManager(mediaManager);

        MediaMetaData image = MediaMetaData.createMediaMetaData();
        MediaMetaData video = MediaMetaData.createMediaMetaData();

        Occasion occasion = Occasion.createOccasion();
        OccasionLayout layout = occasion.getLayout();
        layout.setVideoGuid(video.getGuid());
        layout.setImageGuid(image.getGuid());

        when(mediaManager.loadMediaMetaDataByGuid(layout.getImageGuid())).thenReturn(image);
        when(mediaManager.loadMediaMetaDataByGuid(layout.getVideoGuid())).thenReturn(video);

        aspect.handleOccasionList(newArrayList(occasion));

        verify(mediaManager, times(1)).loadMediaMetaDataByGuid(layout.getImageGuid());
        verify(mediaManager, times(1)).loadMediaMetaDataByGuid(layout.getVideoGuid());

        assertEquals(image, layout.getTransiantImage());
        assertEquals(video, layout.getTransiantVideo());
    }

}
