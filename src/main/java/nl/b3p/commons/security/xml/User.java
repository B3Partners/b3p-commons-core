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
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id$
 */
package nl.b3p.commons.security.xml;

//---------------------------------/
//- Imported classes and packages -/
//---------------------------------/
import org.exolab.castor.xml.*;

/**
 * 
 * 
 * @version $Revision$ $Date$
 **/
public class User implements java.io.Serializable {
    //--------------------------/
    //- Class/Member Variables -/
    //--------------------------/
    private java.lang.String _username;
    private java.lang.String _password;
    private java.lang.String _roles;
    //----------------/
    //- Constructors -/
    //----------------/
    public User() {
        super();
    } //-- nl.b3p.commons.security.xml.User()


    //-----------/
    //- Methods -/
    //-----------/
    /**
     * Returns the value of field 'password'.
     * 
     * @return the value of field 'password'.
     **/
    public java.lang.String getPassword() {
        return this._password;
    } //-- java.lang.String getPassword() 

    /**
     * Returns the value of field 'roles'.
     * 
     * @return the value of field 'roles'.
     **/
    public java.lang.String getRoles() {
        return this._roles;
    } //-- java.lang.String getRoles() 

    /**
     * Returns the value of field 'username'.
     * 
     * @return the value of field 'username'.
     **/
    public java.lang.String getUsername() {
        return this._username;
    } //-- java.lang.String getUsername() 

    /**
     **/
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * 
     * @param out
     **/
    public void marshal(java.io.Writer out)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {

        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
     **/
    public void marshal(org.xml.sax.ContentHandler handler)
            throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {

        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'password'.
     * 
     * @param password the value of field 'password'.
     **/
    public void setPassword(java.lang.String password) {
        this._password = password;
    } //-- void setPassword(java.lang.String) 

    /**
     * Sets the value of field 'roles'.
     * 
     * @param roles the value of field 'roles'.
     **/
    public void setRoles(java.lang.String roles) {
        this._roles = roles;
    } //-- void setRoles(java.lang.String) 

    /**
     * Sets the value of field 'username'.
     * 
     * @param username the value of field 'username'.
     **/
    public void setUsername(java.lang.String username) {
        this._username = username;
    } //-- void setUsername(java.lang.String) 

    /**
     * 
     * 
     * @param reader
     **/
    public static nl.b3p.commons.security.xml.User unmarshal(java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (nl.b3p.commons.security.xml.User) Unmarshaller.unmarshal(nl.b3p.commons.security.xml.User.class, reader);
    } //-- nl.b3p.commons.security.xml.User unmarshal(java.io.Reader) 

    /**
     **/
    public void validate()
            throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 
}
