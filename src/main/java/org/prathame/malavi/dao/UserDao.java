package org.prathame.malavi.dao;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.prathame.malavi.entity.User;

@ApplicationScoped
public class UserDao implements PanacheRepository<User> {

    /**
     * Find user by username (ID)
     */
    public User findById(String username) {
        User user = find("userName", username).firstResult();
        return user;
    }

    /**
     * Save or update user
     */
    public void save(User user) {
        persist(user);
    }

    /**
     * Delete user by username
     */
    public void deleteById(String username) {
        delete("userName", username);
    }



    public boolean existsByKeycloakId(String id) {
        return find("keycloakId", id).firstResultOptional().isPresent();
    }

    public User findByKeycloakId(String id) {
        return find("keycloakId", id).firstResult();
    }
}