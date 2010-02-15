package com.blackbox.server.user;

import com.blackbox.user.BasePromoCode;
import com.blackbox.user.User;
import org.springframework.transaction.annotation.Transactional;

public interface IPromoCodeDao {

    /**
     * This will populate the field with a new code.
     *
     * Note that this will call session.saveOrUpdate, so the object
     * has to be populated in a state that is insertable in the db (ie, the master field must be set).
     *
     * @param pc The object to place a unique code in.
     */
    @Transactional
    void generateUniquePromoCode(BasePromoCode pc);

    @Transactional
    BasePromoCode save(BasePromoCode promoCode);

    BasePromoCode loadPromoCodeByGuid(String guid);

    BasePromoCode loadPromoCodeByCode(String code);

    BasePromoCode loadPromoCodeByMaster(String masterGuid);

    @Transactional
    void delete(BasePromoCode promoCode);
}
