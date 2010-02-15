/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.commerce.event;

import com.blackbox.commerce.Product;
import org.yestech.event.event.BaseEvent;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class SaveProductEvent extends BaseEvent<Product> {
    private static final long serialVersionUID = -7094635797098651276L;

    public SaveProductEvent(Product product) {
        super(product);
    }
}
