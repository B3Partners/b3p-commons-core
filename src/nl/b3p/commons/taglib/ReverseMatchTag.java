package nl.b3p.commons.taglib;

import java.util.*;

import org.apache.struts.taglib.logic.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.RequestUtils;


/**
 * Evalute the nested body content of this tag if the specified value
 * is a substring of the specified variable.
 *
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.3 $ $Date: 2003/10/12 10:50:31 $
 */


public class ReverseMatchTag extends ConditionalTagBase {
    
    
    // ------------------------------------------------------------- Properties
    
    
    /**
     * The location where the match must exist (<code>start</code> or
     * <code>end</code>), or <code>null</code> for anywhere.
     */
    protected String location = null;
    
    public String getLocation() {
        return (this.location);
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    
    /**
     * The value to which the variable specified by other attributes of this
     * tag will be matched.
     */
    protected String value = null;
    
    public String getValue() {
        return (this.value);
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    
    // --------------------------------------------------------- Public Methods
    
    
    /**
     * Release all allocated resources.
     */
    public void release() {
        
        super.release();
        location = null;
        value = null;
        
    }
    
    
    // ------------------------------------------------------ Protected Methods
    
    
    /**
     * Evaluate the condition that is being tested by this particular tag,
     * and return <code>true</code> if the nested body content of this tag
     * should be evaluated, or <code>false</code> if it should be skipped.
     * This method must be implemented by concrete subclasses.
     *
     * @exception JspException if a JSP exception occurs
     */
    protected boolean condition() throws JspException {
        
        return (condition(true));
        
    }
    
    
    /**
     * Evaluate the condition that is being tested by this particular tag,
     * and return <code>true</code> if the nested body content of this tag
     * should be evaluated, or <code>false</code> if it should be skipped.
     * This method must be implemented by concrete subclasses.
     *
     * @param desired Desired value for a true result
     *
     * @exception JspException if a JSP exception occurs
     */
    protected boolean condition(boolean desired) throws JspException {
        
        // Acquire the specified variable
        String variable = null;
        if (cookie != null) {
            Cookie cookies[] =
            ((HttpServletRequest) pageContext.getRequest()).
            getCookies();
            if (cookies == null)
                cookies = new Cookie[0];
            for (int i = 0; i < cookies.length; i++) {
                if (cookie.equals(cookies[i].getName())) {
                    variable = cookies[i].getValue();
                    break;
                }
            }
        } else if (header != null) {
            variable =
            ((HttpServletRequest) pageContext.getRequest()).
            getHeader(header);
        } else if (name != null) {
            Object value =
            RequestUtils.lookup(pageContext, name, property, scope);
            if (value != null)
                variable = value.toString();
        } else if (parameter != null) {
            variable = pageContext.getRequest().getParameter(parameter);
        } else {
            JspException e = new JspException("Error logic selector");
            RequestUtils.saveException(pageContext, e);
            throw e;
        }
        if (variable == null) {
            JspException e = new JspException("Error logic variable:" + value);
            RequestUtils.saveException(pageContext, e);
            throw e;
        }
        
        // Perform the reversed comparison requested by the location attribute
        boolean matched = false;
        if (location == null) {
            StringTokenizer st = new StringTokenizer(value);
            while (st.hasMoreTokens()) {
                String theToken = st.nextToken();
                if (theToken.equals(variable)) {
                    matched = true;
                    break;
                }
            }
        } else if (location.equals("start")) {
            matched = value.startsWith(variable);
        } else if (location.equals("end")) {
            matched = value.endsWith(variable);
        } else {
            JspException e = new JspException("Error logic location: " + location);
            RequestUtils.saveException(pageContext, e);
            throw e;
        }
        
        // Return the final result
        return (matched == desired);
        
    }
    
    
}
