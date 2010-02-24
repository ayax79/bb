package com.blackbox.server.message.publisher;

import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.server.external.ITwitterClient;
import com.blackbox.server.external.IUrlShortener;
import com.blackbox.server.user.IExternalCredentialsDao;
import com.blackbox.server.util.OccasionUtil;
import com.blackbox.server.util.Twitterizer;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.MessageFormat;

import static com.blackbox.foundation.user.ExternalCredentials.CredentialType.TWITTER;

public class PublishOccasionToTwitter {

    final private static Logger logger = LoggerFactory.getLogger(PublishOccasionToTwitter.class);

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
                doPublication(camelMessage.getBody(Occasion.class));
            } catch (Exception t) {
                exchange.setException(t);
                logger.error("Unable to publish to twitter", t);
            }
        }
    }

    public boolean doPublication(Occasion occasion) throws IOException {
        if (occasion == null || !occasion.isPublishToTwitter()) {
            return false;
        }

        ExternalCredentials externalCredentials = occasion.getExternalCredentials(TWITTER);
        if (externalCredentials == null) {
            externalCredentials = credentialsDao.loadByOwnerAndCredType(occasion.getSender().getGuid(), TWITTER);
        }

        if (externalCredentials != null) {
            send(externalCredentials, Twitterizer.buildTwitterMessage(occasion, OccasionUtil.generateOccasionUrl(occasion, urlShortener), urlShortener));
            return true;
        } else {
            logger.warn(MessageFormat.format("External credentials for that occasion {0} were not found!", occasion));
            return false;
        }
    }

    protected void send(ExternalCredentials externalCredentials, String message) throws IOException {
        twitterClient.publish(message, externalCredentials.decryptUsername(), externalCredentials.decryptPassword());
    }


}