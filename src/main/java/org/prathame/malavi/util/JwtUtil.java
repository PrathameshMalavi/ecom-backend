package org.prathame.malavi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.quarkus.runtime.Startup;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.prathame.malavi.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetails;

@ApplicationScoped
@Startup
public class JwtUtil {

    private static final String SECRET_KEY = "learn_programming_yourself";
    private static final int TOKEN_VALIDITY = 3600 * 5; // 5 hours

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }

    public Boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
//                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
//                .compact();
//    }

    public String generateToken(User user) {
        Set<String> roles = new HashSet<>();
        user.getRole().forEach(role -> roles.add("ROLE_" + role.getRoleName()));

        return Jwt.issuer("prathamesh.versevision") // any unique issuer
                .upn(user.getUserName())
                .groups(roles)
                .expiresIn(Duration.ofHours(6))
                .sign();
    }
}
