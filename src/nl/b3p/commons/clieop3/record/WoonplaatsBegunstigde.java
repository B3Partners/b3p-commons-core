/*
 * $Id: WoonplaatsBegunstigde.java 1412 2005-09-06 11:04:40Z Matthijs $
 */

package nl.b3p.commons.clieop3.record;

import nl.b3p.commons.clieop3.ClieOp3OutputStream;
import nl.b3p.commons.clieop3.PadUtils;

public class WoonplaatsBegunstigde extends Record {
    
    private static final String RECORDCODE = "0173";
    private static final char VARIANTCODE = 'B';
    
    private String woonplaats;
    
    public WoonplaatsBegunstigde(String woonplaats) {
        super(RECORDCODE, VARIANTCODE);
        
        this.woonplaats = ClieOp3OutputStream.cleanClieOp3String(woonplaats);
    }

    protected void appendRecordContents(StringBuffer buf) {
        /* Postbank verwerkt alleen eerste 32 karakters */
        PadUtils.padText(woonplaats, 35, buf);
    }
}
