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

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.social.Address;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XmlRootElement(name = "invoice")
@XStreamAlias("invoice")
public class Invoice extends BBPersistentObject {
    private ShoppingCart cart;
    private Collection<Split> splits;

    private Address shipping;

    public Invoice() {
        shipping = new Address();
    }


    public Address getShipping() {
        return shipping;
    }

    public void setShipping(Address shipping) {
        this.shipping = shipping;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public Collection<Split> getSplits() {
        return splits;
    }

    public void setSplits(Collection<Split> splits) {
        this.splits = splits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invoice)) return false;
        if (!super.equals(o)) return false;

        Invoice invoice = (Invoice) o;

        if (cart != null ? !cart.equals(invoice.cart) : invoice.cart != null) return false;
        //noinspection RedundantIfStatement
        if (splits != null ? !splits.equals(invoice.splits) : invoice.splits != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (cart != null ? cart.hashCode() : 0);
        result = 31 * result + (splits != null ? splits.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "cart=" + cart +
                ", splits=" + splits +
                '}';
    }
}
