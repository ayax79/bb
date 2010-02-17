/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.foundation.commerce.Inventory;
import com.blackbox.foundation.commerce.Product;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.commerce.IInventoryDao;
import com.blackbox.server.commerce.event.LoadInventoryEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(LoadInventoryEvent.class)
public class LoadInventoryListener extends BaseBlackboxListener<LoadInventoryEvent, Inventory> {

    private IInventoryDao inventoryDao;

    @Override
    public void handle(LoadInventoryEvent inventoryEvent, ResultReference<Inventory> result) {
//        final Merchant merchant = inventory.getMerchant();
//        for (Product product : inventory.getProducts()) {
//            product.setSupplier(merchant);
//            //TODO:  deletePrimary product by sku and merchant
//            inventoryDao.removeProductBySkuAndMerchant(product.getSku(), product.getSupplier().getIntegrationGuid());
////            inventoryDao.saveProduct(product);
//        }
        List<Product> products = inventoryDao.loadAllProducts();
        Inventory inventory = new Inventory();
        inventory.setProducts(products);
        result.setResult(inventory);
    }

    public void setInventoryDao(IInventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }
}