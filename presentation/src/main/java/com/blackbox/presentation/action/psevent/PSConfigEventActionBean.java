/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blackbox.presentation.action.psevent;

import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.foundation.media.MediaLibrary.MediaLibraryType;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.presentation.action.util.MediaUtil;
import com.blackbox.presentation.action.util.PresentationUtil;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.Relationship;
import com.blackbox.foundation.social.RelationshipNetwork;
import com.blackbox.foundation.user.ExternalCredentials;
import static com.blackbox.foundation.user.ExternalCredentials.CredentialType.TWITTER;
import com.blackbox.foundation.user.IUser;
import com.blackbox.foundation.user.IUserManager;
import com.google.code.facebookapi.FacebookJsonRestClient;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.ArrayList;
import java.util.List;

@UrlBinding("/event/config")
public class PSConfigEventActionBean extends PSBaseEventActionBean {

    @SpringBean("mediaManager")
    private IMediaManager mediaManager;
    @SpringBean("socialManager")
    protected ISocialManager socialManager;
    @SpringBean("userManager")
    IUserManager userManager;

    private RelationshipNetwork network;
    private List[] uvMapList;

    private MediaMetaData occasionVideo;
    private MediaMetaData occasionImage;
    private String guid;
    private boolean twitterRemember;
    private boolean facebookRemember;

    private String twitterUsername;
    private String twitterPassword;
    private String twitterDescription;
    private String facebookDescription;
    private int facebookCategory;
    private int facebookSubCategory;
    private boolean publishToTwitter;
    private boolean publishToFacebook;

    public int getFacebookCategory() {
        return facebookCategory;
    }

    public void setFacebookCategory(int facebookCategory) {
        this.facebookCategory = facebookCategory;
    }

    public int getFacebookSubCategory() {
        return facebookSubCategory;
    }

    public void setFacebookSubCategory(int facebookSubCategory) {
        this.facebookSubCategory = facebookSubCategory;
    }

    public boolean isFacebookRemember() {
        return facebookRemember;
    }

    public void setFacebookRemember(boolean facebookRemember) {
        this.facebookRemember = facebookRemember;
    }

    public String getFacebookDescription() {
        return facebookDescription;
    }

    public void setFacebookDescription(String facebookDescription) {
        this.facebookDescription = facebookDescription;
    }

    public boolean isPublishToFacebook() {
        return publishToFacebook;
    }

    public void setPublishToFacebook(boolean publishToFacebook) {
        this.publishToFacebook = publishToFacebook;
    }

    public boolean isPublishToTwitter() {
        return publishToTwitter;
    }

    public void setPublishToTwitter(boolean publishToTwitter) {
        this.publishToTwitter = publishToTwitter;
    }

    public String getTwitterDescription() {
        return twitterDescription;
    }

    public void setTwitterDescription(String twitterDescription) {
        this.twitterDescription = twitterDescription;
    }

    public boolean isTwitterRemember() {
        return twitterRemember;
    }

    public void setTwitterRemember(boolean twitterRemember) {
        this.twitterRemember = twitterRemember;
    }

    public String getTwitterUsername() {
        return twitterUsername;
    }

    public void setTwitterUsername(String twitterUsername) {
        this.twitterUsername = twitterUsername;
    }

    public String getTwitterPassword() {
        return twitterPassword;
    }

