package org.egc.commons.exception;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.UnexpectedTypeException;
import java.util.List;

/**
 * Description:
 * <pre>
 * 统一对controller参数校验（@valid 或 @Validated）异常（ 400 Bad Request）进行处理
 * 使用时需要在 spring mvc 配置中声明，使spring能够扫描到此类，如
 *  {@code
 *    <context:component-scan base-package="org.egc.commons.exception" use-default-filters="false">
 *       <context:include-filter type="assignable" expression="org.egc.commons.exception.ValidationExceptionHandler"/>
 *    </context:component-scan>
 * }*
 * 参考：https://www.cnblogs.com/woshimrf/p/spring-web-400.html
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /9/17 8:20
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@ResponseBody
public class ValidationExceptionHandler {

    /**
     * The Logger.
     */
    Logger logger = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    /**
     * 参数校验异常
     *
     * @param e the MethodArgumentNotValidException
     * @return json error result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonErrorResult argumentNotValidException(MethodArgumentNotValidException e) {

        // 获取字段绑定的异常信息，如 @Size(min=1, max=6,message="Size should be between 1 to 6")
        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = "[ Argument Not Valid ] ";

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage += fieldError.getDefaultMessage() + ", ";
        }

        logger.error(errorMesssage);
        return new JsonErrorResult(errorMesssage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Process argument type mismatch exception.
     *
     * @param e the MethodArgumentTypeMismatchException
     * @return the json error result
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonErrorResult argumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        logger.error("[ Argument Type Mismatch ] {}", e);
        return new JsonErrorResult("[ Argument Type Mismatch ]" + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 无法反序列化JSON数据，相对应的另一个异常为 HttpMessageNotWritableException
     *
     * @param e the HttpMessageNotReadableException
     * @return json error result
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JsonErrorResult messageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("JSON parse error: {}", e);
        return new JsonErrorResult("[ JSON Parse Error ] " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Spring Controller 参数绑定异常.
     *
     * @param e    the BindException
     * @param resp the resp
     * @return the json object
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JSONObject bindException(BindException e, HttpServletResponse resp) {

        JSONObject jsonObject = new JSONObject();
        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        resp.setCharacterEncoding("UTF-8");

        List<ObjectError> errors = e.getAllErrors();
        JSONArray newErrors = new JSONArray();

        // 组装错误信息
        for (ObjectError error : errors) {
            String objString = JSONObject.toJSONString(error);
            JSONObject errJSON = JSONObject.parseObject(objString);
            errJSON.remove("arguments");
            errJSON.remove("bindingFailure");
            errJSON.remove("codes");
            errJSON.remove("objectName");
            errJSON.remove("code");
            newErrors.add(errJSON);
        }

        jsonObject.put("status", HttpStatus.BAD_REQUEST.value());
        jsonObject.put("errors", newErrors);
        logger.error("[ Bind Exception ] {}", newErrors);
        return jsonObject;
    }

    /**
     * Illegal params exception.
     *
     * @param e the UnexpectedTypeException
     * @return the json error result
     */
    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonErrorResult illegalParamsException(UnexpectedTypeException e) {
        logger.error("[ Unexpected Type ] {}", e);
        return new JsonErrorResult("[ Illegal_params ] " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
