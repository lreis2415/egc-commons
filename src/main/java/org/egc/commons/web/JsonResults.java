package org.egc.commons.web;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

/**
 * <pre>
 * 用于组织数据为一个JSON对象
 * 与 {@link JsonResult} 基本是一样的，但是 T 相比 Object 可以避免类型转换错误
 * @author houzhiwei
 * @date 2020-5-18 15:25:37
 */
public class JsonResults<T> implements Serializable {
    private T data;
    private String msg;
    /**
     * redirect URL
     */
    private String url;

    /**
     * Getter for property 'status'.
     *
     * @return Value for property 'status'.
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Setter for property 'status'.
     *
     * @param status Value to set for property 'status'.
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    private HttpStatus status;

    public JsonResults() {
        super();
    }

    public JsonResults(String msg) {
        this.msg = msg;
    }

    public JsonResults(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
    }

    public JsonResults(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static FastJsonJsonView jsonView(Object data, String msg) {
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attrs = Maps.newHashMap();
        attrs.put("data", data);
        attrs.put("msg", msg);
        view.setAttributesMap(attrs);
        return view;
    }
}
