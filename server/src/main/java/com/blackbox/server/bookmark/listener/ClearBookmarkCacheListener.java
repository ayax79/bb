package com.blackbox.server.bookmark.listener;

import com.blackbox.bookmark.Bookmark;
import com.blackbox.bookmark.WishStatus;
import com.blackbox.bookmark.UserWish;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.bookmark.WishStatusCacheKey;
import com.blackbox.server.bookmark.WishCountCacheKey;
import com.blackbox.server.bookmark.event.AbstractBookmarkChangeEvent;
import org.yestech.cache.ICacheManager;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;
import org.joda.time.DateTime;
import org.joda.time.DateMidnight;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author A.J. Wright
 */
@ListenedEvents({com.blackbox.server.bookmark.event.CreateBookmarkEvent.class, com.blackbox.server.bookmark.event.DeleteBookmarkEvent.class})
@AsyncListener
public class ClearBookmarkCacheListener extends BaseBlackboxListener<AbstractBookmarkChangeEvent, Object> {

    @Resource(name = "wishStatusCache")
    private ICacheManager<WishStatusCacheKey, WishStatus> wishStatusCache;

    @Resource(name = "userWishCache")
    private ICacheManager<String, List<UserWish>> userWishCache;

    @Resource(name = "wishCountCache")
    private ICacheManager<WishCountCacheKey, Integer> wishCountCache;

    @Override
    public void handle(AbstractBookmarkChangeEvent event, ResultReference<Object> objectResultReference) {
        Bookmark bookmark = event.getType();
        if (bookmark.getType() == Bookmark.BookmarkType.WISH) {
            wishStatusCache.flush(new WishStatusCacheKey(bookmark.getTarget().getGuid(), bookmark.getOwner().getGuid()));
            wishStatusCache.flush(new WishStatusCacheKey(bookmark.getOwner().getGuid(), bookmark.getTarget().getGuid()));
            userWishCache.flush(bookmark.getTarget().getGuid());
            userWishCache.flush(bookmark.getOwner().getGuid());
            DateTime dt = new DateMidnight().minusMonths(1).toDateTime();
            wishCountCache.flush(new WishCountCacheKey(bookmark.getTarget().getGuid(), dt));
            wishCountCache.flush(new WishCountCacheKey(bookmark.getOwner().getGuid(), dt));
        }
    }

}
