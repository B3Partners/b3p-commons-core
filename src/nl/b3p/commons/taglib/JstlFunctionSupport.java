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
 * $Id$
 */
package nl.b3p.commons.taglib;

import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

/**
 * Verschillende handige functies voor gebruik in JSTL. Zie jstl-functions.tld.
 */
public class JstlFunctionSupport {

    public static boolean isUserInRole(HttpServletRequest request, String role) {
        return request.isUserInRole(role);
    }

    public static boolean contains(Set set, Object object) {
        /* NPE indien set null is is semantisch juist, maar in JSP waarschijnlijk
         * niet gewenst en is false returnen "beter"
         */
        if (set == null) {
            return false;
        }
        return set.contains(object);
    }

    public static boolean containsKey(Map map, Object object) {
        /* NPE indien map null is is semantisch juist, maar in JSP waarschijnlijk
         * niet gewenst en is false returnen "beter"
         */
        if (map == null) {
            return false;
        }
        return map.containsKey(object);
    }
}
