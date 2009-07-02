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

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import javax.servlet.jsp.JspException;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.commons.beanutils.PropertyUtils;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.LinkTag;

/**
 * Maakt een link met beschrijving uit een bean referentie
 * <p>
 * Parameters:
 * <ul>
 * <li>blink -  verwijzing naar de bean met de info[]
 * <li>bpage -  de bean member met de url van de link [value]
 * <li>bmessage - de bean member met de beschrijving van de link [label]
 * </ul>
 * <p>
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.5 $ $Date: 2004/02/05 19:50:33 $
 */
public class LinkBeanTag extends LinkTag {
    // ------------------------------------------------------------- Properties
    /**
     * De extra attributen tov de html:link tag
     */
    protected String blink = null;
    protected String bpage = "value";
    protected String bmessage = "label";

    /** Getter for property blink.
     * @return Value of property blink.
     */
    public java.lang.String getBlink() {
        return blink;
    }

    /** Setter for property blink.
     * @param blink New value of property blink.
     */
    public void setBlink(java.lang.String blink) {
        this.blink = blink;
    }

    /** Getter for property bpage.
     * @return Value of property bpage.
     */
    public java.lang.String getBpage() {
        return bpage;
    }

    /** Setter for property bpage.
     * @param bpage New value of property bpage.
     */
    public void setBpage(java.lang.String bpage) {
        this.bpage = bpage;
    }

    /** Getter for property bmessage.
     * @return Value of property bmessage.
     */
    public java.lang.String getBmessage() {
        return bmessage;
    }

    /** Setter for property bmessage.
     * @param bmessage New value of property bmessage.
     */
    public void setBmessage(java.lang.String bmessage) {
        this.bmessage = bmessage;
    }    // ---------------------------------------------------- locale variabelen
    /**
     * The servlet context attribute key for our resources.
     */
    private String bundle = Globals.MESSAGES_KEY;
    /**
     * The default Locale for our server.
     */
    private static final Locale defaultLocale = Locale.getDefault();
    /**
     * The session scope key under which our Locale is stored.
     */
    private String localeKey = Globals.LOCALE_KEY;
    /**
     * The message resources for this package.
     */
    private static MessageResources messages =
            MessageResources.getMessageResources("nl.b3p.mesa.services.LocalStrings");
    // --------------------------------------------------------- Public Methods
    /**
     * Render the beginning of the bean hyperlink.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Calculate page from bean, if present and no page attribute
        if (blink != null && bpage != null) {
            // Retrieve the required property
            try {
                Object bean = pageContext.findAttribute(blink);
                page = (String) PropertyUtils.getProperty(bean, bpage);
                if (page == null) {
                    return (SKIP_BODY);
                }
            } catch (IllegalAccessException e) {
                throw new JspException("No access: " + e.getMessage());
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                throw new JspException("No result: " + e.getMessage());
            } catch (NoSuchMethodException e) {
                throw new JspException("No method: " + e.getMessage());
            } catch (Exception e) {
            }
        }

        return super.doStartTag();
    }

    /**
     * Save the associated label from the body content.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        return super.doAfterBody();

    }

    /**
     * Render the end of the hyperlink.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Calculate message from bean, if present and no body content
        if (blink != null && bmessage != null && text == null) {
            // Retrieve the required property
            String key = null;
            try {
                Object bean = pageContext.findAttribute(blink);
                key = (String) PropertyUtils.getProperty(bean, bmessage);
            } catch (IllegalAccessException e) {
                throw new JspException("No access: " + e.getMessage());
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                throw new JspException("No result: " + e.getMessage());
            } catch (NoSuchMethodException e) {
                throw new JspException("No method: " + e.getMessage());
            } catch (Exception e) {
            }

            // Retrieve the message string we are looking for
            String message = TagUtils.getInstance().message(pageContext, this.bundle, this.localeKey, key);
            if (message != null && message.length() > 0) {
                text = message;
            } else if (key != null) {
                text = key;
            } else {
                text = "";
            }
        }

        return super.doEndTag();
    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        blink = null;
        bpage = "value";
        bmessage = "label";
        bundle = Globals.MESSAGES_KEY;
        localeKey = Globals.LOCALE_KEY;

    }
}
