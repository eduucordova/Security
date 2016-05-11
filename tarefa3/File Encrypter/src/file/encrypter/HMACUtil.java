package file.encrypter;

import java.security.Security;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author eduardo
 */
public class HMACUtil {
    
    public static String GenerateHmac(String key, String message) {
        Security.addProvider(new BouncyCastleProvider());
        
        // Using SHA256
        SHA256Digest digest = new SHA256Digest();
        
        HMac umHmac = new HMac(digest);
        
        byte[] resBuf1 = new byte[umHmac.getMacSize()];

        byte[] input = message.getBytes();
        
        // Hmac key derivated from a master key
        KeyParameter HmacKey = new KeyParameter(Hex.decode(key));

        umHmac.init(HmacKey);
        umHmac.update(input, 0, input.length);
        umHmac.doFinal(resBuf1, 0);
        
        return Hex.toHexString(resBuf1);
    }
}
