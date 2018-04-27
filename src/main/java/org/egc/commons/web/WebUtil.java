package org.egc.commons.web;

import com.alibaba.fastjson.JSON;
import org.egc.commons.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * TODO
 *
 * @Author houzhiwei
 * @Date 2016/6/22 17:51.
 */
public class WebUtil {
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

    public static void writeJson(HttpServletResponse response, Object jsonObj)
    {
        response.setContentType("application/json;charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(jsonObj));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new BusinessException(new Throwable(), "Can not get a PrintWriter to write JSON!");
        }
    }

    private static final String[] HEADERS_TO_TRY = {
            "X-Real-IP",
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};

    /**
     * 获取客户端访问ip<br/>
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效
     * http://stackoverflow.com/questions/16558869/getting-ip-address-of-client
     *
     * @param request
     * @return ip
     */
    public static String getClientIP(HttpServletRequest request) throws UnknownHostException
    {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            //本地localhost访问
            if ("127.0.0.1".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
                if (InetAddress.getLocalHost() instanceof Inet4Address)
                    return Inet4Address.getLocalHost().getHostAddress();
                else if (InetAddress.getLocalHost() instanceof Inet6Address)
                    return Inet6Address.getLocalHost().getHostAddress();
                System.out.println("getLocalHost: " + ip);
                return InetAddress.getLocalHost().getHostAddress();
            }
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个IP值，第一个为真实IP。
                int index = ip.indexOf(',');
                if (index != -1)
                    ip = ip.substring(0, index);
//                System.out.println("ip: " + ip);
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    /**
     * get Client InetAddress
     *
     * @param request
     * @return InetAddress
     * @throws BusinessException(UnknownHostException)
     */
    public static InetAddress getClientIPAddress(HttpServletRequest request)
    {
        try {
            InetAddress address = InetAddress.getByName(getClientIP(request));
            // for 这部分没有用？
            for (String header : HEADERS_TO_TRY) {
                String ip = request.getHeader(header);
                //本地localhost访问
                if ("127.0.0.1".equalsIgnoreCase(ip) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
                    System.out.println("localhost  " + ip);
                    if (InetAddress.getLocalHost() instanceof Inet4Address)
                        return Inet4Address.getLocalHost();
                    else if (InetAddress.getLocalHost() instanceof Inet6Address)
                        return Inet6Address.getLocalHost();
                    return InetAddress.getLocalHost();
                }
            }
            if (address instanceof Inet4Address) {
//                System.out.println("ipv4 getByName getHostAddress: " + address.getHostAddress());
                return Inet4Address.getByName(getClientIP(request));
            } else if (address instanceof Inet6Address) {
//                System.out.println("ipv6 getByName getHostAddress: " + address.getHostAddress());
                return Inet6Address.getByName(getClientIP(request));
            }
            return address;
        } catch (UnknownHostException ex) {
            throw new BusinessException(ex, "Can not get host address!");
        }
    }

    public static String getIPFromInetAddress(InetAddress inetAddress)
    {
        return inetAddress.getHostAddress();
    }
}