package org.egc.commons.exception;

import java.io.Serializable;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/1/19 15:30
 */
public class JsonErrorResult  implements Serializable
{
    private String msg;
    private int code;
    private String cause;

    public JsonErrorResult(String cause)
    {
        this.cause = cause;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
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
        return "JsonErrorResult{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", cause='" + cause + '\'' +
                '}';
    }
}
