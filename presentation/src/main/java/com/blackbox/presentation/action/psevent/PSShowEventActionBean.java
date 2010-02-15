package com.blackbox.presentation.action.psevent;

import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaMetaData;
import com.blackbox.occasion.*;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;
import com.blackbox.user.ViewedBy.ViewedByType;
import com.blackbox.activity.IActivityManager;
import com.blackbox.activity.IActivity;
import static com.google.common.collect.Lists.newArrayList;

import java.io.File;
import java.lang.Exception;
import java.util.List;
import java.util.Collection;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.lib.util.Pair;

@UrlBinding("/event/show/{eventParam}")
public class PSShowEventActionBean extends PSBaseEventActionBean {
    final public static String LOGIN_EMAIL = "PSShowEventActionBean.LOGIN_EMAIL";
    final private static Logger logger = LoggerFactory.getLogger(PSShowEventActionBean.class);
    private String eventParam;
    @SpringBean("mediaManager")
    IMediaManager mediaManager;
    @SpringBean("activityManager")
    IActivityManager activityManager;
    @SpringBean("occasionManager")
    IOccasionManager occasionManager;
    @SpringBean("uploadTempDir")
    File uploadTempImageDir;
    @SpringBean("userManager")
    protected IUserManager userManager;
    private List<Attendee> attending = newArrayList();
    private List<Attendee> maybeAttending = newArrayList();
    private List<Attendee> canot = newArrayList();
    private List<User> attendingUser = newArrayList();
    private List<User> maybeAttendingUser = newArrayList();
    private List<User> canotUser = newArrayList();
    private String email;
    private String password;
    private boolean attendee = false;
    private Collection<IActivity> comments = newArrayList();

    public List<User> getAttendingUser() {
        return attendingUser;
    }

    public void setAttendingUser(List<User> attendingUser) {
        this.attendingUser = attendingUser;
    }

    public List<User> getMaybeAttendingUser() {
        return maybeAttendingUser;
    }

    public void setMaybeAttendingUser(List<User> maybeAttendingUser) {
        this.maybeAttendingUser = maybeAttendingUser;
    }

    public List<User> getCanotUser() {
        return canotUser;
    }

    public void setCanotUser(List<User> canotUser) {
        this.canotUser = canotUser;
    }

    public boolean isAttendee() {
        return attendee;
    }

