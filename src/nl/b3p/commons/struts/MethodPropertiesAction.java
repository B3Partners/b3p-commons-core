/*
 * $Id: MethodPropertiesAction.java 2855 2006-03-08 08:35:26Z Matthijs $
 */

package nl.b3p.commons.struts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public abstract class MethodPropertiesAction extends ParameterLookupDispatchAction {
	
	/* Initialiseer met lege HashMap zodat op de monitor gesynchronizeerd kan worden */
	private final Map actionMethodPropertiesMap = new HashMap();
	
	protected abstract Map getActionMethodPropertiesMap();
	
	protected Class getActionMethodPropertiesClass() {
		return ActionMethodProperties.class;
	}
	
	private void initActionMethodPropertiesMap() {
		synchronized(actionMethodPropertiesMap) {
			if(actionMethodPropertiesMap.isEmpty()) {
				Map realizedActionMethodPropertiesMap = getActionMethodPropertiesMap();
				if(realizedActionMethodPropertiesMap == null || realizedActionMethodPropertiesMap.isEmpty()) {
					throw new IllegalStateException("empty actionMethodPropertiesMap");
				}
				actionMethodPropertiesMap.putAll(realizedActionMethodPropertiesMap);
				Class requiredClass = getActionMethodPropertiesClass();
				for (Iterator it = actionMethodPropertiesMap.values().iterator(); it.hasNext();) {
					Object props = (Object)it.next();
					if(!props.getClass().equals(requiredClass)) {
						throw new IllegalArgumentException("required ActionMethodProperties class " + requiredClass.getName() + ", found " + props.getClass().getName());
					}
				}
			}
		}		
	}
	
	protected ActionMethodProperties getMethodProperties(HttpServletRequest request) {
		initActionMethodPropertiesMap();
		return (ActionMethodProperties)actionMethodPropertiesMap.get(getDispatchedParameter(request));
	}
	
	protected Map getParameterMethodMap() {
		initActionMethodPropertiesMap();
		Map parameterMethodMap = new HashMap();
		for (Iterator it = actionMethodPropertiesMap.keySet().iterator(); it.hasNext();) {
			String parameter = (String)it.next();
			ActionMethodProperties props = (ActionMethodProperties)actionMethodPropertiesMap.get(parameter);
			parameterMethodMap.put(parameter, props.getMethodName());			
		}
		return parameterMethodMap;
	}
	
	/* Dit zijn gewoon handige methodes voor Action in het algemeen, maar 
	 * multiple inheritance is niet mogelijk dus hier maar plaatsen...
	 */
	protected void addMessage(HttpServletRequest request, ActionMessage message) {
		ActionMessages messages = getMessages(request);
		messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		saveMessages(request, messages);		
	}
        
	protected void addMessage(HttpServletRequest request, String resourceKey) {
		addMessage(request, new ActionMessage(resourceKey));
	}

	protected void addMessage(HttpServletRequest request, String resourceKey, Object arg0) {
		addMessage(request, new ActionMessage(resourceKey, arg0));
	}

	protected void addMessage(HttpServletRequest request, String resourceKey, Object arg0, Object arg1) {
		addMessage(request, new ActionMessage(resourceKey, arg0, arg1));
	}

	protected void addMessage(HttpServletRequest request, String resourceKey, Object[] args) {
		addMessage(request, new ActionMessage(resourceKey, args));
	}
}
