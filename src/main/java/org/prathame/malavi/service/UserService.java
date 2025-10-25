package org.prathame.malavi.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.prathame.malavi.dao.RoleDao;
import org.prathame.malavi.dao.UserDao;
import org.prathame.malavi.entity.Role;
import org.prathame.malavi.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class UserService {

    @Inject
    UserDao userRepository;

    @Inject
    RoleDao roleRepository;

    @Transactional
    public void initRoleAndUser() {
        // ----- Create roles -----
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleRepository.persist(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for newly created record");
        roleRepository.persist(userRole);

        // ----- Create admin user -----
        User adminUser = new User();
        adminUser.setUserName("admin123");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userRepository.persist(adminUser);

        // ----- Create regular user -----
        User user = new User();
        user.setUserName("raj123");
        user.setUserPassword(getEncodedPassword("raj@123"));
        user.setUserFirstName("raj");
        user.setUserLastName("sharma");

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRole(userRoles);
        userRepository.persist(user);
    }

    @Transactional
    public User registerNewUser(User user) {
        Role role = roleRepository.findById("User");
        if (role == null) {
            throw new IllegalStateException("Default role 'User' not found");
        }

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRole(userRoles);
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));

        userRepository.persist(user);
        return user;
    }

//    public String getEncodedPassword(String password) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // default version
//        return encoder.encode(password);
////        return BcryptUtil.bcryptHash(password);
//    }

    public String getEncodedPassword(String password) {
        try {
            return bytesToHex(MessageDigest.getInstance("SHA-256").digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
