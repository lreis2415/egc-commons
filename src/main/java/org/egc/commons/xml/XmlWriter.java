package org.egc.commons.xml;

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
 *
 * @author houzhiwei
 * @date 2017/1/4 11:46
 */
public class XmlWriter
{
    /**
     * Write document to xml file.
     *
     * @param doc      the document
     * @param filename the xml filename
     * @return true(success) or false(failure)
     */
    public boolean doc2XmlFile(Document doc, String filename)
    {
        boolean flag = true;
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(CharEncoding.UTF_8);
        try {
            XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(filename)), format);
            writer.write(doc);
            writer.close();
        } catch (IOException ex) {
            flag = false;
            ex.printStackTrace();
            throw new BusinessException(ex, "Write XML file [ " + filename + " ] failed!");
        }
        System.out.println(flag);
        return flag;
    }

   /* public static void append2XML(String xmlFile)
    {
        // 1.创建一个SAXReader对象reader
        SAXReader reader = new SAXReader();
        try {
            // 2.通过reader对象的read方法加载xml文件，获取Document对象
            Document doc = reader.read(new File(xmlFile));
            Element root = doc.getRootElement();

            Element book = root.element("book");
            Element language = book.addElement("language");
            language.setText("简体中文");

            // 3.设置输出格式和输出流
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(CharEncoding.UTF_8);
            XMLWriter writer = new XMLWriter(new FileOutputStream(xmlFile), format);
            writer.write(doc);// 将文档写入到输出流
            writer.flush();
            writer.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }*/
}
