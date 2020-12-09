package org.egc.commons.xml;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;

/**
 * @author houzhiwei
 * @date 2020/10/10 11:15
 */
@Slf4j
public class XmlTransformer {
    /**
     * 使用 XSLT 转换 XML文件.
     * 例如将标准的 iso-19139元数据 xml文件转换为 GeoDCAT-AP 规范的 XML
     * （https://github.com/SEMICeu/iso-19139-to-dcat-ap/blob/master/documentation/HowTo.md）
     *
     * @param srcXml   源 XML文件路径
     * @param dstXml   目标 XML文件路径
     * @param xsltFile XSLT 文件路径
     */
    public static void transformByXslt(String srcXml, String dstXml, String xsltFile) {
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Templates cachedXslt = tf.newTemplates(new StreamSource(xsltFile));
            // 获取转换器对象实例
            Transformer transformer = cachedXslt.newTransformer();
            // 进行转换
            transformer.transform(new StreamSource(srcXml),
                    new StreamResult(new FileOutputStream(dstXml)));
        } catch (FileNotFoundException | TransformerException e) {
            log.error("Cannot transform the xml using the given xslt", e);
        }
    }
}
