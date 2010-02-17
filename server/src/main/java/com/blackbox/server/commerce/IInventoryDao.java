/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce;

import com.blackbox.foundation.commerce.Product;
import com.blackbox.foundation.commerce.Merchant;
import com.blackbox.foundation.social.Category;

import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public interface IInventoryDao {
    Product saveProduct(Product product);

    Product loadProductByGuid(String guid);

    List<Product> retrieveProductsByCategory(Category cat);

    void deleteAllProduct();

    List<Product> retrieveProductsByCategories(List<Category> category);

    List<Product> retrieveProductsBySupplier(Merchant suppler);

    void removeProductBySkuAndMerchant(String sku, String integrationGuid);

    List<Product> loadAllProducts();
}
