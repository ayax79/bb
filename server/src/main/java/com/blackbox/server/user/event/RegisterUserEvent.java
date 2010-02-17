/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user.event;

import com.blackbox.foundation.user.User;
import com.blackbox.foundation.billing.BillingInfo;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

import java.util.List;

/**
 *
 *
 */
@EventResultType(User.class)
public class RegisterUserEvent extends BaseEvent<User>
{
    private List<String> leechEmails;
    private BillingInfo billing;
    private String promoCodeGuid;

    public RegisterUserEvent(User user, List<String> leechEmails) {
        super(user);
        this.leechEmails = leechEmails;
    }

    public List<String> getLeechEmails() {
        return leechEmails;
    }

    public String getPromoCodeGuid() {
        return promoCodeGuid;
    }

    public void setPromoCodeGuid(String promoCodeGuid) {
        this.promoCodeGuid = promoCodeGuid;
    }
}