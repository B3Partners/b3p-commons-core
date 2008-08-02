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

/*
 * $Id: ExtendedMethodProperties.java 2771 2006-03-28 13:29:24Z Chris $
 */
package nl.b3p.commons.struts;

public class ExtendedMethodProperties extends ActionMethodProperties {
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

    public ExtendedMethodProperties(String methodName) {
        super(methodName);
    }

    public ExtendedMethodProperties(String methodName,
            String defaultForwardName,
            String alternateForwardName) {
        super(methodName);
        this.setDefaultForwardName(defaultForwardName);
        this.setAlternateForwardName(alternateForwardName);
    }

    public ExtendedMethodProperties(String methodName,
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
