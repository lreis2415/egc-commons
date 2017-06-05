package org.egc.commons.exception;

import org.springframework.http.HttpStatus;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/1/19 15:30
 */
public class ErrorResult
{
    private String msg;
    private int errorCode;
    private String cause;
    private HttpStatus status;

    public ErrorResult(HttpStatus status)
    {
        this.status = status;
    }

    public ErrorResult(HttpStatus status, String cause)
    {
        this.status = status;
        this.cause = cause;
    }

    public HttpStatus getStatus()
    {
        return status;
    }

    public void setStatus(HttpStatus status)
    {
        this.status = status;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getCause()
    {
        return cause;
    }

    public void setCause(String cause)
    {
        this.cause = cause;
    }

    @Override
    public String toString()
    {
        return "ErrorResult{" +
                "msg='" + msg + '\'' +
                ", errorCode=" + errorCode +
                ", cause='" + cause + '\'' +
                ", status=" + status +
                '}';
    }
}
