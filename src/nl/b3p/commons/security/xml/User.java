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

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

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
    public java.lang.String getPassword()
    {
        return this._password;
    } //-- java.lang.String getPassword() 

    /**
     * Returns the value of field 'roles'.
     * 
     * @return the value of field 'roles'.
    **/
    public java.lang.String getRoles()
    {
        return this._roles;
    } //-- java.lang.String getRoles() 

    /**
     * Returns the value of field 'username'.
     * 
     * @return the value of field 'username'.
    **/
    public java.lang.String getUsername()
    {
        return this._username;
    } //-- java.lang.String getUsername() 

    /**
    **/
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
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
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'password'.
     * 
     * @param password the value of field 'password'.
    **/
    public void setPassword(java.lang.String password)
    {
        this._password = password;
    } //-- void setPassword(java.lang.String) 

    /**
     * Sets the value of field 'roles'.
     * 
     * @param roles the value of field 'roles'.
    **/
    public void setRoles(java.lang.String roles)
    {
        this._roles = roles;
    } //-- void setRoles(java.lang.String) 

    /**
     * Sets the value of field 'username'.
     * 
     * @param username the value of field 'username'.
    **/
    public void setUsername(java.lang.String username)
    {
        this._username = username;
    } //-- void setUsername(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static nl.b3p.commons.security.xml.User unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (nl.b3p.commons.security.xml.User) Unmarshaller.unmarshal(nl.b3p.commons.security.xml.User.class, reader);
    } //-- nl.b3p.commons.security.xml.User unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
