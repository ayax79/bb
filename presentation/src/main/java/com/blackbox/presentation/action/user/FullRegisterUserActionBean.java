/*
 * 
 */
package com.blackbox.presentation.action.user;

import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.Status;
import com.blackbox.foundation.Utils;
import com.blackbox.foundation.activity.ActivityProfile;
import com.blackbox.foundation.billing.BillingInfo;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.media.MediaPublish;
import com.blackbox.foundation.media.MediaRecipient;
import com.blackbox.foundation.security.SecurityRealmEnum;
import com.blackbox.foundation.security.UsernameOnlyAuthToken;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.user.*;
import com.blackbox.foundation.util.IGeoUtil;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.persona.PersonaActionBean;
import com.blackbox.presentation.action.util.EighteenOrOlderTypeConverter;
import com.blackbox.presentation.action.util.MediaUtil;
import com.blackbox.presentation.extension.BlackBoxContext;
import com.blackbox.presentation.session.UserSessionService;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.*;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.publish.objectmodel.ArtifactType;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

import static com.blackbox.foundation.EntityReference.createEntityReference;
import static com.blackbox.foundation.IBlackBoxConstants.BUFFER_SIZE;
import static com.blackbox.presentation.action.media.SessionImageActionBean.SESSION_IMAGE_PARAM;
import static com.blackbox.presentation.action.util.PresentationUtil.getProperty;
import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.apache.commons.io.FileUtils.openInputStream;
import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;

/**
 * Handles Registration of Users
 */
@SuppressWarnings({"UnusedDeclaration"})
@UrlBinding("/register/{$event}/{pc}")
public class FullRegisterUserActionBean extends BaseBlackBoxActionBean {


    public static final String REGISTRATION_USER = "com.blackbox.presentation.action.user.FullRegisterUserActionBean.USER";
    public static final String PROMO_CODE_KEY = "promoCode";
    public static final String DEFAULT_PLACEHOLDER = "/library/images/defaultplaceholder.jpg";

    private static final Logger logger = LoggerFactory.getLogger(FullRegisterUserActionBean.class);

    @SpringBean("mediaManager")
    IMediaManager mediaManager;
    @SpringBean("userManager")
    IUserManager userManger;
    @SpringBean("uploadTempDir")
    File uploadTempImageDir;
    @SpringBean("geoUtil")
    IGeoUtil geoUtil;
    @SpringBean("userSessionService")
    UserSessionService userSessionService;

    /**
     * Contains all of the current registration information for the current registration.
     * This value will be stored in the session with the session key {@link #REGISTRATION_USER}.
     */
    @ValidateNestedProperties({
            @Validate(field = "username", required = true, mask = "\\w+", maxlength = 25, on = "step2", minlength = 3),
            @Validate(field = "email", required = true, converter = EmailTypeConverter.class, on = "step2"),
            @Validate(field = "password", required = true, minlength = 5, on = "step2"),
            @Validate(field = "name", required = true, on = "step3"),
            @Validate(field = "lastname", required = true, on = "step3")
    })
    protected User user;

    @ValidateNestedProperties({
            @Validate(field = "location.city", required = true, on = "step3"),
            @Validate(field = "location.country", required = true, on = "step3"),
            @Validate(field = "location.zipCode", required = true, on = "step3"),
            @Validate(field = "timeZone", required = true, on = "step3"),
            @Validate(field = "sex", required = true, on = "step3"),
            @Validate(field = "birthday", required = true, converter = EighteenOrOlderTypeConverter.class, on = "step3")
    })
    protected Profile profile;

    /**
     * The field that will contain the second time the password was entered so we can
     * validate that they entered their password correctly twice.
     */
    @Validate(required = true, minlength = 5, on = "step2")
    protected String password2;

    @Validate(required = true, on = "step2")
    protected String kaptcha;

    @Validate(required = true, on = "step3")
    protected String avatarUploaded;


    @Validate(required = true, mask = "yes", on = "step2")
    protected String registerLegal;
    protected String tnc;

    /**
     * Avatar crop Height
     */
    protected int avatarH;

    /**
     * Avatar crop width
     */
    protected int avatarW;

