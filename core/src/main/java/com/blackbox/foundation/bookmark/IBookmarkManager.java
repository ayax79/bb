package com.blackbox.foundation.bookmark;

import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.user.PaginationResults;
import com.blackbox.foundation.util.Count;

import java.util.List;

/**
 *
 *
 */
public interface IBookmarkManager {

    Bookmark createBookmark(Bookmark bookmark);

    void deleteBookmark(String guid);

    List<Bookmark> loadByUserGuid(String userGuid);

    List<Bookmark> loadByUserGuidAndToEntityType(String userGuid, EntityType toEntityType);

    Bookmark updateBookmark(Bookmark bookmark);

    void deleteBookmark(String ownerGuid, String targetGuid);

    Bookmark loadBookmark(String guid);

    WishStatus loadWishStatus(String fromEntity, String toEntity);

    PaginationResults<UserWish> loadUserWishes(String guid, int startIndex, int maxResults);

    Count monthlyWishCount(String guid);
}
