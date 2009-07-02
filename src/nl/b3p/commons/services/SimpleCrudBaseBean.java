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
package nl.b3p.commons.services;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Deze class maakt het mogelijk om een records in een database hoofdtabel te bewerken,
 * wissen of nieuw aan te maken.
 * <p>
 * <p>
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.0 $ $Date: 2005/05/17 12:48:31 $
 */
public abstract class SimpleCrudBaseBean extends FormBaseBean {

    protected Log log = LogFactory.getLog(this.getClass());
    private String newAction = null;
    protected Object theObject = null;
    public static final String INVALID_ACTION = "INVALID";
    public static final String CANCELLED_ACTION = "CANCELLED";
    public static final String START_ACTION = "Start";
    public static final String EDIT_ACTION = "Edit";
    public static final String NEW_ACTION = "New";
    public static final String SAVE_ACTION = "Save";
    public static final String DELETE_ACTION = "Delete";    // Algemene knoppen
    public static final String OK_BUTTON = "ok";
    public static final String CANCEL_BUTTON = "cancel";
    public static final String DELETE_BUTTON = "delete";
    public static final String NEW_BUTTON = "new";
    public static final String SAVE_BUTTON = "save";
    public static final String EDIT_BUTTON = "edit";    // Dit zijn de waarden voor tag van de foutmeldingen in de jsp
    public static final String MAIN_MESSAGE = "MAIN_MESSAGE";    // Default id voor lege subrecords
    protected static int TEMPNEW_ID = -1;    // Configuratie parameters
    private boolean directSave = false;
    private boolean directDelete = false;
    private boolean allowEdits = true; // inclusief delete

    /**
     * Deze minimale constructor kent geen locale instelling, geen
     * message resources en geen foutterugmelding en zal niet vaak
     * gebruikt worden.
     * <p>
     * @param req de request waarmee het struts formulier gepost is
     * @param dform het struts formulier zelf
     * @param mapp de mapping voor deze struts action
     */
    public SimpleCrudBaseBean(HttpServletRequest req,
            DynaValidatorForm dform,
            ActionMapping mapp) {
        this(req, null, null, null, dform, mapp);
    }

    /**
     * De constructor bepaalt uit de request de parameters welke gepost zijn en slaat deze
     * lokaal op. Hiernaast wordt het struts formulier zelf lokaal opgeslagen.
     * <p>
     * Indien locale, message resources en foutterugmelding null zijn, wordt een default
     * waarde hiervoor  vastgesteld, zodat de class verder kan functioneren.
     * <p>
     * @param req de request waarmee het struts formulier gepost is
     * @param loc de locale zoals door struts bepaald
     * @param mess de message resources waaruit de foutmelding worden gehaald
     * @param err de reeks van foutmeldingen, zoals deze tijdens de uitvoer van de functies wordt opgebouwd
     * @param dform het struts formulier zelf
     * @param mapp de mapping voor deze struts action
     */
    public SimpleCrudBaseBean(HttpServletRequest req,
            Locale loc,
            MessageResources mess,
            ActionMessages err,
            DynaValidatorForm dform,
            ActionMapping mapp) {

        super(req, loc, mess, err, dform, mapp);
    }

    /**
     * Deze functie procest een request, welke is opgeslagen bij de constructie van de bean.
     * <p>
     * Afhankelijk van de waarde van <i>action</i> en de waarden in de request wordt een
     * record in de tabel bewerkt, gewist of nieuw aangemaakt.
     * <p>
     * In deze functie worden een aantal functies aangeroepen, die weer abstracte functies
     * aanroepen die bij een concrete implementatie moeten worden gedefinieerd.
     * <p>
     * @return een struts actionforward, zoals door deze functie vastgesteld.
     * @param tokenValid wordt false indien dit formulier een tweede keer wordt gepost
     * @param transactionCancelled wordt true indien de cancel knop is geklikt
     * @param validateErrors bevat alle validatie errors, zoals door struts vastgesteld
     */
    public ActionForward processAction(boolean tokenValid, boolean transactionCancelled, ActionErrors validateErrors) {

        if (!isInit) {
            errors.add(MAIN_MESSAGE, new ActionMessage("error.invoerenrecord.general"));
            return mapping.findForward("failure");
        }

        ActionErrors verrs = determineAction(tokenValid, transactionCancelled, validateErrors);

        determineObjects();

        ActionForward f = null;
        f = doCreateAction();
        if (f != null) {
            return f;
        }
        f = doDeleteAction();
        if (f != null) {
            return f;
        }
        f = doSaveAction(verrs);
        // Alternatief: f = doSaveAllAction(verrs);
        if (f != null) {
            return f;
        }
        f = doEditAction(verrs);
        if (f != null) {
            return f;
        }
        f = populateMainForm();
        if (f != null) {
            return f;
        }
        determineNewAction();

        return null;
    }

