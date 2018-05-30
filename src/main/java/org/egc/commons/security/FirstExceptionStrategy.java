package org.egc.commons.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.realm.Realm;

/**
 * 参考 https://stackoverflow.com/questions/13446436/apache-shiro-exception-handling-with-multiple-realms
 * [must] one Realm per Token type.
 * 多个realm时，shiro 只会抛出一般化的 AuthenticationException，不会区分 UnknownAccountException 之类异常
 * @author houzhiwei
 * @date 2018/5/30 7:56
 */
public class FirstExceptionStrategy extends FirstSuccessfulStrategy {
    @Override
    public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
        if ((t != null) && (t instanceof AuthenticationException)) {
            throw (AuthenticationException) t;
        }
        return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
    }
}
