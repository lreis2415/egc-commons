package org.egc.commons.exception;

import org.springframework.http.HttpStatus;

/**
 * TODO
 * 业务异常处理类
 *
 * @author houzhiwei
 * @date 2016/12/8 14:43
 * @link http://blog.csdn.net/king87130/article/details/8011843
 */
public class BusinessException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private int errorCode;

    private HttpStatus httpStatus;

    public BusinessException(String friendlyErrMsg)
    {
        super(createFriendlyErrMsg(friendlyErrMsg));
    }

    public BusinessException(String friendlyErrMsg, HttpStatus status)
    {
        super(createFriendlyErrMsg(friendlyErrMsg));
        this.errorCode = status.value();
        this.httpStatus = status;
    }

    public BusinessException(Throwable throwable)
    {
        super(throwable);
    }

    public BusinessException(Throwable throwable, HttpStatus status)
    {
        super(throwable);
        this.errorCode = status.value();
        this.httpStatus = status;
    }

    public BusinessException(Throwable throwable, String friendlyErrMsg)
    {
        super(friendlyErrMsg, throwable);
    }

    public BusinessException(Throwable throwable, String friendlyErrMsg, HttpStatus status)
    {
        super(friendlyErrMsg, throwable);
        this.errorCode = status.value();
        this.httpStatus = status;
    }

    /**
     * 友好的错误提示
     *
     * @param msgBody
     * @return
     */
    private static String createFriendlyErrMsg(String msgBody)
    {
        String prefixStr = "Sorry, ";
        String suffixStr = ". Try again later or report the error to us!";

        StringBuffer friendlyErrMsg = new StringBuffer("");

        friendlyErrMsg.append(prefixStr);

        friendlyErrMsg.append(msgBody);

        friendlyErrMsg.append(suffixStr);

        return friendlyErrMsg.toString();
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus()
    {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus)
    {
        this.httpStatus = httpStatus;
    }
}
