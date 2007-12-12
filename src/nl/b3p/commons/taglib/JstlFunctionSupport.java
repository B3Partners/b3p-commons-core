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
        return set.contains(object);
    }
    
    public static boolean containsKey(Map map, Object object) {
        return map.containsKey(object);
    }
}
