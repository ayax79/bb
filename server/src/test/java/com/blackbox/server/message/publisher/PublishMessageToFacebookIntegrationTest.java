package com.blackbox.server.message.publisher;

import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.testingutils.ExternalCredentialsFixture;
import org.junit.Test;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author colin@blackboxrepublic.com
 */
public class PublishMessageToFacebookIntegrationTest extends BaseIntegrationTest {

    @Resource
    PublishMessageToFacebook publishMessageToFacebook;

    @Test
    public void publishToFacebook() throws Exception {
        Message message = new Message();
        message.setPublishToFacebook(true);
        message.setBody("Posting to facebook " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date()));
        message.addExternalCredentials(ExternalCredentials.buildFacebookExternalCredentials(message.toEntityReference(), ExternalCredentialsFixture.facebookPoster));
        String results = publishMessageToFacebook.doPublication(message);
        assertNotNull(results);
        assertTrue("The results didn't seem like a real identifier to me " + results + " @ see http://wiki.developers.facebook.com/index.php/Error_codes ", results.contains("_"));
    }
}
