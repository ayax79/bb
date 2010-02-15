/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XmlRootElement(name = "status")
public enum Status {
    ENABLED(0, "enabled"),
    DISABLED(1, "disabled"),
    UNVERIFIED(2, "unverified"),
    DELETED(3, "deleted"),
    PRE_REGISTERED(4, "pre_registered"),
    CLOSED(5, "closed"),
    INCOMPLETE(6, "incomplete");

    private int value;
    private String key;

    Status(int value, String key) {
        this.value = value;
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}
