/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.service.commerce;

import com.blackbox.commerce.ShoppingCart;
import com.blackbox.commerce.ICommerceManager;
import com.blackbox.service.security.AuthenticationUtils;

import javax.ws.rs.PathParam;
import javax.annotation.Resource;
import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@Service
public class CommerceService implements ICommerceService {

    @Resource(name = "commerceManager")
    private ICommerceManager commerceManager;


//    @Resource(name = "serviceAuthentication")
//    private AuthenticationUtils authentication;
//
//    public void setAuthentication(AuthenticationUtils authentication) {
//        this.authentication = authentication;
//    }

    @Override
    public ShoppingCart loadCart(String session, String guid) {
        ShoppingCart shoppingCart = commerceManager.retrieveCart(UUID.fromString(guid));
        return shoppingCart;
    }

    public void setCommerceManager(ICommerceManager commerceManager) {
        this.commerceManager = commerceManager;
    }
}
