package com.blackbox.util;

import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Bounds implements Serializable {

    private int startIndex;
    private int offset;
    private int maxResults = 10;
    @XmlJavaTypeAdapter(JodaDateTimeXmlAdapter.class)
    private DateTime startDate;
    @XmlJavaTypeAdapter(JodaDateTimeXmlAdapter.class)
    private DateTime endDate;
    private static final long serialVersionUID = 5125897299531895866L;

    public Bounds() {
    }

    /**
     * @param startIndex
     * @param maxResults
     */
    public Bounds(int startIndex, int maxResults) {
        this.startIndex = startIndex;
        this.maxResults = maxResults;
    }

    public Bounds(int startIndex, int offset, int maxResults) {
        this.startIndex = startIndex;
        this.offset = offset;
        this.maxResults = maxResults;
    }

    public Bounds(DateTime startDate, DateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Bounds(int startIndex, int maxResults, DateTime startDate, DateTime endDate) {
        this.startIndex = startIndex;
        this.maxResults = maxResults;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Bounds(int startIndex, int offset, int maxResults, DateTime startDate, DateTime endDate) {
        this.startIndex = startIndex;
        this.offset = offset;
        this.maxResults = maxResults;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public ReadableDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public ReadableDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public static Bounds copyBounds(Bounds bounds) {
        Bounds b = new Bounds();
        b.endDate = bounds.endDate;
        b.startDate = bounds.startDate;
        b.startIndex = bounds.startIndex;
        b.maxResults = bounds.maxResults;
        return b;
    }

    public Bounds next(int numberMore) {
        startIndex = startIndex + numberMore;
        return this;
    }

    public static Bounds boundLess() {
        return new Bounds(0, Integer.MAX_VALUE);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bounds bounds = (Bounds) o;

        if (maxResults != bounds.maxResults) return false;
        if (startIndex != bounds.startIndex) return false;
        if (endDate != null ? !endDate.equals(bounds.endDate) : bounds.endDate != null) return false;
        //noinspection RedundantIfStatement
        if (startDate != null ? !startDate.equals(bounds.startDate) : bounds.startDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startIndex;
        result = 31 * result + maxResults;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bounds{" +
                "startIndex=" + startIndex +
                ", maxResults=" + maxResults +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
