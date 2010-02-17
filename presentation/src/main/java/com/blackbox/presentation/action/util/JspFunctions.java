package com.blackbox.presentation.action.util;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.IEntity;
import com.blackbox.foundation.activity.ActivityThread;
import com.blackbox.foundation.activity.ActivityTypeEnum;
import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.occasion.Occasion;
import static com.blackbox.presentation.action.util.PresentationUtil.getProperty;
import static com.blackbox.presentation.action.util.PresentationUtil.getNameInfo;
import com.blackbox.presentation.extension.BlackBoxContext;
import com.blackbox.foundation.social.Relationship;
import com.blackbox.foundation.social.RelationshipNetwork;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.Vouch;
import com.blackbox.foundation.user.User;
import static com.blackbox.foundation.user.User.UserType.*;
import com.blackbox.foundation.util.DateUtil;
import com.blackbox.foundation.util.NameInfo;
import net.sourceforge.stripes.action.ActionBeanContext;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.yestech.lib.i18n.CanadaProvinceEnum;
import org.yestech.lib.i18n.USStateEnum;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import static java.lang.Boolean.getBoolean;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.util.Locale;

/**
 *
 *
 */
public class JspFunctions {

    private static final Logger logger = LoggerFactory.getLogger(JspFunctions.class);

    public static boolean canSeeName(IEntity e) {
        return (isFriend(e) || isInRelationship(e));
    }

    public static boolean isFriend(IEntity e) {
        if (e == null) {
            logger.warn("Null value passed into JspFunctions:isFriend");
            return false;
        }

        ActionBeanContext actionBeanContext = PresentationResourceHolder.getContext();
        if (actionBeanContext instanceof BlackBoxContext) {

            BlackBoxContext context = (BlackBoxContext) actionBeanContext;

            // Show the user's name if they are in the network,
            // otherwise just show their username
            RelationshipNetwork network = context.getNetwork();
            if (network != null) {
                return network.isFriend(e.getGuid()) ||
                        network.isInRelationship(e.getGuid()) || 
                        network.isRelationStatus(e.getGuid(), Relationship.RelationStatus.IN_RELATIONSHIP_PENDING);
            }
        }
        return false;
    }

    public static boolean isBlocked(IEntity e) {
        ActionBeanContext actionBeanContext = PresentationResourceHolder.getContext();
        if (actionBeanContext instanceof BlackBoxContext) {
            BlackBoxContext context = (BlackBoxContext) actionBeanContext;
            return context.getBlocked().contains(e.toEntityReference());
        }
        return false;
    }

    public static boolean isPending(IEntity e) {
        ActionBeanContext actionBeanContext = PresentationResourceHolder.getContext();
        if (actionBeanContext instanceof BlackBoxContext) {

            BlackBoxContext context = (BlackBoxContext) actionBeanContext;

            // Show the user's name if they are in the network,
            // otherwise just show their username
            RelationshipNetwork network = context.getNetwork();
            if (network != null) {
                return network.isPending(e.getGuid());
            }
        }
        return false;
    }

    public static boolean isInRelationship(IEntity e) {
        if (e == null) {
            logger.warn("Null value passed into JspFunctions:isInRelationship");
            return false;
        }
        ActionBeanContext actionBeanContext = PresentationResourceHolder.getContext();
        if (actionBeanContext instanceof BlackBoxContext) {

            BlackBoxContext context = (BlackBoxContext) actionBeanContext;

            // Show the user's name if they are in the network,
            // otherwise just show their username
            RelationshipNetwork network = context.getNetwork();
            if (network != null) {
                return network.isInRelationship(e.getGuid());
            }
        }
        return false;

    }

