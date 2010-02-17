package com.blackbox.common;

import com.blackbox.EntityReference;
import com.blackbox.activity.IActivity;
import com.blackbox.util.Affirm;
import com.google.common.base.Predicate;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author colin@blackboxrepublic.com
 */
public final class ActivitySenderOwnerPredicate implements Predicate<IActivity> {

    private Collection<EntityReference> userEntityReferences;

    public ActivitySenderOwnerPredicate(EntityReference userEntityReferences) {
        this(newArrayList(Affirm.isNotNull(userEntityReferences, "userEntityReferences")));
    }

    public ActivitySenderOwnerPredicate(Collection<EntityReference> userEntityReferences) {
        this.userEntityReferences = Affirm.isNotNull(userEntityReferences, "userEntityReferences");
    }

    @Override
    public boolean apply(IActivity input) {
        return userEntityReferences.contains(input.getSender());
    }
}