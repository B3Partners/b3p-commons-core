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
package nl.b3p.commons.taglib;

import java.util.*;
import java.text.SimpleDateFormat;

import java.sql.Timestamp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import nl.b3p.commons.services.*;
import org.apache.struts.taglib.TagUtils;

/**
 * Tag die een dtum volgens de locale print, indien het een datum is.
 *
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.5 $ $Date: 2003/11/04 22:06:08 $
 */
public class WriteDateTag extends TagSupport {

    protected Log log = LogFactory.getLog(this.getClass());    // ------------------------------------------------------------- Properties
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
        if (name != null) {
            // er zou een datum te vinden moeten zijn
            if (TagUtils.getInstance().lookup(pageContext, name, scope) == null) {
                return (SKIP_BODY);  // Nothing to output
            // Look up the requested property value
            }
            value = TagUtils.getInstance().lookup(pageContext, name, property, scope);
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
        if (value != null) {
            if (value instanceof Date) {
                output = FormUtils.DateToString((Date) value, locale);
                if (log.isDebugEnabled()) {
                    log.debug("Date: " + output);
                }
            } else if (value instanceof Timestamp) {
                Timestamp temptime = (Timestamp) value;
                Date tempdate = new Date();
                tempdate.setTime(temptime.getTime());
                output = FormUtils.DateToString(tempdate, locale);
                if (log.isDebugEnabled()) {
                    log.debug("Timestamp: " + output);
                }
            } else if (value instanceof String) {
                SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                sdf.applyPattern("yyyy-MM-dd HH:mm");
                Date tempdate = null;
                try {
                    tempdate = sdf.parse((String) value);
                    output = FormUtils.DateToString(tempdate, locale);
                    if (log.isDebugEnabled()) {
                        log.debug("String (yyyy-MM-dd HH:mm): " + output);
                    }
                } catch (java.text.ParseException pe) {
                    output = ((String) value).trim();
                    int spacedex = output.indexOf(' ');
                    if (spacedex > 1) {
                        output = output.substring(0, spacedex);
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("No conversion: " + output);
                    }
                }
            }
        }
        TagUtils.getInstance().write(pageContext, output);

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
