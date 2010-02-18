/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.Utils;
import com.blackbox.foundation.activity.ActivityFactory;
import com.blackbox.foundation.media.*;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.user.IUserDao;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.testingutils.UserFixture;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Bounds;
import org.junit.Test;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.annotation.Resource;
import java.util.List;

import static com.blackbox.foundation.social.NetworkTypeEnum.*;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

/**
 *
 *
 */
public class MediaDaoIntegrationTest extends BaseIntegrationTest {

    @Resource(name = "mediaDao")
    protected IMediaDao mediaDao;


    @Resource
    IUserDao userDao;

    @Test
    public void testSave() {
        MediaMetaData md = ActivityFactory.createMedia();
        md.setFileName("myfile.png");
        md.setMimeType("image/png");
        md.setArtifactType(ArtifactType.IMAGE);
        Utils.applyGuid(md);
        EntityReference owner = User.createUser().toEntityReference();
        md.setArtifactOwner(owner);
        EntityReference recipient = User.createUser().toEntityReference();
        md.getRecipients().add(new MediaRecipient(recipient, md));
        md.setSize(823988932923898L);
//        md.setAccessControlList(null);
        md.setRecipientDepth(NetworkTypeEnum.SELF);
        mediaDao.save(md);
        assertNotNull(md.getGuid());
        assertNotNull(md.getVersion());

        MediaMetaData md2 = mediaDao.loadMediaMetaDataByGuid(md.getGuid());
        assertNotNull(md2);

        md2.setComment("foo foo foo");
        md2.setEpisodeId("lskdfjlfdkj");
        mediaDao.save(md2);

        List<MediaMetaData> list = mediaDao.loadMedia(newArrayList(owner.getGuid()), newArrayList(recipient.getGuid()),
                newArrayList(DIRECT, WORLD, ALL_MEMBERS, FRIENDS, SELF), new Bounds());

        mediaDao.delete(md);
    }

    @Test
    public void testALL_MEMBERS() {
        MediaMetaData md = ActivityFactory.createMedia();
        md.setFileName("myfile.png");
        md.setMimeType("image/png");
        md.setArtifactType(ArtifactType.IMAGE);
        Utils.applyGuid(md);
        EntityReference owner = User.createUser().toEntityReference();
        md.setArtifactOwner(owner);
        EntityReference recipient = User.createUser().toEntityReference();
        md.getRecipients().add(new MediaRecipient(recipient, md));
        md.setSize(823988932923898L);
//        md.setAccessControlList(null);
        md.setRecipientDepth(NetworkTypeEnum.ALL_MEMBERS);
        mediaDao.save(md);
        assertNotNull(md.getGuid());
        assertNotNull(md.getVersion());

        List<MediaMetaData> list = mediaDao.loadMedia(newArrayList(owner.getGuid()), newArrayList(recipient.getGuid()),
                newArrayList(DIRECT, WORLD, ALL_MEMBERS, FRIENDS, SELF), new Bounds());

        assertNotNull(list);
        assertTrue(!list.isEmpty());


        mediaDao.delete(md);
    }


    @Test
    public void testSaveWithPermissions() {
        MediaMetaData md = ActivityFactory.createMedia();
        md.setFileName("myfile.png");
        md.setMimeType("image/png");
        md.setArtifactType(ArtifactType.IMAGE);
        Utils.applyGuid(md);
        md.setArtifactOwner(User.createUser().toEntityReference());
        md.getRecipients().add(new MediaRecipient(User.createUser().toEntityReference(), md));
        md.setSize(823988932923898L);
        md.setRecipientDepth(NetworkTypeEnum.SELF);

//        HashSet<SocialPermission> socialPermissions = new HashSet<SocialPermission>();
//        HashSet<ApplicationPermission> applicationPermissions = new HashSet<ApplicationPermission>();

//        AccessControlList accessControlList = md.getAccessControlList();
//        SocialPermission defaultSocialPermission = SecurityFactory.createPermission(PermissionTypeEnum.SOCIAL);
//        PersistenceUtil.setDates(defaultSocialPermission);
//        defaultSocialPermission.setRelationshipType(NetworkTypeEnum.ALL_MEMBERS.name());
//        defaultSocialPermission.setDepth(NetworkTypeEnum.ALL_MEMBERS.getDepth());
//        socialPermissions.add(defaultSocialPermission);
//        accessControlList.setSocialPermissions(socialPermissions);
//
//        ApplicationPermission defaultApplicationPermission = SecurityFactory.createPermission(PermissionTypeEnum.APPLICATION);
//        PersistenceUtil.setDates(defaultApplicationPermission);
//        defaultApplicationPermission.setCategory(ApplicationPermissionCategoryEnum.PROFILE.name());
//        defaultApplicationPermission.setAccess(ApplicationPermissionAccessEnum.READ.name());
//        applicationPermissions.add(defaultApplicationPermission);
//        accessControlList.setApplicationPermissions(applicationPermissions);

//        PersistenceUtil.setDates(accessControlList);
//        Utils.applyGuid(accessControlList);
        mediaDao.save(md);
        assertNotNull(md.getGuid());
        assertNotNull(md.getVersion());

        mediaDao.delete(md);
    }

