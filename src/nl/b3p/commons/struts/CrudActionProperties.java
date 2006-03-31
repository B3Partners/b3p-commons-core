/*
 * $Id: CrudActionProperties.java 2771 2006-03-28 13:29:24Z Chris $
 */

package nl.b3p.commons.struts;

public class CrudActionProperties extends ActionMethodProperties {
    /*
     * Het blokje default heeft betrekking op de normale afloop van
     * een actie, bv OK actie.
     * De forwardName is de struts forward waarnaar na afloop wordt
     * geforward.  De messageKey geeft een melding bij een normale 
     * afloop van de functie.
     * Indien er een redispatch plaatsvindt, dan worden de forwardName
     * en de messageKey bepaald door de nieuwe methode. Dus slechts
     * een van beide wordt gebruikt.
     */
    private String defaultForwardName = null;
    private String defaultMessageKey = null;
    
    /*
     * Het blokje alternate heeft meestal betrekking op een
     * abnormale afloop van een actie, bv cancel actie of fout
     */
    private String alternateForwardName = null;
    private String alternateMessageKey = null;
    
    public CrudActionProperties(String methodName) {
        super(methodName);
    }
    
    public CrudActionProperties(String methodName,
            String defaultForwardName,
            String alternateForwardName) {
        super(methodName);
        this.setDefaultForwardName(defaultForwardName);
        this.setAlternateForwardName(alternateForwardName);
    }
    
    public CrudActionProperties(String methodName,
            String defaultForwardName,
            String alternateForwardName,
            String defaultMessageKey,
            String alternateMessagekey) {
        super(methodName);
        this.setDefaultForwardName(defaultForwardName);
        this.setAlternateForwardName(alternateForwardName);
        this.setDefaultMessageKey(defaultMessageKey);
        this.setAlternateMessageKey(alternateMessagekey);
    }
    
    public String getDefaultForwardName() {
        return defaultForwardName;
    }
    
    public void setDefaultForwardName(String defaultForwardName) {
        this.defaultForwardName = defaultForwardName;
    }
    
    public String getAlternateForwardName() {
        return alternateForwardName;
    }
    
    public void setAlternateForwardName(String alternateForwardName) {
        this.alternateForwardName = alternateForwardName;
    }
    
    public String getDefaultMessageKey() {
        return defaultMessageKey;
    }
    
    public void setDefaultMessageKey(String defaultMessageKey) {
        this.defaultMessageKey = defaultMessageKey;
    }
    
    public String getAlternateMessageKey() {
        return alternateMessageKey;
    }
    
    public void setAlternateMessageKey(String alternateMessagekey) {
        this.alternateMessageKey = alternateMessagekey;
    }

}
