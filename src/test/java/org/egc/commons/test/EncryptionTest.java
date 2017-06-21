package org.egc.commons.test;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import org.egc.commons.security.AesEncryption;
import org.egc.commons.security.EncryptionUtil;
import org.egc.commons.security.JwtUtil;
import org.junit.Assert;
import org.junit.Test;

import java.security.Key;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2016/12/24 15:45
 */
public class EncryptionTest {
	@Test
	public void encryptTest()
	{
		System.out.println(EncryptionUtil.generateSalt());
		System.out.println(EncryptionUtil.getMD5VerifyCode("yesqincheng@sina.com"));
		System.out.println(EncryptionUtil.sha256Encrypt("yesqincheng@sina.com").getEncrypted());
	}

	@Test
	public void aesTest()
	{
		Key key = AesEncryption.getAesKey(128);
		System.out.println(AesEncryption.key2String(key));
		String ks = AesEncryption.key2String(key);
		Key key1 = AesEncryption.string2Key(ks);
		Assert.assertEquals(key, key1);
	}

	@Test
	public void aesTest2()
	{
		AesCipherService aesService = new AesCipherService();
		Key key = AesEncryption.getAesKey(128);
		ByteSource pw = aesService.encrypt("123".getBytes(), key.getEncoded());
		String encryptedPW = Base64.encodeToString(pw.getBytes());
		System.out.println(encryptedPW);
	}

	@Test
	public void aesTest3()
	{
		Key key = AesEncryption.getAesKey(128);
		String ks = AesEncryption.key2String(key);
		String pwd = AesEncryption.aesEncrypt("houzw", ks).getEncrypted();
		System.out.println(ks);
		System.out.println(pwd);
	}

	@Test
	public void aesDecryptTest()
	{
		String epwd = "cSotgxszGapnMOhQiPivVA==";
		String pwd = "houzw";
		String ks = "PJzFUxyZfHV2o+zGXpmpEw==";
		//String pwd2 = AesEncryption.aesDecrypt(epwd, ks);
		// System.out.println(AesEncryption.aesDecrypt(epwd, ks));
		// Assert.assertEquals(pwd, pwd2);
		System.out.println(AesEncryption.cryptoAesDecrypt(epwd, ks));
	}

	@Test
	public void key() {
//        System.out.println(new String(JwtUtil.generateKey().getEncoded(), Charset.forName("UTF-8")));
		System.out.println(JwtUtil.SIGNING_KEY_STRING);
		System.out.println(new String(JwtUtil.SIGNING_KEY.getEncoded()));
	}

	@Test
	public void jwt() {
		System.out.println(JwtUtil.createJwt("1", "me", "yes", 40000));
		//eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.
		//eyJqdGkiOiIxIiwiaWF0IjoxNDk3NzcyMzA3LCJzdWIiOiJ5ZXMiLCJpc3MiOiJtZSIsImV4cCI6MTQ5Nzc3MjM0N30.
		//EpmWHXB4ybldDyFFD8dKjtjhyJ052iWoxkMnyENmAHw
//		System.out.println(DatatypeConverter.printHexBinary(JwtUtil.generateKey().getEncoded()));
		System.out.println(JwtUtil.parseJWT4Username("eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiIxIiwiaWF0IjoxNDk3NzcyMzA3LCJzdWIiOiJ5ZXMiLCJpc3MiOiJtZSIsImV4cCI6MTQ5Nzc3MjM0N30.EpmWHXB4ybldDyFFD8dKjtjhyJ052iWoxkMnyENmAHw"));
	}
}
