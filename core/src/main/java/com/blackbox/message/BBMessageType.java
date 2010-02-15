/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.message;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public enum BBMessageType {
    TEXT_COMMENT("txtComment", "blackbox"), ACTIVITY("activity", "blackbox"),
    VIDEO_COMMENT("videoComment", "blackbox");

    private String type;
    private String owner;

    BBMessageType(String type, String owner) {
        this.type = type;
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public String getOwner() {
        return owner;
    }
}
