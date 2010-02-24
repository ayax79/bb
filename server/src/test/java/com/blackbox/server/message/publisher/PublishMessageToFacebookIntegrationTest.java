package com.blackbox.server.message.publisher;

import com.blackbox.foundation.message.Message;
import com.blackbox.foundation.user.ExternalCredentials;
import com.blackbox.server.BaseIntegrationTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import javax.annotation.Resource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.*;

/**
 * @author colin@blackboxrepublic.com
 */
public class PublishMessageToFacebookIntegrationTest extends BaseIntegrationTest {

    @Resource
    PublishMessageToFacebook publishMessageToFacebook;

    private String facebookSessionId = "b4f23addee3430324e91a65f-100000720294855";

    @Ignore(value = "development in progress: blowing on: com.google.code.facebookapi.FacebookException: Session key invalid or no longer valid")
    @Test
    public void publishToFacebook() throws IOException {
        Message message = new Message();
        message.setPublishToFacebook(true);
        message.setBody("Posting to facebook " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date()));
        message.addExternalCredentials(ExternalCredentials.buildExternalCredentials(ExternalCredentials.CredentialType.FACEBOOK,
                message.toEntityReference(), facebookSessionId));
        String results = publishMessageToFacebook.doPublication(message);
        System.out.println("results = " + results);

    }
}
