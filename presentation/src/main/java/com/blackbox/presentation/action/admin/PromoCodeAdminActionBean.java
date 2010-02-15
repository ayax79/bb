package com.blackbox.presentation.action.admin;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.RequireUserType;
import com.blackbox.presentation.action.util.EighteenOrOlderTypeConverter;
import com.blackbox.user.*;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.*;

import java.util.Collection;
import java.util.ArrayList;

import org.joda.time.DateTime;

@RequireUserType({User.UserType.BLACKBOX_ADMIN, User.UserType.BLACKBOX_MARKETING})
public class PromoCodeAdminActionBean extends BaseBlackBoxActionBean {

    @SpringBean("userManager")
    IUserManager userManager;

    @Validate(required = true)
    private String username;

    @Validate(required = true, on = "singleUse", converter = EmailTypeConverter.class)
    private String email;

    @Validate(required = true, on = "multiUse")
    private String promoCampaignName;

    @Validate(required = true, on = "multiUse")
    private DateTime expirationDate;

    private User user;
    private SingleUsePromoCode promoCode;
    private MultiUserPromoCode promoCodeMulti;
    private Integer evaluationPeriod;
    private String adminUserType;
    private Collection<String> userLevels = new ArrayList<String>();
    private User.UserType userType;


    @ValidationMethod
    public void validate(ValidationErrors errors) {
        user = userManager.loadUserByUsername(username);
        if (user == null) {
            errors.add("username", new SimpleError(String.format("User %s does not exist", username)));
        }
    }

    @DontValidate
    @DefaultHandler
    public Resolution begin() {
        adminUserType = getCurrentUser().getType().toString();
        if ("BLACKBOX_ADMIN".equals(adminUserType)) {
            userLevels.add("LIMITED");
            userLevels.add("NORMAL");
            userLevels.add("PROMOTER");
            userLevels.add("VENDOR");
            userLevels.add("AFFILIATE");

        } else if ("BLACKBOX_MARKETING".equals(adminUserType)) {
            userLevels.add("LIMITED");
            userLevels.add("NORMAL");
            userLevels.add("PROMOTER");
            userLevels.add("AFFILIATE");
        }

        return new ForwardResolution("/WEB-INF/admin/promocode.jsp");
    }

    public Resolution list() {
        return new ForwardResolution("/WEB-INF/admin/promocode.jsp");
    }

    public Resolution edit() {
        return new ForwardResolution("/WEB-INF/admin/promocode.jsp");
    }

    public Resolution singleUse() {
        promoCode = new SingleUsePromoCode();
        promoCode.setUserType(userType);
        promoCode.setMaster(user);
        promoCode.setEmail(email);

        promoCode = userManager.createSingleUsePromoCode(promoCode);
        return new ForwardResolution("/WEB-INF/admin/promocode.jsp");
    }

    public Resolution multiUse() {
        promoCodeMulti = new MultiUserPromoCode();
        promoCodeMulti.setUserType(userType);
        promoCodeMulti.setMaster(user);
        promoCodeMulti.setPromoCampaignName(promoCampaignName);
        promoCodeMulti.setEvaluationPeriod(evaluationPeriod);
        promoCodeMulti.setExpirationDate(expirationDate);

        promoCodeMulti = userManager.createMultiUserPromoCode(promoCodeMulti);
        return new ForwardResolution("/WEB-INF/admin/promocode.jsp");
    }

    public BasePromoCode getPromoCode() {
        return promoCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEvaluationPeriod() {
        return evaluationPeriod;
    }

    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAdminUserType() {
        return adminUserType;
    }

    public void setAdminUserType(String adminUserType) {
        this.adminUserType = adminUserType;
    }

    public Collection<String> getUserLevels() {
        return userLevels;
    }

    public void setUserLevels(Collection<String> userLevels) {
        this.userLevels = userLevels;
    }

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }

    public void setEvaluationPeriod(Integer evaluationPeriod) {
        this.evaluationPeriod = evaluationPeriod;
    }

    public IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPromoCampaignName() {
        return promoCampaignName;
    }

    public void setPromoCampaignName(String promoCampaignName) {
        this.promoCampaignName = promoCampaignName;
    }

    public MultiUserPromoCode getPromoCodeMulti() {
        return promoCodeMulti;
    }

    public void setPromoCodeMulti(MultiUserPromoCode promoCodeMulti) {
        this.promoCodeMulti = promoCodeMulti;
    }
}
