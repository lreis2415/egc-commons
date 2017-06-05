package org.egc.commons.security;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.OperationMode;
import org.apache.shiro.crypto.PaddingScheme;
import org.apache.shiro.util.ByteSource;

import java.security.Key;

/**
 * Basic AES Encryption Functions
 *
 * @author houzhiwei
 * @date 2017/1/11 15:00
 */
public abstract class BaseAesEncryption
{
    /**
     * AES encrypt password
     *
     * @param pwd     password
     * @param key     key
     * @param keySize keySize 128/192/256
     * @return the encrypted Base64 encoded password string
     */
    protected static String aesEncrypt(String pwd, Key key, int keySize)
    {
        ByteSource pwdBs = ByteSource.Util.bytes(aesEncrypt2Bytes(pwd, key, keySize));
        return Base64.encodeToString(pwdBs.getBytes());
    }

    /**
     * Aes encrypt to hex password string.
     *
     * @param pwd     the pwd
     * @param key     the key
     * @param keySize the key size 128/192/256
     * @return the encrypted Hex encoded password string
     */
    protected static String aesEncrypt2Hex(String pwd, Key key, int keySize)
    {
        ByteSource pwdBs = ByteSource.Util.bytes(aesEncrypt2Bytes(pwd, key, keySize));
        return Hex.encodeToString(pwdBs.getBytes());
    }

    /**
     * AES encrypt password
     *
     * @param pwd     password
     * @param key     key
     * @param keySize keySize 128/192/256
     * @return the encrypted password in bytes
     */
    private static byte[] aesEncrypt2Bytes(String pwd, Key key, int keySize)
    {
        AesCipherService aesService = new AesCipherService();
        aesService.setKeySize(keySize);
        //ByteSource.getBytes()
        return aesService.encrypt(pwd.getBytes(), key.getEncoded()).getBytes();
    }

    private static byte[] aesEncrypt2Bytes(String pwd, Key key, int keySize, PaddingScheme scheme, boolean generateIV, OperationMode mode)
    {
        AesCipherService aesService = new AesCipherService();
        aesService.setKeySize(keySize);
        aesService.setPaddingScheme(scheme);
        aesService.setGenerateInitializationVectors(generateIV);
        aesService.setMode(mode);
        //ByteSource.getBytes()
        return aesService.encrypt(pwd.getBytes(), key.getEncoded()).getBytes();
    }
}
