/*
 * $Id: Omschrijving.java 1407 2005-09-05 17:14:21Z Matthijs $
 */

package nl.b3p.commons.clieop3.record;

import nl.b3p.commons.clieop3.ClieOp3OutputStream;
import nl.b3p.commons.clieop3.PadUtils;

public class Omschrijving extends Record {
    
    private static final String RECORDCODE = "0160";
    private static final char VARIANTCODE = 'A';
    
    private String omschrijving;
    
    public Omschrijving(String omschrijving) {
        super(RECORDCODE, VARIANTCODE);

        this.omschrijving = ClieOp3OutputStream.cleanClieOp3String(omschrijving);
    }

    protected void appendRecordContents(StringBuffer buf) {
        PadUtils.padText(omschrijving, 32, buf);
    }    
}
