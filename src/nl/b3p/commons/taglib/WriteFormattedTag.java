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

import java.text.SimpleDateFormat;
import java.text.Format;

import java.text.DecimalFormat;
import java.text.NumberFormat;


import java.util.Locale;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import nl.b3p.commons.services.*;
import org.apache.struts.taglib.TagUtils;

/**
 * Tag die een tekstveld probeert te formateren volgens een opgegeven
 * format en een class. Als er een fout optreedt, dan wordt gewoon
 * de tekst afgedrukt.
 *
 * <b3ps:writeFormatted name="pagesForm" property="datechanged" format="MM-dd-yyyy" formatClass="java.util.Date" scope="request"/>
 * <b3ps:writeFormatted name="pagesForm" property="linenumber" format="#,###.#" formatClass="java.lang.Integer" scope="request"/>
 * <b3ps:writeFormatted name="pagesForm" property="tekst" format="" formatClass="java.lang.String" scope="request"/>
 *
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.4 $ $Date: 2004/10/20 08:53:37 $
 */
public class WriteFormattedTag extends TagSupport {

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
    protected String format = null;

    public String getFormat() {
        return (this.format);
    }

    public void setFormat(String format) {
        this.format = format;
    }
    protected String formatClass = null;

    public String getFormatClass() {
        return (this.formatClass);
    }

    public void setFormatClass(String formatClass) {
        this.formatClass = formatClass;
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
            if (TagUtils.getInstance().lookup(pageContext, name, scope) == null) {
                return (SKIP_BODY);  // Nothing to output
            // Look up the requested property value
            }
            value = TagUtils.getInstance().lookup(pageContext, name, property, scope);
        }

        // Get locale of this request for date printing
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        Locale locale = request.getLocale();

        Object valueObject = null;
        if (!(value == null || (value instanceof java.lang.String && ((String) value).trim().length() == 0))) {
            // Find class to use for conversion
            String stringValue = value.toString();
            // omzetten naar juiste object type
            try {
                if (formatClass.equals("java.lang.Boolean")) {
                    valueObject = (Object) new java.lang.Boolean(stringValue);
                } else if (formatClass.equals("java.lang.BigDecimal")) {
                    valueObject = (Object) new java.math.BigDecimal(stringValue);
                } else if (formatClass.equals("java.lang.BigInteger")) {
                    valueObject = (Object) new java.math.BigInteger(stringValue);
                } else if (formatClass.equals("java.lang.Byte")) {
                    valueObject = (Object) new java.lang.Byte(stringValue);
                } else if (formatClass.equals("java.lang.Double")) {
                    valueObject = (Object) new java.lang.Double(stringValue);
                } else if (formatClass.equals("java.lang.Float")) {
                    valueObject = (Object) new java.lang.Float(stringValue);
                } else if (formatClass.equals("java.lang.Integer")) {
                    valueObject = (Object) new java.lang.Integer(stringValue);
                } else if (formatClass.equals("java.lang.Long")) {
                    valueObject = (Object) new java.lang.Long(stringValue);
                } else if (formatClass.equals("java.lang.Short")) {
                    valueObject = (Object) new java.lang.Short(stringValue);
                } else if (formatClass.equals("java.lang.String")) {
                    char[] strChars = stringValue.toCharArray();
                    StringBuffer newStr = new StringBuffer();
                    int strLength = strChars.length;
                    if (strLength == 0) {
                        newStr.append("-");
                    }
                    int i = 0;
                    while (i < strLength) {
                        newStr.append(strChars[i]);
                        if (strChars[i] == '\n') {
                            newStr.append("<br>");
                        }
                        i++;
                    }
                    valueObject = newStr.toString();
                } else if (formatClass.equals("java.util.Date")) {
                    valueObject = (Object) FormUtils.StringToDate(stringValue, locale);
                } else if (formatClass.equals("java.util.Date2")) {
                    valueObject = (Object) FormUtils.FormStringToDate(stringValue, locale);
                } else if (formatClass.equals("java.util.Date3")) {
                    valueObject = (Object) FormUtils.SortStringToDate(stringValue, locale);
                } else if (formatClass.equals("java.sql.Date")) {
                    valueObject = (Object) new java.sql.Date(Long.parseLong(stringValue));
                } else if (formatClass.equals("java.sql.Time")) {
                    valueObject = (Object) new java.sql.Time(Long.parseLong(stringValue));
                } else if (formatClass.equals("java.sql.Timestamp")) {
                    valueObject = (Object) new java.sql.Timestamp(Long.parseLong(stringValue));
                } else {
                    valueObject = null;
                }
            } catch (java.lang.NumberFormatException nfe) {
                valueObject = null;
                if (log.isDebugEnabled()) {
                    log.debug("NumberFormat Exception: " + nfe);
                }
            } catch (java.lang.IllegalArgumentException iare) {
                valueObject = null;
                if (log.isDebugEnabled()) {
                    log.debug("Illegal Argument Exception: " + iare);
                }
            }
        }

        // Gevonden objecttype formatteren
        String output = "";
        Format formatter = null;
        if (valueObject != null && format != null) {
            if (valueObject instanceof Number) {
                if (log.isDebugEnabled()) {
                    log.debug("Format using Number");
                }
                try {
                    formatter = NumberFormat.getNumberInstance(locale);
                    ((DecimalFormat) formatter).applyPattern(format);
                } catch (IllegalArgumentException e) {
                    if (log.isDebugEnabled()) {
                        log.debug("writeFormatted Number error: " + e);
                    }
                }
            } else if (valueObject instanceof java.util.Date) {
                if (log.isDebugEnabled()) {
                    log.debug("Format using Date");
                }
                formatter = new SimpleDateFormat(format);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Format using toString for class: " + valueObject.getClass().getName());
                }
            }
        }
        if (valueObject != null) {
            if (formatter != null && format != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Formatter");
                }
                output = formatter.format(valueObject);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("No formatter");
                }
                output = valueObject.toString();
            }
        } else {
            output = "-";
        }
        if (log.isDebugEnabled()) {
            log.debug("Output: " + output);
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
        format = null;
        formatClass = null;

    }
}
