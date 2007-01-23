/*
 * $Id: ParameterLookupDispatchAction.java 2993 2006-03-27 06:21:31Z Chris $
 */

package nl.b3p.commons.struts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 * Deze abstracte <b>DispatchAction</b> dispatcht naar een publieke methode
 * welke genoemd is in een Map van parameter naar methode naam. Indien het
 * request een parameter uit de Map met niet-lege waarde bevat wordt de
 * bijbehorende methode aangeroepen. Indien er geen methode kan worden gevonden
 * wordt <code>unspecified()</code> aangeroepen.
 * <p>
 * Indien het request was gecancelled (door het indrukken van een
 * <code>html:cancel</code> knop) wordt <code>cancelled()</code> aangeroepen.
 * <p>
 * Met deze action kunnen verschillende methodes van een Action class worden
 * aangeroepen door meerdere submit buttons op &eacute;&eacute;n form;
 * bijvoorbeeld:
 * <p>
 * <code>
 * &lt;html:submit property="save"&gt;Opslaan&lt;/html:submit&gt;<br>
 * &lt;html:submit property="delete" onclick="bCancel = true;"&gt;Verwijderen&lt;/html:submit&gt;<br>
 * </code>
 * <p>
 * Of doormiddel van een submit met JavaScript, bijvoorbeeld:
 * <code>
 * <pre>
 * &lt;html:hidden property="filterChange"/&gt;
 * &lt;script language="JavaScript1.1"&gt;
 * &lt;!--
 *     function onFilterChange(event) {
 *         bCancel = true; // geen validation op deze functie
 *         document.forms[0].filterChange.value = "submit";
 *         document.forms[0].submit();
 *     }
 * // --&gt;
 * &lt;/script&gt;
 * &lt;html:select property="filter" onchange="onFilterChange();"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;...
 * &lt;/html:select&gt;
 * </pre>
 * </code>
 * <b>LET OP:</b> Ten behoeve van het kunnen submitten van een form met
 * JavaScript met een hidden property maakt deze Action na het dispatchen
 * indien er een String property in het DynaActionForm aanwezig is met de naam
 * van parameter van de uitgevoerde methode deze leeg.
 * <p>
 * Bij deze voorbeelden hoort de volgende subclass:
 * <p>
 * <code>
 * <pre>
 * public class ExampleAction extends ParameterLookupDispatchAction {
 *     protected Map getParameterMethodMap() {
 *         Map map = new HashMap();
 *         map.put("save", "mySaveMethod");
 *         map.put("delete", myDeleteMethod");
 *         map.put("filterChange", "myFilterChangeMethod");
 *         return map;
 *     }
 *
 *     public ActionForward mySaveMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
 *     throws Exception {
 *         ...
 *     }
 *
 *     <i>idem voor myDeleteMethod en myFilterChangeMethod</i>
 * }
 * </pre>
 * </code>
 */
public abstract class ParameterLookupDispatchAction extends UrlPathDispatchAction {
    
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        ActionForward af = super.execute(mapping, form, request, response);
        
        /* Indien het form een DynaActionForm is en het form heeft een String
         * property met de naam van de parameter die is gesubmit, zet deze
         * property dan naar een lege String. Dit voor het kunnen submitten dmv
         * JavaScript met een hidden property. Indien je de property niet
         * leegmaakt wordt de methode voor de JavaScript submit bij een volgende
         * submit met een andere knop mogelijk weer uitgevoerd in plaats van de
         * juiste knop.
         */
        String methodParameter = getDispatchedParameter(request);
        if(methodParameter != null && form instanceof DynaActionForm) {
            DynaActionForm dynaForm = (DynaActionForm)form;
            Object formPropertyValue = dynaForm.getMap().get(methodParameter);
            if(formPropertyValue != null && formPropertyValue instanceof String && ((String)formPropertyValue).length() > 0) {
                dynaForm.set(methodParameter, "");
            }
        }
        
        return af;
    }
    
    
    protected String getMethodParameter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        /* eerst kijken of hiervoor al een methode in de url of elders gevonden is
         * zo niet dan in de request parameters kijken
         */
        String parameter = super.getMethodParameter(mapping, form, request, response);
        
        if (parameter==null) {
            /* Kijk of er niet-lege parameters zijn die in de parameterMethodMap
             * keyset voorkomen. Dit is het geval indien een submit knop is
             * ingedrukt (of door JavaScript een form is gesubmit).
             */
            
            Set keys = parameterMethodMap.keySet();
            for(Iterator i = keys.iterator(); i.hasNext();) {
                String key = (String)i.next();
                String keyParam = request.getParameter(key);
                if(keyParam != null && keyParam.length() > 0) {
                    parameter = key;
                    break;
                }
            }
        }
        
        return parameter;
    }
}