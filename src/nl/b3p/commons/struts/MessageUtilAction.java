/*
 * $Id$
 */

package nl.b3p.commons.struts;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class MessageUtilAction extends Action {
	
	/* Dit zijn gewoon handige methodes voor Action in het algemeen, maar 
	 * multiple inheritance is niet mogelijk dus hier maar plaatsen...
	 *
	 * Staan ook in MethodPropertiesAction
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
    
    protected void addAttributeMessage(HttpServletRequest request, String attribute, ActionMessage message) {
        ActionMessages messages = (ActionMessages)request.getAttribute(attribute);
        if(messages == null) {
            messages = new ActionMessages();
            request.setAttribute(attribute, messages);
        }
        messages.add(ActionMessages.GLOBAL_MESSAGE, message);
    }
}
