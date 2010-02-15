/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author andrew
 */
@Path("/")
public interface IPingService {

    @GET
    @Path("/ping")
    @Produces("text/plain")
    String ping();

}
