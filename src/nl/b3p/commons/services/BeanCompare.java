package nl.b3p.commons.services;

import java.util.*;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.PropertyUtils;

public class BeanCompare implements Comparator {
    
    private String compareField = null;
    
    public BeanCompare(String fieldName) {
        compareField = fieldName;
    }
    
    public int compare(Object obj, Object obj1) {
        Object compareObject = null;
        Object compareObject1 = null;
        try {
            if(compareField.indexOf(".") != -1) {
                String[] compareFields = compareField.split("\\.");
                Object compareSubObject = null;
                Object compareSubObject1 = null;
                String compareEndField = "";
                for(int i = 0; i < compareFields.length; i++) {
                    if((i-1) != -1) {
                        compareSubObject = PropertyUtils.getProperty(obj, compareFields[i-1]);
                        compareSubObject1 = PropertyUtils.getProperty(obj1, compareFields[i-1]);
                        compareEndField = compareFields[i];
                    }
                }
                compareObject = PropertyUtils.getProperty(compareSubObject, compareEndField);
                compareObject1 = PropertyUtils.getProperty(compareSubObject1, compareEndField);
            } else {
                compareObject = PropertyUtils.getProperty(obj, compareField);
                compareObject1 = PropertyUtils.getProperty(obj1, compareField);
            }
        } catch (IllegalAccessException iae) {
        } catch (InvocationTargetException ite) {
        } catch (NoSuchMethodException nsme) {
        }
        if (compareObject != null && compareObject1 != null) {
            if ( (compareObject instanceof Comparable) && (compareObject1 instanceof Comparable) ) {
                return ((Comparable) compareObject).compareTo((Comparable) compareObject1);
            }
            if ( (compareObject instanceof Object) && (compareObject1 instanceof Object) ) {
                return ((Object) compareObject).toString().compareTo(((Object) compareObject1).toString());
            }
            String theClass = compareObject.getClass().getName();
            throw new IllegalArgumentException("Invalid property class: '" + theClass + "'");
        }
        return 0;
    }
    
}
