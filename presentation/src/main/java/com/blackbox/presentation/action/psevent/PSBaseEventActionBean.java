package com.blackbox.presentation.action.psevent;

import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.media.SessionImageActionBean;
import com.blackbox.presentation.action.util.PresentationUtil;
import com.blackbox.presentation.extension.BlackBoxContext;
import com.blackbox.foundation.user.User;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.controller.LifecycleStage;
import org.yestech.rpx.objectmodel.RPXUtil;
import org.yestech.lib.util.Pair;
import org.yestech.lib.util.ITuple;

import javax.servlet.http.HttpSession;

public class PSBaseEventActionBean extends BaseBlackBoxActionBean {

    protected static final String OCCASION_EDIT_PAGE_TOKEN = "__occasion_edit_page_token__";
    private static final String OCCASION = "com.blackbox.presentation.action.psevent.OCCASION_SESSION_KEY";
    protected String defaultImageLocation;
    protected String defaultVideoLocation;
    protected boolean unsaved;

    protected Occasion occasion;

    protected void removedEditOccasionMode() {
        getContext().getRequest().getSession().removeAttribute(OCCASION_EDIT_PAGE_TOKEN);
    }

    protected void setEditOccasionMode(String guid) {
        getContext().getRequest().getSession().setAttribute(OCCASION_EDIT_PAGE_TOKEN, Pair.create(guid, Boolean.TRUE));
    }

    protected Pair<String, Boolean> getEditOccasionMode() {
        Pair<String, Boolean> editMode = (Pair<String, Boolean>) getContext().getRequest().getSession().getAttribute(OCCASION_EDIT_PAGE_TOKEN);
        return editMode;
    }

    @Before(stages = LifecycleStage.BindingAndValidation)
    public void pullOccasionFromSession() {
        occasion = (Occasion) getContext().getRequest().getSession().getAttribute(OCCASION);
    }

    @net.sourceforge.stripes.action.After
    public void replaceSessionOccasion() {
        BlackBoxContext blackBoxContext = getContext();
        if (blackBoxContext.getValidationErrors().isEmpty()) {
            blackBoxContext.getRequest().getSession().setAttribute(OCCASION, occasion);
        }
    }

    public boolean isViewerOwner() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return occasion != null && occasion.getOwner().getGuid().equals(currentUser.getGuid());
    }

    public void setUnsaved(boolean unsaved) {
        this.unsaved = unsaved;
    }

    public boolean isUnsaved() {
        if (!unsaved) {
            unsaved = occasion != null && occasion.getVersion() == null;
        }
        return unsaved;
    }

    public String getDefaultImageLocation() {
        return defaultImageLocation;
    }

    public void setDefaultImageLocation(String defaultImageLocation) {
        this.defaultImageLocation = defaultImageLocation;
    }

    public String getDefaultVideoLocation() {
        return defaultVideoLocation;
    }

    public void setDefaultVideoLocation(String defaultVideoLocation) {
        this.defaultVideoLocation = defaultVideoLocation;
    }

    protected FileBean getTempImageFromHttpSession() {
        return (FileBean) getContext().getRequest().getSession().getAttribute(SessionImageActionBean.SESSION_IMAGE_PARAM);
    }

    protected void saveTempImageToHttpSession(FileBean fileData) throws Exception {
        resetSessionImage(fileData);
    }

    protected void removeSessionTempImage() {
        getContext().getRequest().getSession().removeAttribute(SessionImageActionBean.SESSION_IMAGE_PARAM);
    }

    protected void resetSessionImage(FileBean fileData) throws Exception {
        FileBean old = (FileBean) getContext().getRequest().getSession().getAttribute(SessionImageActionBean.SESSION_IMAGE_PARAM);
        if (old != null) {
            old.delete();
        }
        getContext().getRequest().getSession().setAttribute(SessionImageActionBean.SESSION_IMAGE_PARAM, fileData);
    }

    public String getEncodedUrl() {
        String prefix = PresentationUtil.getProperty("presentation.url");
        String myurl = prefix + "/psevent/PSEventRpx.action?_eventName=handleRpx&handleRpx=1";
        return RPXUtil.uriEncode(myurl);
    }

    protected void cleanHttpSession() {
        HttpSession session = getContext().getRequest().getSession();
        session.removeAttribute(OCCASION_EDIT_PAGE_TOKEN);
        session.removeAttribute(OCCASION);
        session.removeAttribute("defaultImageLocation");
        session.removeAttribute("defaultVideoLocation");
        session.removeAttribute("image_x");
        session.removeAttribute("image_y");
        session.removeAttribute("image_w");
        session.removeAttribute("image_h");
        session.removeAttribute("allImages");
        session.removeAttribute("userType");
        session.removeAttribute("OwnerEdit");
        session.removeAttribute("event_image_location");
        session.removeAttribute("event_video_location");
        session.removeAttribute("tempbbUser");
        session.removeAttribute(SessionImageActionBean.SESSION_IMAGE_PARAM);
    }

    public Occasion getOccasion() {
        return occasion;
    }

    public void setOccasion(Occasion occasion) {
        this.occasion = occasion;
    }

	@Override
	public MenuLocation getMenuLocation() {
		return MenuLocation.event;
	}

    @Override
    public boolean isHasIntro() {
        return true;
    }
}
