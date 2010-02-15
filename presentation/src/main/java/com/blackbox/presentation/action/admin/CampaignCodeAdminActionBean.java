package com.blackbox.presentation.action.admin;

import com.blackbox.presentation.action.util.RequireUserType;
import com.blackbox.presentation.action.util.PresentationUtil;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.user.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.controller.LifecycleStage;

import java.util.Collection;
import java.util.ArrayList;

import org.joda.time.DateTime;
import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;

/**
 * Description
 *
 * @author Andy Nelsen
 * @date Dec 21, 2009
 */
@RequireUserType({User.UserType.BLACKBOX_ADMIN, User.UserType.BLACKBOX_MARKETING})
public class CampaignCodeAdminActionBean extends BaseBlackBoxActionBean {
    @SpringBean("userManager")
    IUserManager userManager;

    private User user;
    private Profile profile;
    private LookingFor lookingFor;
    private MoodThermometer mood;

    private String campaignName;
    private String username;
    private User.UserType userType;
    private MultiUserPromoCode promoCodeMulti;
    private Integer evaluationPeriod;
    private DateTime expirationDate;
    //private Boolean collectCC;
    private String userStatus;

    private Collection<String> userLevels = new ArrayList<String>();

    @Before (stages = LifecycleStage.BindingAndValidation, on = "createCampaign")
    public void preProcess() {
        lookingFor = new LookingFor();
        mood = new MoodThermometer();
        profile = new Profile();
        user = User.createUser(); //this will reserve a guid, since the promo code creation needs it.
    }

    @DontValidate
    @DefaultHandler
    public Resolution begin() {
        // Set up the available user levels
        userLevels.add("LIMITED");
        userLevels.add("NORMAL");
        return new ForwardResolution("/WEB-INF/admin/marketing/campaign.jspf");
    }

    public Resolution createCampaign() {
        // Initalize LookingFor vals for the new Profile
        lookingFor.setDates(false);
        lookingFor.setDonkeySex(false);
        lookingFor.setFriends(false);
        lookingFor.setHookup(false);
        lookingFor.setLove(false);
        lookingFor.setSnuggling(false);

        // Initialize slider vals for the new Profile
        mood.setEnergyLevel(0);
        mood.setInterestLevel(0);
        mood.setMakePrivate(true);
        mood.setOrientation(0);
        mood.setPolyStatus(0);
        mood.setRelationshipStatus(0);

        // Set up a dummy Profile for the user
        profile.setBirthdayInVisible(true);
        profile.setLookingFor(lookingFor);
        profile.setMood(mood);

        // Create the user in the system
        user.setPassword(sha1Hash(PresentationUtil.getProperty("campaignuser.defaultpassword")));
        user.setEmail(user.getUsername() + "@blackboxrepublic.com");
        user.setType(User.UserType.AFFILIATE);
        user.setProfile(profile);
        userManager.createUser(user);

        // Create the promo code for the campaign
        promoCodeMulti = new MultiUserPromoCode();
        promoCodeMulti.setUserType(userType);
        promoCodeMulti.setMaster(user);
        promoCodeMulti.setPromoCampaignName(campaignName);
        promoCodeMulti.setExpirationDate(expirationDate);
        promoCodeMulti = userManager.createMultiUserPromoCode(promoCodeMulti);

        String campaignUrl = "http://www.blackboxrepublic.com/community/campaign/" + user.getUsername();

        getContext().getMessages().add(new SimpleMessage(String.format("New campaign URL created successfully: %s ", campaignUrl)));
        return new RedirectResolution(CampaignCodeAdminActionBean.class);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public LookingFor getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(LookingFor lookingFor) {
        this.lookingFor = lookingFor;
    }

    public MoodThermometer getMood() {
        return mood;
    }

    public void setMood(MoodThermometer mood) {
        this.mood = mood;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }

    public Integer getEvaluationPeriod() {
        return evaluationPeriod;
    }

    public void setEvaluationPeriod(Integer evaluationPeriod) {
        this.evaluationPeriod = evaluationPeriod;
    }

    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    /*public Boolean isCollectCC() {
        return collectCC;
    }

    public void setCollectCC(Boolean collectCC) {
        this.collectCC = collectCC;
    }*/

    public Collection<String> getUserLevels() {
        return userLevels;
    }

    public void setUserLevels(Collection<String> userLevels) {
        this.userLevels = userLevels;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public MultiUserPromoCode getPromoCodeMulti() {
        return promoCodeMulti;
    }

    public void setPromoCodeMulti(MultiUserPromoCode promoCodeMulti) {
        this.promoCodeMulti = promoCodeMulti;
    }

    public IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }
}
