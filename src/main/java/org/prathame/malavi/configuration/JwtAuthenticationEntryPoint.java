package org.prathame.malavi.configuration;

import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JwtAuthenticationEntryPoint implements ExceptionMapper<UnauthorizedException> {

    @Override
    public Response toResponse(UnauthorizedException exception) {
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Unauthorized")
                .build();
    }
}