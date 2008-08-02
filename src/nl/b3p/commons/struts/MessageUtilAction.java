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
        ActionMessages messages = (ActionMessages) request.getAttribute(attribute);
        if (messages == null) {
            messages = new ActionMessages();
            request.setAttribute(attribute, messages);
        }
        messages.add(ActionMessages.GLOBAL_MESSAGE, message);
    }
}
