/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.Utils;
import com.blackbox.commerce.Merchant;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.commerce.ICommerceDao;
import com.blackbox.server.commerce.event.SaveMerchantEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(SaveMerchantEvent.class)
public class SaveMerchantListener extends BaseBlackboxListener<SaveMerchantEvent, Merchant> {

    private ICommerceDao commerceDao;


    @Override
    public void handle(SaveMerchantEvent saveMerchantEvent, ResultReference<Merchant> result) {
        Utils.applyGuid(saveMerchantEvent.getType());
        Merchant merchant = commerceDao.saveMerchant(saveMerchantEvent.getType());
        result.setResult(merchant);
    }

    public void setCommerceDao(ICommerceDao commerceDao) {
        this.commerceDao = commerceDao;
    }
}
