package com.blackbox.user;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * This is for promo code object that can be used by things like promoters. Unlike {@link com.blackbox.user.SingleUsePromoCode}
 * they are not deleted after one use and they are not tied to one email.
 *
 * @author A.J. Wright
 */
@XmlRootElement
public class MultiUserPromoCode extends BasePromoCode {
    private static final long serialVersionUID = -7715627278688004819L;

    private String promoCampaignName;
    private String collectCC;

    public MultiUserPromoCode() {
        type = PromoCodeType.MULTI_USE;
    }

    public String getPromoCampaignName() {
        return promoCampaignName;
    }

    public void setPromoCampaignName(String promoCampaignName) {
        this.promoCampaignName = promoCampaignName;
    }

    public String getCollectCC() {
        return collectCC;
    }

    public void setCollectCC(String collectCC) {
        this.collectCC = collectCC;
    }
}