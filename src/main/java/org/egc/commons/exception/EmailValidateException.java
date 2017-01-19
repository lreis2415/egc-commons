package org.egc.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 邮箱验证异常
 * 邮箱已存在时抛出异常，并自定义原因
 *
 * @author houzhiwei
 * @date 2017/1/20 0:12
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "This E-Mail has already bean used!")
public class EmailValidateException extends RuntimeException
{
}
