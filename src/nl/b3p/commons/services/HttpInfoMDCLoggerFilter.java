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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.MDC;

/**
 * Dit filter logt de volgende keys in de log4j Mapped Diagnostic Context (MDC):
 * HttpRemoteAddr
 * HttpRemotePort
 * HttpRequestURI
 * HttpSessionId
 *
 * Deze kunnen bijvoorbeeld worden gebruikt door de PatternLayout met 
 * %X{remoteAddr}:%X{remotePort} om IP adres en port te loggen, wat meestal 
 * handiger is dan de thread naam. N.B. in HTTP/1.1 kunnen door de browser 
 * meerdere requests over dezelfde connectie worden gedaan, en ook door de 
 * applicatie intern kunnen opnieuw requests worden gedaan. Een IP:Port duidt
 * dus niet perse een uniek HTTP request door de browser aan.
 *
 * Ook de sessionId wordt gelogd (van de huidige sessie, niet de door de client
 * gespecificeerde session Id). <b>Let op: indien de init param "createSession"
 * op "true" staat maakt dit filter een nieuwe sessie indien deze nog niet 
 * bestaat!</b>
 */
public class HttpInfoMDCLoggerFilter implements Filter {

    boolean createSession;
    private static final String KEY_REMOTE_ADDR = "HttpRemoteAddr";
    private static final String KEY_REMOTE_PORT = "HttpRemotePort";
    private static final String KEY_REQUEST_URI = "HttpRequestURI";
    private static final String KEY_SESSION_ID = "HttpSessionId";

    public void init(FilterConfig filterConfig) throws ServletException {
        createSession = "true".equals(filterConfig.getInitParameter("createSession"));
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        MDC.put(KEY_REMOTE_ADDR, req.getRemoteAddr());
        MDC.put(KEY_REMOTE_PORT, req.getRemotePort() + "");
        MDC.put(KEY_REQUEST_URI, req.getRequestURI());

        if (createSession) {
            MDC.put(KEY_SESSION_ID, req.getSession().getId());
        } else {
            HttpSession sess = req.getSession(false);
            if (sess != null) {
                MDC.put(KEY_SESSION_ID, req.getSession().getId());
            } else {
                MDC.remove(KEY_SESSION_ID);
            }
        }

        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}