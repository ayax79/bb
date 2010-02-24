package com.blackbox.server.util;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJaxbRestClient;
import com.google.code.facebookapi.IFacebookRestClient;

/**
 * @author colin@blackboxrepublic.com
 */
public class FacebookClientFactory {

    public static IFacebookRestClient buildClient(String sessionKey) throws FacebookException {
        return new FacebookJaxbRestClient("54eb4f351c275836be9a536f4eb73914", "ec814b0cb2070092554d72237a0dc8d7", sessionKey);
    }


}
