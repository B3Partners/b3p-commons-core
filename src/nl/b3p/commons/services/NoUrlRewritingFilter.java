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
        if (httpOnly && request.getScheme().equals("https")) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, new HttpServletResponseWrapper((HttpServletResponse) response) {

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
