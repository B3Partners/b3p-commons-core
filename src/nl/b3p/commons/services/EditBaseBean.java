/*
 * EditBaseBean.java
 *
 * Created on 3 april 2005, 16:07
 */

package nl.b3p.commons.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Deze class maakt het mogelijk om een records in een database hoofdtabel met
 * een of meer afhankelijke subtabellen te bewerken, wissen of nieuw aan te maken.
 * <p>
 * Uitgangspunt is een struts formulier waarin alle velden van de hoofdtabel en de
 * subtabellen zijn opgenomen. Indien meer records van een subtabel bij het record
 * van de hoofdtabel horen, dan dient toch elk veld van de subtabel slechts eenmaal
 * in het formulieer voor te komen (er wordt telkens slechts 1 subrecord tegelijkertijd
 * bewerkt).
 * <p>
 * De parameters van de constructor van deze class vertegenwoordigen alle informatie,
 * welke via het posten van een struts formulier beschikbaar komt plus struts informatie
 * voor mapping en foutafhandeling.
 * <p>
 * De class kent 2 publieke functies: een voor het uitvoeren van de gewenste actie (process)
 * en een voor het opvragen van de actie als tekst (getAction). Verder kent de class
 * vele abstracte functies waarin het daadwerkelijke werk voor een concrete implementatie
 * kan worden vastgelegd.
 * <p>
 * Het struts formulier wordt via een bijbehorende JSP bewerkt. In deze JSP moeten een aantal
 * knoppen met een vastgestelde naam worden aangebracht om het geheel te laten functioneren.
 * In het formulier bestaan de volgende submit-knoppen percies 1 keer:
 * <UL>
 * <LI><B>process</B>-knop verwerkt alle veranderingen in het formulier, dus ook veranderingen
 * in velden die betrekking hebben op record van subtabellen.
 * <LI><B>delete</B>-knop wist het hoofdrecord met alle bij behorende subrecords
 * <LI><B>new</B>-knop maakt een nieuw hoofdrecord aan, waarbij alle subrecords leeg zijn.
 * </UL>
 * De JSP bevat vervolgens voor iedere subtabel een dropdown waarin alle mogelijke subrecords
 * behorende bij dit hoofdrecord zijn opgenomen. Zodra een subrecord uit deze dropdown wordt gekozen
 * wordt het formulier automatisch gesubmit. De nieuwe JSP pagina bevat nu het geselecteerde
 * subrecord als actieve record en de waarden uit dit actieve record zijn in de bijbehorende
 * velden ingevuld.
 * <p>
 * De subtabel kan een van 2 soorten zijn:
 * <ul>
 * <li> <B>one-to-many</B>: in dit geval kunnen de waarden in de subtabel per record bewerkt
 * worden en zijn de knoppen <B>subnew</B> en <B>subdelete</B> noodzakelijk per subtabel.
 * <li> <B>many-to-many</B>: in dit geval kan alleen een relatie gemaakt of verbroken
 * worden en zijn de knoppen <B>newjoin</B> en <B>deletejoin</B> noodzakelijk per tabel.
 * </ul>
 * De naam van deze knoppen dienen uitgebreid worden met een rangnummer van de subtabel. De
 * exacte waarde van het rangnummer is niet van belang, zolang dit nummer maar consequent in
 * JSP en deze class wordt doorgevoerd, bijvoorbeeld: subnew1 en subnew2.
 * <p>
 * De knoppen hebben de volgende betekenis:
 * <ul>
 * <li><B>subnew + rangnummer</B>: creeert een leeg subrecord, dat gevuld kan worden en bij
 * de eerste volgende <B>process</B> van het gehele formulier wordt dit nieuwe record toegevoegd.
 * <li><B>subdelete + rangnummer</B>: wist het actieve subrecord
 * <li><B>newjoin + rangnummer</B>: creeert een koppeling tussen het hoofdrecord en dit subrecord
 * <li><B>deletejoin + rangnummer</B>: verbreekt de koppeling tussen het hoofdrecord en dit subrecord
 * </ul>
 * Verder kan nog een cancel-knop worden toegevoegd voor het gehele formulier. Het belangrijk
 * te weten dat altij het gehele formulier wordt gepost; het hoofdrecord met alle actieve
 * subrecords van de verschillende subtabellen. Het is dus niet mogelijk een subrecord te
 * bewaren terwijl aan een ander subrecord nog wordt gewerkt.
 * <p>
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.0 $ $Date: 2005/05/17 12:48:31 $
 */

public abstract class EditBaseBean extends FormBaseBean {
    
    protected Log log = LogFactory.getLog(this.getClass());
    
    private String newAction = null;
    
    protected Object theObject = null;
    protected ArrayList subObjects = new ArrayList();
    
    public static final String INVALID_ACTION = "INVALID";
    public static final String CANCELLED_ACTION = "CANCELLED";
    
    public static final String START_ACTION = "Start";
    public static final String EDIT_ACTION = "Edit";
    public static final String NEW_ACTION = "New";
    public static final String SAVE_ACTION = "Save";
    public static final String SAVENEW_ACTION = "SaveNew";
    public static final String DELETE_ACTION = "Delete";
    
