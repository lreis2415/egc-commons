package org.egc.commons.xml;


import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.egc.commons.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

/**
 * read and parse xml
 *
 * @author houzhiwei
 * @date 2017/1/4 11:46
 */
public class XmlParser {
    private static final Logger logger = LoggerFactory.getLogger(XmlParser.class);

    public static boolean validate(String xmlPath, String xsdPath) {
        boolean result = true;
        try {
            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(language);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlPath));
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * map java object to xml file
     * @param clazz  java 映射类
     * @param object java 对象
     * @param path   生成的XML存放位置
     * @throws JAXBException
     */
    public static void java2xml(Class<?> clazz, Object object, String path) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        //PrintStream out = System.out; //file改为out则输出到控制台
        marshaller.marshal(object, new File(path));
    }

    /**
     * parse Xml 2 java object.
     *
     * @param clazz the java class
     * @param path  the path
     * @return the object
     * @throws Exception the exception
     */
    public static Object xml2java(Class<?> clazz, String path) throws Exception {
        if (StringUtils.isBlank(path) || !new File(path).exists()) {
            throw new FileNotFoundException();
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        // 需对object 强制转换为相应的类
        Object object = unmarshaller.unmarshal(new File(path));
        return object;
    }

    /**
     * Read a xml document.
     *
     * @param filename the xml filename
     * @return the document
     */
    public static Document readXmlFile(String filename) {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            // 读取XML文件,获得document对象
            document = saxReader.read(new File(filename));
        } catch (DocumentException ex) {
            logger.error("Read XML file [ " + filename + " ] failed!", ex);
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
    public static Document readXmlFile(URL url) {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            // 读取XML文件,获得document对象
            document = saxReader.read(url);
        } catch (DocumentException ex) {
            logger.error("Read XML file from [ " + url + " ] failed!", ex);
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
    public static Document string2Document(String xmlStr) {
        try {
            return DocumentHelper.parseText(xmlStr);
        } catch (DocumentException de) {
            throw new BusinessException(de, "The string could not be parsed to a xml document");
        }
    }

    public static String xmlFile2String(String xmlFile) {
        Document document = readXmlFile(xmlFile);
        Element root = document.getRootElement();
        return document.asXML();
    }

    /**
     * convert document to map
     *
     * @param doc Document
     * @return HashMap
     */
    public static Map<String, Object> dom2Map(Document doc) {
        Map<String, Object> map = new HashMap<>();
        if (doc == null) {
            return map;
        }
        Element root = doc.getRootElement();
        for (Iterator<Attribute> iterator = root.attributeIterator(); iterator.hasNext(); ) {
            Attribute attr = (Attribute) iterator.next();
            map.put(attr.getName(), attr.getValue());
        }
        for (Iterator<Element> iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element e = (Element) iterator.next();
            List<Element> list = e.elements();
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
    public static Map<String, Object> el2Map(Element e) {
        Map<String, Object> map = new HashMap<>();
        List<Element> list = e.elements();
        if (list.size() > 0) {
            for (Element element : list) {
                attributes2Map(((Element) element).attributes(), map);
                List mapList = new ArrayList();
                if (((Element) element).elements().size() > 0) {
                    Map<String, Object> m = el2Map((Element) element);
                    if (map.get(((Element) element).getName()) != null) {
                        Object obj = map.get(((Element) element).getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList<>();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(((Element) element).getName(), mapList);
                    } else {
                        map.put(((Element) element).getName(), m);
                    }
                } else {
                    if (map.get(((Element) element).getName()) != null) {
                        Object obj = map.get(((Element) element).getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(((Element) element).getText());
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(((Element) element).getText());
                        }
                        map.put(((Element) element).getName(), mapList);
                    } else {
                        map.put(((Element) element).getName(), ((Element) element).getText());
                    }
                }
            }
        } else {
            map.put(e.getName(), e.getTextTrim());
        }
        return map;
    }

    public static Map<String, Object> attributes2Map(List<Attribute> attrs, Map<String, Object> map) {
        for (Attribute attr : attrs) {
            map.put(attr.getName(), attr.getValue());
        }
        return map;
    }

}
