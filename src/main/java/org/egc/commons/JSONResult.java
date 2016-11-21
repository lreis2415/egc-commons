package org.egc.commons;

import org.egc.commons.Web.Status;

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
    private String url;
    private int status;

    public JsonResult()
    {
        super();
    }

    public JsonResult(String msg, int status)
    {
        this.msg = msg;
        this.status = status;
    }

    public JsonResult(int status)
    {
        this.status = status;
    }

    public JsonResult(String msg)
    {
        this.msg = msg;
        this.status = Status.OK;
    }

    public JsonResult(Object data)
    {
        this.data = data;
        this.status = Status.OK;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
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
