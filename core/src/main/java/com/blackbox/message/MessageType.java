/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.message;

import java.io.Serializable;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class MessageType implements Serializable {
    private String customDefinedType;
    private String customTypeOwner;

    public MessageType() {
    }

    public MessageType(String customDefinedType, String customTypeOwner) {
        this.customDefinedType = customDefinedType;
        this.customTypeOwner = customTypeOwner;
    }

    public String getCustomDefinedType() {
        return customDefinedType;
    }

    public void setCustomDefinedType(String customDefinedType) {
        this.customDefinedType = customDefinedType;
    }

    public String getCustomTypeOwner() {
        return customTypeOwner;
    }

    public void setCustomTypeOwner(String customTypeOwner) {
        this.customTypeOwner = customTypeOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageType that = (MessageType) o;

        if (customDefinedType != null ? !customDefinedType.equals(that.customDefinedType) : that.customDefinedType != null)
            return false;
        //noinspection RedundantIfStatement
        if (customTypeOwner != null ? !customTypeOwner.equals(that.customTypeOwner) : that.customTypeOwner != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = customDefinedType != null ? customDefinedType.hashCode() : 0;
        result = 31 * result + (customTypeOwner != null ? customTypeOwner.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "customDefinedType='" + customDefinedType + '\'' +
                ", customTypeOwner='" + customTypeOwner + '\'' +
                '}';
    }
}
