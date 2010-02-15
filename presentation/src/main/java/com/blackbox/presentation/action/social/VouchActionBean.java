package com.blackbox.presentation.action.social;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.JspFunctions;
import com.blackbox.presentation.action.util.PresentationUtil;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.io.StringReader;

/**
 * @author A.J. Wright
 */
public class VouchActionBean extends BaseBlackBoxActionBean {

    @SpringBean
    private IUserManager userManager;

    private String username;


    public Resolution isVouchedTest() {
        User user = userManager.loadUserByUsername(username);
        if (user != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("<html><body><h1>");
            boolean vouched = JspFunctions.isVouched(user.getGuid());
            builder.append(vouched);
            builder.append("</h1></body></html>");
            return new StreamingResolution("text/html", new StringReader(builder.toString()));
        }
        return null;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
