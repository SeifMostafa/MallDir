package com.example.seifmostafa.malldir.file_model;

import org.w3c.dom.Document;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReadXmlFile {

    public static Document readFile(String filePath) {
        
    	Document doc = null;
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(file);
            doc.getDocumentElement().normalize();

        } catch (Exception ex) {
            Logger.getLogger(ReadXmlFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    }
}
