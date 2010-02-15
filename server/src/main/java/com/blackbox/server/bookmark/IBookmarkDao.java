package com.blackbox.server.bookmark;

import com.blackbox.group.Group;
import com.blackbox.bookmark.Bookmark;
import com.blackbox.EntityType;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.joda.time.DateTime;

/**
 *
 *
 */
public interface IBookmarkDao {
    @Transactional
    Bookmark save(Bookmark bookmark);

    void delete(Bookmark bookmark);

    List<Bookmark> loadByUserGuid(String userGuid);

    List<Bookmark> loadByUserGuidAndToEntityType(String userGuid, EntityType entityType);

    Bookmark loadByGuid(String guid);

    List<Bookmark> loadAllRelatedBookmarks(String userGuid);

    @Transactional
    void delete(String ownerGuid, String targetGuid);

    List<Bookmark> loadByTargetGuidAndBookmarkType(String userGuid, Bookmark.BookmarkType bookmarkType);

    List<Bookmark> loadByUserAndTargetGuidAndBookmarkType(String guid, String wishingGuid, Bookmark.BookmarkType bookmarkType);

    List<String> loadWishStatus(String fromEntity, String toEntity);

    int wishCount(String guid, DateTime startTime);
}
