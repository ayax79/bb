/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.foundation.commerce.Product;
import com.blackbox.server.commerce.IInventorySearchDao;
import com.blackbox.server.commerce.event.LoadProductsByMerchantEvent;
import com.blackbox.server.BaseBlackboxListener;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(LoadProductsByMerchantEvent.class)
public class LoadProductsByMerchantListener extends BaseBlackboxListener<LoadProductsByMerchantEvent, List<Product>> {

    
    private IInventorySearchDao inventorySearchDao;

    @Override
    public void handle(LoadProductsByMerchantEvent loadProductsByMerchantEvent, ResultReference<List<Product>> result) {
        List<Product> products = inventorySearchDao.retrieveProductsByMerchant(loadProductsByMerchantEvent.getType());
        result.setResult(products);
    }


    @Resource( name = "inventorySearchDao")
    public void setInventorySearchDao(IInventorySearchDao inventorySearchDao) {
        this.inventorySearchDao = inventorySearchDao;
    }
}