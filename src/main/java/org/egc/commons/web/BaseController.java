package org.egc.commons.web;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 *
 * @Author houzhiwei
 * @Date 2016/11/3 22:39.
 */
public class BaseController
{
    public static String msg = "msg";

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    //@ModelAttribute
    // 1)放置在方法的形参上：表示引用Model中的数据
    // 2)放置在方法上面：表示请求该类的每个Action前都会首先执行它，也可以将一些准备数据的操作放置在该方法里面。
    // 每次都需要重新获取,因为spring为单例
    @ModelAttribute
    protected void setRequestResponse(HttpServletResponse res, HttpServletRequest req)
    {
        this.request = req;
        this.response = res;
    }

}
