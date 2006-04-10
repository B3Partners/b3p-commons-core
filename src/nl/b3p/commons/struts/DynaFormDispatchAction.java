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

	protected Method getMethod(String name)
			throws NoSuchMethodException {

		synchronized(methods) {
			Method method = (Method) methods.get(name);
			if (method == null) {
				try {
					/* Probeer met types uit deze class, dus met DynaValidatorForm */
					method = clazz.getMethod(name, types);
				} catch(NoSuchMethodException nsme) {
					throw nsme;
					// Voor fallback op methode met ActionFormParameter:
					/* Probeer met ActionForm */
					//method = clazz.getMethod(name, super.types);
				}
				methods.put(name, method);
			}
			return (method);
		}
	}

	protected ActionForward unspecified(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {

		try {
			Method method = getMethod("unspecified");
			Object args[] = {mapping, form, request, response};
			return (ActionForward)method.invoke(this, args);
		} catch(NoSuchMethodException nsme) {
			return super.unspecified(mapping, (DynaValidatorForm)form, request, response);
		}
	}

	protected ActionForward cancelled(ActionMapping mapping,
									  ActionForm form,
									  HttpServletRequest request,
									  HttpServletResponse response)
			throws Exception {
		try {
			Method method = getMethod("cancelled");
			Object args[] = {mapping, form, request, response};
			return (ActionForward)method.invoke(this, args);
		} catch(NoSuchMethodException nsme) {
			return super.unspecified(mapping, (DynaValidatorForm)form, request, response);
		}
	}
}
