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
 * $Id: Opdrachtgever.java 1412 2005-09-06 11:04:40Z Matthijs $
 */
package nl.b3p.commons.clieop3.record;

import java.util.Date;
import java.text.SimpleDateFormat;

import nl.b3p.commons.clieop3.ClieOp3OutputStream;
import nl.b3p.commons.clieop3.PadUtils;

public class Opdrachtgever extends Record {

    private static final String RECORDCODE = "0030";
    private static final char VARIANTCODE = 'B';
    public static final char NAW_GEWENST = '2';
    public static final char NAW_NIETGEWENST = '1';
    public static final char NAW_NVT = '1';
    public static final char TESTCODE_PRODUCTIE = 'P';
    public static final char TESTCODE_TEST = 'T';
    private char nawCode;
    private Date verwerkingsDatum;
    private String naam;
    private char testCode;

    public Opdrachtgever(
            char nawCode,
            Date verwerkingsDatum,
            String naam,
            char testCode) {

        super(RECORDCODE, VARIANTCODE);

        this.nawCode = nawCode;
        this.verwerkingsDatum = verwerkingsDatum;
        this.naam = ClieOp3OutputStream.cleanClieOp3String(naam);
        this.testCode = testCode;
    }

    public char getNawCode() {
        return nawCode;
    }

    public Date getVerwerkingsDatum() {
        return verwerkingsDatum;
    }

    public String getNaam() {
        return naam;
    }

    public char getTestCode() {
        return testCode;
    }

    protected void appendRecordContents(StringBuffer buf) {
        buf.append(nawCode);

        if (verwerkingsDatum == null) {
            buf.append("000000");
        } else {
            SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
            String date = df.format(verwerkingsDatum);
            buf.append(date);
        }
        /* Postbank verwerkt alleen eerste 32 karakters */
        PadUtils.padText(naam, 35, buf);
        buf.append(testCode);
    }
}
