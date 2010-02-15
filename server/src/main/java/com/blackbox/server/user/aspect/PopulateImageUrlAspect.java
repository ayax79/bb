package com.blackbox.server.user.aspect;

import com.blackbox.EntityType;
import com.blackbox.media.AvatarImage;
import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaMetaData;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.user.Profile;
import com.blackbox.user.User;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.apache.shiro.authc.AuthenticationInfo;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @author A.J. Wright
 */
@Aspect
public class PopulateImageUrlAspect {

    @Resource
    IMediaManager mediaManager;

    @Pointcut("execution(public org.apache.shiro.authc.AuthenticationInfo com.blackbox.server.security.AuthenticationManager.load(org.apache.shiro.authc.AuthenticationToken)) || " +
            "execution(public java.util.List com.blackbox.server.user.UserManager.*(..)) || " +
            "execution(public com.blackbox.user.User com.blackbox.server.user.UserManager.*(..)) || " +
            "execution(public com.blackbox.user.Profile com.blackbox.server.user.UserManager.*(..)) || " +
            "execution(public * com.blackbox.server.entity.EntityManager.loadEntity(..))")

    private void populateImageUrlPointcut() {
    }

    @AfterReturning(
            pointcut = "populateImageUrlPointcut()",
            returning = "returnValue"
    )
    public void populateImageUrl(Object returnValue) {
        if (returnValue == null) return;

        if (returnValue instanceof Profile) {
            Profile profile = (Profile) returnValue;
            addImageToProfile(profile.getUser(), profile);
        } else if (returnValue instanceof User) {
            User u = (User) returnValue;
            addImageToProfile(u, u.getProfile());
        } else if (returnValue instanceof AuthenticationInfo) {
            AuthenticationInfo auth = (AuthenticationInfo) returnValue;

            if (auth.getPrincipals() != null && !auth.getPrincipals().isEmpty()) {
                User user = (User) auth.getPrincipals().getPrimaryPrincipal();
                if (user != null) {
                    addImageToProfile(user, user.getProfile());
                }
            }
        } else if (returnValue instanceof Collection) {
            Collection<?> col = (Collection<?>) returnValue;
            for (Object o : col) {
                if (o != null && o instanceof Profile) {
                    Profile p = (Profile) o;
                    addImageToProfile(p.getUser(), p);
                } else if (o != null && o instanceof User) {
                    User u = (User) o;
                    addImageToProfile(u, u.getProfile());
                } 
            }
        }

    }

    protected void addImageToProfile(User u, Profile p) {
        if (p == null) {
        }
        else {

            MediaMetaData metaData = mediaManager.loadProfileMetaMediaData(u.toEntityReference());
            if (metaData != null) {
                p.setProfileImgUrl(metaData.getLocation());
            }

            AvatarImage image = mediaManager.loadAvatarImageFor(EntityType.USER, u.getGuid(), null);
            if (image != null) {
                p.setAvatarUrl(image.getImageLink());
            }
        }
    }

}
