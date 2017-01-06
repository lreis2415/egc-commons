package org.egc.commons.web;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.egc.commons.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.Map;

/**
 * Gets & Process freemarker template
 *
 * @author houzhiwei
 * @date 2017/1/3 16:49
 */
public abstract class BaseFreeMarkerConfig
{
    @Autowired
    //@Qualifier("freeMarkerConfigurer")
    protected FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * Gets ftl with spring-freeMarker Configuration xml.
     *
     * @param ftlName the ftl file name, under src/main/resources/config/ftl/
     * @return the ftl Template
     */
    protected Template getFtlTemplate(String ftlName)
    {
        try {
            return freeMarkerConfigurer.getConfiguration().getTemplate(ftlName);
        } catch (IOException e) {
            throw new BusinessException(e, "Read freemarker template " + ftlName + " failed!");
        }
    }

    /**
     * Process Template and get it's content as string.
     *
     * @param tpl  the template
     * @param data the data for rendering in java.util.Map type
     * @return the template as string
     */
    protected String processTpl(Template tpl, Map data)
    {
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(tpl, data);
        } catch (TemplateException te) {
            throw new BusinessException(te, "Rendering freemarker template failed!");
        } catch (IOException e) {
            throw new BusinessException(e, "Process freemarker template failed!");
        }
    }

}
