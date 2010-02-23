package com.blackbox.server.message.publisher;

import com.blackbox.foundation.message.Message;
import com.blackbox.server.external.IUrlShortener;
import com.blackbox.server.user.IExternalCredentialsDao;
import com.blackbox.server.util.FacebookClientFactory;
import com.google.code.facebookapi.BundleActionLink;
import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJsonRestClient;
import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;

import static com.google.common.collect.Lists.newArrayList;

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

    void doPublication(Message message) throws IOException {
        if (message == null || !message.isPublishToFacebook()) {
            return;
        }

        try {
            BundleActionLink link = new BundleActionLink();
            String url = "http://todo.com"; // OccasionUtil.generateOccasionUrl(message, urlShortener);
            String shortenUrl = urlShortener.shorten(url);
            link.setHref(shortenUrl);
            link.setText(StringUtils.substring(message.getName(), 0, 20) + "....");

            FacebookJsonRestClient client = FacebookClientFactory.buildClient();
            String response = client.stream_publish(message.getBody(), null, newArrayList(link), null, client.users_getLoggedInUser());
            logger.debug(response);
        } catch (FacebookException e) {
            logger.error("Error creating facebook event", e);
        }
    }

}