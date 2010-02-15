package com.blackbox.presentation.action.psevent;

import com.blackbox.EntityType;
import com.blackbox.Status;
import com.blackbox.Utils;
import com.blackbox.media.IMediaManager;
import com.blackbox.media.MediaLibrary;
import com.blackbox.media.MediaMetaData;
import com.blackbox.occasion.IOccasionManager;
import com.blackbox.occasion.Occasion;
import com.blackbox.occasion.OccasionLayout;
import com.blackbox.user.User.UserType;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.joda.time.DateTime;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@UrlBinding("/event/create")
public class PSCreateEventActionBean extends PSBaseEventActionBean {

    private List<MediaMetaData> personalImages;
    private List<MediaLibrary> personalVideoList;
    private FileBean fileData;
    private String mediaType;
    @SpringBean("mediaManager")
    private IMediaManager mediaManager;
    @SpringBean("occasionManager")
    private IOccasionManager occasionManager;

    public FileBean getFileData() {
        return fileData;
    }

    public void setFileData(FileBean fileData) {
        this.fileData = fileData;
    }

    public List<MediaMetaData> getPersonalImages() {
        return personalImages;
    }

    public void setPersonalImages(List<MediaMetaData> personalImages) {
        this.personalImages = personalImages;
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


    @DontValidate
    public Resolution create() throws Exception {
        // get total personal media lib ,then select the personal event image lib form them
        //List<MediaLibrary> totalMediaLibList = mediaManager.loadMediaLibrariesForOwner(getCurrentUser().toEntityReference());

        HttpSession session = getContext().getRequest().getSession();

        removedEditOccasionMode();
        occasion = Occasion.createOccasion();
        occasion.setGuid(Utils.generateGuid());
        //occasion.setOwnerIdentifier(getCurrentUser().getOwnerIdentifier());
        occasion.setOwner(getCurrentUser());
        occasion.setOwnerType(EntityType.OCCASION);
        occasion.setStatus(Status.ENABLED);
        occasion.setEventTime(new DateTime());

        //occasion = occasionManager.createOccasion(occasion);
        OccasionLayout layout = occasion.getLayout();
        //layout.setImage(MediaUtil.buildMetaDataMock(mediaManager, "/library/images/defaultplaceholder.jpg", occasion.toEntityReference(), ArtifactType.IMAGE, false));
        layout.setDataFormat(OccasionLayout.DateFormat.valueOf(PSEventDefaultSetting.DATA_FORMAT));
        layout.setLayoutType(OccasionLayout.LayoutType.valueOf(PSEventDefaultSetting.LAYOUT_TYPE));
        layout.setTextAlign(PSEventDefaultSetting.TEXT_ALIGN);
        layout.setTextColor(PSEventDefaultSetting.TEXT_COLOR);
        layout.setShowHeading(PSEventDefaultSetting.HEADING_SHOW);
        session.setAttribute("defaultImageLocation", PSEventDefaultSetting.IMAGES_LOCATION);
        session.setAttribute("defaultVideoLocation", PSEventDefaultSetting.VIDEO_LOCATION);

        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/detail.jsp");
    }

    @Override
    public boolean isHasIntro() {
        return true;
    }

}
