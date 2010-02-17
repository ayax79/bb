/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.foundation.Utils;
import com.blackbox.foundation.commerce.Product;
import com.blackbox.server.commerce.IInventoryDao;
import com.blackbox.server.commerce.event.SaveProductEvent;
import com.blackbox.server.BaseBlackboxListener;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(SaveProductEvent.class)
public class SaveProductListener extends BaseBlackboxListener<SaveProductEvent, Product> {

    private IInventoryDao inventoryDao;

    @Override
    public void handle(SaveProductEvent productEvent, ResultReference<Product> result) {
        Utils.applyGuid(productEvent.getType());
        Product product = inventoryDao.saveProduct(productEvent.getType());
        result.setResult(product);
    }

    public void setInventoryDao(IInventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }
}