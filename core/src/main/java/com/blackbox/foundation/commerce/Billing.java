/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.commerce;

import com.blackbox.foundation.social.Address;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.yestech.lib.currency.Money;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@XStreamAlias("billing")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Billing {
    private Address address;
    private Money amount;

    public Billing() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Billing billing = (Billing) o;

        if (address != null ? !address.equals(billing.address) : billing.address != null) return false;
        //noinspection RedundantIfStatement
        if (amount != null ? !amount.equals(billing.amount) : billing.amount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BillingInfo{" +
                "address=" + address +
                ", amount=" + amount +
                '}';
    }
}
