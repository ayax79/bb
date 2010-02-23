/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.activity.IRecipient;
import com.blackbox.foundation.media.*;
import static com.blackbox.server.util.PersistenceUtil.insertOrUpdate;
import com.blackbox.foundation.social.NetworkTypeEnum;
import static com.blackbox.foundation.social.NetworkTypeEnum.ALL_MEMBERS;
import static com.blackbox.foundation.social.NetworkTypeEnum.WORLD;
import com.blackbox.foundation.util.Bounds;
import static com.google.common.collect.Lists.newArrayList;

import org.compass.core.CompassOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yestech.publish.objectmodel.ArtifactType;
import org.yestech.publish.objectmodel.episodic.IEpisodicArtifact;
import org.yestech.publish.objectmodel.episodic.IEpisodicArtifactPersister;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 *
 */
@Repository("mediaDao")
@SuppressWarnings("unchecked")
public class IbatisMediaDao implements IMediaDao, IEpisodicArtifactPersister {

    private static final Logger log = LoggerFactory.getLogger(IbatisMediaDao.class); 

    @Resource(name = "sqlSessionTemplate")
    SqlSessionOperations template;
    @Resource(name = "compassTemplate")
    CompassOperations compassTemplate;

    @Override
    @Transactional(readOnly = false)
    public void save(MediaMetaData mediaMetaData) {

        insertOrUpdate(mediaMetaData, template, "MediaMetaData.insert", "MediaMetaData.update");
        List<IRecipient> recipients = mediaMetaData.getRecipients();
        if (recipients != null) {
            for (IRecipient recipient : recipients) {
                MediaRecipient r = (MediaRecipient) recipient;
                r.setMediaMetaData(mediaMetaData);
                insertOrUpdate(r, template, "MediaRecipient.insert", "MediaRecipient.update");
            }
        }
        compassTemplate.save(mediaMetaData);
    }

    @Override
    @Transactional
    public void save(CorkboardImage corkboardImage) {
        insertOrUpdate(corkboardImage, template, "CorkboardImage.insert", "CorkboardImage.update");
    }

