/*
 * $Id: RekeningnummerChecker.java 1417 2005-09-06 13:23:24Z Matthijs $
 */

package nl.b3p.commons.clieop3;

import java.math.BigInteger;

public class RekeningnummerChecker {
    
    private RekeningnummerChecker() {
    }
    
    /**
     * Test of het rekeningnummer een mogelijk gironummer is. Een gironummer 
     * bestaat uit maximaal 7 cijfers zonder voorloopnul. Indien deze methode
     * true geeft is het geen mogelijk bankrekeningnummer.
     */
    public static boolean isMogelijkGiroNummer(BigInteger rekeningnummer) {
        if(rekeningnummer.signum() != 1) {
            return false;
        }
        int len = rekeningnummer.toString().length();
        return (len > 0) && (len <= 7);
    }
    
    /**
     * Test of het rekeningnummer een mogelijk bankrekeningnummer is. Een 
     * bankrekeningnummer bestaat uit 9 of 10 cijfers zonder voorloopnul. Indien
     * deze methode true geeft is het geen mogelijk gironummer.
     *
     * TODO check er zijn dus nooit bankrekeningnummers met 8 cijfers (2 voorlopnullen)?
     */
    public static boolean isMogelijkBankrekeningNummer(BigInteger rekeningnummer) {
        if(rekeningnummer.signum() != 1) {
            return false;
        }
        int len = rekeningnummer.toString().length();
        return len == 9 || len == 10;
    }
    
    /**
     * Test of een bankrekeningnummer voldoet aan de elfproef.
     */
    public static boolean voldoetAanElfProef(BigInteger bankrekeningNummer) {
        
        if(!isMogelijkBankrekeningNummer(bankrekeningNummer)) {
            return false;
        }
        
        String rekNr = bankrekeningNummer.toString();
        int len = rekNr.length();
        
        int m = len;
        int v = 0;
        
        for(int i=0; i<len; i++) {
            char c = rekNr.charAt(i);
            int cijfer = c - '0';
            v += cijfer * m--;
        }
        return v % 11 == 0;
    }
}
