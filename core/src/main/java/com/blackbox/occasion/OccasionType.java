package com.blackbox.occasion;

/**
 *
 *
 */
public enum OccasionType {
    OPEN(0),
    INVITE_ONLY(1),
    PRIVATE(2);

    private int databaseIdentifier;

    OccasionType(int databaseIdentifier) {
        this.databaseIdentifier = databaseIdentifier;
    }

    public int getDatabaseIdentifier() {
        return databaseIdentifier;
    }

    public static OccasionType getByDatabaseIdentifier(int dbIdentifier) {
        OccasionType type = null;
        for (OccasionType OccasionType : values()) {
            if (OccasionType.getDatabaseIdentifier() == dbIdentifier) {
                type = OccasionType;
                break;
            }
        }
        return type;
    }
}
