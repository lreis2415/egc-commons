package org.egc.commons.exception;

import com.alibaba.fastjson.JSONArray;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/1/19 15:30
 */
public class JsonErrorResult implements Serializable {
    private Object msg;

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
    private String cause;

    public JsonErrorResult(String cause, String msg, HttpStatus status) {
        this.cause = cause;
        this.status = status;
        this.msg = msg;
    }

    public JsonErrorResult(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
    }

    public JsonErrorResult(JSONArray errors, HttpStatus status) {
        this.msg = errors;
        this.status = status;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "JsonErrorResult{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                ", cause='" + cause + '\'' +
                '}';
    }


}
