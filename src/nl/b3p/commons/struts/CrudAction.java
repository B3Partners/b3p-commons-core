/*
 * $Id: CrudAction.java 2964 2006-03-23 10:30:17Z Matthijs $
 */

package nl.b3p.commons.struts;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

public class CrudAction extends MethodPropertiesAction {
    
    private static final Log log = LogFactory.getLog(CrudAction.class);
    
    protected static final String GENERAL_ERROR_KEY = "error.exception";
    protected static final String TOKEN_ERROR_KEY = "error.token";
    protected static final String NOTFOUND_ERROR_KEY = "error.notfound";
    protected static final String VALIDATION_ERROR_KEY = "error.validation";
    
    protected static final String SUCCESS = "success";
    protected static final String FAILURE = "failure";
    protected static final String LISTFW = "success";
    
    protected static final String DEFAULT_FORWARDFIELD = "action";
    protected static final String ALTERNATE_FORWARDFIELD = "alt_action";
    
    protected static final String CONFIRM = "confirm";
    protected static final String DELETE_CONFIRM = "deleteConfirm";
    protected static final String SAVE_CONFIRM = "saveConfirm";
    protected static final String DELETE = "delete";
    protected static final String CREATE = "create";
    protected static final String SAVE = "save";
    protected static final String EDIT = "edit";
    protected static final String LIST = "list";
    
    protected Class getActionMethodPropertiesClass() {
        return CrudActionProperties.class;
    }
    
    protected Map getActionMethodPropertiesMap() {
        Map map = new HashMap();
        
        CrudActionProperties crudProp = null;
        
        crudProp = new CrudActionProperties(CONFIRM);
        map.put(CONFIRM, crudProp);
        
        crudProp = new CrudActionProperties(DELETE_CONFIRM);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("warning.crud.delete");
        map.put(DELETE_CONFIRM, crudProp);
        
        crudProp = new CrudActionProperties(SAVE_CONFIRM);
        crudProp.setDefaultForwardName(SUCCESS); // Na saveConfirm forward naar success
        crudProp.setDefaultMessageKey("warning.crud.save");
        map.put(SAVE_CONFIRM, crudProp);
        
        crudProp = new CrudActionProperties(DELETE);
        crudProp.setDefaultForwardName(LISTFW);
        crudProp.setDefaultMessageKey("warning.crud.deletedone");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("error.crud.deletefailed");
        map.put(DELETE, crudProp);
        
        crudProp = new CrudActionProperties(CREATE);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setAlternateForwardName(LISTFW);
        map.put(CREATE, crudProp);
        
        crudProp = new CrudActionProperties(SAVE);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("warning.crud.savedone");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("error.crud.savefailed");
        map.put(SAVE, crudProp);
        
        crudProp = new CrudActionProperties(EDIT);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setAlternateForwardName(LISTFW);
        map.put(EDIT, crudProp);
        
        crudProp = new CrudActionProperties(LIST);
        crudProp.setDefaultForwardName(LISTFW);
        crudProp.setAlternateForwardName(FAILURE);
        map.put(LIST, crudProp);
        
        return map;
    }
    
    protected void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {}
    
    protected ActionForward getUnspecifiedDefaultForward(ActionMapping mapping, HttpServletRequest request) {
        return mapping.findForward(SUCCESS);
    }
    
    protected ActionForward getUnspecifiedAlternateForward(ActionMapping mapping, HttpServletRequest request) {
        ActionForward af = mapping.getInputForward();
        if (af!=null)
            return af;
        return getUnspecifiedDefaultForward(mapping, request);
    }
    
