package org.prathame.malavi.dao;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.prathame.malavi.entity.Role;


@ApplicationScoped
public class RoleDao implements PanacheRepository<Role> {

    /**
     * Find role by ID (role name)
     */
    public Role findById(String roleName) {
        return find("id", roleName).firstResult();
    }

    /**
     * Save or update role
     */
    public void save(Role role) {
        persist(role);
    }

    /**
     * Delete role by ID
     */
    public void deleteById(String roleName) {
        delete("id", roleName);
    }
}