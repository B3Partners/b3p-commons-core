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
 * $Id: BatchEnd.java 1401 2005-09-05 16:11:38Z Matthijs $
 */
package nl.b3p.commons.clieop3.record;

import java.math.BigInteger;

import nl.b3p.commons.clieop3.PadUtils;

public class BatchEnd extends Record {

    private static final String RECORDCODE = "9990";
    private static final char VARIANTCODE = 'A';
    private BigInteger totaalBedragCenten;
    private BigInteger totaalRekeningnummers;
    private int aantalPosten;

    public BatchEnd(
            BigInteger totaalBedragCenten,
            BigInteger totaalRekeningnummers,
            int aantalPosten) {

        super(RECORDCODE, VARIANTCODE);

        this.totaalBedragCenten = totaalBedragCenten;
        this.totaalRekeningnummers = totaalRekeningnummers;
        this.aantalPosten = aantalPosten;
    }

    protected void appendRecordContents(StringBuffer buf) {
        PadUtils.padNumber(totaalBedragCenten.toString(), 18, buf);

        /* Indien het totaal van alle rekeningnummers opgeteld meer dan 10 karakters
         * lang is dan het begin afkappen tot 10 karakters
         *
         * TODO test!
         */
        String totRekNrs = totaalRekeningnummers.toString();
        final int TOTREK_LENGTH = 10;
        if (totRekNrs.length() > TOTREK_LENGTH) {
            totRekNrs = totRekNrs.substring(totRekNrs.length() - TOTREK_LENGTH);
        }
        PadUtils.padNumber(totRekNrs, TOTREK_LENGTH, buf);

        PadUtils.padNumber(aantalPosten + "", 7, buf);
    }
}
