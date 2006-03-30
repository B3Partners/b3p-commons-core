/*
 * $Id: MenuItem.java 1654 2005-10-06 12:20:28Z Matthijs $
 */

package nl.b3p.commons.services;

import java.util.*;

public class MenuItem {
    
    private String label;
    private String url;
    private boolean selected;
    private boolean enabled = true;
    private Map properties = new HashMap();
    
    private MenuItem parent = null;
    private List subItems = null;    
    
    public MenuItem() {
    }
    
    public MenuItem(String label, String url) {
        this();
        this.setLabel(label);
        this.setUrl(url);
        afterConstructed();
    }
    
    public MenuItem(String label, String url, boolean enabled) {
        this(label, url);
        this.setEnabled(enabled);
    }
    
    /* Voor subclasses om extra initialisatie te doen zonder elke constructor te
     * moeten overriden (alleen deze methode hoeft te worden overridden)
     */
    protected void afterConstructed() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public List getSubItems() {
        return subItems;
    }

    public void setSubItems(List subItems) {
        this.subItems = subItems;
        
        for(Iterator i = subItems.iterator(); i.hasNext();) {
            MenuItem item = (MenuItem)i.next();
            item.setParent(this);
        }
    }

    public MenuItem getParent() {
        return parent;
    }

    public void setParent(MenuItem parent) {
        this.parent = parent;
    }
}
