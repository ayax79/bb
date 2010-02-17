package com.blackbox.server.message;

import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.activity.IActivityThread;
import org.joda.time.DateTime;

import java.util.Comparator;
import java.util.SortedSet;

/**
 * Returns which thread should come first.
 * <p/>
 * Expects that children are already sorted in ascending order.
 */
public class InboxComparator<T extends IActivity> implements Comparator<IActivityThread<T>> {

    private static final InboxComparator<IActivity> instance = new InboxComparator<IActivity>();

    @Override
    public int compare(IActivityThread<T> o1, IActivityThread<T> o2) {
        IActivity activity2 = lastActivity(o2);
        IActivity activity1 = lastActivity(o1);
        DateTime d1 = activity1.getPostDate() != null ? activity1.getPostDate() : activity1.getModified(); 
        DateTime d2 = activity2.getPostDate() != null ? activity2.getPostDate() : activity2.getModified();
        return d2.compareTo(d1);
    }

    protected IActivity lastActivity(IActivityThread<T> at) {
        SortedSet<T> set = at.flatten();
        return set.first();
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends IActivity> InboxComparator<T> getInboxComparator() {
        return (InboxComparator<T>) instance;
    }

}
