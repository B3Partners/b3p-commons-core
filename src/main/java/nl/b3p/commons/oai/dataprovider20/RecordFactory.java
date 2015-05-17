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
package nl.b3p.commons.oai.dataprovider20;

import java.util.*;

/**
 * This interface is used to implement any data provider record
 */
public interface RecordFactory {

    /**
     * return a dublin core record
     * 
     * @param id
     *            the record id
     * @return the DC record
     */
    public DCRecord getRecord(String id) throws Exception;

    /**
     * @param from
     *            from parameter in OAI -- accession date
     * @param until
     *            until parameter in OAI -- accession date
     * @param set
     *            set parameter
     * @param startno
     *            start cursor in result set
     * @param size
     *            expected returned size startno and size are used to support
     *            resumptionToken, if no resumption is required, simply assign 0
     *            to size
     */
    public Vector getRecords(String from, String until, String set,
            int startno, int size) throws Exception;

    /**
     * @param startno
     *            start cursor in result set
     * @param endno
     *            end cursor in result set
     */
    public Vector getSets(int startno, int endno) throws Exception;
}
