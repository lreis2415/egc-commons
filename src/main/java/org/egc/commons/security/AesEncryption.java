package org.egc.commons.security;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.OperationMode;
import org.apache.shiro.crypto.PaddingScheme;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * <pre>
 * AES加解密处理
 * 字符串有使用Base64（默认）、Hex编码处理两种
 * </pre>
 *
 * @author houzhiwei
 * @date 2017/1/10 23:47
 */
public class AesEncryption extends BaseAesEncryption {
    /**
     * Gets aes key.
     *
     * @param keySize the key size 128/192/256
     * @return the aes key
     */
    public static Key getAesKey(int keySize) {
        AesCipherService aesService = new AesCipherService();
        aesService.setKeySize(keySize);
        Key key = aesService.generateNewKey();
        return key;
    }

    /*------------------------------Base64------------------------------*/

    /**
     * Gets base 64 key string.
     *
     * @return the base 64 key string
     */
    public static String getBase64KeyStr() {
        Key key = getAesKey(128);
        return key2String(key);
    }

    /**
     * Gets base 64 key string.
     *
     * @param size the key size: 128/192/256
     * @return the base 64 key string
     */
    public static String getBase64KeyStr(int size) {
        Key key = getAesKey(size);
        return key2String(key);
    }

    /**
     * Key to string.
     *
     * @param key the key
     * @return the Base64 encoded string
     */
    public static String key2String(Key key) {
        return Base64.encodeToString(key.getEncoded());
    }

    /**
     * key string to SecretKey key.
     *
     * @param keyStr the Base64 encoded key string
     * @return the SecretKey type key
     */
    public static Key string2Key(String keyStr) {
        byte[] encodedKey = Base64.decode(keyStr);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * AES encrypt with provided key.
     *
     * @param pwd    the password
     * @param keyStr the key String
     * @return the encrypted Base64 encoded password and key(salt)
     */
    public static EncryptedDTO aesEncrypt(String pwd, String keyStr) {
        String encryptedPWD = aesEncrypt(pwd, string2Key(keyStr), 128);
        EncryptedDTO encrypted = new EncryptedDTO(keyStr, encryptedPWD);
        return encrypted;
    }

    /**
     * AES encrypt.
     *
     * @param pwd the password
     * @return the encrypted Base64 encoded password and key(salt)
     */
    public static EncryptedDTO aesEncrypt(String pwd) {
        Key key = getAesKey(128);
        String encryptedPW = aesEncrypt(pwd, key, 128);
        EncryptedDTO encrypted = new EncryptedDTO(key2String(key), encryptedPW);
        return encrypted;
    }

    /**
     * AES decrypt Base64 string.
     *
     * @param encryptedPW the encrypted Base64 encoded password
     * @param keyStr      the key string to decrypt
     * @return the password
     */
    public static String aesDecrypt(String encryptedPW, String keyStr) {
        AesCipherService aesService = new AesCipherService();
        String pw = new String(aesService.decrypt(Base64.decode(encryptedPW), string2Key(keyStr).getEncoded()).getBytes());
        return pw;
    }

    /*----------------FrontEnd - BackEnd encrypt & decrypt------------*/

    /**
     * Decrypt CryptoJS v3 AES encrypted string.
     * <pre>
     * OperationMode: ECB
     * PaddingScheme: PKCS5 (Pkcs7 in CryptoJS)
     * Do not Generate Initialization Vectors
     * In JavaScript：
     * -------------------------------------------------------
     * var cryptoAesEncryptEcb = function (keyStr, pwd){
     * var key = CryptoJS.enc.Base64.parse(keyStr);
     * var encrypted = CryptoJS.AES.encrypt(pwd, key, {
     * mode: CryptoJS.mode.ECB,
     * padding: CryptoJS.pad.Pkcs7
     * });
     * var encryptedPwd = encrypted.toString();
     * return encryptedPwd;
     * };
     * -------------------------------------------------------
     * </pre>
     *
     * @param encryptedPW the encrypted password
     * @param keyStr      the key string
     * @return the decrypted password string
     */
    public static String cryptoAesDecrypt(String encryptedPW, String keyStr) {
        AesCipherService aesService = new AesCipherService();
        aesService.setPaddingScheme(PaddingScheme.PKCS5);
        aesService.setGenerateInitializationVectors(false);
        aesService.setMode(OperationMode.ECB);
        String pw = new String(aesService.decrypt(Base64.decode(encryptedPW), string2Key(keyStr).getEncoded()).getBytes());
        return pw;
    }

    /*------------------------------Hex------------------------------*/

    /**
     * Gets hex key string.
     *
     * @return the hex key string
     */
    public static String getHexKeyStr() {
        Key key = getAesKey(128);
        return key2HexString(key);
    }

    /**
     * Hex key string to SecretKey key.
     *
     * @param keyStr the key str
     * @return the key
     */
    public static Key hexString2Key(String keyStr) {
        byte[] encodedKey = Hex.decode(keyStr);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * Key to hex string string.
     *
     * @param key the key
     * @return the string
     */
    public static String key2HexString(Key key) {
        return Hex.encodeToString(key.getEncoded());
    }

    /**
     * AES decrypt hex string.
     *
     * @param encryptedPW the encrypted Hex encoded password
     * @param keyStr      the key string
     * @return the password
     */
    public static String aesHexDecrypt(String encryptedPW, String keyStr) {
        AesCipherService aesService = new AesCipherService();
        String pw = new String(aesService.decrypt(Hex.decode(encryptedPW), hexString2Key(keyStr).getEncoded()).getBytes());
        return pw;
    }
}
