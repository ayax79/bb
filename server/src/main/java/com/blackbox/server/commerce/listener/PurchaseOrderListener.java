/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.foundation.commerce.Invoice;
import com.blackbox.server.commerce.ICommerceDao;
import com.blackbox.server.commerce.event.PurchaseCartEvent;
import com.blackbox.server.BaseBlackboxListener;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(PurchaseCartEvent.class)
public class PurchaseOrderListener extends BaseBlackboxListener<PurchaseCartEvent, Invoice> {

    private ICommerceDao commerceDao;

    @Override
    public void handle(PurchaseCartEvent purchaseCartEvent, ResultReference<Invoice> result) {
        Invoice invoice = commerceDao.purchase(purchaseCartEvent.getType());
        result.setResult(invoice);
    }

    public void setCommerceDao(ICommerceDao commerceDao) {
        this.commerceDao = commerceDao;
    }
}
