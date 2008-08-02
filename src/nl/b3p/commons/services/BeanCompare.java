/*
 * B3P Commons Core is a library with commonly used classes for webapps.
 * Included are clieop3, oai, security, struts, taglibs and other
 * general helper classes and extensions.
 *
 * Copyright 2000 - 2008 B3Partners BV
 * 
 * This file is part of B3P Commons Core.
 * 
 * B3P Commons Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Commons Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Commons Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
            if (compareField.indexOf(".") != -1) {
                String[] compareFields = compareField.split("\\.");
                Object compareSubObject = null;
                Object compareSubObject1 = null;
                String compareEndField = "";
                for (int i = 0; i < compareFields.length; i++) {
                    if ((i - 1) != -1) {
                        compareSubObject = PropertyUtils.getProperty(obj, compareFields[i - 1]);
                        compareSubObject1 = PropertyUtils.getProperty(obj1, compareFields[i - 1]);
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
            if ((compareObject instanceof Comparable) && (compareObject1 instanceof Comparable)) {
                return ((Comparable) compareObject).compareTo((Comparable) compareObject1);
            }
            if ((compareObject instanceof Object) && (compareObject1 instanceof Object)) {
                return ((Object) compareObject).toString().compareTo(((Object) compareObject1).toString());
            }
            String theClass = compareObject.getClass().getName();
            throw new IllegalArgumentException("Invalid property class: '" + theClass + "'");
        }
        return 0;
    }
}
