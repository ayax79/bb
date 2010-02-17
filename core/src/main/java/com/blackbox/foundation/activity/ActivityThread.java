package com.blackbox.foundation.activity;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.util.Affirm;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.*;

import static com.blackbox.foundation.activity.AscendingActivityAndGuidComparator.getAscendingActivityComparator;
import static com.google.common.collect.Sets.newTreeSet;

/**
 * A simple object used for giving structure to a thread.
 *
 * @param <E> The type of IActivity to make the instance for.
 */
@XmlRootElement(name = "thread")
@XmlSeeAlso({MediaMetaData.class, Message.class, Occasion.class})
public class ActivityThread<E extends IActivity> implements IActivityThread<E> {

    private static final long serialVersionUID = 284752586391093207L;
    private E parent;
    private Collection<E> children;
    private E lastChild;
    private Collection<NetworkTypeEnum> networkTypes;

    public ActivityThread(E parent) {
        this();
        this.parent = parent;
    }

    public ActivityThread(E parent, Collection<E> children) {
        this(parent);
        this.children = Affirm.isNotNull(children, "children", IllegalArgumentException.class);
    }

    public ActivityThread() {
        this.children = newTreeSet(getAscendingActivityComparator());
    }

    public E getLastChild() {
        return lastChild;
    }

    public void setLastChild(E lastChild) {
        this.lastChild = lastChild;
    }

    @Override
    public Collection<NetworkTypeEnum> getNetworkTypes() {
        return networkTypes;
    }

    @Override
    public void setNetworkTypes(Collection<NetworkTypeEnum> networkTypes) {
        this.networkTypes = Collections.unmodifiableCollection(Affirm.isNotNull(networkTypes, "networkTypes", IllegalArgumentException.class));
    }

    public void addChild(E child) {
        children.add(child);
        lastChild = child;
    }

    @XmlElementWrapper(name = "children")
    @Override
    public Collection<E> getChildren() {
        return children;
    }

    @Override
    public void setChildren(Collection<E> children) {
        this.children = children;
    }

    @Override
    public E getParent() {
        return parent;
    }

    @Override
    public void setParent(E parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityThread that = (ActivityThread) o;

        if (children != null ? !children.equals(that.children) : that.children != null) return false;
        //noinspection RedundantIfStatement
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ActivityThread{" +
                "parent=" + parent +
                ", children=" + children +
                '}';
    }

    @Override
    public int compareTo(ActivityThread<E> other) {
        E otherParent = other.getParent();
        if (otherParent != null) {
            DateTime otherPostDate = otherParent.getPostDate();
            if (otherPostDate != null) {
                E thisParent = getParent();
                if (thisParent != null) {
                    DateTime thisPostDate = thisParent.getPostDate();
                    int postDateComparison = otherPostDate.compareTo(thisPostDate);
                    if (postDateComparison != 0) {
                        return postDateComparison;
                    }
                    return compareChildrenSizes(other);
                }
                return 1; // if there is a date, but not one in the other
            }
        }
        return compareChildrenSizes(other);
    }

    private int compareChildrenSizes(ActivityThread<E> other) {
        return other.getChildren().size() - getChildren().size(); // the ActivityThread with more children goes first;
    }

    @Override
    public SortedSet<E> flatten() {
        TreeSet<E> ts = new TreeSet<E>(new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                BBPersistentObject po1 = (BBPersistentObject) o1;
                BBPersistentObject po2 = (BBPersistentObject) o2;

                DateTime d1 = o1.getPostDate() != null ? o1.getPostDate() : po1.getModified();
                DateTime d2 = o2.getPostDate() != null ? o2.getPostDate() : po2.getModified();

                return d2.compareTo(d1);
            }
        });
        ts.addAll(children);
        ts.add(parent);
        return ts;
    }

}
