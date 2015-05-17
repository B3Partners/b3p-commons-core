/*
 * B3P Commons Core is a library with commonly used classes for webapps.
 * Included are clieop3, oai, security, struts, taglibs and other
 * general helper classes and extensions.
 *
 * Copyright 2000 - 2008 B3Partners BV
 * 
 * This file is part of B3P Commons Core.
 * 
 * B3P Commons Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Commons Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Commons Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.commons.oai.util.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class contains various tools to deal with XML documents
 */
public class XMLTool {

    /**
     * Creates a new Document
     */
    public static Document createDocumentRoot() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            return doc;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Coverts a DOM to an output stream
     */
    public static void Dom2Stream(org.w3c.dom.Document doc,
            java.io.Writer writer) {
        if (doc == null) {
            System.out.println("doc is null");
        } else {
            System.out.println(doc);
        }
        if (writer == null) {
            System.out.println("writer is null");
        }
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();

            // Use the TransformerFactory to instantiate a Transformer that will
            // work with
            // the stylesheet you specify. This method call also processes the
            // stylesheet
            // into a compiled Templates object.
            Transformer transformer = tFactory.newTransformer();

            // Use the Transformer to apply the associated Templates object to
            // an XML document
            // (foo.xml) and write the output to a file (foo.out).

            transformer.transform(new DOMSource(doc), new StreamResult(writer));
        } catch (Exception e) {
            try {
                writer.write(output(doc));
                e.printStackTrace();
            } catch (Exception ex) {
                System.out.println(e);
            }
        }

    }

    /**
     * Print the document out.
     */
    public static String output(Document doc) {
        String out;
        out = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        out += showElement(doc.getDocumentElement());
        return out;
    }

    /**
     * Display one element
     */
    public static String showElement(Element e) {
        String out;
        out = "<" + e.getNodeName() + " ";
        NamedNodeMap map = e.getAttributes();
        for (int i = 0; i < map.getLength(); i++) {
            out += map.item(i).getNodeName() + "=\"" + map.item(i).getNodeValue() + "\" ";
        }
        out += ">\n";

        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                out += showElement((Element) nl.item(i));
            } else if (nl.item(i).getNodeType() == Node.TEXT_NODE) {
                // System.out.println(nl.item(i).getNodeValue());
                out += nl.item(i).getNodeValue();
            } else {
                System.out.println("other node");
            }
        }
        out = out + "</" + e.getNodeName() + ">";
        return out;
    }
}
