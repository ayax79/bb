package com.blackbox.billing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;

/**
 * @author A.J. Wright
 */
public class MoneyUnitTest {

    @Test
    public void testMarshall() throws JAXBException {
        Money money = new Money(23232332);
        JAXBContext ctx = JAXBContext.newInstance(Money.class);
        Marshaller marshaller = ctx.createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.marshal(money, writer);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        Object result = unmarshaller.unmarshal(new StringReader(writer.toString()));
        assertNotNull(result);
        assertEquals(money, result);
    }

    @Test
    public void format() {
        Money money = new Money(50);
        String result = money.format(Locale.US);
        assertNotNull(result);
        assertEquals("$50.00", result);
    }

}
