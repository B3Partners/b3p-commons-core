package nl.b3p.commons.taglib;


import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ResponseUtils;

import nl.b3p.commons.services.*;


/**
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.4 $ $Date: 2003/04/30 14:32:45 $
 */

public class SelectBeanTag extends TagSupport {
    
    
    // ----------------------------------------------------- Instance Variables
    
    
    /**
     * The attribute name.
     */
    private String name = "item";
    private String parameter = "id";
    private String actionClass = "setItem.do";
    private String target = "success";
    // ------------------------------------------------------------- Properties
    /**
     * Return the attribute name.
     */
    public String getName() {
        
        return (this.name);
        
    }
    /**
     * Set the attribute name.
     *
     * @param name The new attribute name
     */
    public void setName(String name) {
        
        this.name = name;
        
    }
    /** Getter for property actionClass.
     * @return Value of property actionClass.
     */
    public java.lang.String getActionClass() {
        return actionClass;
    }
    
    /** Setter for property actionClass.
     * @param action New value of property actionClass.
     */
    public void setActionClass(java.lang.String actionClass) {
        this.actionClass = actionClass;
    }
    
    /** Getter for property target.
     * @return Value of property target.
     */
    public java.lang.String getTarget() {
        return target;
    }
    
    /** Setter for property target.
     * @param target New value of property target.
     */
    public void setTarget(java.lang.String target) {
        this.target = target;
    }
    
    /** Getter for property parameter.
     * @return Value of property parameter.
     */
    public java.lang.String getParameter() {
        return parameter;
    }
    
    /** Setter for property parameter.
     * @param parameter New value of property parameter.
     */
    public void setParameter(java.lang.String parameter) {
        this.parameter = parameter;
    }
    // --------------------------------------------------------- Public Methods
    
    
    /**
     * Render the beginning of the hyperlink.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        
        // Generate the URL to be encoded
        HttpServletRequest request =
        (HttpServletRequest) pageContext.getRequest();
        
        StringBuffer results = new StringBuffer();
        
        OrderedLabelValueBean thisItem = null;
        thisItem = (OrderedLabelValueBean) pageContext.findAttribute(name);
        String thisValue = null;
        if (thisItem != null)
            thisValue = thisItem.getValue();
        if (thisValue==null || thisValue.length()<=0) {
            results.append("<a name=\"message\">");
        } else {
            
            StringBuffer url = new StringBuffer(request.getContextPath());
            url.append(actionClass);
            if (actionClass.indexOf("?")!=-1)
                url.append("&");
            else
                url.append("?");
            url.append(parameter);
            url.append("=");
            
            url.append(ResponseUtils.filter(thisValue));
            if (target!=null && !target.equals("")) {
                url.append("&target=");
                url.append(target);
            }
            
            // Generate the hyperlink start element
            HttpServletResponse response =
            (HttpServletResponse) pageContext.getResponse();
            
            results.append("<a href=\"");
            results.append(response.encodeURL(url.toString()));
            results.append("\">");
        }
        
        // Print this element to our output writer
        JspWriter writer = pageContext.getOut();
        try {
            writer.print(results.toString());
        } catch (IOException e) {
            throw new JspException("SelectBeanTag IO Error: " + e.getMessage());
        }
        
        // Evaluate the body of this tag
        return (EVAL_BODY_INCLUDE);
        
    }
    
    /**
     * Render the end of the hyperlink.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
        
        
        // Print the ending element to our output writer
        JspWriter writer = pageContext.getOut();
        try {
            writer.print("</a>");
        } catch (IOException e) {
            throw new JspException("SelectBeanTag IO Error: " + e.getMessage());
        }
        
        return (EVAL_PAGE);
        
    }
    
    
    /**
     * Release any acquired resources.
     */
    public void release() {
        
        super.release();
        this.name = "item";
        this.actionClass = "setItem.do";
        this.parameter = "id";
        this.target = "success";
    }
    
    
}
