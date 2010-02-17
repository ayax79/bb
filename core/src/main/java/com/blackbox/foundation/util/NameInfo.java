package com.blackbox.foundation.util;

import java.io.Serializable;

public class NameInfo implements Serializable {

    private String displayName;
    private String username;

    public NameInfo(String displayName, String username) {
        this.displayName = displayName;
        this.username = username;
    }

    public NameInfo() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NameInfo nameInfo = (NameInfo) o;

        if (displayName != null ? !displayName.equals(nameInfo.displayName) : nameInfo.displayName != null)
            return false;
        //noinspection RedundantIfStatement
        if (username != null ? !username.equals(nameInfo.username) : nameInfo.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = displayName != null ? displayName.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NameInfo{" +
                "name='" + displayName + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
