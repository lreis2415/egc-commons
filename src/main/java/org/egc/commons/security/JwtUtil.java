package org.egc.commons.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwtParser;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;

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

    //region basic functions

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
        // default SignatureAlgorithm.HS512
        Key key = MacProvider.generateKey();
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
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(new Md5Hash(keyStr, "SALT", 2).toBase64());
        return new SecretKeySpec(apiKeySecretBytes, algorithm.getJcaName());
    }

    /**
     * Generates salted key.
     *
     * @param algorithm {@link SignatureAlgorithm}
     * @param keyStr    the secret key string from properties
     * @param salt      e.g. user_id + e-mail
     * @return the salted key {@link Key}
     */
    public static Key generateSaltedKey(SignatureAlgorithm algorithm, String keyStr, String salt) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(new Md5Hash(keyStr, salt, 3).toBase64());
        return new SecretKeySpec(apiKeySecretBytes, algorithm.getJcaName());
    }

    //endregion

    //region creation functions

    /**
     * create a JWT(JsonWebToken)
     * jwt的签发
     *
     * @param id     token id
     * @param sub    subject：主题、用户(email 或 用户名)
     * @param config jwt 配置信息（key等）
     * @return jwt string
     */
    public static String createJwt(String id, String sub, JwtConfig config, Key key) {
        //sign JWT with ApiKey secret
        //set the JWT Claims
        JwtBuilder jwtBuilder = basicJwtBuilder(id, sub, config, key, false);
        //生成JWT
        return jwtBuilder.compact();
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
    public static String createUserJwt(UUID uuid, int userId, String sub, List<String> roles, JwtConfig config, Key key) {
        return generateUserToken(uuid, userId, sub, roles, config, key);
    }

    /**
     * 生成用于刷新用户access token的refresh token
     *
     * @param oldToken 旧的token
     * @param config   jwt 配置信息（key等）
     * @return jwt string
     */
    /*public static String createRefreshJwt(String oldToken, JwtConfig config) {
        JwtTokenInfo tokenInfo = parseUserJwt(oldToken, config.getKey(), config.getIssuer());
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
    public static String createRefreshJwt(UUID uuid, String sub, JwtConfig config, Key key) {
        JwtBuilder jwtBuilder = basicJwtBuilder(uuid.toString(), sub, config, key, true);
        //生成JWT
        return jwtBuilder.compact();
    }

    //endregion

    //region parse functions

    /**
     * validate and read JWT(JsonWebToken)
     *
     * @param token  Json Web Token String
     * @param secret 密钥（必须与签发时使用的密钥一致才能正确解析）
     * @param iss    issuer 签发者
     * @return {@link JwtTokenInfo}
     * @throws SignatureException    the signature exception
     * @throws MalformedJwtException the malformed jwt exception
     * @throws ExpiredJwtException   the expired jwt exception
     */
    public static JwtTokenInfo parseUserJwt(String token, Key secret, String iss)
            throws SignatureException, MalformedJwtException, ExpiredJwtException
    {
        // make sure that we can trust jwt
        Claims claims = parseJwt(token, secret, iss);
        JwtTokenInfo tokenInfo = new JwtTokenInfo();
        tokenInfo.setId(claims.getId());
        tokenInfo.setUserId((int) claims.get("userId"));
        tokenInfo.setSubject(claims.getSubject());
        tokenInfo.setIssuer(claims.getIssuer());
        tokenInfo.setIssued_at(claims.getIssuedAt());
//        tokenInfo.setRoles((List) claims.get("roles"));
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
        // (DatatypeConverter.parseBase64Binary(keyStr))
        return Jwts.parser().setSigningKey(secret)
                .requireIssuer(iss)
                .parseClaimsJws(token).getBody();
    }

    /**
     * <pre/>
     * 获取Token中的payload而不验证其有效性<br/>
     * 不需要提供 密钥
     * see https://github.com/jwtk/jjwt/issues/315
     * <p>
     * Jwt parsedToken = Jwts.parser().parse(splitToken[0] + "." + splitToken[1] + ".");
     * (Claims) jwt.getBody();
     *
     * @param token
     * @return claims {@link Claims}
     */
    public static Claims getClaimsWithoutKey(String token) {
        String[] splitToken = token.split("\\.");
        String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";

        DefaultJwtParser parser = new DefaultJwtParser();
        Jwt<?, ?> jwt = parser.parse(unsignedToken);
        Claims claims = (Claims) jwt.getBody();
        return claims;
    }


    //endregion

    //region utilities

    public static boolean isTokenExpired(String token, Key secret, String iss) {
        try {
            parseJwt(token, secret, iss);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }

    public static boolean verify(String token, Key secret, String iss) {
        try {
            parseJwt(token, secret, iss);
        } catch (SignatureException e) {
            return false;
        } catch (MalformedJwtException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        }
        return true;
    }

    public static int getUserIdFromToken(String token) {
        int id = (Integer) getClaimsWithoutKey(token).get("userId");
        return id;
    }

    /**
     * 获得user id 与 subject（e-mail）
     *
     * @param token
     * @return {@link Map<String, String>}
     * keySet = [id, sub]
     */
    public static Map<String, String> getUserIdAndSubFromToken(String token) {
        Claims claims = getClaimsWithoutKey(token);
        int id = (Integer) claims.get("userId");
        String sub = claims.getSubject();
        Map<String, String> m = new HashMap<>();
        m.put("id", id + "");
        m.put("sub", sub);
        return m;
    }

    /**
     * get token from Authorization string from request header
     *
     * @param authHeader
     * @return
     */
    public static String getTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(JwtConsts.TOKEN_PREFIX)) {
            return null;
        }
        // "Bearer "之后的部分，即token
        return authHeader.substring(authHeader.indexOf(" ") + 1);
    }

    //endregion

    //region private functions

    /**
     * 生成用户token
     *
     * @param uuid
     * @param userId
     * @param sub
     * @param roles
     * @param config
     * @return
     */
    private static String generateUserToken(UUID uuid, int userId, String sub, List<String> roles, JwtConfig config, Key key) {
        JwtBuilder jwtBuilder = basicJwtBuilder(uuid.toString(), sub, config, key, false);
        jwtBuilder.claim("roles", roles).claim("userId", userId);
        //生成JWT
        return jwtBuilder.compact();
    }

    /**
     * 生成具有基本信息的 JwtBuilder
     *
     * @param id
     * @param sub
     * @param config  jwt 配置信息
     * @param refresh 是否使用刷新时间
     * @return
     */
    private static JwtBuilder basicJwtBuilder(String id, String sub, JwtConfig config, Key key, boolean refresh) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //set the JWT Claims
        JwtBuilder jwtBuilder = Jwts.builder()
//                .setHeaderParam("typ", "JWT")
                .setId(id)
                //压缩
                .compressWith(CompressionCodecs.DEFLATE)
                //iat，发行时间
                .setIssuedAt(now)
                .setSubject(sub)
                .setIssuer(config.getIssuer())
                .signWith(JwtConsts.SIGNING_ALGORITHM, key);
        //if it has been specified, add the expiration
        long ttl;

        if (refresh) {
            ttl = config.getRefreshTTL();
        } else {
            ttl = config.getTtlMillis();
        }

        if (ttl >= 0) {
            long expMillis = nowMillis + ttl;
            //exp：Expiration time，过期时间
            jwtBuilder.setExpiration(new Date(expMillis));
        }
        return jwtBuilder;
    }


    //endregion
}
