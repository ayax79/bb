package com.blackbox.foundation.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Registration implements Serializable {

    private User user;
    private List<String> leechEmails;
    private String promoCodeGuid;
    private String affiliateId;

    public Registration() {
    }

    public Registration(User user, String affiliateId, List<String> leechEmails) {
        this.user = user;
        this.affiliateId = affiliateId;
        this.leechEmails = leechEmails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getLeechEmails() {
        return leechEmails;
    }

    public void setLeechEmails(List<String> leechEmails) {
        this.leechEmails = leechEmails;
    }

    public String getPromoCodeGuid() {
        return promoCodeGuid;
    }

    public void setPromoCodeGuid(String promoCodeGuid) {
        this.promoCodeGuid = promoCodeGuid;
    }

    public String getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }
}
