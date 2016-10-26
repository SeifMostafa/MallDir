package com.example.seifmostafa.malldir.file_model;

import org.w3c.dom.Document;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlData  {
    
    private static String filePath = "D:/MallEditor/Resources/Map.xml";

    private static Document XML_DOC;
    
    public static void IntiData() {

    	Document doc = ReadXmlFile.readFile(filePath);
    	XmlData.XML_DOC = doc;

    }
    
    public static Document getXML_DOC() {
        return XML_DOC;
    }
    
    public static void setXML_DOC(Document XML_DOC) {
        XmlData.XML_DOC = XML_DOC;
        WriteXmlFile.writeFile(filePath, XML_DOC);
    }

	// write the content into xml file
    public static void writeXMLcontect() throws TransformerException
    {
    	TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(XML_DOC);
		StreamResult result = new StreamResult(new File(filePath));
		transformer.transform(source, result);
 
		//System.out.println("Done");
    }
    
}
