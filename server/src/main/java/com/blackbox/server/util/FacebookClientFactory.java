package com.blackbox.server.util;

import com.blackbox.foundation.user.FacebookCredentials;
import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJaxbRestClient;
import com.google.code.facebookapi.IFacebookRestClient;

/**
 * @author colin@blackboxrepublic.com
 */
public class FacebookClientFactory {

    public static IFacebookRestClient buildClient(FacebookCredentials facebookCredentials) throws FacebookException {
        return new FacebookJaxbRestClient(facebookCredentials.getApiKey(), facebookCredentials.getSessionSecret(), facebookCredentials.getSessionKey(), true);
    }

}
