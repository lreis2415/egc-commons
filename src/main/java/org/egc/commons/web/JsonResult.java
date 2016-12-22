package org.egc.commons.web;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * TODO
 *
 * @Author houzhiwei
 * @Date 2016/11/4 21:52.
 */
public class JsonResult implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Object data;
    private String msg;
    /**
     * redirect URL
     */
    private String url;
    /**
     * status code
     */
    private HttpStatus status;

    public JsonResult()
    {
        super();
    }

    public JsonResult(String msg, HttpStatus status)
    {
        this.msg = msg;
        this.status = status;
    }

    public JsonResult(HttpStatus status)
    {
        this.status = status;
    }

    public JsonResult(String msg)
    {
        this.msg = msg;
        this.status = HttpStatus.OK;
    }

    public JsonResult(Object data)
    {
        this.data = data;
        this.status = HttpStatus.OK;
    }

    public HttpStatus getStatus()
    {
        return status;
    }

    public void setStatus(HttpStatus status)
    {
        this.status = status;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }


}
