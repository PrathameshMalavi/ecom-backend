package org.prathame.malavi.configuration;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.prathame.malavi.common.MailService;
import org.prathame.malavi.dao.UserDao;
import org.prathame.malavi.entity.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Path("/")
public class UserEntryPoint{

    @Inject
    UserDao userRepository;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    JsonWebToken jwt;

    @Inject
    MailService mailService;


    @GET
    @Path("/api")
    @RolesAllowed({"user", "admin"})
    @Transactional
    public Response getCurrentUser() {
        System.out.println("Called Me :  " +  jwt.getSubject());
        System.out.println("Groups User          " + extractClientRoles());

        String keycloakId = jwt.getSubject();
        User user = userRepository.findByKeycloakId(keycloakId);
        if (user == null) {
            user = new User();
            String role = extractClientRoles().contains("admin") ? "admin" : "user";
            user.setKeycloakId(keycloakId);
            user.setUserName(jwt.getClaim("email"));
            user.setUserFirstName(jwt.getClaim("given_name"));
            user.setUserLastName(jwt.getClaim("family_name"));
            user.setRole(role);
            userRepository.persist(user);
            String  userfullName = user.getUserFirstName() +" " + user.getUserLastName();
            mailService.sendWelcomeMail(user.getUserName(),userfullName,"templates/welcome.html");
        }
        return Response.ok(user).build();
    }


    private Set<String> extractClientRoles() {
        Set<String> roles = new HashSet<>();

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("ecommerce-frontend");
            if (clientAccess != null && clientAccess.containsKey("roles")) {
                roles.addAll((Collection<? extends String>) clientAccess.get("roles"));
            }
        }

        return roles;
    }
}

