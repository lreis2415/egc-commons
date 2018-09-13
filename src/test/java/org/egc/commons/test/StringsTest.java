package org.egc.commons.test;

import org.egc.commons.util.StringUtil;
import org.junit.Test;

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

}
