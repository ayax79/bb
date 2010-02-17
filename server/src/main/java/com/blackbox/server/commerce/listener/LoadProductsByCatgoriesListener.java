/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.foundation.commerce.Product;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.commerce.IInventorySearchDao;
import com.blackbox.server.commerce.event.LoadProductsByCategoriesEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(LoadProductsByCategoriesEvent.class)
public class LoadProductsByCatgoriesListener extends BaseBlackboxListener<LoadProductsByCategoriesEvent, List<Product>> {

    private IInventorySearchDao inventorySearchDao;

    @Override
    public void handle(LoadProductsByCategoriesEvent loadProductsByTagsEvent, ResultReference<List<Product>> result) {
        List<Product> products = inventorySearchDao.retrieveProductsByCategories(loadProductsByTagsEvent.getCategories());
        result.setResult(products);
    }

    @Resource( name = "inventorySearchDao")
    public void setInventorySearchDao(IInventorySearchDao inventorySearchDao) {
        this.inventorySearchDao = inventorySearchDao;
    }
}
