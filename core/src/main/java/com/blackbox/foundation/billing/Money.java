package com.blackbox.foundation.billing;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class Money implements Serializable {


    protected BigDecimal amount;

    public Money() {
    }

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public Money(long amount) {
        this.amount = new BigDecimal(amount);
    }

    public Money(int amount) {
        this.amount = new BigDecimal(amount);
    }

    public Money(double amount) {
        this.amount = new BigDecimal(amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String format(Locale locale) {
        BigDecimal a = amount;
        if (a == null) {
            a = new BigDecimal(0);
        }
        NumberFormat currency = NumberFormat.getCurrencyInstance(locale);
        return currency.format(a);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        //noinspection RedundantIfStatement
        if (amount != null ? !amount.equals(money.amount) : money.amount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return amount != null ? amount.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                '}';
    }
}
