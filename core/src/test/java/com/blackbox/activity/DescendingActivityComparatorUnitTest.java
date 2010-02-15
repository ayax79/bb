/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.activity;

import static com.blackbox.activity.DescendingActivityComparator.getDescendingActivityComparator;
import com.blackbox.media.MediaMetaData;
import com.blackbox.message.Message;
import com.blackbox.occasion.Occasion;
import static com.google.common.collect.Lists.newArrayList;
import org.joda.time.DateTime;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class DescendingActivityComparatorUnitTest {

    @Test
    public void testCompareListUnSortedSmallestToLargest() {
        final MediaMetaData data1 = ActivityFactory.createMedia();
        DateTime time1 = new DateTime();
        time1 = time1.plusMillis(1);
        data1.setPostDate(time1);
        final Message message1 = ActivityFactory.createMessage();
        DateTime time2 = new DateTime();
        time2 = time2.plusMillis(2);
        message1.setPostDate(time2);
        final MediaMetaData data2 = ActivityFactory.createMedia();
        DateTime time3 = new DateTime();
        time3 = time3.plusMillis(3);
        data2.setPostDate(time3);
        final Occasion occasion1 = ActivityFactory.createOccasion();
        DateTime time4 = new DateTime();
        time4 = time4.plusMillis(4);
        occasion1.setPostDate(time4);

        List<IActivity> unsorted = newArrayList();
        unsorted.add(data1);
        unsorted.add(message1);
        unsorted.add(data2);
        unsorted.add(occasion1);

        List<IActivity> sorted = newArrayList();
        sorted.add(occasion1);
        sorted.add(data2);
        sorted.add(message1);
        sorted.add(data1);

        Collections.sort(unsorted, getDescendingActivityComparator());
        assertEquals(sorted, unsorted);
    }

    @Test
    public void testCompareListUnSortedAlreadySorted() {
        final MediaMetaData data1 = ActivityFactory.createMedia();
        DateTime time1 = new DateTime();
        time1 = time1.plusMillis(1);
        data1.setPostDate(time1);
        final Message message1 = ActivityFactory.createMessage();
        DateTime time2 = new DateTime();
        time2 = time2.plusMillis(2);
        message1.setPostDate(time2);
        final MediaMetaData data2 = ActivityFactory.createMedia();
        DateTime time3 = new DateTime();
        time3 = time3.plusMillis(3);
        data2.setPostDate(time3);
        final Occasion occasion1 = ActivityFactory.createOccasion();
        DateTime time4 = new DateTime();
        time4 = time4.plusMillis(4);
        occasion1.setPostDate(time4);

        List<IActivity> unsorted = newArrayList();
        unsorted.add(occasion1);
        unsorted.add(data2);
        unsorted.add(message1);
        unsorted.add(data1);

        List<IActivity> sorted = newArrayList();
        sorted.add(occasion1);
        sorted.add(data2);
        sorted.add(message1);
        sorted.add(data1);

        Collections.sort(unsorted, getDescendingActivityComparator());
        assertEquals(sorted, unsorted);
    }

    @Test
    public void testCompareListUnSortedMixed() {
        final MediaMetaData data1 = ActivityFactory.createMedia();
        DateTime time1 = new DateTime();
        time1 = time1.plusMillis(1);
        data1.setPostDate(time1);
        final Message message1 = ActivityFactory.createMessage();
        DateTime time2 = new DateTime();
        time2 = time2.plusMillis(2);
        message1.setPostDate(time2);
        final MediaMetaData data2 = ActivityFactory.createMedia();
        DateTime time3 = new DateTime();
        time3 = time3.plusMillis(3);
        data2.setPostDate(time3);
        final Occasion occasion1 = ActivityFactory.createOccasion();
        DateTime time4 = new DateTime();
        time4 = time4.plusMillis(4);
        occasion1.setPostDate(time4);

        List<IActivity> unsorted = newArrayList();
        unsorted.add(data2);
        unsorted.add(data1);
        unsorted.add(occasion1);
        unsorted.add(message1);

        List<IActivity> sorted = newArrayList();
        sorted.add(occasion1);
        sorted.add(data2);
        sorted.add(message1);
        sorted.add(data1);

        Collections.sort(unsorted, getDescendingActivityComparator());
        assertEquals(sorted, unsorted);
    }

    @Test
    public void testCompare() {
        final MediaMetaData data1 = ActivityFactory.createMedia();
        DateTime time1 = new DateTime();
        time1 = time1.plusMillis(1);
        data1.setPostDate(time1);
        final Message message1 = ActivityFactory.createMessage();
        DateTime time2 = new DateTime();
        time2 = time2.plusMillis(2);
        message1.setPostDate(time2);
        DescendingActivityComparator comparator = getDescendingActivityComparator();
        assertEquals(1, comparator.compare(data1, message1));
    }
}
