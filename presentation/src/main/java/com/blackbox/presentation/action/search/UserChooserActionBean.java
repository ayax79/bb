package com.blackbox.presentation.action.search;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import static com.blackbox.presentation.action.util.JspFunctions.displayName;

import com.blackbox.foundation.social.RelationshipNetwork;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.user.IUserManager;
import com.google.common.base.Function;
import static com.google.common.collect.Collections2.transform;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.*;

public class UserChooserActionBean extends BaseBlackBoxActionBean {

    private User user = getCurrentUser();
    private Set<User> users = new TreeSet<User>(DISPLAY_NAME_COMPARATOR);

    @SpringBean("userManager")
    IUserManager userManager;


    private final Function<String, User> USER_FUNCTION = new Function<String, User>() {
        @Override
        public User apply(String guid ) {
            return userManager.loadUserByGuid(guid);
        }
    };

    private static final Comparator<User> DISPLAY_NAME_COMPARATOR = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            return displayName(o1).compareTo(displayName(o2));
        }
    };


    @DefaultHandler
    public Resolution load() {

        addUsers();
		populateFollowers(users);

        return new ForwardResolution("/ajax/user-chooser.jspf");
    }

    public Resolution all() {

        addUsers();
        populateFollowers(users);

        return new ForwardResolution("/ajax/user-chooser-list.jspf");
    }

    private void addUsers() {
        final RelationshipNetwork network = getContext().getNetwork();
        Collection<String> references = new ArrayList<String>(network.getRelationshipMap().keySet());
        if (!references.isEmpty()) {
            users.addAll(transform(references, USER_FUNCTION));
        }
    }

    public Resolution friends() {

        Collection<String> relationships = getContext().getNetwork().getFriends();
        users.addAll(transform(relationships, USER_FUNCTION));

        return new ForwardResolution("/ajax/user-chooser-list.jspf");
    }

    public Resolution following() {
        Collection<String> relationships = getContext().getNetwork().getFollowing();
        if (relationships != null && !relationships.isEmpty()) {
            users.addAll(transform(relationships, USER_FUNCTION));
        }

        return new ForwardResolution("/ajax/user-chooser-list.jspf");
    }

    public Resolution followers() {
        populateFollowers(users);
        return new ForwardResolution("/ajax/user-chooser-list.jspf");
    }

    protected void populateFollowers(Set<User> users) {
        
    }

    public User getUser() {
        return user;
    }

    public Collection<User> getUsers() {
        return users;
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}
