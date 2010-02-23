package com.blackbox.server.util;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJsonRestClient;

/**
 * @author colin@blackboxrepublic.com
 */
public class FacebookClientFactory {

    public static FacebookJsonRestClient buildClient() throws FacebookException {
        return new FacebookJsonRestClient("54eb4f351c275836be9a536f4eb73914", "ec814b0cb2070092554d72237a0dc8d7");
    }


}
