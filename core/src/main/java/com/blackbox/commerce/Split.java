/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.commerce;

import com.blackbox.user.User;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.yestech.lib.currency.Money;
import org.yestech.lib.util.Pair;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Locale;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */

@XmlRootElement(name = "invoiceSplit")
public class Split extends Pair<User, Billing> {
    private Long identifier;
    private Long version;
    private DateTime created;
    private DateTime modified;

    public Split() {
    }

    public Split(User user, Billing billing) {
        super(user, billing);
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @XmlJavaTypeAdapter(com.blackbox.util.JodaDateTimeXmlAdapter.class)
    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    @XmlJavaTypeAdapter(com.blackbox.util.JodaDateTimeXmlAdapter.class)
    public DateTime getModified() {
        return modified;
    }

    public void setModified(DateTime modified) {
        this.modified = modified;
    }

    @Override
    public User getFirst() {
        return super.getFirst();
    }

    @Override
    public void setFirst(User first) {
        super.setFirst(first);
    }

    @Override
    public void setSecond(Billing second) {
        super.setSecond(second);
    }

    @Override
    public Billing getSecond() {
        return super.getSecond();
    }

    public String getContributionAmount() {
        return getSecond().getAmount().getAmount().toString();
    }

    public void setContributionAmount(String productAmount) {

        Billing billing = getSecond();
        Money amount;
        if (billing != null && billing.getAmount() != null) {
            amount = new Money(productAmount, billing.getAmount().getLocale());
            billing.setAmount(amount);
        } else if (billing != null && billing.getAmount() == null) {
            amount = new Money(productAmount);
        } else {
            billing = new Billing();
            amount = new Money(productAmount);
        }
        billing.setAmount(amount);
        setSecond(billing);
    }

    public String getContributionAmountLocale() {
        Locale locale = getSecond().getAmount().getLocale();
        return locale.getLanguage() + ":" + locale.getCountry();
    }

    public void setContributionAmountLocale(String amountLocale) {
        String[] locale = StringUtils.split(amountLocale, ":");
        Billing billing = getSecond();
        Money amount;
        if (billing != null && billing.getAmount() != null) {
            amount = new Money(getSecond().getAmount().getAmount(), new Locale(locale[0], locale[1]));
            billing.setAmount(amount);
        } else if (billing != null && billing.getAmount() == null) {
            amount = new Money("0", new Locale(locale[0], locale[1]));
        } else {
            billing = new Billing();
            amount = new Money("0");
        }
        billing.setAmount(amount);
        setSecond(billing);
    }
}
