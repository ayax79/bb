package com.blackbox.presentation.action.util;

import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.IEntity;
import com.blackbox.foundation.activity.ActivityProfile;
import com.blackbox.foundation.media.AvatarImage;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.presentation.extension.BlackBoxContext;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.user.IUser;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.ExternalConfigPropertyConfigurer;
import com.blackbox.foundation.util.NameInfo;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.context.WebApplicationContext;
import org.yestech.cache.ICacheManager;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import static com.blackbox.presentation.action.util.JspFunctions.*;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.springframework.web.context.WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;

/**
 * Various utils for the presentation layer
 */
public final class PresentationUtil {

    private PresentationUtil() {
    }

    public static String getProperty(String key) {
        ExternalConfigPropertyConfigurer props =
                (ExternalConfigPropertyConfigurer) getSpringContext(getRequest().getSession()).getBean("presentation-properties");
        return props.getProperty(key);
    }

    public static Resolution createResolutionWithText(ActionBeanContext context, String text) {
        if (text == null) text = "";
        context.getResponse().setHeader("Stripes-Success", "true");
        return new StreamingResolution("text/plain", new StringReader(text));
    }

    public static Resolution createJSONResolutionWithText(ActionBeanContext context, String text) {
        if (text == null) text = "";
        context.getResponse().setHeader("Stripes-Success", "true");
        return new StreamingResolution("text/json", new StringReader(text));
    }

    public static Resolution createResolutionPrivacyError(ActionBeanContext context) {
        return createResolutionWithText(context, "not today son....");
    }

    public static Resolution createResolutionWithJson(ActionBeanContext context, JSONObject json) {
        return createResolutionWithText(context, json.toString());
    }

    public static Resolution createResolutionWithJsonArray(ActionBeanContext context, JSONArray json) {
        return createResolutionWithText(context, json.toString());
    }

    public static WebApplicationContext getSpringContext(HttpSession session) {
        return (WebApplicationContext) session.getServletContext()
                .getAttribute(ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }


    public static boolean isExceptionType(Exception e, Class<? extends Exception> clazz) {
        if (clazz.isAssignableFrom(e.getClass())) {
            return true;
        }
        if (e.getCause() != null && clazz.isAssignableFrom(e.getCause().getClass())) {
            return true;
        }
        //noinspection RedundantIfStatement
        if (e.getCause() != null && e.getCause().getCause() != null && clazz.isAssignableFrom(e.getCause().getCause().getClass())) {
            return true;
        }
        return false;
    }

    public static byte[] buildByteArray(FileBean fileData) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(fileData.getInputStream(), out);
        return out.toByteArray();
    }

    public static MediaMetaData buildMediaMetaData(FileBean fileBean, User user, String comment) {

        MediaMetaData metaData = MediaMetaData.createMediaMetaData();
        populateMediaMetaData(metaData, fileBean, user, comment);
        return metaData;
    }

    public static void populateMediaMetaData(MediaMetaData metaData, FileBean fileBean, User user, String comment) {
        String fileName = StringUtils.replace(getName(fileBean.getFileName()), " ", "_");
        metaData.setArtifactOwner(user.toEntityReference());
        metaData.setArtifactType(ArtifactType.IMAGE);
        metaData.setFileName(fileName);
        metaData.setMimeType(fileBean.getContentType());
        metaData.setSize(fileBean.getSize());
        metaData.setComment(comment);
        metaData.setRecipientDepth(NetworkTypeEnum.SELF);
        ActivityProfile profile = new ActivityProfile();
        profile.setSenderDisplayName(user.getUsername());
        metaData.setSenderProfile(profile);
    }

    public static void populateMetaData(MediaMetaData metaData, IUser user, String comment) {
        metaData.setArtifactOwner(user.toEntityReference());
        metaData.setArtifactType(ArtifactType.IMAGE);
        metaData.setMimeType("image/png");
        metaData.setFileName("my_photo.png");
        metaData.setComment(comment);
        ActivityProfile profile = new ActivityProfile();
        User currentUser = getCurrentUser();
        profile.setSenderDisplayName(currentUser.getUsername());
        profile.setSenderAvatarImage(getAvatarImage(currentUser.toEntityReference()).getImageLink());
        metaData.setSenderProfile(profile);
    }

    @SuppressWarnings({"unchecked"})
    public static AvatarImage getAvatarImage(EntityType entityType, String guid) {
        BlackBoxContext actionBeanContext = (BlackBoxContext) PresentationResourceHolder.getContext();
        HttpServletRequest request = actionBeanContext.getRequest();
        WebApplicationContext context = getSpringContext(request.getSession());

        User user = actionBeanContext.getUser();

        String currentUserGuid = null;
        if (user != null) {
            currentUserGuid = user.getGuid();
        }

        AvatarCacheKey key = new AvatarCacheKey(currentUserGuid != null ? currentUserGuid : "public", guid);
        ICacheManager<AvatarCacheKey, AvatarImage> avatarCache = (ICacheManager<AvatarCacheKey, AvatarImage>) context.getBean("avatarCache");

        AvatarImage avatarImage = avatarCache.get(key);
        if (avatarImage == null) {

            IMediaManager mediaManager = (IMediaManager) context.getBean("mediaManager");
            avatarImage = mediaManager.loadAvatarImageFor(entityType == null ? EntityType.USER : entityType, guid, currentUserGuid);
            avatarCache.put(key, avatarImage);
        }

        return avatarImage;
    }

    public static AvatarImage getAvatarImage(EntityReference er) {
        return getAvatarImage(er.getOwnerType(), er.getGuid());
    }

    @SuppressWarnings({"unchecked"})
    public static NameInfo getNameInfo(IEntity entity) {
        if (entity == null) return null;
        User currentUser = getCurrentUser();
        if (currentUser == null)
            return null; // The context was probably, null just return (not sure why it should ever be null)

        ICacheManager<DisplayNameCacheKey, NameInfo> cache = (ICacheManager<DisplayNameCacheKey, NameInfo>) getSpringBean("displayNameCache");
        DisplayNameCacheKey key = new DisplayNameCacheKey(currentUser.getGuid(), entity.getGuid());
        NameInfo nameInfo = cache.get(key);
        if (nameInfo != null) {
            return nameInfo;
        }

        if (entity instanceof EntityReference && entity.getOwnerType() == EntityType.USER) {
            IUserManager userManager = (IUserManager) getSpringBean("userManager");
            entity = userManager.loadUserByGuidForDisplayName(entity.getGuid());
        }

        if (entity instanceof User) {
            User user = (User) entity;
            nameInfo = new NameInfo();
            nameInfo.setUsername(user.getUsername());

            // If the currently logged in user is the current user, then
            // use the current user's name
            if (currentUser.getGuid().equals(user.getGuid())) {

                nameInfo.setDisplayName(userFullName(user));
            }

            // Show the user's name if they are in the network,
            // otherwise just show their username
            if (nameInfo.getDisplayName() == null && isFriend(user)) {
                nameInfo.setDisplayName(userFullName(user));
            }

            if (nameInfo.getDisplayName() == null) {
                nameInfo.setDisplayName(user.getUsername());
            }
        }

        if (nameInfo == null && entity instanceof BaseEntity) {
            String s = ((BaseEntity) entity).getName();
            nameInfo = new NameInfo(s, null);
        }

        cache.put(key, nameInfo);
        return nameInfo;
    }


}
