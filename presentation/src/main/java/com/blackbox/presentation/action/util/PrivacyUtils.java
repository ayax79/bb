package com.blackbox.presentation.action.util;

import com.blackbox.foundation.security.Privacy;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.user.IUserManager;
import static com.blackbox.presentation.action.util.JspFunctions.getSpringBean;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.Vouch;
import com.blackbox.foundation.social.RelationshipNetwork;

import java.util.List;

/**
 *
 *
 */
public class PrivacyUtils {
    private static IUserManager userManager = (IUserManager) getSpringBean("userManager");
    private static ISocialManager socialManager = (ISocialManager) getSpringBean("socialManager");

    public static boolean isAllowedToFollow(User user, String privacyUserGuid) {
        boolean allowed = true;
//        Privacy privacy = userManager.loadPrivacy(privacyUserGuid);
//        if (privacy != null && !privacy.isLimitedCanFollow()) {
//            if (user.getType() == User.UserType.LIMITED || !isVouched(user)) {
//                return false;
//            }
//
////            if (!isFriends(user, privacyUserGuid) && !privacy.isNonFriendsCanFollow()) {
////                return false;
////            }
//        }
        return allowed;
    }

    public static boolean isAllowedToPrivateMessage(User user, String privacyUserGuid) {
        boolean allowed = true;
//        Privacy privacy = userManager.loadPrivacy(privacyUserGuid);
//        if (privacy != null && !privacy.isLimitedCanFollow()) {
//            if (user.getType() == User.UserType.LIMITED || !isVouched(user)) {
//                return false;
//            }
//
////            if (!isFriends(user, privacyUserGuid) && !privacy.isNonFriendsCanFollow()) {
////                return false;
////            }
//        }
        return allowed;
    }

    public static boolean isAllowedToSearch(User user, String privacyUserGuid) {
        boolean allowed = true;
//        Privacy privacy = userManager.loadPrivacy(privacyUserGuid);
//        if (privacy != null && !privacy.isLimitedCanFollow()) {
//            if (user.getType() == User.UserType.LIMITED || !isVouched(user)) {
//                return false;
//            }
//
////            if (!isFriends(user, privacyUserGuid) && !privacy.isNonFriendsCanFollow()) {
////                return false;
////            }
//        }
        return allowed;
    }

    public static boolean isAllowedToViewPersona(User user, String privacyUserGuid) {
        boolean allowed = true;
//        Privacy privacy = userManager.loadPrivacy(privacyUserGuid);
//        if (privacy != null) {
//            if (user.getType() == User.UserType.LIMITED && !isVouched(user)) {
//                return false;
//            }
//
////            if (!isFriends(user, privacyUserGuid) && !privacy.isNonFriendsCanFollow()) {
////                return false;
////            }
//        }
        return allowed;
    }

    public static boolean isAllowedToGift(User user, String privacyUserGuid) {
        boolean allowed = true;
//        Privacy privacy = userManager.loadPrivacy(privacyUserGuid);
//        if (privacy != null) {
//            if (user.getType() == User.UserType.LIMITED && !isVouched(user)) {
//                return false;
//            }
////            if (!isFriends(user, privacyUserGuid) && !privacy.isNonFriendsCanFollow()) {
////                return false;
////            }
//        }
        return allowed;
    }

    public static boolean isAllowedToSeeStalk(User user, String privacyUserGuid) {
        boolean allowed = true;
//        Privacy privacy = userManager.loadPrivacy(privacyUserGuid);
//        if (privacy != null) {
//            if (user.getType() == User.UserType.LIMITED && !isVouched(user)) {
//                return false;
//            }
//
////            if (!isFriends(user, privacyUserGuid) && !privacy.isNonFriendsCanFollow()) {
////                return false;
////            }
//        }
        return allowed;
    }

    private static boolean isFriends(User user, String privacyUserGuid) {
        RelationshipNetwork network = socialManager.loadRelationshipNetwork(privacyUserGuid);
        return network != null && network.isFriend(user.getGuid());
    }

    private static boolean isVouched(User user) {
        List<Vouch> vouches = socialManager.loadVouchByTarget(user.getGuid());
        return vouches != null && vouches.size() > 0;
    }
}
