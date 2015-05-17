/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.b3p.commons.services;

import java.io.*; 
import javax.servlet.*;

/**
 * Gebruik deze filter om de request encoding in te stellen. Default is deze
 * volgens de HTTP spec ISO-8859-1. Veel browsers sturen GEEN Content-Type met
 * "encoding" parameter. Wel sturen browsers een POST request in de charset van
 * de pagina van het form. De servlet container denkt in dat geval dan dat het 
 * request in ISO-8859-1 is.
 *
 * Indien de pageEncoding dus niet ISO-8859-1 is dit filter gebruiken.
 * 
 * http://wiki.apache.org/tomcat/Tomcat/UTF-8
 * 
 * Voorbeeld van web.xml
 * 
 * <filter>
 *   <filter-name>Request encoding filter</filter-name>
 *   <filter-class>nl.b3p.commons.services.SetRequestEncodingFilter</filter-class>
 *     <init-param>
 *       <param-name>requestEncoding</param-name>
 *       <param-value>UTF-8</param-value>
 *     </init-param>
 * </filter>
 *
 * <filter-mapping>
 *   <filter-name>Request encoding filter</filter-name>
 *   <url-pattern>/*</url-pattern>
 * </filter-mapping>
 *
 */
public class SetRequestEncodingFilter implements Filter {

    private String encoding;

    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("requestEncoding");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next) throws IOException, ServletException {
        // Respect the client-specified character encoding
        // (see HTTP specification section 3.4.1)
        if(null == request.getCharacterEncoding()) {
            request.setCharacterEncoding(encoding);
        }
        next.doFilter(request, response);
    }

    public void destroy() {
    }
}
