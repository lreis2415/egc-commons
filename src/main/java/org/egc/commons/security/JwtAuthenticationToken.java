package org.egc.commons.security;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 用于 shiro 验证 JWT 的token
 *
 * @author houzhiwei
 * @date 2018/5/5 20:55
 */
public class JwtAuthenticationToken implements AuthenticationToken {
    private String token;
    private String principal;

    public JwtAuthenticationToken(String principal, String token) {
        this.principal = principal;
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
