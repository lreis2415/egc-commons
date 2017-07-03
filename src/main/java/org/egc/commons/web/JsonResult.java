package org.egc.commons.web;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.google.common.collect.Maps;

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

    public JsonResult()
    {
        super();
    }

    public JsonResult(String msg)
    {
        this.msg = msg;
    }

    public JsonResult(Object data)
    {
        this.data = data;
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
