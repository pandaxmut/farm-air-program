package com.example.gateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

/**
 * @author wuyunbin
 */
@Slf4j
public class JwtUtils {
    public static String createJwt(String id, Claims claims, Long ttl, String secretKey) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setId(id)
                .setClaims(claims)
                //签发时间
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ttl))
                .signWith(key)
                .compact();
    }

    public static String createJwt(String id, String name, Integer role, Long ttl, String secretKey) {
        Claims claims = Jwts.claims();
        claims.put("name", name);
        claims.put("role", role);
        claims.put("userId", id);
        return createJwt(id, claims, ttl, secretKey);
    }

    public static Claims parseJwt(String token, String secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean validateToken(String token, String secretKey) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            //TODO 这里包含了很多异常，比如签名，过期异常等 抛出并处理就行。
            System.out.println(e.getMessage());
            return false;
        }
    }


}