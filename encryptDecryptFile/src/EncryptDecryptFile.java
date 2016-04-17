import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.*;
import java.security.SecureRandom;
import javax.crypto.spec.IvParameterSpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import java.util.Scanner;

public class EncryptDecryptFile {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CTR/NoPadding";

    public static void encrypt(String key, File inputFile, File outputFile) throws CryptoException {
        try {
            byte[] iv = generateRandomIv();
            doCrypto(Cipher.ENCRYPT_MODE, key, iv, inputFile, outputFile);
        } catch (Exception ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }

    public static void decrypt(String key, File inputFile, File outputFile) throws CryptoException {
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] iv = new byte[16];
            inputStream.read(iv);
            inputStream.close();
            doCrypto(Cipher.DECRYPT_MODE, key, iv, inputFile, outputFile);
        } catch (IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }

    private static void doCrypto(int cipherMode, String key, byte[] iv, File inputFile, File outputFile) throws CryptoException {
        try {
            byte[] byteKey = new byte[16];
            if(key.getBytes().length > 16)
                System.arraycopy(key.getBytes(), 0, byteKey, 0, 16);
            else {
                byteKey = Arrays.copyOfRange(key.getBytes(), 0, 16);
            }
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Key secretKey = new SecretKeySpec(byteKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey, ivSpec);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = {};
            if(cipherMode == Cipher.DECRYPT_MODE) {
                byte[] destination = new byte[inputBytes.length - iv.length];
                System.arraycopy(inputBytes, 16, destination, 0, destination.length);
                outputBytes = cipher.doFinal(destination);
            }
            else {
                outputBytes = cipher.doFinal(inputBytes);
            }
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            if(cipherMode == Cipher.ENCRYPT_MODE) {
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

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }

    private static byte[] generateRandomIv()  throws CryptoException {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            return iv;
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }

    public static void debugger() {
        Scanner input = new Scanner(System.in);
        String continuar = "N";
        while (!continuar.equals("S")) {
            System.out.println("Continuar (S/N)? ");
            continuar = input.nextLine();
        }
    }
}
