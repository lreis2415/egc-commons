package org.egc.commons.web;

import org.egc.commons.security.JwtUtil;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 *
 * @Author houzhiwei
 * @Date 2016/11/3 22:39.
 */
public abstract class BaseController {

    /**
     * 只返回简单的消息时使用<br/>
     * 返回数据的情况下请使用 json
     */
    protected String msg;

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected JsonResult json;

    /**
     * <pre/>
     * json web token
     * 只有在使用jwt进行登录验证时才有值，否则为 null
     */
    protected String token;

    /**
      @ModelAttribute
      1)放置在方法的形参上：表示引用Model中的数据
      2)放置在方法上面：表示请求该类的每个Action前都会首先执行它，也可以将一些准备数据的操作放置在该方法里面。
      每次都需要重新获取,因为spring为单例
     */
    @ModelAttribute
    protected void setRequestResponse(HttpServletResponse res, HttpServletRequest req)
    {
        this.request = req;
        this.response = res;
        json = new JsonResult();
        msg = "message";
        this.token = getToken();
    }

    /**
     * get json web token from request header
     *
     * @return
     */
    private String getToken() {
        String jwt = this.request.getHeader(JwtUtil.HEADER_STRING);
        if (jwt == null || !jwt.startsWith(JwtUtil.TOKEN_PREFIX)) {
            return null;
        }
        return jwt.substring(jwt.indexOf(" ")); // "Bearer "之后的部分，即token
    }
}