    public ActionForward processLists() {
        // Aanmaken van lijsten en plaatsen op de sessie
        try {
            createLists();
        } catch (B3pCommonsException be) {
            errors.add(MAIN_MESSAGE, new ActionMessage("error.invoerenrecord.general"));
            return mapping.findForward("failure");
        }

        return null;
    }

    /**
     * Deze functie bepaalt de waarde van <I>action</I>. Indien er nog geen <I>action</I> gedefinieerd
     * is krijgt deze de waarde START_ACTION. Deze functie weet dan dat er niet op token gecontroleerd
     * moet worden. Als de cancel knop is geklikt dan wordt de <I>action</I> veranderd in CANCELLED_ACTION.
     * De waarde voor <I>newAction</I>, de <I>action</I> voor de volgende ronde, komt default op EDIT_ACTION; dit
     * kan later nog worden aangepast.
     * <p>
     * @param validateErrors bekende foutmeldingen
     * @param tokenValid false indien het formulier 2x gepost wordt
     * @param transactionCancelled true als de cancel knop geklikt is
     * @return foutenmeldingen van formulier
     */
    protected ActionErrors determineAction(boolean tokenValid, boolean transactionCancelled, ActionErrors validateErrors) {
        try {
            action = getParamAsString("action");
        } catch (B3pCommonsException be) {
            if (log.isErrorEnabled()) {
                log.error(be);
            }
        }

        newAction = action;
        if (nullOrEmpty(action)) {
            action = START_ACTION;
            newAction = EDIT_ACTION;
        }

        // De eerste keer is er nog geen token
        if (!isAction(START_ACTION)) {
            // Validate the transactional control token
            if (!tokenValid) {
                errors.add(MAIN_MESSAGE, new ActionMessage("error.transaction.token"));
                // Report any errors we have discovered back to the original form
                action = INVALID_ACTION;
                newAction = EDIT_ACTION;
            }
        } else {
            // Bij start nog niets doen met validatie
            validateErrors = null;
            action = EDIT_ACTION;
            newAction = EDIT_ACTION;
        }

        // Was this transaction cancelled?
        if (transactionCancelled) {
            if (log.isDebugEnabled()) {
                log.debug(" Transaction '" + action + "' was cancelled");
            }
            action = CANCELLED_ACTION;
            newAction = EDIT_ACTION;
        }
        return validateErrors;
    }

    /**
     * Deze functie haalt het hoofdobject op en de subobjecten worden in een array
     * bewaard voor gebruik later.
     */
    protected void determineObjects() {
        // Haal actieve object op
        theObject = null;
        try {
            theObject = getMainObject();
        } catch (B3pCommonsException te) {
        }
        if (theObject == null) {
            return;
        }
    }

