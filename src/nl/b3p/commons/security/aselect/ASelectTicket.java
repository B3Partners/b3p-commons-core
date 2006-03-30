/*
 * $Id: ASelectTicket.java 2291 2006-01-12 09:51:50Z Matthijs $
 */

package nl.b3p.commons.security.aselect;

import java.util.Date;
import java.util.Map;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Deze class stelt een A-Select ticket voor en wordt door de 
 * ASelectAuthorizationFilter op de sessie gezet indien de gebruiker 
 * geautoriseerd is via A-Select.
 * <p>
 * Webapplicaties kunnen gebruik maken van dit object uit de sessie om toegang
 * te krijgen tot de eigenschappen van de ingelogde gebruiker. Met de static
 * methode <code>getFromSession(HttpSession session)</code> kan een ticket 
 * uit een sessie worden gehaald.
 * <p>
 * @author matthijsln
 */
public abstract class ASelectTicket {
    
    protected static Log log = LogFactory.getLog(ASelectTicket.class);
    
    /**
     * Onder deze key wordt een ticket opgeslagen in een HttpSession
     */
    private static final String TICKET_SESSION_KEY = ASelectTicket.class.getName() + ".TICKET";

    /* A-Select ticket attributen */
    
    private String ticket;
    private String appId;
    private String uid;
    private String organization;
    private Date startTime;
    private Date expirationTime;
    private String authSPLevel;
    private String authSP;
    private String attributes; 
    /* TODO: base64/cgi/url decode attributes naar Map, wordt nu niet gedecodeerd */
    
    private HttpSession session;

    protected ASelectTicket(String ticket, String appId, Date startTime, Date expTime, String uid,
        String organization, String authSPLevel, String authSP, String attributes) {
        this.ticket = ticket;
        this.appId = appId;
        this.startTime = startTime;
        this.expirationTime = expTime;
        this.uid = uid;
        this.organization = organization;
        this.authSPLevel = authSPLevel;
        this.attributes = attributes;
    }
    
    protected abstract void doVerify()
    throws IOException, ASelectAuthorizationException;
    
    /**
     * Verifieert of het ticket geldig is. 
     * <p>
     * <b>Indien dit ticket niet geldig is wordt deze uit de sessie verwijderd en
     * wordt een ASelectAuthorizationException gethrowed.</b>
     * 
     * @throws ASelectAuthorizationException als het ticket ongeldig is
     * @throws IOException indien er een fout optreedt bij communicatie met A-Select
     * @throws UnsupportedOperationException indien deze methode niet van 
     *   toepassing is (bij webserver-filter api)
     */
    public void verify()
    throws IOException, ASelectAuthorizationException {
        try {
            doVerify();
        } catch(ASelectAuthorizationException aae) {
            removeFromSession();
            throw aae;
        }
    }
    
    protected abstract void doKill()
    throws IOException;
    
    /**
     * Maakt het ticket ongeldig en verwijderd deze uit de sessie.
     */
    public void kill()
    throws IOException {
        doKill();
        removeFromSession();
    }
    
    private void removeFromSession() {
        if(session != null) {
            session.removeAttribute(TICKET_SESSION_KEY);
        }
    }

    /**
     * Deze methode heeft package visibility omdat alleen ASelectAuthorizationFilter
     * een ticket op de sessie kan zetten.
     */
    void putOnSession(HttpSession session) {
        this.session = session;
        session.setAttribute(TICKET_SESSION_KEY, this);
    }

    /**
     * Geeft het A-Select ticket indien die aanwezig is in de sessie of
     * null wanneer er geen ticket is of de sessie ongeldig is.
     */
    public static ASelectTicket getFromSession(HttpSession session) {
        ASelectTicket t = null;
        try {
            t = (ASelectTicket)session.getAttribute(TICKET_SESSION_KEY);
        } catch(IllegalStateException ise) {
            log.error("getFromSession(): session IllegalStateException");
        } catch(ClassCastException cce) {
            log.error("getFromSession(): not an ASelectTicket object in session", cce);
        }
        return t;
    }  

    /**
     * @return ticket id dat door de A-Select server is afgegeven na verificatie van
     * de credentials. Deze waarde is opaque voor de applicatie.
     */
    public String getTicketId() {
        return ticket;
    }
    
    public String getAppId() {
        return appId;
    }
    
    public String getUid() {
        return uid;
    }

    public String getOrganization() {
        return organization;
    }

    /**
     * @return de attributen zoals die zijn ontvangen van A-Select, niet
     *   base64/cgi/url gedecodeerd
     */
    public String getUndecodedAttributes() {
        return attributes;
    }
    
    public Date getStartTime() {
        return startTime;
    }
    
    public Date getExpirationTime() {
        return expirationTime;
    }
    
    public String getAuthSPLevel() {
        return authSPLevel;
    }
    
    public String getAuthSP() {
        return authSP;
    }               
}
