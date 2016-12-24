package org.egc.commons.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * TODO
 * 路径获取工具类
 * @author houzhiwei
 * @date 2016/11/23 16:06
 */
public class PathUtil
{
    /**
     * 项目根路径（绝对）
     *
     * @return
     * @throws IOException
     */
    public static String getProjectRoot() throws IOException
    {
        File file = new File("");
        return file.getCanonicalPath();
    }

    /**
     * （编译后）类根路径
     * <pre/>classes或test-classes
     *
     * @param clazz
     * @return
     */
    public static String getClassPath(Class clazz)
    {
        return clazz.getClass().getResource("/").getPath();
    }

    /**
     * Gets package path.
     *
     * @param clazz the clazz
     * @return the package path
     */
    public static String getPackagePath(Class clazz)
    {
        return clazz.getClass().getResource("").getPath();
    }

    /**
     * Gets WEB-INF path.
     *
     * @param request the request
     * @return the web inf root
     */
    public static String getWebInfRoot(HttpServletRequest request)
    {
        return request.getSession().getServletContext().getRealPath("/");
    }

    public static String getRelativePath()
    {
        return System.getProperty("user.dir");
    }

    /**
     * Gets context path.
     *
     * @param request the request
     * @return the context path
     */
    public static String getContextPath(HttpServletRequest request)
    {
        return request.getContextPath();
    }
}
