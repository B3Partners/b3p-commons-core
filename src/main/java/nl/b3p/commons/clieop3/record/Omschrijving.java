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
 * $Id: Omschrijving.java 1407 2005-09-05 17:14:21Z Matthijs $
 */
package nl.b3p.commons.clieop3.record;

import nl.b3p.commons.clieop3.ClieOp3OutputStream;
import nl.b3p.commons.clieop3.PadUtils;

public class Omschrijving extends Record {

    private static final String RECORDCODE = "0160";
    private static final char VARIANTCODE = 'A';
    private String omschrijving;

    public Omschrijving(String omschrijving) {
        super(RECORDCODE, VARIANTCODE);

        this.omschrijving = ClieOp3OutputStream.cleanClieOp3String(omschrijving);
    }

    protected void appendRecordContents(StringBuffer buf) {
        PadUtils.padText(omschrijving, 32, buf);
    }
}
