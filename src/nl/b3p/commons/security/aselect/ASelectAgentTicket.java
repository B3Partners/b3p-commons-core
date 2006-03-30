/*
 * $Id: ASelectAgentTicket.java 2290 2006-01-12 09:50:47Z Matthijs $
 */

package nl.b3p.commons.security.aselect;

import java.util.Date;
import java.io.IOException;
import java.util.Map;
import java.util.Hashtable;

/**
 * Deze class implementeert de doVerify() methode die dit ticket verifieert
 * bij de A-Select Agent met behulp van een ASelectAgentClient die als extra
 * parameter bij de constructor moet worden meegegeven.
 */
public class ASelectAgentTicket extends ASelectTicket implements ASelectConstants {
    private ASelectAgentClient client;
    
    /**
     * Deze constructor heeft bewust package visibility omdat deze class 
     * alleen door ASelectAuthorizationFilter kan worden geinstantieerd.
     */
    ASelectAgentTicket(String ticket, String appId, Date startTime, Date expTime,
            String uid, String organization, String authSPLevel, String authSP,
            String attributes,
            ASelectAgentClient client) {
        super(ticket, appId, startTime, expTime, uid, organization, authSPLevel, authSP,
            attributes);
        this.client = client;
    }
    
    protected void doVerify() 
    throws IOException, ASelectAuthorizationException {
        Map params = new Hashtable();
        params.put("request", "verify_ticket");
        params.put("ticket", getTicketId());
        params.put("uid", getUid());        
        params.put("organization", getOrganization());     
        params.put("app_id", getAppId());

        Map asResponse = null;
        asResponse = client.performRequest(params);

        String asResult = (String)asResponse.get("result_code");        

        if(!ASELECT_OK.equals(asResult)) {
            /* De A-Select Agent maakt geen onderscheid tussen een onbekend of
             * expired ticket
             */
            if(ASELECT_AGENT_TICKET_UNKNOWN.equals(asResult)
            || ASELECT_AGENT_TICKET_EXPIRED.equals(asResult)) {
                throw new ASelectAuthorizationException("Ticket expired or onknown");
            } 
            if(ASELECT_AGENT_TICKET_INVALID.equals(asResult)) {
                throw new ASelectAuthorizationException("Invalid ticket");
            }
            throw new ASelectAuthorizationException("A-Select Agent error code verifying ticket: " + asResult);
        }
    }
    
    protected void doKill()
    throws IOException {
        Map params = new Hashtable();
        params.put("request", "kill_ticket");
        params.put("ticket", getTicketId());
        Map asResponse = null;
        asResponse = client.performRequest(params);
        
        String asResult = (String)asResponse.get("result_code");
        
        if(!ASELECT_OK.equals(asResult)) {
            /* jammer maar helaas */
        }
    }
}
