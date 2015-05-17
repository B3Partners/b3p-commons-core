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
 * $Id: NaamBegunstigde.java 2202 2005-12-14 16:14:18Z Matthijs $
 */
package nl.b3p.commons.clieop3.record;

import nl.b3p.commons.clieop3.ClieOp3OutputStream;
import nl.b3p.commons.clieop3.PadUtils;

public class NaamBegunstigde extends Record {

    private static final String RECORDCODE = "0170";
    private static final char VARIANTCODE = 'B';
    private String naam;

    public NaamBegunstigde(String naam) {
        super(RECORDCODE, VARIANTCODE);

        this.naam = ClieOp3OutputStream.cleanClieOp3String(naam);
    }

    protected void appendRecordContents(StringBuffer buf) {
        /* Postbank verwerkt alleen eerste 32 karakters */
        PadUtils.padText(naam, 35, buf);
    }
}
