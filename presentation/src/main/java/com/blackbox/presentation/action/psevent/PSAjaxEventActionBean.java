package com.blackbox.presentation.action.psevent;

import com.blackbox.foundation.Utils;
import com.blackbox.foundation.business.UserToAttendingAttendeeFunction;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.occasion.*;
import com.blackbox.presentation.action.util.CommaStringConverter;
import com.blackbox.presentation.action.util.MediaUtil;
import com.blackbox.presentation.action.util.PeriodTypeConverter;
import com.blackbox.presentation.extension.JodaShortDateTimeConverter;
import com.blackbox.foundation.social.Address;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.AmPmEnum;
import com.blackbox.foundation.util.GeoUtil;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.lib.util.Pair;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithText;
import static com.blackbox.foundation.util.DateUtil.join;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@UrlBinding("/action/ajax/event")
public class PSAjaxEventActionBean extends PSBaseEventActionBean {

    private static final Logger logger = LoggerFactory.getLogger(PSAjaxEventActionBean.class);
    private static final DateTimeFormatter eventDateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm a");

    @SpringBean("mediaManager")
    private IMediaManager mediaManager;
    @SpringBean("occasionManager")
    private IOccasionManager occasionManager;
    @SpringBean("uploadTempDir")
    private File uploadTempImageDir;
    @SpringBean("userManager")
    protected IUserManager userManager;

    private List<String> categories;
    private List<MediaLibrary> personalVideoList;
    private FileBean fileData;
    private String mediaType;
    private String eventName;
    private String catalog;
    private String imageGUID;
    private String imageType;
    private String guid;
    private String event_url;
    private String psvp_selector;
    private String status;

    private List<MediaMetaData> personalImages;

    @Validate(converter = JodaShortDateTimeConverter.class)
    private DateTime date;
    @Validate(converter = PeriodTypeConverter.class)
    private ReadablePeriod startTime;
    private AmPmEnum startAmpm;
    @Validate(converter = PeriodTypeConverter.class)
    private ReadablePeriod endTime;
    private AmPmEnum endAmpm;

    private boolean not_save; //todo - clean this up. its ugly

    private String[] bboxReceiverGuidList;

    @Validate(converter = CommaStringConverter.class)
    private String[] emailReceiverStr;

    @Validate(converter = CommaStringConverter.class)
    private String[] live_import;

    @SuppressWarnings({"unchecked"})
    @HandlesEvent("images")
    @DontValidate
    public Resolution getImagesHTML() {
        List<MediaMetaData> allImages = (List<MediaMetaData>) getContext().getRequest().getSession().getAttribute("allImages");
        if (allImages != null) {
            setPersonalImages(allImages);
        } else {
            setPersonalImages(new ArrayList<MediaMetaData>());
        }
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/event_image.jspf");
    }

    @SuppressWarnings({"unchecked"})
    @DefaultHandler
    @DontValidate
    public Resolution startEventConfig() {
        List<MediaMetaData> allImages = (List<MediaMetaData>) getContext().getRequest().getSession().getAttribute("allImages");
        occasion.setName(this.getEventName());
        MediaMetaData eventImage = null;
        if (imageType != null && imageType.equals("media")) {
            for (MediaMetaData m : allImages) {
                if (m.getGuid().equals(getImageGUID())) {
                    eventImage = m;
                    break;
                }
            }
            if (eventImage != null) {
                occasion.getLayout().setImageGuid(eventImage.getGuid());
                occasion.getLayout().setTransiantImage(eventImage);
            }
            occasion.getLayout().setVideoGuid(null);
            occasion.getLayout().setTransiantVideo(null);
        } else {
//            occasion.getLayout().setImages(MediaUtil.buildMetaData(occasion.getLayout().getImages(), mediaManager, fileData, getCurrentUser(), ArtifactType.IMAGE));
        }
        return new StreamingResolution("text", new StringReader("success"));
    }

    @DontValidate
    public Resolution saveEventLayout() throws Exception {

        FileBean fileBean = getTempImageFromHttpSession();
        if (fileBean != null) {
            MediaMetaData originalData = MediaUtil.buildMetaData(mediaManager, fileBean, occasion.toEntityReference(), ArtifactType.IMAGE, false);
            if (originalData != null) {
                occasion.getLayout().setImageGuid(originalData.getGuid());
                occasion.getLayout().setTransiantImage(originalData);
            }
        }

        return new StreamingResolution("text", new StringReader("success"));
    }

    @DontValidate
    public Resolution preview() throws Exception {
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/preview.jsp");
    }

