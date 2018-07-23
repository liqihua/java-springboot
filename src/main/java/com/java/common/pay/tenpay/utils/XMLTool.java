package com.java.common.pay.tenpay.utils;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;

public class XMLTool {
	
	
	/**
	 * 方法名：objToXML
	 * 详述：把实体转成xml格式字符串
	 * @param obj
	 * @return String
	 */
	public static String objToXML(Object obj){
		XStream xstream = new XStream();
		xstream.alias("xml", obj.getClass());
		return xstream.toXML(obj).replace("__", "_");
	}
	
	
	/**
	 * 方法名：getDocumentBuilder
	 * 详述：创建DocumentBuilder对象
	 * @return DocumentBuilder
	 */
	public static DocumentBuilder getDocumentBuilder() {
		// 获得dom解析器工厂（工作的作用是用于创建具体的解析器）
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// 获得具体的dom解析器
		try {
			return dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 方法名：parseXMLDocument
	 * 详述：把String转成Document对象
	 * @param xmlString
	 * @return Document
	 */
	public static Document parseXMLDocument(String xmlString) {  
        if (xmlString == null) {  
            throw new IllegalArgumentException();  
        }  
        try {  
            return newDocumentBuilder().parse(new InputSource(new StringReader(xmlString)));  
        } catch (Exception e) {  
            throw new RuntimeException(e.getMessage());  
        }  
    }
	
	/**
	 * 方法名：newDocumentBuilder
	 * 详述：创建一个DocumentBuilder实例
	 * @return
	 * @throws ParserConfigurationException DocumentBuilder
	 */
	public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {  
		return newDocumentBuilderFactory().newDocumentBuilder();  
	} 

	/**
	 * 方法名：newDocumentBuilderFactory
	 * 详述：创建一个DocumentBuilderFactory实例
	 * @return DocumentBuilderFactory
	 */
	public static DocumentBuilderFactory newDocumentBuilderFactory() {  
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        dbf.setNamespaceAware(true);  
        return dbf;  
    } 
}
