package com.blackbox.server.external;

import com.blackbox.message.Message;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Required;
import static org.yestech.lib.util.EncoderUtil.uriEncode;

import java.io.IOException;
import static java.lang.String.format;

public class VioletBlueUrlShortener implements IUrlShortener {

    private String presentationUrl;

    public String shorten(String url) throws IOException {
        HttpClient client = new HttpClient();
        url = uriEncode(url);
        GetMethod get = new GetMethod(
                format("http://vb.ly/api/?action=shorturl&url=%s&format=simple", url));

        client.executeMethod(get);
        return get.getResponseBodyAsString().trim();
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

    public String getPresentationUrl() {
        return presentationUrl;
    }
}
