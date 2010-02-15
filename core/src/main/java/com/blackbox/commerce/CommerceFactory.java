/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.commerce;

import com.blackbox.Utils;
import com.blackbox.user.User;

import java.util.UUID;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class CommerceFactory {

    public static Product createProduct() {
        final Product product = new Product();
        Utils.applyGuid(product);
        return product;
    }

    public static Merchant createMerchant() {
        final Merchant merchant = new Merchant();
        Utils.applyGuid(merchant);
        return merchant;
    }

    public static ShoppingCart createCart() {
        ShoppingCart cart = new ShoppingCart();
        Utils.applyGuid(cart);
        cart.setCartIdentifier(UUID.randomUUID());
        return cart;
    }

    public static CartItem createItem() {
        CartItem item = new CartItem();
        Utils.applyGuid(item);
        return item;
    }

    public static Invoice createInvoice() {
        Invoice invoice = new Invoice();
        Utils.applyGuid(invoice);
        return invoice;
    }

    public static Inventory createInventory() {
        Inventory inventory = new Inventory();
        return inventory;
    }

    public static Billing createBilling() {
        Billing billing = new Billing();
        return billing;
    }

    public static Split createSpilt(User user, Billing billing) {
        Split split = new Split(user, billing);
        return split;
    }
}
