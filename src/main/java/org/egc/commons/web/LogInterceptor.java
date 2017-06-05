package org.egc.commons.web;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * 统一日志处理
 * 需在spring配置文件中配置
 * @author houzhiwei
 * @date 2016/12/8 14:29
 * @link http://blog.csdn.net/king87130/article/details/8011843
 */
public class LogInterceptor implements MethodInterceptor
{
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        Logger logger = LoggerFactory.getLogger(invocation.getClass());
        logger.info("|--------------------------------------------------|");
        logger.info("|--- START LOGGING: "+invocation.getMethod().getName());
        Object obj = invocation.proceed();// execute method

        return obj;
    }
}