    protected void prepareMethod(ActionForm form, HttpServletRequest request, String def, String alt) throws Exception {
        // nieuwe default actie zetten
        DynaValidatorForm dynaForm = (DynaValidatorForm)form;
        dynaForm.set(DEFAULT_FORWARDFIELD, def);
        dynaForm.set(ALTERNATE_FORWARDFIELD, alt);
        createLists(dynaForm, request);
    }
    
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(form, request, EDIT, LIST);
        addDefaultMessage(mapping, request);
        return mapping.findForward(SUCCESS);
    }
    
    public ActionForward deleteConfirm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(form, request, DELETE, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }
    
    public ActionForward saveConfirm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(form, request, SAVE, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }
    
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(form, request, EDIT, LIST);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }
    
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(form, request, LIST, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }
    
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(form, request, LIST, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }
    
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // nieuwe default actie op delete zetten
        DynaValidatorForm dynaForm = (DynaValidatorForm)form;
        dynaForm.initialize(mapping);
        prepareMethod(form, request, LIST, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }
    
    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        // nieuwe default actie op delete zetten
        DynaValidatorForm dynaForm = (DynaValidatorForm)form;
        dynaForm.initialize(mapping);
        prepareMethod(form, request, EDIT, LIST);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }
    
    public ActionForward confirm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return redispatchFormField(mapping, form, request, response, DEFAULT_FORWARDFIELD);
    }
    
    public ActionForward cancelled(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return redispatchFormField(mapping, form, request, response, ALTERNATE_FORWARDFIELD);
    }
    
    public ActionForward redispatchFormField(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String formfield) throws Exception {
        String methodParameter = null;
        if (formfield!=null) {
            DynaValidatorForm dynaForm = (DynaValidatorForm)form;
            methodParameter = (String)dynaForm.get(formfield);
        }
        return redispatch(mapping, form, request, response, methodParameter);
    }
    
    public ActionForward redispatch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String methodParameter) throws Exception {
        String methodName = null;
        if(methodParameter != null) {
            methodName = (String)parameterMethodMap.get(methodParameter);
        }
        request.setAttribute(DISPATCHED_PARAMETER, methodParameter);
        request.setAttribute(DISPATCHED_METHOD_NAME, methodName);
        
        return dispatchMethod(mapping, form, request, response, methodName);
    }
    
    protected void addDefaultMessage(ActionMapping mapping, HttpServletRequest request) {
        String defaultMessagekey = null;
        
        CrudActionProperties props = (CrudActionProperties)getMethodProperties(request);
        if (props != null) {
            defaultMessagekey = props.getDefaultMessageKey();
        }
        
        if(defaultMessagekey != null)
            addMessage(request, defaultMessagekey);
    }
    
    protected void addAlternateMessage(ActionMapping mapping, HttpServletRequest request, String causeKey) {
        addAlternateMessage(mapping, request, causeKey, null);
    }
    
    protected void addAlternateMessage(ActionMapping mapping, HttpServletRequest request, String causeKey, String cause) {
        if (cause == null) {
            MessageResources messages = getResources(request);
            Locale locale = getLocale(request);
            cause = messages.getMessage(locale, causeKey);
        }
        
        String alternateMessagekey = null;
        
        CrudActionProperties props = (CrudActionProperties)getMethodProperties(request);
        if(props != null) {
            alternateMessagekey = props.getAlternateMessageKey();
        }
        
        if(alternateMessagekey != null)
            addMessage(request, alternateMessagekey, cause);
        else
            addMessage(request, GENERAL_ERROR_KEY, cause);
        
    }
    
    protected ActionForward getAlternateForward(ActionMapping mapping, HttpServletRequest request) {
        String alternateMessagekey = null;
        ActionForward alternateForward = null;
        
        CrudActionProperties props = (CrudActionProperties)getMethodProperties(request);
        if(props != null) {
            alternateForward = mapping.findForward(props.getAlternateForwardName());
        }
        if (alternateForward != null)
            return alternateForward;
        
        return getUnspecifiedAlternateForward(mapping, request);
    }
    
    protected ActionForward getDefaultForward(ActionMapping mapping, HttpServletRequest request) {
        ActionForward defaultForward = null;
        
        CrudActionProperties props = (CrudActionProperties)getMethodProperties(request);
        if (props != null) {
            defaultForward = mapping.findForward(props.getDefaultForwardName());
        }
        
        if (defaultForward != null)
            return defaultForward;
        
        return getUnspecifiedDefaultForward(mapping, request);
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        ActionForward forward = super.execute(mapping, form, request, response);
        /* Check van token in de implementatie van save en delete */
        saveToken(request);
        return forward;
    }
    
}