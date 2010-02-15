/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce;

import com.blackbox.commerce.Product;
import com.blackbox.commerce.Merchant;
import com.blackbox.social.Category;

import java.util.List;
import java.util.Set;

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