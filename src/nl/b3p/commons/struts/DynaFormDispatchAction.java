/*
 * $Id$
 */

package nl.b3p.commons.struts;

import java.lang.reflect.Method;
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
		synchronized(methods) {
			Method method = (Method) methods.get(name);
			if(method == null) {
				try {
					/* Probeer met types uit deze class, dus met DynaValidatorForm */
					method = clazz.getMethod(name, types);
				} catch(NoSuchMethodException nsme) {
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
		try {
			Method method = getMethod("unspecified");
			Object args[] = {mapping, form, request, response};
			return (ActionForward)method.invoke(this, args);
		} catch(NoSuchMethodException nsme) {
			/* Indien geen unspecified met DynaValidatorForm parameter roep super
			 * implementatie aan welke een ServletException throwt
			 */
			return super.unspecified(mapping, form, request, response);
		}
	}

	protected ActionForward cancelled(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* Idem voor cancelled() als voor unspecified() */
		try {
			Method method = getMethod("cancelled");
			Object args[] = {mapping, form, request, response};
			return (ActionForward)method.invoke(this, args);
		} catch(NoSuchMethodException nsme) {
			/* super implementatie aanroepen, returnt null */
			return super.cancelled(mapping, form, request, response);
		}
	}
}
