package com.blackbox.server.external;

import com.blackbox.message.Message;

import java.io.IOException;

/**
 */
public interface IUrlShortener {
    String shortMessageUrl(Message message) throws IOException;

    String shorten(String url) throws IOException;

    String shorten() throws IOException;

    String getPresentationUrl();
}
