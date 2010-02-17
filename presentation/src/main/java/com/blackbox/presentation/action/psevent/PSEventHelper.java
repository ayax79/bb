
package com.blackbox.presentation.action.psevent;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.Status;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.foundation.media.MediaLibrary.MediaLibraryType;
import com.blackbox.foundation.media.MediaMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;


public class PSEventHelper {
    
    static MediaLibrary createPersonalImageMediaLibrary(EntityReference r,IMediaManager mediaManager,String name){
        MediaLibrary mockLib = MediaLibrary.createMediaLibrary();
            List<MediaMetaData> mediaList = new ArrayList<MediaMetaData>(12);
            mockLib.setCreated(new DateTime(new Date().getTime()));
            mockLib.setDescription("Just mock up personal event image lib");
            mockLib.setName(name);
            mockLib.setOwner(r);
            mockLib.setStatus(Status.ENABLED);
            mockLib.setType(MediaLibraryType.IMAGE);
            mockLib.setMedia(mediaList);
            mockLib = mediaManager.saveMediaLibrary(mockLib);
            return mockLib;
    }
    
}
