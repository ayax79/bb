/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox;

import com.blackbox.commerce.CartItem;
import com.blackbox.commerce.Merchant;
import com.blackbox.commerce.Product;
import com.blackbox.commerce.ShoppingCart;
import com.blackbox.group.Group;
import com.blackbox.media.MediaLibrary;
import com.blackbox.media.MediaMetaData;
import com.blackbox.message.Message;
import com.blackbox.occasion.Occasion;
import com.blackbox.occasion.Promoter;
import com.blackbox.user.User;
import org.terracotta.modules.annotations.InstrumentedClass;
import org.yestech.publish.objectmodel.IArtifactOwnerType;

/**
 * Represents all the Types of Entities in Blackbox.  Everything in Blackbox is an Entity that
 * can have items linked in a relationship.
 */
@InstrumentedClass
public enum EntityType implements IArtifactOwnerType {
    /*
    NOTE:  Must add add the end as order matters for persistence!!!!!
     */
    USER(User.class, 0),
    OCCASION(Occasion.class, 1),
    GROUP(Group.class, 2),
    PROMOTER(Promoter.class, 3),
    MERCHANT(Merchant.class, 4),
    CARTITEM(CartItem.class, 5),
    SHOPPINGCART(ShoppingCart.class, 6),
    PRODUCT(Product.class, 7),
    MEDIA_LIBRARY(MediaLibrary.class, 8),
    MESSAGE(Message.class, 9),
    MEDIA(MediaMetaData.class, 10);

    private Class<? extends IEntity> clazz;
    private int databaseIdentifier;

    EntityType(Class<? extends IEntity> clazz, int databaseIdentifier) {
        this.clazz = clazz;
        this.databaseIdentifier = databaseIdentifier;
    }

    public Class<? extends IEntity> getClazz() {
        return clazz;
    }

    public int getDatabaseIdentifier() {
        return databaseIdentifier;
    }

    public static EntityType valueOf(Class<? extends IEntity> clazz) {

        for (EntityType type : values()) {
            if (type.getClazz().isAssignableFrom(clazz)) {
                return type;
            }
        }
        return null;
    }

    public static EntityType getByDatabaseIdentifier(int dbIdentifier) {
        EntityType type = null;
        for (EntityType entityTypeEnum : values()) {
            if (entityTypeEnum.getDatabaseIdentifier() == dbIdentifier) {
                type = entityTypeEnum;
                break;
            }
        }
        return type;
    }

}
