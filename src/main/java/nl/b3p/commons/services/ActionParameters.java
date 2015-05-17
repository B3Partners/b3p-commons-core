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

/*
 * $Id: ActionParameters.java 1651 2005-10-06 12:07:10Z Matthijs $
 */
package nl.b3p.commons.services;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
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
        return (DynaValidatorForm) form;
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
