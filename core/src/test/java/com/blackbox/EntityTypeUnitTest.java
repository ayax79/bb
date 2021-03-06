package com.blackbox;

import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.commerce.Merchant;
import com.blackbox.foundation.user.User;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 *
 */
public class EntityTypeUnitTest {

    @Test
    public void testValueOfWithClass() {
        EntityType result = EntityType.valueOf(User.class);
        assertEquals(EntityType.USER, result);

        result = EntityType.valueOf(Merchant.class);
        assertEquals(EntityType.MERCHANT, result);
    }


}
