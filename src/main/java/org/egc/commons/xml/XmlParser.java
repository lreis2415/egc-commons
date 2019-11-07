package org.egc.commons.xml;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.egc.commons.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.*;

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

    /**
     * convert document to map
     *
     * @param doc Document
     * @return HashMap
     */
    public static Map dom2Map(Document doc) {
        Map map = new HashMap();
        if (doc == null) {
            return map;
        }
        Element root = doc.getRootElement();
        for (Iterator iterator = root.attributeIterator(); iterator.hasNext(); ) {
            Attribute attr = (Attribute) iterator.next();
            map.put(attr.getName(), attr.getValue());
        }
        for (Iterator iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element e = (Element) iterator.next();
            List list = e.elements();
            if (list.size() > 0) {
                map.put(e.getName(), el2Map(e));
            } else {
                map.put(e.getName(), e.getText());
            }
        }
        return map;
    }
    /**
     * convert element to map
     *
     * @param e Element
     * @return HashMap
     */
    public static Map el2Map(Element e) {
        Map map = new HashMap();
        List list = e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                map = attributes2Map(iter.attributes(), map);
                List mapList = new ArrayList();
                if (iter.elements().size() > 0) {
                    Map m = el2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        } else {
            map.put(e.getName(), e.getText());
        }
        return map;
    }

    public static Map attributes2Map(List<Attribute> attrs, Map map) {
        for (Attribute attr : attrs) {
            map.put(attr.getName(), attr.getValue());
        }
        return map;
    }

}
