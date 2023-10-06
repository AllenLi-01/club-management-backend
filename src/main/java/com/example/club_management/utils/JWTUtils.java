package com.example.club_management.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.club_management.utils.ResponseCode.*;

public class JWTUtils {
    //失效时间 单位 s
    private static final long expire = 43200;//12h
    //32位秘钥
    private static final String secret = "eDWPnqSTpkzV2tbHGTZzZ@YLTZ2sfCyh";

    //生成token
    public static String generateToken(int uid){
        Date now = new Date();
        Date expiration = new Date(now.getTime()+1000*expire);
        return Jwts.builder()
                .setHeaderParam("type","JWT")
                .setSubject(Integer.toString(uid))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    //解析token
    public static Map<String, Object> getClaimsByToken(String token) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            // 验证成功，将claims放入resultMap中
            resultMap.put("claims", claims);
            resultMap.put("code", LOGIN_SUCCESS);
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            // Token 验证失败
            resultMap.put("code", e instanceof SignatureException ? ILLEGAL_TOKEN : TOKEN_EXPIRED);
        }
        return resultMap;
    }


}