    /**
     * Avatar horizontal crop placement
     */
    protected int avatarX;

    /**
     * Avatar vertical crop placement
     */
    protected int avatarY;

    /**
     * Persona picture crop height
     */
    protected int personaH;

    /**
     * Persona picture crop width
     */
    protected int personaW;

    /**
     * Persona crop horizontal start point.
     */
    protected int personaX;

    /**
     * Persona crop vertical start point.
     */
    protected int personaY;

    /**
     * Promocode code passed in on the request either on the url at start of registration or
     * on the second step before billing.
     */
    protected String pc;

    /**
     * Holds a reference to the promocode the user is registering with if one exists.
     * <p/>
     * This value is acquired by using the pc param specified above.
     */
    protected BasePromoCode promoCode;

    /**
     * An array of emails of people the current registree is opting to pay for.
     */
    protected String[] leechEmails;

    /**
     * Contains the file reference used for file uploads
     */
    protected FileBean userImage;

    /**
     * Performs some preProcessing functions:<br />
     * <ol>
     * <li> If there was a promocode specified in the request we will invalidate the session since it should be
     * a new registration.
     * <li> Performs a check to see if there is a user already logged in, if there is then we will forward them back
     * to the dashboard since you shouldn't be allowed to run through registration when you are already logged in.
     * <li> Handles pulling the user object with registration information from the session.
     * </ol>
     *
     * @return Null unless there is a user already logged in, then it will return redirect resolution back to the dashboard.
     */
    @Before(stages = LifecycleStage.BindingAndValidation)
    public RedirectResolution preProcess() {
        HttpServletRequest request = getContext().getRequest();
        HttpSession session = getContext().getRequest().getSession();

        // Should be a new registration if there is a promocode, invalidate the session just to be safe.
        if (request.getParameter("promoCode") != null) {
            session.invalidate();
        }

        // They are already logged in, don't let them through registration.
        if (getCurrentUser() != null) {
            logger.warn("Attempted to go through registration as a already logged in user: " + getCurrentUser());
            return new RedirectResolution(DashBoardActionBean.class);
        }

        BlackBoxContext blackBoxContext = getContext();
        if (blackBoxContext.getValidationErrors().isEmpty()) {
            String username = request.getParameter("user.username");
            if (username != null) {
                // check and see if there is an incomplete registration for this user
                user = userManger.loadUserByUsername(username);
                if (user != null && user.getStatus() != Status.INCOMPLETE) {
                    // this should not happen, go ahead and report it as an error since a registration has been completed for this
                    // username already.
                    // though it is unlikely they will ever get here.
                    getContext().getValidationErrors().add("user.username", new SimpleError("Username must be unique", username));
                    return null; //short circuit
                }
            }

            // check to see if there is already a user in the session
            if (user == null) {
                user = (User) session.getAttribute(REGISTRATION_USER);
            }

            // finally if we were unable to an attain an existing incomplete registration or 
            if (user == null) {
                user = User.createUser();
            }
            user.setStatus(Status.INCOMPLETE); // keep them in the incomplete state
            profile = user.getProfile();
            promoCode = (BasePromoCode) session.getAttribute(PROMO_CODE_KEY);
        }

        return null;
    }

    /**
     * Overrides the user in the session with any changes from the action event method.
     */
    @After
    public void replaceSessionUser() {
        BlackBoxContext blackBoxContext = getContext();
        if (blackBoxContext.getValidationErrors().isEmpty()) {
            blackBoxContext.getRequest().getSession().setAttribute(REGISTRATION_USER, user);
        }
    }

    /**
     * Performs the kaptcha validation from the first step of registration.
     *
     * @param errors Collection of errors than can be populated.
     */
    @ValidationMethod(on = "step2")
    public void validateStep2(ValidationErrors errors) {
        logger.info("called validation");

        HttpSession session = getContext().getRequest().getSession();
        String kaptchaExpected = (String) session.getAttribute(KAPTCHA_SESSION_KEY);

//        if (kaptcha == null || !kaptcha.equalsIgnoreCase(kaptchaExpected)) {
//            errors.add("kaptcha", new LocalizableError("register.kaptcha.error"));
//        }

        if (!user.getPassword().equals(password2)) {
            errors.put("password2", new LocalizableError("register.validation.password.mismatch"));
        }

    }

