package com.blackbox.server.media.listener;

import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;
import com.blackbox.server.media.event.LoadMediaLibrariesByOwnerEvent;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.media.MediaLibrary;
import com.blackbox.EntityReference;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 */
@ListenedEvents(LoadMediaLibrariesByOwnerEvent.class)
public class LoadMediaLibrariesByOwnerListener extends BaseBlackboxListener<LoadMediaLibrariesByOwnerEvent, List<MediaLibrary>> {

    @Resource(name = "mediaDao")
    private IMediaDao mediaDao;

    @Override
    public void handle(LoadMediaLibrariesByOwnerEvent event, ResultReference<List<MediaLibrary>> result) {
        final EntityReference owner = event.getType();
        final List<MediaLibrary> list = mediaDao.loadMediaLibrariesByOwner(owner);
        result.setResult(list);
    }

    public void setMediaDao(IMediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }
}
