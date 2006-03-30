/*
 * B3pCommonsException.java
 *
 * Created on 16 mei 2005, 10:32
 *
 */

package nl.b3p.commons.services;

import org.apache.commons.lang.exception.*;

/**
 *
 * @author Chris
 */
public class B3pCommonsException extends NestableException {
    
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
