/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.encrypter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author eduardo
 */
public class EncryptDecryptFile {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int MAC_SIZE = 128;

    public static void encrypt(String key, File inputFile, File outputFile) {
        byte[] iv = generateRandomIv();
        doGcmBlockCipher(true, key, iv, inputFile, outputFile);
    }

    public static void decrypt(String key, File inputFile, File outputFile) throws FileNotFoundException, IOException {
        byte[] iv;
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            iv = new byte[16];
            inputStream.read(iv);
        }
        doGcmBlockCipher(false, key, iv, inputFile, outputFile);
    }

//    private static void doCrypto(int cipherMode, SecretKey key, byte[] iv, File inputFile, File outputFile) {
//        try {
//            IvParameterSpec ivSpec = new IvParameterSpec(iv);
//            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
//            cipher.init(cipherMode, key, ivSpec);
//            
//            FileInputStream inputStream = new FileInputStream(inputFile);
//            byte[] inputBytes = new byte[(int) inputFile.length()];
//            inputStream.read(inputBytes);
//            
//            byte[] outputBytes = {};
//            if(cipherMode == Cipher.DECRYPT_MODE) {
//                byte[] destination = new byte[inputBytes.length - iv.length];
//                System.arraycopy(inputBytes, 16, destination, 0, destination.length);
//                outputBytes = cipher.doFinal(destination);
//            }
//            else {
//                outputBytes = cipher.doFinal(inputBytes);
//            }
//            FileOutputStream outputStream = new FileOutputStream(outputFile);
//            
//            if(cipherMode == Cipher.ENCRYPT_MODE) {
//                byte[] iv_output = new byte[iv.length + outputBytes.length];
//                System.arraycopy(iv, 0, iv_output, 0, iv.length);
//                System.arraycopy(outputBytes, 0, iv_output, iv.length, outputBytes.length);
//                outputStream.write(iv_output);
//            }
//            else {
//                outputStream.write(outputBytes);
//            }
//            
//            inputStream.close();
//            outputStream.close();
//        } catch (Exception ex) {
//            Logger.getLogger(EncryptDecryptFile.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    private static byte[] generateRandomIv() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (NoSuchProviderException | NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptDecryptFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return iv;
    }
    
    private static void doGcmBlockCipher(boolean encrypt, String key, byte[] iv, File inputFile, File outputFile){
        try {
            Security.addProvider(new BouncyCastleProvider());

            GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());

            // Parametros a serem passados para o GCM: chave, tamanho do mac, o nonce
            KeyParameter keyParam = new KeyParameter(key.getBytes());     
            AEADParameters params = new AEADParameters(keyParam, MAC_SIZE, iv);

            // true para cifrar
            gcm.init(encrypt, params);

            // Get file bytes
            FileInputStream inputStream = null;
            byte[] inputBytes = {};
            try {
                inputStream = new FileInputStream(inputFile);
                inputBytes = new byte[(int) inputFile.length()];
                inputStream.read(inputBytes);
            } catch (Exception ex) {
                Logger.getLogger(EncryptDecryptFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            int lengthOutc = 0;

            byte[] outputBytes = {};
            // false se for decript
            if(!encrypt) {
                byte[] destination = new byte[inputBytes.length - iv.length];
                System.arraycopy(inputBytes, 16, destination, 0, destination.length);
                lengthOutc = gcm.processBytes(destination, 0, destination.length, outputBytes, 0);
                gcm.doFinal(outputBytes, lengthOutc);
            }
            else {
                lengthOutc = gcm.processBytes(inputBytes, 0, inputBytes.length, outputBytes, 0);
                gcm.doFinal(outputBytes, lengthOutc);
            }


            FileOutputStream outputStream = new FileOutputStream(outputFile);

            if(encrypt) {
                byte[] iv_output = new byte[iv.length + outputBytes.length];
                System.arraycopy(iv, 0, iv_output, 0, iv.length);
                System.arraycopy(outputBytes, 0, iv_output, iv.length, outputBytes.length);
                outputStream.write(iv_output);
            }
            else {
                outputStream.write(outputBytes);
            }

            inputStream.close();
            outputStream.close();
        }catch(Exception ex){
            //fuck it
        }
    }
}
