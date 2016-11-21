package org.egc.commons.Web;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.InetAddress;

/**
 * TODO
 *
 * @Author houzhiwei
 * @Date 2016/6/22 17:51.
 */
public class WebUtil
{
    /**
     * 访问是否为Ajax
     *
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request)
    {
        boolean flag = false;
        String type = request.getHeader("X-Requested-With");
        if (type != null && type.equalsIgnoreCase("XMLHttpRequest"))
            flag = true;
        return flag;
    }
    public static void writeJson(HttpServletResponse response, Object jsonObj) throws Exception
    {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(jsonObj));
        writer.flush();
        writer.close();
    }
    /**
     * 获取客户端访问ip<br/>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效
     *
     * @param request
     * @return ip
     */
    public static String getClientIP(HttpServletRequest request) throws  Exception
    {
        String ip = request.getHeader("X-Real-IP");
        if (Strings.isNullOrEmpty(ip)|| "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            if (!Strings.isNullOrEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个IP值，第一个为真实IP。
                int index = ip.indexOf(',');
                if (index != -1)
                    ip = ip.substring(0, index);
            } else
                ip = request.getRemoteAddr();
        }
        //本地localhost访问
        if("127.0.0.1".equalsIgnoreCase(ip)||"0:0:0:0:0:0:0:1".equalsIgnoreCase(ip))
        {
            ip= InetAddress.getLocalHost().getHostAddress();
        }
        return ip;
    }
}
