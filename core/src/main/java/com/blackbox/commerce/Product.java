/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.commerce;

import com.blackbox.BaseEntity;
import com.blackbox.EntityTypeAnnotation;
import com.blackbox.IEntity;
import com.blackbox.Utils;
import com.blackbox.security.IAsset;
import com.blackbox.social.Category;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.yestech.lib.currency.Money;
import org.yestech.lib.xml.XmlUtils;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Locale;

import static com.blackbox.EntityType.PRODUCT;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@EntityTypeAnnotation(PRODUCT)
@XStreamAlias("product")
@XmlRootElement(name = "product")
public class Product extends BaseEntity implements IAsset, IEntity {
    @XStreamAlias("total")
    private Money amount;

    private Merchant supplier;

    private String tinyImageLocation;
    private String largeImageLocation;
    private String supplierTinyImageLocation;
    private String supplierLargeImageLocation;
    private String description;

    private List<Category> categories;

    private DateTime lastUpdate;
    private String sku;
    private int quantity;
    private int currentQuantity;

    public Product() {
        super(PRODUCT);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @XmlJavaTypeAdapter(com.blackbox.util.JodaDateTimeXmlAdapter.class)
    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(DateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getProductAmount() {
        return amount.getAmount().toString();
    }

    public void setProductAmount(String productAmount) {
        if (amount != null) {
            amount = new Money(productAmount, amount.getLocale());
        } else {
            amount = new Money(productAmount);
        }
    }

    public String getProductAmountLocale() {
        Locale locale = amount.getLocale();
        return locale.getLanguage() + ":" + locale.getCountry();
    }

    public void setProductAmountLocale(String amountLocale) {
        String[] locale = StringUtils.split(amountLocale, ":");
        if (amount != null) {
            amount = new Money(amount.getAmount(), new Locale(locale[0], locale[1]));
        } else {
            amount = new Money("0", new Locale(locale[0], locale[1]));
        }

    }

    public Merchant getSupplier() {
        return supplier;
    }

    public void setSupplier(Merchant supplier) {
        this.supplier = supplier;
    }

    public String getSupplierTinyImageLocation() {
        return supplierTinyImageLocation;
    }

    public void setSupplierTinyImageLocation(String supplierTinyImageLocation) {
        this.supplierTinyImageLocation = supplierTinyImageLocation;
    }

    public String getSupplierLargeImageLocation() {
        return supplierLargeImageLocation;
    }

    public void setSupplierLargeImageLocation(String supplierLargeImageLocation) {
        this.supplierLargeImageLocation = supplierLargeImageLocation;
    }

    public String getTinyImageLocation() {
        return tinyImageLocation;
    }

    public void setTinyImageLocation(String tinyImageLocation) {
        this.tinyImageLocation = tinyImageLocation;
    }

    public String getLargeImageLocation() {
        return largeImageLocation;
    }

    public void setLargeImageLocation(String largeImageLocation) {
        this.largeImageLocation = largeImageLocation;
    }

    @Override
    public String toString() {
        return "Product{" +
                "super=" + super.toString() +
                "amount=" + amount +
                ", supplier=" + supplier +
                ", tinyImageLocation='" + tinyImageLocation + '\'' +
                ", largeImageLocation='" + largeImageLocation + '\'' +
                ", categories=" + categories +
                ", lastUpdate=" + lastUpdate +
                ", sku='" + sku + '\'' +
                '}';
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public static Product createProduct() {
        return CommerceFactory.createProduct();
    }

    /**
     * Used to create a Product from an xml
     *
     * @param xml
     * @return
     */
    public static Product valueOf(String xml) {
        return XmlUtils.fromXml(xml, Utils.xmlAliases());
    }
}
