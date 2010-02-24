package com.blackbox.server.message.publisher;

import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.server.util.FacebookClientFactory;
import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.IFacebookRestClient;

/**
 * @author colin@blackboxrepublic.com
 */
public class FacebookUtils {

    public boolean userHasThisMessage(ExternalCredentials externalCredentials) throws FacebookException {
        IFacebookRestClient client = FacebookClientFactory.buildClient(externalCredentials.getFacebookCredentials());
//            Long eventId = client.(faceBookEvent);
//            logger.debug(MessageFormat.format("Event cross-posted to Facebook with id {0}", eventId));
        return false;
    }
}
