/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.commerce.event;

import com.blackbox.foundation.commerce.ShoppingCart;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 *
 *
 */
@EventResultType(ShoppingCart.class)
public class LoadShoppingCartEvent extends BaseEvent<Long> {
    private static final long serialVersionUID = -5888336450544721168L;

    public LoadShoppingCartEvent(long identifier) {
        super(identifier);
    }
}