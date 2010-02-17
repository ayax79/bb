package com.blackbox.presentation.action.persona;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.MiniProfile;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.foundation.user.User;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

public class MiniPersonaActionBean extends BaseBlackBoxActionBean {

    private MiniProfile profile;
    private String identifier;
    private User user;
    @SpringBean("userManager")
    private IUserManager userManager;
    @SpringBean("socialManager")
    private ISocialManager socialManager;

	@Before
    public void preProcess() {
        if (isBlank(identifier)) {
            identifier = getCurrentUser().getUsername();
        }

        user = userManager.loadUserByUsername(identifier);
        if (user == null) user = userManager.loadUserByGuid(identifier);

        if (user == null) {
            String msg = String.format("User passed in %s does not exist", identifier);
            throw new IllegalArgumentException(msg);
        }
    }

    @DefaultHandler
    @DontValidate
    public Resolution begin() {

        profile = userManager.loadMiniProfile(identifier);
        return new ForwardResolution("/ajax/miniProfile.jsp");
    }
	
	public boolean getBlocksCurrentUser() {
		List<EntityReference> blocks = getSocialManager().loadBlocks(user.getGuid());
		return blocks.contains(getCurrentUser().toEntityReference());
	}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public MiniProfile getProfile() {
        return profile;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ISocialManager getSocialManager() {
		return socialManager;
	}

	public void setSocialManager(ISocialManager socialManager) {
		this.socialManager = socialManager;
	}
}
