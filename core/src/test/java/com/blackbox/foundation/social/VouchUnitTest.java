package com.blackbox.foundation.social;

import org.joda.time.DateTime;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: andrew
 * Date: Nov 27, 2009
 * Time: 1:46:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class VouchUnitTest {
    @Test
    public void isPermanent() {
        Vouch vouch = new Vouch();
        vouch.setExpirationDate(new DateTime().plusYears(100));
        assertTrue(Vouch.isPermanent(vouch));
    }

}
