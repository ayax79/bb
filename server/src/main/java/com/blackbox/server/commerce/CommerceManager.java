/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce;

import com.blackbox.foundation.Status;
import com.blackbox.foundation.commerce.*;
import com.blackbox.server.commerce.event.LoadMerchantEvent;
import com.blackbox.server.commerce.event.PurchaseCartEvent;
import com.blackbox.server.commerce.event.SaveCartEvent;
import com.blackbox.server.commerce.event.SaveMerchantEvent;
import org.yestech.cache.ICacheManager;
import org.yestech.event.multicaster.BaseServiceContainer;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@SuppressWarnings("unchecked")
// @Service("commerceManager")
public class CommerceManager extends BaseServiceContainer implements ICommerceManager {

    private ICacheManager<String, ShoppingCart> cache;

    public ICacheManager getCache() {
        return cache;
    }

    @Resource(name = "shoppingCartCache")
    public void setCache(ICacheManager cache) {
        this.cache = cache;
    }

    @Override
    public ShoppingCart saveCart(ShoppingCart cart) {
        return (ShoppingCart) getEventMulticaster().process(new SaveCartEvent(cart));
    }

    @Override
    public ShoppingCart addCartItem(UUID cartIdentifier, CartItem cartItem) {
        ShoppingCart cart = retrieveCart(cartIdentifier);
        cart.addItem(cartItem);
        return cart;
    }

    @Override
    public ShoppingCart retrieveCart(UUID cartIdentifier) {
        ShoppingCart cart = cache.get(cartIdentifier.toString());
        if (cart == null) {
            cart = CommerceFactory.createCart();
            cache.put(cart.getCartIdentifier().toString(), cart);
        }
        return cart;
    }

    @Override
    public ShoppingCart removeCartItem(UUID cartIdentifier, CartItem cartItem) {
        ShoppingCart cart = retrieveCart(cartIdentifier);
        cart.removeItem(cartItem);
        return cart;
    }

    @Override
    public ShoppingCart updateCartItem(UUID cartIdentifier, CartItem cartItem) {
        ShoppingCart cart = retrieveCart(cartIdentifier);
        cart.updateItem(cartItem);
        return cart;
    }

    @Override
    public Merchant saveMerchant(Merchant merchant) {
        return (Merchant) getEventMulticaster().process(new SaveMerchantEvent(merchant));
    }

    @Override
    public Merchant loadMerchant(String guid) {
        return (Merchant) getEventMulticaster().process(new LoadMerchantEvent(guid));
    }

    @Override
    public Merchant disableMerchant(Merchant merchant) {
        merchant.setStatus(Status.DISABLED);
        return (Merchant) getEventMulticaster().process(new SaveMerchantEvent(merchant));
    }

    @Override
    public Merchant enableMerchant(Merchant merchant) {
        merchant.setStatus(Status.ENABLED);
        return (Merchant) getEventMulticaster().process(new SaveMerchantEvent(merchant));
    }

    @Override
    public Invoice purchase(Invoice invoice) {
        return (Invoice) getEventMulticaster().process(new PurchaseCartEvent(invoice));
    }
}
