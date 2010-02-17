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
public interface IInventorySearchDao {

    Product retrieveProduct(String guid);

    List<Product> retrieveProductsByCategory(Category cat);

    List<Product> retrieveProductsByCategories(List<Category> category);

    List<Product> retrieveProductsByMerchant(Merchant suppler);

}