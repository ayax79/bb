/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.commerce;

import com.blackbox.foundation.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang.StringUtils;
import org.yestech.lib.currency.Money;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static com.blackbox.foundation.EntityType.SHOPPINGCART;
import static org.yestech.lib.currency.CurrencyUtils.add;
import static org.yestech.lib.currency.CurrencyUtils.subtract;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XStreamAlias("cart")
@EntityTypeAnnotation(SHOPPINGCART)
@XmlRootElement(name = "shoppingCart")
public class ShoppingCart extends BaseEntity implements IEntity, IDated {
    private UUID cartIdentifier;
    private Map<String, CartItem> items;

    @XmlElement(name = "total")
    private Money total;

    public ShoppingCart() {
        super(SHOPPINGCART);
        total = new Money(BigDecimal.ZERO);
        items = new HashMap<String, CartItem>();
    }

    public UUID getCartIdentifier() {
        return cartIdentifier;
    }

    public void setCartIdentifier(UUID cartIdentifier) {
        this.cartIdentifier = cartIdentifier;
    }

    public Money getTotal() {
        return total;
    }

    public void setTotal(Money total) {
        this.total = total;
    }

    public String getCartTotalAmount() {
        return total.getAmount().toString();
    }

    public void setCartTotalAmount(String cartTotal) {
        if (total != null) {
            total = new Money(cartTotal, total.getLocale());
        } else {
            total = new Money(cartTotal);
        }
    }

    public String getCartTotalLocale() {
        Locale locale = total.getLocale();
        return locale.getLanguage() + ":" + locale.getCountry();
    }

    public void setCartTotalLocale(String cartTotalLocale) {
        String[] locale = StringUtils.split(cartTotalLocale, ":");
        if (total != null) {
            total = new Money(total.getAmount(), new Locale(locale[0], locale[1]));
        } else {
            total = new Money("0", new Locale(locale[0], locale[1]));
        }

    }

    public void addItem(CartItem item) {
        if (item != null) {
            total = add(total, item.getTotal());
            items.put(item.getProductGuid(), item);
        }
    }

    public void removeItem(CartItem item) {
        if (item != null) {
            total = subtract(total, item.getTotal());
            items.remove(item.getProductGuid());
        }
    }

    public void updateItem(CartItem item) {
        if (item != null) {
            CartItem currentItem = items.remove(item.getProductGuid());
            if (currentItem != null) {
                total = subtract(total, currentItem.getTotal());
            }
            total = add(total, item.getTotal());
            items.put(item.getProductGuid(), item);
        }
    }

    public Map<String, CartItem> getItems() {
        return items;
    }

    public void setItems(Map<String, CartItem> items) {
        this.items = items;
    }

    public static ShoppingCart createShoppingCart() {
        ShoppingCart cart = new ShoppingCart();
        Utils.applyGuid(cart);
        return cart;
    }
}
