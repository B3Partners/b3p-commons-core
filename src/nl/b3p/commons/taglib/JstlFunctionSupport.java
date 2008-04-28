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
        if(set == null) {
            return false;
        }
        return set.contains(object);
    }
    
    public static boolean containsKey(Map map, Object object) {
        /* NPE indien map null is is semantisch juist, maar in JSP waarschijnlijk
         * niet gewenst en is false returnen "beter"
         */
        if(map == null) {
            return false;
        }
        return map.containsKey(object);
    }
}
