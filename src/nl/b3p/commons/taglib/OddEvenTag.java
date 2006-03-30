package nl.b3p.commons.taglib;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.ServletRequest;
import java.io.PrintWriter;
import java.io.IOException;

import java.util.*;

/**
 * alterneert uitvoertekst op basis van iteratie loop
 * <p>
 * Parameters:
 * <ul>
 * <li>loop - variabele met indexId van iteratie [loop]
 * <li>odd - uitvoer bij oneven loop []
 * <li>even - uitvoer bij even loop []
 * </ul>
 * <p>
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.1 $ $Date: 2003/06/04 21:43:08 $
 */

public class OddEvenTag extends TagSupport {
    
    private String loop = "loop";
    private String odd = null;
    private String even = null;
    
    public OddEvenTag() {
        super();
    }
    
    public void otherDoStartTagOperations()  {
        // niets doen
    }
    
    public boolean theBodyShouldBeEvaluated()  {
        // lege tag
        return false;
    }
    
    
    //
    // methods called from doEndTag()
    //
    public void otherDoEndTagOperations() throws JspException {
        
        if (loop!=null && loop.length()>1 &&
        odd!=null && odd.length()>1 &&
        even!=null && even.length()>1) {
            Integer loopinteger = (Integer) pageContext.findAttribute(loop);
            int loopint = loopinteger.intValue();
            // Print the ending element to our output writer
            JspWriter writer = pageContext.getOut();
            try {
                writer.print(((loopint & 1) == 0) ? even : odd);
            } catch (IOException e) {
                throw new JspException("oddEvenTag Error: " + e.toString());
            }
        }
    }
    
    /**
     *
     * Fill in this method to determine if the rest of the JSP page
     * should be generated after this tag is finished.
     * Called from doEndTag().
     *
     */
    public boolean shouldEvaluateRestOfPageAfterEndTag()  {
        return true;
    }
    
    
    /** .//GEN-BEGIN:doStartTag
     *
     * This method is called when the JSP engine encounters the start tag,
     * after the attributes are processed.
     * Scripting variables (if any) have their values set here.
     * @return EVAL_BODY_TAG if the JSP engine should evaluate the tag body, otherwise return SKIP_BODY.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     */
    public int doStartTag() throws JspException, JspException {
        otherDoStartTagOperations();
        
        if (theBodyShouldBeEvaluated()) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }//GEN-END:doStartTag
    
    /** .//GEN-BEGIN:doEndTag
     *
     *
     * This method is called after the JSP engine finished processing the tag.
     * @return EVAL_PAGE if the JSP engine should continue evaluating the JSP page, otherwise return SKIP_PAGE.
     * This method is automatically generated. Do not modify this method.
     * Instead, modify the methods that this method calls.
     *
     */
    public int doEndTag() throws JspException, JspException {
        otherDoEndTagOperations();
        
        if (shouldEvaluateRestOfPageAfterEndTag()) {
            return EVAL_PAGE;
        } else {
            return SKIP_PAGE;
        }
    }//GEN-END:doEndTag
    
    /** Getter for property loop.
     * @return Value of property loop.
     */
    public java.lang.String getLoop() {
        return loop;
    }
    
    /** Setter for property loop.
     * @param loop New value of property loop.
     */
    public void setLoop(java.lang.String loop) {
        this.loop = loop;
    }
    
    /** Getter for property odd.
     * @return Value of property odd.
     */
    public java.lang.String getOdd() {
        return odd;
    }
    
    /** Setter for property odd.
     * @param odd New value of property odd.
     */
    public void setOdd(java.lang.String odd) {
        this.odd = odd;
    }
    
    /** Getter for property even.
     * @return Value of property even.
     */
    public java.lang.String getEven() {
        return even;
    }
    
    /** Setter for property even.
     * @param even New value of property even.
     */
    public void setEven(java.lang.String even) {
        this.even = even;
    }
    
}
