/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.listener;

import com.blackbox.foundation.Utils;
import com.blackbox.foundation.commerce.ShoppingCart;
import com.blackbox.server.commerce.ICommerceDao;
import com.blackbox.server.commerce.event.SaveCartEvent;
import com.blackbox.server.BaseBlackboxListener;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(SaveCartEvent.class)
public class SaveShoppingCartListener extends BaseBlackboxListener<SaveCartEvent, ShoppingCart> {

    private ICommerceDao commerceDao;

    @Override
    public void handle(SaveCartEvent saveCartEvent, ResultReference<ShoppingCart> result) {
        Utils.applyGuid(saveCartEvent.getType());
        ShoppingCart savedCart = commerceDao.saveCart(saveCartEvent.getType());
        result.setResult(savedCart);
    }

    public void setCommerceDao(ICommerceDao commerceDao)
    {
        this.commerceDao = commerceDao;
    }

}
