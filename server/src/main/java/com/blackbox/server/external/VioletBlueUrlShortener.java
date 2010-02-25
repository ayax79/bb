package com.blackbox.server.external;

import com.blackbox.foundation.Utils;
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
import java.text.MessageFormat;

import static java.lang.String.format;

public class VioletBlueUrlShortener implements IUrlShortener {

    private static final Logger logger = LoggerFactory.getLogger(VioletBlueUrlShortener.class);

    private String presentationUrl;
    private String serviceUrl;

    /**
     * @return a shortened url or the parameter url if the underlying shortener provider was unable to complete the request.
     */
    public String shorten(String url) throws IOException {
        PostMethod post = new PostMethod(serviceUrl);
        HttpClient client = new HttpClient();
        post.setParameter("url", url);
        int response = client.executeMethod(post);
        if (response != 200 && response != 201) {
            logger.warn(MessageFormat.format("Unable to shorten url...will return whole url due to response code {0}", response));
            return url;
        }
        String shorty = post.getResponseBodyAsString().trim();
        // we are getting back 'Please log in' instead of a shorty so let's guard against that...
        return Utils.isValidUrl(shorty) ? shorty : url;
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
