/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.commerce.event;

import com.blackbox.commerce.Product;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 *
 *
 */
@EventResultType(Product.class)
public class LoadProductEvent extends BaseEvent<String> {
    private static final long serialVersionUID = -3903494967549465528L;

    public LoadProductEvent(String guid) {
        super(guid);
    }
}