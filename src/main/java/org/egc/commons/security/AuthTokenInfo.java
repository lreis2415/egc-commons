package org.egc.commons.security;

import java.util.Date;
import java.util.List;

/**
 * Json Web Token detail info
 *
 * @author houzhiwei
 * @date 2017/6/16 14:14
 */
public class AuthTokenInfo {

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

	/**
	 * user roles
	 */
	private List<String> roles;

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getNot_before() {
		return not_before;
	}

	public void setNot_before(String not_before) {
		this.not_before = not_before;
	}

	public Date getIssued_at() {
		return issued_at;
	}

	public void setIssued_at(Date issued_at) {
		this.issued_at = issued_at;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
