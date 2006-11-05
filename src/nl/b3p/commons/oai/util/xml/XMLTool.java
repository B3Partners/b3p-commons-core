package nl.b3p.commons.oai.util.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

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
		if (doc == null)
			System.out.println("doc is null");
		else
			System.out.println(doc);

		if (writer == null)
			System.out.println("writer is null");

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
		for (int i = 0; i < map.getLength(); i++)
			out += map.item(i).getNodeName() + "=\""
					+ map.item(i).getNodeValue() + "\" ";
		out += ">\n";

		NodeList nl = e.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE)
				out += showElement((Element) nl.item(i));
			else if (nl.item(i).getNodeType() == Node.TEXT_NODE) {
				// System.out.println(nl.item(i).getNodeValue());
				out += nl.item(i).getNodeValue();
			} else
				System.out.println("other node");

		}
		out = out + "</" + e.getNodeName() + ">";
		return out;
	}
}
