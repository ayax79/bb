/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.foundation.commerce.Product;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.commerce.IInventoryDao;
import com.blackbox.server.commerce.event.LoadProductEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(LoadProductEvent.class)
public class LoadProductByGuidListener extends BaseBlackboxListener<LoadProductEvent, Product> {

    private IInventoryDao inventoryDao;

    @Override
    public void handle(LoadProductEvent loadProductEvent, ResultReference<Product> result) {
        Product product = inventoryDao.loadProductByGuid(loadProductEvent.getType());
        result.setResult(product);
    }

    public void setInventoryDao(IInventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }
}