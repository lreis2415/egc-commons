package org.egc.commons.security;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JSON Web Token constants
 *
 * @author houzhiwei
 * @date 2018/5/6 10:09
 */
public class JwtConsts {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_PREFIX = "refresh_token ";
    public static final String HEADER_STRING = "Authorization";
    /**
     *  jwt 签名算法
     */
    public static final SignatureAlgorithm SIGNING_ALGORITHM_HS256 = SignatureAlgorithm.HS256;
    public static final SignatureAlgorithm SIGNING_ALGORITHM = SignatureAlgorithm.HS512;
}
