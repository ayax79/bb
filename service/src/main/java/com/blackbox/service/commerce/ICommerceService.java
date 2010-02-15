/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.service.commerce;

import com.blackbox.commerce.ShoppingCart;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@Path("/")
public interface ICommerceService {

    @GET
    @Path("/loadCart/{session}/{guid}/")
    @Produces("application/json")
    ShoppingCart loadCart(@PathParam("session") String session,
                          @PathParam("guid") String guid);

}
