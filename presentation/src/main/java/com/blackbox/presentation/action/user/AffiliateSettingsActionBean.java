/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.presentation.action.user;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * @author Ida
 */
@UrlBinding("/partner-settings")
public class AffiliateSettingsActionBean extends BaseBlackBoxActionBean {
    @DefaultHandler
    @DontValidate
    public Resolution begin() {
        //_logger.info("PartnerSettings    begin");
        //user = userManager.loadUserByGuid(getCurrentUser().getGuid());
        return new ForwardResolution("/affiliate.jsp");
    }
}