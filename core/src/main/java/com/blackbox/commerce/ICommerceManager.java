/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.commerce;

import java.util.UUID;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public interface ICommerceManager {

    ShoppingCart saveCart(ShoppingCart cart);

    ShoppingCart addCartItem(UUID cartIdentifier, CartItem cartItem);

    ShoppingCart retrieveCart(UUID cartIdentifier);

    ShoppingCart removeCartItem(UUID cartIdentifier, CartItem cartItem);

    ShoppingCart updateCartItem(UUID cartIdentifier, CartItem cartItem);

    Merchant saveMerchant(Merchant merchant);

    Merchant loadMerchant(String guid);

    Merchant disableMerchant(Merchant merchant);

    Merchant enableMerchant(Merchant merchant);

    Invoice purchase(Invoice invoice);
}
