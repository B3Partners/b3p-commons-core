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