    @Override
    @Transactional(readOnly = false)
    public void save(List<MediaMetaData> mediaMetaDatas) {
        for (MediaMetaData mediaMetaData : mediaMetaDatas) {
            save(mediaMetaData);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(MediaMetaData mediaMetaData) {
        template.delete("MediaLibrary.deleteJoinByMediaGuid", mediaMetaData.getGuid());
        template.delete("MediaRecipient.deleteByMetaDataGuid", mediaMetaData.getGuid());
        template.delete("MediaMetaData.delete", mediaMetaData.getGuid());
        compassTemplate.delete(mediaMetaData);
    }

    @Override
    public MediaMetaData loadMediaMetaDataByGuid(String guid) {
        return (MediaMetaData) template.selectOne("MediaMetaData.load", guid);
    }

    @Override
    public MediaMetaData loadProfileMediaMetaDataByOwner(EntityReference owner) {
        HashMap<String, Object> params = new HashMap<String, Object>(3);
        params.put("avatar", false);
        params.put("profile", true);
        params.put("guid", owner.getGuid());
        return (MediaMetaData) template.selectOne("MediaMetaData.loadProfileAvatar", params);
    }

    @Override
    public MediaMetaData loadAvatarMediaMetaDataByOwner(EntityReference owner) {
        HashMap<String, Object> params = new HashMap<String, Object>(3);
        params.put("avatar", false);
        params.put("profile", true);
        params.put("guid", owner.getGuid());
        return (MediaMetaData) template.selectOne("MediaMetaData.loadProfileAvatar", params);
    }

    @Override
    public MediaLibrary loadMediaLibraryByMediaMetaGuid(String guid) {
        return (MediaLibrary) template.selectOne("MediaLibrary.loadByMediaMetaDataGuid", guid);
    }

    @Override
    public MediaLibrary loadMediaLibraryByGuid(String guid) {
        return (MediaLibrary) template.selectOne("MediaLibrary.load", guid);
    }

    @Override
    @Transactional(readOnly = false)
    public MediaLibrary save(MediaLibrary library) {
        insertOrUpdate(library, template, "MediaLibrary.insert", "MediaLibrary.update");
        for (MediaMetaData media : library.getMedia()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("library", library);
            map.put("media", media);
            if (media.getVersion() == null) {
                save(media);
                template.insert("MediaLibrary.insertJoin", map);
            }
        }

        return library;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(MediaLibrary library) {
        template.delete("MediaLibrary.deleteJoin", library.getGuid());
        if (library.getMedia() != null) {
            for (MediaMetaData metaData : library.getMedia()) {
                delete(metaData);
            }
        }
        template.delete("MediaLibrary.delete", library.getGuid());
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<MediaLibrary> loadMediaLibrariesByOwner(EntityReference owner) {
        return template.selectList("MediaLibrary.loadByOwner", owner.getGuid());
    }

    public List<MediaMetaData> loadMedia(Collection<NetworkTypeEnum> types, Bounds bounds) {
        return loadMedia(null, null, types, bounds);
    }


    public List<MediaMetaData> loadMedia(final Collection<String> owners,
                                         final Collection<String> recipients,
                                         final Collection<NetworkTypeEnum> networkType,
                                         final Bounds bounds) {


        Collection<NetworkTypeEnum> types = new ArrayList<NetworkTypeEnum>(networkType);
        Collection<NetworkTypeEnum> publicNetworkTypes = new ArrayList<NetworkTypeEnum>(2);
        if (types.remove(WORLD)) {
            publicNetworkTypes.add(WORLD);
        }

        if (types.remove(ALL_MEMBERS)) {
            publicNetworkTypes.add(ALL_MEMBERS);
        }

        HashMap<String, Object> params = new HashMap<String, Object>(4);
        params.put("owners", owners);
        params.put("recipients", recipients);
        params.put("types", networkType);
        params.put("publicNetworkTypes", publicNetworkTypes);

        return template.selectList("MediaMetaData.loadMedia", params);
    }


    @Override
    public MediaMetaData loadByEpisodeId(String episodeId) {
        return (MediaMetaData) template.selectOne("MediaMetaData.loadByEpisodeId", episodeId);
    }

    @Override
    public MediaMetaData loadLastMediaMetaData(final String guid) {
        HashMap<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", guid);
        params.put("recipientDepth", newArrayList(NetworkTypeEnum.ALL_MEMBERS, NetworkTypeEnum.WORLD, NetworkTypeEnum.FRIENDS, NetworkTypeEnum.FOLLOWING, NetworkTypeEnum.FOLLOWERS));

        return (MediaMetaData) template.selectOne("MediaMetaData.loadLast", params);
    }

    @Override
    public void save(IEpisodicArtifact episodicArtifact) {
        save((MediaMetaData) episodicArtifact);
    }

    @Override
    public AvatarImage loadAvatarImage(final EntityType entityType, final String guid) {

        AvatarImage image = (AvatarImage) template.selectOne("AvatarImage.load", guid);
        if (image != null) {
            image.setEntityType(entityType);
        }

        return image;
    }

    @Override
    public List<CorkboardImage> loadCorkboardImages(String guid) {
        return template.selectList("CorkboardImage.loadByOwner", guid);
    }

    @Override
    public List<MediaMetaData> loadAllAssociatedMedia(final String guid, final NetworkTypeEnum... types) {

        HashMap<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", guid);
        if (types != null && types.length > 0) {
            params.put("recipientDepth", types);
        }

        return template.selectList("MediaMetaData.loadAssociated", params);
    }

    @Override
    @Transactional
    public void delete(CorkboardImage corkboardImage) {
        template.delete("MediaMetaData.delete", corkboardImage.getMetaDataGuid());
        template.delete("CorkboardImage.delete", corkboardImage.getGuid());
    }

    @Override
    public int totalPhotosByAssociatedAlbumForUser(String libraryGuid) {
        HashMap<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", libraryGuid);
        params.put("type", ArtifactType.IMAGE);
        return (Integer) template.selectOne("MediaLibrary.countPhotosByAlbum", params);
    }

    @Override
    public int totalPhotosAllAlbumsForUser(String ownerGuid) {
        HashMap<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", ownerGuid);
        params.put("type", ArtifactType.IMAGE);
        return (Integer) template.selectOne("MediaLibrary.countPhotosForUser", params);
    }

    @Override
    public int totalNumberOfAlbumsForUser(String ownerGuid) {
        return (Integer) template.selectOne("MediaLibrary.countAlbumsForUser", ownerGuid);
    }

    @Override
    public void reindex() {
        List<MediaMetaData> list = template.selectList("MediaMetaData.loadAll");
        for (MediaMetaData mediaMetaData : list) {
            log.info("Reindexing MediaMetaData : "+mediaMetaData.getGuid());
            compassTemplate.save(mediaMetaData);
        }
    }
}