    /**
     * A registration entry point that allows a promo code to be specified in the url.
     * After setting up the promo code in the session, this action will just forward to the first page of registration.
     *
     * @return a forward resolution to the first page of resolution.
     */
    public Resolution code() {
        if (pc != null) {

            if (promoCode == null) {
                promoCode = userManger.loadPromoCodeByCode(pc);
            }
            if (promoCode == null) {
                promoCode = new InvalidPromoCode(pc);
            }
            getContext().getRequest().getSession().setAttribute(PROMO_CODE_KEY, promoCode);

        }

        return begin();
    }


    /**
     * Default entry point for registration.
     *
     * @return An action forward to the first page of registration.
     */
    @DefaultHandler
    public Resolution begin() {
        logger.info("called begin");

        return new ForwardResolution("/WEB-INF/jsp/include/register/reg-step1.jspf");
    }

    /**
     * Collects information from the first step of registration and forwards to step too.
     *
     * @return A forward resolution to step 2 of resolution.
     */
    public Resolution step2() {
        if (promoCode != null && promoCode instanceof InvalidPromoCode) {
            getContext().getValidationErrors().add("pc", new LocalizableError("register.promo.invalid"));
            pc = promoCode.getCode();
        } else if (promoCode != null) {
            pc = promoCode.getCode();
        }

        userManger.captureEmail(user.getEmail());

        // if everything worked alright, save a registration in an incomplete state
        user.setStatus(Status.INCOMPLETE);
        assert user.getGuid() != null;
        // Make sure the password gets hashed.
        user.setPassword(sha1Hash(user.getPassword()));
        user = userManger.save(user);
        assert user != null;
        assert user.getGuid() != null;
        assert user.getVersion() != null;

        return new ForwardResolution("/WEB-INF/jsp/include/register/reg-step2.jspf");
    }

    @ValidationMethod(on = "step3")
    public void validateConfirm(ValidationErrors errors) {
        if (pc != null) {
            if (promoCode == null) {
                promoCode = userManger.loadPromoCodeByCode(pc);
            }
            if (promoCode == null) {
                errors.add("pc", new LocalizableError("register.promo.invalid"));
            } else {
                getContext().getRequest().getSession().setAttribute(PROMO_CODE_KEY, promoCode);
            }

        }
    }


    public Resolution step3() {
        // step 3 used to be billing, now it just goes to the confirm page.
        logger.info("called step3");
        return new ForwardResolution("/WEB-INF/jsp/include/register/confirm.jspf");
    }

    /**
     * The last step of registration.
     *
     * @return A redirect resolution to login with login credentials specified.
     */
    public Resolution register() {
        logger.info("called register");
        user.setStatus(Status.ENABLED);
        user.setOwnerType(EntityType.USER);
        if (user.getGuid() == null) {
            Utils.applyGuid(user);
        }

        //----------------------------------------
        handleUserImages();
        getProfile().getLocation().setGeoLocation(geoUtil.fetchGeoInfoForAddress(getProfile().getLocation()));
        //save for account privacy
        applyDefaultPrivacySettings();
        user.setProfile(profile);
        logger.info("before reg, user Type is:" + user.getType());


        HttpSession session = getContext().getRequest().getSession();

        String affiliateIdentifier = getAffiliateIdentifier();
        Registration registration = new Registration(user, affiliateIdentifier, leechEmails != null ? Arrays.asList(leechEmails) : null);
        if (promoCode != null) {
            registration.setPromoCodeGuid(promoCode.getGuid());
        }

        userManger.register(registration);

        try {
            Thread.sleep(2000); //sleep for 2 seconds to make sure everything on server side is ok
        }
        catch (Exception ex) {
            // ignored 
        }

        session.removeAttribute(PROMO_CODE_KEY);
        session.removeAttribute(REGISTRATION_USER);

        UsernameOnlyAuthToken authToken = new UsernameOnlyAuthToken(user.getUsername());
        authToken.setRealm(SecurityRealmEnum.WEB);
        Subject subject = SecurityUtils.getSubject();
        subject.login(authToken);
        userSessionService.populateContext(user, getContext());

        return new RedirectResolution(PersonaActionBean.class)
                .addParameter("firstTime", "true");

    }

