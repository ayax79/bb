package com.blackbox.server.bookmark;

import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.bookmark.Bookmark;
import com.blackbox.foundation.bookmark.IBookmarkManager;
import com.blackbox.foundation.bookmark.UserWish;
import com.blackbox.foundation.bookmark.WishStatus;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.PaginationResults;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Affirm;
import com.blackbox.foundation.util.Count;
import com.blackbox.server.bookmark.event.CreateBookmarkEvent;
import com.blackbox.server.bookmark.event.DeleteBookmarkEvent;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.yestech.cache.ICacheManager;
import org.yestech.event.multicaster.BaseServiceContainer;
import org.yestech.lib.util.Pair;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.blackbox.foundation.util.PaginationUtil.buildPaginationResults;

/**
 *
 *
 */
@Service("bookmarkManager")
public class BookmarkManager extends BaseServiceContainer implements IBookmarkManager {

    private static final Logger log = LoggerFactory.getLogger(BookmarkManager.class);

    @Resource(name = "bookmarkDao")
    private IBookmarkDao bookmarkDao;

    @Resource(name = "userManager")
    private IUserManager userManager;

    @Resource(name = "wishStatusCache")
    private ICacheManager<WishStatusCacheKey, WishStatus> wishStatusCache;

    @Resource(name = "userWishCache")
    private ICacheManager<String, List<UserWish>> userWishCache;

    @Resource(name = "wishCountCache")
    private ICacheManager<WishCountCacheKey, Integer> wishCountCache;

    @Override
    public Bookmark createBookmark(Bookmark bookmark) {
        User owner = userManager.loadUserByGuid(bookmark.getOwner().getGuid());
        bookmark.setOwner(owner);

        Bookmark b = bookmarkDao.save(bookmark);
        if (b.getTarget().getOwnerType() == EntityType.USER) {
            userManager.setSessionRefreshNeeded(b.getTarget().getGuid());
        }
        userManager.setSessionRefreshNeeded(b.getOwner().getGuid());
        //todo:  send notification
        getEventMulticaster().process(new CreateBookmarkEvent(b));
        return b;
    }

    @Override
    public WishStatus loadWishStatus(String fromEntity, String toEntity) {

        WishStatus status = wishStatusCache.get(new WishStatusCacheKey(fromEntity, toEntity));

        if (status == null) {

            List<String> guids = bookmarkDao.loadWishStatus(fromEntity, toEntity);

            boolean wished = guids.contains(fromEntity);
            boolean wishedBy = guids.contains(toEntity);

            if (wished && wishedBy) {
                status = WishStatus.MUTUAL;
            } else if (wished) {
                status = WishStatus.WISHED;
            } else if (wishedBy) {
                status = WishStatus.WISHED_BY;
            } else {
                status = WishStatus.NEITHER;
            }
            wishStatusCache.put(new WishStatusCacheKey(fromEntity, toEntity), status);
        }
        return status;
    }

    @Override
    public PaginationResults<UserWish> loadUserWishes(final String guid, int startIndex, int maxResults) {
        List<UserWish> wishes = userWishCache.get(guid);
        boolean fromCache = true;
        if (wishes == null) {
            List<Bookmark> bookmarks = bookmarkDao.loadAllRelatedBookmarks(guid);
            HashMap<Pair<String, String>, UserWish> wishMap = new HashMap<Pair<String, String>, UserWish>();

            for (Bookmark b : bookmarks) {
                Pair<String, String> bookmark = new Pair<String, String>(b.getOwner().getGuid(), b.getTarget().getGuid());
                String description = b.getDescription();

                UserWish wish = wishMap.get(bookmark);
                if (wish == null) {
                    wish = wishMap.get(new Pair<String, String>(bookmark.getSecond(), bookmark.getFirst()));
                }
                boolean reverse = !guid.equals(bookmark.getFirst());

                // if no user wish already exists
                if (wish == null) {
                    String from = bookmark.getFirst();
                    String to = bookmark.getSecond();
                    User user;
                    WishStatus wishStatus;
                    if (!from.equals(guid)) {
                        user = userManager.loadUserByGuid(from);
                        wishStatus = loadWishStatus(guid, from);
                    } else {
                        user = userManager.loadUserByGuid(to);
                        wishStatus = loadWishStatus(guid, to);
                    }
                    wish = new UserWish(user, wishStatus);
                    wishMap.put(bookmark, wish);
                }

                if (reverse) {
                    wish.setFromDescription(description);
                } else {
                    wish.setToDescription(description);
                }

            }

            wishes = new ArrayList<UserWish>(wishMap.values());
            userWishCache.put(guid, wishes);
            fromCache = false;
        }
        return buildPaginationResults(wishes, startIndex, maxResults);
    }

    @Override
    public Bookmark loadBookmark(String guid) {
        return bookmarkDao.loadByGuid(guid);
    }

    @Override
    public Bookmark updateBookmark(Bookmark bookmark) {
        return createBookmark(bookmark);
    }

    @Override
    public void deleteBookmark(String guid) {
        Bookmark bookmark = bookmarkDao.loadByGuid(guid);
        Affirm.isNotNull(bookmark, "bookmark", "Unable to find bookmark by for that guid: " + guid, IllegalArgumentException.class);
        bookmarkDao.delete(bookmark);
        getEventMulticaster().process(new DeleteBookmarkEvent(bookmark));
    }

    @Override
    public void deleteBookmark(String ownerGuid, String targetGuid) {
        bookmarkDao.delete(ownerGuid, targetGuid);
    }

    @Override
    public List<Bookmark> loadByUserGuid(String userGuid) {
        return bookmarkDao.loadByUserGuid(userGuid);
    }

    @Override
    public List<Bookmark> loadByUserGuidAndToEntityType(String userGuid, EntityType toEntityType) {
        return bookmarkDao.loadByUserGuidAndToEntityType(userGuid, toEntityType);
    }

    @Override
    public Count monthlyWishCount(String guid) {
        DateTime startTime = new DateMidnight().minusMonths(1).toDateTime();
        WishCountCacheKey key = new WishCountCacheKey(guid, startTime);
        Integer count = wishCountCache.get(key);
        if (count == null) {
            count = bookmarkDao.wishCount(guid, startTime);
            wishCountCache.put(key, count);
        }
        return new Count(count);
    }

    public void setBookmarkDao(IBookmarkDao bookmarkDao) {
        this.bookmarkDao = bookmarkDao;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

}
