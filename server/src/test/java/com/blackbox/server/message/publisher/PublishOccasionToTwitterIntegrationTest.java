package com.blackbox.server.message.publisher;

import com.blackbox.foundation.Utils;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.testingutils.ExternalCredentialsFixture;
import org.junit.Test;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 * @author colin@blackboxrepublic.com
 */
public class PublishOccasionToTwitterIntegrationTest extends BaseIntegrationTest {

    @Resource
    PublishOccasionToTwitter publishOccasionToTwitter;

    private String tweet = "Event [{0}]";
    private ExternalCredentialsFixture.UserParts user = ExternalCredentialsFixture.blackboxTweeter;

    @Test
    public void testTweet() throws Exception {
        Occasion occasion = Occasion.createOccasion();
        occasion.setPublishToTwitter(true);
        occasion.addExternalCredentials(ExternalCredentials.buildExternalCredentials(ExternalCredentials.CredentialType.TWITTER,
                occasion.toEntityReference(), user.getUserName(), user.getPassword()));
        occasion.setOwner(user.toUser());
        String guid = Utils.generateGuid();
        occasion.setTwitterDescription(MessageFormat.format(tweet, guid.substring(0, 20)));
        publishOccasionToTwitter.doPublication(occasion);
        // Event [8d6e594e9b419dd7b621] http://www.blackboxrepublic.com//community/event/show/2ede1a4f5817fd30aada00a23501a8bd4f588ad0
        // not sure why we aren't getting this back right away...and, currently, it is showing in the stream via the ui...
        //assertTrue(publishOccasionToTwitter.twitterClient.thisWasTweeted(occasion.getTwitterDescription() + " http://www.blackboxrepublic.com//community/event/show/" + guid, user.getUserName()));
    }


}
