package file.encrypter;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * @author eduardo
 */
public class PBKDF2Util {

    /**
     * Gerar chave derivada da senha
     * @param key
     * @param s
     * @param iterations
     * @return
     */
    public static SecretKey generateDerivedKey(String key, String s, Integer iterations) {
        byte[] salt = {};
        if(s == null) {
            try {
                salt = getSalt();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            salt = s.getBytes();
        }
        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), salt, iterations, 128);
        SecretKeyFactory pbkdf2 = null;
        SecretKey sk = null;
        try {
            pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            sk = pbkdf2.generateSecret(spec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sk;
    }
    
    /*Usado para gerar o salt  */
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[128];
        sr.nextBytes(salt);
        return salt;
    }    
}
