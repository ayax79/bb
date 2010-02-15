/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.social;

import com.blackbox.Utils;
import static com.google.common.collect.Lists.newArrayList;
import org.yestech.lib.xml.XmlUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Container for a Collection of {@link com.blackbox.social.Category} used primarily for transports
 *
 * @author Artie Copeland
 * @version $Revision: $
 */
@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.NONE)
public class Categories implements Serializable {

    @XmlElement(name = "category")
    private List<Category> categoryCollection = newArrayList();

    public Categories() {
    }

    public List<Category> getCategoryCollection() {
        return categoryCollection;
    }

    public void setCategoryCollection(List<Category> categoryCollection) {
        this.categoryCollection = categoryCollection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categories)) return false;

        Categories that = (Categories) o;

        if (categoryCollection != null ? !categoryCollection.equals(that.categoryCollection) : that.categoryCollection != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return categoryCollection != null ? categoryCollection.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categoryCollection=" + categoryCollection +
                '}';
    }

    /**
     * Used to create a Categories from an xml
     *
     * @param xml
     * @return
     */
    public static Categories valueOf(String xml) {
        return XmlUtils.fromXml(xml, Utils.xmlAliases());
    }
}
