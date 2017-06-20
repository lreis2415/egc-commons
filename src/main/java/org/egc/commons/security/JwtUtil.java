package org.egc.commons.security;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.egc.commons.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * JsonWebToken (JWT) 生成与解析工具类
 * 可以在properties文件中配置 jwt.secret 作为密钥
 * （通过配置 context:property-placeholder），
 * 也可不配置而由系统生成一个随机的密钥
 *
 * @author houzhiwei
 * @link https://stormpath.com/blog/jwt-java-create-verify
 * @date 2017/6/16 9:35
 */
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    public static Key SIGNING_KEY;//jwt 签名密钥
    public static SignatureAlgorithm SIGNING_ALGORITHM;//jwt 签名算法

    @Value("${jwt.secret}")
    public static String SIGNING_KEY_STRING;//jwt 签名密钥字符串

    static {
        SIGNING_ALGORITHM = SignatureAlgorithm.HS256;
        if (Strings.isNullOrEmpty(SIGNING_KEY_STRING)) {
            SIGNING_KEY_STRING = generateKeyStr();
        }
        SIGNING_KEY = getFromKeyString(SIGNING_ALGORITHM, SIGNING_KEY_STRING);
    }

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

    public static Key getFromKeyString(SignatureAlgorithm algorithm, String keyStr) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(keyStr);
        return new SecretKeySpec(apiKeySecretBytes, algorithm.getJcaName());
    }

    /**
     * create a JWT(JsonWebToken)
     * jwt的签发
     *
     * @param id        token id / user id
     * @param iss       issuer，发行者
     * @param sub       subject：主题、用户(email 或 用户名)
     * @param roles     用户角色列表
     * @param ttlMillis 过期时间（毫秒）
     * @return jwt
     */
    public static String createJWT(String id, String iss, String sub, List<String> roles, long ttlMillis) {
        //sign JWT with ApiKey secret
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //set the JWT Claims
        JwtBuilder jwtBuilder = Jwts.builder().setHeaderParam("type", "JWT")
                .setId(id)
                .setIssuedAt(now) //iat，发行时间
                .setSubject(sub)
                .claim("roles", roles)
                .setIssuer(iss)
                .signWith(SIGNING_ALGORITHM, SIGNING_KEY);
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
     * @param jwt Json Web Token String
     * @return {@link AuthTokenInfo}
     */
    public static AuthTokenInfo parseJWT(String jwt) {
        try {
            // make sure that we can trust jwt
            Claims claims = Jwts.parser().setSigningKey(SIGNING_KEY)//(DatatypeConverter.parseBase64Binary(keyStr))
                    .parseClaimsJws(jwt).getBody();

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

    /**
     * parse jwt and get username(subject)
     *
     * @param jwt
     * @return username(subject)
     */
    public static String parseJWT4Username(String jwt) {
        try {
            // make sure that we can trust jwt
            return Jwts.parser().setSigningKey(SIGNING_KEY)//(DatatypeConverter.parseBase64Binary(key))
                    .parseClaimsJws(jwt).getBody().getSubject();
        } catch (SignatureException | MalformedJwtException e) {
            // don't trust the JWT!
            log.error("Json Web Token Signature Exception");
            throw new BusinessException(e, e.getLocalizedMessage());
        } catch (ExpiredJwtException e) {
            log.error("Json Web Token Expired");
            throw new BusinessException(e, "Json Web Token Expired!");
        }
    }
}
