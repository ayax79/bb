package com.blackbox.server.external;

import com.blackbox.server.BaseContextAwareIntegrationTest;
import org.junit.Test;

import static junit.framework.Assert.*;

import javax.annotation.Resource;

/**
 * @author colin@blackboxrepublic.com
 */
public class VioletBlueUrlShortenerIntegrationTest extends BaseContextAwareIntegrationTest {

    @Resource
    VioletBlueUrlShortener shortener;

    @Test
    public void testShortenBadUrlStaysParameterUrl() throws Exception {
        String badUrl = "http: //bad.org/super$bad";
        assertEquals(badUrl, shortener.shorten(badUrl));
    }

    // when the url shortener returns 'Please log in' instead of the shortened url, we want to just return the input url

    @Test
    public void testShortenBadProviderStaysParameterUrl() throws Exception {
        String goodUrl = "http://www.blackboxrepublic.com/community/event/show/ade1665026defa6abc2580e5bc8bf96695ed44b4";
        assertEquals("This failure means violet blue is back up and working!!! wooo hooo! delete this now...", goodUrl, shortener.shorten(goodUrl));

    }
}
