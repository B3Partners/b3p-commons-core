/*
 * XmlDatabase.java
 *
 * Created on 28 januari 2005, 15:12
 */

package nl.b3p.commons.security;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.*;
import javax.servlet.http.*;

import nl.b3p.commons.security.xml.*;
import org.exolab.castor.xml.*;

/**
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.1 $ $Date: 2004/04/29 16:47:27 $
 */

public class XmlSecurityDatabase extends HttpServlet {
    
    private static Log log = null;
    
    private static WebappUsers securityDatabase = null;
    private static Hashtable userpasswords = null;
    private static Hashtable userroles = null;
    private static int maxNumOfSessions = 0;
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        // Zet de logger
        log = LogFactory.getLog(this.getClass());
        // Initialize Torque
        try {
            String configLocation = getServletContext().getRealPath(config.getInitParameter("config"));
            log("config pad: " + configLocation);
            FileReader fr = new FileReader(configLocation);
            if (fr==null) {
                if (log.isDebugEnabled())
                    log.debug("config reader is null");
            } else {
                try {
                    securityDatabase = WebappUsers.unmarshal(fr);
                } catch (MarshalException me) {
                    log.error("MarshalException", me);
                } catch (ValidationException ve) {
                    log.error("MarshalException", ve);
                }
            }
        } catch (Exception e) {
            log.error("Xml Security Database load exception", e);
            throw new UnavailableException("Cannot load xml security database");
        }
        
        // Omzetten in gemakkelijk uitleesbare objecten
        if (securityDatabase!=null) {
            if (log.isDebugEnabled())
                log.debug("Xml Database is not null.");
            maxNumOfSessions = securityDatabase.getMaxsessions();
            if (log.isInfoEnabled())
                log.info("Max number of active sessions: " + maxNumOfSessions + " (0 = no limit)");
            userpasswords = new Hashtable();
            userroles = new Hashtable();
            int userCount = securityDatabase.getUserCount();
            for (int i=0; i<userCount; i++) {
                User aUser = null;
                try {
                    aUser = securityDatabase.getUser(i);
                } catch (IndexOutOfBoundsException ioobe) {
                    break;
                }
                if (aUser!=null) {
                    if (log.isDebugEnabled())
                        log.debug("Init user: " + aUser.getUsername());
                    userpasswords.put(aUser.getUsername(), aUser.getPassword());
                    String theRoles = aUser.getRoles();
                    if (theRoles==null || theRoles.length()==0)
                        continue;
                    ArrayList roleList = new ArrayList();
                    int roleCount = securityDatabase.getRoleCount();
                    for (int j=0; j<roleCount; j++) {
                        Role aRole = null;
                        try {
                            aRole = securityDatabase.getRole(j);
                        } catch (IndexOutOfBoundsException ioobe) {
                            break;
                        }
                        if (aRole!=null && theRoles.indexOf(aRole.getRolename())>=0) {
                            roleList.add(aRole.getRolename());
                            if (log.isDebugEnabled())
                                log.debug("  adding role: " + aRole.getRolename());
                        }
                    }
                    userroles.put(aUser.getUsername(), roleList);
                }
            }
        }
        if (log.isInfoEnabled())
            log.info("Initializing Xml Security Database servlet");
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        securityDatabase = null;
        userpasswords = null;
        userroles = null;
        super.destroy();
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter();
        out.close();
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    public static boolean booleanAuthenticate(String username, String password) {
        if (log.isDebugEnabled())
            log.debug("Trying to login: " + username + " with password: " + password);
        int nos = SessionCounter.getActiveSessions();
        if (log.isInfoEnabled())
            log.info("Number of active sessions: " + nos);
        if (maxNumOfSessions>0 && nos>maxNumOfSessions)
            return false;
        if (userpasswords==null)
            return false;
        if (username==null || password==null)
            return false;
        if (userpasswords.containsKey(username) && password.equals((String) userpasswords.get(username))) {
            return true;
        }
        return false;
    }
    
    public static boolean isUserInRole(String username, String role) {
        if (log.isDebugEnabled())
            log.debug("Checking role: " + role + " for user: " + username);
        int nos = SessionCounter.getActiveSessions();
        if (log.isInfoEnabled())
            log.info("Number of active sessions: " + nos);
        if (maxNumOfSessions>0 && nos>maxNumOfSessions)
            return false;
        if (userroles==null)
            return false;
        if (username==null || role==null)
            return false;
        if (userroles.containsKey(username)) {
            ArrayList roleList = (ArrayList) userroles.get(username);
            if (roleList.contains(role)) {
                if (log.isDebugEnabled())
                    log.debug("  OK!");
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return the jndi path for this database.
     * @return
     */
    public static WebappUsers getSecurityDatabase() {
        return (securityDatabase);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "XML Security Database servlet";
    }
    
}
