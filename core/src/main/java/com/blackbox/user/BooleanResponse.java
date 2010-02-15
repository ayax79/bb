package com.blackbox.user;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class BooleanResponse implements Serializable {

    private boolean value;

    public BooleanResponse() {
    }

    public BooleanResponse(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BooleanResponse that = (BooleanResponse) o;

        //noinspection RedundantIfStatement
        if (value != that.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }

    @Override
    public String toString() {
        return "BooleanResponse{" +
                "value=" + value +
                '}';
    }
}