    // Volgende knop wordt aangevuld met rangnummer van subtabel
    public static final String SUBDELETE_ACTION = "SubDelete";
    public static final String SUBNEW_ACTION = "SubNew";
    public static final String SUBSAVENEW_ACTION = "SubSaveNew";
    public static final String SUBSAVE_ACTION = "SubSave";
    public static final String SUBEDIT_ACTION = "SubEdit";
    
    // Algemene knoppen
    public static final String OK_BUTTON = "ok";
    public static final String CANCEL_BUTTON = "cancel";
    
    public static final String DELETE_BUTTON = "delete";
    public static final String NEW_BUTTON = "new";
    public static final String SAVE_BUTTON = "save";
    public static final String SAVENEW_BUTTON = "savenew";
    public static final String EDIT_BUTTON = "edit";
    
    // Volgende knoppen worden aangevuld met rangnummer van subtabel
    public static final String SUBNEW_BUTTON = "subnew";
    public static final String SUBSAVE_BUTTON = "subsave";
    public static final String SUBSAVENEW_BUTTON = "subsavenew";
    public static final String SUBDELETE_BUTTON = "subdelete";
    public static final String SUBEDIT_BUTTON = "subedit";
    
    public static final String NEWJOIN_BUTTON = "newjoin";
    public static final String DELETEJOIN_BUTTON = "deletejoin";
    
    // Dit zijn de waarden voor tag van de foutmeldingen in de jsp
    public static final String MAIN_MESSAGE = "MAIN_MESSAGE";
    // Volgende knoppen worden aangevuld met rangnummer van subtabel
    public static final String SUB_MESSAGE = "SUB_MESSAGE";
    
    // Default id voor lege subrecords
    protected static int TEMPNEW_ID = -1;
    
