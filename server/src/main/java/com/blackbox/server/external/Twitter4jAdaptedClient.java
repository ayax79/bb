package com.blackbox.server.external;

import com.blackbox.foundation.exception.BlackBoxException;
import org.apache.commons.lang.StringUtils;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;

/**
 * @author colin@blackboxrepublic.com
 */
public class Twitter4jAdaptedClient implements ITwitterClient {

    @Override
    public void publish(String text, String userName, String password) throws BlackBoxException {
        try {

            new Twitter(userName, password).updateStatus(text);
        } catch (TwitterException e) {
            throw new BlackBoxException(e);
        }
    }

    @Override
    public boolean thisWasTweeted(String text, String userName) throws BlackBoxException {
        try {
            List<Status> statuses = new Twitter().getUserTimeline(userName);
            for (Status status : statuses) {
                if (status.getText().contains(StringUtils.left(text, 120))) {
                    return true;
                }
            }
        } catch (TwitterException e) {
            throw new BlackBoxException(e);
        }
        return false;
    }
}
