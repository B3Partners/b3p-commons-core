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
 * B3pCommonsException.java
 *
 * Created on 16 mei 2005, 10:32
 *
 */
package nl.b3p.commons.services;

/**
 *
 * @author Chris
 */
public class B3pCommonsException extends Exception {

    /**
     * Creates a new instance of B3pCommonsException 
     */
    public B3pCommonsException() {
        super();
    }

    /**
     * Nieuwe B3pCommonsException met beschrijvende tekst.
     * @param msg beschrijving van foutconditie
     */
    public B3pCommonsException(String msg) {
        super(msg);
    }

    /**
     * Nieuwe B3pCommonsException met beschrijvende tekst en
     * veroorzakende exceptie.
     * @param msg beschrijving van foutconditie
     * @param cause onderliggende oorzaak van de exceptie
     */
    public B3pCommonsException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Nieuwe B3pCommonsException met veroorzakende exceptie.
     * @param cause onderliggende exceptie
     */
    public B3pCommonsException(Throwable cause) {
        super(cause);
    }
}
