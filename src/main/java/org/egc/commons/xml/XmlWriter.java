package org.egc.commons.xml;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.CharEncoding;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.egc.commons.exception.BusinessException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * TODO
 * https://dom4j.github.io/
 *
 * @author houzhiwei
 * @date 2017/1/4 11:46
 */
@Slf4j
public class XmlWriter {
    /**
     * Write document to xml file.
     *
     * @param doc      the document
     * @param filename the xml filename
     * @return true(success) or false(failure)
     */
    public boolean doc2XmlFile(Document doc, String filename) {
        filename = FilenameUtils.normalize(filename);
        boolean flag = true;
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(CharEncoding.UTF_8);
        try {
            XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(filename)), format);
            writer.write(doc);
            writer.close();
        } catch (IOException ex) {
            flag = false;
            //log.error("Write XML file [ " + filename + " ] failed!");
            throw new BusinessException(ex, "Write XML file [ " + filename + " ] failed!");
        }
        return flag;
    }
}
