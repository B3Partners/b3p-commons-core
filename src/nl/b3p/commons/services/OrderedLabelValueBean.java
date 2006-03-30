
package nl.b3p.commons.services;

import java.util.*;

/**
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.3 $ $Date: 2004/09/22 08:53:58 $
 */

public class OrderedLabelValueBean extends Object implements java.io.Serializable, Comparable {
    
    protected String ordering = null;
    protected String label = null;
    protected String value = null;
    protected String selected = null;
    protected String param1 = null;
    protected String param2 = null;
    protected String param3 = null;
    protected String param4 = null;
    protected String param5 = null;
    protected String param6 = null;
    protected String param7 = null;
    protected String param8 = null;
    protected String param9 = null;
    protected String visible = null;
    
    // ----------------------------------------------------------- Constructors
    public OrderedLabelValueBean() {
    }
    
    public OrderedLabelValueBean(String ordering, String label, String value, String selected) {
        this.ordering = ordering;
        this.label = label;
        this.value = value;
        this.selected = selected;
    }
    
    public OrderedLabelValueBean(String ordering, String label, String value, String selected,
    String param1) {
        this.ordering = ordering;
        this.selected = selected;
        this.label = label;
        this.value = value;
        this.param1 = param1;
    }
    
    public OrderedLabelValueBean(String ordering, String label, String value, String selected,
    String param1, String param2) {
        this.ordering = ordering;
        this.selected = selected;
        this.label = label;
        this.value = value;
        this.param1 = param1;
        this.param2 = param2;
    }
    
    public OrderedLabelValueBean(String ordering, String label, String value, String selected,
    String param1, String param2, String param3) {
        this.ordering = ordering;
        this.selected = selected;
        this.label = label;
        this.value = value;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }
    
    public OrderedLabelValueBean(String ordering, String label, String value, String selected,
    String param1, String param2, String param3, String param4) {
        this.ordering = ordering;
        this.selected = selected;
        this.label = label;
        this.value = value;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }
    
    public OrderedLabelValueBean(String ordering, String label, String value, String selected,
    String param1, String param2, String param3, String param4, String param5) {
        this.ordering = ordering;
        this.selected = selected;
        this.label = label;
        this.value = value;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
    }
    
    public OrderedLabelValueBean(String ordering, String label, String value, String selected,
    String param1, String param2, String param3, String param4, String param5, String param6) {
        this.ordering = ordering;
        this.selected = selected;
        this.label = label;
        this.value = value;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
        this.param6 = param6;
    }
    
    // ------------------------------------------------------------- Properties
    
    /** Getter for property ordering.
     * @return Value of property ordering.
     */
    public String getOrdering() {
        return ordering;
    }
    
