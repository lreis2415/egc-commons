package org.egc.commons.exception;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.google.common.collect.Maps;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <pre>
 * 全局异常处理，返回json数据
 * 不需要捕获异常，抛出即可
 * 需要在spring中配置bean
 * {@code
 *  <bean id="exceptionHandler" class="org.egc.commons.exception.JsonHandlerExceptionResolver"/>
 * }
 * 参考 http://blog.csdn.net/chwshuang/article/details/48089203
 * </pre>
 *
 * @author houzhiwei
 * @date 2016/12/6 15:49
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class JsonHandlerExceptionResolver implements HandlerExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(JsonHandlerExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();
//        使用jsonview，直接返回Json数据。也可以返回普通的modelandview，使用jsp页面视图
        FastJsonJsonView jsonView = new FastJsonJsonView();
        Map<String, Object> attrs = Maps.newHashMap();
        attrs.put("msg", ex.getMessage());
        attrs.put("cause", ex.getCause());
        jsonView.setAttributesMap(attrs);
        mv.setView(jsonView);
        if (ex instanceof BusinessException) {
            HttpStatus status = ((BusinessException) ex).getHttpStatus();
            if (status != null) {
                mv.setStatus(status);
            }
        }
        return mv;
    }

}
