package org.egc.commons.utils;

import com.google.common.base.Preconditions;

import java.io.*;
import java.util.Properties;

/**
 * <pre>
 * 属性配置文件工具类
 * Properties Utility
 * @author houzhiwei
 * @date 2016/11/23
 */
public class PropertiesUtil
{
    public static String getProperty(Properties properties, String key)
    {
        String property = properties.getProperty(key);
        return property;
    }

    /**
     * 从/config目录下获取属性
     *
     * @param key      属性键 Property key
     * @param filename 属性文件名（不用后缀）/ filename without ".properties"
     * @return 属性值 Property value
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static String getPropertyFromConfig(String key, String filename) throws IOException, FileNotFoundException
    {
        String path = PathUtil.getClassPath(PropertiesUtil.class) + "/config/";
        String fullFilename = path + filename + ".properties";
        Properties properties = new Properties();
        File propertiesFile = new File(fullFilename);
        FileInputStream fis = new FileInputStream(propertiesFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        properties.load(bis);
        String p = properties.getProperty(key);
        fis.close();
        bis.close();
        return p;
    }

    /**
     * <pre/>读取src/main/resources/config下面的properties文件
     *
     * @param filename 文件名(不用后缀)/ filename without ".properties"
     * @return Properties
     * @throws IOException
     */
    public static Properties readPropertiesFromConfig(String filename) throws IOException
    {
        Preconditions.checkNotNull(filename, "Error, filename can not be null!");
        Properties properties = new Properties();
        String path = "/config/";
        String fullFilename = path + filename + ".properties";
        properties = readProperties(fullFilename);
        // properties.load(Class.class.getResourceAsStream(fullFilename));
        return properties;
    }

    /**
     * 读取properties文件
     *
     * @param filepath 完整文件名。不以’/'开头时默认是从此类所在的包下取资源，
     *                 以’/'开头则是从ClassPath根下获取 / full filename
     * @return Properties 实例
     * @throws IOException
     */
    public static Properties readProperties(String filepath) throws IOException
    {
        Preconditions.checkNotNull(filepath, "Error, filename can not be null!");
        Properties properties = new Properties();
        properties.load(Class.class.getResourceAsStream(filepath));
        return properties;
    }
}
