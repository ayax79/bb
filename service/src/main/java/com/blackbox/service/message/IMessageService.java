/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.service.message;

import com.blackbox.message.Message;
import com.blackbox.EntityReference;

import javax.ws.rs.*;
import java.util.List;

/**
 *
 *
 */
@Path("/messages")
public interface IMessageService
{

//    @GET
//    @Path("/{ownerGuid}")
//    @Produces("application/xml")
//    List<Message> loadMessagesByOwner(@PathParam("ownerGuid") EntityReference owner);

//    @POST
//    @Path("/create/ascii")
//    void createAsciiMessage(@PathParam("subject") String subject,
//                       @PathParam("body") String body,
//                       @PathParam("toOwnerId") Long toOwnerId,
//                       @PathParam("fromOwnerId") Long fromOwnerId,
//                       @PathParam("parentMessageId") Long parentMessageId);
//
//    @POST
//    @Path("/create/binary")
//    void createBinaryMessage(@PathParam("subject") String subject,
//                       @PathParam("toOwnerId") Long toOwnerId,
//                       @PathParam("fromOwnerId") Long fromOwnerId,
//                       @PathParam("parentMessageId") Long parentMessageId);

}
