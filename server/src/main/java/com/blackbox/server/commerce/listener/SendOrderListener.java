/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.foundation.commerce.Invoice;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.commerce.ICommerceDao;
import com.blackbox.server.commerce.event.PurchaseCartEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(PurchaseCartEvent.class)
@AsyncListener
public class SendOrderListener extends BaseBlackboxListener<PurchaseCartEvent, Invoice> {

    private ICommerceDao commerceDao;

    @Override
    public void handle(PurchaseCartEvent purchaseCartEvent, ResultReference<Invoice> result) {
        throw new UnsupportedOperationException("not yet implemented...");
    }

    public void setCommerceDao(ICommerceDao commerceDao) {
        this.commerceDao = commerceDao;
    }
}