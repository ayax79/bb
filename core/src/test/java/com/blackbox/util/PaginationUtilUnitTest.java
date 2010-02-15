package com.blackbox.util;

import com.blackbox.user.PaginationResults;
import org.apache.commons.collections15.Closure;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;

/**
 * @author colin@blackboxrepublic.com
 */
public class PaginationUtilUnitTest {

    private List<String> list;
    private CountingClosure<String> closure;

    @Before
    public void initialize() {
        closure = new CountingClosure<String>();
        list = newArrayList();
        list.add("one");
        list.add("two");
        list.add("three");
    }

    @Test
    public void testSubListExecutesOnClosure() throws Exception {
        List<String> results = PaginationUtil.subList(list, 0, 1, closure);
        assertEquals(1, results.size());
        assertEquals(1, closure.getCount());
    }

    @Test
    public void testBuildPaginationResultsExecutesOnClosure() throws Exception {
        PaginationResults<String> results = PaginationUtil.buildPaginationResults(list, 0, 2, closure);
        assertEquals(2, results.getNumResults());
        assertEquals(2, closure.getCount());
    }

    private static class CountingClosure<T> implements Closure<T> {

        private int count;

        @Override
        public void execute(T t) {
            count++;
        }

        public int getCount() {
            return count;
        }
    }
}
