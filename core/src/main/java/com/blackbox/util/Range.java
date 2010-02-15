package com.blackbox.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Range implements Serializable {

    protected int startIndex;
    protected int lastIndex;

    public Range() {
    }

    public Range(int startIndex, int lastIndex) {
        this.startIndex = startIndex;
        this.lastIndex = lastIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Range range = (Range) o;

        if (lastIndex != range.lastIndex) return false;
        //noinspection RedundantIfStatement
        if (startIndex != range.startIndex) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startIndex;
        result = 31 * result + lastIndex;
        return result;
    }

    @Override
    public String toString() {
        return "Range{" +
                "startIndex=" + startIndex +
                ", lastIndex=" + lastIndex +
                '}';
    }

   
}
