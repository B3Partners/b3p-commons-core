/*
 * $Id: ASelectAgentClient.java 2202 2005-12-14 16:14:18Z Matthijs $
 */

package nl.b3p.commons.security.aselect;

import java.util.*;
import java.net.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Stuurt requests naar de A-Select Agent en parst response.
 *
 * @author matthijsln
 */
public class ASelectAgentClient implements ASelectClient {
    
    private static Log log = LogFactory.getLog(ASelectAgentClient.class);
    
    private static final String ASELECT_AGENT_EOL = "\r\n";

    private static final int ASELECT_AGENT_DEFAULTPORT = 1495;
        
    private String host;
    private int port;    

    private boolean agentDecodeBug = false;
    
    /* Dit is niet expliciet gespecificeerd in de A-Select documentatie */
    private String charset = "UTF-8";
    
    public ASelectAgentClient(Properties config) {

        host = config.getProperty("agent_host", "localhost");
        port = ASELECT_AGENT_DEFAULTPORT;
        
        try {
            String agentPortString = config.getProperty("agent_port");
            
            if(agentPortString != null) {
                port = Integer.parseInt(agentPortString);
                
                if(port < 0 || port > 65535) {
                    throw new IllegalArgumentException("invalid port number");
                }
            }
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("agent_port must be number");
        }       

        String agentDecodeBugString = config.getProperty("agent_has_decode_bug");
        if(agentDecodeBugString != null) {
            agentDecodeBug = agentDecodeBugString.toLowerCase().equals("true");
        }                    
    }
    
    public void setCharset(String charset) {
        this.charset = charset;
    }
    
    public String getCharset() {
        return this.charset;
    }
    
    public Map performRequest(Map params)
    throws IOException {
        
        StringBuffer request = new StringBuffer("");
        Iterator i = params.entrySet().iterator();
        boolean first = true;
        while(i.hasNext()) {
            if(!first) {
                request.append("&");
            } else {
                first = false;
            }
            
            Map.Entry entry = (Map.Entry)i.next();           
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            
            /* workaround voor bug in Agent waardoor deze het request niet
             * URL-decodeert.
             * Zie http://nic.surfnet.nl/scripts/wa.exe?A1=ind0503&L=a-select#4
             */
            if(!agentDecodeBug) {
                key = URLEncoder.encode(key, charset);
                value = URLEncoder.encode(key, charset);
            }
            request.append(key).append("=").append(value);
        }   
        request.append(ASELECT_AGENT_EOL);        
        
        String requestString = request.toString();

        log.debug("connecting to agent");
        Socket socket = new Socket(host, port);
        
        Map result;
        try {
            log.debug("request: " + requestString);
            socket.getOutputStream().write(requestString.getBytes());

            String response = (new BufferedReader(new InputStreamReader(socket.getInputStream(), charset))).readLine();
            log.debug("response: " + response);

            result = ASelectUtils.parseQueryString(response, charset);
            result.put("complete_response", response);
        } finally {
            socket.close();
        }
        return result;            
    }
}
