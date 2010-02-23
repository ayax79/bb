package com.blackbox.server.message.publisher;

import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.occasion.OccasionType;
import com.blackbox.server.BaseIntegrationTest;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author colin@blackboxrepublic.com
 */
public class PublishOccasionToFacebookIntegrationTest extends BaseIntegrationTest {

    @Resource
    PublishOccasionToFacebook publishOccasionToFacebook;

    private String guid = "29e180f8-5609-464e-b817-869c56f15e0b";

    @Test
    public void testPublishToFacebook() throws Exception {
        Occasion occasion = Occasion.createOccasion();
        occasion.setPublishToFacebook(true);
        occasion.setOccasionType(OccasionType.OPEN);
//        occasion.set(ExternalCredentials.buildExternalCredentials(ExternalCredentials.CredentialType.FACEBOOK, occasion.toEntityReference(), "johnnie.blackbox", "bla5ph3my"));
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
        occasion.setEventTime(new DateTime(2010, 1, 1, 12, 0, 0, 0));
        occasion.setEventEndTime(new DateTime(2010, 1, 1, 13, 0, 0, 0));

        publishOccasionToFacebook.doPublication(occasion);
    }
}
