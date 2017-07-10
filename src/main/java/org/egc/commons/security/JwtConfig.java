package org.egc.commons.security;

import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;

/**
 * JSON WEB TOKEN Configuration
 *
 * @author houzhiwei
 * @date 2017 /7/7 16:35
 */
public class JwtConfig {
    /**
     * 签名密钥 （配置文件中设置或程序生成）
     */
    private Key key;
    /**
     * issuer 签发者（配置文件中设置）
     */
    private String issuer;
    /**
     * Signature Algorithm 签名算法
     */
    private SignatureAlgorithm alg;
    /**
     * 过期时长（毫秒）（配置文件中设置）
     */
    private long ttlMillis;
    /**
     * 刷新时长
     */
    private long refreshTTL;

    /**
     * Gets key.
     *
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Gets issuer.
     *
     * @return the issuer
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets issuer.
     *
     * @param issuer the issuer
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
     * Gets alg.
     *
     * @return the alg
     */
    public SignatureAlgorithm getAlg() {
        return alg;
    }

    /**
     * Sets alg.
     *
     * @param alg the alg
     */
    public void setAlg(SignatureAlgorithm alg) {
        this.alg = alg;
    }

    /**
     * Gets ttl millis.
     *
     * @return the ttl millis
     */
    public long getTtlMillis() {
        return ttlMillis;
    }

    /**
     * Sets ttl millis.
     *
     * @param ttlMillis the ttl millis
     */
    public void setTtlMillis(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    /**
     * Gets refresh ttl.
     *
     * @return the refresh ttl
     */
    public long getRefreshTTL() {
        return refreshTTL;
    }

    /**
     * Sets refresh ttl.
     *
     * @param refreshTTL the refresh ttl
     */
    public void setRefreshTTL(long refreshTTL) {
        this.refreshTTL = refreshTTL;
    }
}
