/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.commerce.Inventory;
import com.blackbox.commerce.Merchant;
import com.blackbox.commerce.Product;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.commerce.IInventoryDao;
import com.blackbox.server.commerce.event.RemoveInventoryEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(RemoveInventoryEvent.class)
public class RemoveInventoryListener extends BaseBlackboxListener<RemoveInventoryEvent, Inventory> {

    private IInventoryDao inventoryDao;

    @Override
    public void handle(RemoveInventoryEvent inventoryEvent, ResultReference<Inventory> result) {
        final Inventory inventory = inventoryEvent.getType();
        final Merchant merchant = inventory.getMerchant();
        for (Product product : inventory.getProducts()) {
            product.setSupplier(merchant);
            //TODO:  deletePrimary product by sku and merchant
            inventoryDao.removeProductBySkuAndMerchant(product.getSku(), product.getSupplier().getIntegrationGuid());
//            inventoryDao.saveProduct(product);
        }
    }

    public void setInventoryDao(IInventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }
}