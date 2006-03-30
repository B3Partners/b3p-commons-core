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
        if(totRekNrs.length() > TOTREK_LENGTH) {
            totRekNrs = totRekNrs.substring(totRekNrs.length() - TOTREK_LENGTH);
        }
        PadUtils.padNumber(totRekNrs, TOTREK_LENGTH, buf);

        PadUtils.padNumber(aantalPosten + "", 7, buf);
    }   
}
