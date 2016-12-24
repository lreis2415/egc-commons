package org.egc.commons.security;

/**
 * 加密后的结果: 密码 & 盐值
 * @author houzhiwei
 * @date 2016/12/24 15:35
 */
public class Encrypted
{
    public Encrypted(String salt, String hashed)
    {
        this.salt = salt;
        this.hashed = hashed;
    }

    private String salt;

    public String getHashed()
    {
        return hashed;
    }

    private String hashed;

    public String getSalt()
    {
        return salt;
    }


}
