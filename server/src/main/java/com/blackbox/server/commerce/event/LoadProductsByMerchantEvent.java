/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.commerce.event;

import com.blackbox.foundation.commerce.Merchant;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

import java.util.List;

@EventResultType(List.class)
public class LoadProductsByMerchantEvent extends BaseEvent<Merchant> {
    private static final long serialVersionUID = 2155078199242129805L;

    public LoadProductsByMerchantEvent(Merchant merchant) {
        super(merchant);
    }
}