/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.presentation.action.persona;

import com.blackbox.foundation.social.Vouch;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.ArrayList;
import java.util.List;

@UrlBinding("/persona/vouch/{$event}/{$username}")
public class PSVouchActionBean extends BasePersonaActionBean {

    @SpringBean("userManager") IUserManager userManager;

    private List<Vouch> vouchList;
    private List<User> results;
    private String username;
    
    public Resolution load() {
        User user = userManager.loadUserByUsername(username); 

        vouchList=getSocialManager().loadVouchByTarget(user.getGuid());
        results = new ArrayList<User>();
        
        return new ForwardResolution("/ajax/vouch/vouch.jsp");
    }

    public List<Vouch> getVouchList() {
        return vouchList;
    }

    public void setVouchList(List<Vouch> vouchList) {
        this.vouchList = vouchList;
    }

    public List<User> getResults() {
        return results;
    }

    public void setResults(List<User> results) {
        this.results = results;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
