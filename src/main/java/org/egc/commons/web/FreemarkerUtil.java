package org.egc.commons.web;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.*;

/**
 * @author houzhiwei
 * @date 2019 /9/10 13:58
 */
@Slf4j
public class FreemarkerUtil {
    /**
     * Gets freemarker template.
     *
     * @param ftl the template file (ftl) name (in folder classpath*:/templates/)
     * @return the freemarker template object
     * @throws IOException the io exception
     */
    public static Template getTemplate(String ftl) throws IOException {
        Configuration config = new Configuration(Configuration.VERSION_2_3_23);
        config.setDateFormat("yyyy-MM-dd");
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        // classpath 下的模板路径
        config.setClassLoaderForTemplateLoading(ClassLoader.getSystemClassLoader(), "templates");
        return config.getTemplate(ftl);
    }

    /**
     * Process template to file
     *
     * @param model    the model
     * @param ftl      the template file (ftl) name
     * @param filename the name (with path) of the generated file
     * @return the boolean
     */
    public static boolean process2File(Object model, String ftl, String filename)
    {
        File dir = new File(FilenameUtils.getFullPath(filename));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            Template baseTemplate = getTemplate(ftl);
            Writer out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(FilenameUtils.normalize(filename)), "UTF-8"));
            baseTemplate.process(model, out);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }
}
