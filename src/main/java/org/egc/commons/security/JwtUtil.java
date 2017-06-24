package org.egc.commons.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.egc.commons.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * JsonWebToken (JWT) 生成与解析工具类
 *
 * @author houzhiwei
 * @link https://stormpath.com/blog/jwt-java-create-verify
 * @date 2017/6/16 9:35
 * @date 2017/6/21
 */
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * generate secret key string using Shiro AesCipherService
     *
     * @return key string
     */
    public static String generateKeyStr() {
        return AesEncryption.getBase64KeyStr(256);
    }

    /**
     * generate secret key using jjwt MacProvider
     * HS512
     *
     * @return key
     */
    public static Key generateKey() {
        Key key = MacProvider.generateKey(); // default SignatureAlgorithm.HS512
        return key;
    }

    public static Key getKeyFromString(SignatureAlgorithm algorithm, String keyStr) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(keyStr);
        return new SecretKeySpec(apiKeySecretBytes, algorithm.getJcaName());
    }

    /**
     * create a JWT(JsonWebToken)
     * jwt的签发
     *
     * @param id        token id / user id
     * @param sub       subject：主题、用户(email 或 用户名)
     * @param roles     用户角色列表
     * @param ttlMillis 过期时间（毫秒）
     * @param alg       Signature Algorithm 签名算法
     * @param iss       issuer 签发者
     * @param secret    签名密钥
     * @return jwt
     */
    public static String createJwt(String id, String sub, List<String> roles, String iss, Key secret, SignatureAlgorithm alg, long ttlMillis) {
        //sign JWT with ApiKey secret
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //set the JWT Claims
        JwtBuilder jwtBuilder = Jwts.builder().setHeaderParam("type", "JWT")
                .setId(id)
                .compressWith(CompressionCodecs.DEFLATE) //压缩
                .setIssuedAt(now) //iat，发行时间
                .setSubject(sub)
                .claim("roles", roles)
                .setIssuer(iss)
                .signWith(alg, secret);
        //if it has been specified, add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            jwtBuilder.setExpiration(exp); //exp：Expiration time，过期时间
        }
        return jwtBuilder.compact(); //生成JWT
    }


    /**
     * validate and read JWT(JsonWebToken)
     *
     * @param token  Json Web Token String
     * @param secret 密钥（必须与签发时使用的密钥一致才能正确解析）
     * @param iss    issuer 签发者
     * @return {@link AuthTokenInfo}
     */
    public static AuthTokenInfo parseJwt(String token, Key secret, String iss) {
        try {
            // make sure that we can trust jwt
            Claims claims = Jwts.parser().setSigningKey(secret)//(DatatypeConverter.parseBase64Binary(keyStr))
                    .requireIssuer(iss)
                    .parseClaimsJws(token).getBody();

            AuthTokenInfo tokenInfo = new AuthTokenInfo();
            tokenInfo.setId(claims.getId());
            tokenInfo.setSubject(claims.getSubject());
            tokenInfo.setIssuer(claims.getIssuer());
            tokenInfo.setIssued_at(claims.getIssuedAt());
            tokenInfo.setRoles((List) claims.get("roles"));
            tokenInfo.setExpiration(claims.getExpiration());
            return tokenInfo;
        } catch (SignatureException | MalformedJwtException e) {
            // don't trust the JWT!
            log.error("Json Web Token Signature Exception", e);
            throw new BusinessException(e, "Json Web Token Signature Exception");
        } catch (ExpiredJwtException e) {
            log.error("Json Web Token Expired", e);
            throw new BusinessException(e, "Json Web Token Expired!");
        }
    }
}
