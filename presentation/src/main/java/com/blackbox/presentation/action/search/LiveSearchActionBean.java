package com.blackbox.presentation.action.search;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.foundation.social.RelationshipNetwork;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.NameInfo;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.yestech.lib.util.Pair;

import java.util.Collection;

/**
 *
 *
 */
public class LiveSearchActionBean extends BaseBlackBoxActionBean {

    private Collection<User> users;
    private String term;

    @SpringBean("userManager")
    IUserManager userManager;

    public Resolution search() {

        RelationshipNetwork network = getContext().getNetwork();
        if (term != null) {
            users = Lists.transform(network.find(term), new Function<Pair<String, NameInfo>, User>() {

                @Override
                public User apply(Pair<String, NameInfo> from) {
                    return userManager.loadUserByGuid(from.getFirst());
                }
            });
        }
        return new ForwardResolution("/ajax/live-search.jspf");
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
