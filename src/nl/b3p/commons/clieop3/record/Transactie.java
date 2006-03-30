/*
 * $Id: Transactie.java 2202 2005-12-14 16:14:18Z Matthijs $
 */

package nl.b3p.commons.clieop3.record;

import java.math.BigInteger;

import nl.b3p.commons.clieop3.PadUtils;
import nl.b3p.commons.clieop3.RekeningnummerChecker;

public class Transactie extends Record {
    
    private static final String RECORDCODE = "0100";
    private static final char VARIANTCODE = 'A';

    public static final String TRANSACTIE_ONZUIVERE_CREDITEURENBETALING = "0000";
    public static final String TRANSACTIE_ONZUIVERE_SALARISBETALING     = "0003";
    public static final String TRANSACTIE_ZUIVERE_CREDITEURENBETALING   = "0005";
    public static final String TRANSACTIE_ZUIVERE_SALARISBETALING       = "0008";
    public static final String TRANSACTIE_ZUIVERE_INCASSO               = "1001";
    public static final String TRANSACTIE_ONZUIVERE_INCASSO             = "1002";
    
    /* constanten voor getTransactieSoort() */
    public static final int TRANSACTIE_BETALING = 0;
    public static final int TRANSATIIE_SALARIS  = 1;
    public static final int TRANSACTIE_INCASSO  = 2;
    
    private String transactieSoort;
    private BigInteger bedragCenten;
    private BigInteger rekeningnummerBetaler;
    private BigInteger rekeningnummerBegunstigde;
    
    public Transactie(
        String transactieSoort,
        BigInteger bedragCenten,
        BigInteger rekeningnummerBetaler,
        BigInteger rekeningnummerBegunstigde) {
        
        super(RECORDCODE, VARIANTCODE);
        
        this.transactieSoort = transactieSoort;
        this.bedragCenten = bedragCenten;
        this.rekeningnummerBetaler = rekeningnummerBetaler;
        this.rekeningnummerBegunstigde = rekeningnummerBegunstigde;
    }
    
    public String getTransactieSoort() {
        return transactieSoort;
    }
    
    public BigInteger getBedragCenten() {
        return bedragCenten;
    }
    
    public BigInteger getRekeningnummerBetaler() {
        return rekeningnummerBetaler;
    }
    
    public BigInteger getRekeningnummberBegunstigde() {
        return rekeningnummerBegunstigde;
    }
    
    protected void appendRecordContents(StringBuffer buf) {
        buf.append(transactieSoort);
        PadUtils.padNumber(bedragCenten.toString(), 12, buf);
        PadUtils.padNumber(rekeningnummerBetaler.toString(), 10, buf);
        PadUtils.padNumber(rekeningnummerBegunstigde.toString(), 10, buf);
    }        
    
    /**
     * Controleert of het rekeningnummer een girorekening of een bankrekening
     * is en geeft aan de hand daarvan een transactiesoort met zuiver of 
     * onzuiver terug voor gebruik bij de constructor.
     *
     * @throws IllegalArgumentException indien het rekeningnummer geen giro- of 
     *   bankrekeningnummer is of indien soort ongeldig is.
     */
    public static String getTransactieSoort(BigInteger rekeningnummer, int soort) {
        if(RekeningnummerChecker.voldoetAanElfProef(rekeningnummer)) {
            switch(soort) {
                case TRANSACTIE_BETALING: return TRANSACTIE_ZUIVERE_CREDITEURENBETALING;
                case TRANSATIIE_SALARIS: return TRANSACTIE_ZUIVERE_SALARISBETALING;
                case TRANSACTIE_INCASSO: return TRANSACTIE_ZUIVERE_INCASSO;
                default: throw new IllegalArgumentException("ongeldige soort: " + soort);
            }
        } else if(RekeningnummerChecker.isMogelijkGiroNummer(rekeningnummer)) {
            switch(soort) {
                case TRANSACTIE_BETALING: return TRANSACTIE_ONZUIVERE_CREDITEURENBETALING;
                case TRANSATIIE_SALARIS: return TRANSACTIE_ONZUIVERE_SALARISBETALING;
                case TRANSACTIE_INCASSO: return TRANSACTIE_ONZUIVERE_INCASSO;
                default: throw new IllegalArgumentException("ongeldige soort: " + soort);
            }
        } else {
            throw new IllegalArgumentException("ongeldig rekeningnummer: " + rekeningnummer);
        }
    }
}
