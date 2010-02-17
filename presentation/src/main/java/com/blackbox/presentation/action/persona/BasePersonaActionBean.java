package com.blackbox.presentation.action.persona;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.IEntity;
import com.blackbox.foundation.bookmark.Bookmark;
import com.blackbox.foundation.bookmark.IBookmarkManager;
import com.blackbox.foundation.media.AvatarImage;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.AvatarCacheKey;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.Ignore;
import com.blackbox.foundation.social.Relationship;
import com.blackbox.foundation.user.User;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.yestech.cache.ICacheManager;

/**
 * @author A.J. Wright
 */
@SuppressWarnings({"ResolutionMethods"})
public abstract class BasePersonaActionBean extends BaseBlackBoxActionBean {

    @SpringBean("socialManager") protected ISocialManager socialManager;
    @SpringBean("bookmarkManager") protected IBookmarkManager bookmarkManager;
    @SpringBean("avatarCache") protected ICacheManager<AvatarCacheKey, AvatarImage> avatarCache;
    @SpringBean("personaHelper") protected PersonaHelper personaHelper;

    protected String returnPage;
    protected String description;

    @Override
    public MenuLocation getMenuLocation() {
        return MenuLocation.persona;
    }

    public String getReturnPage() {
        return returnPage;
    }

    public void setReturnPage(String returnPage) {
        this.returnPage = returnPage;
    }

    protected void flushPersonaCache() {
        personaHelper.flushPersonaPageCache(getContext());
    }


    protected void executeAccept(String targetGuid) {
        User current = getCurrentUser();
        flushPersonaCache();
        socialManager.acceptRequest(current.getGuid(), targetGuid);
    }

    protected void executeFollow(EntityReference toEntity) {
        User current = getCurrentUser();
        if (!isOwner(toEntity)) {
            Relationship r = Relationship.createRelationship(
                    current.toEntityReference(),
                    toEntity,
                    Relationship.RelationStatus.FOLLOW);
            socialManager.relate(r);
            flushPersonaCache();
        }
    }

    protected void executeIgnore(EntityReference target) {
        User current = getCurrentUser();
        if (!isOwner(target)) {
            socialManager.ignore(new Ignore(current.toEntityReference(), target, Ignore.IgnoreType.SOFT));
            flushPersonaCache();
        }
    }

    protected void executeWish(EntityReference target) {
        if (!isOwner(target)) {
            User current = getCurrentUser();
            Bookmark bm = Bookmark.createBookmark();
            bm.setType(Bookmark.BookmarkType.WISH);
            bm.setOwner(current);
            bm.setTarget(target);
            bm.setDescription(description);
            flushPersonaCache();
            bookmarkManager.createBookmark(bm);
            avatarCache.flush(new AvatarCacheKey(current.getGuid(), target.getGuid()));
            avatarCache.flush(new AvatarCacheKey(target.getGuid(), current.getGuid()));
        }
    }


    protected boolean isOwner(IEntity target) {
        return target!= null && target.getGuid().equals(getCurrentUser().getGuid());
    }

    protected ISocialManager getSocialManager() {
        return socialManager;
    }

    protected IBookmarkManager getBookmarkManager() {
        return bookmarkManager;
    }

    protected ICacheManager<AvatarCacheKey, AvatarImage> getAvatarCache() {
        return avatarCache;
    }

    @Override
    public boolean isHasIntro() {
        return true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
