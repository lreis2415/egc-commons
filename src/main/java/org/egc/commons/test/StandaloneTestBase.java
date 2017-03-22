package org.egc.commons.test;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * TODO: 优化测试
 * <pre>独立单元测试
 * Mockito + JUnit
 *
 * @author houzhiwei
 * @date 2017/2/28 21:13
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class StandaloneTestBase
{
    //不是测试Controller时不需要
    protected MockMvc mockMvc;
    //首先需要  this.mockMvc = MockMvcBuilders.standaloneSetup(xxController).build();


    //@Mock
    //测试相关的Service

    //@InjectMocks
    //测试的controller
    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(StandaloneTestBase.class);
        System.out.println("-------------test start-----------");
    }


    @After
    public void teardown()
    {
        System.out.println("--------------test end------------");
    }
}