    /**
     * Deze functie checkt of de NEW_ACTION moet worden uitgevoerd. Afhankelijk
     * van het feit of het record nieuw of bestaand is krijgt de gebruiker een melding.
     * Het daadwerkelijke opslaan volgt in de volgende ronde, indien de PROCESS_ACTION
     * wordt uitgevoerd.
     * <p>
     * @return altijd null
     */
    protected ActionForward doCreateAction() {
        // Bij create mode wordt process omgeleid met nieuwe action
        if (isAction(NEW_ACTION) && buttonPressed(OK_BUTTON)) {
            if (allowEdits) {
                if (theObject != null) {
                    if (log.isDebugEnabled()) {
                        log.debug(" process this, save existing");
                    }
                    errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.save"));
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug(" process this, create new");
                    }
                    errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.new"));
                }
                newAction = SAVE_ACTION;
            } else {
                errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.notallowed"));
                newAction = EDIT_ACTION;
            }
        }
        if (isAction(NEW_ACTION) && buttonPressed(CANCEL_BUTTON)) {
            if (log.isInfoEnabled()) {
                log.info(" Cancel new action");
            }
            newAction = EDIT_ACTION;
        }
        return null;
    }

    /**
     * Deze functie checkt of de DELETE_ACTION moet worden uitgevoerd. Het hoofdrecord met alle
     * bijbehorende subrecords wordt dan gewist. In de vorige ronde is een waarschuwing gegeven!
     * <p>
     * @return niet null bij fouten
     */
    protected ActionForward doDeleteAction() {
        // Was this transaction a Delete?
        if (isAction(DELETE_ACTION) && buttonPressed(OK_BUTTON) && theObject != null) {
            if (allowEdits) {
                return deleteAction();
            } else {
                errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.notallowed"));
                newAction = EDIT_ACTION;
            }
        }
        if (isAction(DELETE_ACTION) && buttonPressed(CANCEL_BUTTON)) {
            if (log.isInfoEnabled()) {
                log.info(" Cancel delete action");
            }
            newAction = EDIT_ACTION;
        }
        return null;
    }

    /**
     * Het hoofdrecord met alle bijbehorende subrecords wordt gewist.
     * In de vorige ronde is een waarschuwing gegeven!
     * <p>
     * @return niet null bij fouten
     */
    protected ActionForward deleteAction() {
        if (log.isInfoEnabled()) {
            log.info(" Deleting Record '" + theObject.toString() + "'");
        }
        try {
            deleteObject();
            theObject = null;
            setID(null);
        } catch (B3pCommonsException dbe) {
            if (log.isErrorEnabled()) {
                log.error("  Database error deleting: ", dbe);
            }
            errors.add(MAIN_MESSAGE, new ActionMessage("error.database", dbe.getMessage()));
            return (mapping.findForward("failure"));
        }
        if (log.isDebugEnabled()) {
            log.debug(" Reset DynaForm bean under key " + mapping.getAttribute());
        }
        form.initialize(mapping);
        newAction = EDIT_ACTION;

        if (directDelete) {
            errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.deletedone"));
        }
        return null;
    }

    /**
     * Deze functie checkt of de SAVE_ACTION moet worden uitgevoerd.
     * <p>
     * @param validateErrors zoals deze door struts zijn vastgesteld
     * @return niet null bij validatie errors
     */
    protected ActionForward doSaveAction(ActionErrors validateErrors) {
        if (isAction(SAVE_ACTION) && buttonPressed(OK_BUTTON)) {
            if (allowEdits) {
                return saveAction(validateErrors);
            } else {
                errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.notallowed"));
                newAction = EDIT_ACTION;
            }
        }
        if (isAction(SAVE_ACTION) && buttonPressed(CANCEL_BUTTON)) {
            if (log.isInfoEnabled()) {
                log.info(" Cancel save action");
            }
            newAction = EDIT_ACTION;
        }
        return null;
    }

    /**
     * Deze functie voert de SAVE_ACTION uit. Er wordt
     * gecontroleerd of struts fouten in de invoer van het formulier heeft geconstateerd.
     * Als dit zo is wordt verder verwerking afgebroken en gaat men meteen terug naar het
     * formulier. Als er geen fouten zijn dan wordt de database gevuld met de informatie
     * in het formulier.
     * <p>
     * @param validateErrors zoals deze door struts zijn vastgesteld
     * @return niet null bij validatie errors
     */
    protected ActionForward saveAction(ActionErrors validateErrors) {
        // validatie
        if (reduceMainErrors(validateErrors)) {
            if (log.isInfoEnabled()) {
                log.info("Validation error!");
            }
            errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.savewitherrors"));
            newAction = SAVE_ACTION;
        } else {
            try {
                // Het hoofdobject wordt alleen aangemaakt, indien
                // in het form een merker is aangebracht die dit aangeeft.
                if (theObject == null && Integer.toString(TEMPNEW_ID).equals(getMainID())) {
                    theObject = getNewObject();
                }
                if (theObject != null) {
                    populateObject();
                    syncID();
                    if (log.isDebugEnabled()) {
                        log.debug(" Populating database object from form bean");
                    }
                }
            } catch (Exception dbe) {
                if (log.isErrorEnabled()) {
                    log.error("  Database error creating object: ", dbe);
                }
                errors.add(MAIN_MESSAGE, new ActionMessage("error.database", dbe.getMessage()));
                return (mapping.findForward("failure"));
            }
            newAction = EDIT_ACTION;
            if (directSave) {
                errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.savedone"));
            }
        }
        return null;
    }

    /**
     * Deze functie checkt of de EDIT_ACTION moet worden uitgevoerd. Dit is de hoofdactie
     * van het formulier. Meestal wordt vanuit deze actie een nieuwe actie gestart, de gebruiker
     * krijgt dan bij de volgende ronde een melding en de gewenste actie kan dan worden afgemaakt.
     * <p>
     * Vanuit de EDIT_ACTION kan het hoofdrecord in de volgende ronde bewerkt, gewist of nieuw gemaakt
     * worden. Ook kan een many-to-many koppeling worden gemaakt of verbroken.
     * <p>
     * @return altijd null
     * @param validateErrors foutenmeldingen van formulier
     */
    protected ActionForward doEditAction(ActionErrors validateErrors) {

        // Bij edit mode worden new en delete omgeleid met nieuwe action
        if (isAction(EDIT_ACTION)) {
            // wis scherm om nieuw record te kunnen creeren
            if (buttonPressed(NEW_BUTTON)) {
                if (allowEdits) {
                    if (log.isDebugEnabled()) {
                        log.debug(" Reset DynaForm bean under key " + mapping.getAttribute());
                    }
                    form.initialize(mapping);
                    try {
                        theObject = getNewObject();
                        // merker dat record nieuw is, wordt later op gecheckt
                        setID(Integer.toString(TEMPNEW_ID));
                    } catch (B3pCommonsException me) {
                    }
                    if (log.isDebugEnabled()) {
                        log.debug(" create new");
//                errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.new"));
                    }
                } else {
                    errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.notallowed"));
                }
                newAction = EDIT_ACTION;
            }
            // bewaar dit record
            if (buttonPressed(SAVE_BUTTON)) {
                if (allowEdits) {
                    if (directSave) {
                        ActionForward p = saveAction(validateErrors);
                        if (p != null) {
                            return p;
                        }
                    } else {
                        if (reduceMainErrors(validateErrors)) {
                            if (log.isInfoEnabled()) {
                                log.info("Validation error!");
                            }
                            errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.savewitherrors"));
                        } else {
                            if (theObject != null) {
                                if (log.isDebugEnabled()) {
                                    log.debug(" save existing");
                                }
                                errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.save"));
                            } else {
                                if (log.isDebugEnabled()) {
                                    log.debug(" create new");
                                }
                                errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.new"));
                            }
                        }
                        newAction = SAVE_ACTION;
                    }
                } else {
                    errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.notallowed"));
                }
            }
            // delete dit record
            if (buttonPressed(DELETE_BUTTON)) {
                if (allowEdits) {
                    if (directDelete) {
                        ActionForward p = deleteAction();
                        if (p != null) {
                            return p;
                        }
                    } else {
                        if (theObject != null) {
                            if (log.isDebugEnabled()) {
                                log.debug(" delete");
                            }
                            errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.delete"));
                            newAction = DELETE_ACTION;
                        } else {
                            if (log.isDebugEnabled()) {
                                log.debug(" delete, but no record");
                            }
                            errors.add(MAIN_MESSAGE, new ActionMessage("error.invoerenrecord.nodelete"));
                        }
                    }
                } else {
                    errors.add(MAIN_MESSAGE, new ActionMessage("warning.invoerenrecord.notallowed"));
                }
            }
            if (buttonPressed(CANCEL_BUTTON)) {
                if (log.isInfoEnabled()) {
                    log.info(" Cancel edit action????");
                }
                newAction = EDIT_ACTION;
            }
        }
        return null;
    }

    /**
     * Deze functie vult het hoofdformulier met
     * de bijgewerkte informatie uit de database.
     * <p>
     * @return niet null bij fouten
     */
    protected ActionForward populateMainForm() {
        if (theObject == null) {
            return null;
        }
        try {
            // Bij save action moet het form niet opnieuw gevuld worden

            if (!isNewAction(SAVE_ACTION)) {
                populateForm();
                if (log.isDebugEnabled()) {
                    log.debug(" Populating form from " + theObject.toString());
                }
            }
        } catch (B3pCommonsException pe) {
            errors.add(MAIN_MESSAGE, new ActionMessage("error.invoerenrecord.general"));
            return mapping.findForward("failure");
        }
        return null;
    }

    /**
     * Deze functie verwijdert de niet relevante errors uit de lijst door
     * te kijken naar de velden (properties) die in het form aanwezig zijn.
     * <p>
     * @return true indien relevante fouten
     * @param ve
     */
    protected boolean reduceMainErrors(ActionErrors ve) {
        if (ve == null || ve.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Deze functie vult de nieuwe actie in op het struts form, zodat
     * de volgende ronde hiermee gewerkt wordt.
     * <p>
     * @return niet null bij fouten
     */
    protected void determineNewAction() {
        try {
            setForm("action", newAction);
        } catch (B3pCommonsException pe) {
            errors.add(MAIN_MESSAGE, new ActionMessage("error.invoerenrecord.general"));
        }
        return;
    }

    /**
     * Tijdens de uitvoer van de functies zal een nieuwe actie berekend worden voor de volgende post.
     * Deze nieuwe actie wordt hieropgeslagen en juist voor het beeindigen van de process-functie
     * wordt het <i>action</i>-veld op deze nieuwe waarde gezet, zodat het formulier de volgende keer
     * deze actie zal hebben.
     * <p>
     * @return nieuwe actie voor de ronde
     */
    protected String getNewAction() {
        return newAction;
    }

    /**
     * setter voor newAction
     * @param newAction zet een nieuwe actie voor de volgende ronde
     */
    protected void setNewAction(String newAction) {
        this.newAction = newAction;
    }

    /**
     * test of een actie de huidige actie voor de volgende ronde is
     * @param lact te testen actie
     * @return true indien huidige actie voor de volgende ronde gelijk is aan de te testen actie
     */
    protected boolean isNewAction(String lact) {
        if (lact == null) {
            return false;
        }
        return lact.equals(getNewAction());
    }

    /**
     * Een concrete implementatie van deze functie haalt het id op van de hoofdtabel
     * uit het struts formulier dat getoond wordt, bijvoorbeeld via:
     * <CODE>return getForm("id");</CODE>
     * <p>
     * @return het id of null, indien onbekend
     * @throws B3pCommonsException bij fouten
     */
    protected String getMainID() throws B3pCommonsException {
        throw new B3pCommonsException("not implemented!");
    }

    /**
     * Een concrete implementatie van deze functie haalt het eerst het id op van de hoofdrecord
     * uit het struts formulier dat getoond wordt, bijvoorbeeld via:
     * <CODE>int id = getMainObjectID();</CODE>
     * Vervolgens wordt het bijbehorende object (persistence laag class) welke een record uit de tabel
     * beschrijft opgehaald, bijvoorbeeld via:
     * <CODE>return hsession.createQuery("from tabel where id = id");</CODE>
     * <p>
     * @return het tabelobject
     * @throws B3pCommonsException bij fouten
     */
    protected Object getMainObject() throws B3pCommonsException {
        throw new B3pCommonsException("not implemented!");
    }

    /**
     * Een concrete implementatie van deze functie zorgt ervoor dat een nieuw persistent
     * object wordt aangemaakt voor de hoofdtabel.
     * <p>
     * @throws B3pCommonsException bij fouten
     * @return het nieuwe persistent object
     */
    protected Object getNewObject() throws B3pCommonsException {
        throw new B3pCommonsException("not implemented!");
    }

    /**
     * Een concrete implementatie van deze functie plaatst een nieuw id in het id veld.
     * Indien subid null is, wordt het veld leeg gemaakt.
     * <p>
     * @param id waarde van nieuw id
     * @throws B3pCommonsException bij fouten
     */
    protected void setID(String id) throws B3pCommonsException {
        throw new B3pCommonsException("not implemented!");
    }

    /**
     * Een concrete implementatie van deze functie synchroniseert het id van het hoofdobject
     * met het id in het strutsformulier.
     * <p>
     * De code kan er als volgt uitzien:
     * <CODE>
     * if (theObject!=null) {
     *  int id = ((Cast)theObject).getId();
     *  setID(id);
     * } else {
     *  setID(null);
     * }
     * </CODE>
     * <p>
     * @throws B3pCommonsException bij fouten
     */
    protected void syncID() throws B3pCommonsException {
        throw new B3pCommonsException("not implemented!");
    }

    /**
     * Een concrete implementatie van deze functie moet het hoofdobject verwijderen
     * uit de database, alle afhankelijke subrecords dienen vooraf al verwijderd te
     * of the database moet <i>on delete cascade</i> in gesteld hebben.
     * <p>
     * @throws B3pCommonsException bij fouten
     */
    protected void deleteObject() throws B3pCommonsException {
        throw new B3pCommonsException("not implemented!");
    }

    /**
     * Een concrete implementatie van deze functie vult het hoofdobject vanuit het form.
     * <p>
     * @throws B3pCommonsException bij fouten
     */
    protected void populateObject() throws B3pCommonsException {
        throw new B3pCommonsException("not implemented!");
    }

    /**
     * Een concrete implementatie van deze functie zorgt voor het vullen van het form uit het hoofdobject.
     * <p>
     * @throws B3pCommonsException bij fouten
     */
    protected void populateForm() throws B3pCommonsException {
        throw new B3pCommonsException("not implemented!");
    }

    /**
     * Elk struts formulier maakt gebruik van een aantal lijsten. Ieder dropdown zal via een lijst
     * gevuld worden. Bij gebruik van subtabellen zal voor iedere subtabel een lijst gemaakt moeten
     * worden waarin alle subrecords staan die bij het huidige hoofdrecord horen. Deze lijsten worden
     * op de sessie gezet en door het formulier later gebruikt.
     * <p>
     * De namen van de lijsten moeten afgestemd worden met het gebruik in de jsp's waarin het struts
     * formulier wordt gebruikt.
     * <p>
     * @throws nl.b3p.commons.services.B3pCommonsException bij fout ophalen lijsten
     */
    protected void createLists() throws B3pCommonsException {
        return;
    }

    /**
     * Configuratie parameter van EditBaseBean welke bepaalt of een save
     * meteen (true) moet worden uitgevoerd of pas na een waarschuwing (false).
     * @return meteen bewaren of niet
     */
    public boolean isDirectSave() {
        return directSave;
    }

    /**
     * Configuratie parameter van EditBaseBean welke bepaalt of een save
     * meteen (true) moet worden uitgevoerd of pas na een waarschuwing (false).
     * @param directSave meteen bewaren of niet
     */
    public void setDirectSave(boolean directSave) {
        this.directSave = directSave;
    }

    /**
     * Configuratie parameter van EditBaseBean welke bepaalt of een delete
     * meteen (true) moet worden uitgevoerd of pas na een waarschuwing (false).
     * @return meteen wissen of niet
     */
    public boolean isDirectDelete() {
        return directDelete;
    }

    /**
     * Configuratie parameter van EditBaseBean welke bepaalt of een delete
     * meteen (true) moet worden uitgevoerd of pas na een waarschuwing (false).
     * @param directDelete meteen wissen of niet
     */
    public void setDirectDelete(boolean directDelete) {
        this.directDelete = directDelete;
    }

    /**
     * Configuratie parameter van EditBaseBean welke bepaalt of
     * de bebruiker de records mag editten of niet. Indien niet worden
     * de knoppen niet actief.
     * @return edit of niet
     */
    public boolean isAllowEdits() {
        return allowEdits;
    }

    /**
     * Configuratie parameter van EditBaseBean welke bepaalt of
     * de bebruiker de records mag editten of niet. Indien niet worden
     * de knoppen niet actief.
     * @param allowEdits edit of niet
     */
    public void setAllowEdits(boolean allowEdits) {
        this.allowEdits = allowEdits;
    }
}
