package com.example.seifmostafa.malldir.data;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

class WriteXmlFile {

	protected void writeFile(String filePath, Document xml) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            File file = new File(filePath);

            transformer.transform(new DOMSource(xml),
                    new StreamResult(new FileOutputStream(file)));

            DOMSource source = new DOMSource(xml);
            StreamResult console = new StreamResult(System.out);
            transformer.transform(source, console);

            System.out.println("\nXML DOM Created Successfully..");
        } catch (Exception ex) {
            Logger.getLogger(WriteXmlFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
