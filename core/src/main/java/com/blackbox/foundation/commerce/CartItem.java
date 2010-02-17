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

import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.EntityTypeAnnotation;
import com.blackbox.foundation.IDated;
import com.blackbox.foundation.IEntity;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang.StringUtils;
import org.yestech.lib.currency.CurrencyUtils;
import org.yestech.lib.currency.Money;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Locale;

import static com.blackbox.foundation.EntityType.CARTITEM;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XStreamAlias("cartItem")
@EntityTypeAnnotation(CARTITEM)
@XmlRootElement(name = "cartItem")
public class CartItem extends BaseEntity implements IEntity, IDated {
    private int quantity = 1;
    private Product product;
    private String merchantGuid;

    public CartItem() {
        super(CARTITEM);
        product = CommerceFactory.createProduct();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getMerchantGuid() {
        return merchantGuid;
    }

    public void setMerchantGuid(String merchantGuid) {
        this.merchantGuid = merchantGuid;
    }

    public String getProductGuid() {
        return product.getGuid();
    }

    public void setProductGuid(String productGuid) {
        product.setGuid(productGuid);
    }

    public String getProductAmount() {
        return product.getAmount().getAmount().toString();
    }

    public void setProductAmount(String productAmount) {
        if (product.getAmount() != null) {
            product.setAmount(new Money(productAmount, product.getAmount().getLocale()));
        } else {
            product.setAmount(new Money(productAmount));
        }
    }

    public String getProductAmountLocale() {
        Locale locale = product.getAmount().getLocale();
        return locale.getLanguage() + ":" + locale.getCountry();
    }

    public void setProductAmountLocale(String amountLocale) {
        String[] locale = StringUtils.split(amountLocale, ":");
        if (product.getAmount() != null) {
            product.setAmount(new Money(product.getAmount().getAmount(), new Locale(locale[0], locale[1])));
        } else {
            product.setAmount(new Money("0", new Locale(locale[0], locale[1])));
        }

    }

    public String getProductSku() {
        return product.getSku();
    }

    public void setProductSku(String productSku) {
        product.setSku(productSku);
    }

    public Money getTotal() {
        return CurrencyUtils.multiple(product.getAmount(), new Money(new BigDecimal(quantity)));
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "merchantGuid='" + merchantGuid + '\'' +
                ", quantity=" + quantity +
                ", product=" + product +
                "} " + super.toString();
    }

    public static CartItem createCartItem() {
        return CommerceFactory.createItem();
    }
}
