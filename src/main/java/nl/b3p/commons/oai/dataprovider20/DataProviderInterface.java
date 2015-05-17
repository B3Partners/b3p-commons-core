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

import nl.b3p.commons.oai.dataprovider20.error.OAIError;
import org.w3c.dom.*;

/**
 * Every data provider class has to implement this interface
 */
public interface DataProviderInterface {

    /**
     * 
     * @return Element
     */
    public Element identify();

    /**
     * 
     * @return Element
     */
    public Element listSets() throws OAIError;

    /**
     * 
     * @param resumptionToken
     * @return Element
     */
    public Element listSets(String resumptionToken) throws OAIError;

    /**
     * 
     * @param id
     * @return Element
     */
    public Element listMetadataFormats(String identifier) throws OAIError;

    /**
     * 
     * @param fileafter
     * @param filebefore
     * @param setspec
     * @param metaformat
     * @return Element
     */
    public Element listIdentifiers(String fileafter, String filebefore,
            String setspec, String metaformat) throws OAIError;

    /**
     * 
     * @param resumptionToken
     * @return Element
     */
    public Element listIdentifiers(String resumptionToken) throws OAIError;

    /**
     * 
     * @param fileafter
     * @param filebefore
     * @param setspec
     * @param metaformat
     * @return Element
     */
    public Element listRecords(String fileafter, String filebefore,
            String setspec, String metaformat) throws OAIError;

    /**
     * 
     * @param resumptionToken
     * @return Element
     */
    public Element listRecords(String resumptionToken) throws OAIError;

    /**
     * 
     * @param handle
     * @param metaformat
     * @return Element
     */
    public Element getRecord(String handle, String metaformat) throws OAIError;
}
