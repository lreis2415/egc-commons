package org.egc.commons.test;

import org.egc.commons.util.PathUtil;
import org.egc.commons.util.PropertiesUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * TODO
 *
 * @author houzhiwei
 * @Date 2016/11/23 15:58
 */
public class PropertiesTests
{
    String filename = "test";

    @Test
    public void getTest() throws Exception
    {
        String p = "";
        p = PropertiesUtil.getPropertyFromConfig("test.a", filename);
        System.out.println(PropertiesUtil.readPropertiesFromConfig(filename).getProperty("test.a"));
        System.out.println(p);
    }

    @Test
    public void readTest() throws Exception
    {
        //Properties p = PropertiesUtil.readProperties("test.properties");
        Properties p2 = PropertiesUtil.readProperties("/test.properties");
        System.out.println(p2.getProperty("test.b"));
    }

    @Test
    public void pathTest() throws IOException
    {
        System.out.println(PathUtil.getProjectRoot());
    }
}
