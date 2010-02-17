package com.blackbox.foundation.user;

import com.blackbox.foundation.activity.ActivityThread;
import com.blackbox.foundation.bookmark.UserWish;
import com.blackbox.foundation.search.SearchResult;
import com.blackbox.foundation.social.Connection;
import com.blackbox.foundation.social.UserVouch;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.List;

/**
 * @author A.J. Wright
 */
@XmlRootElement
@XmlSeeAlso({
        ActivityThread.class,
        UserWish.class,
        UserVouch.class,
        Connection.class,
        com.blackbox.foundation.occasion.Occasion.class,
        SearchResult.class
})
public class PaginationResults<E> implements Serializable {

    private int startIndex;
    private int numResults;
    private long totalResults;
    private List<E> results;
    private static final long serialVersionUID = 4529547906849384224L;

    /**
     * Starting point of the threads.
     *
     * @return Starting point of the threads.
     */
    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * Number of results returned in the results. Same as results.size().
     *
     * @return Number of results returned in the results. Same as results.size().
     */
    public int getNumResults() {
        return numResults;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    /**
     * Total amount of threads in the system that match the request.
     *
     * @return the total num results.
     */
    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public List<E> getResults() {
        return results;
    }

    public void setResults(List<E> results) {
        this.results = results;
    }

}
