package com.blackbox.presentation.action.search;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.social.RelationshipNetwork;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;
import com.blackbox.util.NameInfo;
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
public class AutoCompleteActionBean extends BaseBlackBoxActionBean {

    @SpringBean("userManager")
    IUserManager userManager;

    private Collection<User> relationships;
    private String q; //renamed from term until I can modify plugin to send different key

    public Resolution search() {

        RelationshipNetwork network = getContext().getNetwork();
        if (q != null) {
            relationships = Lists.transform(network.find(q), new Function<Pair<String, NameInfo>, User>() {
                @Override
                public User apply(Pair<String, NameInfo> from) {
                    return userManager.loadUserByGuid(from.getFirst());
                }
            });
        }
        return new ForwardResolution("/ajax/auto-complete-results.jspf");
    }

    public Collection<User> getRelationships() {
        return relationships;
    }

    public void setQ(String q) {
        this.q = q;
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}
