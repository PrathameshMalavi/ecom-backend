package org.prathame.malavi.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.prathame.malavi.dao.UserDao;
import org.prathame.malavi.entity.JwtRequest;
import org.prathame.malavi.entity.JwtResponse;
import org.prathame.malavi.entity.User;
import org.prathame.malavi.util.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtService {

    @Inject
    JwtUtil jwtUtil;

    @Inject
    UserDao userDao;

    /**
     * Create JWT token for user
     */
    @Transactional
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();

        // Authenticate user manually
        authenticate(userName, userPassword);

        User user = userDao.findById(userName);
        if (user == null) {
            throw new Exception("User not found");
        }

        // Generate JWT token
        String newGeneratedToken = jwtUtil.generateToken(user);

        return new JwtResponse(user, newGeneratedToken);
    }

    /**
     * Manual authentication
     */
    private void authenticate(String userName, String userPassword) throws Exception {
        User user = userDao.findById(userName);
        if (user == null) {
            throw new Exception("USER_NOT_FOUND");
        }


        System.out.println("Heloo" + user.getUserPassword());
        System.out.println("Heloo" + getEncodedPassword(userPassword));

        if (!user.getUserPassword().equals(getEncodedPassword(userPassword))) {
//            throw new Exception("INVALID_CREDENTIALS");
        }

//        getAuthority(user);
    }

    /**
     * Get roles as strings for authorization (if needed)
     */
    public Set<String> getAuthority(User user) {
        Set<String> authorities = new HashSet<>();
        user.getRole().forEach(role -> authorities.add("ROLE_" + role.getRoleName()));
        return authorities;
    }

//    public String getEncodedPassword(String password) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // default version
//        return encoder.encode(password);
//    }

    public String getEncodedPassword(String password) throws Exception {
        return bytesToHex(MessageDigest.getInstance("SHA-256").digest(password.getBytes()));
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
