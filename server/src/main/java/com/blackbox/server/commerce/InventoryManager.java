/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce;

import com.blackbox.foundation.Status;
import com.blackbox.foundation.commerce.IInventoryManager;
import com.blackbox.foundation.commerce.Inventory;
import com.blackbox.foundation.commerce.Product;
import com.blackbox.foundation.commerce.Merchant;
import com.blackbox.server.commerce.event.*;
import com.blackbox.foundation.social.Category;
import org.yestech.event.multicaster.BaseServiceContainer;

import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@SuppressWarnings("unchecked")
//@Service("inventoryManager")
public class InventoryManager extends BaseServiceContainer implements IInventoryManager {

    private IInventoryDao inventoryDao;

    private ICommerceDao commerceDao;

    @Override
    public Product saveProduct(Product product) {
        return (Product) getEventMulticaster().process(new SaveProductEvent(product));
    }

    @Override
    public Product loadProduct(String guid) {
        return (Product) getEventMulticaster().process(new LoadProductEvent(guid));
    }

    @Override
    public Product disableProduct(String guid) {
        Product product = inventoryDao.loadProductByGuid(guid);
        product.setStatus(Status.DISABLED);
        return (Product) getEventMulticaster().process(new SaveProductEvent(product));
    }

    @Override
    public Product enableProduct(String guid) {
        Product product = inventoryDao.loadProductByGuid(guid);
        product.setStatus(Status.ENABLED);
        return (Product) getEventMulticaster().process(new SaveProductEvent(product));
    }

    @Override
    public Inventory loadInventory() {
        return (Inventory) getEventMulticaster().process(new LoadInventoryEvent());
    }

    @Override
    public List<Product> loadProductsByMerchant(String supplier) {
        Merchant merchant = commerceDao.loadMerchantByGuid(supplier);
        return (List<Product>) getEventMulticaster().process(new LoadProductsByMerchantEvent(merchant));
    }

    @Override
    public List<Product> loadProductsByCategories(List<Category> categories) {
        return (List<Product>) getEventMulticaster().process(new LoadProductsByCategoriesEvent(categories));
    }

    @Override
    public Inventory saveInventory(Inventory inventory) {
        return (Inventory) getEventMulticaster().process(new SaveInventoryEvent(inventory));
    }

    @Override
    public Inventory removeInventory(Inventory inventory) {
        return (Inventory) getEventMulticaster().process(new RemoveInventoryEvent(inventory));
    }

    public void setInventoryDao(IInventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    public void setCommerceDao(ICommerceDao commerceDao) {
        this.commerceDao = commerceDao;
    }
}
