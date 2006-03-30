/*
 * FormBaseBean.java
 *
 * Created on 3 april 2005, 16:07
 */

package nl.b3p.commons.services;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Deze class slaat de informatie van een struts formulier op samen met alle bijkomende
 * informatie uit de request. Vervolgens kan de request bewerkt worden. Deze class
 * moet nog overklast worden voor het concrete werk.
 * <p>
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.0 $ $Date: 2005/05/17 12:48:31 $
 */

public abstract class FormBaseBean {
    
    protected Log log = LogFactory.getLog(this.getClass());
    
    protected String action = null;
    
    protected ActionMessages errors = null;
    protected HttpSession session = null;
    protected HttpServletRequest request = null;
    protected Locale locale = null;
    protected MessageResources messages = null;
    protected DynaValidatorForm form = null;
    protected Map requestParams = null;
    protected ActionMapping mapping = null;
    
    protected boolean isInit = false;
    
    public static final String UNKNOWN_ACTION = "Unknown";
    
    /**
     * Deze minimale constructor kent geen locale instelling, geen
     * message resources en geen foutterugmelding en zal niet vaak
     * gebruikt worden.
     * <p>
     * @param req de request waarmee het struts formulier gepost is
     * @param dform het struts formulier zelf
     * @param mapp de mapping voor deze struts action
     */
    public FormBaseBean(HttpServletRequest req,
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
    public FormBaseBean(HttpServletRequest req,
            Locale loc,
            MessageResources mess,
            ActionMessages err,
            DynaValidatorForm dform,
            ActionMapping mapp) {
        
        this.request = req;
        this.session = req.getSession();
        
        if (req instanceof MultipartRequestWrapper) {
            MultipartRequestWrapper mpr = (MultipartRequestWrapper) req;
            Enumeration mprenum = mpr.getParameterNames();
            Map tempMap = new HashMap();
            while (mprenum.hasMoreElements()) {
                String param = (String) mprenum.nextElement();
                tempMap.put(param, mpr.getParameter(param));
            }
            this.requestParams = tempMap;
        } else
            this.requestParams = new HashMap(req.getParameterMap());
        
        this.locale = loc;
        this.messages = mess;
        this.errors = err;
        this.form = dform;
        this.mapping = mapp;
        
        if (this.errors==null)
            this.errors = new ActionErrors();
        if (this.locale==null)
            this.locale = Locale.getDefault();
        if (this.messages==null)
            this.messages = MessageResources.getMessageResources("nl.b3p.commons.services.FormBaseStrings");
        
        this.setAction(UNKNOWN_ACTION);
        
        this.isInit=true;
    }
    
    /**
     * getter voor action
     * @return geeft de huidige actie
     */
    public String getAction() {
        return action;
    }
    
    /**
     * setter voor action
     * @param action zet een nieuwe actie
     */
    protected void setAction(String action) {
        this.action = action;
    }
    
    /**
     * test of een actie de huidige actie is
     * @param lact te testen actie
     * @return true indien huidige actie gelijk is aan de te testen actie
     */
    protected boolean isAction(String lact) {
        if (lact==null)
            return false;
        return lact.equals(getAction());
    }
    
    /**
     * gemaksfunctie die test of een knop met een bepaalde waarde in de request voorkomt en
     * een waarde heeft; hiermee wordt vastgesteld dat deze knop geklikt is.
     * <p>
     * @param butt naam van de te testen knop
     * @return true indien de knop geklikt is
     */
    protected boolean buttonPressed(String butt) {
        if (butt==null)
            return false;
        String butval = null;
        try {
            butval = getParamAsString(butt);
        } catch (B3pCommonsException be) {}
        return !nullOrEmpty(butval);
    }
    
    /**
     * gemaksfunctie die test of een string niet null of leeg is.
     * @param astr te testen string
     * @return true indien leeg of null
     */
    protected static boolean nullOrEmpty(String astr) {
        return (astr == null || astr.length()==0);
    }
    
    /**
     * De parameters uit de request zijn opgenomen tijdens de constructie van de class in een Map.
     * Deze functie haalt de waarde op van een bepaalde parameter. Indien de parameter meerdere
     * waarden heeft, wordt alleen de eerste doorgegeven.
     * <p>
     * @param param naam van de parameter
     * @return waarde van de parameter
     */
    protected String getParamAsString(String param) throws B3pCommonsException {
        Object value = getParamAsObject(param);
        if (value==null)
            return null;
        if (value instanceof String)
            return (String) value;
        if (value instanceof String[]) {
            String[] sa = (String[]) value;
            if (sa.length>0)
                return sa[0];
            else
                return null;
        }
        throw new B3pCommonsException("Verkeerde functie aanroep: getParamAsString bij param: " + (param==null?"?":param));
    }
    
    /**
     * De parameters uit de request zijn opgenomen tijdens de constructie van de class in een Map.
     * Deze functie haalt de waarde op van een bepaalde parameter. Normaal betreft het een String
     * array en deze functie cast daar naar toe.
     * <p>
     * @param param naam van de parameter
     * @return waarde van de parameter
     */
    protected String[] getParamAsStringArray(String param) throws B3pCommonsException {
        Object value = getParamAsObject(param);
        if (!(value instanceof String[]) && value!=null)
            throw new B3pCommonsException("Verkeerde functie aanroep: getParamAsStringArray bij param: " + (param==null?"?":param));
        return (String[]) value;
    }
    
    /**
     * De parameters uit de request zijn opgenomen tijdens de constructie van de class in een Map.
     * Deze functie haalt de waarde op van een bepaalde parameter.
     * <p>
     * @param param naam van de parameter
     * @return waarde van de parameter
     */
    protected Object getParamAsObject(String param) {
        return requestParams.get(param);
    }
    
    /**
     *
     * Deze gemaksfunctie haalt een waarde voor een formulierveld op en cast deze naar een String.
     *
     * <p>
     *
     * @param param naam van formulierveld
     *
     * @throws B3pCommonsException indien er iets fout gaat bij het ophalen de waarde van het formulierveld (bv als het veld niet bestaat)
     *
     * @return waarde van het formulierveld als String
     *
     */
    protected String getForm(String param) throws B3pCommonsException {
        Object obj = getFormAsObject(param);
        if (!(obj instanceof String) && obj!=null)
            throw new B3pCommonsException("Verkeerde functie aanroep: getForm bij param: " + (param==null?"?":param));
        return (String) obj;
    }
    
    /**
     *
     * Deze gemaksfunctie haalt een waarde voor een formulierveld op en cast deze naar een String Array.
     *
     * <p>
     *
     * @param param naam van formulierveld
     *
     * @throws B3pCommonsException indien er iets fout gaat bij het ophalen de waarde van het formulierveld (bv als het veld niet bestaat)
     *
     * @return waarde van het formulierveld als String
     *
     */
    protected String[] getFormAsStringArray(String param) throws B3pCommonsException {
        Object obj = getFormAsObject(param);
        if (!(obj instanceof String[]) && obj!=null)
            throw new B3pCommonsException("Verkeerde functie aanroep: getFormAsStringArray bij param: " + (param==null?"?":param));
        return (String[]) obj;
    }
    
    /**
     *
     * Deze gemaksfunctie haalt een waarde voor een formulierveld op en cast deze naar een Integer.
     *
     * <p>
     *
     * @param param naam van formulierveld
     *
     * @throws B3pCommonsException indien er iets fout gaat bij het ophalen de waarde van het formulierveld (bv als het veld niet bestaat)
     *
     * @return waarde van het formulierveld als Integer
     *
     */
    protected Integer getFormAsInteger(String param) throws B3pCommonsException {
        Object obj = getFormAsObject(param);
        if (!(obj instanceof Integer) && obj!=null)
            throw new B3pCommonsException("Verkeerde functie aanroep: getFormAsInteger bij param: " + (param==null?"?":param));
        return (Integer) obj;
    }
    
    /**
     *
     * Deze gemaksfunctie haalt een waarde voor een formulierveld op en cast deze naar een FormFile.
     *
     * Een FormFile is een struts object waarmee een multipart formulier kan worden opgehaald.
     *
     * <p>
     *
     * @param param naam van formulierveld
     *
     * @throws B3pCommonsException indien er iets fout gaat bij het ophalen de waarde van het formulierveld (bv als het veld niet bestaat)
     *
     * @return waarde van het formulierveld als String
     *
     */
    protected FormFile getFormAsFormFile(String param) throws B3pCommonsException {
        Object obj = getFormAsObject(param);
        if (!(obj instanceof FormFile) && obj!=null)
            throw new B3pCommonsException("Verkeerde functie aanroep: getFormAsFormFile bij param: " + (param==null?"?":param));
        return (FormFile) obj;
    }
    
    /**
     *
     * Deze gemaksfunctie haalt een waarde voor een formulierveld op en cast deze naar een boolean.
     *
     * <p>
     *
     * @param param naam van formulierveld
     *
     * @throws B3pCommonsException indien er iets fout gaat bij het ophalen de waarde van het formulierveld (bv als het veld niet bestaat)
     *
     * @return waarde van het formulierveld als boolean
     *
     */
    protected boolean getFormAsBoolean(String param) throws B3pCommonsException {
        Object obj = getFormAsObject(param);
        if (!(obj instanceof Boolean) && obj!=null)
            throw new B3pCommonsException("Verkeerde functie aanroep: getFormAsBoolean bij param: " + (param==null?"?":param));
        Boolean bo = (Boolean) obj;
        return bo!=null && bo.booleanValue()? true : false;
    }
    
    /**
     *
     * Deze gemaksfunctie haalt een waarde voor een formulierveld op als Object. Deze functie wordt
     *
     * gebruikt door alle vergelijkbare functies in deze class.
     *
     * <p>
     *
     * Indien er fouten optreden, dan worden deze omgevormd naar een B3pCommonsException.
     *
     * <p>
     *
     * @param param naam van formulierveld
     *
     * @throws B3pCommonsException indien er iets fout gaat bij het ophalen de waarde van het formulierveld (bv als het veld niet bestaat)
     *
     * @return waarde van het formulierveld als String
     *
     */
    protected Object getFormAsObject(String param) throws B3pCommonsException {
        if (form==null)
            throw new B3pCommonsException("Form null error.");
        try {
            return form.get(param);
        } catch (java.lang.IllegalArgumentException iae) {
            if (log.isErrorEnabled())
                log.error("Dynaform get IllegalArgumentException ", iae);
            throw new B3pCommonsException("getForm Error: ",  iae);
        } catch (java.lang.NullPointerException npe) {
            if (log.isErrorEnabled())
                log.error("Dynaform get NullPointerException ", npe);
            throw new B3pCommonsException("getForm Error: ",  npe);
        } catch (java.lang.ClassCastException cce) {
            if (log.isErrorEnabled())
                log.error("Dynaform get ClassCastException ", cce);
            throw new B3pCommonsException("getForm Error: ",  cce);
        }
    }
    
    /**
     *
     * Deze gemaksfunctie plaatst een waarde in een struts formulierveld. Indien er
     *
     * fouten optreden dan worden deze omgevormd naar een B3pCommonsException.
     *
     * <p>
     *
     * @param param de naam van het formulierveld
     *
     * @param value de waarde van het formulierveld als Object
     *
     * @throws B3pCommonsException indien er iets fout gaat bij het bepalen van een waarden (bv als een veld niet bestaat)
     *
     */
    protected void setForm(String param, Object value) throws B3pCommonsException {
        try {
            form.set(param, value);
        } catch (java.lang.IllegalArgumentException iae) {
            if (log.isErrorEnabled())
                log.error("Dynaform get IllegalArgumentException ", iae);
            throw new B3pCommonsException("setForm Error: ",  iae);
        } catch (java.lang.NullPointerException npe) {
            if (log.isErrorEnabled())
                log.error("Dynaform get NullPointerException ", npe);
            throw new B3pCommonsException("setForm Error: ",  npe);
        } catch (java.lang.ClassCastException cce) {
            if (log.isErrorEnabled())
                log.error("Dynaform get ClassCastException ", cce);
            throw new B3pCommonsException("setForm Error: ",  cce);
        }
    }
    
}
