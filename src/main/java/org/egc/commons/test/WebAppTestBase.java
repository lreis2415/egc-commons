package org.egc.commons.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * <pre>WebApp测试基类
 * <em> @WebAppConfiguration 默认(value = "src/main/webapp")</em>
 * <em> @ContextConfiguration("classpath*:/config/spring.xml")</em>
 * 可使用MockMVC
 * @author houzhiwei
 * @date 2016/12/8 22:08
 */
@ContextConfiguration("classpath*:/config/spring.xml")
@WebAppConfiguration
@Rollback
public abstract class WebAppTestBase extends AbstractTransactionalJUnit4SpringContextTests
{
    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Before
    public void setup()
    {
        MockMvcBuilders.webAppContextSetup(wac).build();
    }
}
