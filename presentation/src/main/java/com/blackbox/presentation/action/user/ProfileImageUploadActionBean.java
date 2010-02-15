package com.blackbox.presentation.action.user;

import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.FileBean;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringReader;

import static com.blackbox.presentation.action.media.SessionImageActionBean.SESSION_IMAGE_PARAM;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;

/**
 * @author A.J. Wright
 */
public class ProfileImageUploadActionBean extends BaseBlackBoxActionBean {

    private FileBean userImage;

    @DontValidate
    public Resolution uploadImage() throws IOException {
        clearOldSessionImage();
        getContext().getResponse().setHeader("Stripes-Success", "true");
        return new StreamingResolution("text", new StringReader("success"));
    }

    protected void clearOldSessionImage() throws IOException {
        HttpSession session = getContext().getRequest().getSession();
        FileBean old = (FileBean) session.getAttribute(SESSION_IMAGE_PARAM);
        if (old != null) {
            old.delete();
        }
        session.setAttribute(SESSION_IMAGE_PARAM, userImage);
    }

    public FileBean getUserImage() {
        return userImage;
    }

    public void setUserImage(FileBean userImage) {
        this.userImage = userImage;
    }
}
