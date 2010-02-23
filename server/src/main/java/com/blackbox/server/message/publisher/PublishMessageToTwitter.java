package com.blackbox.server.message.publisher;

import com.blackbox.foundation.message.Message;
import com.blackbox.server.external.ITwitterClient;
import com.blackbox.server.external.IUrlShortener;
import com.blackbox.server.user.IExternalCredentialsDao;
import com.blackbox.foundation.user.ExternalCredentials;

import static com.blackbox.foundation.user.ExternalCredentials.CredentialType.TWITTER;

import com.blackbox.server.util.Twitterizer;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.MessageFormat;

public class PublishMessageToTwitter {

    final private static Logger logger = LoggerFactory.getLogger(PublishMessageToTwitter.class);

    @Resource
    IExternalCredentialsDao credentialsDao;

    @Resource
    IUrlShortener urlShortener;

    @Resource
    ITwitterClient twitterClient;

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

    void doPublication(Message message) throws IOException {
        if (message == null || !message.isPublishToTwitter()) {
            return;
        }

        ExternalCredentials externalCredentials = message.getCreds();
        if (externalCredentials == null) {
            externalCredentials = credentialsDao.loadByOwnerAndCredType(message.getSender().getGuid(), TWITTER);
        }

        if (externalCredentials != null) {
            send(externalCredentials, Twitterizer.buildTwitterMessage(message, urlShortener));
        } else {
            logger.warn(MessageFormat.format("External credentials for that message {0} were not found!", message));
        }
    }

    protected void send(ExternalCredentials externalCredentials, String message) throws IOException {
        twitterClient.publish(message, externalCredentials.decryptUsername(), externalCredentials.decryptPassword());
    }


}
