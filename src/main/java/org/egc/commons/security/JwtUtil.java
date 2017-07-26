package org.egc.commons.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <pre>
 * JsonWebToken (JWT) 生成与解析工具类
 *
 * @author houzhiwei
 * @link https ://stormpath.com/blog/jwt-java-create-verify
 * @date 2017 /6/16 9:35
 * @date 2017 /6/21
 */
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    /**
     * The constant TOKEN_PREFIX.
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * The constant HEADER_STRING.
     */
    public static final String HEADER_STRING = "Authorization";

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
     * @return key key
     */
    public static Key generateKey() {
        Key key = MacProvider.generateKey(); // default SignatureAlgorithm.HS512
        return key;
    }

    /**
     * Gets key from string.
     *
     * @param algorithm the algorithm
     * @param keyStr    the key str
     * @return the key from string
     */
    public static Key getKeyFromString(SignatureAlgorithm algorithm, String keyStr) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(keyStr);
        return new SecretKeySpec(apiKeySecretBytes, algorithm.getJcaName());
    }

    /**
     * create a JWT(JsonWebToken)
     * jwt的签发
     *
     * @param id     token id
     * @param sub    subject：主题、用户(email 或 用户名)
     * @param config jwt 配置信息（key等）
     * @return jwt string
     */
    public static String createJwt(String id, String sub, JwtConfig config) {
        //sign JWT with ApiKey secret
        long ttlMillis = config.getTtlMillis();
        //set the JWT Claims
        JwtBuilder jwtBuilder = basicJwtBuilder(id, sub, config, ttlMillis);
        return jwtBuilder.compact(); //生成JWT
    }

    /**
     * create access json web token
     *
     * @param uuid   token id
     * @param userId subject id
     * @param sub    subject, such as email or username
     * @param roles  user's roles 用户角色列表
     * @param config jwt 配置信息（key等）
     * @return jwt string
     */
    public static String createUserJwt(UUID uuid, int userId, String sub, List<String> roles, JwtConfig config) {
        long ttlMillis = config.getTtlMillis();
        return generateUserToken(uuid, userId, sub, roles, config, ttlMillis);
    }

    /**
     * 生成用于刷新用户access token的refresh token
     *
     * @param oldToken 旧的token
     * @param config   jwt 配置信息（key等）
     * @return jwt string
     */
    /*public static String createRefreshJwt(String oldToken, JwtConfig config) {
        AuthTokenInfo tokenInfo = parseUserJwt(oldToken, config.getKey(), config.getIssuer());
        long refreshTTL = config.getRefreshTTL();
        return generateUserToken(UUID.randomUUID(), tokenInfo.getUserId(),
                                 tokenInfo.getSubject(), tokenInfo.getRoles(), config, refreshTTL);//生成JWT
    }*/

    /**
     * <pre>
     * 生成 userJwt（access token）时生成refreshJwt（refresh token）
     * 若 access token 过期而 refresh token 没有过期，则重新生成 access token
     * 若 refresh token 也过期，则需要重新登录
     * </pre>
     *
     * @param uuid
     * @param sub
     * @param config jwt configuration
     * @return
     */
    public static String createRefreshJwt(UUID uuid, String sub, JwtConfig config) {
        long refreshTTL = config.getRefreshTTL();
        JwtBuilder jwtBuilder = basicJwtBuilder(uuid.toString(), sub, config, refreshTTL);
        return jwtBuilder.compact(); //生成JWT
    }

    /**
     * validate and read JWT(JsonWebToken)
     *
     * @param token  Json Web Token String
     * @param secret 密钥（必须与签发时使用的密钥一致才能正确解析）
     * @param iss    issuer 签发者
     * @return {@link AuthTokenInfo}
     * @throws SignatureException    the signature exception
     * @throws MalformedJwtException the malformed jwt exception
     * @throws ExpiredJwtException   the expired jwt exception
     */
    public static AuthTokenInfo parseUserJwt(String token, Key secret, String iss)
            throws SignatureException, MalformedJwtException, ExpiredJwtException
    {
        // make sure that we can trust jwt
        Claims claims = parseJwt(token, secret, iss);
        AuthTokenInfo tokenInfo = new AuthTokenInfo();
        tokenInfo.setId(claims.getId());
        tokenInfo.setUserId((int) claims.get("userId"));
        tokenInfo.setSubject(claims.getSubject());
        tokenInfo.setIssuer(claims.getIssuer());
        tokenInfo.setIssued_at(claims.getIssuedAt());
        tokenInfo.setRoles((List) claims.get("roles"));
        tokenInfo.setExpiration(claims.getExpiration());
        return tokenInfo;
    }

    /**
     * parse jwt
     *
     * @param token  the token
     * @param secret the secret
     * @param iss    the iss
     * @return jwt claims
     * @throws SignatureException    the signature exception
     * @throws MalformedJwtException the malformed jwt exception
     * @throws ExpiredJwtException   the expired jwt exception
     */
    public static Claims parseJwt(String token, Key secret, String iss)
            throws SignatureException, MalformedJwtException, ExpiredJwtException
    {
        // make sure that we can trust jwt
        return Jwts.parser().setSigningKey(secret)//(DatatypeConverter.parseBase64Binary(keyStr))
                .requireIssuer(iss)
                .parseClaimsJws(token).getBody();
    }

    //--------------------------------------------- private functions ------------------------------------------------------//

    /**
     * 生成用户token
     *
     * @param uuid
     * @param userId
     * @param sub
     * @param roles
     * @param config
     * @param ttl    Time To Live, 生存时间/有效时间
     * @return
     */
    private static String generateUserToken(UUID uuid, int userId, String sub, List<String> roles, JwtConfig config, long ttl) {
        JwtBuilder jwtBuilder = basicJwtBuilder(uuid.toString(), sub, config, ttl);
        jwtBuilder.claim("roles", roles).claim("userId", userId);
        return jwtBuilder.compact(); //生成JWT
    }

    /**
     * 生成具有基本信息的 JwtBuilder
     *
     * @param id
     * @param sub
     * @param config jwt 配置信息
     * @param ttl
     * @return
     */
    private static JwtBuilder basicJwtBuilder(String id, String sub, JwtConfig config, long ttl) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //set the JWT Claims
        JwtBuilder jwtBuilder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setId(id)
                .compressWith(CompressionCodecs.DEFLATE) //压缩
                .setIssuedAt(now) //iat，发行时间
                .setSubject(sub)
                .setIssuer(config.getIssuer())
                .signWith(config.getAlg(), config.getKey());
        //if it has been specified, add the expiration
        if (ttl >= 0) {
            long expMillis = nowMillis + ttl;
            jwtBuilder.setExpiration(new Date(expMillis));//exp：Expiration time，过期时间
        }
        return jwtBuilder;
    }
}
