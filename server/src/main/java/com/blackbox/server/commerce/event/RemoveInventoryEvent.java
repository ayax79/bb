/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.event;

import com.blackbox.foundation.commerce.Inventory;
import org.yestech.event.event.BaseEvent;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class RemoveInventoryEvent extends BaseEvent<Inventory> {
    private static final long serialVersionUID = -6908341474985565336L;

    public RemoveInventoryEvent(Inventory inventory) {
        super(inventory);
    }
}