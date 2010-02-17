/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media;

import com.blackbox.foundation.media.*;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.util.Bounds;
import com.blackbox.foundation.social.NetworkTypeEnum;

import java.util.List;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 */
public interface IMediaDao
{
    @Transactional(readOnly = false)
    void save(MediaMetaData contentMetaData);

    MediaMetaData loadMediaMetaDataByGuid(String guid);

    @Transactional(readOnly = false)
    void delete(MediaMetaData contentMetaData);

    MediaLibrary loadMediaLibraryByGuid(String guid);

    /**
     * Inserts or updates a media library entry.
     *
     * This method will cascade to the underlying {@link com.blackbox.foundation.media.MediaMetaData} objects,
     * if and only if they have not been previously persisted
     * (version == null). This will prevent updates being performed on partial objects, since most of the media library
     * methods return only partial objects for {@link com.blackbox.foundation.media.MediaMetaData}.
     *
     * 
     * @param library The library object to insert or updated.
     * @return The updated library object.
     */
    MediaLibrary save(MediaLibrary library);

    List<MediaLibrary> loadMediaLibrariesByOwner(EntityReference owner);

    @Transactional(readOnly = false)
    void delete(MediaLibrary library);

    MediaMetaData loadProfileMediaMetaDataByOwner(EntityReference owner);
    MediaMetaData loadAvatarMediaMetaDataByOwner(EntityReference owner);

    List<MediaMetaData> loadMedia(Collection<String> owner, Collection<String> recipients, Collection<NetworkTypeEnum> types, Bounds bounds);

    MediaMetaData loadByEpisodeId(String episodeId);

    MediaMetaData loadLastMediaMetaData(String guid);

    @Transactional(readOnly = false)
    void save(List<MediaMetaData> mediaMetaDatas);

    List<MediaMetaData> loadMedia(Collection<NetworkTypeEnum> types, Bounds bounds);

    AvatarImage loadAvatarImage(EntityType entityType, String guid);

    @Transactional
    void save(CorkboardImage corkboardImage);

    List<CorkboardImage> loadCorkboardImages(String guid);

    List<MediaMetaData> loadAllAssociatedMedia(String guid, NetworkTypeEnum... types);

    @Transactional
    void delete(CorkboardImage corkboardImage);

    MediaLibrary loadMediaLibraryByMediaMetaGuid(String guid);

    int totalPhotosByAssociatedAlbumForUser(String guid);

    int totalPhotosAllAlbumsForUser(String guid);

    int totalNumberOfAlbumsForUser(String guid);

    void reindex();
}
