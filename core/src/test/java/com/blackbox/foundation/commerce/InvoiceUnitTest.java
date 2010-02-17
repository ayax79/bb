/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.commerce;

import com.blackbox.foundation.social.Address;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.yestech.lib.currency.Money;
import org.yestech.lib.i18n.CountryEnum;
import org.yestech.lib.i18n.USStateEnum;
import org.yestech.lib.xml.XmlUtils;

import java.util.UUID;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class InvoiceUnitTest {

    @Test
    public void testInvoiceXml() {
        final Invoice invoice = CommerceFactory.createInvoice();
        invoice.setGuid("guid");
        Address address = new Address();
        address.setAddress1("address1");
        address.setAddress2("address2");
        address.setCountry(CountryEnum.UNITED_STATES);
        address.setState(USStateEnum.ARKANSAS.name());
        address.setZipCode("99999");
        invoice.setShipping(address);

        final ShoppingCart cart = CommerceFactory.createCart();
        cart.setGuid(null);
        cart.setCartIdentifier(UUID.fromString("ec9fb930-6c16-46d3-8747-e286d1512c2"));

        CartItem item1 = CommerceFactory.createItem();
        item1.setGuid("2b5149c13a8e0b197647f09584d270099ba926a2");
        int amount1 = 100;
        item1.getProduct().setAmount(new Money(amount1));
        item1.getProduct().setGuid("d6fd775ffc84fe6615d1dcc07be0752b9f77944a");
        item1.setQuantity(6);
        cart.addItem(item1);

        CartItem item2 = CommerceFactory.createItem();
        item2.setGuid("1a474316d7b32293772219dfee939452a3113c14");
        item2.getProduct().setGuid("d6fd775ffc84fe6615d1dcc07be0752b9f77944a");
        int amount2 = 500;
        item2.setQuantity(1);
        item2.getProduct().setAmount(new Money(amount2));
        cart.addItem(item2);

        invoice.setCart(cart);
        final String xml = XmlUtils.toXml(invoice, true);
        final String testString = "<invoice>\n" +
                "  <guid>guid</guid>\n" +
                "  <cart>\n" +
                "    <ownerType>SHOPPINGCART</ownerType>\n" +
                "    <cartIdentifier>ec9fb930-6c16-46d3-8747-0e286d1512c2</cartIdentifier>\n" +
                "    <items>\n" +
                "      <entry>\n" +
                "        <string>d6fd775ffc84fe6615d1dcc07be0752b9f77944a</string>\n" +
                "        <cartItem>\n" +
                "          <guid>1a474316d7b32293772219dfee939452a3113c14</guid>\n" +
                "          <ownerType>CARTITEM</ownerType>\n" +
                "          <quantity>1</quantity>\n" +
                "          <product>\n" +
                "            <guid>d6fd775ffc84fe6615d1dcc07be0752b9f77944a</guid>\n" +
                "            <ownerType>PRODUCT</ownerType>\n" +
                "            <total>\n" +
                "              <amount>500</amount>\n" +
                "              <locale>en_US</locale>\n" +
                "              <curreny>USD</curreny>\n" +
                "            </total>\n" +
                "            <quantity>0</quantity>\n" +
                "            <currentQuantity>0</currentQuantity>\n" +
                "          </product>\n" +
                "        </cartItem>\n" +
                "      </entry>\n" +
                "    </items>\n" +
                "    <total>\n" +
                "      <amount>1100</amount>\n" +
                "      <locale>en_US</locale>\n" +
                "      <curreny>USD</curreny>\n" +
                "    </total>\n" +
                "  </cart>\n" +
                "  <shipping>\n" +
                "    <address1>address1</address1>\n" +
                "    <address2>address2</address2>\n" +
                "    <state>ARKANSAS</state>\n" +
                "    <country>UNITED_STATES</country>\n" +
                "    <zipCode>99999</zipCode>\n" +
                "  </shipping>\n" +
                "</invoice>";

        assertEquals(testString, xml);
    }
}
