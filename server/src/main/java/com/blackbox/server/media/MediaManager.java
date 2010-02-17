/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.Utils;
import com.blackbox.foundation.bookmark.IBookmarkManager;
import com.blackbox.foundation.exception.MediaStoreException;
import com.blackbox.foundation.media.*;
import com.blackbox.server.media.event.*;
import com.blackbox.server.user.IUserDao;
import com.blackbox.server.util.MediaPublishUtil;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Count;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yestech.cache.ICacheManager;
import org.yestech.event.multicaster.BaseServiceContainer;
import org.yestech.lib.util.Pair;
import org.yestech.publish.client.IPublishBridge;
import org.yestech.publish.objectmodel.ArtifactType;
import org.yestech.publish.objectmodel.DefaultFileArtifact;
import org.yestech.publish.objectmodel.PublisherProperties;
import org.yestech.publish.util.PublishUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.blackbox.foundation.Utils.getCurrentDateTime;
import static com.blackbox.server.util.MediaPublishUtil.buildThumbnailUniqueName;
import static org.apache.commons.io.FileUtils.openOutputStream;
import static org.apache.commons.io.FileUtils.touch;
import static org.yestech.publish.util.PublishUtils.generateUniqueIdentifier;

/**
 * Service for storing and retrieve content from the system.
 *
 * @author A.J. Wright
 */
@Service("mediaManager")
public class MediaManager extends BaseServiceContainer implements IMediaManager {

    private static final String HTTP_SEPARATOR = "/";
    private static final Logger logger = LoggerFactory.getLogger(MediaManager.class);

    @Resource(name = "mediaDao")
    IMediaDao mediaDao;

    @Resource(name = "userDao")
    IUserDao userDao;

    @Resource(name = "avatarImageCache")
    ICacheManager<String, AvatarImage> avatarImageCache;

    @Resource
    IBookmarkManager bookmarkManager;

    @Resource(name = "publisherProperties")
    PublisherProperties publisherProperties;

    @Resource(name = "mediaPublishBridge")
    IPublishBridge bridge;

    @Resource(name = "publishTempDir")
    File publishTempDir;

    @Resource(name = "profileImageCache")
    ICacheManager<String, MediaMetaData> profileImageCache;


    @Resource(name = "ownerMediaLibraryCache")
    ICacheManager<String, List<MediaLibrary>> ownerMediaLibraryCache;

    public MediaMetaData publishMedia(MediaPublish<MediaMetaData> media) throws IOException {
        MediaMetaData metaData = media.getMediaMetaData();
        cleanFileName(metaData);
        byte[] data = media.getData();
        publish(metaData, new ByteArrayInputStream(data));
        media.setData(null);
        return metaData;
    }

    protected void cleanFileName(MediaMetaData metaData) {
        metaData.setFileName(metaData.getFileName().replaceAll("\\s+", "_"));
    }

    private MediaMetaData publish(MediaMetaData metaData, InputStream content) {
        if (metaData.getGuid() == null) {
            Utils.applyGuid(metaData);
        }

        File file = new File(publishTempDir, metaData.getGuid());
        if (!publishTempDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            publishTempDir.mkdirs();
        }

        assert !file.exists();
        OutputStream out = null;

        try {
            touch(file);
            out = openOutputStream(file);
            IOUtils.copy(content, out);
        }
        catch (IOException e) {
            logger.error("error storing file: " + file, e);
            throw new MediaStoreException(e);
        }
        finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(content);
        }

        // determine the correct artifact type
        MediaPublishUtil.MediaInfo info = MediaPublishUtil.loadMediaInfo(metaData.getFileName());
        metaData.setMimeType(info.getMimeType());
        metaData.setArtifactType(info.getType());

        String uniqueFileName = PublishUtils.generateUniqueIdentifier(metaData);
        String thumbnailFileName = buildThumbnailUniqueName(uniqueFileName);
        String artifactDirectoryName = generateUniqueIdentifier(metaData.getArtifactOwner());
        if (metaData.getArtifactType() != ArtifactType.VIDEO) {

            String urlPrefix = publisherProperties.getProperty(Pair.create(metaData.getArtifactType(), "urlPrefix"));
            StringBuilder builder = new StringBuilder();
            String location = builder.append(urlPrefix).append(HTTP_SEPARATOR).append(artifactDirectoryName).append(HTTP_SEPARATOR).append(uniqueFileName).toString();
            metaData.setLocation(location);
            builder = new StringBuilder();
            String thumbLocation = builder.append(urlPrefix).append(HTTP_SEPARATOR).append(artifactDirectoryName).append(HTTP_SEPARATOR).append(thumbnailFileName).toString();
            metaData.setThumbnailLocation(thumbLocation);
        }
        metaData.setCreated(getCurrentDateTime());
        metaData.setUniqueNames(Pair.create(artifactDirectoryName, uniqueFileName));


