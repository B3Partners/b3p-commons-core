/*
 * $Id: Betalingskenmerk.java 1407 2005-09-05 17:14:21Z Matthijs $
 */

package nl.b3p.commons.clieop3.record;

import nl.b3p.commons.clieop3.ClieOp3OutputStream;
import nl.b3p.commons.clieop3.PadUtils;

public class Betalingskenmerk extends Record {
    
    private static final String RECORDCODE = "0150";
    private static final char VARIANTCODE = 'A';
    
    private String kenmerk;
       
    public Betalingskenmerk(String kenmerk) {
        super(RECORDCODE, VARIANTCODE);
        
        this.kenmerk = ClieOp3OutputStream.cleanClieOp3String(kenmerk);
    }

    protected void appendRecordContents(StringBuffer buf) {
        PadUtils.padText(kenmerk, 16, buf);
    }
}
