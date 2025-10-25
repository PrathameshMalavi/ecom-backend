package org.prathame.malavi.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.prathame.malavi.entity.JwtRequest;
import org.prathame.malavi.entity.JwtResponse;
import org.prathame.malavi.service.JwtService;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JwtController {

    @Inject
    JwtService jwtService;

    @POST
    @Path("/authenticate")
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }
}