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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
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
    public static final String DELETE_ACTION = "Delete";
    
    // Volgende knop wordt aangevuld met rangnummer van subtabel
    public static final String SUBDELETE_ACTION = "SubDelete";
    public static final String SUBNEW_ACTION = "SubNew";
    public static final String SUBSAVE_ACTION = "SubSave";
    public static final String SUBEDIT_ACTION = "SubEdit";
    
    // Algemene knoppen
    public static final String OK_BUTTON = "ok";
    public static final String CANCEL_BUTTON = "cancel";
    
    public static final String DELETE_BUTTON = "delete";
    public static final String NEW_BUTTON = "new";
    public static final String SAVE_BUTTON = "save";
    public static final String EDIT_BUTTON = "edit";
    
    // Volgende knoppen worden aangevuld met rangnummer van subtabel
    public static final String SUBNEW_BUTTON = "subnew";
    public static final String SUBSAVE_BUTTON = "subsave";
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
        
        ActionErrors verrs = determineAction(tokenValid, transactionCancelled, validateErrors);
        
        determineObjects();
        
        ActionForward f = null;
        try {
            f = doCreateAction();
            if (f!=null)
                return f;
            
            f = doDeleteAction();
            if (f!=null)
                return f;
            
            f = doSaveAction(verrs);
            // Alternatief: f = doSaveAllAction(verrs);
            if (f!=null)
                return f;
            
            f = doEditAction(verrs);
            if (f!=null)
                return f;
            
            int numOfSubs = subObjects.size();
            if (theObject!=null) {
                for (int subNum=1; subNum<=numOfSubs; subNum++) {
                    f = doSubEditAction(subNum, verrs);
                    if (f!=null)
                        return f;
                    f = doSubDeleteAction(subNum);
                    if (f!=null)
                        return f;
                    f = doSubSaveAction(subNum, verrs);
                    if (f!=null)
                        return f;
                }
            }
            
            f = populateMainForm();
            if (f!=null)
                return f;
            
            if (theObject!=null) {
                for (int subNum=1; subNum<=numOfSubs; subNum++) {
                    f = populateSubForms(subNum);
                    if (f!=null)
                        return f;
                }
            }
            
            determineNewAction();
            
        } finally {
            
            // Aanmaken lijst voor koppeltabel
            try {
                createJoinList();
            } catch (B3pCommonsException be) {
                errors.add(MAIN_MESSAGE, new ActionError("error.invoerenrecord.general"));
                return mapping.findForward("failure");
            }
            
            // Aanmaken van lijsten en plaatsen op de sessie
            try {
                createLists();
            } catch (B3pCommonsException be) {
                errors.add(MAIN_MESSAGE, new ActionError("error.invoerenrecord.general"));
                return mapping.findForward("failure");
            }
            
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
    protected ActionErrors determineAction(boolean tokenValid, boolean transactionCancelled, ActionErrors validateErrors) {
        try {
            action = getParamAsString("action");
        } catch (B3pCommonsException be) {
            if (log.isErrorEnabled())
                log.error(be);
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
        } catch (B3pCommonsException te) {}
        if (theObject==null)
            return;
        // Haal actieve gekoppelde objecten op, indien van toepassing
        try {
            int subNum = 1;
            do {
                subObjects.add(getSubObject(subNum)); // 0-based
                subNum++;
            } while (subNum<=10); // limiet om einde van loop zeker te stellen
        } catch (B3pCommonsException te) {}
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
    protected ActionForward userProcess(boolean tokenValid, boolean transactionCancelled, ActionErrors validateErrors) {
        return null;
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
                if (theObject!=null) {
                    if (log.isDebugEnabled())
                        log.debug(" process this, save existing");
                    errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.save"));
                } else {
                    if (log.isDebugEnabled())
                        log.debug(" process this, create new");
                    errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.new"));
                }
                newAction = SAVE_ACTION;
            } else {
                errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
                newAction = EDIT_ACTION;
            }
        }
        if (isAction(NEW_ACTION) && buttonPressed(CANCEL_BUTTON)) {
            if (log.isInfoEnabled())
                log.info(" Cancel new action");
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
        if (isAction(DELETE_ACTION) && buttonPressed(OK_BUTTON) && theObject!=null) {
            if (allowEdits) {
                return deleteAction();
            } else {
                errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
                newAction = EDIT_ACTION;
            }
        }
        if (isAction(DELETE_ACTION) && buttonPressed(CANCEL_BUTTON)) {
            if (log.isInfoEnabled())
                log.info(" Cancel delete action");
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
        if (log.isInfoEnabled())
            log.info(" Deleting Record '" + theObject.toString() + "'");
        try {
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
        } catch (B3pCommonsException dbe) {
            if (log.isErrorEnabled())
                log.error("  Database error deleting: ", dbe);
            errors.add(MAIN_MESSAGE, new ActionError("error.database", dbe.getMessage()));
            return (mapping.findForward("failure"));
        }
        if (log.isDebugEnabled())
            log.debug(" Reset DynaForm bean under key " + mapping.getAttribute());
        
        form.initialize(mapping);
        subObjects = new ArrayList();
        newAction = EDIT_ACTION;
        
        if (directDelete)
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.deletedone"));
        
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
                errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
                newAction = EDIT_ACTION;
            }
        }
        if (isAction(SAVE_ACTION) && buttonPressed(CANCEL_BUTTON)) {
            if (log.isInfoEnabled())
                log.info(" Cancel save action");
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
            if (log.isInfoEnabled())
                log.info("Validation error!");
            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.savewitherrors"));
            newAction = SAVE_ACTION;
        } else {
            try {
                // Het hoofdobject wordt alleen aangemaakt, indien
                // in het form een merker is aangebracht die dit aangeeft.
                if (theObject == null && Integer.toString(TEMPNEW_ID).equals(getMainID())) {
                    theObject = getNewObject();
                    subObjects = new ArrayList();
                }
                if (theObject!=null) {
                    populateObject();
                    syncID();
                    if (log.isDebugEnabled())
                        log.debug(" Populating database object from form bean");
                }
            } catch (Exception dbe) {
                if (log.isErrorEnabled())
                    log.error("  Database error creating object: ", dbe);
                errors.add(MAIN_MESSAGE, new ActionError("error.database", dbe.getMessage()));
                return (mapping.findForward("failure"));
            }
            newAction = EDIT_ACTION;
            if (directSave)
                errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.savedone"));
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
        
        int numOfSubs = subObjects.size();
        
        // Bij edit mode worden new en delete omgeleid met nieuwe action
        if (isAction(EDIT_ACTION)) {
            // wis scherm om nieuw record te kunnen creeren
            if (buttonPressed(NEW_BUTTON)) {
                if (allowEdits) {
                    if (log.isDebugEnabled())
                        log.debug(" Reset DynaForm bean under key " + mapping.getAttribute());
                    form.initialize(mapping);
                    try {
                        theObject = getNewObject();
                        // merker dat record nieuw is, wordt later op gecheckt
                        setID(Integer.toString(TEMPNEW_ID));
                        for (int subNum=1; subNum<=numOfSubs; subNum++) {
                            setSubID(subNum, null);
                            Object subObject = getNewSubObject(subNum);
                            subObjects.set(subNum-1, subObject );
                        }
                    } catch (B3pCommonsException me) {}
                    if (log.isDebugEnabled())
                        log.debug(" create new");
//                errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.new"));
                } else {
                    errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
                }
                newAction = EDIT_ACTION;
            }
            // bewaar dit record
            if (buttonPressed(SAVE_BUTTON)) {
                if (allowEdits) {
                    if (directSave) {
                        ActionForward p = saveAction(validateErrors);
                        if (p!=null)
                            return p;
                    } else {
                        if (reduceMainErrors(validateErrors)) {
                            if (log.isInfoEnabled())
                                log.info("Validation error!");
                            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.savewitherrors"));
                        } else {
                            if (theObject!=null) {
                                if (log.isDebugEnabled())
                                    log.debug(" save existing");
                                errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.save"));
                            } else {
                                if (log.isDebugEnabled())
                                    log.debug(" create new");
                                errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.new"));
                            }
                        }
                        newAction = SAVE_ACTION;
                    }
                } else {
                    errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
                }
            }
            // delete dit record
            if (buttonPressed(DELETE_BUTTON)) {
                if (allowEdits) {
                    if (directDelete) {
                        ActionForward p = deleteAction();
                        if (p!=null)
                            return p;
                    } else {
                        if (theObject!=null) {
                            if (log.isDebugEnabled())
                                log.debug(" delete");
                            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.delete"));
                            newAction = DELETE_ACTION;
                        } else {
                            if (log.isDebugEnabled())
                                log.debug(" delete, but no record");
                            errors.add(MAIN_MESSAGE, new ActionError("error.invoerenrecord.nodelete"));
                        }
                    }
                } else {
                    errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
                }
            }
            // New, delete, join or disjoin voor gekoppelde objecten
            if (theObject!=null) {
                for (int subNum=1; subNum<=numOfSubs; subNum++) {
                    Object subObject =(Object) subObjects.get(subNum-1);
                    
                    // creeer nieuw subrecord
                    String subNewButton = SUBNEW_BUTTON + subNum;
                    if (buttonPressed(subNewButton)) {
                        if (allowEdits) {
                            try {
                                // merker dat record nieuw is, wordt later op gecheckt
                                setSubID(subNum, Integer.toString(TEMPNEW_ID));
                                subObject = getNewSubObject(subNum);
                            } catch (B3pCommonsException me) {}
                            subObjects.set(subNum-1, subObject );
//                        errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subnew", getSubNames(subNum)));
                            newAction = SUBEDIT_ACTION + subNum;
                        } else {
                            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.notallowed"));
                            newAction = EDIT_ACTION;
                        }
                    }
                    // edit dit subrecord
                    String subEditButton = SUBEDIT_BUTTON + subNum;
                    if (buttonPressed(subEditButton)) {
                        if (subObject!=null) {
                            if (log.isDebugEnabled())
                                log.debug(" edit subrecord");
//                            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subedit", getSubNames(subNum)));
                            newAction = SUBDELETE_ACTION + subNum;
                        } else {
                            if (log.isDebugEnabled())
                                log.debug(" edit subrecord, but no subrecord present");
//                            errors.add(SUB_MESSAGE + subNum, new ActionError("error.invoerenrecord.nosubedit", getSubNames(subNum)));
                        }
                        newAction = SUBEDIT_ACTION + subNum;
                    }
                    
                    // check of newjoin knop is gedrukt
                    String newJoinButton = NEWJOIN_BUTTON + subNum;
                    if (buttonPressed(newJoinButton) && subObject!=null) {
                        if (allowEdits) {
                            try {
                                createJoin(subNum);
                                if (log.isInfoEnabled())
                                    log.info(" Aanmaken join tussen: " + theObject.toString() + " en: " + subObject.toString());
                            } catch (Exception jdoe) {
                                if (log.isErrorEnabled())
                                    log.error(" Database error creating join: ", jdoe);
                                errors.add(MAIN_MESSAGE, new ActionError("error.invoerenrecord.addjoin", new Integer(subNum), jdoe.getMessage()));
                                return (mapping.findForward("failure"));
                            }
                        } else {
                            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
                            newAction = EDIT_ACTION;
                        }
                    }
                    // check of deletejoin knop is gedrukt
                    String deleteJoinButton = DELETEJOIN_BUTTON + subNum;
                    if (buttonPressed(deleteJoinButton) && subObject!=null) {
                        if (allowEdits) {
                            try {
                                deleteJoin(subNum);
                                if (log.isInfoEnabled())
                                    log.info(" Verwijderen join tussen: " + theObject.toString() + " en: " + subObject.toString());
                            } catch (Exception jdoe) {
                                if (log.isErrorEnabled())
                                    log.error(" Database error deleting join: ", jdoe);
                                errors.add(MAIN_MESSAGE, new ActionError("error.invoerenrecord.deletejoin", new Integer(subNum), jdoe.getMessage()));
                                return (mapping.findForward("failure"));
                            }
                        } else {
                            errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
                            newAction = EDIT_ACTION;
                        }
                    }
                }
            }
            if (buttonPressed(CANCEL_BUTTON)) {
                if (log.isInfoEnabled())
                    log.info(" Cancel edit action????");
                newAction = EDIT_ACTION;
            }
        }
        return null;
    }
    
    /**
     * Deze functie zorgt ervoor dat een subrecord bewerkt kan worden.
     * <p>
     * @return niet null bij fouten
     * @param subNum rangnummer formulier
     * @param validateErrors foutenmeldingen van formulier
     */
    protected ActionForward doSubEditAction(int subNum, ActionErrors validateErrors) {
        
        Object subObject =(Object) subObjects.get(subNum-1);
        
        String subEditAction = SUBEDIT_ACTION + subNum;
        // Bij subedit mode worden new en delete omgeleid met nieuwe action
        if (isAction(subEditAction)) {
            // Was this transaction a subSave?
            String subSaveButton = SUBSAVE_BUTTON + subNum;
            if (buttonPressed(subSaveButton)) {
                if (allowEdits) {
                    if (directSubSave) {
                        ActionForward p = subSaveAction(subNum, validateErrors);
                        if (p!=null)
                            return p;
                    } else {
                        // validatie
                        if (reduceSubErrors(subNum, validateErrors)) {
                            if (log.isInfoEnabled())
                                log.info("Validation error subform!");
                            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subsavewitherrors", getSubNames(subNum)));
                        } else {
                            if (subObject!=null) {
                                if (log.isDebugEnabled())
                                    log.debug(" subsave existing");
                                errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subsave", getSubNames(subNum)));
                            } else {
                                if (log.isDebugEnabled())
                                    log.debug(" create subnew");
                                errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subnew", getSubNames(subNum)));
                            }
                        }
                        newAction = SUBSAVE_ACTION +subNum;
                    }
                } else {
                    errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.notallowed"));
                }
            }
            
            // creeer nieuw subrecord
            String subNewButton = SUBNEW_BUTTON + subNum;
            if (buttonPressed(subNewButton)) {
                if (allowEdits) {
                    try {
                        // merker dat record nieuw is, wordt later op gecheckt
                        setSubID(subNum, Integer.toString(TEMPNEW_ID));
                        subObject = getNewSubObject(subNum);
                    } catch (B3pCommonsException me) {}
                    subObjects.set(subNum-1, subObject );
//                        errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subnew", getSubNames(subNum)));
                    newAction = SUBEDIT_ACTION + subNum;
                } else {
                    errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.notallowed"));
                }
            }
            
            // delete dit subrecord
            String subDeleteButton = SUBDELETE_BUTTON + subNum;
            if (buttonPressed(subDeleteButton)) {
                if (allowEdits) {
                    if (directSubDelete) {
                        ActionForward p = subDeleteAction(subNum);
                        if (p!=null)
                            return p;
                    } else {
                        if (subObject!=null) {
                            if (log.isDebugEnabled())
                                log.debug(" delete subrecord");
                            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subdelete", getSubNames(subNum)));
                            newAction = SUBDELETE_ACTION + subNum;
                        } else {
                            if (log.isDebugEnabled())
                                log.debug(" delete subrecord, but no subrecord present");
                            errors.add(SUB_MESSAGE + subNum, new ActionError("error.invoerenrecord.nosubdelete", getSubNames(subNum)));
                            newAction = EDIT_ACTION;
                        }
                    }
                } else {
                    errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.notallowed"));
                }
            }
            
            // cancel of subedit
            if (buttonPressed(CANCEL_BUTTON)) {
                if (log.isInfoEnabled())
                    log.info(" Cancel subedit action");
                try {
                    // Bij annuleren worden de velden dan gereset via nieuw subobject
                    subObject = getNewSubObject(subNum);
                    subObjects.set(subNum-1,  subObject);
                    setSubID(subNum, "");
                } catch (Exception dbe) {
                    if (log.isErrorEnabled())
                        log.error("  Database error creating object: ", dbe);
                    errors.add(SUB_MESSAGE + subNum, new ActionError("error.database", dbe.getMessage()));
                    return (mapping.findForward("failure"));
                }
                newAction = EDIT_ACTION;
            }
        }
        return null;
    }
    
    /**
     * Deze functie controleert voor alle subrecords of er een gewist moet worden.
     * <p>
     * @return niet null bij fouten
     * @param subNum rangnummer formulier
     */
    protected ActionForward doSubDeleteAction(int subNum) {
        Object subObject =(Object) subObjects.get(subNum-1);
        // Was this transaction a subDelete?
        String subEditAction = SUBEDIT_ACTION + subNum;
        String subDeleteAction = SUBDELETE_ACTION + subNum;
        if (subDeleteAction.equals(action) && buttonPressed(OK_BUTTON) && subObject!=null) {
            if (allowEdits) {
                return subDeleteAction(subNum);
            } else {
                errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.notallowed"));
                newAction = EDIT_ACTION;
            }
        }
        if (subDeleteAction.equals(action) && buttonPressed(CANCEL_BUTTON)) {
            if (log.isInfoEnabled())
                log.info(" Cancel subdelete action");
            try {
                // Bij annuleren worden de velden dan gereset via nieuw subobject
                subObject = getNewSubObject(subNum);
                subObjects.set(subNum-1,  subObject);
                setSubID(subNum, "");
            } catch (Exception dbe) {
                if (log.isErrorEnabled())
                    log.error("  Database error creating object: ", dbe);
                errors.add(SUB_MESSAGE + subNum, new ActionError("error.database", dbe.getMessage()));
                return (mapping.findForward("failure"));
            }
            newAction = EDIT_ACTION;
        }
        return null;
    }
    
    /**
     * Deze functie wist een subrecord.
     * <p>
     * @return niet null bij fouten
     * @param subNum rangnummer van formulier
     */
    protected ActionForward subDeleteAction(int subNum) {
        Object subObject =(Object) subObjects.get(subNum-1);
        if (log.isInfoEnabled())
            log.info(" deleting subrecord '" + subObject.toString() + "'");
        try {
            deleteSubObject(subNum);
            // HACK: door een leeg object door te geven wordt dit subformulier gewist
            subObject = getNewSubObject(subNum);
            subObjects.set(subNum-1, subObject );
            setSubID(subNum, null);
        } catch (B3pCommonsException dbe) {
            if (log.isErrorEnabled())
                log.error("  Database error deleting: ", dbe);
            errors.add(SUB_MESSAGE + subNum, new ActionError("error.database", dbe.getMessage()));
            return (mapping.findForward("failure"));
        }
        newAction = EDIT_ACTION;
        
        if (directSubDelete)
            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subdeletedone", getSubNames(subNum)));
        
        return null;
    }
    
    /**
     * Deze functie controleert voor alle subrecords of er een gesaved moet worden.
     * <p>
     * @return niet null bij fouten
     * @param subNum rangnummer van formulier
     * @param validateErrors foutenmeldingen van formulier
     */
    protected ActionForward doSubSaveAction(int subNum, ActionErrors validateErrors) {
        Object subObject =(Object) subObjects.get(subNum-1);
        String subSaveAction = SUBSAVE_ACTION + subNum;
        if (subSaveAction.equals(action) && buttonPressed(OK_BUTTON)) {
            if (allowEdits) {
                return subSaveAction(subNum, validateErrors);
            } else {
                errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.notallowed"));
                newAction = EDIT_ACTION;
            }
        }
        if (subSaveAction.equals(action) && buttonPressed(CANCEL_BUTTON)) {
            if (log.isInfoEnabled())
                log.info(" Cancel subsave action");
            try {
                // Bij annuleren worden de velden dan gereset via nieuw subobject
                subObject = getNewSubObject(subNum);
                subObjects.set(subNum-1,  subObject);
                setSubID(subNum, "");
            } catch (Exception dbe) {
                if (log.isErrorEnabled())
                    log.error("  Database error creating object: ", dbe);
                errors.add(SUB_MESSAGE + subNum, new ActionError("error.database", dbe.getMessage()));
                return (mapping.findForward("failure"));
            }
            newAction = EDIT_ACTION;
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
    protected ActionForward subSaveAction(int subNum, ActionErrors validateErrors) {
        Object subObject =(Object) subObjects.get(subNum-1);
        String subEditAction = SUBEDIT_ACTION + subNum;
        String subSaveAction = SUBSAVE_ACTION + subNum;
        // validatie
        if (reduceSubErrors(subNum, validateErrors)) {
            if (log.isInfoEnabled())
                log.info("Validation error subform!");
            errors.add(SUB_MESSAGE + subNum, new ActionError("warning.invoerenrecord.subsavewitherrors", getSubNames(subNum)));
            newAction = subSaveAction;
        } else {
            try {
                // Een nieuw subobject wordt alleen aangemaakt indien
                // in het form een merker is aangebracht die dit aangeeft.
                if (subObject == null && Integer.toString(TEMPNEW_ID).equals(getSubID(subNum))) {
                    subObject = getNewSubObject(subNum);
                    subObjects.set(subNum-1,  subObject);
                }
                if (subObject!=null) {
                    if (log.isInfoEnabled())
                        log.info(" Adding Subrecord " + subNum + " '" + subObject.toString() + "'");
                    populateSubObject(subNum);
                    syncSubID(subNum);
                }
            } catch (Exception dbe) {
                if (log.isErrorEnabled())
                    log.error("  Database error creating object: ", dbe);
                errors.add(SUB_MESSAGE + subNum, new ActionError("error.database", dbe.getMessage()));
                return (mapping.findForward("failure"));
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
     * Deze functie checkt op de SAVE_ACTION moet worden uitgevoerd. Er wordt dan
     * gecontroleerd of struts fouten in de invoer van het formulier heeft geconstateerd.
     * Als dit zo is wordt verder verwerking afgebroken en gaat men meteen terug naar het
     * formulier. Als er geen fouten zijn dan wordt de database gevuld met de informatie
     * in het formulier.
     * Dit is een alternatief voor de doSaveAction, waarbij alle subrecords ook worden
     * gesaved.
     * <p>
     * @param validateErrors zoals deze door struts zijn vastgesteld
     * @return niet null bij validatie errors
     */
    protected ActionForward doSaveAllAction(ActionErrors validateErrors) {
        if (isAction(SAVE_ACTION) && buttonPressed(OK_BUTTON)) {
            if (allowEdits) {
                return saveAllAction(validateErrors);
            } else {
                errors.add(MAIN_MESSAGE, new ActionError("warning.invoerenrecord.notallowed"));
                newAction = EDIT_ACTION;
            }
        }
        if (isAction(SAVE_ACTION) && buttonPressed(CANCEL_BUTTON)) {
            if (log.isInfoEnabled())
                log.info(" Cancel save action");
            newAction = EDIT_ACTION;
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
    protected ActionForward saveAllAction(ActionErrors validateErrors) {
        // validatie
        if (validateErrors!=null && !validateErrors.isEmpty()) {
            if (log.isInfoEnabled())
                log.info("Validation error!");
            errors.add(validateErrors);
            return (mapping.findForward("success"));
        }
        try {
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
                if (log.isDebugEnabled())
                    log.debug(" Populating database object and subobjects from form bean");
            }
        } catch (Exception dbe) {
            if (log.isErrorEnabled())
                log.error("  Database error creating object: ", dbe);
            errors.add(MAIN_MESSAGE, new ActionError("error.database", dbe.getMessage()));
            return (mapping.findForward("failure"));
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
    protected ActionForward populateMainForm() {
        if (theObject==null)
            return null;
        try {
            // Bij save action moet het form niet opnieuw gevuld worden
            
            if (!isNewAction(SAVE_ACTION)) {
                populateForm();
                if (log.isDebugEnabled())
                    log.debug(" Populating form from " + theObject.toString());
            }
        } catch (B3pCommonsException pe) {
            errors.add(MAIN_MESSAGE, new ActionError("error.invoerenrecord.general"));
            return mapping.findForward("failure");
        }
        return null;
    }
    
    /**
     * Deze functie vult een subformulieren met
     * de bijgewerkte informatie uit de database.
     * <p>
     * @return niet null bij fouten
     */
    protected ActionForward populateSubForms(int subNum) {
        Object subObject =(Object) subObjects.get(subNum-1);
        if (subObject==null)
            return null;
        String subSaveAction = SUBSAVE_ACTION + subNum;
        try {
            // Bij subsave action moet het subform niet opnieuw gevuld worden
            if (!isNewAction(subSaveAction)) {
                populateSubForm(subNum);
                if (log.isDebugEnabled())
                    log.debug(" Populating subform from " + subObject.toString());
            }
        } catch (B3pCommonsException pe) {
            errors.add(SUB_MESSAGE + subNum, new ActionError("error.invoerenrecord.general"));
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
    protected void determineNewAction() {
        try {
            setForm("action", newAction);
        } catch (B3pCommonsException pe) {
            errors.add(MAIN_MESSAGE, new ActionError("error.invoerenrecord.general"));
        }
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
