/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.event;

import com.blackbox.commerce.ShoppingCart;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@EventResultType(ShoppingCart.class)
public class SaveCartEvent extends BaseEvent<ShoppingCart> {
    private static final long serialVersionUID = -2784179635638343441L;

    public SaveCartEvent(ShoppingCart cart) {
        super(cart);
    }
}
