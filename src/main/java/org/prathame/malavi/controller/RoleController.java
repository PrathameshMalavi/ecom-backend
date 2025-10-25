package org.prathame.malavi.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.prathame.malavi.entity.Role;
import org.prathame.malavi.service.RoleService;

@Path("/")
public class RoleController {

    @Inject
    RoleService roleService;

    @POST
    @Path("/createNewRole")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Role createNewRole(Role role) {
        return roleService.createNewRole(role);
    }
}