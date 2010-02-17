package com.blackbox.server.media;

import com.blackbox.foundation.*;
import com.blackbox.foundation.bookmark.IBookmarkManager;
import com.blackbox.foundation.media.AvatarImage;
import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.server.user.IUserDao;
import com.blackbox.foundation.user.User;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertNotNull;
import static org.apache.commons.io.FileUtils.openInputStream;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Matchers;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.yestech.cache.ICacheManager;
import org.yestech.event.ResultReference;
import org.yestech.publish.objectmodel.PublisherProperties;

import java.io.*;
import java.util.List;

import static java.lang.String.valueOf;

/**
 *
 *
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore
public class MediaManagerUnitTest {

    @Mock
    IEntity ownerMock;
    @Mock ICacheManager<String, AvatarImage> avatarImageCache;
    @Mock IMediaDao mediaDao;
    @Mock IBookmarkManager bookmarkManager;
    @Mock IUserDao userDao;
    @Mock ICacheManager<String, List<MediaLibrary>> ownerMediaLibraryCache;
    MediaManager mediaManager;

    File tempDir;

    @Before
    public void setup() throws IOException {
        // hack to create a temporary dir
        tempDir = File.createTempFile("tmpdir", "");
        assertTrue(tempDir.delete());
        assertTrue(tempDir.mkdirs());

        mediaManager = new MediaManager();
        mediaManager.mediaDao = mediaDao;
        mediaManager.avatarImageCache = avatarImageCache;
        mediaManager.bookmarkManager = bookmarkManager;
        mediaManager.userDao = userDao;
        mediaManager.ownerMediaLibraryCache = ownerMediaLibraryCache;
    }


    @Test
    public void testStore() throws IOException {
        final String guid = Utils.generateGuid();
        when(ownerMock.getOwnerType()).thenReturn(EntityType.USER);
        when(ownerMock.getGuid()).thenReturn(guid);

//        listener.setPublishTempDir(tempDir);
        PublisherProperties properties = new PublisherProperties();
//        listener.setPublisherProperties(properties);
        MediaMetaData md = MediaMetaData.createMediaMetaData();
        md.setLibrary(true);
        md.setArtifactOwner(EntityReference.createEntityReference(ownerMock));

        ResultReference<MediaMetaData> ref = new ResultReference<MediaMetaData>();
        ref.setResult(md);

        String content = "sdlkjdslkjsdflksfdjjfdsljfdsljkfdsjlkdsfjlkdsfljkd";
//        listener.store(md, new ByteArrayInputStream(content.getBytes("UTF-8")));

        File createdFile = new File(tempDir, valueOf(md.getGuid()));
        assertTrue(createdFile.exists());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = openInputStream(createdFile);

        IOUtils.copy(in, out);
        IOUtils.closeQuietly(in);
        IOUtils.closeQuietly(out);

        byte[] result = out.toByteArray();
        String stringResult = new String(result, "UTF-8");
        assertEquals(content, stringResult);
    }

    @Test
    public void cleanFileNameTest() {
        String file = "Photo 1.png";
        MediaManager mm = new MediaManager();
        MediaMetaData md = new MediaMetaData();
        md.setFileName(file);
        mm.cleanFileName(md);
        assertEquals("Photo_1.png", md.getFileName());
    }

    @Test
    public void loadAvatarImageForTestCacheMiss() {

        EntityType entityType = EntityType.USER;
        String guid = "23o8qa7tu3p;eas";
        String caller = "vsa;li3suzatxel";
        String username = "s;ldkajv;sad";

        AvatarImage image = new AvatarImage(guid, username, User.UserType.FOUNDER, "as;dlfjadfs");
        when(mediaDao.loadAvatarImage(entityType, guid)).thenReturn(image);

        AvatarImage result = mediaManager.loadAvatarImageFor(entityType, guid, caller);
        assertEquals(guid, result.getEntityGuid());
        assertEquals(username, result.getName());

        verify(avatarImageCache, atMost(1)).put(guid, image);
        verify(avatarImageCache, atLeast(1)).get(guid);
    }

    @Test
    public void loadAvatarImageForTestNoAvatar() {

        EntityType entityType = EntityType.USER;
        String guid = "23o8qa7tu3p;eas";
        String caller = "vsa;li3suzatxel";
        String username = "s;ldkajv;sad";
        User user = new User(username, guid, null, Status.ENABLED, User.UserType.NORMAL);

        when(userDao.loadUserByGuid(guid)).thenReturn(user);

        AvatarImage result = mediaManager.loadAvatarImageFor(entityType, guid, caller);
        assertEquals(guid, result.getEntityGuid());
        assertEquals(username, result.getName());

        ArgumentCaptor<AvatarImage> captor = ArgumentCaptor.forClass(AvatarImage.class);
        verify(avatarImageCache).put(Matchers.eq(guid), captor.capture());

        AvatarImage result2 = captor.getValue();
        assertEquals(guid, result2.getEntityGuid());
        assertEquals(username, result2.getName());
    }

    @Test
    public void testMediaLibrary() {

        MediaLibrary library = new MediaLibrary();
        library.setOwner(User.createUser().toEntityReference());
        MediaMetaData mmd =  new MediaMetaData();
        library.setMedia(newArrayList(mmd));
        when(this.mediaDao.save(library)).thenReturn(library);

        MediaLibrary ml = mediaManager.saveMediaLibrary(library);
        assertNotNull(ml);

        verify(this.ownerMediaLibraryCache).flush(library.getOwner().getGuid());
        verify(this.mediaDao).save(library);
    }

}
