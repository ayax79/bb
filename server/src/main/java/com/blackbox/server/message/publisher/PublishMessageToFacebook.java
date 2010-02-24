package com.blackbox.server.message.publisher;

import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.server.external.IUrlShortener;
import com.blackbox.server.user.IExternalCredentialsDao;
import com.blackbox.server.util.FacebookClientFactory;
import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.IFacebookRestClient;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;

public class PublishMessageToFacebook {

    final private static Logger logger = LoggerFactory.getLogger(PublishMessageToFacebook.class);

    @Resource
    IExternalCredentialsDao credentialsDao;

    @Resource
    IUrlShortener urlShortener;

    public void save(Exchange exchange) {
        org.apache.camel.Message camelMessage = exchange.getIn();
        if (camelMessage != null) {
            try {
                doPublication(camelMessage.getBody(Message.class));
            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }
        } else {
            logger.warn("Required camelMessage not found!");
        }
    }

    String doPublication(Message message) throws IOException, FacebookException {
        if (message == null || !message.isPublishToFacebook()) {
            return null;
        }

        IFacebookRestClient client = FacebookClientFactory.buildClient(message.getExternalCredentials(ExternalCredentials.CredentialType.FACEBOOK).getFacebookCredentials());
        String response = client.stream_publish(message.getBody(), null, null, null, null);
        logger.debug(response);
        return response;
    }

}