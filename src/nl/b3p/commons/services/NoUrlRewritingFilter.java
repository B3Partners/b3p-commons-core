/*
 * $Id$
 */

package nl.b3p.commons.services;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Dit filter voorkomt dat URL rewriting wordt toegepast. Standaard wordt dit
 * gedaan indien er nog geen JSESSIONID cookie wordt meegegeven door de 
 * user-agent (het eerste request of indien een user-agent geen cookies 
 * accepteert).
 *
 * Als de init-param "httpOnly" op "true" staat wordt URL rewriting wel
 * toegepast (indien geen cookies) voor requests met het "https" scheme.
 */
public class NoUrlRewritingFilter implements Filter {
    
    private boolean httpOnly;
   
    public void init(FilterConfig filterConfig) throws ServletException {
        httpOnly = "true".equals(filterConfig.getInitParameter("httpOnly"));
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(httpOnly && request.getScheme().equals("https")) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, new HttpServletResponseWrapper((HttpServletResponse)response) {
                public String encodeURL(String url) {
                    return url;
                }

                public String encodeUrl(String url) {
                    return url;
                }

                public String encodeRedirectUrl(String url) {
                    return url;
                }

                public String encodeRedirectURL(String url) {
                    return url;
                }
            });
        }
    }

    public void destroy() {
    }
}
