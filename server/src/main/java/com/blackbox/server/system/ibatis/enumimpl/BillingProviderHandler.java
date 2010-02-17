package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.foundation.billing.BillingInfo;
import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;

public class BillingProviderHandler extends OrdinalEnumTypeHandler {

    public BillingProviderHandler() {
        super(BillingInfo.BillingProvider.class);
    }

}
