/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.service.point;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 *
 */
@Path("/")
public interface IPointService
{

    @GET
    @Path("/loadPoints/user{userId}")
    @Produces("application/xml")
    long loadPointsByUserGuid(@PathParam("userId") String userGuid);
}
