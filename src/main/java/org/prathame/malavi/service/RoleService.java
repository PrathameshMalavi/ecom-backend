package org.prathame.malavi.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.prathame.malavi.dao.RoleDao;
import org.prathame.malavi.entity.Role;


@ApplicationScoped
public class RoleService {

    @Inject
    RoleDao roleDao;

    @Transactional
    public Role createNewRole(Role role) {
        roleDao.save(role);
        return role;
    }
}