import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider; // Incluido

public class SecurityCipher {

  private Key aesKey;
  private IvParameterSpec ivSpec;
  private Cipher cipher;

  protected void inicia() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
    // Incluido
    // Instanciar um novo Security provider
    int addProvider = Security.addProvider(new BouncyCastleProvider());

    // Instancia o cipher
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");

    // Chave na String
    byte[] key = {};
    String stringKey = "43b23e8c67e7967eeb8ac5c08d5abbf8";
    try {
      key = Hex.decodeHex(stringKey.toCharArray());
    }
    catch (DecoderException ex) {
      Logger.getLogger(SecurityCipher.class.getName()).log(Level.SEVERE, null, ex);
    }
    aesKey = new SecretKeySpec(key, "AES");

    // IV na String
    byte[] iv = {};
    String stringIv = "c72694c2b2eb48531d1d06c2909a3bad";
    try {
      iv = Hex.decodeHex(stringIv.toCharArray());
    }
    catch (DecoderException ex) {
      Logger.getLogger(SecurityCipher.class.getName()).log(Level.SEVERE, null, ex);
    }
    ivSpec = new IvParameterSpec(iv);
  }

  public String decrypt(String dec) throws InvalidKeyException, InvalidAlgorithmParameterException {
    try {
      cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
      byte[] embytes = {};
      try {
        embytes = Hex.decodeHex(dec.toCharArray());
      }
      catch (DecoderException ex) {
        Logger.getLogger(SecurityCipher.class.getName()).log(Level.SEVERE, null, ex);
      }
      String decryptedString = new String(cipher.doFinal(embytes));
      return decryptedString;
    }
    catch (IllegalBlockSizeException | BadPaddingException e) {
      System.out.println(e);
    }
    return null;
  }

  public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException {
    SecurityCipher obj = new SecurityCipher();
    obj.inicia();
    String cifrada = "326fdff77f429abe6e88204fdbf5288159074444e5ce92468e45073c0a80da742181a425eb27c942b3e29f2d40f023c3";
    String decifrada = obj.decrypt(cifrada);
    System.out.println("Mensagem decifrada = " + decifrada);
  }
}
