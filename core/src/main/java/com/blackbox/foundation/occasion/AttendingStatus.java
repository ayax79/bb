package com.blackbox.foundation.occasion;

/**
 *
 *
 */
public enum AttendingStatus {
    ATTENDING(0),
    NOT_ATTENDING(1),
    TENATIVE(2),
    NOT_RESPONDED(3),
    NOT_INVITED(4),
    REQUESTED_INVITATION(5),
    REJECTED_INVITATION_REQUEST(6);

    private int databaseIdentifier;

    AttendingStatus(int databaseIdentifier) {
        this.databaseIdentifier = databaseIdentifier;
    }

    public int getDatabaseIdentifier() {
        return databaseIdentifier;
    }
}
