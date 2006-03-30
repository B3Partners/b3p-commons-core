/*
 * $Id: BestandStart.java 2202 2005-12-14 16:14:18Z Matthijs $
 */

package nl.b3p.commons.clieop3.record;

import java.util.Date;
import java.text.SimpleDateFormat;

import nl.b3p.commons.clieop3.ClieOp3OutputStream;
import nl.b3p.commons.clieop3.PadUtils;

public class BestandStart extends Record {
    
    private static final String RECORDCODE = "0001";
    private static final char VARIANTCODE = 'A';

    private static final String BESTANDSNAAM = "CLIEOP03";

    private Date aanmaakDatum;
    private String identificatie;
    private int volgnummer;
    private boolean isDuplicaat;
    
    public BestandStart(
        Date aanmaakDatum, 
        String identificatie, 
        int volgnummer,
        boolean isDuplicaat) {
        
        super(RECORDCODE, VARIANTCODE);
        
        this.aanmaakDatum = aanmaakDatum;
        this.identificatie = ClieOp3OutputStream.cleanClieOp3String(identificatie);
        this.volgnummer = volgnummer;
        this.isDuplicaat = isDuplicaat;
    }
    
    public Date getAanmaakDatum() {
        return aanmaakDatum;
    }
    
    public String getIdentificatie() {
        return identificatie;
    }
    
    public int getVolgnummer() {
        return volgnummer;
    }
    
    public boolean isDuplicaat() {
        return isDuplicaat;
    }
    
    protected void appendRecordContents(StringBuffer buf) {
        SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
        String date = df.format(aanmaakDatum);
        buf.append(date);
        
        buf.append(BESTANDSNAAM);
        PadUtils.padText(identificatie, 5, buf);
        
        /* Bestandsidentificatie: dag van maand met volgnummer */
        buf.append(date.substring(0, 2));        
        PadUtils.padNumber(volgnummer + "", 2, buf);

        buf.append(isDuplicaat ? '2' : '1');
    }
    
    public static void main(String[] args) {
        BestandStart entry = new BestandStart(new Date(), "TEST", 1, false);
        System.out.println(entry.getRecordData());
    }
}
