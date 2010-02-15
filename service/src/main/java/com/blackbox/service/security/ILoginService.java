/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.service.security;

import com.blackbox.user.IUser;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Login Service
 *
 */
@Path("/")
public interface ILoginService {
    @POST
    @Path("/login/{username}/{password}/{guid}")
    @Produces("application/xml")
    IUser login(@PathParam("username") String username, @PathParam("password") String password,
                @PathParam("guid") String guid);
}
