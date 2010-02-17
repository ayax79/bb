/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.commerce;

import com.blackbox.foundation.Utils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.yestech.lib.xml.XmlUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XStreamAlias("inventory")
@XmlRootElement(name = "inventory")
public class Inventory implements Serializable {

    @XmlList
    private List<Product> products;

    @XmlElement(name = "merchant")
    private Merchant merchant;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "products=" + products +
                ", merchant=" + merchant +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;

        Inventory inventory = (Inventory) o;

        if (merchant != null ? !merchant.equals(inventory.merchant) : inventory.merchant != null) return false;
        if (products != null ? !products.equals(inventory.products) : inventory.products != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = products != null ? products.hashCode() : 0;
        result = 31 * result + (merchant != null ? merchant.hashCode() : 0);
        return result;
    }

    /**
     * Used to create a Inventory from an xml
     *
     * @param xml
     * @return
     */
    public static Inventory valueOf(String xml) {
        return XmlUtils.fromXml(xml, Utils.xmlAliases());
    }
}
