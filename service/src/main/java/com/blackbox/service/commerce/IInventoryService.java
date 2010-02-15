/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.service.commerce;

import com.blackbox.social.Categories;
import com.blackbox.commerce.Product;

import javax.ws.rs.*;
import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@Path("/")
public interface IInventoryService {

    @GET
    @Path("/loadByCategories/{session}/{categories}")
    @Produces("application/xml")
    List<Product> loadProductsByCategories(@PathParam("session") String session,
                                           @PathParam("categories") Categories
                                                   categories);
    @GET
    @Path("/loadInventory")
    @Produces("text/xml")
    String loadInventory();

    @PUT
    @Path("/uploadInventory/{merchantGuid}")
    @Consumes("text/xml")
    @Produces("text/plain")
    String uploadInventory(@PathParam("merchantGuid") String merchantGuid,
                           String inventoryXml);

    @PUT
    @Path("/uploadProduct/{merchantGuid}")
    @Consumes("text/xml")
    @Produces("text/plain")
    String uploadProduct(@PathParam("merchantGuid") String merchantGuid,
                           String productXml);

    @DELETE
    @Path("/deleteInventory/{merchantGuid}")
    @Consumes("text/xml")
    @Produces("text/plain")
    String deleteInventory(@PathParam("merchantGuid") String merchantGuid,
                          String inventoryXml);

}
