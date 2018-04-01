package org.egc.commons.exception;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <pre/>
 * 全局异常处理，返回json数据
 * 不需要捕获异常，抛出即可
 * 需要在spring中配置bean
 * 参考 http://blog.csdn.net/chwshuang/article/details/48089203
 *
 * @author houzhiwei
 * @date 2016/12/6 15:49
 */
public class JsonHandlerExceptionResolver implements HandlerExceptionResolver
{
    private static final Logger logger = LoggerFactory.getLogger(JsonHandlerExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    {
        ModelAndView mv = new ModelAndView();
//        使用jsonview，直接返回Json数据。也可以返回普通的modelandview，使用jsp页面视图
        FastJsonJsonView jsonView = new FastJsonJsonView();
        Map<String, Object> attrs = Maps.newHashMap();
        attrs.put("msg", ex.getMessage());
        attrs.put("cause", ex.getCause());
//        attrs.put("localMsg", ex.getLocalizedMessage());
        jsonView.setAttributesMap(attrs);
        mv.setView(jsonView);
        if (ex instanceof BusinessException) {
            HttpStatus status = ((BusinessException) ex).getHttpStatus();
            if (status != null)
                mv.setStatus(status);
            if(((BusinessException) ex).isPrint())
                logger.error("Exception Log: ", ex);
        }
        return mv;
    }
}
