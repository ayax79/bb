/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.commerce;

import com.blackbox.social.Category;

import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public interface IInventoryManager {

    Product saveProduct(Product product);

    Product loadProduct(String guid);

    Product disableProduct(String guid);

    Product enableProduct(String guid);

    Inventory loadInventory();

    List<Product> loadProductsByMerchant(String supplier);

    List<Product> loadProductsByCategories(List<Category> categories);

    Inventory saveInventory(Inventory inventory);

    Inventory removeInventory(Inventory inventory);

}
