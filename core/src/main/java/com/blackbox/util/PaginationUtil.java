package com.blackbox.util;

import com.blackbox.user.PaginationResults;
import org.apache.commons.collections15.Closure;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.functors.NOPClosure;

import java.util.Collections;
import java.util.List;

/**
 * @author A.J. Wright
 */
public final class PaginationUtil {

    private PaginationUtil() {
    }

    /**
     * Sublists the list parameter according to bounds' start index and maximum results.
     * <strong>warning</strong>this method does not operate on bounds' date fields!
     *
     * @param bounds you want to be used to sublist or null if you don't want to limit the sub list
     */
    public static <T> List<T> subList(List<T> list, Bounds bounds) {
        Bounds boundsToUse = upgradeNullBounds(bounds);
        return subList(list, boundsToUse.getStartIndex(), boundsToUse.getMaxResults());
    }

    public static <T> List<T> subList(List<T> list, int startIndex, int maxResults) {
        return subList(list, startIndex, maxResults, NOPClosure.<T>getInstance());
    }

    public static <T> List<T> subList(List<T> list, int startIndex, int maxResults, Closure<T> closure) {
        maxResults = startIndex + maxResults;
        if (maxResults > list.size()) {
            maxResults = list.size();
        }
        if (startIndex > maxResults) {
            return Collections.emptyList();
        }
        List<T> subList = list.subList(startIndex, maxResults);
        CollectionUtils.forAllDo(subList, closure);
        return subList;
    }

    /**
     * Builds pagination results the list parameter according to bounds' start index and maximum results.
     * <strong>warning</strong>this method does not operate on bounds' date fields!
     *
     * @param bounds you want to be used to pagination results or null if you don't want to limit the sub list
     */
    public static <T> PaginationResults<T> buildPaginationResults(List<T> list, Bounds bounds) {
        Bounds boundsToUse = upgradeNullBounds(bounds);
        return buildPaginationResults(list, boundsToUse.getStartIndex(), boundsToUse.getMaxResults());
    }

    public static <T> PaginationResults<T> buildPaginationResults(List<T> list, int startIndex, int maxResults) {
        return buildPaginationResults(list, startIndex, maxResults, NOPClosure.<T>getInstance());
    }

    public static <T> PaginationResults<T> buildPaginationResults(List<T> list, int startIndex, int maxResults, Closure<T> closure) {
        int total = list.size();
        list = subList(list, startIndex, maxResults, closure);
        PaginationResults<T> results = new PaginationResults<T>();
        results.setTotalResults(total);
        results.setNumResults(list.size());
        results.setStartIndex(startIndex);
        results.setResults(list);
        return results;
    }

    private static Bounds upgradeNullBounds(Bounds bounds) {
        return bounds == null ? Bounds.boundLess() : bounds;
    }

}