    private void applyDefaultPrivacySettings() {
        profile.setLimitedCanComment(false);
        profile.setLimitedCanFollow(false);
        profile.setLimitedCanPm(false);
        profile.setLimitedCanSearch(false);
        profile.setLimitedCanSeeActivity(false);
        profile.setLimitedCanSeeGifts(false);
        profile.setLimitedCanSeePictures(false);
        profile.setNonFriendsCanComment(true);
        profile.setNonFriendsCanFollow(true);
        profile.setNonFriendsCanPm(true);
        profile.setNonFriendsCanSearch(true);
    }

    protected void handleUserImages() {
        final String uploadFullPath = getUploadTempImageDir().getAbsolutePath() + File.separator + Utils.generateGuid();

        FileBean fileData = (FileBean) getContext().getRequest().getSession().getAttribute(SESSION_IMAGE_PARAM);
        String location = null;
        String avatarLocation = null;
        if (fileData != null && fileData.getFileName() != null) {
            String fileName = fileData.getFileName().replace(" ", "_");
            MediaMetaData profileImage = MediaUtil.prepareBuildMetaData(ArtifactType.IMAGE, fileName, false, fileData);
            MediaMetaData avatarImage = MediaUtil.prepareBuildMetaData(ArtifactType.IMAGE, "avatar_" + fileName, false, fileData);
            profileImage.setArtifactOwner(createEntityReference(user));
            avatarImage.setArtifactOwner(createEntityReference(user));
            avatarImage.setProfile(false);
            avatarImage.setAvatar(true);
            profileImage.setProfile(true);
            avatarImage.setRecipientDepth(NetworkTypeEnum.SELF);
            profileImage.setRecipientDepth(NetworkTypeEnum.SELF);
            avatarImage.getRecipients().add(new MediaRecipient(createEntityReference(user), avatarImage));
            profileImage.getRecipients().add(new MediaRecipient(createEntityReference(user), profileImage));
            avatarImage.setSenderProfile(new ActivityProfile());
            profileImage.setSenderProfile(new ActivityProfile());
            File imageFile;
            File avatarFile;
            InputStream image;
            InputStream avatarImageStream;
            try {
                //crop profile image
                if (!(personaX == 0 && personaY == 0 && personaW == 0 && personaH == 0)) {
                    imageFile = MediaUtil.getCroppedImage(fileData, uploadFullPath, personaX, personaY, personaW, personaH);
                    image = new BufferedInputStream(openInputStream(imageFile), BUFFER_SIZE);
                } else {
                    image = fileData.getInputStream();
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                IOUtils.copy(image, outputStream);
                location = mediaManager.publishMedia(new MediaPublish<MediaMetaData>(profileImage, outputStream.toByteArray())).getLocation();
                logger.info("profileImage:" + location);
                //crop avatar image
                if (!(avatarX == 0 && avatarY == 0 && avatarW == 0 && avatarY == 0)) {
                    avatarFile = MediaUtil.getCroppedImage(fileData, uploadFullPath, avatarX, avatarY, avatarW, avatarH);
                    avatarImageStream = new BufferedInputStream(openInputStream(avatarFile), BUFFER_SIZE);
                } else {
                    avatarImageStream = fileData.getInputStream();
                }
                outputStream = new ByteArrayOutputStream();
                IOUtils.copy(avatarImageStream, outputStream);
                avatarLocation = mediaManager.publishMedia(new MediaPublish<MediaMetaData>(avatarImage, outputStream.toByteArray())).getLocation();
                logger.info("avatarImage:" + avatarLocation);
            } catch (Exception e) {
                logger.error("error", e);
            } finally {
                try {
                    deleteDirectory(new File(uploadFullPath));
                } catch (Exception e) {
                    logger.error("error cleaning up: " + uploadFullPath);
                }
                FileBean old = (FileBean) getContext().getRequest().getSession().getAttribute(SESSION_IMAGE_PARAM);
                if (old != null) {
                    try {
                        old.delete();
                    } catch (Exception e) {
                        logger.error("error cleaning up", e);
                    }
                }
                getContext().getRequest().getSession().removeAttribute(SESSION_IMAGE_PARAM);
            }
            getProfile().setProfileImgUrl(location);
            getProfile().setAvatarUrl(avatarLocation);
        } else {
            getProfile().setProfileImgUrl(DEFAULT_PLACEHOLDER);
            getProfile().setAvatarUrl(DEFAULT_PLACEHOLDER);

        }
    }

    protected FileBean getSessionImage() {
        HttpSession session = getContext().getRequest().getSession();
        FileBean fileBean = (FileBean) session.getAttribute(SESSION_IMAGE_PARAM);
        session.removeAttribute(SESSION_IMAGE_PARAM);
        return fileBean;
    }

    protected BillingInfo buildFromPromoCode(BasePromoCode promoCode) {
        assert promoCode != null;
        BillingInfo mb = new BillingInfo();
        mb.setProviderGuid(promoCode.getGuid());
        mb.setProvider(BillingInfo.BillingProvider.LEECH);
        mb.setLastBilled(new DateTime());
        if (promoCode.getEvaluationPeriod() != null) {
            mb.setNextBillDate(new DateTime().plusMonths(promoCode.getEvaluationPeriod()));
        }
        return mb;
    }

    public String getRecaptchaPublicKey() {
        return getProperty("recaptcha.public.key");
    }


    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public File getUploadTempImageDir() {
        return uploadTempImageDir;
    }

    public void setUploadTempImageDir(File uploadTempImageDir) {
        this.uploadTempImageDir = uploadTempImageDir;
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

    public String getRegisterLegal() {
        return registerLegal;
    }

    public void setRegisterLegal(String registerLegal) {
        this.registerLegal = registerLegal;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public BasePromoCode getPromoCode() {
        return promoCode;
    }

    public int getAvatarH() {
        return avatarH;
    }

    public void setAvatarH(int avatarH) {
        this.avatarH = avatarH;
    }

    public int getAvatarW() {
        return avatarW;
    }

    public void setAvatarW(int avatarW) {
        this.avatarW = avatarW;
    }

    public int getAvatarX() {
        return avatarX;
    }

    public void setAvatarX(int avatarX) {
        this.avatarX = avatarX;
    }

    public int getAvatarY() {
        return avatarY;
    }

    public void setAvatarY(int avatarY) {
        this.avatarY = avatarY;
    }

    public int getPersonaH() {
        return personaH;
    }

    public void setPersonaH(int personaH) {
        this.personaH = personaH;
    }

    public int getPersonaW() {
        return personaW;
    }

    public void setPersonaW(int personaW) {
        this.personaW = personaW;
    }

    public int getPersonaX() {
        return personaX;
    }

    public void setPersonaX(int personaX) {
        this.personaX = personaX;
    }

    public int getPersonaY() {
        return personaY;
    }

    public void setPersonaY(int personaY) {
        this.personaY = personaY;
    }

    public FileBean getUserImage() {
        return userImage;
    }

    public void setUserImage(FileBean userImage) {
        this.userImage = userImage;
    }

    public String getKaptcha() {
        return kaptcha;
    }


    public String getAvatarUploaded() {
        return avatarUploaded;
    }

    public void setAvatarUploaded(String avatarUploaded) {
        this.avatarUploaded = avatarUploaded;
    }

    public void setKaptcha(String kaptcha) {
        this.kaptcha = kaptcha;
    }

    public String[] getLeechEmails() {
        return leechEmails;
    }

    public void setLeechEmails(String[] leechEmails) {
        this.leechEmails = leechEmails;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String promoCode) {
        this.pc = promoCode;
    }

    public static class InvalidPromoCode extends BasePromoCode {
        private static final long serialVersionUID = 2901181696165840316L;

        public InvalidPromoCode() {
        }

        public InvalidPromoCode(String code) {
            setCode(code);
        }
    }

    String getAffiliateIdentifier() {
        if (getContext() == null || getContext().getRequest() == null || getContext().getRequest().getCookies() == null) {
            return null;
        }
        Cookie[] cookies = getContext().getRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (PartnerActionBean.AFFILIATE_COOKIE_KEY.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }


}
