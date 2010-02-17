package com.blackbox.presentation.action.bookmark;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.bookmark.Bookmark;
import com.blackbox.foundation.bookmark.IBookmarkManager;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import static com.blackbox.presentation.action.util.JSONUtil.toJSON;
import com.blackbox.foundation.user.IUser;
import com.blackbox.foundation.user.User;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 *
 */
public class BookmarkActionBean extends BaseBlackBoxActionBean {

    @SpringBean("bookmarkManager")
    private IBookmarkManager bookmarkManager;

    @ValidateNestedProperties({
            @Validate(field = "guid", required = true, on = {"create", "jsonCreate"}),
            @Validate(field = "ownerType", required = true, on = {"listWithEntityType", "create", "jsonCreate"})
    })
    private EntityReference target;

    private Bookmark bookmark;

    private List<Bookmark> bookmarks = Collections.emptyList();

    @DefaultHandler
    public Resolution list() throws JSONException {
        IUser user = getCurrentUser();
        assert user != null;

        bookmarks = bookmarkManager.loadByUserGuid(user.getGuid());

        if (getView() == ViewType.json) {
            getContext().getResponse().setHeader("Stripes-Success", "true");
            return new StreamingResolution("text", new StringReader(toJSONArray(bookmarks).toString()));
        }

        return new ForwardResolution("/dev/bookmarks.jsp");
    }


    public Resolution listWithEntityType() throws JSONException {
        IUser user = getCurrentUser();
        assert user != null;

        bookmarks = bookmarkManager.loadByUserGuidAndToEntityType(user.getGuid(), target.getOwnerType());
        if (getView() == ViewType.json) {
            getContext().getResponse().setHeader("Stripes-Success", "true");
            return new StreamingResolution("text", new StringReader(toJSONArray(bookmarks).toString()));
        }

        return new ForwardResolution("/dev/bookmarks.jsp");
    }

    JSONArray toJSONArray(Collection<Bookmark> bookmarks) throws JSONException {
        JSONArray json = new JSONArray();
        for (Bookmark bookmark : bookmarks) {
            json.put(toJSON(bookmark));
        }
        return json;
    }

    public Resolution create() throws JSONException {
        User user = getCurrentUser();
        assert user != null;

        bookmark = new Bookmark();
        bookmark.setTarget(target);
        bookmark.setOwner(user);

        bookmark = bookmarkManager.createBookmark(bookmark);
        if (getView() == ViewType.json) {
            getContext().getResponse().setHeader("Stripes-Success", "true");
            return new StreamingResolution("text", new StringReader(toJSON(bookmark).toString()));

        }
        return new ForwardResolution("/dev/bookmarks.jsp");
    }

    public void setBookmarkManager(IBookmarkManager bookmarkManager) {
        this.bookmarkManager = bookmarkManager;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public Bookmark getBookmark() {
        return bookmark;
    }

    public EntityReference getTarget() {
        return target;
    }

    public void setTarget(EntityReference target) {
        this.target = target;
    }

}
