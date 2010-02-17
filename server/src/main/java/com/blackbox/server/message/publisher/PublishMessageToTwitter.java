package com.blackbox.server.message.publisher;

import com.blackbox.foundation.message.Message;
import com.blackbox.server.external.ITwitterClient;
import com.blackbox.server.external.IUrlShortener;
import com.blackbox.server.user.IExternalCredentialsDao;
import com.blackbox.foundation.user.ExternalCredentials;
import static com.blackbox.foundation.user.ExternalCredentials.CredentialType.TWITTER;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;

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
                Message message = camelMessage.getBody(Message.class);
                if (message != null && message.isPublishToTwitter()) {
                    ExternalCredentials cred = message.getCreds();
                    if (cred == null) {
                        cred = credentialsDao.loadByOwnerAndCredType(message.getSender().getGuid(), TWITTER);
                    }
                    
                    if (cred != null) {
                        String twitMessage = buildTwitterMessage(message);
                        send(cred, twitMessage);
                    }
                }

            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }
        }
    }

    protected void send(ExternalCredentials cred, String msg) throws IOException {
        twitterClient.publish(msg, cred.decryptUsername(), cred.decryptPassword());
    }

    protected String buildTwitterMessage(Message message) throws IOException {
        String body = message.getShortBody();
        if (body == null) {
            body = message.getBody();
        }

        String url = urlShortener.shortMessageUrl(message);

        int totalSize = body.length() + url.length() + 4; // plus one is for one extra space;

        StringBuilder builder = new StringBuilder();
        if (totalSize > 140) {
            body = body.substring(0, 140 - (url.length() + 4));
            builder.append(body)
                    .append("...");

        }
        else {
            builder.append(body);
        }
        
        return builder
                .append(" ")
                .append(url)
                .toString();
    }



}
