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
package nl.b3p.commons.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Rolf
 */
public class Encryption {

    private static KeyGenerator kgen = null;
    private static SecretKey skey = null;
    private static SecretKeySpec skeySpec = null;
    private static Cipher cipher = null;

    public static byte[] sha1Encrypt(String x) throws Exception {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return d.digest();
    }

    public static byte[] md5Encrypt(String x) throws Exception {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("MD5");
        d.reset();
        d.update(x.getBytes());
        return d.digest();
    }

    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

    public static String encryptAES(String message) {
        byte[] encryptedMessage = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");

            kgen.init(128); // 192 and 256 bits may not be available

            // Generate the secret key specs.
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();

            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

            // Instantiate the cipher

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            encryptedMessage = cipher.doFinal(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("encrypted String: " + asHex(encryptedMessage));
        String encryptedString = asHex(encryptedMessage);
        return encryptedString;
    }

    public static String decryptAES(String cipherTekst) {
        String originalString = null;
        try {
            byte[] encryptedMessage = cipherTekst.getBytes();

            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(encryptedMessage);
            originalString = new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return originalString;
    }
}
