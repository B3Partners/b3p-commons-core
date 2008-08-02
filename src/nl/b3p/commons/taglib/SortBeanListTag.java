package nl.b3p.commons.taglib;

import java.util.*;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import nl.b3p.commons.services.*;

/**
 * Sorteert een list van beans op een veld naar keuze.
 * <p>
 * Parameters:
 * <ul>
 * <li>svar - sessievariabele met naam van de list[]
 * <li>field - veldnaam waarop gesorteerd moet worden [label]
 * <li>reverse - reverse sortering [false]
 * </ul>
 * <p>
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.1 $ $Date: 2003/04/30 14:32:10 $
 */
public final class SortBeanListTag extends TagSupport {
    // --------------------------------------------------- Instance Variables
    private Object svar = null;
    private String field = "label";
    private String reverse = "false";
    // ----------------------------------------------------------- Properties
    /** Getter for property svar.
     * @return Value of property svar.
     */
    public Object getSvar() {
        return svar;
    }

    /** Setter for property svar.
     * @param svar New value of property svar.
     */
    public void setSvar(Object svar) {
        this.svar = svar;
    }

    /** Getter for property field.
     * @return Value of property field.
     */
    public java.lang.String getField() {
        return field;
    }

    /** Setter for property field.
     * @param field New value of property field.
     */
    public void setField(java.lang.String field) {
        this.field = field;
    }

    /** Getter for property reverse.
     * @return Value of property reverse.
     */
    public java.lang.String getReverse() {
        return reverse;
    }

    /** Setter for property reverse.
     * @param reverse New value of property reverse.
     */
    public void setReverse(java.lang.String reverse) {
        this.reverse = reverse;
    }
    // ------------------------------------------------------- Public Methods
    /**
     * Defer our checking until the end of this tag is encountered.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        return (SKIP_BODY);

    }

    /**
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // als er geen veld is opgegeven gewoon doorgaan
        if (this.field.length() < 1) {
            return (EVAL_PAGE);
        }
        List theList = null;

        if (this.svar instanceof String) {

            String sessionList = (String) this.svar;
            // als er geen list is opgegeven gewoon doorgaan
            if (sessionList.length() < 1) {
                return (EVAL_PAGE);            // zoek de sessie
            }
            HttpSession session = pageContext.getSession();
            if (session == null) {
                return (EVAL_PAGE);
            }
            theList = (List) session.getAttribute(sessionList);

        } else if (this.svar instanceof List) {

            theList = (List) this.svar;

        }

        if (theList == null || theList.isEmpty()) {
            return (EVAL_PAGE);
        }
        BeanCompare c = new BeanCompare(field);
        try {
            Collections.sort(theList, c);
        } catch (ClassCastException cce) {
        } catch (UnsupportedOperationException uoe) {
        }

        // als er geen reverse is opgegeven dan default false
        if (this.reverse != null && this.reverse.equals("true")) {
            Collections.reverse(theList);
        }
        return (EVAL_PAGE);
    }

    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
        this.svar = null;
        this.field = "label";
        this.reverse = "false";
    }
}
