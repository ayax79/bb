/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.persona;

import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.foundation.media.MediaLibrary.MediaLibraryType;
import com.blackbox.presentation.action.util.DateTimeUtils;
import static com.blackbox.presentation.action.util.JSONUtil.toJSON;
import com.blackbox.presentation.action.util.MediaUtil;
import com.blackbox.foundation.social.RelationshipNetwork;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.List;

@UrlBinding("/action/ajax/psshowalbum")
@Deprecated
public class PSPictureActionBean extends BasePersonaActionBean {

    @SpringBean("mediaManager") private IMediaManager mediaManager;
    @SpringBean("userManager") private IUserManager userManager;
    @SpringBean("personaHelper") protected PersonaHelper personaHelper;

    private List<MediaLibrary> mediaLibList;
    private MediaLibrary showingMediaLib;
    private List<String> mediaLibCoverList;
    private User user;
    private String identifier;
    private boolean owner;
    private boolean friend;
    private boolean blocked;


    public IMediaManager getContentManager() {
        return mediaManager;
    }

    public void setContentManager(IMediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    @Before
    public void preProcess() {
        user = userManager.loadUserByUsername(identifier);
        owner = isOwner(user);
        RelationshipNetwork network = getContext().getNetwork();
        friend = network.isFriend(user.getGuid()) || network.isInRelationship(user.getGuid());
        blocked = getContext().getBlocked().contains(user.toEntityReference());
    }

    @DefaultHandler
    public Resolution getAlbumListAsJSON() throws JSONException {
        // confirmUser();
        List<MediaLibrary> totalMediaLibList = mediaManager.loadMediaLibrariesForOwner(user.toEntityReference());
        List<MediaLibrary> albumList = MediaUtil.getMediaLibListByType(totalMediaLibList, MediaLibraryType.IMAGE);
//        albumList = MediaUtil.getMediaLibListBasedOnNetwork(albumList, isOwner(user), JspFunctions.isFriend(user), isFollowing(user), JspFunctions.isBlocked(user));

        JSONArray array = new JSONArray();
        for (MediaLibrary ml : albumList) {
            array.put(toJSON(ml));
        }
        getContext().getResponse().setHeader("Stripes-Success", "true");

        return new StreamingResolution("text", new StringReader(array.toString()));
    }

    public Resolution getPicsCount() throws JSONException {
        // confirmUser();
        List<MediaLibrary> totalMediaLibList = mediaManager.loadMediaLibrariesForOwner(user.toEntityReference());
        List<MediaLibrary> albumList = MediaUtil.getMediaLibListByType(totalMediaLibList, MediaLibraryType.IMAGE);
        int all = 0;
        int today = 0;
        for (MediaLibrary lib : albumList) {
            if (DateTimeUtils.isToday(lib.getCreated())) {
                if (lib.getMedia() != null) {
                    today = today + lib.getMedia().size();
                }
            }
            if (lib.getMedia() != null) {
                all = all + lib.getMedia().size();
            }
        }
        getContext().getResponse().setHeader("Stripes-Success", "true");
        JSONObject json = new JSONObject();
        json.put("all", all);
        if (isOwner(user)) {
            json.put("today", 0);
        } else {
            json.put("today", today);
        }

        return new StreamingResolution("text", new StringReader(json.toString()));
    }

    private void loadHelper() {
        String indexStr = getContext().getRequest().getParameter("index");
        if (indexStr == null) {
            indexStr = getContext().getRequest().getParameter("defaultAlbumIndex");
        }
        int index;
        List<MediaLibrary> totalMediaLibList = mediaManager.loadMediaLibrariesForOwner(user.toEntityReference());
        mediaLibList = MediaUtil.getMediaLibListByType(totalMediaLibList, MediaLibraryType.IMAGE);
//        mediaLibList = MediaUtil.getMediaLibListBasedOnNetwork(mediaLibList,
//                isOwner(user.toEntityReference()),
//                JspFunctions.isFriend(user),
//                isFollowing(user),
//                JspFunctions.isBlocked(user));
        mediaLibCoverList = MediaUtil.getMediaLibCoverList(mediaLibList);
        getContext().getRequest().setAttribute("mediaLibList", mediaLibList);
        getContext().getRequest().setAttribute("mediaLibCoverList", mediaLibCoverList);
        if (indexStr != null && indexStr.length() > 0) {
            index = Integer.parseInt(indexStr);
            if (index >= 0) {
                showingMediaLib = personaHelper.getShowingLib(mediaLibList, index);
                getContext().getRequest().setAttribute("showingMediaLib", showingMediaLib);
            } 
        }
    }

    @DontValidate
    public Resolution showalbum() {
        loadHelper();
        return new ForwardResolution("/WEB-INF/jsp/include/persona_album_bot.jspf");
    }

    @DontValidate
    public Resolution showalbumlist() {
        loadHelper();
        return new ForwardResolution("/WEB-INF/jsp/include/persona_album_top.jspf");
    }

    public void setMediaLibCoverList(List<String> mediaLibCoverList) {
        this.mediaLibCoverList = mediaLibCoverList;
    }

    public List<MediaLibrary> getMediaLibList() {
        return mediaLibList;
    }

    public void setMediaLibList(List<MediaLibrary> mediaLibList) {
        this.mediaLibList = mediaLibList;
    }

    public MediaLibrary getShowingMediaLib() {
        return showingMediaLib;
    }

    public void setShowingMediaLib(MediaLibrary showingMediaLib) {
        this.showingMediaLib = showingMediaLib;
    }

    public List<String> getMediaLibCoverList() {
        return mediaLibCoverList;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
