package com.taldate.backend.auth.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    // private final int tokenDuration = CHANGEME. in ms?
//    @Value("${jwt_secret}") // if server restarts, users will get logged out otherwise
    SecretKey key = Jwts.SIG.HS256.key().build();

    public String generateToken(int userId) {
        return Jwts.builder()
                .issuedAt(new Date())
                /*.expiration()*/
                /*.claims(...).and()*/
                .claim("userId", userId)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * If token is invalid/expired/...,
     * .parseSignedClaims() will throw an exception and
     * execution won't continue in JwtRequestFilter
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
