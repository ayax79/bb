package com.blackbox.server.media.listener;

import com.blackbox.media.MediaLibrary;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.media.event.SaveMediaLibraryEvent;
import com.blackbox.server.BaseBlackboxListener;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents(SaveMediaLibraryEvent.class)
public class SaveMediaLibraryListener extends BaseBlackboxListener<SaveMediaLibraryEvent, MediaLibrary> {

    @Resource(name = "mediaDao")
    private IMediaDao mediaDao;

    @Override
    public void handle(SaveMediaLibraryEvent event, ResultReference<MediaLibrary> result) {

        final MediaLibrary mediaLibrary = event.getType();
        result.setResult(mediaLibrary);
    }

    public void setMediaDao(IMediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }
}
