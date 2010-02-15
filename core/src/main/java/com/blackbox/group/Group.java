package com.blackbox.group;

import com.blackbox.BaseEntity;
import com.blackbox.EntityType;
import com.blackbox.EntityTypeAnnotation;
import com.blackbox.Utils;

import static com.blackbox.EntityType.GROUP;

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
