/*
 * WriteDateTag.java
 *
 * Created on 2 juli 2002, 10:27
 */

package nl.b3p.commons.taglib;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.sql.Timestamp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import nl.b3p.commons.services.*;

/**
 * Tag die een dtum volgens de locale print, indien het een datum is.
 *
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.5 $ $Date: 2003/11/04 22:06:08 $
 */


public class WriteDateTag extends TagSupport {
    
    protected Log log = LogFactory.getLog(this.getClass());
    
    // ------------------------------------------------------------- Properties
    
    
    
    /**
     * Name of the bean that contains the data we will be rendering.
     */
    protected String name = null;
    
    public String getName() {
        return (this.name);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    /**
     * Name of the property to be accessed on the specified bean.
     */
    protected String property = null;
    
    public String getProperty() {
        return (this.property);
    }
    
    public void setProperty(String property) {
        this.property = property;
    }
    
    
    /**
     * The scope to be searched to retrieve the specified bean.
     */
    protected String scope = null;
    
    public String getScope() {
        return (this.scope);
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
    
    
    
    // --------------------------------------------------------- Public Methods
    
    
    /**
     * Process the start tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        
        // Look up the requested bean
        Object value = null;
        if (name!=null) {
            // er zou een datum te vinden moeten zijn
            if (RequestUtils.lookup(pageContext, name, scope) == null)
                return (SKIP_BODY);  // Nothing to output
            // Look up the requested property value
            value = RequestUtils.lookup(pageContext, name, property, scope);
        }
        if (value == null) {
            // een voorbeeld datum met locale wordt afgedrukt
            Date exValue = new Date();
            value = exValue;
        }
        
        // Get locale of this request for date printing
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        Locale locale = request.getLocale();
        
        // Print this property value to our output writer
        String output = null;
        if (value!=null) {
            if (value instanceof Date) {
                output = FormUtils.DateToString((Date) value, locale);
                if (log.isDebugEnabled())
                    log.debug("Date: " + output);
            } else if (value instanceof Timestamp) {
                Timestamp temptime = (Timestamp) value;
                Date tempdate = new Date();
                tempdate.setTime(temptime.getTime());
                output = FormUtils.DateToString(tempdate, locale);
                if (log.isDebugEnabled())
                    log.debug("Timestamp: " + output);
            } else if (value instanceof String) {
                SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                sdf.applyPattern("yyyy-MM-dd HH:mm");
                Date tempdate = null;
                try {
                    tempdate = sdf.parse((String) value);
                    output = FormUtils.DateToString(tempdate, locale);
                    if (log.isDebugEnabled())
                        log.debug("String (yyyy-MM-dd HH:mm): " + output);
                } catch (java.text.ParseException pe) {
                    output = ((String) value).trim();
                    int spacedex = output.indexOf(' ');
                    if ( spacedex>1 ) {
                        output = output.substring(0, spacedex);
                    }
                    if (log.isDebugEnabled())
                        log.debug("No conversion: " + output);
                }
            }
        }
        ResponseUtils.write(pageContext, output);
        
        // Continue processing this page
        return (SKIP_BODY);
        
    }
    
    
    /**
     * Release all allocated resources.
     */
    public void release() {
        
        super.release();
        name = null;
        property = null;
        scope = null;
        
    }
    
    
}
