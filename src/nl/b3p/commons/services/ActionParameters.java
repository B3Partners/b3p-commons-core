/*
 * $Id: ActionParameters.java 1651 2005-10-06 12:07:10Z Matthijs $
 */

package nl.b3p.commons.services;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;
import org.apache.struts.validator.*;
import org.apache.struts.util.MessageResources;

public class ActionParameters {

    private ActionMapping mapping;
    private ActionForm form;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Locale locale;
    private MessageResources messages;
    private ActionErrors errors;
    
    public ActionParameters(ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response,
        MessageResources messages,
        Locale locale) {
        
        this.mapping = mapping;
        this.form = form;
        this.request = request;
        this.response = response;
        this.messages = messages;
        this.locale = locale;
    }

    public ActionMapping getMapping() {
        return mapping;
    }

    public ActionForm getForm() {
        return form;
    }
    
    public DynaValidatorForm getDynaValidatorForm() {
        return (DynaValidatorForm)form;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Locale getLocale() {
        return locale;
    }

    public MessageResources getMessages() {
        return messages;
    }

    public ActionErrors getErrors() {
        return errors;
    }

    public void setErrors(ActionErrors errors) {
        this.errors = errors;
    }
}
