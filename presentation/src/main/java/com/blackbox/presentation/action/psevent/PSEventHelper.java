
package com.blackbox.presentation.action.psevent;

import com.blackbox.EntityReference;
import com.blackbox.Status;
import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaLibrary;
import com.blackbox.media.MediaLibrary.MediaLibraryType;
import com.blackbox.media.MediaMetaData;
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
