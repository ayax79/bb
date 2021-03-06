package com.blackbox.server.external;

import com.blackbox.foundation.exception.BlackBoxException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.MessageFormat;

public class TwitterClient implements ITwitterClient {

    private static final Logger logger = LoggerFactory.getLogger(TwitterClient.class);

    private String updateUrl;
    private String searchUserStatusesUrl;

    public void publish(String text, String username, String password) throws BlackBoxException {
        if (logger.isDebugEnabled()) {
            logger.debug("Tweeting: {0} for user name: {1}", text, username);
        }
        try {
            doPublish(text, username, password);
        } catch (IOException e) {
            throw new BlackBoxException(e);
        }
    }

    private void doPublish(String text, String username, String password) throws IOException {
        PostMethod post = new PostMethod(updateUrl);
        try {
            HttpClient client = new HttpClient();
            post.setParameter("status", text);
            Credentials credentials = new UsernamePasswordCredentials(username, password);
            client.getState().setCredentials(AuthScope.ANY, credentials);
            int result = client.executeMethod(post);
            if (result != 200 && result != 201) {
                logger.warn(MessageFormat.format("Http response code from twitter is {0} for method {1}. This will result in that users {2} post not being tweeted!", result, post.getPath(), username));
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Twitter responded: " + post.getResponseBodyAsString());
            }
        } finally {
            post.releaseConnection();
        }
    }

    @Override
    /**
     * @return true if *the first 110!* characters of the text parameter is present in any data from the user's twitter feed.
     * Such text *doesn't even have to be in the feed.entry.content part of the feed returned.
     *
     * This method should only be used for testing and could be upgraded by adding rss feed parser to truly fetch and compare posting
     * with contents. 
     */
    public boolean thisWasTweeted(String text, String userName) throws BlackBoxException {
        try {
            return doWasTweeted(text, userName);
        } catch (IOException e) {
            throw new BlackBoxException(e);
        }
    }

    private boolean doWasTweeted(String text, String userName) throws IOException {
        GetMethod get = new GetMethod(searchUserStatusesUrl + userName);
        try {
            HttpClient client = new HttpClient();
            int result = client.executeMethod(get);
            if (result != 200 && result != 201) {
                logger.warn(MessageFormat.format("Http response code from twitter is {0} for method {1}. This will result in that users {2} post not being tweeted!", result, get.getPath(), userName));
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Twitter responded: " + get.getResponseBodyAsString());
            }
            return get.getResponseBodyAsString().contains(StringUtils.left(text, 110));
        } finally {
            get.releaseConnection();
        }
    }

    @Required
    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    @Required
    public void setSearchUserStatusesUrl(String searchUserStatusesUrl) {
        this.searchUserStatusesUrl = searchUserStatusesUrl;
    }
}
