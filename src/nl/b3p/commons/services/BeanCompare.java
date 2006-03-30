package nl.b3p.commons.services;

import java.lang.*;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.PropertyUtils;
import java.math.BigDecimal;

public class BeanCompare implements Comparator {
    
    private String compareField = null;
    
    public BeanCompare(String fieldName) {
        compareField = fieldName;
    }
    
    public int compare(Object obj, Object obj1) {
        Object compareObject = null;
        Object compareObject1 = null;
        try {
            compareObject = PropertyUtils.getProperty(obj, compareField);
            compareObject1 = PropertyUtils.getProperty(obj1, compareField);
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
