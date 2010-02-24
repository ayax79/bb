package com.blackbox.server.util;

import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.server.external.IUrlShortener;

import java.io.IOException;

/**
 * A small place to map to twitter specifications....
 *
 * @author colin@blackboxrepublic.com
 */
public final class Twitterizer {

    private Twitterizer() {
    }

    /**
     * @return a twitter message based on message short body or message body
     */
    public static String buildTwitterMessage(Message message) throws IOException {
        String body = message.getShortBody();
        if (body == null) {
            body = message.getBody();
        }
        return makeSmsWorthyPayload(body, "");
    }

    /**
     * @return a twitter message based on occasion twitter description or occasion description
     */
    public static String buildTwitterMessage(Occasion occasion, String occasionUrl, IUrlShortener urlShortener) throws IOException {
        String body = occasion.getTwitterDescription();
        if (body == null) {
            body = occasion.getDescription();
        }
        return makeSmsWorthyPayload(body, urlShortener.shorten(occasionUrl));
    }

    public static String makeSmsWorthyPayload(String body, String url) {
        int totalSize = body.length() + url.length() + 4; // plus one is for one extra space;

        StringBuilder builder = new StringBuilder();
        if (totalSize > 140) {
            body = body.substring(0, 140 - (url.length() + 4));
            builder.append(body)
                    .append("...");

        } else {
            builder.append(body);
        }

        return builder
                .append(" ")
                .append(url)
                .toString();
    }

}