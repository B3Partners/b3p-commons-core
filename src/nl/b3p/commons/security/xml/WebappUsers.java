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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class WebappUsers implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private int _maxsessions = 0;

    /**
     * keeps track of state for field: _maxsessions
    **/
    private boolean _has_maxsessions;

    private java.util.ArrayList _roleList;

    private java.util.ArrayList _userList;


      //----------------/
     //- Constructors -/
    //----------------/

    public WebappUsers() {
        super();
        _roleList = new ArrayList();
        _userList = new ArrayList();
    } //-- nl.b3p.commons.security.xml.WebappUsers()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vRole
    **/
    public void addRole(Role vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _roleList.add(vRole);
    } //-- void addRole(Role) 

    /**
     * 
     * 
     * @param index
     * @param vRole
    **/
    public void addRole(int index, Role vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _roleList.add(index, vRole);
    } //-- void addRole(int, Role) 

    /**
     * 
     * 
     * @param vUser
    **/
    public void addUser(User vUser)
        throws java.lang.IndexOutOfBoundsException
    {
        _userList.add(vUser);
    } //-- void addUser(User) 

    /**
     * 
     * 
     * @param index
     * @param vUser
    **/
    public void addUser(int index, User vUser)
        throws java.lang.IndexOutOfBoundsException
    {
        _userList.add(index, vUser);
    } //-- void addUser(int, User) 

    /**
    **/
    public void clearRole()
    {
        _roleList.clear();
    } //-- void clearRole() 

    /**
    **/
    public void clearUser()
    {
        _userList.clear();
    } //-- void clearUser() 

    /**
    **/
    public void deleteMaxsessions()
    {
        this._has_maxsessions= false;
    } //-- void deleteMaxsessions() 

    /**
    **/
    public java.util.Enumeration enumerateRole()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_roleList.iterator());
    } //-- java.util.Enumeration enumerateRole() 

    /**
    **/
    public java.util.Enumeration enumerateUser()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_userList.iterator());
    } //-- java.util.Enumeration enumerateUser() 

    /**
     * Returns the value of field 'maxsessions'.
     * 
     * @return the value of field 'maxsessions'.
    **/
    public int getMaxsessions()
    {
        return this._maxsessions;
    } //-- int getMaxsessions() 

    /**
     * 
     * 
     * @param index
    **/
    public Role getRole(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _roleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Role) _roleList.get(index);
    } //-- Role getRole(int) 

    /**
    **/
    public Role[] getRole()
    {
        int size = _roleList.size();
        Role[] mArray = new Role[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Role) _roleList.get(index);
        }
        return mArray;
    } //-- Role[] getRole() 

    /**
    **/
    public int getRoleCount()
    {
        return _roleList.size();
    } //-- int getRoleCount() 

    /**
     * 
     * 
     * @param index
    **/
    public User getUser(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _userList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (User) _userList.get(index);
    } //-- User getUser(int) 

    /**
    **/
    public User[] getUser()
    {
        int size = _userList.size();
        User[] mArray = new User[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (User) _userList.get(index);
        }
        return mArray;
    } //-- User[] getUser() 

    /**
    **/
    public int getUserCount()
    {
        return _userList.size();
    } //-- int getUserCount() 

    /**
    **/
    public boolean hasMaxsessions()
    {
        return this._has_maxsessions;
    } //-- boolean hasMaxsessions() 

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
     * 
     * 
     * @param vRole
    **/
    public boolean removeRole(Role vRole)
    {
        boolean removed = _roleList.remove(vRole);
        return removed;
    } //-- boolean removeRole(Role) 

    /**
     * 
     * 
     * @param vUser
    **/
    public boolean removeUser(User vUser)
    {
        boolean removed = _userList.remove(vUser);
        return removed;
    } //-- boolean removeUser(User) 

    /**
     * Sets the value of field 'maxsessions'.
     * 
     * @param maxsessions the value of field 'maxsessions'.
    **/
    public void setMaxsessions(int maxsessions)
    {
        this._maxsessions = maxsessions;
        this._has_maxsessions = true;
    } //-- void setMaxsessions(int) 

    /**
     * 
     * 
     * @param index
     * @param vRole
    **/
    public void setRole(int index, Role vRole)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _roleList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _roleList.set(index, vRole);
    } //-- void setRole(int, Role) 

    /**
     * 
     * 
     * @param roleArray
    **/
    public void setRole(Role[] roleArray)
    {
        //-- copy array
        _roleList.clear();
        for (int i = 0; i < roleArray.length; i++) {
            _roleList.add(roleArray[i]);
        }
    } //-- void setRole(Role) 

    /**
     * 
     * 
     * @param index
     * @param vUser
    **/
    public void setUser(int index, User vUser)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _userList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _userList.set(index, vUser);
    } //-- void setUser(int, User) 

    /**
     * 
     * 
     * @param userArray
    **/
    public void setUser(User[] userArray)
    {
        //-- copy array
        _userList.clear();
        for (int i = 0; i < userArray.length; i++) {
            _userList.add(userArray[i]);
        }
    } //-- void setUser(User) 

    /**
     * 
     * 
     * @param reader
    **/
    public static nl.b3p.commons.security.xml.WebappUsers unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (nl.b3p.commons.security.xml.WebappUsers) Unmarshaller.unmarshal(nl.b3p.commons.security.xml.WebappUsers.class, reader);
    } //-- nl.b3p.commons.security.xml.WebappUsers unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
