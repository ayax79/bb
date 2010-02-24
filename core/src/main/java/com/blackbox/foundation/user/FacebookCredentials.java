package com.blackbox.foundation.user;

import java.io.Serializable;

/**
 * @author colin@blackboxrepublic.com
 */
public class FacebookCredentials implements Serializable {

    private String sessionKey;
    private String sessionSecret;
    private String apiKey;
    private boolean liveForever;

    public FacebookCredentials(String apiKey, String sessionSecret, String sessionKey, boolean liveForever) {
        this.apiKey = apiKey;
        this.sessionSecret = sessionSecret;
        this.sessionKey = sessionKey;
        this.liveForever = liveForever;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getSessionSecret() {
        return sessionSecret;
    }

    public String getApiKey() {
        return apiKey;
    }

    public boolean isLiveForever() {
        return liveForever;
    }
}
