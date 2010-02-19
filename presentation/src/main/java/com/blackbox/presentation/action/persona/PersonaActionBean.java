/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.persona;

import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.*;
import com.blackbox.foundation.bookmark.WishStatus;
import com.blackbox.foundation.gifting.IGiftingManager;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.foundation.media.MediaLibrary.MediaLibraryType;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.occasion.IOccasionManager;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.presentation.action.util.AvatarCacheKey;
import com.blackbox.presentation.action.util.JSONUtil;
import com.blackbox.presentation.action.util.JspFunctions;
import com.blackbox.presentation.session.UserSessionService;
import com.blackbox.foundation.search.WordFrequency;
import com.blackbox.foundation.social.*;
import com.blackbox.foundation.user.*;
import com.blackbox.foundation.user.ViewedBy.ViewedByType;
import com.blackbox.foundation.util.Count;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.blackbox.presentation.action.BaseBlackBoxActionBean.ViewType.json;
import static com.blackbox.presentation.action.util.JSONUtil.toJSON;
import static com.blackbox.presentation.action.util.JspFunctions.isBlocked;
import static com.blackbox.presentation.action.util.MediaUtil.*;
import static com.blackbox.presentation.action.util.PresentationUtil.*;
import static com.blackbox.presentation.action.util.PrivacyUtils.isAllowedToFollow;
import static org.apache.commons.lang.StringUtils.isBlank;

@UrlBinding("/persona/{identifier}")
public class PersonaActionBean extends BasePersonaActionBean {

    @SpringBean("mediaManager")
    private IMediaManager mediaManager;
    @SpringBean("threadPool")
    private ExecutorService threadPool;
    @SpringBean("occasionManager")
    private IOccasionManager occasionManager;
    @SpringBean("userManager")
    private IUserManager userManager;
    @SpringBean("userSessionService")
    private UserSessionService userSessionService;
    @SpringBean("activityManager")
    IActivityManager activityManager;
    @SpringBean("giftingManager")
    IGiftingManager giftingManager;
    @SpringBean("personaHelper") protected PersonaHelper personaHelper;


    final private static Logger logger = LoggerFactory.getLogger(PersonaActionBean.class);
    private List<MediaLibrary> mediaLibList;
    private int totalImages = 0;
    private List<MediaLibrary> videoLibList;

    private MediaLibrary showingMediaLib; // I think this can be removed (?)
    private MediaLibrary currentAlbum;

    private String albumGuid;
    private String imageGuid;

    private String ownerProfileImage;
    private Profile viewer;
    private String viewerProfileImage;
    private Collection<Relationship> relationships;
    private List<Occasion> occasionList;
    private String identifier;
    private PaginationResults<IActivity> gifts;
    private Collection<ActivityThread<IActivity>> threads;
    private AssociatedActivityFilterType activityFilterType = AssociatedActivityFilterType.ALL;
    private int startIndex = 0;
    private int maxResults = 10;
    private Vouch vouch;

    // Indicates whether this is the first time the page has been viewed
    private boolean firstTime;

    private UserPersona persona;
    private Collection<ViewedBy> viewedBy;

    protected User user;

    protected boolean owner;
    protected Profile profile;

    // Vouches for this user
    private PaginationResults<UserVouch> vouches;


    // Vouches this user has given
    protected List<Vouch> hasVouched;

    // Permanent vouch availablity
    protected boolean permanentVouch;
    protected boolean vouched;

    @Before
    public void preProcess() {
        if (isBlank(identifier)) {
            identifier = getCurrentUser().getUsername();
        }

        user = userManager.loadUserByUsername(identifier);
        if (user == null) user = userManager.loadUserByGuid(identifier);

        if (user == null) {
            String msg = String.format("User passed in %s does not exist", identifier);
            logger.warn(msg);
            throw new IllegalArgumentException(msg);
        }

        if (isOwner(user)) {
            owner = true;
        }
    }


    public int getTotalVouches() {
        int total = 0;
        if (vouches.getResults() != null) {
            for (UserVouch vouch : vouches.getResults()) {
                if (vouch != null) {
                    if (vouch.getDirection() == UserVouch.VouchDirection.MUTUAL || vouch.getDirection() == UserVouch.VouchDirection.INCOMING) {
                        total++;
                    }
                }
            }
        }
        return total;
    }

