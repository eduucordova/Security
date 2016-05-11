/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.encrypter;

import java.security.Security;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.DecoderException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;


/**
 *
 * @author eduardo
 */
public class GCMCipher {
    public static String GCMBlockCipherBC(String masterKey, String text, boolean mode)
    {
        Security.addProvider(new BouncyCastleProvider());
        byte[] plainBytes = Hex.decode(text);

        String pN;
        pN = "cafebabefacedbaddecaf888";
        byte[] N = null;
        try {
            N = org.apache.commons.codec.binary.Hex.decodeHex(pN.toCharArray());
        } catch (DecoderException ex) {
            Logger.getLogger(GCMCipher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Instancia um GCM com AES usando o formato da BouncyCastle
        GCMBlockCipher gcm = new GCMBlockCipher(new AESEngine());
        
        // Parametros a serem passados para o GCM: chave, tamanho do mac, o nonce
        KeyParameter key = new KeyParameter(masterKey.getBytes());
        AEADParameters params = new AEADParameters(key, 128, N);
        
        // true para cifrar
        gcm.init(mode, params);
        int outsize = gcm.getOutputSize(plainBytes.length);
        byte[] outc = new byte[outsize];
        //processa os bytes calculando o offset para cifrar
        int lengthOutc = gcm.processBytes(plainBytes, 0, plainBytes.length, outc, 0);

        try {
            //cifra os bytes
            gcm.doFinal(outc, lengthOutc);
        } catch (IllegalStateException ex) {
            Logger.getLogger(GCMCipher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidCipherTextException ex) {
            Logger.getLogger(GCMCipher.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Hex.toHexString(outc);
    }
}
