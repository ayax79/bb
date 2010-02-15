package com.blackbox.user;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * A promo code that is used for things like the couple and group plans.
 * There is one email tied to one code, only that one email can use the code and after registration it is deleted.
 *
 * @author A.J. Wright
 */
@XmlRootElement
public class SingleUsePromoCode extends BasePromoCode {

    private static final long serialVersionUID = -7715627278688004819L;

    private String email;

    public SingleUsePromoCode() {
        type = PromoCodeType.SINGLE_USE;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}