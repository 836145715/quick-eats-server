package com.zmx.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Data
@Component
public class JwtUtils {

//    @Value("${jwt.secret}")
//    private String secret;

    private static final SecretKey SIGNING_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    @Value("${jwt.expire}")
    private long expire;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    /**
     * 生成token
     *
     * @param userId 用户ID
     * @return token字符串
     */
    public String generateToken(Long userId) {
        Date nowDate = new Date();
        // 过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 60 * 1000);

        // 使用安全的密钥
        Key key = Keys.hmacShaKeyFor(SIGNING_KEY.getEncoded());

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        return tokenPrefix + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析JWT
     *
     * @param token JWT字符串
     * @return Claims
     */
    public Claims getClaimByToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        // 如果token带有前缀，去除前缀
        if (token.startsWith(tokenPrefix)) {
            token = token.substring(tokenPrefix.length());
        }

        try {
            // 使用安全的密钥
            Key key = Keys.hmacShaKeyFor(SIGNING_KEY.getEncoded());
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断token是否过期
     *
     * @param claims Claims
     * @return 是否过期
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /**
     * 从token中获取用户ID
     *
     * @param token JWT字符串
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimByToken(token);
        if (claims != null && !isTokenExpired(claims)) {
            return Long.valueOf(claims.get("userId").toString());
        }
        return null;
    }
}