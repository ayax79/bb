package com.blackbox.server.external;

import java.io.IOException;

/**
 * @author andrew
 */
public interface ITwitterClient {

    void publish(String text, String username, String password) throws IOException;

    /**
     * @return true if *the first 110!* characters of the text parameter is present in any data from the user's twitter feed.
     *         Such text *doesn't even have to be in the feed.entry.content part of the feed returned.
     *         <p/>
     *         This method should only be used for testing and could be upgraded by adding rss feed parser to truly fetch and compare posting
     *         with contents.
     */
    boolean thisWasTweeted(String text, String username) throws IOException;
}