    @DontValidate
    public Resolution confirmReceiver() throws Exception {
        List<Attendee> receivers = new ArrayList<Attendee>();
        if (bboxReceiverGuidList != null) {
            for (String guid : bboxReceiverGuidList) {
                if (guid.length() > 0) {
                    User u = userManager.loadUserByGuid(guid);
                    Attendee attendee = new Attendee();
                    attendee.setEmail(u.getEmail());
                    attendee.setBbxUserName(u.getName());
                    attendee.setBbxUserGuid(guid);
                    attendee.setAttendeeSource(AttendeeSource.BBOX_NETWORK);
                    attendee.setCreated(new DateTime());
                    if (occasion.getOccasionType() != OccasionType.OPEN) {
                        attendee.setPassword(PSEventDefaultSetting.DEFAULT_PASSWORD);
                    }
                    attendee.setAttendingStatus(AttendingStatus.NOT_RESPONDED);
                    attendee.setAcknowledged(false);
                    receivers.add(attendee);
                }
            }
        }
        if (emailReceiverStr != null) {
            for (String email : emailReceiverStr) {
                Attendee attendee = new Attendee();
                attendee.setEmail(email);
                attendee.setAttendeeSource(AttendeeSource.TYPE_IN_EMAIL);
                attendee.setCreated(new DateTime());
                if (occasion.getOccasionType() != OccasionType.OPEN) {
                    attendee.setPassword(PSEventDefaultSetting.DEFAULT_PASSWORD);
                }
                attendee.setAttendingStatus(AttendingStatus.NOT_RESPONDED);
                attendee.setAcknowledged(false);
                receivers.add(attendee);
            }
        }
        Pair<String, Boolean> editMode = getEditOccasionMode();
        if (editMode == null || editMode.getSecond() == null || !editMode.getSecond()) {
            Attendee eventCreator = new Attendee();
            receivers.add(new UserToAttendingAttendeeFunction(new DateTime(), PSEventDefaultSetting.DEFAULT_PASSWORD).apply(getCurrentUser()));
            occasion.setAttendees(receivers);
        } else {
            occasion.getAttendees().addAll(receivers);
        }

        return new StreamingResolution("text", new StringReader("success"));
    }

    //TODO:add function for album images.
    private Occasion cropImage(Occasion occasion) throws Exception {
        Occasion reO = null;
        Integer image_x = null;
        Integer image_y = null;
        Integer image_w = null;
        Integer image_h = null;
        try {
            HttpSession session = getContext().getRequest().getSession();
            image_x = new Integer((String) session.getAttribute("image_x"));
            image_y = new Integer((String) session.getAttribute("image_y"));
            image_w = new Integer((String) session.getAttribute("image_w"));
            image_h = new Integer((String) session.getAttribute("image_h"));
        }
        catch (Exception e) {
            logger.error("crop error", e);
        }
        FileBean original = getTempImageFromHttpSession();
        if (image_x != null && image_x > 0 && image_y != null && image_y > 0 && image_w != null && image_w > 0 && image_h != null && image_h > 0) {
            final String uploadFullPath = getUploadTempImageDir().getAbsolutePath() + File.separator + Utils.generateGuid();

            if (original != null) {
                File cropedImage = MediaUtil.getCroppedImage(original, uploadFullPath, image_x, image_y, image_w, image_h);
                MediaMetaData cropedMediaMetaData =
                        MediaUtil.buildMetaData(
                                mediaManager,
                                new FileBean(cropedImage, original.getContentType(), original.getFileName()),
                                occasion.toEntityReference(),
                                ArtifactType.IMAGE, false);
                occasion.getLayout().setImageGuid(cropedMediaMetaData.getGuid());
                occasion.setAvatarLocation(cropedMediaMetaData.getLocation());
                reO = occasion;
            }
        } else {
            if (original != null) {
                MediaMetaData originalData = MediaUtil.buildMetaData(mediaManager, original, occasion.toEntityReference(), ArtifactType.IMAGE, false);
                occasion.getLayout().setImageGuid(originalData.getGuid());
                occasion.setAvatarLocation(originalData.getLocation());
                reO = occasion;
            }

        }
        return reO;
    }

    @DontValidate
    public Resolution finalSave() throws Exception {
        occasionManager.updateOccasion(occasion);  // this occasion has to be loaded from db then apply the update, otherwise it won't work
        occasionManager.publish(occasion);
        cleanHttpSession();
        return createResolutionWithText(getContext(), "success");
    }

