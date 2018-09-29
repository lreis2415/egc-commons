package org.egc.commons.test;

import org.egc.commons.util.StringUtil;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/7/20 17:49
 */
public class StringsTest {
    @Test
    public void uriTest() {
        String u = "htpaontbase/uni/UniProps#你好---";
        System.out.println(StringUtil.isUriValid(u));
    }

    @Test
    public void testAuth() {
        String header = "Bearer jwt";
        System.out.println(header.substring(header.indexOf(" ") + 1));
    }

    @Test
    public void strTest(){
        String path="F:\\GIScience\\GDAL-2.1.3.win-amd64-py3.4.msi";
        System.out.println(Paths.get(""));
    }

}
