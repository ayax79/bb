package com.blackbox.server.message.publisher;

import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.occasion.OccasionType;
import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.testingutils.ExternalCredentialsFixture;
import com.google.code.facebookapi.FacebookException;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author colin@blackboxrepublic.com
 */
public class PublishOccasionToFacebookIntegrationTest extends BaseIntegrationTest {

    @Resource
    PublishOccasionToFacebook publishOccasionToFacebook;

    private String guid = "29e180f8-5609-464e-b817-869c56f15e0b";

    @Test
    public void testPublishToFacebook() throws Exception {
        doPublish(2);
    }

    @Test
    public void testPublishToFacebookNoEndDate() throws Exception {
        doPublish(null);
    }

    private void doPublish(Integer eventDurationInHours) throws IOException, FacebookException {
        Occasion occasion = Occasion.createOccasion();
        occasion.setPublishToFacebook(true);
        occasion.setOccasionType(OccasionType.OPEN);
        occasion.addExternalCredentials(ExternalCredentials.buildFacebookExternalCredentials(occasion.toEntityReference(), ExternalCredentialsFixture.facebookPoster));
        occasion.setName(PublishOccasionToFacebookIntegrationTest.class.getSimpleName());
        occasion.setFacebookCategory(1);
        occasion.setFacebookSubCategory(1);
        occasion.setHostBy("colin");
        occasion.setLocation("colin's house");
        occasion.setDescription(guid);
        occasion.setFacebookDescription(occasion.getDescription());
        occasion.setEmail("colin@blackboxrepublic.com");
        occasion.setPhoneNumber("5031234567");
        occasion.getAddress().setAddress1("1 se washington");
        occasion.getAddress().setAddress2("suite 152");
        occasion.getAddress().setCity("portland");
        DateTime start = new DateTime().plusMonths(1);
        occasion.setEventTime(start);
        if (eventDurationInHours != null) {
            occasion.setEventEndTime(start.plusHours(eventDurationInHours));
        }

        publishOccasionToFacebook.doPublication(occasion);
    }

}