    @DefaultHandler
    @DontValidate
    public Resolution begin() {

        if (user == null) throw new IllegalArgumentException("An invalid user param was passed in.");

        int userStatus = user.getStatus().getValue();
        if (userStatus == 1 || userStatus == 3 || userStatus == 5) {
            return new RedirectResolution("/persona_inactive.jsp");
        }

        profile = user.getProfile();


        vouches = getSocialManager().loadUserVouches(user.getGuid(), 0, 666); //THE NUMBER OF THE BEAST

        hasVouched = getSocialManager().loadOutgoingByVoucher(user.getGuid());
        persona = userManager.loadUserPersona(user.getGuid(), getCurrentUser().getGuid());
        threads = loadActivityThreads(0, 4);

        // If the user is not the current user
        // The we need to mark that this profile has been viewed
        if (user != null && !isOwner()) {
            // execute on the thread pole, because we don't care about results
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    userManager.saveViewedBy(getCurrentUser().getGuid(), user.getGuid(), ViewedByType.PROFILE);
                }
            });
        }

        if (isOwner()) {
            getContext().getRequest().getSession().removeAttribute("ownerGuid");
        }
        else {
            getContext().getRequest().getSession().setAttribute("ownerGuid", user.getGuid());
            viewer = userManager.loadUserByGuid(user.getGuid()).getProfile();
            setViewerProfileImage(profile.getProfileImgUrl());
        }

        loadAlbums();

        viewedBy = persona.getViewedBy();
        ownerProfileImage = persona.getProfileImageUrl();
        relationships = persona.getRelationships();
        occasionList = occasionManager.loadByOwnerGuid(user.getGuid());

        vouched = isAlreadyVouched();

        return new ForwardResolution("/persona.jsp");
    }


    private void loadAlbums() {
        EntityReference albumOwner = user.toEntityReference();
        List<MediaLibrary> totalMediaLibList = mediaManager.loadMediaLibrariesForOwner(albumOwner);
        mediaLibList = getMediaLibListByType(totalMediaLibList, MediaLibraryType.IMAGE);
        mediaLibList = getMediaLibListBasedOnNetwork(mediaLibList, user, getCurrentUser());
        for (MediaLibrary library : mediaLibList) {
            List<MediaMetaData> mmd = library.getMedia();
            if (mmd != null) {
                totalImages += mmd.size();
            }
        }
    }

    public Resolution deleteAlbum() throws JSONException {
        mediaManager.deleteMediaLibrary(albumGuid);
        Count allAlbumTotal = mediaManager.totalPhotosAllAlbumsForUser(getCurrentUser().getGuid());
        Count totalNumberAlbums = mediaManager.totalNumberOfAlbumsForUser(getCurrentUser().getGuid());
        JSONObject json = new JSONObject();
        json.put("allAlbumTotal", allAlbumTotal.getValue());
        json.put("totalNumberAlbums", totalNumberAlbums.getValue());
        return createResolutionWithJson(getContext(), json);
    }

    public Resolution deleteImage() throws JSONException {
        Count albumTotal = mediaManager.deleteMediaMetaDataFromLibrary(imageGuid, getCurrentUser().getGuid());
        Count allAlbumTotal = mediaManager.totalPhotosAllAlbumsForUser(getCurrentUser().getGuid());
        JSONObject json = new JSONObject();
        json.put("allAlbumTotal", allAlbumTotal.getValue());
        json.put("albumTotal", albumTotal.getValue());
        return createResolutionWithJson(getContext(), json);
    }

    public Resolution createAlbum() {
        loadAlbums();
        return new ForwardResolution("/ajax/persona/photos/persona_album_create.jspf");
    }

    public Resolution addToAlbum() {
        loadAlbums();
        currentAlbum = mediaManager.loadMediaLibraryByGuid(albumGuid);
        updateMediaLibBasedOnNetwork(currentAlbum, user, getCurrentUser());
        getContext().getRequest().getSession().setAttribute("com.blackbox.presentation.action.MEDIA_LIB", currentAlbum);
        return new ForwardResolution("/ajax/persona/photos/persona_album_create.jspf");
    }

    public Resolution viewAlbums() {
        loadAlbums();
        return new ForwardResolution("/WEB-INF/jsp/include/persona_albums.jspf");
    }

    public Resolution viewAlbum() {
        loadAlbums();
        currentAlbum = mediaManager.loadMediaLibraryByGuid(albumGuid);
        updateMediaLibBasedOnNetwork(currentAlbum, user, getCurrentUser());
        return new ForwardResolution("/ajax/persona/photos/persona_album.jspf");
    }

    public Resolution activity() {
        threads = loadActivityThreads(startIndex, maxResults);
        return new ForwardResolution("/WEB-INF/jsp/include/persona_activity.jspf");
    }

    public Resolution activity_list() throws JSONException {
        threads = loadActivityThreads(startIndex, maxResults);

        if (getView() == json) {
            return createResolutionWithJsonArray(getContext(), threadsToJson(threads));
        }

        return new ForwardResolution("/ajax/stream/activity_list.jspf");
    }

    public Resolution vouches() throws JSONException {
        return new ForwardResolution("/WEB-INF/jsp/include/persona_vouches.jspf");
    }

    public Resolution wordCloud() throws JSONException {
        List<WordFrequency> list = userManager.highFrequencyWords(user.getGuid());
        return createResolutionWithJsonArray(getContext(), toJSON(list));
    }

    protected JSONArray threadsToJson(Collection<ActivityThread<IActivity>> threads) throws JSONException {

        JSONArray array = new JSONArray();
        for (IActivityThread<IActivity> activity : threads) {
            array.put(JSONUtil.toJSON(activity));
        }
        return array;
    }

    public List<MediaLibrary> getMediaLibList() {
        return mediaLibList;
    }

    public IActivityManager getActivityManager() {
        return activityManager;
    }

    public void setActivityManager(IActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    public Collection<ActivityThread<IActivity>> getThreads() {
        return threads;
    }

    public void setThreads(Collection<ActivityThread<IActivity>> threads) {
        this.threads = threads;
    }

    public AssociatedActivityFilterType getActivityFilterType() {
        return activityFilterType;
    }

    public void setActivityFilterType(AssociatedActivityFilterType activityFilterType) {
        this.activityFilterType = activityFilterType;
    }

    public void setMediaLibList(List<MediaLibrary> mediaLibList) {
        this.mediaLibList = mediaLibList;
    }

    public MediaLibrary getShowingMediaLib() {
        return showingMediaLib;
    }

    public void setShowingMediaLib(MediaLibrary showingMediaLib) {
        this.showingMediaLib = showingMediaLib;
    }

    public MediaLibrary getCurrentAlbum() {
        return currentAlbum;
    }

    public void setCurrentAlbum(MediaLibrary currentAlbum) {
        this.currentAlbum = currentAlbum;
    }

    public String getAlbumGuid() {
        return albumGuid;
    }

    public void setAlbumGuid(String albumGuid) {
        this.albumGuid = albumGuid;
    }

    public String getImageGuid() {
        return imageGuid;
    }

    public void setImageGuid(String imageGuid) {
        this.imageGuid = imageGuid;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public List<MediaLibrary> getVideoLibList() {
        return videoLibList;
    }

    public Collection<Relationship> getRelationships() {
        return relationships;
    }

    public void setVideoLibList(List<MediaLibrary> videoLibList) {
        this.videoLibList = videoLibList;
    }

    public String getOwnerProfileImage() {
        return ownerProfileImage;
    }

    public IMediaManager getContentManager() {
        return mediaManager;
    }

    public void setContentManager(IMediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    public Profile getViewer() {
        return viewer;
    }

    public void setViewer(Profile viewer) {
        this.viewer = viewer;
    }

    public String getViewerProfileImage() {
        return viewerProfileImage;
    }

    public void setViewerProfileImage(String viewerProfileImage) {
        this.viewerProfileImage = viewerProfileImage;
    }

    public List<Occasion> getOccasionList() {
        return occasionList;
    }

    public void setOccasionList(List<Occasion> occasionList) {
        this.occasionList = occasionList;
    }

    public void setOccasionManager(IOccasionManager occasionManager) {
        this.occasionManager = occasionManager;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public Collection<ViewedBy> getViewedBy() {
        return viewedBy;
    }


    public UserPersona getPersona() {
        return persona;
    }

    public boolean isOwner() {
        return owner;
    }

    public boolean isOnline() {
        return userSessionService.isUserOnline(user.getGuid());
    }

    public PaginationResults<UserVouch> getVouches() {
        return vouches;
    }

    public void setVouches(PaginationResults<UserVouch> vouches) {
        this.vouches = vouches;
    }

    public boolean isVouched() {
        return vouched;
    }

    public void setVouched(boolean vouched) {
        this.vouched = vouched;
    }

    @DontValidate
    public Resolution vouch() {
        logger.info(" vouch");
        //JSONObject resultMessage = new JSONObject();

        if (!owner) {
            vouch.setVoucher(getCurrentUser().toEntityReference());
            vouch.setTarget(user.toEntityReference());
            getSocialManager().vouch(new VouchRequest(vouch, permanentVouch));
        }

        return createResolutionWithText(getContext(), "success");
    }

    private boolean isAlreadyVouched() {
        boolean vouched = true;
        if (!owner) {
            Vouch userVouch = getSocialManager().loadVouchByVoucherAndVouchee(getCurrentUser().getGuid(), user.getGuid());
            if (userVouch == null) {
                vouched = false;
            }
        }
        return vouched;
    }

    @DontValidate
    public Resolution wish() throws JSONException {
        executeWish(user.toEntityReference());
        WishStatus wishStatus = getBookmarkManager().loadWishStatus(getCurrentUser().getGuid(), user.getGuid());
        return createResolutionWithText(getContext(), wishStatus.toString());
    }


    @DontValidate
    public Resolution unVouch() {
        logger.info(" unvouch");
        if (!owner) {
            getSocialManager().deleteVouch(getCurrentUser().getGuid(), user.getGuid());
        }
        return createResolutionWithText(getContext(), "success");
    }

    @DontValidate
    public Resolution deleteVouch() {
        logger.info(" unvouch");
        if (!owner) {
            getSocialManager().deleteVouch(user.getGuid(), getCurrentUser().getGuid());
        }
        return createResolutionWithText(getContext(), "success");
    }


    @DontValidate
    public Resolution unrelate() throws JSONException {
        if (!owner) {
            User current = getCurrentUser();
            getSocialManager().deleteRelationship(current.getGuid(), user.getGuid());
        }

        if ("persona".equals(returnPage)) {
            return new RedirectResolution("/persona/" + user.getGuid());
        }
        RelationshipNetwork network = getSocialManager().loadRelationshipNetwork(getCurrentUser().getGuid());
        return createResolutionWithJson(getContext(), getRelationshipStates(network, isBlocked(user)));
    }

    @DontValidate
    public Resolution follow() throws JSONException {
        if (!isAllowedToFollow(getCurrentUser(), user.getGuid())) {
            return createResolutionPrivacyError(getContext());
        }
        executeFollow(user.toEntityReference());
        if ("persona".equals(returnPage)) {
            return new RedirectResolution("/persona/" + user.getGuid());
        }
        RelationshipNetwork network = getSocialManager().loadRelationshipNetwork(getCurrentUser().getGuid());

        return createResolutionWithJson(getContext(), getRelationshipStates(network, isBlocked(user)));
    }

    @DontValidate
    public Resolution block() throws JSONException {
        if (!owner) {
            User current = getCurrentUser();
            getSocialManager().ignore(new Ignore(current.toEntityReference(), user.toEntityReference(), Ignore.IgnoreType.HARD));
        }

        if ("persona".equals(returnPage)) {
            return new RedirectResolution("/persona/" + user.getGuid());
        }
        RelationshipNetwork network = getSocialManager().loadRelationshipNetwork(getCurrentUser().getGuid());
        return createResolutionWithJson(getContext(), getRelationshipStates(network, true));
    }

    @DontValidate
    public Resolution unblock() throws JSONException {
        if (!owner) {
            User current = getCurrentUser();
            getSocialManager().deleteIgnore(current.getGuid(), user.getGuid());
        }

        if ("persona".equals(returnPage)) {
            return new RedirectResolution("/persona/" + user.getGuid());
        }
        RelationshipNetwork network = getSocialManager().loadRelationshipNetwork(getCurrentUser().getGuid());
        return createResolutionWithJson(getContext(), getRelationshipStates(network, false));
    }


    @DontValidate
    public Resolution ignored() {
        executeIgnore(user.toEntityReference());
        return createResolutionWithText(getContext(), "success");
    }

    @DontValidate
    public Resolution accept() {
        executeAccept(user.getGuid());
        return createResolutionWithText(getContext(), "success");
    }

    @DontValidate
    public Resolution reject() {
        User current = getCurrentUser();
        getSocialManager().rejectRequest(current.getGuid(), user.getGuid());
        return createResolutionWithText(getContext(), "success");
    }

    public List<BaseEntity> getFollowed() {
        List<BaseEntity> followed = new ArrayList<BaseEntity>();
        List<Relationship> toReList = getSocialManager().loadRelationshipsByToEntityGuid(user.toEntityReference().getGuid());
        for (Relationship re : toReList) {
            if (re.getWeight() >= Relationship.RelationStatus.FOLLOW.getWeight() && re.getWeight() < Relationship.RelationStatus.FRIEND.getWeight()) {
                followed.add(re.getFromEntityObject());
            }
        }
        return followed;
    }

    @DontValidate
    public Resolution unWish() throws JSONException {
        User current = getCurrentUser();
        getBookmarkManager().deleteBookmark(current.getGuid(), user.getGuid());
        getAvatarCache().flush(new AvatarCacheKey(current.getGuid(), user.getGuid()));
        getAvatarCache().flush(new AvatarCacheKey(user.getGuid(), current.getGuid()));
        if ("persona".equals(returnPage)) {
            return new RedirectResolution("/persona/" + user.getGuid());
        }
        return createResolutionWithJson(getContext(), getWishStates());
    }


    @DontValidate
    public Resolution friend() throws JSONException {
        if (!owner) {
            User current = getCurrentUser();
            Relationship r = Relationship.createRelationship(
                    current.toEntityReference(),
                    user.toEntityReference(),
                    Relationship.RelationStatus.FRIEND_PENDING);
            getSocialManager().relate(r);
        }
        if ("persona".equals(returnPage)) {
            return new RedirectResolution("/persona/" + user.getGuid());
        }
        RelationshipNetwork network = getSocialManager().loadRelationshipNetwork(getCurrentUser().getGuid());
        return createResolutionWithJson(getContext(), getRelationshipStates(network, isBlocked(user)));
    }

    /**
     * synonym for unrelate
     *
     * @return streaming resolution with json
     * @throws org.json.JSONException Thrown if there is problems creating json object
     */
    @DontValidate
    public Resolution unfriend() throws JSONException {
        return unrelate();
    }


    private JSONObject getRelationshipStates(RelationshipNetwork network, boolean isDonkey) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("isFriend", network.isFriend(user.getGuid()));
        object.put("isPending", network.isRelationStatus(user.getGuid(), Relationship.RelationStatus.FRIEND_PENDING));
        object.put("isFollowing", network.isFollowing(user.getGuid()));
        object.put("isBlocked", isDonkey);
        return object;
    }

    private JSONObject getWishStates() throws JSONException {
        WishStatus wishStatus = getBookmarkManager().loadWishStatus(getCurrentUser().getGuid(), user.getGuid());
        JSONObject object = new JSONObject();
        object.put("isWished", wishStatus == WishStatus.WISHED);
        object.put("isBeenWished", wishStatus == WishStatus.WISHED_BY);
        object.put("isWishedBothSide", wishStatus == WishStatus.MUTUAL);
        return object;
    }


    @Override
    public MenuLocation getMenuLocation() {
        return MenuLocation.persona;
    }

    public User getUser() {
        return user;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getReturnPage() {
        return returnPage;
    }

    public void setReturnPage(String returnPage) {
        this.returnPage = returnPage;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public boolean isWished() {
        return persona.getWishStatus() == WishStatus.WISHED;
    }

    public boolean isBeenWished() {
        return persona.getWishStatus() == WishStatus.WISHED_BY;
    }

    public boolean isWishedBothSide() {
        return persona.getWishStatus() == WishStatus.MUTUAL;
    }

    public boolean isPermanentVouch() {
        return permanentVouch;
    }

    public void setPermanentVouch(boolean permanentVouch) {
        this.permanentVouch = permanentVouch;
    }

    protected Collection<ActivityThread<IActivity>> loadActivityThreads(int startIndex, int maxResults) {
        if (getCurrentUser().getGuid().equals(user.getGuid()) || JspFunctions.isFriend(getCurrentUser())) {
            return activityManager.loadAssociatedActivity(user.getGuid(), AssociatedActivityFilterType.ALL, startIndex, maxResults);
        }
        else {
            return activityManager.loadAssociatedActivityFilterNetworkTypes(user.getGuid(), AssociatedActivityFilterType.ALL,
                    new NetworkTypeEnum[]{NetworkTypeEnum.ALL_MEMBERS, NetworkTypeEnum.WORLD}, startIndex, maxResults);
        }

    }

    protected PaginationResults<IActivity> loadVirtualGifts(int startIndex, int numResults) {
        return giftingManager.loadVirtualGiftsReceivedInBounds(user.getGuid(), startIndex, numResults);
    }

    public PaginationResults<IActivity> getGifts() {
        return gifts;
    }

    public Vouch getVouch() {
        return vouch;
    }

    public void setVouch(Vouch vouch) {
        this.vouch = vouch;
    }

    public int getTotalImages() {
        return totalImages;
    }

    public List<Vouch> getHasVouched() {
        return hasVouched;
    }

    public void setHasVouched(List<Vouch> hasVouched) {
        this.hasVouched = hasVouched;
    }

    public boolean getBlocksCurrentUser() {
        List<EntityReference> blocks = getSocialManager().loadBlocks(user.getGuid());
        return blocks.contains(getCurrentUser().toEntityReference());
    }

}
