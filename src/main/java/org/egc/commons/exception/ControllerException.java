package org.egc.commons.exception;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * TODO test
 * @author houzhiwei
 * @date 2020/10/6 10:16
 */
@ControllerAdvice
public class ControllerException {

    /**
     * 专门用来捕获和处理Controller层的空指针异常
     *
     * @param e the NullPointerException
     * @return model and view
     */
    @ExceptionHandler(NullPointerException.class)
    public ModelAndView nullPointerExceptionHandler(NullPointerException e) {
        ModelAndView mv = new ModelAndView(new FastJsonJsonView());
        mv.addObject("success", false);
        mv.addObject("msg", e.getLocalizedMessage());
        return mv;
    }

    /**
     * 专门用来捕获和处理Controller层的运行时异常
     *
     * @param e the RuntimeException
     * @return the model and view
     */
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView runtimeExceptionHandler(RuntimeException e) {
        ModelAndView mv = new ModelAndView(new FastJsonJsonView());
        mv.addObject("success", false);
        mv.addObject("msg", e.getLocalizedMessage());
        return mv;
    }

    /**
     * 专门用来捕获和处理Controller层的异常
     *
     * @param e the Exception
     * @return the model and view
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(Exception e) {
        ModelAndView mv = new ModelAndView(new FastJsonJsonView());
        mv.addObject("success", false);
        mv.addObject("msg", e.getLocalizedMessage());
        return mv;
    }
}

