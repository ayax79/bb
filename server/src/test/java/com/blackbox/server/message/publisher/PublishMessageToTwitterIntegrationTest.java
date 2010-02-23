package com.blackbox.server.message.publisher;

import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.testingutils.ExternalCredentialsFixture;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertTrue;

/**
 * @author colin@blackboxrepublic.com
 */
public class PublishMessageToTwitterIntegrationTest extends BaseIntegrationTest {

    @Resource
    private PublishMessageToTwitter publishMessageToTwitter;

    private String tweet = "Working from bonfire @ ";
    private String username = ExternalCredentialsFixture.blackboxTweeter.getUserName();
    private String password = ExternalCredentialsFixture.blackboxTweeter.getPassword();

    @Test
    public void testTweet() throws IOException {
        Message message = new Message();
        message.setBody(tweet + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date()));
        message.setPublishToTwitter(true);
        message.setCreds(ExternalCredentials.buildExternalCredentials(ExternalCredentials.CredentialType.TWITTER,
                message.toEntityReference(), username, password));
        publishMessageToTwitter.doPublication(message);
        assertTrue(publishMessageToTwitter.twitterClient.thisWasTweeted(tweet, username));
    }

}
