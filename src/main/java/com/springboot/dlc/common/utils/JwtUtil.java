package com.springboot.dlc.common.utils;

import com.springboot.dlc.common.enums.ResultEnum;
import com.springboot.dlc.common.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

/**
 * @Author: liujiebang
 * @Description: JWT工具类
 * @Date: 2018/7/2 16:52
 **/
public class JwtUtil {


    private static String audience = "2389rfjskdfh238hsjfhsjk8234eufhsgdfgj83h";

    private static String base64Secret = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";

    private static String issuer = "restapiauthtication";

    /**
     * 解析jwt
     */
    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            //解析token
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            Optional<Claims> claimsOptional = Optional.of(claims);
            if (claimsOptional.isPresent()) {
                Date expiration = claimsOptional.get().getExpiration();
                if (expiration.before(expiration)) {
                    throw new AuthException(ResultEnum.CODE_10);
                }
            }
            return claims;
        } catch (Exception ex) {
            return null;
        }
    }


    /**
     * 构建jwt
     */

    public static String createJWT(String unique, String uniqueId, long TTLMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("ljb", "JwtUtil")
                .claim(unique, uniqueId)
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        return builder.compact();
    }

    public static String getUniqueId(String token, String unique) {
        final Claims claims = JwtUtil.parseJWT(token, base64Secret);
        String uniqueId = (String) claims.get(unique);
        return uniqueId;
    }

}
