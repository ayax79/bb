/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.presentation.extension;

import com.blackbox.EntityReference;
import com.blackbox.social.Relationship;
import com.blackbox.social.RelationshipNetwork;
import com.blackbox.user.User;
import org.terracotta.modules.annotations.InstrumentedClass;

import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@SuppressWarnings({"unchecked"})
@InstrumentedClass
public class DefaultBlackBoxContext extends BlackBoxContext {
    //TODO:  remove from storing in session

    public static final String USER_KEY = "__user__";
    public static final String NETWORK_KEY = "__network__";
    public static final String BLOCKS_KEY = "__blocks__";
    public static final String REVERSE_RELATIONSHIP_KEY = "__reverse_relationship_key__";

    public User getUser() {
        return (User) getRequest().getSession().getAttribute(USER_KEY);
    }

    public void setUser(User user) {
        getRequest().getSession().setAttribute(USER_KEY, user);
    }

    public RelationshipNetwork getNetwork() {
        return (RelationshipNetwork) getRequest().getSession().getAttribute(NETWORK_KEY);
    }

    public void setNetwork(RelationshipNetwork network) {
        getRequest().getSession().setAttribute(NETWORK_KEY, network);
    }

    @Override
    public List<EntityReference> getBlocked() {
        return (List<EntityReference>) getRequest().getSession().getAttribute(BLOCKS_KEY);
    }

    @Override
    public void setBlocked(List<EntityReference> blocks) {
        getRequest().getSession().setAttribute(BLOCKS_KEY, blocks);
    }

    @Override
    public List<Relationship> getReverseRelationships() {
        return (List<Relationship>) getRequest().getSession().getAttribute(REVERSE_RELATIONSHIP_KEY);
    }

    @Override
    public void setReverseRelationships(List<Relationship> relationships) {
        getRequest().getSession().setAttribute(REVERSE_RELATIONSHIP_KEY, relationships);
    }

}