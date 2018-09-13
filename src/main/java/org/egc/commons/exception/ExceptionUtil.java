package org.egc.commons.exception;

import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 关于异常的工具类.
 *
 * @author calvin
 * @version 2013 -01-15
 */
public class ExceptionUtil
{
    /**
     * 将CheckedException转换为UncheckedException.
     *
     * @param e the e
     * @return the runtime exception
     */
    public static RuntimeException unchecked(Exception e)
    {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    /**
     * 将ErrorStack转化为String.
     *
     * @param e the e
     * @return the stack trace as string
     */
    public static String getStackTraceAsString(Throwable e)
    {
        if (e == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 判断异常是否由某些底层的异常引起.
     *
     * @param ex                    the ex
     * @param causeExceptionClasses the cause exception classes
     * @return the boolean
     */
    public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses)
    {
        Throwable cause = ex.getCause();
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * 在request中获取异常类
     *
     * @param request the request
     * @return throwable
     */
    public static Throwable getThrowable(HttpServletRequest request)
    {
        Throwable ex = null;
        if (request.getAttribute("exception") != null) {
            ex = (Throwable) request.getAttribute("exception");
        } else if (request.getAttribute("javax.servlet.error.exception") != null) {
            ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
        }
        return ex;
    }

    /**
     * Throw and logging exception.
     *
     * @param e              the e
     * @param logger         the logger
     * @param friendlyErrMsg the friendly err msg
     */
    public static void throwAndLoggingException(Throwable e, Logger logger, String friendlyErrMsg)
    {
        e.printStackTrace();
        logger.info(getStackTraceAsString(e));
        throw new BusinessException(e, friendlyErrMsg);
    }
}
