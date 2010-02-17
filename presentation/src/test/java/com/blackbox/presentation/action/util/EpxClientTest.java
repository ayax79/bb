package com.blackbox.presentation.action.util;

import static junit.framework.Assert.assertEquals;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import com.blackbox.foundation.billing.Money;

/**
 * @author A.J. Wright
 */
public class EpxClientTest {

    @Test
    public void parseTacTest() throws Exception {

        String xml = "<RESPONSE><FIELDS><FIELD KEY=\"TAC\">w5cx1M47Okx2MuFAisyQktzjnodLK4CD9oQXGbgoEd6FO94LUh5F/3AljH9iQkds</FIELD></FIELDS></RESPONSE>";
        String value = new DefaultEpxClient().parseTac(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        assertEquals("w5cx1M47Okx2MuFAisyQktzjnodLK4CD9oQXGbgoEd6FO94LUh5F/3AljH9iQkds", value);
    }

    @Test
    public void testFormatMoney() {
        Money money = new Money(32);
        String result = new DefaultEpxClient().formatMoney(money);
        assertEquals("32.00", result);
    }


}
