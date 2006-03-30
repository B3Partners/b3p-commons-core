/*
 * $Id: BestandEnd.java 1382 2005-09-02 15:53:01Z Matthijs $
 */

package nl.b3p.commons.clieop3.record;

public class BestandEnd extends Record {

    private static final String RECORDCODE = "9999";
    private static final char VARIANTCODE = 'A';

    public BestandEnd() {
        super(RECORDCODE, VARIANTCODE);
    }

    protected void appendRecordContents(StringBuffer buf) {
    }
}
