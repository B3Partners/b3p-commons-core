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
 * $Id: BatchStart.java 1401 2005-09-05 16:11:38Z Matthijs $
 */
package nl.b3p.commons.clieop3.record;

import java.math.BigInteger;

import nl.b3p.commons.clieop3.PadUtils;

public class BatchStart extends Record {

    private static final String RECORDCODE = "0010";
    private static final char VARIANTCODE = 'B';
    public static final String TRANSACTIEGROEP_BETALINGEN = "00";
    public static final String TRANSACTIEGROEP_INCASSOS = "10";
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