    @DontValidate
    public Resolution saveReceiver() throws Exception {
        List<Attendee> tempEmailReceiver = new ArrayList<Attendee>();
        List<User> tempbbUser = new ArrayList<User>();
        List<Attendee> tempLiveImport = new ArrayList<Attendee>();
        if (live_import != null) {
            for (String email : live_import) {
                Attendee attendee = new Attendee();
                attendee.setEmail(email);
                attendee.setAttendeeSource(AttendeeSource.MSN_LIVE);
                attendee.setCreated(new DateTime());
                attendee.setPassword(PSEventDefaultSetting.DEFAULT_PASSWORD);
                attendee.setAttendingStatus(AttendingStatus.TENATIVE);
                tempLiveImport.add(attendee);
            }
        }

        if (bboxReceiverGuidList != null) {
            for (String guid : bboxReceiverGuidList) {
                if (guid.length() > 0) {
                    User u = userManager.loadUserByGuid(guid);
                    tempbbUser.add(u);
                }
            }
        }
        if (emailReceiverStr != null) {
            for (String email : emailReceiverStr) {
                Attendee attendee = new Attendee();
                attendee.setEmail(email);
                attendee.setAttendeeSource(AttendeeSource.TYPE_IN_EMAIL);
                attendee.setCreated(new DateTime());
                attendee.setPassword(PSEventDefaultSetting.DEFAULT_PASSWORD);
                attendee.setAttendingStatus(AttendingStatus.TENATIVE);
                tempEmailReceiver.add(attendee);
            }

        }
        HttpSession session = getContext().getRequest().getSession();
        session.setAttribute("tempbbUser", tempbbUser);
        session.setAttribute("tempEmailReceiver", tempEmailReceiver);
        session.setAttribute("tempLiveImport", tempLiveImport);
        return new StreamingResolution("text", new StringReader("success"));
    }

    @DontValidate
    public Resolution saveDetails() throws Exception {
        DateTime startDate = join(date, startTime, startAmpm);
        occasion.setEventTime(startDate);
        if (endTime != null) {
            DateTime endDateTime = join(date, endTime, endAmpm);
            occasion.setEventEndTime(endDateTime);
        }

        //process long and lat
        Address address = occasion.getAddress();
        if (address != null) {
            address.setGeoLocation(new GeoUtil().fetchGeoInfoForAddress(address));
        }
        String ownerEdit = (String) getContext().getRequest().getSession().getAttribute("OwnerEdit");
        if (ownerEdit != null && "Y".equals(ownerEdit) && (!not_save)) {
            Occasion afterCropO = cropImage(occasion);
            if (afterCropO != null) {
                occasion = afterCropO;
            }
            occasionManager.updateOccasion(occasion);
        }

        // shortcut to next step...
        return new StreamingResolution("text/plain", "success");
    }

    DateTime parseEventDate(String eventDate, String eventTime) throws ParseException {
        String dateTimeStr = eventDate.trim() + " " + eventTime.trim();
        return eventDateFormatter.parseDateTime(dateTimeStr);
    }

    @DontValidate
    public Resolution getEventLayout() throws Exception {
        OccasionLayout layout = occasion.getLayout();
        JSONObject json = new JSONObject();
        json.put("font", layout.getFont());
        json.put("bkColor", layout.getBackgroundColor());
        json.put("layoutType", layout.getLayoutType());
        json.put("textAgiln", layout.getTextAlign());
        json.put("dataFormate", layout.getDataFormat());
        return new StreamingResolution("text", new StringReader(json.toString()));
    }

    @DontValidate
    public Resolution addMediaToEvent() throws Exception {
        saveTempImageToHttpSession(fileData);
        return new StreamingResolution("text/plain", "success");
    }

    @DontValidate
    public Resolution updateMediaByGuid() throws Exception {
        if (isNotBlank(guid)) {
            OccasionLayout layout = occasion.getLayout();
            layout.setImageGuid(guid);
        }
        //video
        return new StreamingResolution("text/plain", "success");
    }

    @DontValidate
    public Resolution updateRsvpByEmail() {
        String return_str = "failure";
        String email = (String) getContext().getRequest().getSession().getAttribute(PSShowEventActionBean.LOGIN_EMAIL);
        for (Attendee attendee : occasion.getAttendees()) {
            if (StringUtils.equals(email, attendee.getEmail())) {
                attendee.setAttendingStatus(AttendingStatus.valueOf(status));
                occasion = occasionManager.loadByGuid(occasion.getGuid());
                return_str = "success";
            }
        }
        return new StreamingResolution("text/plain", return_str + "," + status);
    }

    private User castAttendeeToUser(Attendee a) {
        User user;
        if (a.getBbxUserName() == null) {
            user = User.createUser();
            user.setEmail(a.getEmail());
            user.setUsername(null);
        } else {
            user = userManager.loadUserByUsername(a.getBbxUserName());
            if (user == null) {
                user = User.createUser();
                user.setEmail(a.getEmail());
                user.setUsername(null);
            }
        }
        return user;
    }

