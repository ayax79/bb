/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blackbox.presentation.action.user;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.PresentationUtil;
import com.blackbox.user.IUserManager;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Ida
 */
@SuppressWarnings({"UnusedDeclaration"})
@UrlBinding("/action/ajax/useravailable")
public class UserAvailableActionBean extends BaseBlackBoxActionBean {

    private static final Logger logger = LoggerFactory.getLogger(UserAvailableActionBean.class);
    @Validate(field = "username", required = true, mask = "\\w+", maxlength = 25, on = "step2", minlength = 1)
    private String token;

    @SpringBean("userManager")
    private IUserManager userManger;

    public Resolution availableByUsername() throws IOException, JSONException {
        boolean available = userManger.isUsernameAvailable(token);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("success", available);
        return PresentationUtil.createResolutionWithJson(getContext(), jsonObj);
    }

    public Resolution availableByEmail() throws IOException, JSONException {
        boolean available = userManger.isEmailAvailable(token);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("success", available);
        return PresentationUtil.createResolutionWithJson(getContext(), jsonObj);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public IUserManager getUserManger() {
        return userManger;
    }

    public void setUserManger(IUserManager userManger) {
        this.userManger = userManger;
    }
}
