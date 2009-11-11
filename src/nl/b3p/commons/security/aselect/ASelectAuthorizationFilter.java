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
 * $Id: ASelectAuthorizationFilter.java 2291 2006-01-12 09:51:50Z Matthijs $
 */
package nl.b3p.commons.security.aselect;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Dit is een authorisatie-filter die gebruik maakt van A-Select. Het zorgt
 * ervoor dat alle requests die geforward worden in de filter-chain zijn
 * gedaan door een gebruiker die is geautoriseerd via A-Select.
 * <p>
 * Verschillende API's: webserver-filter is speciaal omdat die alleen via het
 * wel of niet aanwezig zijn van cookies gezet door de filter communiceert en
 * de applicatie dus niet zelf requests initieert.
 * <p>
 * Deze filter reserveert het gebruik van de URL parameters rid en
 * aselect_credentials.
 * <p>
 * Filter init-params:
 * <ul>
 *   <li>api: agent, (webserver-filter, server)<br>
 *    Welke API moet worden gebruikt voor communicatie met de A-Select
 *    server (voorlopig is alleen agent geimplementeerd welke gebruikt
 *    wordt in piblweb).
 *   </li>
 *   <li>
 *   app_id<br>
 *     A-Select application ID. Alleen van toepassing bij agent of server
 *     API.
 *   </li>
 *   <li>agent_host (optioneel)</li>
 *   <li>agent_port (optioneel, default 1495)</li>
 *   <li>server_url (verplicht bij server api)</li>
 *   <li>server_id (verplicht bij server api)</li>
 *   <li>error_page<br>
 *      Moet nog aan gewerkt worden.
 *   </li>
 * <p>
 * Dit filter plaatst de volgende keys in de log4j Mapped Diagnostic Context 
 * (MDC):
 * ASelectUid
 * ASelectTicket
 * <p>
 * TODO impl api webserver-filter, server<br>
 * TODO impl error_page<br>
 * TODO error messages uit resource files
 *
 * @author  matthijsln
 */
public class ASelectAuthorizationFilter implements Filter, ASelectConstants {

    private static Log log = LogFactory.getLog(ASelectAuthorizationFilter.class);
    private static final int ASELECT_API_AGENT = 0;
    private static final int ASELECT_API_SERVER = 1;
    private static final int ASELECT_API_WEBSERVERFILTER = 2;
    private static final String ASELECT_ORIGINAL_APP_URL = ASelectAuthorizationFilter.class.getName() + ".ORIGINAL_APP_URL";  /* naam van sessie attribute */

    private static final String ASELECT_REDIRECT_BACK = "aselect__redirect_back";       /* naam van URL param */

    /* Indien in sessie dit attribuut aanwezig is dan gebruik forced_logon
     * Kan worden gezet door forceLogin() bijv na killen van ticket
     */
    private static final String ASELECT_FORCE_LOGIN = ASelectAuthorizationFilter.class.getName() + ".FORCE_LOGIN";
    /**
     * Indien de configuratie niet in orde is wordt geen exception gethrowed
     * in de init() methode, omdat dan de filter niet wordt geinstalleerd. Deze
     * variabele wordt dan false gezet. In de doFilter() methode wordt indien
     * configOK = false pas een Exception gethrowd zodat niet bij een foute
     * configuratie de applicatie zonder authorisatie toch kan worden gebruikt.
     */
    private boolean configOK = false;
    private FilterConfig filterConfig = null;
    private int api;
    private String appId;
    private boolean avoidURLParamsInRedirect;

    /* TODO remote_organization */
    private ASelectClient client;

    public ASelectAuthorizationFilter() {
    }

    /**
     * Deze functie initialiseert het filter en controleert de init parameters.
     * Indien deze parameters ongeldig zijn wordt dit gelogd en wordt in de
     * doFilter() methode een exception gethrowd en niet de filter chain
     * voortgezet.
     */
    public void init(FilterConfig filterConfig) {
        if (log.isInfoEnabled()) {
            log.info("init");
        }

        configOK = false;

        try {
            this.filterConfig = filterConfig;

            String apiString = filterConfig.getInitParameter("api");
            if (apiString != null) {
                apiString = apiString.toLowerCase();
            }

            /* voorlopig is alleen de agent API geimplementeerd. */

            if (apiString.equals("agent")) {
                api = ASELECT_API_AGENT;
                client = new ASelectAgentClient(filterConfigToProperties(filterConfig));
            } else {
                throw new IllegalArgumentException("invalid \"api\" init parameter");
            }

            appId = filterConfig.getInitParameter("app_id");
            if (appId == null) {
                throw new IllegalArgumentException("\"app_id\" init parameter required");
            }

            avoidURLParamsInRedirect = "true".equals(filterConfig.getInitParameter("avoid_url_params_in_redirect"));

            configOK = true;
        } catch (IllegalArgumentException e) {
            log.error("error initializing filter " + filterConfig.getFilterName(), e);
        }

        if (!configOK) {
            log.error("bad config; disallowing access to application");
        }
    }

