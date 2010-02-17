/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blackbox.presentation.action.persona;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.bookmark.Bookmark;
import com.blackbox.foundation.bookmark.IBookmarkManager;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import static com.blackbox.presentation.action.util.JSONUtil.toJSON;
import com.blackbox.foundation.user.IUser;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WishListActionBean extends BaseBlackBoxActionBean {
    final private static Logger _logger = LoggerFactory.getLogger(WishListActionBean.class);
    @SpringBean("bookmarkManager")
    private IBookmarkManager bookmarkManager;
    @ValidateNestedProperties({
        @Validate(field = "guid", required = true, on = {"create", "jsonCreate"}),
        @Validate(field = "ownerType", required = true, on = {"listWithEntityType", "create", "jsonCreate"})
    })
    private EntityReference target;
    private Collection<String> tags;
    private Bookmark bookmark;
    private List<Bookmark> bookmarks;

    @DefaultHandler
    public Resolution getListAsJSON() throws JSONException {
        IUser user = getCurrentUser();
        assert user != null;
        bookmarks=new ArrayList();
        try{
            //TODO: not work!->bookmarkManager.loadByUserGuid();
        bookmarks = bookmarkManager.loadByUserGuid(user.getGuid());
        }catch(Exception e){
            _logger.error("bookmarkManager do not work");
        }
        

        getContext().getResponse().setHeader("Stripes-Success", "true");
        return new StreamingResolution("text", new StringReader(toJSONArray(bookmarks).toString()));

    }

//    public Resolution listWithEntityType() throws JSONException {
//        IUser user = getCurrentUser();
//        assert user != null;
//
//        bookmarks = bookmarkManager.loadByUserGuidAndToEntityType(user.getGuid(), target.getOwnerType());
//        if (getView() == ViewType.json) {
//            getContext().getResponse().setHeader("Stripes-Success", "true");
//            return new StreamingResolution("text", new StringReader(toJSONArray(bookmarks).toString()));
//        }
//
//        return new ForwardResolution("/dev/bookmarks.jsp");
//    }
    JSONArray toJSONArray(Collection<Bookmark> bookmarks) throws JSONException {
        JSONArray json = new JSONArray();
        for (Bookmark bookmark : bookmarks) {
            json.put(toJSON(bookmark));
        }
        return json;
    }

//    public Resolution create() throws JSONException {
//        IUser user = getCurrentUser();
//        assert user != null;
//
//        bookmark = bookmarkManager.createBookmark(user.getGuid(), target, tags);
//        if (getView() == ViewType.json) {
//            getContext().getResponse().setHeader("Stripes-Success", "true");
//            return new StreamingResolution("text", new StringReader(toJSON(bookmark).toString()));
//
//        }
//        return new ForwardResolution("/dev/bookmarks.jsp");
//    }
    public void setBookmarkManager(IBookmarkManager bookmarkManager) {
        this.bookmarkManager = bookmarkManager;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    public Collection<String> getTags() {
        return tags;
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
