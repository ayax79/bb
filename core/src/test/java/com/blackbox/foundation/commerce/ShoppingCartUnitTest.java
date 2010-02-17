/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.commerce;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.yestech.lib.currency.Money;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class ShoppingCartUnitTest {
    private ShoppingCart cart;

    @Test
    public void testAddOneItem() {
        cart = ShoppingCart.createShoppingCart();
        Money testTotal = new Money(100);
        CartItem item = CartItem.createCartItem();
        int amount = 100;
        item.getProduct().setAmount(new Money(amount));
        item.setQuantity(1);
        cart.addItem(item);
        Money total = cart.getTotal();
        assertEquals(1, cart.getItems().size());
        assertEquals(testTotal, total);
    }

    @Test
    public void testAddMultipleItems() {
        cart = ShoppingCart.createShoppingCart();
        Money testTotal = new Money(600);
        CartItem item1 = CartItem.createCartItem();
        int amount1 = 100;
        item1.getProduct().setAmount(new Money(amount1));
        cart.addItem(item1);

        CartItem item2 = CartItem.createCartItem();
        int amount2 = 500;
        item2.getProduct().setAmount(new Money(amount2));
        item2.setQuantity(1);
        cart.addItem(item2);
        Money total = cart.getTotal();
        assertEquals(2, cart.getItems().size());
        assertEquals(testTotal, total);
    }

    @Test
    public void testAddMultipleItemsDifferentAmounts() {
        cart = ShoppingCart.createShoppingCart();
        Money testTotal = new Money(1100);
        CartItem item1 = CartItem.createCartItem();
        int amount1 = 100;
        item1.getProduct().setAmount(new Money(amount1));
        item1.setQuantity(6);
        cart.addItem(item1);

        CartItem item2 = CartItem.createCartItem();
        int amount2 = 500;
        item2.setQuantity(1);
        item2.getProduct().setAmount(new Money(amount2));
        cart.addItem(item2);
        Money total = cart.getTotal();
        assertEquals(2, cart.getItems().size());
        assertEquals(testTotal, total);
    }

    @Test
    public void testRemoveItem() {
        cart = ShoppingCart.createShoppingCart();
        Money testTotal = new Money(600);
        CartItem item1 = CartItem.createCartItem();
        int amount1 = 100;
        item1.getProduct().setAmount(new Money(amount1));
        item1.setQuantity(6);
        cart.addItem(item1);

        CartItem item2 = CartItem.createCartItem();
        int amount2 = 500;
        item2.setQuantity(1);
        item2.getProduct().setAmount(new Money(amount2));
        cart.addItem(item2);
        assertEquals(2, cart.getItems().size());

        cart.removeItem(item2);
        Money total = cart.getTotal();

        assertEquals(1, cart.getItems().size());
        assertEquals(testTotal, total);
    }

    @Test
    public void testUpdateItem() {
        cart = ShoppingCart.createShoppingCart();
        Money testTotal = new Money(5600);
        CartItem item1 = CartItem.createCartItem();
        int amount1 = 100;
        item1.getProduct().setAmount(new Money(amount1));
        item1.setQuantity(6);
        cart.addItem(item1);

        CartItem item2 = CartItem.createCartItem();
        int amount2 = 500;
        item2.setQuantity(1);
        item2.getProduct().setAmount(new Money(amount2));
        cart.addItem(item2);
        assertEquals(2, cart.getItems().size());

        CartItem updateItem = new CartItem();
        updateItem.setGuid(item2.getGuid());
        int amount = 500;
        updateItem.setQuantity(1);
        updateItem.getProduct().setAmount(new Money(amount));
        updateItem.setProductGuid(item2.getProductGuid());
        updateItem.setQuantity(10);
        cart.updateItem(updateItem);
        Money total = cart.getTotal();

        assertEquals(2, cart.getItems().size());
        assertEquals(testTotal, total);
    }
}
