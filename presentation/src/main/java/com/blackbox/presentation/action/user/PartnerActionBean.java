package com.blackbox.presentation.action.user;

import com.blackbox.foundation.EntityType;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import org.json.JSONObject;
import org.json.JSONException;

import javax.servlet.http.Cookie;

import static com.blackbox.presentation.action.util.PresentationUtil.getProperty;
import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithJson;

import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.AvatarImage;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.EntityReference;

/**
 * @author A.J. Wright
 */
@UrlBinding("/partner/{identifier}")
public class PartnerActionBean implements ActionBean {

    public static final String AFFILIATE_COOKIE_KEY = "blackbox.affiliate";

    @SpringBean("userManager")
    IUserManager userManager;

    @SpringBean("mediaManager")
    IMediaManager mediaManager;

    protected ActionBeanContext context;
    protected String identifier;

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    @Override
    public ActionBeanContext getContext() {
        return context;
    }


    @DefaultHandler
    public Resolution begin() {

        if (isNotBlank(identifier)) {
            Cookie cookie = new Cookie(AFFILIATE_COOKIE_KEY, identifier);
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            getContext().getResponse().addCookie(cookie);
        }

        return new RedirectResolution(getProperty("partner.landing.page"), false);
    }

    public Resolution info() {

        User user = userManager.loadUserByUsername(identifier);
        if (user != null) {
            JSONObject jo = new JSONObject();
            try {
                jo.put("username", user.getUsername());
                jo.put("bio", user.getProfile().getLookingForExplain());

                AvatarImage image = mediaManager.loadAvatarImageFor(EntityType.USER, user.getGuid(), null);
                if (image != null) {
                    jo.put("avatarLocation", image.getImageLink());
                }

                MediaMetaData data = mediaManager.loadProfileMetaMediaData(new EntityReference(EntityType.USER, user.getGuid()));
                if (data != null) {
                    jo.put("profileImageLocation", data.getLocation());
                }

                return createResolutionWithJson(getContext(), jo);

            } catch (JSONException e) {
                return new ErrorResolution(500, e.getMessage());
            }
        } else {
            return new ErrorResolution(404);
        }
    }


    /**
     * The identifier of the affiliate. This can either be a username or guid.
     *
     * @param identifier The identifier of the affilate.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

}
