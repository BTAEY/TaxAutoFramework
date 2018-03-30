package com.ey.tax.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by zhuji on 2/7/2018.
 */
public final class JAXBUtil {
    private static final Logger logger = LogManager.getLogger();

    public static String marshal(Object obj) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj,writer);
            return writer.toString();
        } catch (JAXBException e) {
            logger.error("marshal object to xml occurs error.",e);
        }
        return "";
    }

    public static <T> T unmarshal(String content,Class<T> clazz){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(content);
            SAXParserFactory sax = SAXParserFactory.newInstance();
            sax.setNamespaceAware(false);
            XMLReader xmlReader = sax.newSAXParser().getXMLReader();
            Source source = new SAXSource(xmlReader, new InputSource(reader));
            return (T)unmarshaller.unmarshal(source);
        } catch (JAXBException | SAXException | ParserConfigurationException e) {
            logger.error("unmarshal xml to object occurs error.",e);
        }
        return null;
    }
}
