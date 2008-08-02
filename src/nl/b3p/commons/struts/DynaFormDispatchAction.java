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
 * $Id$
 */
package nl.b3p.commons.struts;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

public class DynaFormDispatchAction extends DispatchAction {

    protected Class[] types = {
        ActionMapping.class,
        DynaValidatorForm.class,
        HttpServletRequest.class,
        HttpServletResponse.class
    };

    protected Method getMethod(String name) throws NoSuchMethodException {
        synchronized (methods) {
            Method method = (Method) methods.get(name);
            if (method == null) {
                try {
                    /* Probeer met types uit deze class, dus met DynaValidatorForm */
                    method = clazz.getMethod(name, types);
                } catch (NoSuchMethodException nsme) {
                    throw nsme;
                // Voor fallback op methode met ActionForm parameter:
					/* Probeer met ActionForm */
                //method = clazz.getMethod(name, super.types);
                }
                methods.put(name, method);
            }
            return method;
        }
    }

    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /* Deze methode kan in een subclass worden overridden, dus hier komen we
         * alleen indien deze methode niet overridden is. Probeer een unspecified()
         * methode met als tweede parameter een DynaValidatorForm aan te roepen:
         */
        Method method;
        try {
            method = getMethod("unspecified");
        } catch (NoSuchMethodException nsme) {
            /* Indien geen unspecified met DynaValidatorForm parameter roep super
             * implementatie aan welke een ServletException throwt
             */
            return super.unspecified(mapping, form, request, response);
        }
        try {
            Object args[] = {mapping, form, request, response};
            return (ActionForward) method.invoke(this, args);
        } catch (InvocationTargetException e) {
            /* Zelfde logica als in DispatchAction.dispatchMethod() */

            // Rethrow the target exception if possible so that the
            // exception handling machinery can deal with it
            Throwable t = e.getTargetException();
            if (t instanceof Exception) {
                throw ((Exception) t);
            } else {
                throw new ServletException(t);
            }
        }
    }

    protected ActionForward cancelled(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /* Idem voor cancelled() als voor unspecified() */
        Method method;
        try {
            method = getMethod("cancelled");
        } catch (NoSuchMethodException nsme) {
            /* super implementatie aanroepen, returnt null */
            return super.cancelled(mapping, form, request, response);
        }
        try {
            Object args[] = {mapping, form, request, response};
            return (ActionForward) method.invoke(this, args);
        } catch (InvocationTargetException e) {
            /* Zelfde logica als in DispatchAction.dispatchMethod() */

            // Rethrow the target exception if possible so that the
            // exception handling machinery can deal with it
            Throwable t = e.getTargetException();
            if (t instanceof Exception) {
                throw ((Exception) t);
            } else {
                throw new ServletException(t);
            }
        }
    }
}
