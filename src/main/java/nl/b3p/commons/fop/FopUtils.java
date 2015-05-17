/**
 * $Id$
 */

package nl.b3p.commons.fop;

import java.io.OutputStream;
import java.net.MalformedURLException;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;

public class FopUtils {
    public static TransformerHandler getFopTransformer(FileSourceWithBaseUrl source, String outputFormat, OutputStream out) throws TransformerConfigurationException, MalformedURLException, FOPException {
        SAXTransformerFactory transformerFactory = (SAXTransformerFactory)TransformerFactory.newInstance();

        TransformerHandler transformer = transformerFactory.newTransformerHandler(source.source);

        FopFactory fopFactory = FopFactory.newInstance();
        if(source.getBaseUrl() != null) {
            fopFactory.setBaseURL(source.getBaseUrl()); /* Zorg ervoor dat FOP ook bestanden kan vinden met relatief pad (bijvoorbeeld bij external-graphic) */
        }
        Fop fop = fopFactory.newFop(outputFormat, out);

        Result res = new SAXResult(fop.getDefaultHandler());
        transformer.setResult(res);

        return transformer;
    }
}