    public static Integer getWeight(IEntity e) {
        if (e == null) {
            logger.warn("Null value passed into JspFunctions:getWeight");
            return null;
        }
        ActionBeanContext actionBeanContext = PresentationResourceHolder.getContext();
        if (actionBeanContext instanceof BlackBoxContext) {

            BlackBoxContext context = (BlackBoxContext) actionBeanContext;

            // Show the user's name if they are in the network,
            // otherwise just show their username
            RelationshipNetwork network = context.getNetwork();
            if (network != null) {
                return network.getWeightForRelationship(e);
            }
        }
        return null;

    }

    public static boolean isFollowing(IEntity e) {
        if (e == null) {
            logger.warn("Null value passed into JspFunctions:isFollowing");
            return false;
        }
        ActionBeanContext actionBeanContext = PresentationResourceHolder.getContext();
        if (actionBeanContext instanceof BlackBoxContext) {

            BlackBoxContext context = (BlackBoxContext) actionBeanContext;

            // Show the user's name if they are in the network,
            // otherwise just show their username
            RelationshipNetwork network = context.getNetwork();
            if (network != null) {
                return network.isFollowing(e.getGuid()) || network.isPending(e.getGuid());
            }
        }
        return false;
    }

    public static boolean isFollowedBy(IEntity e) {
        return isRelatedBy(e, Relationship.RelationStatus.FOLLOW);
    }

    public static boolean isFriendedBy(IEntity e) {
        return isRelatedBy(e, Relationship.RelationStatus.FRIEND);
    }

    public static boolean isVouched(String guid) {
        ISocialManager socialManager = (ISocialManager) getSpringBean("socialManager");
        List<Vouch> vouches = socialManager.loadVouchByTarget(guid);
        return vouches != null && !vouches.isEmpty();
    }

    @SuppressWarnings({"unchecked"})
    public static String displayName(IEntity entity) {
        NameInfo nameInfo = getNameInfo(entity);
        return nameInfo != null ? nameInfo.getDisplayName() : null;
    }

    @SuppressWarnings({"unchecked"})
    public static String userName(IEntity entity) {
        NameInfo nameInfo = getNameInfo(entity);
        return nameInfo != null ? nameInfo.getUsername() : null;
    }

    public static Object getSpringBean(String beanName) {
        ActionBeanContext actionBeanContext = PresentationResourceHolder.getContext();
        ServletContext servletContext = actionBeanContext.getRequest().getSession().getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        return context.getBean(beanName);
    }

    public static User getCurrentUser() {
        BlackBoxContext actionBeanContext = (BlackBoxContext) PresentationResourceHolder.getContext();
        if (actionBeanContext == null) return null;
        return actionBeanContext.getUser();
    }

