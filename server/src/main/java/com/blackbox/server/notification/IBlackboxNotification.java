/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.notification;

import org.apache.camel.Exchange;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public interface IBlackboxNotification {
    void notify(Exchange exchange);
}
