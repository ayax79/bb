/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import static com.blackbox.Utils.applyGuid;
import static com.blackbox.Utils.getCurrentDateTime;
import com.blackbox.commerce.Inventory;
import com.blackbox.commerce.Merchant;
import com.blackbox.commerce.Product;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.commerce.ICommerceDao;
import com.blackbox.server.commerce.IInventoryDao;
import com.blackbox.server.commerce.event.SaveInventoryEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(SaveInventoryEvent.class)
@AsyncListener
public class SaveInventoryListener extends BaseBlackboxListener<SaveInventoryEvent, Inventory> {

    private IInventoryDao inventoryDao;
    private ICommerceDao commerceDao;

    @Override
    public void handle(SaveInventoryEvent inventoryEvent, ResultReference<Inventory> result) {
        //TODO:  handle updates
        final Inventory inventory = inventoryEvent.getType();
        Merchant merchant = commerceDao.loadMerchantByIntegrationGuid(inventory.getMerchant().getIntegrationGuid());
        for (Product product : inventory.getProducts()) {
            applyGuid(product);
            product.setLastUpdate(getCurrentDateTime());
            product.setSupplier(merchant);
            inventoryDao.saveProduct(product);
        }
    }

    public void setCommerceDao(ICommerceDao commerceDao) {
        this.commerceDao = commerceDao;
    }

    public void setInventoryDao(IInventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }
}