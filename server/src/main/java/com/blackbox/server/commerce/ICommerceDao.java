/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce;

import com.blackbox.commerce.ShoppingCart;
import com.blackbox.commerce.Invoice;
import com.blackbox.commerce.Merchant;

import java.util.UUID;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public interface ICommerceDao {
    ShoppingCart saveCart(ShoppingCart cart);

    ShoppingCart loadCartByGuid(String cartGuid);

    ShoppingCart loadCartByGuid(UUID id);

//    void deletePrimary(UUID cartIdentifier);

    void delete(ShoppingCart cart);

    Merchant saveMerchant(Merchant merchant);

    Merchant loadMerchantByGuid(String guid);

    Merchant loadMerchantByIntegrationGuid(String guid);

    Invoice purchase(Invoice invoice);
}