    /** Setter for property ordering.
     * @param ordering New value of property ordering.
     */
    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }
    
    public String getLabel() {
        return (this.label);
    }
    /** Setter for property label.
     * @param label New value of property label.
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }
    
    public String getValue() {
        return (this.value);
    }
    
    /** Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }
    
    public String getSelected() {
        return (this.selected);
    }
    /** Setter for property selected.
     * @param selected New value of property selected.
     */
    public void setSelected(java.lang.String selected) {
        this.selected = selected;
    }
    
    /** Getter for property param1.
     * @return Value of property param1.
     */
    public java.lang.String getParam1() {
        return param1;
    }
    
    /** Setter for property param1.
     * @param param1 New value of property param1.
     */
    public void setParam1(java.lang.String param1) {
        this.param1 = param1;
    }
    
    /** Getter for property param2.
     * @return Value of property param2.
     */
    public java.lang.String getParam2() {
        return param2;
    }
    
    /** Setter for property param2.
     * @param param2 New value of property param2.
     */
    public void setParam2(java.lang.String param2) {
        this.param2 = param2;
    }
    
    /** Getter for property param3.
     * @return Value of property param3.
     */
    public java.lang.String getParam3() {
        return param3;
    }
    
    /** Setter for property param3.
     * @param param3 New value of property param3.
     */
    public void setParam3(java.lang.String param3) {
        this.param3 = param3;
    }
    
    /** Getter for property param4.
     * @return Value of property param4.
     */
    public java.lang.String getParam4() {
        return param4;
    }
    
    /** Setter for property param4.
     * @param param4 New value of property param4.
     */
    public void setParam4(java.lang.String param4) {
        this.param4 = param4;
    }
    
    /** Getter for property param5.
     * @return Value of property param5.
     */
    public java.lang.String getParam5() {
        return param5;
    }
    
    /** Setter for property param5.
     * @param param5 New value of property param5.
     */
    public void setParam5(java.lang.String param5) {
        this.param5 = param5;
    }
    
    /** Getter for property param6.
     * @return Value of property param6.
     */
    public java.lang.String getParam6() {
        return param6;
    }
    
    /** Setter for property param6.
     * @param param6 New value of property param6.
     */
    public void setParam6(java.lang.String param6) {
        this.param6 = param6;
    }
    
    /** Getter for property param7.
     * @return Value of property param7.
     *
     */
    public java.lang.String getParam7() {
        return param7;
    }
    
    /** Setter for property param7.
     * @param param7 New value of property param7.
     *
     */
    public void setParam7(java.lang.String param7) {
        this.param7 = param7;
    }
    
    /** Getter for property param8.
     * @return Value of property param8.
     *
     */
    public java.lang.String getParam8() {
        return param8;
    }
    
    /** Setter for property param8.
     * @param param8 New value of property param8.
     *
     */
    public void setParam8(java.lang.String param8) {
        this.param8 = param8;
    }
    
    /** Getter for property param9.
     * @return Value of property param9.
     *
     */
    public java.lang.String getParam9() {
        return param9;
    }
    
    /** Setter for property param9.
     * @param param9 New value of property param9.
     *
     */
    public void setParam9(java.lang.String param9) {
        this.param9 = param9;
    }
    
    /** Getter for property visible.
     * @return Value of property visible.
     *
     */
    public java.lang.String getVisible() {
        return visible;
    }
    
    /** Setter for property visible.
     * @param visible New value of property visible.
     *
     */
    public void setVisible(java.lang.String visible) {
        this.visible = visible;
    }
    
    // --------------------------------------------------------- Public Methods
    
    /**
     * Return a string representation of this object.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("OrderedLabelValueBean[");
        sb.append(this.ordering);
        sb.append(", ");
        sb.append(this.label);
        sb.append(", ");
        sb.append(this.value);
        sb.append(", ");
        sb.append(this.selected);
        sb.append(", ");
        sb.append(this.param1);
        sb.append(", ");
        sb.append(this.param2);
        sb.append(", ");
        sb.append(this.param3);
        sb.append(", ");
        sb.append(this.param4);
        sb.append(", ");
        sb.append(this.param5);
        sb.append(", ");
        sb.append(this.param6);
        sb.append(", ");
        sb.append(this.param7);
        sb.append(", ");
        sb.append(this.param8);
        sb.append(", ");
        sb.append(this.param9);
        sb.append(", ");
        sb.append(this.visible);
        sb.append("] ");
        return (sb.toString());
    }
    
    public boolean equals(Object o) {
        if (o==null)
            return false;
        if (!(o instanceof OrderedLabelValueBean) )
            return false;
        OrderedLabelValueBean n = (OrderedLabelValueBean)o;
        
        String nString = n.toString();
        String tString = this.toString();
        return nString.equals(tString);
    }
    
    public int hashCode() {
        String tString = this.toString();
        return tString.hashCode();
    }
    
    public int compareTo(Object o) {
        if (o==null)
            return -1;
        if (!(o instanceof OrderedLabelValueBean) )
            return -1;
        OrderedLabelValueBean n = (OrderedLabelValueBean)o;
        
        String nString = n.toString();
        String tString = this.toString();
        
        return tString.compareTo(nString);
    }
    
    // Deze functie gaat er van uit dat het ordering veld altijd
    // overschreven mag worden
    static public ArrayList sortList(ArrayList theList, String fieldName, boolean reverse) {
        if (theList==null || fieldName == null)
            return null;
        
        OrderedLabelValueBean thisBean = null;
        Iterator it = theList.iterator();
        while (it.hasNext()) {
            thisBean = (OrderedLabelValueBean) it.next();
            if (fieldName.equals("label")) {
                thisBean.setOrdering(thisBean.getLabel());
            } else if (fieldName.equals("value")) {
                thisBean.setOrdering(thisBean.getValue());
            } else if (fieldName.equals("selected")) {
                thisBean.setOrdering(thisBean.getSelected());
            } else if (fieldName.equals("param1")) {
                thisBean.setOrdering(thisBean.getParam1());
            } else if (fieldName.equals("param2")) {
                thisBean.setOrdering(thisBean.getParam2());
            } else if (fieldName.equals("param3")) {
                thisBean.setOrdering(thisBean.getParam3());
            } else if (fieldName.equals("param4")) {
                thisBean.setOrdering(thisBean.getParam4());
            } else if (fieldName.equals("param5")) {
                thisBean.setOrdering(thisBean.getParam5());
            } else if (fieldName.equals("param6")) {
                thisBean.setOrdering(thisBean.getParam6());
            } else if (fieldName.equals("param7")) {
                thisBean.setOrdering(thisBean.getParam7());
            } else if (fieldName.equals("param8")) {
                thisBean.setOrdering(thisBean.getParam8());
            } else if (fieldName.equals("param9")) {
                thisBean.setOrdering(thisBean.getParam9());
            } else if (fieldName.equals("visible")) {
                thisBean.setOrdering(thisBean.getVisible());
            }
            
        }
        
        // reverse of niet
        Collections.sort(theList);
        if (reverse)
            Collections.reverse(theList);
        
        return null;
    }
    
}
