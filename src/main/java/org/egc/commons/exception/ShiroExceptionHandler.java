package org.egc.commons.exception;

import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <pre/>
 * Controller的全局异常处理
 * `@ControllerAdvice` 监控所有的`@RequestMapping`方法，也可以指定过滤的条件。
 * https://juejin.im/entry/5a5f3ccff265da3e261bfe61
 * http://blog.didispace.com/springbootexception/
 *
 * @author houzhiwei
 * @date 2018 /5/18 19:41
 */
@ControllerAdvice
public class ShiroExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ShiroExceptionHandler.class);

    /**
     * Authentication exception.
     *
     * @param e the e
     * @return the json error result
     */
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public JsonErrorResult authenticationException(AuthenticationException e) {
        String msg = e.getMessage();
        logger.error("Authentication Error: ", e);
        return new JsonErrorResult("[ Authentication Error ] " + msg, HttpStatus.UNAUTHORIZED);
    }
}