    public void setAttendee(boolean attendee) {
        this.attendee = attendee;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Attendee> getAttending() {
        return attending;
    }

    public void setAttending(List<Attendee> attending) {
        this.attending = attending;
    }

    public List<Attendee> getCanot() {
        return canot;
    }

    public void setCanot(List<Attendee> canot) {
        this.canot = canot;
    }

    public List<Attendee> getMaybeAttending() {
        return maybeAttending;
    }

    public void setMaybeAttending(List<Attendee> maybeAttending) {
        this.maybeAttending = maybeAttending;
    }

    public String getEventParam() {
        return eventParam;
    }

    public void setEventParam(String eventParam) {
        this.eventParam = eventParam;
    }

    @DontValidate
    @DefaultHandler
    public Resolution begin() throws Exception {
        try {
            boolean privateCreate = loadOccasion();
            // It is still possible that there is a version pulled from the session
            if (occasion == null) {
                logger.info("can't find occasion");
                return new ForwardResolution("/WEB-INF/jsp/include/ps_event/event_not_found.jsp");
            }
            if (getContext() != null && getCurrentUser() != null) {
                if (!getCurrentUser().getGuid().equals(occasion.getOwner().getGuid())) {
                    userManager.saveViewedBy(getCurrentUser().getGuid(), occasion.getGuid(), ViewedByType.EVENT);
                }
            }
            return processRequest(privateCreate);
        } catch (Exception e) {
            cleanHttpSession();
            logger.error("exception:", e);
            return new ForwardResolution("/WEB-INF/jsp/include/ps_event/event_not_found.jsp");
        }
    }

    private boolean loadOccasion() {
        boolean privateCreate = false;
        Pair<String, Boolean> editMode = getEditOccasionMode();
        if (editMode != null && editMode.getSecond() != null
                && editMode.getSecond() &&
                editMode.getFirst().equals(eventParam)) {
            pullOccasionFromSession();
            setUnsaved(true);
        } else {
            if (eventParam != null) {
                Occasion o = occasionManager.loadByGuid(eventParam);
                if (o != null) {
                    occasion = o;
                    privateCreate = false;
                } else if (occasion != null && OccasionType.PRIVATE.equals(occasion.getOccasionType())) {
                    privateCreate = true;
                }
            }
        }
        return privateCreate;
    }

    private Resolution processRequest(boolean privateCreate) throws Exception {
        //is this a public event and a nonmember or someone not logged in trying toview
        if (getContext() == null || getCurrentUser() == null) {
            if (OccasionType.OPEN.equals(occasion.getOccasionType())) {
                return transformOccasionForViewing();
            } else {
                //send to login
                return logout();
            }
        } else {
            boolean canShow = false;
            if (occasion.getOccasionType().equals(OccasionType.PRIVATE)) {
                for (Attendee attendee : occasion.getAttendees()) {
                    if (getCurrentUser().getGuid().equals(attendee.getBbxUserGuid())) {
                        canShow = true;
                        break;
                    }
                }
            } else {
                canShow = true;
            }
            if (privateCreate) {
                canShow = true;
            }
            if (canShow) {
                saveMediaLocationToHttpSession();
                return transformOccasionForViewing();
            } else {
                //should be transformOccasionForViewing no access permission
                return new ForwardResolution("/WEB-INF/jsp/include/ps_event/no_access_permission.jsp");
            }
        }
    }

    private void saveMediaLocationToHttpSession() {
        MediaMetaData image = null;
        MediaMetaData video = null;

        if (occasion.getLayout().getImageGuid() != null) {
            image = mediaManager.loadMediaMetaDataByGuid(occasion.getLayout().getImageGuid());
        }
        if (occasion.getLayout().getVideoGuid() != null) {
            video = mediaManager.loadMediaMetaDataByGuid(occasion.getLayout().getVideoGuid());
        }
        if (image != null) {
            getContext().getRequest().getSession().setAttribute("event_image_location", image.getLocation());
        }
        if (video != null) {
            getContext().getRequest().getSession().setAttribute("event_video_location", video.getLocation());
        }
    }

    @DontValidate
    public Resolution edit() throws Exception {
        cleanHttpSession();
        occasion = occasionManager.loadByWebUrl(eventParam);
        saveMediaLocationToHttpSession();
        if (occasion.getOwner().getGuid().equals(getCurrentUser().getGuid())) {
            getContext().getRequest().getSession().setAttribute("OwnerEdit", "Y");
        } else {
            getContext().getRequest().getSession().setAttribute("OwnerEdit", "N");
        }
        setEditOccasionMode(eventParam);
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/config.jsp");
    }

    private Resolution transformOccasionForViewing() throws Exception {
        try {
            // It is still possible that there is a version pulled from the session
            if (occasion == null) {
                logger.info("can't find occasion");
                return new ForwardResolution("/WEB-INF/jsp/include/ps_event/event_not_found.jsp");
            }
            saveMediaLocationToHttpSession();
            List<Attendee> attendeeList = occasion.getAttendees();
            for (Attendee at : attendeeList) {
                AttendingStatus attendingStatus = at.getAttendingStatus();
                if (attendingStatus.equals(AttendingStatus.ATTENDING)) {
                    attending.add(at);
                    attendingUser.add(userManager.loadUserByGuid(at.getBbxUserGuid()));
                } else if (attendingStatus.equals(AttendingStatus.TENATIVE)) {
                    maybeAttending.add(at);
                    maybeAttendingUser.add(userManager.loadUserByGuid(at.getBbxUserGuid()));
                } else if (attendingStatus.equals(AttendingStatus.NOT_ATTENDING)) {
                    canot.add(at);
                    canotUser.add(userManager.loadUserByGuid(at.getBbxUserGuid()));
                }
            }
            setDefaultImageLocation(PSEventDefaultSetting.IMAGES_LOCATION);
            setDefaultVideoLocation(PSEventDefaultSetting.VIDEO_LOCATION);
            comments = activityManager.loadChildrenActivityByParent(occasion.getGuid());
            return new ForwardResolution("/WEB-INF/jsp/include/ps_event/invitation.jsp");
        } catch (Exception e) {
            logger.error("exception", e);
            return new ForwardResolution("/WEB-INF/jsp/include/ps_event/event_not_found.jsp");
        } finally {
            cleanHttpSession();
        }
    }

    public Collection<IActivity> getComments() {
        return comments;
    }

    public void setComments(Collection<IActivity> comments) {
        this.comments = comments;
    }

    @Override
    public boolean isHasIntro() {
        return true;
    }

}
