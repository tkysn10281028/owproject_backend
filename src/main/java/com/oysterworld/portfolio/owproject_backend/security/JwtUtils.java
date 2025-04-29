package com.oysterworld.portfolio.owproject_backend.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    private static final String SECRET_KEY = "[各自で作成]";
    private static final String JWT_ISSUER = "[各自で作成]";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * JWTトークンを検証する
     */
    public static boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * トークンからメールアドレス（subject）を取得する
     */
    public static String getUserEmailAddress(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * JWTトークンを生成する（7日間有効）
     */
    public static String generateAccessToken(String emailAddress) {
        return Jwts.builder()
                .setSubject(emailAddress)
                .setIssuer(JWT_ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7日間
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}
