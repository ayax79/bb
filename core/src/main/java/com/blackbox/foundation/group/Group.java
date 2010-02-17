package com.blackbox.foundation.group;

import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.EntityTypeAnnotation;
import com.blackbox.foundation.Utils;

import static com.blackbox.foundation.EntityType.GROUP;

/**
 *
 *
 */
@EntityTypeAnnotation(GROUP)
public class Group extends BaseEntity {

    private String description;

    public Group() {
        super(EntityType.GROUP);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Group{" +
                "description='" + description + '\'' +
                "} " + super.toString();
    }

    public static Group createGroup() {
        Group g = new Group();
        Utils.applyGuid(g);
        return g;
    }
}
