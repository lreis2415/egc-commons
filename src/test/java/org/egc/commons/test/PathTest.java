package org.egc.commons.test;

import org.egc.commons.util.PathUtil;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/9/4 16:06
 */
public class PathTest {
    @Test
    public void pathTest(){
        System.out.println(PathUtil.resourcesPath());
        System.out.println(PathUtil.getPackagePath(this.getClass()));
        System.out.println(PathUtil.getClassPath(this.getClass()));
        System.out.println(PathUtil.getClassPath());
    }

    @Test
    public void pathTest2(){
        System.out.println(PathUtil.resourcesFilePath("log4j2.xml"));
        System.out.println(PathUtil.classFilePath("/test.properties"));
        System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
    }
}
