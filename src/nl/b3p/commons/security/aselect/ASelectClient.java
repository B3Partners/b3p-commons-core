/*
 * $Id: ASelectClient.java 699 2005-06-22 11:29:02Z Matthijs $
 */

package nl.b3p.commons.security.aselect;

import java.util.Map;
import java.io.IOException;

/**
 * Simpele interface met een methode om een request te sturen naar A-Select (server
 * of agent).
 */
public interface ASelectClient {
    public Map performRequest(Map params) throws IOException;
    public void setCharset(String charset);
    public String getCharset();
}
