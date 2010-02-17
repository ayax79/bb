/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.presentation.action.user;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.persona.PersonaActionBean;
import com.blackbox.presentation.action.persona.DefaultPersonaHelper;
import com.blackbox.presentation.action.persona.PersonaHelper;
import com.blackbox.presentation.action.util.DisplayNameCacheKey;
import com.blackbox.presentation.action.util.StringToLowerCaseConverter;

import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithText;

import com.blackbox.foundation.user.ExternalCredentials;

import static com.blackbox.foundation.user.ExternalCredentials.CredentialType.TWITTER;

import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.GeoUtil;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.*;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.cache.ICacheManager;

import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;

import java.io.IOException;
import java.util.List;

/**
 * @author Ida
 */
@UrlBinding("/user/accountsetting/{$event}")
public class AccountSettingsActionBean extends BaseBlackBoxActionBean implements ValidationErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(AccountSettingsActionBean.class);

    @Validate(converter = StringToLowerCaseConverter.class)
    private String password2;


    @SpringBean("userManager")
    IUserManager userManager;
    @SpringBean("displayNameCache")
    ICacheManager<DisplayNameCacheKey, String> displayNameCache;
    @SpringBean("personaHelper")
    PersonaHelper personaHelper;


    @ValidateNestedProperties({
            // @todo How to require at least one of the LookingFor checkboxes to be checked

            // PERSONA
            @Validate(field = "profile.sex", required = true, on = "updatePersona"),
            @Validate(field = "profile.birthday", required = true, on = "updatePersona"/*, converter = EighteenOrOlderTypeConverter.class*/),
            @Validate(field = "profile.lookingForExplain", maxlength = 240, on = "updatePersona"),

            // ACCOUNT
            @Validate(field = "profile.location.city", required = true, on = "updateAccount"),
            @Validate(field = "profile.location.country", required = true, on = "updateAccount"),
            @Validate(field = "profile.location.zipCode", required = true, on = "updateAccount"),
            @Validate(field = "profile.timeZone", required = true, on = "updateAccount"),
            @Validate(field = "name", required = true, on = "updateAccount"),
            @Validate(field = "lastname", required = true, on = "updateAccount"),
            @Validate(field = "email", required = true, converter = EmailTypeConverter.class, on = "updateAccount"),
            @Validate(field = "password", converter = StringToLowerCaseConverter.class)
    })

    private User user;
    private List<ExternalCredentials> externalCredentials;
    private String credentialUsername;
    private String credentialPassword;

    public static enum SettingsPage {
        persona,
        privacy,
        notifications,
        connections,
        account,
        billing
    }

    private boolean showSaveMessage = false;

    private SettingsPage settingsPage = SettingsPage.persona;

    @Before(stages = LifecycleStage.BindingAndValidation)
    public void preProcess() {
        user = userManager.loadUserByGuid(getCurrentUser().getGuid());
        user.setPassword(null);

    }

    @DefaultHandler
    public Resolution begin() {
        logger.info("AccountSettings    begin");
        return new ForwardResolution("/account_settings.jsp");
    }

    @HandlesEvent("ajaxpersona")
    public Resolution persona() {
        logger.info("persona");
        return new ForwardResolution("/ajax/account_settings/member_persona.jspf");
    }

    @HandlesEvent("ajaxintegrations")
    public Resolution integrations() {
        logger.info("integrations");
        return new ForwardResolution("/ajax/account_settings/member_integrations.jspf");
    }

    @HandlesEvent("ajaxbilling")
    public Resolution billing() {
        logger.info("billing");
        return new ForwardResolution("/ajax/account_settings/member_billing.jspf");
    }

    @HandlesEvent("ajaxaccount")
    public Resolution account() {
        logger.info("account");
        return new ForwardResolution("/ajax/account_settings/member_account.jspf");
    }

    @HandlesEvent("ajaxnotifications")
    public Resolution notifications() {
        logger.info("notifications");
        return new ForwardResolution("/ajax/account_settings/member_notifications.jspf");
    }

    @HandlesEvent("ajaxprivacy")
    public Resolution privacy() {
        logger.info("privacy");
        externalCredentials = userManager.loadExternalCredentialsForUser(user.getGuid());
        return new ForwardResolution("/ajax/account_settings/member_privacy.jspf");
    }


    public Resolution handleValidationErrors(ValidationErrors errors) {
        return new ForwardResolution("/account_settings.jsp");
    }

    @ValidationMethod(on = "updateAccount")
    public void validate(ValidationErrors errors) {
        if (isNotBlank(user.getPassword()) && !user.getPassword().equals(password2)) {
            errors.put("password", new LocalizableError("register.validation.password.mismatch"));
            User old = userManager.loadUserByGuid(getCurrentUser().getGuid());
            user.setPassword(old.getPassword());
            settingsPage = SettingsPage.account;
        }
    }

    public Resolution updateAccount() {
        showSaveMessage = true;
        settingsPage = SettingsPage.account;
        String currentUserGuid = getCurrentUser().getGuid();
        if (isBlank(user.getPassword())) {
            User old = userManager.loadUserByGuid(currentUserGuid);
            user.setPassword(old.getPassword());
        } else {
            user.setPassword(sha1Hash(user.getPassword()));
        }
        user = userManager.save(user);
        personaHelper.flushPersonaPageCache(getContext());
        displayNameCache.flush(new DisplayNameCacheKey(currentUserGuid, currentUserGuid));
        return new ForwardResolution("/account_settings.jsp");
    }

    public Resolution updateBilling() {
        showSaveMessage = true;
        settingsPage = SettingsPage.billing;
        return new ForwardResolution("/account_settings.jsp");
    }

    public Resolution updatePersona() {
        showSaveMessage = true;
        settingsPage = SettingsPage.persona;
        String currentUserGuid = getCurrentUser().getGuid();
        if (isBlank(user.getPassword())) {
            User old = userManager.loadUserByGuid(currentUserGuid);
            user.setPassword(old.getPassword());
        } else {
            user.setPassword(sha1Hash(user.getPassword()));
        }
        user.getProfile().getLocation().setGeoLocation(
                new GeoUtil().fetchGeoInfoForAddress(user.getProfile().getLocation())
        );
        user = userManager.save(user);
        personaHelper.flushPersonaPageCache(getContext());
        displayNameCache.flush(new DisplayNameCacheKey(currentUserGuid, currentUserGuid));
        return new ForwardResolution("/account_settings.jsp");
    }

    public Resolution updatePrivacy() throws IOException {
        showSaveMessage = true;
        settingsPage = SettingsPage.privacy;
        String currentUserGuid = getCurrentUser().getGuid();
        if (isBlank(user.getPassword())) {
            User old = userManager.loadUserByGuid(currentUserGuid);
            user.setPassword(old.getPassword());
        } else {
            user.setPassword(sha1Hash(user.getPassword()));
        }
        user = userManager.save(user);
        return new ForwardResolution("/account_settings.jsp");
    }

    public Resolution updateNotifications() throws IOException {
        showSaveMessage = true;
        settingsPage = SettingsPage.notifications;
        String currentUserGuid = getCurrentUser().getGuid();
        if (isBlank(user.getPassword())) {
            User old = userManager.loadUserByGuid(currentUserGuid);
            user.setPassword(old.getPassword());
        } else {
            user.setPassword(sha1Hash(user.getPassword()));
        }
        user = userManager.save(user);
        return new ForwardResolution("/account_settings.jsp");
    }

    public Resolution updateIntegrations() throws IOException {
        logger.info("update integrations");

        // @todo Implement account settings integration update

        personaHelper.flushPersonaPageCache(getContext());
        return new RedirectResolution(PersonaActionBean.class);
    }

    public Resolution addTwitterCredentials() {
        ExternalCredentials cred = ExternalCredentials.buildExternalCredentials(TWITTER,
                getCurrentUser().toEntityReference(),
                credentialUsername, credentialPassword);
        userManager.saveExternalCredential(cred);

        return createResolutionWithText(getContext(), "success");
    }

    public Resolution hasTwitterCredentials() {
        ExternalCredentials cred = userManager.loadExternalCredentialsForUserAndCredType(
                getCurrentUser().getGuid(), TWITTER);
        return createResolutionWithText(getContext(), String.valueOf(cred != null));
    }

    @Override
    public MenuLocation getMenuLocation() {
        return MenuLocation.settings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public List<ExternalCredentials> getExternalCredentials() {
        return externalCredentials;
    }

    public String getCredentialUsername() {
        return credentialUsername;
    }

    public void setCredentialUsername(String credentialUsername) {
        this.credentialUsername = credentialUsername;
    }

    public String getCredentialPassword() {
        return credentialPassword;
    }

    public void setCredentialPassword(String credentialPassword) {
        this.credentialPassword = credentialPassword;
    }

    public SettingsPage getSettingsPage() {
        return settingsPage;
    }

    public void setSettingsPage(SettingsPage settingsPage) {
        this.settingsPage = settingsPage;
    }

    public boolean isShowSaveMessage() {
        return showSaveMessage;
    }
}
