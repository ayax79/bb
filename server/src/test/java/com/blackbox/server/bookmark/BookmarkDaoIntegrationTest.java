package com.blackbox.server.bookmark;

import com.blackbox.EntityType;
import com.blackbox.bookmark.Bookmark;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.user.IUserDao;
import com.blackbox.user.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 *
 *
 */
public class BookmarkDaoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    protected IBookmarkDao bookmarkDao;

    @Autowired
    protected IUserDao userDao;


    @Test
    public void testSave() {
        User owner = userDao.loadUserByGuid("1");
        User toEntity = User.createUser();
        assertNotNull(toEntity);
        Bookmark bookmark = Bookmark.createBookmark();
        bookmark.setOwner(owner);
        bookmark.setTarget(toEntity.toEntityReference());
        bookmark.setType(Bookmark.BookmarkType.WISH);
        bookmarkDao.save(bookmark);
        assertNotNull(bookmark.getGuid());
        assertNotNull(bookmark.getCreated());
        assertNotNull(bookmark.getModified());

        bookmark.setAcknowledged(true);
        bookmarkDao.save(bookmark);

        Bookmark result = bookmarkDao.loadByGuid(bookmark.getGuid());
        assertNotNull(result);

        List<Bookmark> list = bookmarkDao.loadByUserGuid("1");
        assertNotNull(list);
        assertFalse(list.isEmpty());

        list = bookmarkDao.loadAllRelatedBookmarks("1");
        assertNotNull(list);
        assertFalse(list.isEmpty());

        bookmarkDao.delete(bookmark);
    }

    @Test
    public void testLoadByUserGuidAndToEntityOwnerType() {
        List<Bookmark> list = bookmarkDao.loadByUserGuidAndToEntityType("e62f631581a568075a543751686c00c6b3e39da7", EntityType.USER);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }
}
