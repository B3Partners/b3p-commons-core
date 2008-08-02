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
 * $Id: ASelectConstants.java 699 2005-06-22 11:29:02Z Matthijs $
 */
package nl.b3p.commons.security.aselect;

/**
 * Interface met resultcodes van de A-Select API.
 */
public interface ASelectConstants {

    public static final String ASELECT_OK = "0000";
    /* Agent result codes */
    public static final String ASELECT_AGENT_TICKET_UNKNOWN = "010b";
    public static final String ASELECT_AGENT_TICKET_EXPIRED = "010a";
    public static final String ASELECT_AGENT_TICKET_INVALID = "0109";
    public static final String ASELECT_AGENT_AUTHSESSION_EXPIRED = "0102";
}         
