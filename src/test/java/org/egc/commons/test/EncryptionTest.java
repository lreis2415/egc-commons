package org.egc.commons.test;

import org.egc.commons.security.EncryptionUtil;
import org.junit.Test;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2016/12/24 15:45
 */
public class EncryptionTest
{
    @Test
    public void encryptTest(){
        System.out.println(EncryptionUtil.generateSalt());
        System.out.println(EncryptionUtil.getMD5VerifyCode("yesqincheng@sina.com"));
        System.out.println(EncryptionUtil.sha256Encrypt("yesqincheng@sina.com").getHashed());
    }
}
