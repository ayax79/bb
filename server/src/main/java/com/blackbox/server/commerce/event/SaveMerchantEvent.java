/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.event;

import com.blackbox.commerce.Merchant;
import org.yestech.event.event.BaseEvent;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SaveMerchantEvent extends BaseEvent<Merchant> {
    private static final long serialVersionUID = -8897814627412113293L;

    public SaveMerchantEvent(Merchant merchant) {
        super(merchant);
    }
}
