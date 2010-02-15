/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.persona;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import static com.blackbox.presentation.action.BaseBlackBoxActionBean.ViewType.json;
import static com.blackbox.presentation.action.util.JSONUtil.toJSON;
import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithJson;
import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithText;
import com.blackbox.presentation.extension.JodaShortDateTimeConverter;
import com.blackbox.user.*;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PSProfileActionBean extends BaseBlackBoxActionBean {
    final private static Logger _logger = LoggerFactory.getLogger(PSProfileActionBean.class);
    @SpringBean("userManager")
    private IUserManager userManager;

    @ValidateNestedProperties(
            @Validate(field = "birthday", converter = JodaShortDateTimeConverter.class)
    )
    private Profile profile;
    private User currentUser;

    @Before
    public void grabCurrentProfile() {

        currentUser = getCurrentUser();
        //current = getCurrentProfile();
        // loadProfileByUserGuid is not working current = userManager.loadProfileByUserGuid(user.getGuid());
    }

    @DefaultHandler
    @DontValidate
    public Resolution begin() throws JSONException {
        profile = getCurrentProfile();

        if (getView() == json) {
            return createResolutionWithJson(getContext(), toJSON(profile));
        }
        
        return new ForwardResolution("/account_settings.jsp");
    }

    /*public Resolution store() {

        //profile.setUser(getCurrentUser()); // it cause cycle problem

        if (current != null) {
            profile.setGuid(current.getGuid());
            profile.setVersion(current.getVersion());
            profile.setCreated(current.getCreated());
        }
        Profile currentProfile = userManager.save(profile);
        getContext().getUser().setProfile(currentProfile);

        getContext().getMessages().add(new LocalizableMessage("profile.saved.message"));
        if (getView() == json) {
            return createResolutionWithText(getContext(), "success");
        }

        return new RedirectResolution(getClass()); // redirect back to this page
    }
     */


    @HandlesEvent("profile.location.city")
    public Resolution city() {
        String city = profile.getLocation().getCity();
        User user = userManager.loadUserByGuid(getCurrentUser().getGuid());
        user.getProfile().getLocation().setCity(city);
        userManager.save(user);
        return createResolutionWithText(getContext(), "success");
    }


    @HandlesEvent("profile.location.currentAddress.city")
    public Resolution currentCity() {
        String currentCity = profile.getCurrentAddress().getCity();

        User user = userManager.loadUserByGuid(getCurrentUser().getGuid());
        user.getProfile().getCurrentAddress().setCity(currentCity);
        userManager.save(user);
        return createResolutionWithText(getContext(), "success");
    }

    /* @HandlesEvent("profile.location.zipCode")
    public Resolution zipCode() {
        String zipCode = profile.getLocation().getZipCode();
        profile = current;
        profile.getLocation().setZipCode(zipCode);
        setView(json);
        return store();
    }
    @HandlesEvent("profile.birthday")
    public Resolution birthday() {
        DateTime birthday = profile.getBirthday();
        profile = current;
        profile.setBirthday(birthday);
        setView(json);
        return store();
    }
    @HandlesEvent("profile.politicalViews")
    public Resolution politicalViews() {
        String politicalViews = profile.getPoliticalViews();
        profile = current;
        profile.setPoliticalViews(politicalViews);
        setView(json);
        return store();
    }

    @HandlesEvent("profile.religiousViews")
    public Resolution religiousViews() {
        String religiousViews = profile.getReligiousViews();
        profile = current;
        profile.setReligiousViews(religiousViews);
        setView(json);
        return store();
    }

    @HandlesEvent("profile.website")
    public Resolution website() {
        String website = profile.getWebsite();
        profile = current;
        profile.setWebsite(website);
        setView(json);
        return store();
    }

    @HandlesEvent("profile.bodyMods")
    public Resolution bodyMods() {
        String bodyMods = profile.getBodyMods();
        profile = current;
        profile.setBodyMods(bodyMods);
        setView(json);
        return store();
    }

    @HandlesEvent("profile.mostly")
    public Resolution mostly() {
        String mostly = profile.getMostly();
        profile = current;
        profile.setMostly(mostly);
        setView(json);
        return store();
    }

    @HandlesEvent("profile.sex")
    public Resolution sex() {
        SexEnum sex = profile.getSex();
        profile = current;
        _logger.info("sex is:" + sex + "; profile is:" + profile);
        profile.setSex(sex);
        setView(json);
        return store();
    }

    @HandlesEvent("profile.acceptsGifts")
    public Resolution acceptsGifts() {
        boolean acceptsGifs = profile.isAcceptsGifts();
        profile = current;
        profile.setAcceptsGifts(acceptsGifs);
        setView(json);
        return store();
    }

    @HandlesEvent("profile.lookingFor.dates")
    public Resolution lookingForDates() {
        LookingFor lookingFor = profile.getLookingFor();
        profile = current;
        profile.getLookingFor().setDates(lookingFor.isDates());
        setView(json);
        return store();
    }

    @HandlesEvent("profile.lookingFor.donkeySex")
    public Resolution lookingForDonkeySex() {
        LookingFor lookingFor = profile.getLookingFor();
        profile = current;
        profile.getLookingFor().setDonkeySex(lookingFor.isDonkeySex());
        setView(json);
        return store();
    }

    @HandlesEvent("profile.lookingFor.friends")
    public Resolution lookingForFriends() {
        LookingFor lookingFor = profile.getLookingFor();
        profile = current;
        profile.getLookingFor().setFriends(lookingFor.isFriends());
        setView(json);
        return store();
    }

    @HandlesEvent("profile.lookingFor.hookup")
    public Resolution lookingForHookup() {
        LookingFor lookingFor = profile.getLookingFor();
        profile = current;
        profile.getLookingFor().setHookup(lookingFor.isHookup());
        setView(json);
        return store();
    }

    @HandlesEvent("profile.lookingFor.love")
    public Resolution lookingForLove() {
        LookingFor lookingFor = profile.getLookingFor();
        profile = current;
        profile.getLookingFor().setLove(lookingFor.isLove());
        setView(json);
        return store();
    }

    @HandlesEvent("profile.lookingFor.snuggling")
    public Resolution lookingForSnuggling() {
        LookingFor lookingFor = profile.getLookingFor();
        profile = current;
        profile.getLookingFor().setSnuggling(lookingFor.isSnuggling());
        setView(json);
        return store();
    }
     */

    @Override
    public MenuLocation getMenuLocation() {
        return MenuLocation.persona;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
