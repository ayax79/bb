package com.blackbox.foundation.common;

import com.blackbox.foundation.util.Affirm;
import com.blackbox.foundation.util.Bounds;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * A way to keep around 2 bounds instances when we need them
 *
 * @author colin@blackboxrepublic.com
 */
public class TwoBounds {

    // yes, these have business/entity type names and they are in util and that's not cool. but this can't go in either media or message so where? 
    private Bounds mediaMetaDataBounds;
    private Bounds messageBounds;

    public TwoBounds(Bounds mediaMetaDataBounds, Bounds messageBounds) {
        Affirm.that(mediaMetaDataBounds.getMaxResults() == messageBounds.getMaxResults(), "Both bounds must have same number of maximum results", IllegalArgumentException.class);
        this.mediaMetaDataBounds = mediaMetaDataBounds;
        this.messageBounds = messageBounds;
    }

    public TwoBounds(int startIndex, int maxResults) {
        this(new Bounds(startIndex, maxResults), new Bounds(startIndex, maxResults));
    }

    public Bounds getMediaMetaDataBounds() {
        return mediaMetaDataBounds;
    }

    public Bounds getMessageBounds() {
        return messageBounds;
    }

    public int getMaxResults() {
        return mediaMetaDataBounds.getMaxResults();
    }

    public TwoBounds next(int number) {
        mediaMetaDataBounds.next(number);
        messageBounds.next(number);
        return this;
    }

    public static TwoBounds boundLess() {
        return new TwoBounds(Bounds.boundLess(), Bounds.boundLess());
    }

    public void setStartDate(DateTime dateTime) {
        mediaMetaDataBounds.setStartDate(dateTime);
        messageBounds.setStartDate(dateTime);
    }

    public void setEndDate(DateTime dateTime) {
        mediaMetaDataBounds.setEndDate(dateTime);
        messageBounds.setEndDate(dateTime);
    }

    public static TwoBounds firstTen() {
        return new TwoBounds(Bounds.firstTen(), Bounds.firstTen());
    }

    public void setStartIndex(int startIndex) {
        mediaMetaDataBounds.setStartIndex(startIndex);
        messageBounds.setStartIndex(startIndex);
    }

    public void setMaxResults(int maxResults) {
        mediaMetaDataBounds.setMaxResults(maxResults);
        messageBounds.setMaxResults(maxResults);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
