package nl.b3p.commons.taglib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
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
        if (blink != null && bpage!=null) {
            // Retrieve the required property
            try {
                Object bean = pageContext.findAttribute(blink);
                page = (String) PropertyUtils.getProperty(bean, bpage);
                if (page==null)
                    return (EVAL_PAGE);
            } catch (IllegalAccessException e) {
                throw new JspException("No access: " + e.getMessage());
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                throw new JspException("No result: " + e.getMessage());
            } catch (NoSuchMethodException e) {
                throw new JspException("No method: " + e.getMessage());
            } catch (Exception e) {}
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