    @Test
    public void testLoadProfileMediaMetaDataByOwner() {
        final MediaMetaData md = mediaDao.loadProfileMediaMetaDataByOwner(new EntityReference(EntityType.USER, UserFixture.aj.getGuid()));
        assertNotNull(md);
    }

    @Test
    public void loadMediaLibrariesByOwnerTest() {
        List<MediaLibrary> ml = mediaDao.loadMediaLibrariesByOwner(new EntityReference(EntityType.USER, UserFixture.april.getGuid()));
        assertNotNull(ml);
        assertTrue(ml.size() > 0);
        assertNotNull(ml.get(0).getMedia());
    }

    @Test
    public void loadMediaLibraryByGuid() {
        MediaLibrary mediaLibrary = mediaDao.loadMediaLibraryByGuid("4e708fa87191f716b42dc111747170db347a7085");
        assertNotNull(mediaLibrary);
    }

    @Test
    public void testLoadingSavedMetaData() {
        MediaMetaData metaData = mediaDao.loadMediaMetaDataByGuid("163057a0d5cfdacad9db406e823df203eee6c788");
        assertNotNull(metaData);
    }

    @Test
    public void testLoadLatestActivity() {

        MediaMetaData metaData = mediaDao.loadLastMediaMetaData("105433d2148545d371fb1c71ef4851efb8c04392");
        assertNotNull(metaData);
    }

    @Test
    public void testLoadCorkboardCrud() {
        User aj = userDao.loadMiniProfileByUsername("aj");
        CorkboardImage ci = new CorkboardImage(aj.toEntityReference(), "slkdffdsj", 1, 2, 3, 23, 1.0);
        ci.setLocation("sdlkjfsd");
        ci.setFileName("sdfasdfdsf");
        mediaDao.save(ci);

        ci.setLocation("sd1fklsdfljk");
        mediaDao.save(ci);

        List<CorkboardImage> images = mediaDao.loadCorkboardImages(aj.getGuid());
        assertNotNull(images);
        assertFalse(images.isEmpty());
    }

    @Test
    public void testCrudMedia() {

        MediaMetaData md = ActivityFactory.createMedia();
        md.setFileName("myfile.png");
        md.setMimeType("image/png");
        md.setArtifactType(ArtifactType.IMAGE);
        Utils.applyGuid(md);
        EntityReference owner = User.createUser().toEntityReference();
        md.setArtifactOwner(owner);
        EntityReference recipient = User.createUser().toEntityReference();
        md.getRecipients().add(new MediaRecipient(recipient, md));
        md.setSize(823988932923898L);
//        md.setAccessControlList(null);
        md.setRecipientDepth(NetworkTypeEnum.ALL_MEMBERS);

        MediaLibrary library = new MediaLibrary();
        library.setOwner(md.getArtifactOwner());
        library.setMedia(newArrayList(md));
        library.setNetworkTypeEnum(NetworkTypeEnum.ALL_MEMBERS);

        library = mediaDao.save(library);

        MediaLibrary lib2 = mediaDao.loadMediaLibraryByGuid(library.getGuid());
        assertNotNull(lib2);
        assertEquals(1, lib2.getMedia().size());

        MediaMetaData md2 = ActivityFactory.createMedia();
        md2.setFileName("myfile.png");
        md2.setMimeType("image/png");
        md2.setArtifactType(ArtifactType.IMAGE);
        Utils.applyGuid(md2);
        EntityReference owner2 = User.createUser().toEntityReference();
        md2.setArtifactOwner(owner2);
        EntityReference recipient2 = User.createUser().toEntityReference();
        md2.getRecipients().add(new MediaRecipient(recipient2, md2));
        md2.setSize(823988932923898L);
        md2.setRecipientDepth(NetworkTypeEnum.ALL_MEMBERS);
        library.getMedia().add(md2);
        library.setDescription("sadlkjasfdlkjasdfldfsa");
        mediaDao.save(library);


        lib2 = mediaDao.loadMediaLibraryByGuid(library.getGuid());
        assertEquals(2, lib2.getMedia().size());


        int i = mediaDao.totalNumberOfAlbumsForUser(owner.getGuid());
        assertTrue(i > 0);


        mediaDao.delete(lib2);
    }

    @Test
    public void testLoadAvatarImage() {
        User aj = userDao.loadUserByUsername("aj");
        AvatarImage image = mediaDao.loadAvatarImage(EntityType.USER, aj.getGuid());
        assertNotNull(image);
        assertEquals(aj.getType(), image.getUserType());
    }


    @Test
    public void totalPhotosByAssociatedAlbumForUser() {
        User april = userDao.loadUserByUsername("april");
        // stupid just tests the query to make sure it doesn't blow up.
        mediaDao.totalPhotosByAssociatedAlbumForUser(april.getGuid());
    }


}
