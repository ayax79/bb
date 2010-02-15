package com.blackbox.bookmark;

import com.blackbox.EntityType;
import com.blackbox.user.PaginationResults;
import com.blackbox.util.Count;

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
