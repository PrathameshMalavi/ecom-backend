package org.prathame.malavi.common;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.prathame.malavi.dao.UserDao;

import java.util.Set;

@ApplicationScoped
public class KeycloakService {

//    @Inject
//    SecurityContext securityContext;
    @Inject
    SecurityIdentity identity;

    @Inject
    UserDao userRepository;

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    JsonWebToken jwt;



    public String getUsername() {
//        if (securityContext.getUserPrincipal() != null) {
//            return securityContext.getUserPrincipal().getName();
//        }
        if (securityIdentity.getPrincipal() != null){
            return jwt.getClaim("email");
        }
        return null;
    }

    public Set<String> getUserRoles() {
//        if (securityContext.getUserPrincipal() instanceof OidcJwtCallerPrincipal) {
//            OidcJwtCallerPrincipal principal = (OidcJwtCallerPrincipal) securityContext.getUserPrincipal();
//            return principal.getGroups(); // returns roles/groups
//        }
//        return Set.of();
        if (securityIdentity.getPrincipal() != null){
            return jwt.getGroups();
        }
        return Set.of();
    }


    public boolean hasRole(String role) {
        return jwt.getGroups().contains(role);
    }

    public String getKeycloakId() {
        return jwt.getSubject();
    }


}