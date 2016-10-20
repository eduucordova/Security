/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.encrypter;

import org.apache.commons.codec.binary.Hex;

/**
 * @author eduardo
 */
public class FileEncrypter {
    
    public String GenerateSecureKey(String masterKey, int it) {
        return GenerateSecureKey(masterKey, null, it);
    }
    public String GenerateSecureKey(String masterKey, String salt, int it) {
        return Hex.encodeHexString((PBKDF2Util.generateDerivedKey(masterKey, salt, it)).getEncoded());
    }
    
    public String GenerateHmacWithFileName(String masterKey, String fileName) {
        return HMACUtil.GenerateHmac(masterKey, fileName);
    }
}
