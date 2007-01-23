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
 * aangeroepen door een methode in het path deel van de url op te nemen;
 * bijvoorbeeld:
 * <p>
 * <code>
 * http://www.b3p.nl/servlet/method<br>
 * </code>
 */
public abstract class UrlPathDispatchAction extends DynaFormDispatchAction {
    
    private static final String DISPATCHED_PARAMETER = ParameterLookupDispatchAction.class.getName() + ".DISPATCHED_PARAMETER";
    private static final String DISPATCHED_METHOD_NAME = ParameterLookupDispatchAction.class.getName() + ".DISPATCHED_METHOD_NAME";
    
    /** Mapping van parameter naar methode naam
     */
    /* Initialiseer deze met lege HashMap zodat op de monitor ervan kan worden gesynchronized */
    protected Map parameterMethodMap = new HashMap();
    protected abstract Map getParameterMethodMap();
    
    /* String array met onderdelen van pathinfo deel van url */
    private String[] pathInfo = null;
    protected String[] getPathInfo() {
        return pathInfo;
    }
    /* index van methode in pathInfo array: 0-base*/
    protected int getMethodPathIndex() {
        return -1;
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        if(isCancelled(request)) {
            ActionForward af = cancelled(mapping, form, request, response);
            if(af != null) {
                return af;
            }
        }
        
        String methodParameter = getMethodParameter(mapping, form, request, response);
        
        /* dispatchMethod() zal unspecified() aanroepen indien methodName null is */
        String methodName = setDispatchMethod(methodParameter, request);
        
        ActionForward af = dispatchMethod(mapping, form, request, response, methodName);
        
        return af;
    }
    
    /**
     * Plaatst de parameter en de methode op de request, protected om subklassen
     * de gelegenheid te geven dit aan te passen.
     */
    protected String setDispatchMethod(String methodParameter, HttpServletRequest request) {
        String methodName = null;
        if(methodParameter != null) {
            methodName = (String)parameterMethodMap.get(methodParameter);
        }
        request.setAttribute(DISPATCHED_PARAMETER, methodParameter);
        request.setAttribute(DISPATCHED_METHOD_NAME, methodName);
        return methodName;
    }
    
    /**
     * Geeft de naam van de methode waarnaar is gedispatcht of null indien het
     * request cancelled is of de methode unspecified is.
     */
    protected String getDispatchedMethodName(HttpServletRequest request) {
        return (String)request.getAttribute(DISPATCHED_METHOD_NAME);
    }
    
    /**
     * Geeft de naam van de request parameter die gebruikt is om de methode
     * op te zoeken waarnaar te dispatchen of null indien cancelled of
     * unspecified.
     */
    protected String getDispatchedParameter(HttpServletRequest request) {
        return (String)request.getAttribute(DISPATCHED_PARAMETER);
    }
    
    protected String getMethodParameter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        /* Check met lock op parameterMethodMap indien deze leeg is en zo ja
         * initialiseer deze. Lezen kan zonder lock
         */
        synchronized(parameterMethodMap) {
            if(parameterMethodMap.isEmpty()) {
                Map realizedParameterMethodMap = getParameterMethodMap();
                if(realizedParameterMethodMap == null || realizedParameterMethodMap.isEmpty()) {
                    throw new IllegalStateException("empty parameterMethodMap");
                }
                parameterMethodMap.putAll(realizedParameterMethodMap);
            }
        }
        
        String pi = request.getPathInfo();
        pathInfo = pi==null?null:pi.split("/");
        
        int methodPathIndex = getMethodPathIndex();
        String methodParameter = null;
        if (pathInfo!=null && methodPathIndex>=0 && pathInfo.length>methodPathIndex)
            methodParameter = pathInfo[methodPathIndex];
        
        String methodName = null;
        if(methodParameter != null) {
            methodName = (String)parameterMethodMap.get(methodParameter);
        }
        if (methodName!=null)
            return methodParameter;
        
        return null;
    }
    
}