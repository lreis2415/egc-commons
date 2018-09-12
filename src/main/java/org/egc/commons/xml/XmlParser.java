package org.egc.commons.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.egc.commons.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2017/1/4 11:46
 */
public class XmlParser {
    private static final Logger logger = LoggerFactory.getLogger(XmlParser.class);

    /**
     * Read a xml document.
     *
     * @param filename the xml filename
     * @return the document
     */
    public Document readXmlFile(String filename)
    {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            // 读取XML文件,获得document对象
            document = saxReader.read(new File(filename));
        } catch (DocumentException ex) {
            ex.printStackTrace();
            throw new BusinessException(ex, "Read XML file [ " + filename + " ] failed!");
        }
        return document;
    }

    /**
     * Read a xml document.
     *
     * @param url the xml url
     * @return the document
     */
    public Document readXmlFile(URL url)
    {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            // 读取XML文件,获得document对象
            document = saxReader.read(url);
        } catch (DocumentException ex) {
            ex.printStackTrace();
            throw new BusinessException(ex, "Read XML file from [ " + url + " ] failed!");
        }
        return document;
    }

    /**
     * Parse a string to xml document.
     *
     * @param xmlStr the xml string
     * @return the document
     */
    public Document string2Document(String xmlStr)
    {
        try {
            return DocumentHelper.parseText(xmlStr);
        } catch (DocumentException de) {
            throw new BusinessException(de, "The string could not be parsed to a xml document");
        }
    }

    public String xmlFile2String(String xmlFile)
    {
        SAXReader reader = new SAXReader();
        Document document = readXmlFile(xmlFile);
        Element root = document.getRootElement();
        String docXmlText = document.asXML();
        return docXmlText;
        /*String rootXmlText = root.asXML();
        Element memberElm = root.element("member");
        String memberXmlText = memberElm.asXML();*/
    }
}
