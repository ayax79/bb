/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.service.media;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */

@Path("/media/publish")
public interface IMediaPublishService {

    @POST
    @Path("/image/{session}/{guid}/")
    @Produces("text/plain")
    public String image(@PathParam("session") String session,
                @PathParam("guid") String guid,
                @Context HttpServletRequest request);

    @POST
    @Path("/video/{session}/{guid}/")
    @Produces("text/plain")
    public String video(@PathParam("session") String session,
                @PathParam("guid") String guid,
                @Context HttpServletRequest request);
    
    @POST
    @Path("/audio/{session}/{guid}/")
    @Produces("text/plain")
    public String audio(@PathParam("session") String session,
                @PathParam("guid") String guid,
                @Context HttpServletRequest request);
}