    public static String userFullName(User user) {
        if (user == null) return null;
        else if (user.getName() != null && user.getLastname() != null) return user.getName() + " " + user.getLastname();
        else return user.getUsername();
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends Enum<T>> List<Enum<T>> enumValues(String enumClass) {
        try {
            Method m = Class.forName(enumClass).getDeclaredMethod("values");
            return Arrays.asList((Enum<T>[]) m.invoke(enumClass));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    public static boolean isOccasion(IActivity activity) {
        return ActivityTypeEnum.OCCASION == activity.getActivityType();
    }

    public static boolean isMessage(IActivity activity) {
        return ActivityTypeEnum.MESSAGE == activity.getActivityType();
    }

    public static boolean isMedia(IActivity activity) {
        return ActivityTypeEnum.MEDIA == activity.getActivityType();
    }

    public static String activityThreadToJson(ActivityThread activityThread) throws JSONException {
        return JSONUtil.toJSON(activityThread).toString();
    }

    public static String occasionToJson(Occasion occasion) throws JSONException {
        return JSONUtil.toJSON(occasion).toString();
    }

    public static String formatDate(DateTime dateTime, String pattern) {
        if (dateTime == null) return "";
        return dateTime.toString(pattern, contextLocale());
    }

    public static String timeagoFormatDate(DateTime dateTime) {
        return dateTime.withZone(DateTimeZone.UTC).toString(ISODateTimeFormat.dateTimeNoMillis());
    }

    public static String displayShortTime(DateTime dateTime) {
        return dateTime.toString(DateTimeFormat.shortTime().withLocale(contextLocale()));
    }

    public static int yearsDifference(DateTime first, DateTime second) {
        Period period = new Period(first.getMillis(), second.getMillis());
        return period.getYears();
    }

    public static int age(DateTime birthdate) {
        if (birthdate == null) return -1;
        Period period = new Period(birthdate.getMillis(), System.currentTimeMillis());
        return period.getYears();
    }

    public static String libraryResource(String url) {
        if (getBoolean("local") || url.contains(".swf")) {
            HttpServletRequest request = getRequest();
            return request.getContextPath() + url;
        }
        return getProperty("cdn.url") + url;
    }

    public static boolean isCollapsedPost(String cookie, String guidA) throws UnsupportedEncodingException {
        if (cookie.isEmpty()) {
            return false;
        } else {
            cookie = java.net.URLDecoder.decode(cookie, "UTF-8");
            String[] guids = cookie.split(",");
            for (String guidB : guids) {
                if (guidA.equals(guidB)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isLimited(IEntity e) {
        if (e == null || !(e instanceof User)) return false;
        User user = (User) e;
        return (user.getType() == User.UserType.LIMITED);
    }

    public static String relationshipDescription(IEntity e) {
        ActionBeanContext actionBeanContext = PresentationResourceHolder.getContext();
        if (actionBeanContext instanceof BlackBoxContext) {
            BlackBoxContext context = (BlackBoxContext) actionBeanContext;
            User currentUser = context.getUser();
            ISocialManager socialManager = (ISocialManager) getSpringBean("socialManager");
            Relationship relationship = socialManager.loadRelationshipByEntities(currentUser.getGuid(), e.getGuid());
            if (relationship != null) {
                return relationship.getDescription();
            }
        }
        return "[CONNECTED]";
    }

    public static boolean isRelatedBy(IEntity e, Relationship.RelationStatus status) {
        ActionBeanContext actionBeanContext = PresentationResourceHolder.getContext();
        if (actionBeanContext instanceof BlackBoxContext) {
            BlackBoxContext context = (BlackBoxContext) actionBeanContext;

            List<Relationship> relationships = context.getReverseRelationships();
            if (relationships != null) {
                for (Relationship relationship : relationships) {

                    EntityReference fromEntity = relationship.getFromEntity();
                    if (fromEntity != null &&
                            fromEntity.getGuid().equals(e.getGuid()) &&
                            Relationship.RelationStatus.getClosestStatusForWeight(status.getWeight()) == status) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String stateAbv(String state) {
        if ("ALASKA".equals(state)) return "AK";
        if ("ALABAMA".equals(state)) {
            return "AL";
        }
        if ("ARKANSA".equals(state)) {
            return "AR";
        }
        if ("ARIZON".equals(state)) {
            return "AZ";
        }
        if ("CALIFORNI".equals(state)) {
            return "CA";
        }
        if ("COLORAD".equals(state)) {
            return "CO";
        }
        if ("CONNECTICU".equals(state)) {
            return "CT";
        }
        if ("DISTRICT_OF_COLUMBI".equals(state)) {
            return "DC";
        }
        if ("DELAWAR".equals(state)) {
            return "DE";
        }
        if ("FLORID".equals(state)) {
            return "FL";
        }
        if ("GEORGI".equals(state)) {
            return "GA";
        }
        if ("HAWAI".equals(state)) {
            return "HI";
        }
        if ("IOWA".equals(state)) {
            return "IA";
        }
        if ("IDAHO".equals(state)) {
            return "ID";
        }
        if ("ILLINOIS".equals(state)) {
            return "IL";
        }
        if ("INDIANA".equals(state)) {
            return "IN";
        }
        if ("KANSAS".equals(state)) {
            return "KS";
        }
        if ("KENTUCKY".equals(state)) {
            return "KY";
        }
        if ("LOUISIANA".equals(state)) {
            return "LA";
        }
        if ("MASSACHUSETTS".equals(state)) {
            return "MA";
        }
        if ("MARYLAND".equals(state)) {
            return "MD";
        }
        if ("MAINE".equals(state)) {
            return "ME";
        }
        if ("MICHIGAN".equals(state)) {
            return "MI";
        }
        if ("MINNESOTA".equals(state)) {
            return "MN";
        }
        if ("MISSOURI".equals(state)) {
            return "MO";
        }
        if ("MISSISSIPPI".equals(state)) {
            return "MS";
        }
        if ("MONTANA".equals(state)) {
            return "MT";
        }
        if ("NORTH_CAROLINA".equals(state)) {
            return "NC";
        }
        if ("NORTH_DAKOTA".equals(state)) {
            return "ND";
        }
        if ("NEBRASKA".equals(state)) {
            return "NE";
        }
        if ("NEW_HAMPSHIRE".equals(state)) {
            return "NH";
        }
        if ("NEW_JERSEY".equals(state)) {
            return "NJ";
        }
        if ("NEW_MEXICO".equals(state)) {
            return "NM";
        }
        if ("NEVADA".equals(state)) {
            return "NV";
        }
        if ("NEW_YORK".equals(state)) {
            return "NY";
        }
        if ("OHIO".equals(state)) {
            return "OH";
        }
        if ("OKLAHOMA".equals(state)) {
            return "OK";
        }
        if ("OREGON".equals(state)) {
            return "OR";
        }
        if ("PENNSYLVANIA".equals(state)) {
            return "PA";
        }
        if ("RHODE_ISLAND".equals(state)) {
            return "RI";
        }
        if ("SOUTH_CAROLINA".equals(state)) {
            return "SC";
        }
        if ("SOUTH_DAKOTA".equals(state)) {
            return "SD";
        }
        if ("TENNESSEE".equals(state)) {
            return "TN";
        }
        if ("TEXAS".equals(state)) {
            return "TX";
        }
        if ("UTAH".equals(state)) {
            return "UT";
        }
        if ("VIRGINIA".equals(state)) {
            return "VA";
        }
        if ("VERMONT".equals(state)) {
            return "VT";
        }
        if ("WASHINGTON".equals(state)) {
            return "WA";
        }
        if ("WISCONSIN".equals(state)) {
            return "WI";
        }
        if ("WEST_VIRGINIA".equals(state)) {
            return "WV";
        }
        if ("WYOMING".equals(state)) {
            return "WY";
        }
        return "";
    }

    public static String picDemension(int defaultW, int defaultH, double aspect) {
        int width = defaultW;
        int height = defaultH;
        double defaultAsp = width / height;

        if (aspect == 0.0) {
        } else if (aspect > defaultAsp) {
            height = (int) (width / aspect);
        } else if (aspect < defaultAsp) {
            width = (int) (height * aspect);
        }
        return " width='" + width + "' height='" + height + "' ";
    }

    /*public static int picWidth(int height, double aspect, int defaultWidth) {
        if (aspect == 1.0) {
            return defaultWidth;
        }
        return (int) (height * aspect);
    }
     */

    @SuppressWarnings({"unchecked"})
    public static Enum<?> enumValue(String name, String className) {
        Class<Enum> type;
        try {
            type = (Class<Enum>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
        try {
            return Enum.valueOf(type, name);
        } catch (Exception e) {
            return null;
        }
    }

    public static Enum<?> stateEnum(String name) {
        Enum<?> value = null;
        try {
            value = Enum.valueOf(USStateEnum.class, name);

            return value;
        } catch (IllegalArgumentException e) {
            // ignoring, since it just didn't recognize the enum name
        }

        if (value == null) {
            try {
                value = Enum.valueOf(CanadaProvinceEnum.class, name);
            } catch (IllegalArgumentException e) {
                // ignoring, since it just didn't recognize the enum name
            }
        }
        return value;
    }

    public static <T extends IActivity> T latestActivity(ActivityThread<T> thread) {
        return thread.flatten().first();
    }

    public static String formatTimePeriod(ReadablePeriod period) {
        PeriodFormatter formatter = DateUtil.createPeriodTimeFormatter();
        return formatter.print(period);
    }

    public static boolean isAffiliate(User user) {
        User.UserType type = user.getType();
        return type == AFFILIATE || type == BLACKBOX_EMPLOYEE || type == FOUNDER || type == PROMOTER || type == BLACKBOX_ADMIN;
    }

    public static boolean isEmployee(User user) {
        User.UserType type = user.getType();
        return type == BLACKBOX_EMPLOYEE || type == BLACKBOX_ADMIN;
    }

    public static boolean isMarketing(User user) {
        User.UserType type = user.getType();
        return type == BLACKBOX_MARKETING || type == BLACKBOX_ADMIN;
    }

    public static boolean isAdmin(User user) {
        User.UserType type = user.getType();
        return type == BLACKBOX_ADMIN;
    }

    private static Locale contextLocale() {
        Locale locale = null;
        ActionBeanContext context = PresentationResourceHolder.getContext();
        if (context != null) locale = context.getLocale();
        return locale == null ? Locale.US : locale;
    }

    public static String getBundleString(String bundleName, String key) {
        ResourceBundle myResources = ResourceBundle.getBundle(bundleName, contextLocale());
        return myResources.getString(key);
    }

    public static String limitString(String str, int length) {
        if (str.length() > length) {
            return str.substring(0, length - 1) + "...";
        } else {
            return str;
        }
    }

    public static String urlEncode(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            return null; //shouldn't ever happen
        }
    }

    private static final DateTimeFormatter BATCH_FORMAT = DateTimeFormat.forPattern("yyyyMMdd");


    /**
     * Used to generate a batch_id for transaction. this will always be the date in iso format with no delimeters.
     *
     * @return The batch id used for transaction processing
     */
    public static String batchId() {
        return new DateTime().toString(BATCH_FORMAT);
    }

    protected static HttpServletRequest getRequest() {
        return PresentationResourceHolder.getRequest();
    }

    public static boolean hasProperty(Object o, String propertyName) {
        boolean returnValue;

        Class myClass = o.getClass();
        Class[] parameterTypes = new Class[]{};

        try {
            String methodName = "get" + propertyName.toUpperCase().charAt(0) + propertyName.substring(1);
            returnValue = myClass.getMethod(methodName, parameterTypes) != null;
        }

        catch (NoSuchMethodException e) {
            returnValue = false;
        }

        return returnValue;
    }

	public static boolean isAllowedToFollow(User user, String privacyUserGuid) {
		return PrivacyUtils.isAllowedToFollow(user, privacyUserGuid);
	}

	public static boolean isAllowedToSearch(User user, String privacyUserGuid) {
		return PrivacyUtils.isAllowedToSearch(user, privacyUserGuid);
	}

	public static boolean isAllowedToPrivateMessage(User user, String privacyUserGuid) {
		return PrivacyUtils.isAllowedToPrivateMessage(user, privacyUserGuid);
	}

	public static boolean isAllowedToViewPersona(User user, String privacyUserGuid) {
		return PrivacyUtils.isAllowedToViewPersona(user, privacyUserGuid);
	}

	public static boolean isAllowedToGift(User user, String privacyUserGuid) {
		return PrivacyUtils.isAllowedToGift(user, privacyUserGuid);
	}

	public static boolean isAllowedToSeeStalk(User user, String privacyUserGuid) {
		return PrivacyUtils.isAllowedToSeeStalk(user, privacyUserGuid);
	}


}
