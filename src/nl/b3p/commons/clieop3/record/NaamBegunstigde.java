/*
 * $Id: NaamBegunstigde.java 2202 2005-12-14 16:14:18Z Matthijs $
 */

package nl.b3p.commons.clieop3.record;

import nl.b3p.commons.clieop3.ClieOp3OutputStream;
import nl.b3p.commons.clieop3.PadUtils;

public class NaamBegunstigde extends Record {
    
    private static final String RECORDCODE = "0170";
    private static final char VARIANTCODE = 'B';
    
    private String naam;
    
    public NaamBegunstigde(String naam) {
        super(RECORDCODE, VARIANTCODE);
        
        this.naam = ClieOp3OutputStream.cleanClieOp3String(naam);
    }

    protected void appendRecordContents(StringBuffer buf) {
        /* Postbank verwerkt alleen eerste 32 karakters */
        PadUtils.padText(naam, 35, buf);
    }
}
