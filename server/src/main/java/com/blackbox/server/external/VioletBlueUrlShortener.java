package com.blackbox.server.external;

import com.blackbox.foundation.message.Message;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import static org.yestech.lib.util.EncoderUtil.uriEncode;

import java.io.IOException;

import static java.lang.String.format;

public class VioletBlueUrlShortener implements IUrlShortener {

    private static final Logger logger = LoggerFactory.getLogger(VioletBlueUrlShortener.class);

    private String presentationUrl;
    private String serviceUrl;

    public String shorten(String url) throws IOException {
        PostMethod post = new PostMethod(serviceUrl);
        HttpClient client = new HttpClient();
        post.setParameter("url", url);
        int response = client.executeMethod(post);
        if (response != 200 && response != 201) {
            logger.warn("Unable to shorten url...will return whole url");
            return url;
        }
        String answer = post.getResponseBodyAsString().trim();
        // we are getting a 200 back yet the content is this 'Please log in' so let's ignore that for now...(
        return "Please log in".equals(answer) ? url : answer;
    }


    public String shorten() throws IOException {
        return shorten(presentationUrl);
    }

    public String shortMessageUrl(Message message) throws IOException {
        // todo add external page stuffs
        return shorten(presentationUrl);
    }

    @Required
    public void setPresentationUrl(String presentationUrl) {
        this.presentationUrl = presentationUrl;
    }

    @Required
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getPresentationUrl() {
        return presentationUrl;
    }
}

/*    public String shortenGet(String url) throws IOException {
        HttpClient client = new HttpClient();
        url = uriEncode(url);
        GetMethod get = new GetMethod(serviceUrl + url);
        int response = client.executeMethod(get);
        if (response != 200 && response != 201) {
            logger.warn("Unable to shorten url...will return whole url");
            return url;
        }
        return get.getResponseBodyAsString().trim();
    }
*/