    @DontValidate
    public Resolution updateConnections() {
        List<Attendee> attendees = occasion.getAttendees();
        List<User> attending = new ArrayList<User>();
        List<User> cant = new ArrayList<User>();
        List<User> viewed = new ArrayList<User>();
        List<User> maybe = new ArrayList<User>();
        User currentUser = getContext().getUser();
        boolean owner = false;
        if (currentUser != null && currentUser.getGuid().equals(occasion.getOwner().getGuid())) {
            owner = true;
        }
        for (Attendee a : attendees) {
            AttendingStatus status = a.getAttendingStatus();
            if (status.equals(AttendingStatus.ATTENDING)) {
                if (owner ||
                        occasion.getOccasionWebDetail().getCanViewGuestList() == OccasionWebDetail.CanViewGuestList.VIEW_ALL ||
                        occasion.getOccasionWebDetail().getCanViewGuestList() == OccasionWebDetail.CanViewGuestList.VIEW_YESONLY) {
                    attending.add(castAttendeeToUser(a));
                }
            } else if (status.equals(AttendingStatus.NOT_ATTENDING)) {
                if (owner || occasion.getOccasionWebDetail().getCanViewGuestList() == OccasionWebDetail.CanViewGuestList.VIEW_ALL) {
                    cant.add(castAttendeeToUser(a));
                }
            } else if (status.equals(AttendingStatus.TENATIVE)) {
                if (owner || occasion.getOccasionWebDetail().getCanViewGuestList() == OccasionWebDetail.CanViewGuestList.VIEW_ALL) {
                    maybe.add(castAttendeeToUser(a));
                }
            }
        }
        getContext().getRequest().setAttribute("attending", attending);
        if (OccasionWebDetail.CanViewGuestList.VIEW_ALL.equals(occasion.getOccasionWebDetail().getCanViewGuestList())) {
            getContext().getRequest().setAttribute("cant", cant);
            getContext().getRequest().setAttribute("viewed", viewed);
            getContext().getRequest().setAttribute("maybe", maybe);
        }
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/invitation_connection.jspf");
    }

    public Resolution showRpxcontects() {
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/rpx_import.jspf");
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public ReadablePeriod getStartTime() {
        return startTime;
    }

    public void setStartTime(ReadablePeriod startTime) {
        this.startTime = startTime;
    }

    public void setStartAmpm(AmPmEnum startAmpm) {
        this.startAmpm = startAmpm;
    }

    public void setEndTime(ReadablePeriod endTime) {
        this.endTime = endTime;
    }

    public void setEndAmpm(AmPmEnum endAmpm) {
        this.endAmpm = endAmpm;
    }

    public File getUploadTempImageDir() {
        return uploadTempImageDir;
    }

    public void setUploadTempImageDir(File uploadTempImageDir) {
        this.uploadTempImageDir = uploadTempImageDir;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageGUID() {
        return imageGUID;
    }

    public void setImageGUID(String imageGUID) {
        this.imageGUID = imageGUID;
    }

    public List<MediaMetaData> getPersonalImages() {
        return personalImages;
    }

    public void setPersonalImages(List<MediaMetaData> personalImages) {
        this.personalImages = personalImages;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public FileBean getFileData() {
        return fileData;
    }

    public void setFileData(FileBean fileData) {
        this.fileData = fileData;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<MediaLibrary> getPersonalVideoList() {
        return personalVideoList;
    }

    public void setPersonalVideoList(List<MediaLibrary> personalVideoList) {
        this.personalVideoList = personalVideoList;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public AmPmEnum getEndAmpm() {
        return endAmpm;
    }

    public ReadablePeriod getEndTime() {
        return endTime;
    }

    public AmPmEnum getStartAmpm() {
        return startAmpm;
    }

    public String[] getBboxReceiverGuidList() {
        return bboxReceiverGuidList;
    }

    public void setBboxReceiverGuidList(String[] bboxReceiverGuidList) {
        this.bboxReceiverGuidList = bboxReceiverGuidList;
    }

    public String[] getEmailReceiverStr() {
        return emailReceiverStr;
    }

    public void setEmailReceiverStr(String[] emailReceiverStr) {
        this.emailReceiverStr = emailReceiverStr;
    }

    public String[] getLive_import() {
        return live_import;
    }

    public void setLive_import(String[] live_import) {
        this.live_import = live_import;
    }

    public boolean isNot_save() {
        return not_save;
    }

    public void setNot_save(boolean not_save) {
        this.not_save = not_save;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getEvent_url() {
        return event_url;
    }

    public void setEvent_url(String event_url) {
        this.event_url = event_url;
    }

    public String getPsvp_selector() {
        return psvp_selector;
    }

    public void setPsvp_selector(String psvp_selector) {
        this.psvp_selector = psvp_selector;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @After
    public void replaceSessionOccasion() {
        // don't actually put this in the session on this action
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}
