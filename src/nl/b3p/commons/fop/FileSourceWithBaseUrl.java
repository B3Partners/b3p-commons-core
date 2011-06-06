/**
 * $Id$
 */

package nl.b3p.commons.fop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

public class FileSourceWithBaseUrl {
    public Source source;

    public FileSourceWithBaseUrl(String filename) throws FileNotFoundException {
        File file = new File(filename);
        File path = new File(file.getParent());

        Source src = new StreamSource(new FileInputStream(file));

        /* Zorg ervoor dat in de XSL met relatieve URL's bestanden kunnen worden
         * geinclude
         */
        src.setSystemId(path.toURI().toString());

        this.source = src;
    }

    public String getBaseUrl() {
        return source.getSystemId();
    }
}
