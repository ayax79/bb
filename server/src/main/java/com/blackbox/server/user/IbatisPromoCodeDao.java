package com.blackbox.server.user;

import com.blackbox.server.util.PersistenceUtil;
import com.blackbox.foundation.user.BasePromoCode;
import com.blackbox.foundation.user.MultiUserPromoCode;
import com.blackbox.foundation.user.SingleUsePromoCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yestech.lib.crypto.PasswordGenerator;

import javax.annotation.Resource;

@Repository("promoCodeDao")
public class IbatisPromoCodeDao implements IPromoCodeDao {

    @Resource
    SqlSessionOperations template;

    @Transactional
    public void generateUniquePromoCode(BasePromoCode pc) {

        String code = null;
        while (code == null) {
            code = PasswordGenerator.createPassword(8).toLowerCase();

            try {
                pc.setCode(code);
                save(pc);
            } catch (DataIntegrityViolationException e) {
                code = null;
            }
        }

    }

    @Override
    @Transactional
    public BasePromoCode save(BasePromoCode promoCode) {
        if (promoCode instanceof SingleUsePromoCode) {
            PersistenceUtil.insert(promoCode, template, "PromoCode.insertSingleUse");
        } else if (promoCode instanceof MultiUserPromoCode) {
            PersistenceUtil.insert(promoCode, template, "PromoCode.insertMultiUse");
        }
        return promoCode;
    }

    @Override
    public BasePromoCode loadPromoCodeByGuid(String guid) {
        return (BasePromoCode) template.selectOne("PromoCode.load", guid);
    }

    @Transactional
    @Override
    public void delete(BasePromoCode promoCode) {
        template.delete("PromoCode.delete", promoCode.getGuid());
    }

    @Override
    public BasePromoCode loadPromoCodeByCode(String code) {
        return (BasePromoCode) template.selectOne("PromoCode.loadByCode", code);
    }

    @Override
    public BasePromoCode loadPromoCodeByMaster(String masterGuid) {
        return (BasePromoCode) template.selectOne("PromoCode.loadByMaster", masterGuid);
    }

}
