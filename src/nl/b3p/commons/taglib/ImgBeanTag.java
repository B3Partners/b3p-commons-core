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
import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.html.ImgTag;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * Maakt een plaatje uit een bean referentie als uitbreiding op de
 * standaard Struts Img tag.
 * <p>
 * Parameters:
 * <ul>
 * <li>blink - verwijzing naar de bean met de info []
 * <li>bpage - de bean member met de url naar het plaatje[value]
 * </ul>
 * <p>
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2003/02/06 20:30:02 $
 */
public class ImgBeanTag extends ImgTag {
    // ------------------------------------------------------------- Properties
    /**
     * De extra attributen tov de html:link tag
     */
    protected String blink = null;
    protected String bpage = "value";

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
    // --------------------------------------------------------- Public Methods
    /**
     * Render the beginning of the IMG tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Evaluate the body of this tag
        return super.doStartTag();

    }

    /**
     * Render the end of the IMG tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
        // Calculate page from bean, if present and no page attribute
        if (blink != null && bpage != null) {
            // Retrieve the required property
            try {
                Object bean = pageContext.findAttribute(blink);
                page = (String) PropertyUtils.getProperty(bean, bpage);
                if (page == null) {
                    return (EVAL_PAGE);
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

        // Evaluate the remainder of this page
        return super.doEndTag();

    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        blink = null;
        bpage = "value";
    }
}
