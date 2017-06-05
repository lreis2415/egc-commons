package org.egc.commons.web;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Map;

/**
 * 用于组织数据为一个JSON对象
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
     * default to 200(OK)
     */
    private int status = HttpServletResponse.SC_OK;

    private HttpStatus httpStatus;

    public HttpStatus getHttpStatus()
    {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus)
    {
        this.status = httpStatus.value();
        this.httpStatus = httpStatus;
    }

    public JsonResult()
    {
        super();
    }

    public JsonResult(String msg, int status)
    {
        this.msg = msg;
        this.status = status;
    }

    public JsonResult(HttpStatus httpStatus)
    {
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
    }

    public JsonResult(String msg, HttpStatus httpStatus)
    {
        this.httpStatus = httpStatus;
        this.msg = msg;
        this.status = httpStatus.value();
    }

    public JsonResult(int status)
    {
        this.status = status;
    }

    public JsonResult(String msg)
    {
        this.msg = msg;
        this.status = HttpServletResponse.SC_OK;
    }

    public JsonResult(Object data)
    {
        this.data = data;
        this.status = HttpServletResponse.SC_OK;
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

    public static FastJsonJsonView jsonView(Object data, String msg)
    {
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attrs = Maps.newHashMap();
        attrs.put("data", data);
        attrs.put("msg", msg);
        return view;
    }
}
