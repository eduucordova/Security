/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.encrypter;

import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Hex;
import table.FilesTable;

/**
 * @author eduardo
 */
public class FileEncrypter {
    
//    private static Table<String, String> tableFiles;
    
    public static void FileEncrypter(String masterKey) {
        for(int i = 0; i< 10; i++) {
        String hamc = HMACUtil.GenerateHmac("Isso é uma mensagem aleatória");
        System.out.println(hamc);
        }
        
        FilesTable tableFiles = new FilesTable();
    }
    
    public String GenerateSecureKey(String masterKey, int it) {
        return Hex.encodeHexString((PBKDF2Util.generateDerivedKey(masterKey, it)).getEncoded());
    }
    
    public String GenerateHmacWithFileName(String fileName) {
        return HMACUtil.GenerateHmac(fileName);
    }
}