    // Configuratie parameters
    private boolean directSave = false;
    private boolean directDelete = false;
    private boolean directSubSave = false;
    private boolean directSubDelete = false;
    private boolean returnAfterSubSave = true;
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
    public EditBaseBean(HttpServletRequest req,
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
    public EditBaseBean(HttpServletRequest req,
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
    public ActionForward process(boolean tokenValid, boolean transactionCancelled, ActionErrors validateErrors) {
        
        if (!isInit) {
            errors.add(MAIN_MESSAGE, new ActionError("error.invoerenrecord.general"));
            return mapping.findForward("failure");
        }
        
        ActionForward f = null;
        try {
            ActionErrors verrs = determineAction(tokenValid, transactionCancelled, validateErrors);
            
            determineObjects();
            
            // Welke knop is geklikt
            if (buttonPressed(OK_BUTTON)) {
                f = confirmButton(verrs);
            } else if (buttonPressed(CANCEL_BUTTON)) {
                f = cancelButton();
            } else if (buttonPressed(DELETE_BUTTON)) {
                f = deleteButton();
            } else if (buttonPressed(NEW_BUTTON)) {
                f = newButton();
            } else if (buttonPressed(SAVE_BUTTON)) {
                f = saveButton(verrs);
            } else if (buttonPressed(SAVENEW_BUTTON)) {
                f = saveNewButton(verrs);
            } else if (buttonPressed(EDIT_BUTTON)) {
                f = editButton(verrs);
            } else {
                // Volgende knoppen worden aangevuld met rangnummer van subtabel
                int numOfSubs = subObjects.size();
                for (int subNum=1; subNum<=numOfSubs; subNum++) {
                    if (buttonPressed(SUBNEW_BUTTON + subNum)) {
                        f = subNewButton(subNum);
                        break;
                    } else if (buttonPressed(SUBSAVE_BUTTON + subNum)) {
                        f = subSaveButton(subNum, verrs);
                        break;
                    } else if (buttonPressed(SUBSAVENEW_BUTTON + subNum)) {
                        f = subSaveNewButton(subNum, verrs);
                        break;
                    } else if (buttonPressed(SUBDELETE_BUTTON + subNum)) {
                        f = subDeleteButton(subNum);
                        break;
                    } else if (buttonPressed(SUBEDIT_BUTTON + subNum)) {
                        f = subEditButton(subNum);
                        break;
                    } else if (buttonPressed(NEWJOIN_BUTTON + subNum)) {
                        f = newJoinButton(subNum, verrs);
                        break;
                    } else if (buttonPressed(DELETEJOIN_BUTTON + subNum)) {
                        f = deleteJoinButton(subNum, verrs);
                        break;
                    }
                }
            }
            if (f!=null)
                return f;
            
            
            f = populateMainForm();
            if (f!=null)
                return f;
            
            if (theObject!=null) {
                int numOfSubs = subObjects.size();
                for (int subNum=1; subNum<=numOfSubs; subNum++) {
                    f = populateSubForms(subNum);
                    if (f!=null)
                        return f;
                }
            }
            
            determineNewAction();
            
        } catch (Exception e) {
            log.error("error: ", e);
            errors.add(MAIN_MESSAGE, new ActionError("error.database", e.getMessage()));
            return (mapping.findForward("failure"));
        } finally {
            // evt list maken?
        }
        
        try {
            // Aanmaken lijst voor koppeltabel
            createJoinList();
            // Aanmaken van lijsten en plaatsen op de sessie
            createLists();
            // Voor formulier specifieke afhandelingen
            f = userProcess(tokenValid, transactionCancelled, validateErrors);
            if (f!=null) {
                if(f.getName().equals("none")) {
                    // userProcess() heeft de response al geschreven
                    return null;
                } else {
                    return f;
                }
            }
        } catch (B3pCommonsException be) {
            log.error("error2: ", be);
            errors.add(MAIN_MESSAGE, new ActionError("error.invoerenrecord.general", be.getMessage()));
            return mapping.findForward("failure");
        }
        
        return mapping.findForward("success");
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
    protected ActionErrors determineAction(boolean tokenValid,
            boolean transactionCancelled, ActionErrors validateErrors) throws B3pCommonsException {
        action = getParamAsString("action");
        
        newAction = action;
        if (nullOrEmpty(action)) {
            action = START_ACTION;
            newAction = EDIT_ACTION;
        }
        
        // De eerste keer is er nog geen token
        if (!isAction(START_ACTION)) {
            // Validate the transactional control token
            if (!tokenValid) {
                errors.add(MAIN_MESSAGE, new ActionError("error.transaction.token"));
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
            if (log.isDebugEnabled())
                log.debug(" Transaction '" + action + "' was cancelled");
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
        } catch (B3pCommonsException ex) {
            log.debug("no main object!");
        }
        if (theObject==null)
            return;
        // Haal actieve gekoppelde objecten op, indien van toepassing
        int subNum = 1;
        do {
            try {
                subObjects.add(getSubObject(subNum)); // 0-based
            } catch (B3pCommonsException ex) {
                log.debug("no subobject" + subNum + "!");
            } // 0-based
            subNum++;
        } while (subNum<=10); // limiet om einde van loop zeker te stellen
    }
    
    /**
     * Een subclass kan deze methode overriden om extra acties te doen, bijvoorbeeld
     * bij de save-actie (bevestiging) of code voor een nieuwe knop. Deze methode wordt
     * na het uitvoeren van alle standaardacties aangeroepen door EditBaseBean.process().
     *
     * Indien deze methode een niet-null ActionForward returnt wordt die ActionForward
     * als resultaat van de EditBaseBean.process() geretourneerd, anders wordt naar
     * "success" geforward.
     *
     * Een speciaal geval is indien de <code>userProcess()</code> een ActionForward met de <code>name</code>
     * property <code>"none"</code> teruggeeft. De EditBaseBean.process() zal dan een null
     * ActionForward retourneren wat betekent dat Struts verder niet naar de response
     * zal schrijven.
     *
     * <p>
     * @return Deze implementatie retourneert altijd <code>null</code>.
     */
    protected ActionForward userProcess(boolean tokenValid,
            boolean transactionCancelled, ActionErrors validateErrors) throws B3pCommonsException {
        return null;
    }
    
    protected ActionForward confirmButton(ActionErrors validateErrors) throws B3pCommonsException {
        if (allowEdits) {
            
            if (isAction(SAVE_ACTION)) {
                return saveAction(validateErrors);
//                return saveAllAction(validateErrors);
            }
            if (isAction(DELETE_ACTION) && theObject!=null) {
                return deleteAction();
            }
            
            int numOfSubs = subObjects.size();
            for (int subNum=1; subNum<=numOfSubs; subNum++) {
                Object subObject =(Object) subObjects.get(subNum-1);
                if (subObject!=null) {
                    String subSaveAction = SUBSAVE_ACTION + subNum;
                    if (subSaveAction.equals(action)) {
                        return subSaveAction(subNum, validateErrors);
                    }
                    String subEditAction = SUBEDIT_ACTION + subNum;
                    String subDeleteAction = SUBDELETE_ACTION + subNum;
                    if (subDeleteAction.equals(action) && subObject!=null) {
                        return subDeleteAction(subNum);
                    }
                }
            }
            
        } else {
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
            newAction = EDIT_ACTION;
        }
        return null;
    }
    
    protected ActionForward cancelButton() throws B3pCommonsException {
        log.info(" Cancel action");
        int numOfSubs = subObjects.size();
        Object subObject;
        for (int subNum=1; subNum<=numOfSubs; subNum++) {
            // Bij annuleren worden de velden dan gereset via nieuw subobject
            subObject = getNewSubObject(subNum);
            subObjects.set(subNum-1,  subObject);
            setSubID(subNum, "");
        }
        newAction = EDIT_ACTION;
        return null;
    }
    
    protected ActionForward deleteButton() throws B3pCommonsException {
        if (isAction(EDIT_ACTION) && allowEdits && theObject!=null) {
            if (directDelete) {
                return deleteAction();
            } else {
                log.debug(" delete");
                errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.delete"));
                newAction = DELETE_ACTION;
            }
        } else {
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
        }
        return null;
    }
    
    protected ActionForward newButton() throws B3pCommonsException {
        if (isAction(EDIT_ACTION) && allowEdits) {
            log.debug(" Reset DynaForm bean under key " + mapping.getAttribute());
            form.initialize(mapping);
            theObject = getNewObject();
            // merker dat record nieuw is, wordt later op gecheckt
            setID(Integer.toString(TEMPNEW_ID));
            int numOfSubs = subObjects.size();
            for (int subNum=1; subNum<=numOfSubs; subNum++) {
                setSubID(subNum, null);
                Object subObject = getNewSubObject(subNum);
                subObjects.set(subNum-1, subObject );
            }
            log.debug(" create new");
//                errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.new"));
        } else {
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
        }
        newAction = EDIT_ACTION;
        return null;
    }
    
    protected ActionForward saveButton(ActionErrors validateErrors) throws B3pCommonsException {
        if (isAction(EDIT_ACTION) && allowEdits ) {
            if (directSave) {
                return saveAction(validateErrors);
            } else {
                if (theObject!=null) {
                    log.debug(" save existing");
                    errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.save"));
                } else {
                    log.debug(" create new");
                    errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.new"));
                }
                newAction = SAVE_ACTION;
            }
        } else {
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
        }
        return null;
    }
    
    protected ActionForward saveNewButton(ActionErrors validateErrors) throws B3pCommonsException {
        if (isAction(EDIT_ACTION) && allowEdits ) {
            log.debug(" save current");
            saveAction(validateErrors);
            
            form.initialize(mapping);
            theObject = getNewObject();
            // merker dat record nieuw is, wordt later op gecheckt
            setID(Integer.toString(TEMPNEW_ID));
            int numOfSubs = subObjects.size();
            for (int subNum=1; subNum<=numOfSubs; subNum++) {
                setSubID(subNum, null);
                Object subObject = getNewSubObject(subNum);
                subObjects.set(subNum-1, subObject );
            }
            log.debug(" create new");
            
        } else {
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
        }
        return null;
    }
    
    protected ActionForward editButton(ActionErrors validateErrors) throws B3pCommonsException {
        newAction = EDIT_ACTION;
        return null;
    }
    
    protected ActionForward subNewButton(int subNum) throws B3pCommonsException {
        String subEditAction = SUBEDIT_ACTION + subNum;
        if ((isAction(EDIT_ACTION) || isAction(subEditAction)) && allowEdits) {
            Object subObject =(Object) subObjects.get(subNum-1);
            // merker dat record nieuw is, wordt later op gecheckt
            setSubID(subNum, Integer.toString(TEMPNEW_ID));
            subObject = getNewSubObject(subNum);
            subObjects.set(subNum-1, subObject );
//          errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subnew", getSubNames(subNum)));
            newAction = SUBEDIT_ACTION + subNum;
        } else {
            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.notallowed"));
            newAction = EDIT_ACTION;
        }
        return null;
    }
    
    protected ActionForward subSaveButton(int subNum, ActionErrors validateErrors) throws B3pCommonsException {
        Object subObject =(Object) subObjects.get(subNum-1);
        String subEditAction = SUBEDIT_ACTION + subNum;
        if (isAction(subEditAction) && allowEdits) {
            if (directSubSave) {
                return subSaveAction(subNum, validateErrors);
            } else {
                if (subObject!=null) {
                    log.debug(" subsave existing");
                    errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subsave", getSubNames(subNum)));
                } else {
                    log.debug(" create subnew");
                    errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subnew", getSubNames(subNum)));
                }
                newAction = SUBSAVE_ACTION +subNum;
            }
        } else {
            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.notallowed"));
        }
        return null;
    }
    
    protected ActionForward subSaveNewButton(int subNum, ActionErrors validateErrors) throws B3pCommonsException {
        Object subObject =(Object) subObjects.get(subNum-1);
        String subEditAction = SUBEDIT_ACTION + subNum;
        if (isAction(subEditAction) && allowEdits) {
            subSaveAction(subNum, validateErrors);
            
            // merker dat record nieuw is, wordt later op gecheckt
            setSubID(subNum, Integer.toString(TEMPNEW_ID));
            subObject = getNewSubObject(subNum);
            subObjects.set(subNum-1, subObject );
            
        } else {
            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.notallowed"));
        }
        return null;
    }
    
    protected ActionForward subDeleteButton(int subNum) throws B3pCommonsException {
        Object subObject =(Object) subObjects.get(subNum-1);
        String subEditAction = SUBEDIT_ACTION + subNum;
        if (isAction(subEditAction) && allowEdits && subObject!=null) {
            if (directSubDelete) {
                return subDeleteAction(subNum);
            }
            log.debug(" delete subrecord");
            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subdelete", getSubNames(subNum)));
            newAction = SUBDELETE_ACTION + subNum;
        } else {
            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.notallowed"));
            newAction = EDIT_ACTION;
        }
        return null;
    }
    
    protected ActionForward subEditButton(int subNum) throws B3pCommonsException {
        Object subObject =(Object) subObjects.get(subNum-1);
        if (subObject!=null) {
            log.debug(" edit subrecord");
//                            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subedit", getSubNames(subNum)));
            newAction = SUBEDIT_ACTION + subNum;
        } else {
            log.debug(" edit subrecord, but no subrecord present");
//                            errors.add(SUB_MESSAGE + subNum, new ActionError("error.invoerenrecord.nosubedit", getSubNames(subNum)));
        }
        return null;
    }
    
    protected ActionForward newJoinButton(int subNum, ActionErrors validateErrors) throws B3pCommonsException {
        if (isAction(EDIT_ACTION) && allowEdits) {
            createJoin(subNum);
        } else {
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
            newAction = EDIT_ACTION;
        }
        return null;
    }
    
    protected ActionForward deleteJoinButton(int subNum, ActionErrors validateErrors) throws B3pCommonsException {
        if (isAction(EDIT_ACTION) && allowEdits) {
            deleteJoin(subNum);
        } else {
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
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
    protected ActionForward deleteAction() throws B3pCommonsException {
        log.info(" Deleting Record '" + theObject.toString() + "'");
        int numOfSubs = subObjects.size();
        for (int subNum=1; subNum<=numOfSubs; subNum++) {
            Object subObject =(Object) subObjects.get(subNum-1);
            if (subObject!=null) {
                deleteSubObject(subNum);
                subObject = null;
                subObjects.set(subNum-1, subObject );
                setSubID(subNum, null);
            }
        }
        deleteObject();
        theObject = null;
        setID(null);
        log.debug(" Reset DynaForm bean under key " + mapping.getAttribute());
        
        form.initialize(mapping);
        subObjects = new ArrayList();
        newAction = EDIT_ACTION;
        
        if (directDelete)
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.deletedone"));
        
        return null;
    }
    
    /**
     * Deze functie wist een subrecord.
     * <p>
     * @return niet null bij fouten
     * @param subNum rangnummer van formulier
     */
    protected ActionForward subDeleteAction(int subNum) throws B3pCommonsException {
        Object subObject =(Object) subObjects.get(subNum-1);
        log.info(" deleting subrecord '" + subObject.toString() + "'");
        deleteSubObject(subNum);
        // HACK: door een leeg object door te geven wordt dit subformulier gewist
        subObject = getNewSubObject(subNum);
        subObjects.set(subNum-1, subObject );
        setSubID(subNum, null);
        newAction = EDIT_ACTION;
        
        if (directSubDelete)
            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subdeletedone", getSubNames(subNum)));
        
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
    protected ActionForward saveAction(ActionErrors validateErrors) throws B3pCommonsException {
        // validatie
        if (reduceMainErrors(validateErrors)) {
            log.info("Validation error!");
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.savewitherrors"));
            newAction = SAVE_ACTION;
        } else {
            // Het hoofdobject wordt alleen aangemaakt, indien
            // in het form een merker is aangebracht die dit aangeeft.
            if (theObject == null && Integer.toString(TEMPNEW_ID).equals(getMainID())) {
                theObject = getNewObject();
                subObjects = new ArrayList();
            }
            if (theObject!=null) {
                populateObject();
                syncID();
                log.debug(" Populating database object from form bean");
            }
            newAction = EDIT_ACTION;
            if (directSave)
                errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.savedone"));
        }
        return null;
    }
    
    
    /**
     * Deze functie bewaart een subrecord.
     * <p>
     * @return niet null bij fouten
     * @param subNum rangnummer van formulier
     * @param validateErrors foutenmeldingen van formulier
     */
    protected ActionForward subSaveAction(int subNum, ActionErrors validateErrors) throws B3pCommonsException {
        Object subObject =(Object) subObjects.get(subNum-1);
        String subEditAction = SUBEDIT_ACTION + subNum;
        String subSaveAction = SUBSAVE_ACTION + subNum;
        // validatie
        if (reduceSubErrors(subNum, validateErrors)) {
            log.info("Validation error subform!");
            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subsavewitherrors", getSubNames(subNum)));
            newAction = subSaveAction;
        } else {
            // Een nieuw subobject wordt alleen aangemaakt indien
            // in het form een merker is aangebracht die dit aangeeft.
            if (subObject == null && Integer.toString(TEMPNEW_ID).equals(getSubID(subNum))) {
                subObject = getNewSubObject(subNum);
                subObjects.set(subNum-1,  subObject);
            }
            if (subObject!=null) {
                log.info(" Adding Subrecord " + subNum + " '" + subObject.toString() + "'");
                populateSubObject(subNum);
                syncSubID(subNum);
            }
            if (returnAfterSubSave)
                newAction = EDIT_ACTION;
            else
                newAction = subEditAction;
            
            if (directSubSave)
                errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subsavedone", getSubNames(subNum)));
        }
        return null;
    }
    
    
    /**
     * Deze functie bewaart het hoofdformulier met alle subformulieren. Er wordt dan
     * gecontroleerd of struts fouten in de invoer van het formulier heeft geconstateerd.
     * Als dit zo is wordt verder verwerking afgebroken en gaat men meteen terug naar het
     * formulier. Als er geen fouten zijn dan wordt de database gevuld met de informatie
     * in het formulier.
     * Dit is een alternatief voor de saveAction, waarbij alle subrecords ook worden
     * gesaved.
     * <p>
     * @param validateErrors zoals deze door struts zijn vastgesteld
     * @return niet null bij validatie errors
     */
    protected ActionForward saveAllAction(ActionErrors validateErrors) throws B3pCommonsException {
        // validatie
        if (validateErrors!=null && !validateErrors.isEmpty()) {
            log.info("Validation error!");
            errors.add(validateErrors);
            return null;
        }
        // Het hoofdobject wordt alleen aangemaakt, indien
        // in het form een merker is aangebracht die dit aangeeft.
        if (theObject == null && Integer.toString(TEMPNEW_ID).equals(getMainID())) {
            theObject = getNewObject();
            subObjects = new ArrayList();
        }
        if (theObject!=null) {
            populateObject();
            syncID();
            int numOfSubs = subObjects.size();
            for (int subNum=1; subNum<=numOfSubs; subNum++) {
                Object subObject =(Object) subObjects.get(subNum-1);
                // Een nieuw subobject wordt alleen aangemaakt indien
                // in het form een merker is aangebracht die dit aangeeft.
                if (subObject == null && Integer.toString(TEMPNEW_ID).equals(getSubID(subNum))) {
                    subObject = getNewSubObject(subNum);
                    subObjects.set(subNum-1,  subObject);
                }
                if (subObject!=null) {
                    populateSubObject(subNum);
                    syncSubID(subNum);
                }
            }
            log.debug(" Populating database object and subobjects from form bean");
        }
        newAction = EDIT_ACTION;
        
        if (directSave)
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.savedone"));
        
        return null;
    }
    
    /**
     * Deze functie vult het hoofdformulier met
     * de bijgewerkte informatie uit de database.
     * <p>
     * @return niet null bij fouten
     */
    protected ActionForward populateMainForm() throws B3pCommonsException {
        if (theObject==null)
            return null;
        // Bij save action moet het form niet opnieuw gevuld worden
        if (!isNewAction(SAVE_ACTION)) {
            populateForm();
            log.debug(" Populating form from " + theObject.toString());
        }
        return null;
    }
    
    /**
     * Deze functie vult een subformulieren met
     * de bijgewerkte informatie uit de database.
     * <p>
     * @return niet null bij fouten
     */
    protected ActionForward populateSubForms(int subNum) throws B3pCommonsException {
        Object subObject =(Object) subObjects.get(subNum-1);
        if (subObject==null)
            return null;
        String subSaveAction = SUBSAVE_ACTION + subNum;
        // Bij subsave action moet het subform niet opnieuw gevuld worden
        if (!isNewAction(subSaveAction)) {
            populateSubForm(subNum);
            log.debug(" Populating subform from " + subObject.toString());
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
        if (ve==null || ve.isEmpty())
            return false;
        Iterator it = ve.properties();
        boolean errorsFound = false;
        ArrayList mp = getMainProperties();
        while (it.hasNext()) {
            String ep = (String) it.next();
            Iterator it2 = ve.get(ep);
            if (mp==null || mp.contains(ep)) {
                while (it2.hasNext()) {
                    errorsFound = true;
                    ActionMessage am = (ActionMessage) it2.next();
                    errors.add(MAIN_MESSAGE, am);
                }
            }
        }
        return errorsFound;
    }
    
    /**
     * Deze functie verwijdert de niet relevante errors uit de lijst door
     * te kijken naar de velden (properties) die in het form aanwezig zijn.
     * <p>
     * @return lijst met omgevormde foutmeldingen
     * @param subForm rangnummer van deze subtabel
     * @param validateErrors volledige lijst foutmeldingen
     */
    protected boolean reduceSubErrors(int subForm, ActionErrors ve) {
        if (ve==null || ve.isEmpty())
            return false;
        Iterator it = ve.properties();
        boolean errorsFound = false;
        ArrayList mp = getSubProperties(subForm);
        while (it.hasNext()) {
            String ep = (String) it.next();
            Iterator it2 = ve.get(ep);
            if (mp==null || mp.contains(ep)) {
                while (it2.hasNext()) {
                    errorsFound = true;
                    ActionMessage am = (ActionMessage) it2.next();
                    errors.add(SUB_MESSAGE + subForm, am);
                }
            }
        }
        return errorsFound;
    }
    
    /**
     * Deze functie vult de nieuwe actie in op het struts form, zodat
     * de volgende ronde hiermee gewerkt wordt.
     * <p>
     * @return niet null bij fouten
     */
    protected void determineNewAction() throws B3pCommonsException {
        setForm("action", newAction);
        return;
    }
    
    protected B3pCommonsException getNoSubFormException(int subForm) {
        return new B3pCommonsException(messages.getMessage(locale, "error.invoerenrecord.noform", new Integer(subForm)));
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
        if (lact==null)
            return false;
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
     * Deze functie geeft lijst van veldnamen, waarop gecheckt moet worden bij
     * validatie. Indien null, worden alle velden meegenomen.
     * <p>
     * @return lijst met veldnamen (properties) in formulier
     */
    protected ArrayList getMainProperties() {
        return null;
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
     * Een concrete implementatie van deze functie bepaalt het id in het struts
     * formulier voor de gezochte subtabel.
     * <p>
     * De code kan er als volgt uitzien:
     * <CODE>
     * switch (subForm) {
     *    case 1:
     *        return FormUtils.StringToInt(getForm("selected_sub1_id"));
     *        break;
     *    case 2:
     *        return FormUtils.StringToInt(getForm("selected_sub2_id"));
     *        break;
     *    default:
     *          throw new B3pCommonsException("Subform bestaat niet!");
     * }
     * </CODE>
     * Voor een niet bestaand subform moet de functie een B3pCommonsException geven, anders
     * loopt de functie onnodig lang door.
     * <p>
     * @return id van het tabelobject van de subtabel of null, indien onbekend
     * @param subForm rangnummer van deze subtabel
     * @throws B3pCommonsException bij niet bestaan van het subform
     */
    protected String getSubID(int subForm) throws B3pCommonsException {
        throw getNoSubFormException(subForm);
    }
    
    /**
     * Een concrete implementatie van deze functie haalt het tabelobject op van de gekoppelde
     * subtabel met een subid als aangegeven. Indien de waarde van het veld met de subid
     * gelijk is aan TEMPNEW_ID, dan moet deze functie null retourneren om het juist aanmaken
     * van nieuwe subrecords te garanderen.
     * <p>
     * De code kan er als volgt uitzien:
     * <CODE>
     * int subid = getSubObjectID(subForm);
     * switch (subForm) {
     *    case 1:
     *        return hsession.createQuery("from subtabel1 where id = subid");
     *        break;
     *    case 2:
     *        return hsession.createQuery("from subtabel2 where id = subid");
     *        break;
     *    default:
     *          throw new B3pCommonsException("Subform bestaat niet!");
     * }
     * </CODE>
     * Voor een niet bestaand subform moet de functie een B3pCommonsException geven, anders
     * loopt de functie onnodig lang door.
     * <p>
     * @return het tabelobject van de subtabel
     * @param subForm rangnummer van deze subtabel
     * @throws B3pCommonsException bij niet bestaan van het subform
     */
    protected Object getSubObject(int subForm) throws B3pCommonsException {
        throw getNoSubFormException(subForm);
    }
    
    /**
     * Deze functie geeft lijst van veldnamen, waarop gecheckt moet worden bij
     * validatie. Indien null, worden alle velden meegenomen.
     * <p>
     * @return lijst met veldnamen (properties) in formulier
     * @param subForm rangnummer van deze subtabel
     */
    protected ArrayList getSubProperties(int subForm) {
        return null;
    }
    
    /**
     * Deze functie geeft de naam van het subformulier, zoals dat bij
     * foutmeldingen moet worden geroond. Indien deze functie niet overklast
     * wordt, wordt simpelweg het rangnummer getoond.
     * <p>
     * @return naam van het subformulier
     * @param subForm rangnummer van deze subtabel
     */
    protected String getSubNames(int subForm) {
        return Integer.toString(subForm);
    }
    
    /**
     * Een concrete implementatie van deze functie zorgt ervoor dat een nieuw persistent
     * object wordt aangemaakt voor de gevraagde subtabel.
     * <p>
     * @param subForm rangnummer van deze subtabel
     * @throws B3pCommonsException bij fouten
     * @return het nieuwe persistent subobject
     */
    protected Object getNewSubObject(int subForm) throws B3pCommonsException {
        throw getNoSubFormException(subForm);
    }
    
    /**
     * Een concrete implementatie van deze functie plaatst een subid in het subid veld.
     * Indien subid null is, wordt het veld leeg gemaakt.
     * <p>
     * @param subid waarde van nieuw subid
     * @param subForm rangnummer van deze subtabel
     * @throws B3pCommonsException bij fouten
     */
    protected void setSubID(int subForm, String subid) throws B3pCommonsException {
        throw getNoSubFormException(subForm);
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
     * Een concrete implementatie van deze functie synchroniseert het id van het hoofdobject
     * met het id in het strutsformulier.
     * <p>
     * De code kan er als volgt uitzien:
     * <CODE>
     * Object subObject = subObjects.get(subForm-1);
     * if (subObject!=null) {
     *  switch (subForm) {
     *    case 1:
     *        int subid = ((Cast1)subObject).getSub1Id();
     *        setSubID(subForm, new Integer(subid));
     *        break;
     *    case 2:
     *        int subid = ((Cast2)subObject).getSub2Id();
     *        setSubID(subForm, new Integer(subid));
     *        break;
     *    default:
     *          throw new B3pCommonsException("Subform bestaat niet!");
     *  }
     * } else {
     *  setSubID(subForm, null);
     * }
     * </CODE>
     * <p>
     * @param subForm rangnummer van deze subtabel
     * @throws B3pCommonsException bij fouten
     */
    protected void syncSubID(int subForm) throws B3pCommonsException {
        throw getNoSubFormException(subForm);
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
     * Een concrete implementatie van deze functie wist een record uit een subtabel op
     * basis van het opgegeven record. Het rangnummer van de subtabel is nodig om te
     * bepalen uit welke subtabel gewist moet worden.
     * De sessie kan gebruikt worden om lijsten te wissen of te updaten.
     * <p>
     * @param subForm het rangnummer van de subtabel
     * @throws B3pCommonsException bij fouten
     */
    protected void deleteSubObject(int subForm) throws B3pCommonsException {
        throw getNoSubFormException(subForm);
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
     * Een concrete implementatie van deze functie vult het subobject vanuit het form.
     * <p>
     * @param subForm het rangnummer van de subtabel
     * @throws B3pCommonsException bij fouten
     */
    protected void populateSubObject(int subForm) throws B3pCommonsException {
        throw getNoSubFormException(subForm);
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
     * Een concrete implementatie van deze functie zorgt voor het vullen van het form uit het subobject
     * <p>
     * @param subForm het rangnummer van de subtabel
     * @throws B3pCommonsException bij fouten
     */
    protected void populateSubForm(int subForm) throws B3pCommonsException {
        throw getNoSubFormException(subForm);
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
     * Bij een many-to-many relatie wordt voor het hoofdrecord van het struts formulier opgezocht
     * welke records uit de tabel aan de andere zijde van de many-to-many relatie hierbij horen.
     * <p>
     * De namen van de lijsten moeten afgestemd worden met het gebruik in de jsp's waarin het struts
     * formulier wordt gebruikt.
     * <p>
     * @throws nl.b3p.commons.services.B3pCommonsException bij fout ophalen lijsten
     */
    protected void createJoinList() throws B3pCommonsException {
        return;
    }
    
    /**
     * Een concrete implementatie van deze functie creeert een koppeling tussen het hoofdobject en
     * het koppelobject in de database.
     * <p>
     * @param subForm het rangnummer van de subtabel
     * @throws B3pCommonsException bij fouten
     */
    protected void createJoin(int subForm) throws B3pCommonsException {
        throw getNoSubFormException(subForm);
    }
    
    /**
     * Een concrete implementatie van deze functie verbreekt een koppeling tussen het hoofdobject en
     * het koppelobject in de database.
     * <p>
     * @param subForm het rangnummer van de subtabel
     * @throws B3pCommonsException bij fouten
     */
    protected void deleteJoin(int subForm) throws B3pCommonsException {
        throw getNoSubFormException(subForm);
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
     * Configuratie parameter van EditBaseBean welke bepaalt of een subsave
     * meteen (true) moet worden uitgevoerd of pas na een waarschuwing (false).
     * @return meteen bewaren of niet
     */
    public boolean isDirectSubSave() {
        return directSubSave;
    }
    
    /**
     * Configuratie parameter van EditBaseBean welke bepaalt of een subsave
     * meteen (true) moet worden uitgevoerd of pas na een waarschuwing (false).
     * @param directSubSave meteen bewaren of niet
     */
    public void setDirectSubSave(boolean directSubSave) {
        this.directSubSave = directSubSave;
    }
    
    /**
     * Configuratie parameter van EditBaseBean welke bepaalt of een subdelete
     * meteen (true) moet worden uitgevoerd of pas na een waarschuwing (false).
     * @return meteen wissen of niet
     */
    public boolean isDirectSubDelete() {
        return directSubDelete;
    }
    
    /**
     * Configuratie parameter van EditBaseBean welke bepaalt of een subdelete
     * meteen (true) moet worden uitgevoerd of pas na een waarschuwing (false).
     * @param directSubDelete meteen wissen of niet
     */
    public void setDirectSubDelete(boolean directSubDelete) {
        this.directSubDelete = directSubDelete;
    }
    
    /**
     * Configuratie parameter van EditBaseBean welke bepaalt of na een subsave
     * terug naar het hoofdformulier teruggegaan moet worden (true) of dat
     * in de edit mode van het subformulier gebleven moet worden (false).
     * @return naar edit of niet
     */
    public boolean isReturnAfterSubSave() {
        return returnAfterSubSave;
    }
    
    /**
     * Configuratie parameter van EditBaseBean welke bepaalt of na een subsave
     * terug naar het hoofdformulier teruggegaan moet worden (true) of dat
     * in de edit mode van het subformulier gebleven moet worden (false).
     * @param returnAfterSubSave naar edit of niet
     */
    public void setReturnAfterSubSave(boolean returnAfterSubSave) {
        this.returnAfterSubSave = returnAfterSubSave;
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
