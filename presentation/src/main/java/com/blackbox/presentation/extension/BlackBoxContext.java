/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.presentation.extension;

import net.sourceforge.stripes.action.ActionBeanContext;
import com.blackbox.user.IUser;
import com.blackbox.user.Profile;
import com.blackbox.user.User;
import com.blackbox.social.RelationshipNetwork;
import com.blackbox.social.Ignore;
import com.blackbox.social.Vouch;
import com.blackbox.social.Relationship;
import com.blackbox.EntityReference;
import com.blackbox.bookmark.Bookmark;
import org.terracotta.modules.annotations.Root;
import org.terracotta.modules.annotations.InstrumentedClass;

import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@InstrumentedClass
public abstract class BlackBoxContext extends ActionBeanContext {
    public abstract User getUser();

    public abstract void setUser(User user);

    public abstract RelationshipNetwork getNetwork();

    public abstract void setNetwork(RelationshipNetwork network);

    public abstract List<EntityReference> getBlocked();
    
    public abstract void setBlocked(List<EntityReference> blocks);

    public abstract List<Relationship> getReverseRelationships();

    public abstract void setReverseRelationships(List<Relationship> relationships);

}