package com.blackbox.common;

import com.blackbox.EntityReference;
import com.blackbox.activity.IActivityThread;
import com.blackbox.util.Affirm;
import com.google.common.base.Predicate;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author colin@blackboxrepublic.com
 */
public final class ActivityThreadsMessageOwnerPredicate implements Predicate<IActivityThread> {

    private Collection<EntityReference> userEntityReferences;

    public ActivityThreadsMessageOwnerPredicate(EntityReference userEntityReferences) {
        this(newArrayList(Affirm.isNotNull(userEntityReferences, "userEntityReferences")));
    }

    public ActivityThreadsMessageOwnerPredicate(Collection<EntityReference> userEntityReferences) {
        this.userEntityReferences = Affirm.isNotNull(userEntityReferences, "userEntityReferences");
    }

    @Override
    public boolean apply(IActivityThread input) {
        return userEntityReferences.contains(input.getParent().getSender());
    }

}
