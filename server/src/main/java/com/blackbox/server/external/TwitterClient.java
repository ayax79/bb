package com.blackbox.server.external;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("twitterService")
public class TwitterClient implements ITwitterClient {

    public static final String BASE_URL = "http://twitter.com";

    public void publish(String text, String username, String password) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(BASE_URL + "/statuses/save.json?source=Blackbox%20Republic");
        try {
            post.setParameter("status", text);
            Credentials creds = new UsernamePasswordCredentials(username, password);
            client.getState().setCredentials(AuthScope.ANY, creds);
            client.executeMethod(post);
        } finally {
            post.releaseConnection();
        }
    }

}
