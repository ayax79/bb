/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.activity;


import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.util.AnyTypeAdapter;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Collection;
import java.util.SortedSet;

/**
 * @author Artie Copeland
 */
@XmlJavaTypeAdapter(AnyTypeAdapter.class)
@XmlSeeAlso({ActivityThread.class})
public interface IActivityThread<E extends IActivity> extends Serializable, Comparable<ActivityThread<E>> {

    Collection<E> getChildren();

    void setChildren(Collection<E> children);

    E getParent();

    void setParent(E parent);

    void addChild(E child);

    E getLastChild();

    void setLastChild(E lastChild);

    Collection<NetworkTypeEnum> getNetworkTypes();

    void setNetworkTypes(Collection<NetworkTypeEnum> networkTypes);

    /**
     * Returns the flattened version of the entire thread sorted by postDate descending.
     *
     * @return the flattened version of the entire thread sorted by postDate descending.
     */
    SortedSet<E> flatten();

}