        if (!metaData.isLibrary() && !metaData.isLoosePic()) {
            MediaMetaData profileImage = mediaDao.loadProfileMediaMetaDataByOwner(metaData.getArtifactOwner());
            if (profileImage != null) {
                metaData.getSenderProfile().setSenderProfileImage(profileImage.getLocation());
            }
            MediaMetaData avatarImage = mediaDao.loadAvatarMediaMetaDataByOwner(metaData.getArtifactOwner());
            if (avatarImage != null) {
                metaData.getSenderProfile().setSenderAvatarImage(avatarImage.getLocation());
            }
        }

        // Finally publish the artifact through camel
        DefaultFileArtifact<MediaMetaData, String> artifact = new DefaultFileArtifact<MediaMetaData, String>();
        artifact.setFile(file);
        artifact.setArtifactMetaData(metaData);
        final String guid = Utils.generateGuid();
        artifact.setArtifactIdentifier(guid);
        bridge.publish(artifact);

        // I switched the following two to update the meta data, since it make take a while for the bridge to finish
        // publishing the image.

        if (metaData.isProfile()) {
            profileImageCache.put(metaData.getArtifactOwner().getGuid(), metaData);
        }

        if (metaData.isAvatar()) {
            AvatarImage image = avatarImageCache.get(metaData.getArtifactOwner().getGuid());
            image.setImageLink(metaData.getLocation());
            avatarImageCache.put(metaData.getArtifactOwner().getGuid(), image);
        }
        return metaData;
    }

    @Override
    public CorkboardImage publishCorkboardImage(CorkboardPublish corkboardPublish) throws IOException {
        CorkboardImage image = corkboardPublish.getCorkboardImage();
        MediaMetaData metaData = corkboardPublish.getMediaMetaData();
        metaData.setRecipients(null); // for somereason without this xstream blows up on publish
        metaData.setLibrary(true);
        // lets remove any spaces from the filename, it causes some issues with publishing
        cleanFileName(metaData);
        byte[] data = corkboardPublish.getData();
        publish(metaData, new ByteArrayInputStream(data));

        image.setMetaDataGuid(metaData.getGuid());
        image.setFileName(metaData.getFileName());
        image.setLocation(metaData.getLocation());
        mediaDao.save(metaData);
        mediaDao.save(image);
        return corkboardPublish.getCorkboardImage();
    }

    @Override
    public List<CorkboardImage> loadCorkboard(String guid) {
        List<CorkboardImage> images = mediaDao.loadCorkboardImages(guid);
        for (CorkboardImage image : images) {
            MediaMetaData data = mediaDao.loadMediaMetaDataByGuid(image.getMetaDataGuid());
            if (data != null) {
                image.setFileName(data.getFileName());
                image.setLocation(data.getLocation());
            }
        }
        return images;
    }

    @Override
    public Count deleteMediaMetaDataFromLibrary(String guid, String userGuid) {
        Count totalPhotosInAlbum = new Count(0);
        MediaMetaData metaData = mediaDao.loadMediaMetaDataByGuid(guid);
        if (metaData == null) {
            throw new IllegalArgumentException("No MediaMetaData object with the specified guid exists. : " + guid);
        }

        User user = userDao.loadUserByGuid(userGuid);
        if (user == null) {
            throw new IllegalArgumentException("No User with object with the specified guid exists : " + userGuid);
        }

        if (user.getType() == User.UserType.BLACKBOX_EMPLOYEE ||
                user.getGuid().equals(metaData.getArtifactOwner().getGuid())) {
            MediaLibrary templLibrary = mediaDao.loadMediaLibraryByMediaMetaGuid(guid);
            MediaLibrary library = mediaDao.loadMediaLibraryByGuid(templLibrary.getGuid());
            List<MediaMetaData> datas = library.getMedia();
            if (datas != null) {
                Iterator<MediaMetaData> mediaMetaDataIterator = datas.iterator();
                while (mediaMetaDataIterator.hasNext()) {
                    MediaMetaData mediaMetaData = mediaMetaDataIterator.next();
                    if (mediaMetaData.getGuid().equals(guid)) {
                        mediaMetaDataIterator.remove();
                        break;
                    }
                }
                mediaDao.save(library);
                ownerMediaLibraryCache.flush(userGuid);
                totalPhotosInAlbum = totalPhotosByAssociatedAlbumForUser(library.getGuid(), userGuid);
            }
        } else {
            throw new UnauthorizedException("Media cannot be deleted by user " + user.getUsername());
        }

        if (metaData.isProfile()) {
            profileImageCache.flush(userGuid);
        }
        return totalPhotosInAlbum;
    }

    @Override
    public void deleteMediaMetaData(String guid) {

        MediaMetaData metaData = mediaDao.loadMediaMetaDataByGuid(guid);
        if (metaData == null) {
            throw new IllegalArgumentException("No MediaMetaData object with the specified guid exists. : " + guid);
        }

        if (metaData.isProfile()) {
            profileImageCache.flush(guid);
        }

    }

    @Override
    public void deleteMediaLibrary(String libraryGuid) {
        MediaLibrary library = mediaDao.loadMediaLibraryByGuid(libraryGuid);
        if (library != null) {
            mediaDao.delete(library);
            ownerMediaLibraryCache.flush(library.getOwner().getGuid());
        }
    }

    @Override
    public MediaMetaData saveLibraryImage(MediaMetaData md) {
        save(md);

        MediaLibrary library = mediaDao.loadMediaLibraryByMediaMetaGuid(md.getGuid());
        ownerMediaLibraryCache.flush(library.getOwner().getGuid());
        return md;
    }

    @Override
    public MediaMetaData save(MediaMetaData metaData) {
        mediaDao.save(metaData);
        if (metaData.isAvatar()) {
            avatarImageCache.flush(metaData.getArtifactOwner().getGuid());
        }
        if (metaData.isProfile()) {
            profileImageCache.flush(metaData.getArtifactOwner().getGuid());
        }
        return metaData;
    }

    @Override
    public CorkboardImage saveCorkboardImage(CorkboardImage corkboardImage) {
        mediaDao.save(corkboardImage);
        return corkboardImage;
    }

    @Override
    @Transactional
    public void bulkUpdate(List<CorkboardImage> images) {
        if (images != null && !images.isEmpty()) {
            Map<String, CorkboardImage> imageMap = Maps.uniqueIndex(images, new Function<CorkboardImage, String>() {
                @Override
                public String apply(CorkboardImage from) {
                    return from.getGuid();
                }
            });

            String userGuid = images.get(0).getOwner().getGuid();
            List<CorkboardImage> list = loadCorkboard(userGuid);
            for (CorkboardImage ci0 : list) {
                CorkboardImage ci1 = imageMap.get(ci0.getGuid());
                if (ci1 == null) {
                    mediaDao.delete(ci0);
                } else {
                    mediaDao.save(ci1);
                }
            }
            ownerMediaLibraryCache.flush(userGuid);
        }
    }

    @Override
    public MediaMetaData loadMediaMetaDataByGuid(String guid) {
        return (MediaMetaData) getEventMulticaster().process(new LoadContentMetaDataEvent(guid));
    }

    @Override
    public MediaLibrary loadMediaLibraryByGuid(String guid) {
        return mediaDao.loadMediaLibraryByGuid(guid);
    }

    @Override
    public MediaMetaData loadProfileMetaMediaData(EntityReference entityReference) {
        MediaMetaData data = profileImageCache.get(entityReference.getGuid());
        if (data == null) {
            data = (MediaMetaData) getEventMulticaster().process(new LoadProfileMediaMetaDataEvent(entityReference));
            profileImageCache.put(entityReference.getGuid(), data);
        }
        return data;
    }

    @Override
    public MediaMetaData loadAvatarMetaMediaData(EntityReference entityReference) {
        return mediaDao.loadAvatarMediaMetaDataByOwner(entityReference);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<MediaLibrary> loadMediaLibrariesForOwner(EntityReference entityReference) {
        List<MediaLibrary> libs = ownerMediaLibraryCache.get(entityReference.getGuid());
        if (libs == null) {
            libs = (List<MediaLibrary>) getEventMulticaster().process(new LoadMediaLibrariesByOwnerEvent(entityReference));
            ownerMediaLibraryCache.put(entityReference.getGuid(), libs);
        }
        return libs;
    }

    @Override
    public MediaLibrary saveMediaLibrary(MediaLibrary mediaLibrary) {
        mediaLibrary = mediaDao.save(mediaLibrary);
        ownerMediaLibraryCache.flush(mediaLibrary.getOwner().getGuid());
        return mediaLibrary;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<MediaMetaData> loadRecentMediaForOwner(EntityReference entityReference, int total) {
        return (List<MediaMetaData>) getEventMulticaster().process(new LoadRecentMediaForOwnerEvent(entityReference, total));
    }

    @Override
    public MediaMetaData loadMediaMetaDataByEpisodeId(String episodeId) {
        return mediaDao.loadByEpisodeId(episodeId);
    }

    @Override
    public AvatarImage loadAvatarImageFor(EntityType type, String guid, String callerGuid) {
        //TODO:  FIX to be light weight
        AvatarImage image = avatarImageCache.get(guid);
        if (image == null) {
            image = mediaDao.loadAvatarImage(type, guid);

            // cache the miss (they don't have an avatar yet for some unknown reason)
            if (image == null) {
                //TODO: create method to load avatar from userGuid directly
                User user = userDao.loadUserByGuid(guid);

                if (user != null) {
                    image = new AvatarImage(guid, user.getUsername(), user.getType(), null);
                }
            }

            avatarImageCache.put(guid, image);
        }


        if (image != null && callerGuid != null && !callerGuid.equals(guid)) {
            image = image.cloneAvatarImage();
            image.setWishStatus(bookmarkManager.loadWishStatus(callerGuid, guid));
        }

        return image;
    }

    @Override
    public Count totalPhotosByAssociatedAlbumForUser(String albumGuid, String userGuid) {
        return new Count(mediaDao.totalPhotosByAssociatedAlbumForUser(albumGuid));
    }

    @Override
    public Count totalPhotosAllAlbumsForUser(String guid) {
        return new Count(mediaDao.totalPhotosAllAlbumsForUser(guid));
    }

    @Override
    public Count totalNumberOfAlbumsForUser(String guid) {
        return new Count(mediaDao.totalNumberOfAlbumsForUser(guid));
    }

}
