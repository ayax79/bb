package com.blackbox.server.message.publisher;

import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.testingutils.ExternalCredentialsFixture;
import org.junit.Test;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author colin@blackboxrepublic.com
 */
public class PublishMessageToFacebookIntegrationTest extends BaseIntegrationTest {

    @Resource
    PublishMessageToFacebook publishMessageToFacebook;

//    @Ignore(value = "development in progress: blowing on: com.google.code.facebookapi.FacebookException: Session key invalid or no longer valid")

    @Test
    public void publishToFacebook() throws Exception {
        Message message = new Message();
        message.setPublishToFacebook(true);
        message.setBody("Posting to facebook " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date()));
        message.addExternalCredentials(ExternalCredentials.buildFacebookExternalCredentials(message.toEntityReference(), ExternalCredentialsFixture.facebookPoster));
        String results = publishMessageToFacebook.doPublication(message);
        System.out.println("results = " + results);

    }
}
