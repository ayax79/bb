/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.commerce.CartItem;
import com.blackbox.commerce.Invoice;
import com.blackbox.commerce.ShoppingCart;
import com.blackbox.commerce.Split;
import com.blackbox.server.commerce.event.PurchaseCartEvent;
import com.blackbox.server.social.INetworkDao;
import com.blackbox.social.Relationship;
import com.blackbox.user.User;
import com.blackbox.EntityReference;
import static com.google.common.collect.Lists.newArrayList;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.yestech.event.ResultReference;
import org.yestech.lib.currency.Money;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@RunWith(JMock.class)
public class SaveCartRelationshipsListenerUnitTest {
    private Mockery context = new Mockery();
    private SaveCartRelationshipsListener listener;

    @Before
    public void setUp() {
        listener = new SaveCartRelationshipsListener();
    }

    @Test
    @Ignore
    public void testSaveCart() {
        final INetworkDao networkDao = context.mock(INetworkDao.class, "networkDao");
        Invoice invoice = new Invoice();
        PurchaseCartEvent event = new PurchaseCartEvent(invoice);
        List<Split> splits = newArrayList();
        Split one = new Split();
        final User user1 = User.createUser();
        one.setFirst(user1);
        splits.add(one);

        Split two = new Split();
        User user2 = User.createUser();
        two.setFirst(user2);
        splits.add(two);
        invoice.setSplits(splits);

        ShoppingCart cart = new ShoppingCart();
        final CartItem item1 = CartItem.createCartItem();
        int amount1 = 100;
        item1.getProduct().setAmount(new Money(amount1));
        item1.setQuantity(6);
        cart.addItem(item1);

        CartItem item2 = CartItem.createCartItem();
        int amount2 = 500;
        item2.setQuantity(1);
        item2.getProduct().setAmount(new Money(amount2));
        cart.addItem(item2);

        invoice.setCart(cart);

        int weight = 100;

        final Relationship r1 = new Relationship();
        r1.setFromEntity(user1.toEntityReference());
        r1.setToEntity(item1.getProduct().toEntityReference());
        r1.setWeight(weight);

        final Relationship r2 = new Relationship();
        r2.setFromEntity(user1.toEntityReference());
        r2.setToEntity(item2.getProduct().toEntityReference());
        r2.setWeight(weight);

        final Relationship r3 = new Relationship();
        r3.setFromEntity(user2.toEntityReference());
        r3.setToEntity(item1.getProduct().toEntityReference());
        r3.setWeight(weight);

        final Relationship r4 = new Relationship();
        r4.setFromEntity(user2.toEntityReference());
        r4.setToEntity(item2.getProduct().toEntityReference());
        r4.setWeight(weight);

        final Relationship r5 = new Relationship();
        r5.setFromEntity(user1.toEntityReference());
        r5.setToEntity(user2.toEntityReference());
        r5.setWeight(weight);

        final Relationship r6 = new Relationship();
        r6.setFromEntity(user2.toEntityReference());
        r6.setToEntity(user1.toEntityReference());
        r6.setWeight(weight);


        final Relationship r7 = new Relationship();
        r7.setFromEntity(item1.getProduct().toEntityReference());
        r7.setToEntity(item2.getProduct().toEntityReference());
        r7.setWeight(weight);

        final Relationship r8 = new Relationship();
        r8.setFromEntity(item2.getProduct().toEntityReference());
        r8.setToEntity(item1.getProduct().toEntityReference());
        r8.setWeight(weight);

        final Relationship r9 = new Relationship();
        r9.setToEntity(user1.toEntityReference());
        r9.setFromEntity(item1.getProduct().toEntityReference());
        r9.setWeight(weight);

        final Relationship r10 = new Relationship();
        r10.setToEntity(user1.toEntityReference());
        r10.setFromEntity(item2.getProduct().toEntityReference());
        r10.setWeight(weight);

        final Relationship r11 = new Relationship();
        r11.setToEntity(user2.toEntityReference());
        r11.setFromEntity(item1.getProduct().toEntityReference());
        r11.setWeight(weight);

        final Relationship r12 = new Relationship();
        r12.setToEntity(user2.toEntityReference());
        r12.setFromEntity(item2.getProduct().toEntityReference());
        r12.setWeight(weight);

        final List<Relationship> relationships = newArrayList(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);

        context.checking(new Expectations() {
            {
                for (Relationship r : relationships) {
                    oneOf(networkDao).save((Relationship) with(allOf(
                            hasProperty("fromEntity", allOf(any(EntityReference.class),
                                    hasProperty("guid", equal(r.getFromEntity().getGuid())),
                                    hasProperty("ownerTypes", equal(r.getFromEntity().getOwnerType())))),
                            hasProperty("toEntity", allOf(any(EntityReference.class),
                                    hasProperty("guid", equal(r.getToEntity().getGuid())),
                                    hasProperty("ownerTypes", equal(r.getToEntity().getOwnerType()))))
                    )));
                }
            }
        });

        ResultReference<Invoice> result = new ResultReference<Invoice>();
        result.setResult(invoice);
        listener.setNetworkDao(networkDao);
        listener.setWeight(weight);
        listener.handle(event, result);
    }
}
