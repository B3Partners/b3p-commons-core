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
import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.xml.validators.*;

/**
 * 
 * 
 * @version $Revision$ $Date$
 **/
public class WebappUsersDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {
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
    public WebappUsersDescriptor() {
        super();
        xmlName = "webapp-users";

        //-- set grouping compositor
        setCompositorAsSequence();
        XMLFieldDescriptorImpl desc = null;
        XMLFieldHandler handler = null;
        FieldValidator fieldValidator = null;
        //-- initialize attribute descriptors

        //-- _maxsessions
        desc = new XMLFieldDescriptorImpl(java.lang.Integer.TYPE, "_maxsessions", "maxsessions", NodeType.Attribute);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object)
                    throws IllegalStateException {
                WebappUsers target = (WebappUsers) object;
                if (!target.hasMaxsessions()) {
                    return null;
                }
                return new Integer(target.getMaxsessions());
            }

            public void setValue(java.lang.Object object, java.lang.Object value)
                    throws IllegalStateException, IllegalArgumentException {
                try {
                    WebappUsers target = (WebappUsers) object;
                    // if null, use delete method for optional primitives 
                    if (value == null) {
                        target.deleteMaxsessions();
                        return;
                    }
                    target.setMaxsessions(((Integer) value).intValue());
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        addFieldDescriptor(desc);

        //-- validation code for: _maxsessions
        fieldValidator = new FieldValidator();
        { //-- local scope
            IntegerValidator iv = new IntegerValidator();
            fieldValidator.setValidator(iv);
        }
        desc.setValidator(fieldValidator);

        //-- initialize element descriptors

        //-- _roleList
        desc = new XMLFieldDescriptorImpl(Role.class, "_roleList", "role", NodeType.Element);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object)
                    throws IllegalStateException {
                WebappUsers target = (WebappUsers) object;
                return target.getRole();
            }

            public void setValue(java.lang.Object object, java.lang.Object value)
                    throws IllegalStateException, IllegalArgumentException {
                try {
                    WebappUsers target = (WebappUsers) object;
                    target.addRole((Role) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new Role();
            }
        });
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(true);
        addFieldDescriptor(desc);

        //-- validation code for: _roleList
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(0);
        desc.setValidator(fieldValidator);

        //-- _userList
        desc = new XMLFieldDescriptorImpl(User.class, "_userList", "user", NodeType.Element);
        handler = (new XMLFieldHandler() {

            public java.lang.Object getValue(java.lang.Object object)
                    throws IllegalStateException {
                WebappUsers target = (WebappUsers) object;
                return target.getUser();
            }

            public void setValue(java.lang.Object object, java.lang.Object value)
                    throws IllegalStateException, IllegalArgumentException {
                try {
                    WebappUsers target = (WebappUsers) object;
                    target.addUser((User) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(java.lang.Object parent) {
                return new User();
            }
        });
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(true);
        addFieldDescriptor(desc);

        //-- validation code for: _userList
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(0);
        desc.setValidator(fieldValidator);

    } //-- nl.b3p.commons.security.xml.WebappUsersDescriptor()


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
        return nl.b3p.commons.security.xml.WebappUsers.class;
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
