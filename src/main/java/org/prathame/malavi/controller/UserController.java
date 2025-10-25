package org.prathame.malavi.controller;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.prathame.malavi.entity.User;
import org.prathame.malavi.service.UserService;

@Path("/")
public class UserController {

    @Inject
    UserService userService;

    @PostConstruct
    public void initRoleAndUser() {
//        userService.initRoleAndUser();
    }

    @POST
    @Path("/registerNewUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User registerNewUser(User user) {
        return userService.registerNewUser(user);
    }

    @GET
    @Path("/forAdmin")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("Admin")
    public String forAdmin() {
        return "This URL is only accessible to the admin";
    }

    @GET
    @Path("/forUser")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("User")
    public String forUser() {
        return "This URL is only accessible to the user";
    }


}