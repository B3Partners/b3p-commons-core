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

/**
 * This interface is used to define the identity of any data provider
 */
public interface Identity {

    /** return oai version */
    public String getOAIversion();

    /** return base url */
    public String getBaseURL();

    /** return data provider name */
    public String getName();

    /** return admin email */
    public String getAdminemail();

    /** return the dp id */
    public String getID();

    /** return the delimiter */
    public String getDelimiter();

    /** return a sample identifier */
    public String getSampleIdentifier();

    /** return schema */
    public String getSchema();

    /** return the earliest date stamp */
    public String getEarliestDatestamp();

    /** return deleted item */
    public String getDeletedItem();

    /** return the granularity */
    public String getGranularity();
}
