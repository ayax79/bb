/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.event;

import com.blackbox.commerce.Product;
import com.blackbox.commerce.Inventory;
import org.yestech.event.event.BaseEvent;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SaveInventoryEvent extends BaseEvent<Inventory> {
    private static final long serialVersionUID = -9021661411176533819L;

    public SaveInventoryEvent(Inventory inventory) {
        super(inventory);
    }
}