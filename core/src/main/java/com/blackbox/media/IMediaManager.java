/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.media;

import com.blackbox.EntityReference;
import com.blackbox.EntityType;
import com.blackbox.util.Count;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * @author A.J. Wright
 */
public interface IMediaManager {

    /**
     * Stores content in the system. It will save the meta data object with the correct id and version.
     * Returns the meta data object minus the content. The content will return null.
     *
     * @param media
     */
    MediaMetaData publishMedia(MediaPublish<MediaMetaData> media) throws IOException;

    CorkboardImage publishCorkboardImage(CorkboardPublish corkboardPublish) throws IOException;

    MediaMetaData loadMediaMetaDataByGuid(String guid);

    //    byte[] loadContentById(long id);

    /**
     * Loads the profile MediaMetaData object for the specified entity.
     * For instance if you want to load the profile  MediaMetaData for a specific user you would pass
     * in an EntityReference for that user.
     *
     * @param entityReference The entity reference for the object you are trying to acquire the profile image for.
     * @return The MediaMetaData for the profile image.
     */
    MediaMetaData loadProfileMetaMediaData(EntityReference entityReference);

    MediaMetaData loadAvatarMetaMediaData(EntityReference entityReference);

    List<MediaLibrary> loadMediaLibrariesForOwner(EntityReference entityReference);

    MediaLibrary loadMediaLibraryByGuid(String guid);

    MediaLibrary saveMediaLibrary(MediaLibrary mediaLibrary);

    List<MediaMetaData> loadRecentMediaForOwner(EntityReference entityReference, int total);

    MediaMetaData save(MediaMetaData metaData);

    void deleteMediaMetaData(String userGuid);


    MediaMetaData loadMediaMetaDataByEpisodeId(String episodeId);

    AvatarImage loadAvatarImageFor(EntityType type, String guid, String callerGuid);

    CorkboardImage saveCorkboardImage(CorkboardImage corkboardImage);

    @Transactional
    void bulkUpdate(List<CorkboardImage> images);

    List<CorkboardImage> loadCorkboard(String guid);

    void deleteMediaLibrary(String libraryGuid);

    Count deleteMediaMetaDataFromLibrary(String guid, String userGuid);

    MediaMetaData saveLibraryImage(MediaMetaData md);

    Count totalPhotosByAssociatedAlbumForUser(String albumGuid, String userGuid);

    Count totalPhotosAllAlbumsForUser(String guid);

    Count totalNumberOfAlbumsForUser(String guid);
}
