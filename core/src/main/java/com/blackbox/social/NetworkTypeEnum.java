/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.social;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XmlRootElement
public enum NetworkTypeEnum {
    // don't change the order it will break things!
    SUPER_DIRECT(-2, 0), // Under inbox, love the retarded name
    SELF(-1, 1),
    DIRECT(0, 2),
    FRIENDS(1, 3),
    FRIENDS_OF_FRIENDS(2, 4),
    ALL_MEMBERS(Integer.MAX_VALUE - 100, 5),
    WORLD(Integer.MAX_VALUE, 6),
    FOLLOWING(1, 7),
    FOLLOWERS(2, 8);

    private int depth;
    private int databaseIdentifier;

    NetworkTypeEnum(int depth, int databaseIdentifier) {
        this.depth = depth;
        this.databaseIdentifier = databaseIdentifier;
    }

    public int getDepth() {
        return depth;
    }

    public int getDatabaseIdentifier() {
        return databaseIdentifier;
    }

    public static NetworkTypeEnum getNetworkType(int depth) {
        NetworkTypeEnum type = null;
        for (NetworkTypeEnum networkTypeEnum : values()) {
            if (networkTypeEnum.getDepth() == depth) {
                type = networkTypeEnum;
                break;
            }
        }
        return type;
    }

    public static NetworkTypeEnum getByDatabaseIdentifier(int dbIdentifier) {
        NetworkTypeEnum type = null;
        for (NetworkTypeEnum networkTypeEnum : values()) {
            if (networkTypeEnum.getDatabaseIdentifier() == dbIdentifier) {
                type = networkTypeEnum;
                break;
            }
        }
        return type;
    }
}
