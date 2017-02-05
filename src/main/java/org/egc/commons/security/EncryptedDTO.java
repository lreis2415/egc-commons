package org.egc.commons.security;

/**
 * 加密后的结果: 密码 & 盐值 / AES加密中的key值
 *
 * @author houzhiwei
 * @date 2016/12/24 15:35
 */
public class EncryptedDTO
{
    public EncryptedDTO(String salt, String encrypted)
    {
        this.salt = salt;
        this.encrypted = encrypted;
    }

    private String salt;

    private String key;

    public String getEncrypted()
    {
        return encrypted;
    }

    private String encrypted;

    /**
     * Gets salt/key (in AES).
     *
     * @return the salt/key(in AES).
     */
    public String getSalt()
    {
        return salt;
    }
}
