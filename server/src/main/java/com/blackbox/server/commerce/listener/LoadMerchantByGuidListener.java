/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.foundation.commerce.Merchant;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.commerce.ICommerceDao;
import com.blackbox.server.commerce.event.LoadMerchantEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(LoadMerchantEvent.class)
public class LoadMerchantByGuidListener extends BaseBlackboxListener<LoadMerchantEvent, Merchant> {

    private ICommerceDao commerceDao;

    @Override
    public void handle(LoadMerchantEvent loadMerchantEvent, ResultReference<Merchant> result) {
        Merchant merchant = commerceDao.loadMerchantByGuid(loadMerchantEvent.getType());
        result.setResult(merchant);
    }

    public void setCommerceDao(ICommerceDao commerceDao) {
        this.commerceDao = commerceDao;
    }
}