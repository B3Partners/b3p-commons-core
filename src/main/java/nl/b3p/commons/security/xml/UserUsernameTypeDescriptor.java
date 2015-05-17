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
/**
 * 
 * 
 * @version $Revision$ $Date$
 **/
public class UserUsernameTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {
    //--------------------------/
    //- Class/Member Variables -/
    //--------------------------/
    private java.lang.String nsPrefix;
    private java.lang.String nsURI;
    private java.lang.String xmlName;
    private org.exolab.castor.xml.XMLFieldDescriptor identity;
    //----------------/
    //- Constructors -/
    //----------------/
    public UserUsernameTypeDescriptor() {
        super();
        xmlName = "UserUsernameType";
    } //-- nl.b3p.commons.security.xml.UserUsernameTypeDescriptor()


    //-----------/
    //- Methods -/
    //-----------/
    /**
     **/
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
     **/
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
     **/
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     **/
    public java.lang.Class getJavaClass() {
        return nl.b3p.commons.security.xml.UserUsernameType.class;
    } //-- java.lang.Class getJavaClass() 

    /**
     **/
    public java.lang.String getNameSpacePrefix() {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     **/
    public java.lang.String getNameSpaceURI() {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
     **/
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
     **/
    public java.lang.String getXMLName() {
        return xmlName;
    } //-- java.lang.String getXMLName() 
}
