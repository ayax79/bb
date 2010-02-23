package com.blackbox.server.message.publisher;

import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.occasion.OccasionType;
import com.blackbox.server.external.IUrlShortener;
import com.blackbox.server.user.IExternalCredentialsDao;
import com.blackbox.server.util.FacebookClientFactory;
import com.blackbox.server.util.OccasionUtil;
import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.IFacebookRestClient;
import org.apache.camel.Exchange;
import org.joda.time.chrono.ISOChronology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * todo: should to both wall and non-wall
 */
public class PublishOccasionToFacebook {

    final private static Logger logger = LoggerFactory.getLogger(PublishOccasionToFacebook.class);

    @Resource
    IExternalCredentialsDao credentialsDao;

    @Resource
    IUrlShortener urlShortener;

    public void save(Exchange exchange) {
        org.apache.camel.Message camelMessage = exchange.getIn();
        if (camelMessage != null) {
            try {
                doPublication(camelMessage.getBody(Occasion.class));
            } catch (Exception t) {
                exchange.setException(t);
                logger.error(t.getMessage(), t);
            }
        } else {
            logger.warn("Required camelMessage not found!");
        }
    }

    void doPublication(Occasion occasion) throws IOException {
        if (!occasion.isPublishToFacebook() || OccasionType.OPEN != occasion.getOccasionType()) {
            return;
        }

        String occasionUrl = OccasionUtil.generateOccasionUrl(occasion, urlShortener);
        Map<String, String> faceBookEvent = newHashMap();
//        faceBookEvent.put("call_id", String.valueOf(System.currentTimeMillis()));
        faceBookEvent.put("name", occasion.getName());
        faceBookEvent.put("v", String.valueOf(1.0d));
        faceBookEvent.put("category", String.valueOf(occasion.getFacebookCategory()));
        faceBookEvent.put("subcategory", String.valueOf(occasion.getFacebookSubCategory()));
        faceBookEvent.put("host", occasion.getHostBy());
        faceBookEvent.put("location", occasion.getLocation());
        faceBookEvent.put("description", occasion.getFacebookDescription() + " " + occasionUrl);
        faceBookEvent.put("privacy_type", "OPEN");
        faceBookEvent.put("email", occasion.getEmail());
        faceBookEvent.put("phone", occasion.getPhoneNumber());
        faceBookEvent.put("street", occasion.getAddress().getAddress1() + " " + occasion.getAddress().getAddress2());
        faceBookEvent.put("city", occasion.getAddress().getCity());

        faceBookEvent.put("start_time", String.valueOf(occasion.getEventTime().toDateTime(ISOChronology.getInstanceUTC()).getMillis()));
        faceBookEvent.put("end_time", String.valueOf(occasion.getEventEndTime().toDateTime(ISOChronology.getInstanceUTC()).getMillis()));

//        faceBookEvent.put("session_key", "todo");

        try {
            IFacebookRestClient client = FacebookClientFactory.buildClient();
            long userId = client.users_getLoggedInUser();
            Long eventId = client.events_create(faceBookEvent);
            logger.debug(MessageFormat.format("Event cross-posted to Facebook with id {0}", eventId));
        } catch (FacebookException e) {
            logger.error("Error creating facebook event", e);
        }
    }


}