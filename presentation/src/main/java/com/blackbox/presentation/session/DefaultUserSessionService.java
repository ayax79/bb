package com.blackbox.presentation.session;

import com.blackbox.EntityReference;
import com.blackbox.bookmark.IBookmarkManager;
import com.blackbox.presentation.extension.BlackBoxContext;
import com.blackbox.social.ISocialManager;
import com.blackbox.social.RelationshipNetwork;
import com.blackbox.social.Relationship;
import com.blackbox.user.User;
import com.blackbox.user.IUserManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author A.J. Wright
 */
@Service("userSessionService")
public class DefaultUserSessionService implements UserSessionService {

    @Resource
    private ISocialManager socialManager;

    @Resource
    private IUserManager userManager;

    @Resource
    private IBookmarkManager bookmarkManager;

    public void addOnlineUser(String userGuid) {
        userManager.addOnlineUser(userGuid);
    }

    public void removeOnlineUser(String guid) {
        userManager.removeOnlineUser(guid);
    }

    public int getOnlineUserCount() {
        return userManager.getOnlineUserCount();
    }

    public boolean isUserOnline(String guid) {
        return userManager.isUserOnline(guid).isValue();
    }

    public void populateContext(User user, BlackBoxContext context) {
        context.setUser(user);

        RelationshipNetwork network = socialManager.loadRelationshipNetwork(user.getGuid());
        context.setNetwork(network);
        
        List<EntityReference> list = socialManager.loadBlocks(user.getGuid());
        if (list == null) {
            list = new ArrayList<EntityReference>();
        }
        context.setBlocked(list);


        List<Relationship> relationships = socialManager.loadReverseRelationships(user.getGuid());
        if (relationships == null) {
            relationships = new ArrayList<Relationship>();
        }
        context.setReverseRelationships(relationships);
    }

    public void rebuildContextIfNeeded(BlackBoxContext context) {
        User user = context.getUser();
        //TODO need to fix....
        if (user != null && userManager.isSessionRefreshNeeded(user.getGuid()).isValue()) {
            user = userManager.loadSessionCacheUserByGuid(context.getUser().getGuid());
            populateContext(user, context);
        }
    }

    public void setSocialManager(ISocialManager socialManager) {
        this.socialManager = socialManager;
    }

    public void setBookmarkManager(IBookmarkManager bookmarkManager) {
        this.bookmarkManager = bookmarkManager;
    }
}
