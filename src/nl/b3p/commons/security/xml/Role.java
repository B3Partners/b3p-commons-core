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
public class Role implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _rolename;


      //----------------/
     //- Constructors -/
    //----------------/

    public Role() {
        super();
    } //-- nl.b3p.commons.security.xml.Role()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'rolename'.
     * 
     * @return the value of field 'rolename'.
    **/
    public java.lang.String getRolename()
    {
        return this._rolename;
    } //-- java.lang.String getRolename() 

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
     * Sets the value of field 'rolename'.
     * 
     * @param rolename the value of field 'rolename'.
    **/
    public void setRolename(java.lang.String rolename)
    {
        this._rolename = rolename;
    } //-- void setRolename(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static nl.b3p.commons.security.xml.Role unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (nl.b3p.commons.security.xml.Role) Unmarshaller.unmarshal(nl.b3p.commons.security.xml.Role.class, reader);
    } //-- nl.b3p.commons.security.xml.Role unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
