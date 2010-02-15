package com.blackbox.server.message.publisher;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import com.blackbox.server.external.IUrlShortener;
import com.blackbox.message.Message;

import java.io.IOException;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class PublishMessageToTwitterUnitTest {

    @Mock
    IUrlShortener urlShortener;

    @Test
    public void buildTwitterMessageTest() throws IOException {


        PublishMessageToTwitter pmtt = new PublishMessageToTwitter();
        pmtt.urlShortener = urlShortener;

        Message message = new Message();
        message.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed consequat massa at est posuere at " +
                "interdum mauris tempor. Aliquam dignissim, augue sit amet sollicitudin ornare, magna odio tincidunt ligula," +
                " in accumsan purus lorem faucibus elit. Integer ultrices eros eget urna congue quis hendrerit lectus molestie. " +
                "Suspendisse fermentum vulputate felis sit amet volutpat. Maecenas eros nulla, commodo a tristique at, vulputate facilisis mi." +
                " Sed ut nisi eget est dapibus bibendum. Proin id mauris at augue consequat interdum id a lectus. Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit. Morbi auctor enim sit amet erat sodales in sagittis diam laoreet. Cras in lectus purus. " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Nunc molestie tempor euismod. Praesent viverra urna non mauris varius vitae tincidunt quam lacinia.");

        when(urlShortener.shortMessageUrl(message)).thenReturn("http://vb.ly/sdlfkjas");
        
        String msg = pmtt.buildTwitterMessage(message);
        assertNotNull(msg);
        assertEquals(140, msg.length());
    }


}
