/*
 * Encryption.java
 *
 * Created on 28 februari 2006, 12:18
 *
 */

package nl.b3p.commons.encryption;

/**
 *
 * @author Rolf
 */
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
    
    public static byte[] sha1Encrypt(String x)   throws Exception {
        java.security.MessageDigest d =null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return  d.digest();
    }
    
    public static byte[] md5Encrypt(String x)   throws Exception {
        java.security.MessageDigest d =null;
        d = java.security.MessageDigest.getInstance("MD5");
        d.reset();
        d.update(x.getBytes());
        return  d.digest();
    }
    
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        
        return strbuf.toString();
    }
    
    public static String encryptAES(String message) {
        byte [] encryptedMessage = null;
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
        } catch(Exception e) {
            e.printStackTrace();
        }
        //System.out.println("encrypted String: " + asHex(encryptedMessage));
        String encryptedString = asHex(encryptedMessage);
        return encryptedString;
    }
    
    public static String decryptAES(String cipherTekst) {
        String originalString = null;
        try {
            byte [] encryptedMessage = cipherTekst.getBytes();
            
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte [] original = cipher.doFinal(encryptedMessage);
            originalString = new String(original);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return originalString;
    }
    
}
