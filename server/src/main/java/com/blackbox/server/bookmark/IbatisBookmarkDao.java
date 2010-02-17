package com.blackbox.server.bookmark;

import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.bookmark.Bookmark;
import static com.blackbox.foundation.bookmark.Bookmark.BookmarkType.WISH;
import com.blackbox.server.util.PersistenceUtil;
import org.joda.time.DateTime;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */
@SuppressWarnings({"unchecked"})
@Repository("bookmarkDao")
public class IbatisBookmarkDao implements IBookmarkDao {

    @Resource
    SqlSessionOperations template;

    @Transactional
    public Bookmark save(Bookmark bookmark) {
        PersistenceUtil.insertOrUpdate(bookmark, template, "Bookmark.insert", "Bookmark.update");
        return bookmark;
    }

    @Transactional
    public void delete(Bookmark bookmark) {
        template.delete("Bookmark.delete", bookmark.getGuid());
    }

    @Transactional
    public void delete(final String ownerGuid, final String targetGuid) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("ownerGuid", ownerGuid);
        params.put("targetGuid", targetGuid);
        template.delete("Bookmark.deleteByTargetAndOwner", params);
    }

    public Bookmark loadByGuid(String guid) {
        return (Bookmark) template.selectOne("Bookmark.load",  guid);
    }

    public List<Bookmark> loadByUserGuid(String userGuid) {
        return template.selectList("Bookmark.loadByUserGuid", userGuid);
    }

    public List<Bookmark> loadByUserGuidAndToEntityType(String userGuid, EntityType entityType) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("userGuid", userGuid);
        params.put("type", entityType);
        return template.selectList("Bookmark.loadByUserGuidAndEntityType", params);

    }

    public List<Bookmark> loadAllRelatedBookmarks(String userGuid) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", userGuid);
        params.put("type", WISH);
        
        return template.selectList("Bookmark.loadAllRelated", params);
    }

    @Override
    public List<Bookmark> loadByTargetGuidAndBookmarkType(String userGuid, Bookmark.BookmarkType bookmarkType) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", userGuid);
        params.put("type", bookmarkType);
        return template.selectList("Bookmark.loadByTargetGuidAndBookmarkType", params);
    }

    @Override
    public List<Bookmark> loadByUserAndTargetGuidAndBookmarkType(String guid, String wishingGuid, Bookmark.BookmarkType bookmarkType) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", guid);
        params.put("type", bookmarkType);
        params.put("userGuid", wishingGuid);
        return template.selectList("Bookmark.loadByUserAndTargetGuidAndBookmarkType", params);
    }

    @Override
    public List<String> loadWishStatus(final String fromEntity, final String toEntity) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("fromEntity", fromEntity);
        params.put("toEntity", toEntity);
        params.put("type", WISH);

        return template.selectList("Bookmark.loadWishStatus", params);
    }

    @Override
    public int wishCount(String guid, DateTime startTime) {
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("guid", guid);
        params.put("created", startTime);
        params.put("type", WISH);
        return (Integer) template.selectOne("Bookmark.wishCount", params);
    }

}
