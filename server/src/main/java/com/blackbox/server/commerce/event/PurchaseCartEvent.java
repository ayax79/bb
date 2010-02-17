/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.event;

import com.blackbox.foundation.commerce.Invoice;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@EventResultType(Invoice.class)
public class PurchaseCartEvent extends BaseEvent<Invoice> {
    private static final long serialVersionUID = -7125323269256353565L;

    public PurchaseCartEvent(Invoice invoice) {
        super(invoice);
    }
}
