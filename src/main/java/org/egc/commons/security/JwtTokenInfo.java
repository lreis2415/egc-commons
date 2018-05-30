package org.egc.commons.security;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Json Web Token detail info
 *
 * @author houzhiwei
 * @date 2017 /6/16 14:14
 */
public class JwtTokenInfo implements Serializable {

    private static final long serialVersionUID = -6494580347610919968L;

    public JwtTokenInfo(String id,String subject){
        this.id =id;
        this.subject = subject;
    }

    /**
     * 该JWT的签发者
     * JWT {@code Issuer} claims parameter name: <code>"iss"</code>
     */
    private String issuer = "iss";

    /**
     * 该JWT所面向的用户
     * JWT {@code Subject} claims parameter name: <code>"sub"</code>
     */
    private String subject = "sub";

    /**
     * 接收该JWT的一方
     * JWT {@code Audience} claims parameter name: <code>"aud"</code>
     */
    private String audience = "aud";

    /**
     * 过期时间，Unix时间戳
     * JWT {@code Expiration} claims parameter name: <code>"exp"</code>
     */
    private Date expiration;

    /**
     * JWT {@code Not Before} claims parameter name: <code>"nbf"</code>
     */
    private String not_before = "nbf";

    /**
     * 签发时间
     * JWT {@code Issued At} claims parameter name: <code>"iat"</code>
     */
    private Date issued_at;

    /**
     * JWT {@code JWT ID} claims parameter name: <code>"jti"</code>
     */
    private String id = "jti";

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int userId;

    /**
     * user roles
     */
    private List<String> roles;

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
     * Gets subject(in some scenarios, it's user's email or username).
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject.
     *
     * @param subject the subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets audience.
     *
     * @return the audience
     */
    public String getAudience() {
        return audience;
    }

    /**
     * Sets audience.
     *
     * @param audience the audience
     */
    public void setAudience(String audience) {
        this.audience = audience;
    }

    /**
     * Gets expiration.
     *
     * @return the expiration
     */
    public Date getExpiration() {
        return expiration;
    }

    /**
     * Sets expiration.
     *
     * @param expiration the expiration
     */
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    /**
     * Gets not before.
     *
     * @return the not before
     */
    public String getNot_before() {
        return not_before;
    }

    /**
     * Sets not before.
     *
     * @param not_before the not before
     */
    public void setNot_before(String not_before) {
        this.not_before = not_before;
    }

    /**
     * Gets issued at.
     *
     * @return the issued at
     */
    public Date getIssued_at() {
        return issued_at;
    }

    /**
     * Sets issued at.
     *
     * @param issued_at the issued at
     */
    public void setIssued_at(Date issued_at) {
        this.issued_at = issued_at;
    }

    /**
     * Gets id (in some scenarios, it's user id).
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets roles.
     *
     * @return the roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * Sets roles.
     *
     * @param roles the roles
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
