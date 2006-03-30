package nl.b3p.commons.security;

import org.securityfilter.realm.SimpleSecurityRealmBase;

/**
 * Xml implementation of the SecurityRealmInterface.
 *
 */
public class XmlSecurityRealm extends SimpleSecurityRealmBase {

   public boolean booleanAuthenticate(String username, String password) {
      return XmlSecurityDatabase.booleanAuthenticate(username, password);
   }

   public boolean isUserInRole(String username, String role) {
      return XmlSecurityDatabase.isUserInRole(username, role);
   }
}

