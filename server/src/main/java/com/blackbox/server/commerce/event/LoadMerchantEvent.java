/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.commerce.event;

import com.blackbox.commerce.Merchant;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 *
 *
 */
@EventResultType(Merchant.class)
public class LoadMerchantEvent extends BaseEvent<String> {
    private static final long serialVersionUID = -3912635114696834295L;

    public LoadMerchantEvent(String guid) {
        super(guid);
    }
}