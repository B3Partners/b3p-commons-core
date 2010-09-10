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
 * $Id: ExtendedMethodAction.java 2964 2006-04-11 10:30:17Z Chris $
 */
package nl.b3p.commons.struts;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

public abstract class ExtendedMethodAction extends MethodPropertiesAction {

    protected Class getActionMethodPropertiesClass() {
        return ExtendedMethodProperties.class;
    }

    protected abstract ActionForward getUnspecifiedDefaultForward(ActionMapping mapping, HttpServletRequest request);

    protected ActionForward getUnspecifiedAlternateForward(ActionMapping mapping, HttpServletRequest request) {
        ActionForward af = mapping.getInputForward();
        if (af != null && af.getPath() != null) {
            return af;
        }
        return getUnspecifiedDefaultForward(mapping, request);
    }

    public ActionForward redispatchFormField(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response, String formfield) throws Exception {
        String methodParameter = null;
        if (formfield != null) {
            methodParameter = (String) dynaForm.get(formfield);
        }
        return redispatch(mapping, dynaForm, request, response, methodParameter);
    }

    public ActionForward redispatch(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response, String methodParameter) throws Exception {
        String methodName = setDispatchMethod(methodParameter, request);
        return dispatchMethod(mapping, dynaForm, request, response, methodName);
    }

    protected void addDefaultMessage(ActionMapping mapping, HttpServletRequest request) {
        addDefaultMessage(mapping, request, null);
    }

    protected void addDefaultMessage(ActionMapping mapping, HttpServletRequest request, String attribute) {
        String defaultMessagekey = null;

        ExtendedMethodProperties props = (ExtendedMethodProperties) getMethodProperties(request);
        if (props != null) {
            defaultMessagekey = props.getDefaultMessageKey();
        }

        if (defaultMessagekey != null) {
            if (attribute!=null) {
                addAttributeMessage(request, attribute, defaultMessagekey);
            } else {
                addMessage(request, defaultMessagekey);
            }
        }
    }

    protected void addAlternateMessage(ActionMapping mapping, HttpServletRequest request, String causeKey) {
        addAlternateMessage(mapping, request, causeKey, null);
    }

    protected void addAlternateMessage(ActionMapping mapping, HttpServletRequest request, String causeKey, String cause) {
        String alternateMessagekey = null;

        ExtendedMethodProperties props = (ExtendedMethodProperties) getMethodProperties(request);
        if (props != null) {
            alternateMessagekey = props.getAlternateMessageKey();
        }

        if (alternateMessagekey != null) {
            if (cause == null) {
                MessageResources messages = getResources(request);
                Locale locale = getLocale(request);
                cause = messages.getMessage(locale, causeKey);
            }
            addMessage(request, alternateMessagekey, cause);

        } else if (cause != null) {
            ActionMessage message = new ActionMessage(cause, false);
            addMessage(request, message);

        } else if (causeKey != null) {
            addMessage(request, causeKey);
        }
    }

    protected ActionForward getAlternateForward(ActionMapping mapping, HttpServletRequest request) {
        String alternateMessagekey = null;
        ActionForward alternateForward = null;

        ExtendedMethodProperties props = (ExtendedMethodProperties) getMethodProperties(request);
        if (props != null) {
            alternateForward = mapping.findForward(props.getAlternateForwardName());
        }
        if (alternateForward != null) {
            return alternateForward;
        }
        return getUnspecifiedAlternateForward(mapping, request);
    }

    protected ActionForward getDefaultForward(ActionMapping mapping, HttpServletRequest request) {
        ActionForward defaultForward = null;

        ExtendedMethodProperties props = (ExtendedMethodProperties) getMethodProperties(request);
        if (props != null) {
            defaultForward = mapping.findForward(props.getDefaultForwardName());
        }

        if (defaultForward != null) {
            return defaultForward;
        }
        return getUnspecifiedDefaultForward(mapping, request);
    }
}