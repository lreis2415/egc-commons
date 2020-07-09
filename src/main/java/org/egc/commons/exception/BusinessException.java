package org.egc.commons.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 业务异常处理类
 *
 * @author houzhiwei
 * @date 2016/12/8 14:43
 * @link http ://blog.csdn.net/king87130/article/details/8011843
 */
public class BusinessException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(BusinessException.class);
    private int errorCode;

    /**
     * 是否打印异常信息
     * print exception or not
     */
    private boolean print = false;

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public BusinessException(String friendlyErrMsg)
    {
        super(createFriendlyErrMsg(friendlyErrMsg));
    }

    public BusinessException(String friendlyErrMsg, HttpStatus status)
    {
        super(createFriendlyErrMsg(friendlyErrMsg));
        this.errorCode = status.value();
        this.httpStatus = status;
        logger.error(friendlyErrMsg);
    }


    /**
     * Instantiates a new Business exception.
     *
     * @param friendlyErrMsg the friendly exception message
     * @param status         http status
     * @param print          print exception or not
     */
    public BusinessException(String friendlyErrMsg, HttpStatus status, boolean print)
    {
        super(createFriendlyErrMsg(friendlyErrMsg));
        this.errorCode = status.value();
        this.httpStatus = status;
        this.print = print;
        logger.error(friendlyErrMsg);
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
        logger.error(status.getReasonPhrase(), throwable);
    }

    public BusinessException(Throwable throwable, String friendlyErrMsg)
    {
        super(friendlyErrMsg, throwable);
    }

    public BusinessException(Throwable throwable, String friendlyErrMsg, boolean print)
    {
        super(friendlyErrMsg, throwable);
        this.print = print;
        logger.error(friendlyErrMsg, throwable);
    }

    public BusinessException(Throwable throwable, String friendlyErrMsg, HttpStatus status)
    {
        super(friendlyErrMsg, throwable);
        this.errorCode = status.value();
        this.httpStatus = status;
        logger.error(friendlyErrMsg, throwable);
    }

    /**
     * 友好的错误提示
     *
     * @param msgBody
     * @return
     */
    private static String createFriendlyErrMsg(String msgBody)
    {
//        String prefixStr = "Sorry, ";
//        String suffixStr = ". <br/>Try again later or report the error to us!";
        StringBuffer friendlyErrMsg = new StringBuffer();

//        friendlyErrMsg.append(prefixStr);

        friendlyErrMsg.append(msgBody);

//        friendlyErrMsg.append(suffixStr);

        return friendlyErrMsg.toString();
    }

    /**
     * Getter for property 'errorCode'.
     *
     * @return Value for property 'errorCode'.
     */
    public int getErrorCode()
    {
        return errorCode;
    }

    /**
     * Setter for property 'errorCode'.
     *
     * @param errorCode Value to set for property 'errorCode'.
     */
    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    /**
     * Getter for property 'httpStatus'.
     *
     * @return Value for property 'httpStatus'.
     */
    public HttpStatus getHttpStatus()
    {
        return httpStatus;
    }

    /**
     * Setter for property 'httpStatus'.
     *
     * @param httpStatus Value to set for property 'httpStatus'.
     */
    public void setHttpStatus(HttpStatus httpStatus)
    {
        this.httpStatus = httpStatus;
    }

    /**
     * Getter for property 'print'.
     *
     * @return Value for property 'print'.
     */
    public boolean isPrint() {
        return print;
    }

    /**
     * Setter for property 'print'.
     *
     * @param print Value to set for property 'print'.
     */
    public void setPrint(boolean print) {
        this.print = print;
    }
}
