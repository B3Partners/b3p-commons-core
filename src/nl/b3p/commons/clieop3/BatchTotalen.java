/*
 * $Id: BatchTotalen.java 1401 2005-09-05 16:11:38Z Matthijs $
 */

package nl.b3p.commons.clieop3;

import java.math.BigInteger;

/**
 * Gegevens nodig voor opdrachtbrief per batch.
 */
public class BatchTotalen {
    
    private BigInteger bedragCenten;
    private BigInteger rekeningnummersHash;
    private int aantalPosten;
    private int volgnummer;
    
    public BatchTotalen(
        BigInteger bedragCenten, 
        BigInteger rekeningnummersHash, 
        int aantalPosten, 
        int volgnummer) {
        
        this.bedragCenten = bedragCenten;
        this.rekeningnummersHash = rekeningnummersHash;
        this.aantalPosten = aantalPosten;
        this.volgnummer = volgnummer;
    }
    
    public BigInteger getBedragCenten() {
        return bedragCenten;
    }
    
    public BigInteger getrekeningnummersHash() {
        return rekeningnummersHash;
    }
    
    public int getAantalPosten() {
        return aantalPosten;
    }
    
    public int getVolgnummer() {
        return volgnummer;
    }    
}
