package nl.b3p.commons.services;

/**
 *
 * @author chris van lith
 */
public class B3PCredentials {
    private String userName = null;
    private String password = null;
    private String url = null;
    private boolean preemptive = false;
    
    public String getUserName(){
        return this.userName;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public void setPassword(String password){
        this.password   = password;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the preemptive
     */
    public boolean isPreemptive() {
        return preemptive;
    }

    /**
     * @param preemptive the preemptive to set
     */
    public void setPreemptive(boolean preemptive) {
        this.preemptive = preemptive;
    }
 }
