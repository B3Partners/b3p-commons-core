/*
 * $Id: BatchStart.java 1401 2005-09-05 16:11:38Z Matthijs $
 */

package nl.b3p.commons.clieop3.record;

import java.math.BigInteger;

import nl.b3p.commons.clieop3.PadUtils;

public class BatchStart extends Record {

    private static final String RECORDCODE = "0010";
    private static final char VARIANTCODE = 'B';

    public static final String TRANSACTIEGROEP_BETALINGEN = "00";
    public static final String TRANSACTIEGROEP_INCASSOS   = "10";
    
    private String transactieGroep;
    private BigInteger rekeningOpdrachtgever;
    private String valuta;
    
    private int volgnummer = 1;
    
    public BatchStart(
        String transactieGroep,
        BigInteger rekeningOpdrachtgever,
        String valuta) {
        
        super(RECORDCODE, VARIANTCODE);
        
        this.transactieGroep = transactieGroep;
        this.rekeningOpdrachtgever = rekeningOpdrachtgever;
        this.valuta = valuta;
    }
    
    public String getTransactieGroep() {
        return transactieGroep;
    }
    
    public BigInteger getRekeningOpdrachtgever() {
        return rekeningOpdrachtgever;
    }
    
    public String getValuta() {
        return valuta;
    }
    
    public void setVolgnummer(int volgnummer) {
        this.volgnummer = volgnummer;
    }
    
    protected void appendRecordContents(StringBuffer buf) {
        PadUtils.padText(transactieGroep, 2, buf);
        PadUtils.padNumber(rekeningOpdrachtgever.toString(), 10, buf);
        PadUtils.padNumber(volgnummer + "", 4, buf);
        PadUtils.padText(valuta, 3, buf);
    }
    
    public static void main(String[] args) {
        BatchStart entry = new BatchStart(TRANSACTIEGROEP_BETALINGEN, new BigInteger("0679411372"), "EUR");
        entry.setVolgnummer(1);
        System.out.println(entry.getRecordData());
    }
}
