package com.blackbox.server.external;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: andrew
 * Date: Dec 2, 2009
 * Time: 2:11:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ITwitterClient {
    void publish(String text, String username, String password) throws IOException;
}
