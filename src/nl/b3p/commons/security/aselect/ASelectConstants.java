/*
 * $Id: ASelectConstants.java 699 2005-06-22 11:29:02Z Matthijs $
 */

package nl.b3p.commons.security.aselect;

/**
 * Interface met resultcodes van de A-Select API.
 */
public interface ASelectConstants {
    public static final String ASELECT_OK = "0000";
    
    /* Agent result codes */
    public static final String ASELECT_AGENT_TICKET_UNKNOWN = "010b";
    public static final String ASELECT_AGENT_TICKET_EXPIRED = "010a";
    public static final String ASELECT_AGENT_TICKET_INVALID = "0109";
    public static final String ASELECT_AGENT_AUTHSESSION_EXPIRED = "0102";
}         
