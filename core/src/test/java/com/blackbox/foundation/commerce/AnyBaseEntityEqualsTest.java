package com.blackbox.foundation.commerce;

import com.blackbox.foundation.group.Group;
import com.blackbox.foundation.user.User;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

/**
 * @author colin@blackboxrepublic.com
 */
public class AnyBaseEntityEqualsTest {

    @Test
    public void testEquals() {
        CartItem cartItemOne = CartItem.createCartItem();
        assertEquals(cartItemOne, cartItemOne);
        CartItem cartItemTwo = CartItem.createCartItem();
        assertNotSame(cartItemOne, cartItemTwo);
        Group group = Group.createGroup();
        assertNotSame(cartItemOne, group);

        Group groupWithSameGuidAsCartItem = new Group();
        groupWithSameGuidAsCartItem.setGuid(cartItemOne.getGuid());
        assertNotSame(groupWithSameGuidAsCartItem, cartItemOne);

        User userOne = User.createUser();
        User userTwo = new User(userOne.getGuid());
        assertEquals(userOne, userTwo);

        userOne.setMaxPermanentVouches(userOne.getMaxPermanentVouches() + 31);

        assertEquals(userOne, userTwo);
    }

}