    /**
     * Hulpfunctie om een FilterConfig object om te zetten naar Properties
     */
    private Properties filterConfigToProperties(FilterConfig c) {
        Properties result = new Properties();

        Enumeration params = c.getInitParameterNames();

        while (params.hasMoreElements()) {
            String name = (String) params.nextElement();
            result.setProperty(name, c.getInitParameter(name));
        }
        return result;
    }

    /**
     * Deze methode zorgt ervoor dat alleen chain.doFilter() wordt aangeroepen
     * indien de client is geautoriseerd.
     * <p>
     * Indien de client niet geautoriseerd is wordt deze geredirect naar de
     * A-Select server.
     * <p>
     * Indien de client zich wel geautoriseerd heeft maar de credentials of het
     * ticket is ongeldig wordt een exception gethrowd (TODO naar error page)
     *
     * @throws ServletException
     *   Bij ongeldige configuratie; foute credentials/fout ticket; indien I/O
     *   fout optreedt bij communicatie met de A-Select.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        org.apache.log4j.MDC.remove("ASelectUid");
        org.apache.log4j.MDC.remove("ASelectTicket");

        if (!configOK) {
            throw new ServletException("Invalid filter configuration");
        }

        boolean authorized = false;

        try {
            authorized = checkAuthorization((HttpServletRequest) request, (HttpServletResponse) response);

            if (authorized) {
                chain.doFilter(request, response);
            } else {
                /* gebruiker is al geredirect naar A-Select server danwel naar
                 * foutpagina (FIXME)
                 */
            }
        } catch (ASelectAuthorizationException ae) {
            if (log.isErrorEnabled()) {
                log.error("ASelectAuthorizationException: " + ae.getMessage());
            }
            // TODO redirect naar errorpage van applicatie
            throw new ServletException(ae);
        }
    }

    public void destroy() {
    }

    public static void forceLogin(HttpSession session) {
        /* wordt niet in A-Select 1.3 ondersteund */
        session.setAttribute(ASELECT_FORCE_LOGIN, "true");
    }

    /**
     * Deze methode retourneert <code>true</code> indien de gebruiker is geautoriseerd. Indien
     * de gebruiker voor het eerst bij de webapplicatie komt wordt deze doorgestuurd
     * naar de A-Select server met response.sendRedirect() en wordt <code>false</code>
     * geretourneerd, wat betekent dat verder niks met de response moet worden gedaan/niet
     * chain.doFilter() moet worden aangeroepen. Er kan ook false worden teruggegeven indien
     * met response.sendRedirect() is geredirect naar de originele app URL indien deze was
     * omgeschreven bij de optie "avoid_url_params_in_redirect".
     * <p>
     * Indien een fout optreedt wordt een exception gethrowed en moet geen toegang
     * worden verleend tot de applicatie.
     */
    private boolean checkAuthorization(HttpServletRequest request, HttpServletResponse response)
            throws ASelectAuthorizationException, ServletException, IOException {

        if (api == ASELECT_API_WEBSERVERFILTER) {
            return verifyWebFilterCookies(request);
        }

        /* Deze twee request parameters worden door de A-Select server meegestuurd
         * nadat de gebruiker zich heeft geauthoriseerd bij de A-Select server.
         */
        String asRid = request.getParameter("rid");
        String asCredentials = request.getParameter("aselect_credentials");

        if ((asRid != null) && (asCredentials != null)) {
            if (log.isInfoEnabled()) {
                log.info("verifying credentials");
            }
            if (!verifyCredentials(request, asRid, asCredentials)) {
                /* FIXME redirect naar foutpagina */
                throw new ServletException("Credentials could not be verified");
            } else {
                /* credentials zijn ok, geef toegang tot app */

                if (avoidURLParamsInRedirect && "true".equals(request.getParameter(ASELECT_REDIRECT_BACK))) {
                    HttpSession sess = request.getSession();
                    String originalURL = (String) sess.getAttribute(ASELECT_ORIGINAL_APP_URL);
                    if (originalURL == null) {
                        throw new ServletException("Invalid state: no original app URL in session to redirect to");
                    }
                    sess.removeAttribute(ASELECT_ORIGINAL_APP_URL);
                    if (log.isInfoEnabled()) {
                        log.info("redirecting to original app URL: " + originalURL);
                    }
                    /* bijkomstige handigheid is dat de rid en aselect_credentials niet
                     * in de originalURL voorkomen en deze dus ook niet in de adresbalk
                     * van de browser blijven staan
                     */
                    response.sendRedirect(originalURL);
                    return false;
                }

                return true;
            }
        }

        ASelectTicket ticket = ASelectTicket.getFromSession(request.getSession());

        if (ticket != null) {

            /* deze methode throws een exception indien het ticket niet geldig
             * is of als het niet kon worden gecontroleerd
             */
            try {
                ticket.verify();
            } catch (IOException e) {
                throw new ServletException("Error verifying ticket", e);
            }

            org.apache.log4j.MDC.put("ASelectUid", ticket.getUid());
            org.apache.log4j.MDC.put("ASelectTicket", ticket.getTicketId());

            return true;
        }

        if (log.isInfoEnabled()) {
            log.info("redirecting to A-Select server");
        }
        redirectToASelect(request, response);
        return false;
    }

    private boolean verifyWebFilterCookies(HttpServletRequest request)
            throws ASelectAuthorizationException {
        /* TODO */
        throw new ASelectAuthorizationException("not implemented");
    }

    private boolean isUserError(String code) {
        return ASELECT_AGENT_TICKET_UNKNOWN.equals(code) || ASELECT_AGENT_TICKET_EXPIRED.equals(code) || ASELECT_AGENT_TICKET_INVALID.equals(code) || ASELECT_AGENT_AUTHSESSION_EXPIRED.equals(code);
    }

    private boolean verifyCredentials(HttpServletRequest request, String rid, String credentials)
            throws ServletException, ASelectAuthorizationException {

        Map params = new Hashtable();
        params.put("request", "verify_credentials");
        params.put("rid", rid);
        params.put("aselect_credentials", credentials);

        Map asResponse = null;
        try {
            asResponse = client.performRequest(params);
        } catch (IOException e) {
            throw new ServletException(e);
        }

        String asResult = (String) asResponse.get("result_code");
        String asTicket = (String) asResponse.get("ticket");
        String asTicketExpTime = (String) asResponse.get("ticket_exp_time");
        String asUid = (String) asResponse.get("uid");
        String asOrganization = (String) asResponse.get("organization");
        String asAuthSPLevel = (String) asResponse.get("authsp_level");
        String asAuthSP = (String) asResponse.get("authsp");
        String asAttributes = (String) asResponse.get("attributes");

        if (!ASELECT_OK.equals(asResult)) {
            /* Indien user error naar errorpage met melding ala
             *  "Uw sessie is verlopen of ongeldig of U heeft te lang gewacht
             *   met inloggen. Probeer het opnieuw."
             * Eventueel met link of met refresh na aantal seconden
             *
             * TODO bij request.getSession().isNew() error met "Uw browser moet
             * cookies accepteren" oid
             *
             * Bij andere errors ServletException
             */
            log.info("Received error code \"" + asResult + "\" from A-Select when verifying credentials (from IP: " + request.getRemoteHost() + ")");
            if (isUserError(asResult)) {
                /* FIXME foutmelding afhankelijk van result code */
                return false;
            } else {
                throw new ServletException("A-Select communication error");
            }
        } else {
            Date startTime, expTime;

            /* De Agent geeft ticket_start_time mee maar de Server niet omdat
             * bij de server api je zelf bij moet houden wanneer het ticket is
             * verlopen zoals de agent doet.
             *
             * Een tekortkoming van het A-Select protocol is dat indien de systeemtijd
             * van de A-Select server en de client niet overeenkomen het ticket
             * meteen expired of de duur van het ticket verkeerd is (te  kort
             * danwel te lang).
             *
             * Nou is in een webapplicatie omgeving de systeemtijd van de server
             * die de echte eindtijd van het ticket bepaalt natuurlijk wel te
             * synchronizeren met die van de A-Select server die de originele
             * eindtijd van het ticket bepaalt.
             */

            if (api == ASELECT_API_AGENT) {
                startTime = new Date(Long.parseLong((String) asResponse.get("ticket_start_time")));
            } else {
                startTime = new Date();
            }

            try {
                expTime = new Date(Long.parseLong(asTicketExpTime));

                log.debug("Ticket start date/time:      " + startTime);
                log.debug("Ticket expiration date/time: " + expTime);

                if (startTime.compareTo(expTime) > 0) {
                    throw new ServletException("A-Select server specified ticket expiration time in the past");
                }

            } catch (NumberFormatException nfe) {
                log.error("invalid date received from A-Select server", nfe);
                throw new ServletException("A-Select communication error");
            }

            ASelectTicket ticket;

            if (api == ASELECT_API_AGENT) {
                ticket = new ASelectAgentTicket(asTicket, appId, startTime, expTime,
                        asUid, asOrganization, asAuthSPLevel, asAuthSP, asAttributes,
                        (ASelectAgentClient) client);
            } else {
                /* api == ASELECT_API_SERVER */
                // TODO impl deze ticket, die adv expTime verify() implementeert
                ticket = null;
            //ticket = new ASelectServerTicket(asTicket, startTime, expTime,
            //    asUid, asOrganization, asAuthSPLevel, asAuthSP, asAttributes);
            }
            ticket.putOnSession(request.getSession());
            return true;
        }
    }

    private void redirectToASelect(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        /* XXX alleen simpele GET requests kunnen worden geredirect. Speciale 
         * headers en een request body (zoals bij POST requests) worden niet
         * bewaard. Bij normaal gebruik is dit geen probleem, maar het kan wel 
         * eens voorkomen dat het ticket expired is (of als bijv als app 
         * gerestart is) op het moment dat een gebruiker een knop indrukt die 
         * een POST request veroorzaakt waardoor opnieuw een redirect nodig is.
         */

        String queryString = request.getQueryString();
        String completeURL = request.getRequestURL() + (queryString != null ? "?" + queryString : "");

        if (log.isWarnEnabled() && !"GET".equals(request.getMethod())) {
            log.warn("redirect to A-Select for request with other method than GET (" + request.getMethod() + " " + completeURL + ")");
        }

        StringBuffer appURL;

        if (api == ASELECT_API_AGENT && avoidURLParamsInRedirect) {

            /* Sla de originele URL op in sessie. De URL die we aan de A-Select 
             * Agent geven is een URL die we zelf verzinnen waardoor we zeker 
             * weten dat deze niet meer dan één URL parameter bevat (omdat een
             * URL met een & erin door een bug in de A-Select Agent niet goed 
             * wordt terug geredirect)
             */

            HttpSession sess = request.getSession();
            sess.setAttribute(ASELECT_ORIGINAL_APP_URL, completeURL);

            appURL = request.getRequestURL();
            appURL.append("?" + ASELECT_REDIRECT_BACK + "=true");
        } else {
            appURL = new StringBuffer(completeURL);
        }

        Map asResponse = null;
        try {
            Map params = new Hashtable();
            params.put("request", "authenticate");
            params.put("app_id", appId);
            params.put("app_url", appURL.toString());
            //params.put("uid", uid); // TODO uid evt uit request halen zodat dit door app kan worden meegegeven?
            //params.put("remote_organization", remoteOrganization); // TODO uit init params

            /* wordt niet in A-Select 1.3 ondersteund */
            if (request.getSession().getAttribute(ASELECT_FORCE_LOGIN) != null) {
                params.put("forced_logon", "true");
                request.getSession().removeAttribute(ASELECT_FORCE_LOGIN);
            }

            asResponse = client.performRequest(params);
        } catch (IOException e) {
            log.error("error initiating authentication with A-Select", e);
            throw new ServletException("A-Select communication error");
        }

        String asResult = (String) asResponse.get("result_code");
        String asURL = (String) asResponse.get("as_url");
        String asServer = (String) asResponse.get("a-select-server");
        String asRId = (String) asResponse.get("rid");

        /* een foutcode in de response is bij authenticate nooit een user error */
        if (!ASELECT_OK.equals(asResult)) {
            if (log.isErrorEnabled()) {
                throw new ServletException("A-Select communication error; result code " + asResult);
            }
        }

        if (asURL == null || asServer == null || asRId == null) {
            log.error("missing result parameters; response: " + asResponse.get("complete_response"));
            throw new ServletException("A-Select communication error");
        }

        /* redirect naar URL verkregen van A-Select */

        Map params = new Hashtable();
        params.put("a-select-server", asServer);
        params.put("rid", asRId);

        try {
            String redirectURL = ASelectUtils.appendQueryParameters(asURL, params, client.getCharset());
            response.sendRedirect(redirectURL);
        } catch (UnsupportedEncodingException e) {
            throw new ServletException("Internal error", e);
        } catch (IllegalStateException e) {
            throw new ServletException(e);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }
}
