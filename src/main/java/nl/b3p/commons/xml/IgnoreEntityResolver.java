package nl.b3p.commons.xml;

import java.io.StringReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class IgnoreEntityResolver implements EntityResolver {
    private static final Log log = LogFactory.getLog(IgnoreEntityResolver.class);

    /** This is the fake DTD
    which has no physical file and exists
    only in this form and XML file can
    refer to it as:
    <!DOCTYPE mydoc SYSTEM "internal.dtd">
     */
    public static final String FAKE_DTD = "<!ELEMENT mydoc (empty)>";

    public InputSource resolveEntity(String publicId, String systemId) {
        if (systemId != null) {
            // return a input source to fake dtd
            log.debug("dtd set to fake dtd, systemId: " + systemId);
            InputSource dtdSource = new InputSource(new StringReader(FAKE_DTD));
            dtdSource.setSystemId(systemId);
            dtdSource.setEncoding("UTF-8");
            return dtdSource;
        } else {
            // use the default behaviour
            return null;
        }
    }
}

  
    

 

