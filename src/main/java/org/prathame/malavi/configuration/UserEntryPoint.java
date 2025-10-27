package org.prathame.malavi.configuration;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.prathame.malavi.dao.UserDao;
import org.prathame.malavi.entity.User;

@Path("/")
public class UserEntryPoint{

    @Inject
    UserDao userRepository;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    JsonWebToken jwt;


    @GET
    @Path("/api")
    @RolesAllowed({"user", "admin"})
    @Transactional
    public Response getCurrentUser() {
        System.out.println("Called Me");
        String keycloakId = jwt.getSubject();
        User user = userRepository.findByKeycloakId(keycloakId);
        if (user == null) {
            user = new User();
            user.setKeycloakId(keycloakId);
            user.setUserName(jwt.getClaim("email"));
            user.setUserFirstName(jwt.getClaim("given_name"));
            user.setUserLastName(jwt.getClaim("family_name"));
            String role = jwt.getGroups().contains("admin") ? "admin" : "user";
            user.setRole(role);
            userRepository.persist(user);
        }
        return Response.ok(user).build();
    }
}

