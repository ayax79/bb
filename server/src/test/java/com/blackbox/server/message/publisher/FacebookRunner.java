package com.blackbox.server.message.publisher;

import com.blackbox.server.util.FacebookClientFactory;
import com.blackbox.testingutils.ExternalCredentialsFixture;
import com.google.code.facebookapi.IFacebookRestClient;

/**
 * @author colin@blackboxrepublic.com
 */
public class FacebookRunner {

    public static void main(String[] args) throws Exception {
        IFacebookRestClient client = FacebookClientFactory.buildClient(ExternalCredentialsFixture.facebookPoster);
        System.out.println("client.users_getLoggedInUser() = " + client.users_getLoggedInUser());
    }
}