    public void setTwitterPassword(String twitterPassword) {
        this.twitterPassword = twitterPassword;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public List[] getUvMapList() {
        return uvMapList;
    }

    public void setUvMapList(List[] uvMapList) {
        this.uvMapList = uvMapList;
    }

    public RelationshipNetwork getNetwork() {
        return network;
    }

    public void setNetwork(RelationshipNetwork network) {
        this.network = network;
    }

    public Resolution edit() throws Exception {
        setEditOccasionMode(guid);
        // load the event first...        
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/detail.jsp");
    }

    @SuppressWarnings({"unchecked"})
    @DontValidate
    @DefaultHandler
    public Resolution config() throws Exception {
        List<MediaMetaData> allImages = (List<MediaMetaData>) getContext().getRequest().getSession().getAttribute("allImages");
        setDefaultImageLocation(PSEventDefaultSetting.IMAGES_LOCATION);
        setDefaultVideoLocation(PSEventDefaultSetting.VIDEO_LOCATION);
        if (allImages == null) {
            allImages = getUserAllImages();
            getContext().getRequest().getSession().setAttribute("allImages", allImages);
        }

        if (occasion != null) {
            if (occasion.getLayout().getVideoGuid() != null) {
                occasionVideo = mediaManager.loadMediaMetaDataByGuid(occasion.getLayout().getVideoGuid());
            }
            if (occasion.getLayout().getImageGuid() != null) {
                occasionImage = mediaManager.loadMediaMetaDataByGuid(occasion.getLayout().getImageGuid());
            }
        }

        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/config.jsp");
    }

    @DontValidate
    public Resolution detail() throws Exception {
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/detail.jsp");
    }

    @DontValidate
    public Resolution guestList() throws Exception {
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/guest_list.jsp");
    }

    @DontValidate
    public Resolution webDetail() throws Exception {
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/web_detail.jsp");
    }

    @DontValidate
    public Resolution promote() throws Exception {
        if (publishToTwitter) {
            occasion.setPublishToTwitter(true);
            occasion.setTwitterDescription(twitterDescription);
            if (twitterPassword != null && twitterUsername != null) {
                ExternalCredentials cred = ExternalCredentials.buildExternalCredentials(TWITTER, getCurrentUser().toEntityReference(),
                        twitterUsername, twitterPassword);
                //override just incase they use a new twitter account....  always use latest
                userManager.saveExternalCredential(cred);
            }
        }
        if (publishToFacebook) {
            occasion.setPublishToFacebook(true);
            occasion.setFacebookCategory(facebookCategory);
            occasion.setFacebookSubCategory(facebookSubCategory);
            occasion.setFacebookDescription(facebookDescription);
        }
        RedirectResolution resolution = new RedirectResolution(PSShowEventActionBean.class);
        resolution.addParameter("eventParam", occasion.getGuid());
        return resolution;
    }

    @SuppressWarnings({"unchecked"})
    @DontValidate
    public Resolution member() throws Exception {
        setNetwork(socialManager.loadRelationshipNetwork(getCurrentUser().getGuid()));
        List<IUser> tempbbUser = (List<IUser>) getContext().getRequest().getSession().getAttribute("tempbbUser");
        int friendLength = network.getFriends().size();
        uvMapList = new List[friendLength];
        for (int i = 0; i < network.getFriends().size(); i++) {
            Relationship friend = (Relationship) (network.getFriends().toArray()[i]);
            List<Object> list = new ArrayList<Object>();
            list.add(friend);
            list.add(socialManager.loadVouchByTarget(friend.getGuid()).size());
            list.add(false);
            if (tempbbUser != null) {
                for (IUser selected : tempbbUser) {
                    if (selected.getGuid().equals(friend.getGuid())) {
                        list.set(2, true);
                        break;
                    }
                }
            }
            uvMapList[i] = list;
        }
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/member.jsp");
    }

    protected List<MediaMetaData> getUserAllImages() {
        List<MediaMetaData> images = new ArrayList<MediaMetaData>();
        List<MediaLibrary> libs = MediaUtil.getMediaLibListByType(
                mediaManager.loadMediaLibrariesForOwner(
                        getCurrentUser().toEntityReference()), MediaLibraryType.IMAGE);
        if (libs != null) {
            for (MediaLibrary lib : libs) {
                if (lib.getMedia() != null && !lib.getMedia().isEmpty()) {
                    images.addAll(lib.getMedia());
                }
            }
        } else {
            return new ArrayList<MediaMetaData>();
        }
        return images;
    }

    public String getShowingUrl() {
        return PresentationUtil.getProperty("presentation.url") + "/event/show/";
    }

    public MediaMetaData getOccasionImage() {
        return occasionImage;
    }

    public void setOccasionImage(MediaMetaData occasionImage) {
        this.occasionImage = occasionImage;
    }

    public MediaMetaData getOccasionVideo() {
        return occasionVideo;
    }

    public void setOccasionVideo(MediaMetaData occasionVideo) {
        this.occasionVideo = occasionVideo;
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}
