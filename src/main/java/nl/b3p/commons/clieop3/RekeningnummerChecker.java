/*
 * B3P Commons Core is a library with commonly used classes for webapps.
 * Included are clieop3, oai, security, struts, taglibs and other
 * general helper classes and extensions.
 *
 * Copyright 2000 - 2008 B3Partners BV
 * 
 * This file is part of B3P Commons Core.
 * 
 * B3P Commons Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Commons Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Commons Core.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        if (rekeningnummer.signum() != 1) {
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
        if (rekeningnummer.signum() != 1) {
            return false;
        }
        int len = rekeningnummer.toString().length();
        return len == 9 || len == 10;
    }

    /**
     * Test of een bankrekeningnummer voldoet aan de elfproef.
     */
    public static boolean voldoetAanElfProef(BigInteger bankrekeningNummer) {

        if (!isMogelijkBankrekeningNummer(bankrekeningNummer)) {
            return false;
        }

        String rekNr = bankrekeningNummer.toString();
        int len = rekNr.length();

        int m = len;
        int v = 0;

        for (int i = 0; i < len; i++) {
            char c = rekNr.charAt(i);
            int cijfer = c - '0';
            v += cijfer * m--;
        }
        return v % 11 == 0;
    }
}
