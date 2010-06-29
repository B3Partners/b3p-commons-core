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
 * $Id: CrudAction.java 2964 2006-03-23 10:30:17Z Matthijs $
 */
package nl.b3p.commons.struts;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class CrudAction extends ExtendedMethodAction {

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

    protected Map getActionMethodPropertiesMap() {
        Map map = new HashMap();

        ExtendedMethodProperties crudProp = null;

        crudProp = new ExtendedMethodProperties(CONFIRM);
        map.put(CONFIRM, crudProp);

        crudProp = new ExtendedMethodProperties(DELETE_CONFIRM);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("warning.crud.delete");
        crudProp.setAlternateForwardName(SUCCESS);
        crudProp.setAlternateMessageKey("message.crud.delete");
        map.put(DELETE_CONFIRM, crudProp);

        crudProp = new ExtendedMethodProperties(SAVE_CONFIRM);
        crudProp.setDefaultForwardName(SUCCESS); // Na saveConfirm forward naar success
        crudProp.setDefaultMessageKey("warning.crud.save");
        crudProp.setAlternateForwardName(SUCCESS);
        crudProp.setAlternateMessageKey("message.crud.save");
        map.put(SAVE_CONFIRM, crudProp);

        crudProp = new ExtendedMethodProperties(DELETE);
        crudProp.setDefaultForwardName(LISTFW);
        crudProp.setDefaultMessageKey("warning.crud.deletedone");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("error.crud.deletefailed");
        map.put(DELETE, crudProp);

        crudProp = new ExtendedMethodProperties(CREATE);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setAlternateForwardName(LISTFW);
        map.put(CREATE, crudProp);

        crudProp = new ExtendedMethodProperties(SAVE);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("warning.crud.savedone");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("error.crud.savefailed");
        map.put(SAVE, crudProp);

        crudProp = new ExtendedMethodProperties(EDIT);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setAlternateForwardName(LISTFW);
        map.put(EDIT, crudProp);

        crudProp = new ExtendedMethodProperties(LIST);
        crudProp.setDefaultForwardName(LISTFW);
        crudProp.setAlternateForwardName(FAILURE);
        map.put(LIST, crudProp);

        return map;
    }

    protected void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
    }

    protected ActionForward getUnspecifiedDefaultForward(ActionMapping mapping, HttpServletRequest request) {
        return mapping.findForward(SUCCESS);
    }

    protected void prepareMethod(DynaValidatorForm dynaForm, HttpServletRequest request, String def, String alt) throws Exception {
        // nieuwe default actie zetten
        dynaForm.set(DEFAULT_FORWARDFIELD, def);
        dynaForm.set(ALTERNATE_FORWARDFIELD, alt);
        createLists(dynaForm, request);
    }

    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteConfirm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, DELETE, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }

    public ActionForward saveConfirm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, SAVE, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }

    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }

    public ActionForward list(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }

    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }

    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // nieuwe default actie op delete zetten
        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }

    public ActionForward create(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // nieuwe default actie op delete zetten
        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }

    public ActionForward confirm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return redispatchFormField(mapping, dynaForm, request, response, DEFAULT_FORWARDFIELD);
    }

    public ActionForward cancelled(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return redispatchFormField(mapping, dynaForm, request, response, ALTERNATE_FORWARDFIELD);
    }

    protected String getAction(DynaValidatorForm dynaForm) throws Exception {
        return dynaForm.getString(DEFAULT_FORWARDFIELD);
    }

    protected String getAltAction(DynaValidatorForm dynaForm) throws Exception {
        return dynaForm.getString(ALTERNATE_FORWARDFIELD);
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionForward forward = super.execute(mapping, form, request, response);
        /* Check van token in de implementatie van save en delete */

        /* indien notoken=true in request params dan niet saveToken(); voor
         * bijvoorbeeld een printpagina
         */
        if(!"true".equals(request.getParameter("notoken"))) {
            saveToken(request);
        }
        return forward;
    }
}