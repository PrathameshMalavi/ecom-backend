package org.prathame.malavi.configuration;

import io.smallrye.jwt.auth.principal.JWTParser;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.prathame.malavi.util.JwtUtil;

import java.io.IOException;
import java.text.ParseException;

@Provider
//@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
public class JwtRequestFilter implements ContainerRequestFilter {

    public static String CURRENT_USER = "";

    @Inject
    JwtUtil jwtUtil;

    @Inject
    JWTParser jwtParser; // Provided by Quarkus SmallRye JWT

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // ✅ 1. Allow CORS preflight requests to pass
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }

        System.out.println("888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888");

        if(true){
            CURRENT_USER = "temp";
            return;
        }


        final String authorizationHeader = requestContext.getHeaderString("Authorization");

        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            try {
                // Parse and validate JWT
                JsonWebToken jwt = jwtParser.parse(jwtToken);
                username = jwt.getName();
                CURRENT_USER = username;

                // Optional custom validation
                if (!jwtUtil.validateToken(jwtToken, CURRENT_USER)) {
                    abortRequest(requestContext, "Invalid JWT token");
                }

            } catch (Exception e) {
                abortRequest(requestContext, "JWT token validation failed");
                return;
            }
        } else {
            System.out.println("JWT token missing or does not start with Bearer");
        }
    }

    private void abortRequest(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(
                jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.UNAUTHORIZED)
                        .entity(message)
                        .build()
        );
    }
}