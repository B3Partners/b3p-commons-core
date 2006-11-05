package nl.b3p.commons.oai;

import nl.b3p.commons.oai.dataprovider20.Identity;


public class SimpleIdentity implements Identity {
    // these fields are loaded from property file
    protected String name;
    
    protected String OAIversion;
    
    // info for identify
    protected String adminemail;
    
    protected String id;
    
    protected String earliestDatestamp;
    
    protected String deletedItem;
    
    protected String granularity;
    
    // baseurl
    protected String baseurl;
    
    public SimpleIdentity() {
    }
    
    public String getOAIversion() {
        return OAIversion;
    }
    
    public String getBaseURL() {
        return baseurl;
    }
    
    public String getName() {
        return name;
    }
    
    public String getID() {
        return id;
    }
    
    public String getDelimiter() {
        return ":";
    }
    
    public String getSampleIdentifier() {
        StringBuffer sample = new StringBuffer();
        sample.append(getSchema());
        sample.append(getDelimiter());
        sample.append(getID());
        sample.append(getDelimiter());
        sample.append("b3p/100001");
        return sample.toString();
    }
    
    public String getSchema() {
        return "oai";
    }
    
    public String getAdminemail() {
        return adminemail;
    }
    
    public String getEarliestDatestamp() {
        return earliestDatestamp;
    }
    
    public String getDeletedItem() {
        return deletedItem;
    }
    
    public String getGranularity() {
        return granularity;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setOAIversion(String OAIversion) {
        this.OAIversion = OAIversion;
    }
    
    public void setAdminemail(String adminemail) {
        this.adminemail = adminemail;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setEarliestDatestamp(String earliestDatestamp) {
        this.earliestDatestamp = earliestDatestamp;
    }
    
    public void setDeletedItem(String deletedItem) {
        this.deletedItem = deletedItem;
    }
    
    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }
    
    public String getBaseurl() {
        return baseurl;
    }
    
    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }
    
}
