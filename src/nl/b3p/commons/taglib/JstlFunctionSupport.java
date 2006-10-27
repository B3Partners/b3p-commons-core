/*
 * $Id$
 */

package nl.b3p.commons.taglib;

import javax.servlet.http.HttpServletRequest;

/**
 * Verschillende handige functies voor gebruik in JSTL. Zie jstl-functions.tld.
 */
public class JstlFunctionSupport {
    public static boolean isUserInRole(HttpServletRequest request, String role) {
        return request.isUserInRole(role);
    }    
}
