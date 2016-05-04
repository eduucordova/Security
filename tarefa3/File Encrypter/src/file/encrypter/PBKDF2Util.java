package file.encrypter;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * @param iterations
     * @return
     * @throws java.security.NoSuchAlgorithmException
     */
    public static SecretKey generateDerivedKey(String key, Integer iterations) {
        byte[] salt = {};
        try {
            salt = getSalt();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PBKDF2Util.class.getName()).log(Level.SEVERE, null, ex);
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
        //SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }    
}
