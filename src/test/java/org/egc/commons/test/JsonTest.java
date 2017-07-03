package org.egc.commons.test;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.egc.commons.web.JsonResult;
import org.junit.Test;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2016/12/8 16:49
 */
public class JsonTest
{
    @Test
    public void printJsonResult(){
//        JsonResult jsonResult = new JsonResult("test", HttpStatus.OK);
        JsonResult jsonResult = new JsonResult("test");
        System.out.println(JSON.toJSON(jsonResult));
    }


    @Test
    public void strTest(){
        String indentSpace = Strings.repeat("   ", 1);
        System.out.println("a"+indentSpace+"b");
    }
}
