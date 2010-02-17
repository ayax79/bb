/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.social;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.terracotta.modules.annotations.InstrumentedClass;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XStreamAlias("category")
@XmlRootElement(name = "category")
@InstrumentedClass
public class Category implements Serializable {
    private String name;

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        if (name != null ? !name.equals(category.name) : category.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public static Category createCategory() {
        return new Category();
    }

    public static Category createCategory(String name) {
        Category tag = createCategory();
        tag.setName(name);
        return tag;
    }
}
