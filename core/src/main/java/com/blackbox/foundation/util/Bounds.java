package com.blackbox.foundation.util;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.DateTime;

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

    private static final long serialVersionUID = 5125897299531895866L;

    private int startIndex;
    private int maxResults = 10;
    @XmlJavaTypeAdapter(JodaDateTimeXmlAdapter.class)
    private DateTime startDate;
    @XmlJavaTypeAdapter(JodaDateTimeXmlAdapter.class)
    private DateTime endDate;

    private boolean changed;

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

    public int getStartIndex() {
        return startIndex;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setStartDate(DateTime startDate) {
        this.changed = startDate == null || !EqualsBuilder.reflectionEquals(this.startDate, startDate);
        this.startDate = startDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public void setStartIndex(int startIndex) {
        if (this.startIndex != startIndex) {
            changed = true;
        }
        this.startIndex = startIndex;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public Bounds next(int numberMore) {
        setStartIndex(startIndex + numberMore);
        return this;
    }

    public static Bounds boundLess() {
        return new ImmutableBounds(0, Integer.MAX_VALUE);
    }

    public static Bounds firstTen() {
        return new ImmutableBounds(0, 10);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Bounds)) return false;

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

    public boolean isChanged() {
        return changed;
    }

    private static class ImmutableBounds extends Bounds {

        private ImmutableBounds(int startIndex, int maxResults) {
            super(startIndex, maxResults);
        }

        @Override
        public void setStartIndex(int startIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setStartDate(DateTime startDate) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setEndDate(DateTime endDate) {
            throw new UnsupportedOperationException();
        }
    }

}
