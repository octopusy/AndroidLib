package com.octopusy.commonlib.rsa;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class MainTest {

	private static byte[] cipherData = null;
	private static byte[] res = null;

	private static RSAPrivateKey privateKey = null;
	private static RSAPublicKey publicKey = null;

	static String prikey1 = "";

	static String publicKeyString = "";

	public static void main(String[] args) throws Exception {
		privateKey = RSAEncrypt.loadPrivateKeyByStr(prikey1);
		publicKey = RSAEncrypt.loadPublicKeyByStr(Base64.decode(publicKeyString));

		System.out.println("--------------公钥加密私钥解密过程-------------------");
		String plainText="ihep_公钥加密私钥解密";
		//公钥加密过程
		cipherData = RSAEncrypt.encrypt(publicKey,plainText.getBytes());
		String cipher= Base64.encode(cipherData);
		//私钥解密过程
		res = RSAEncrypt.decrypt(privateKey, Base64.decode(cipher));
		String restr = new String(res);
		System.out.println("原文：" + plainText);
		System.out.println("加密：" + cipher);
		System.out.println("解密：" + restr);
		System.out.println();

		System.out.println("--------------私钥加密公钥解密过程-------------------");
		plainText="ihep_私钥加密公钥解密";
		//私钥加密过程
		cipherData = RSAEncrypt.encrypt(privateKey,plainText.getBytes());
		cipher = Base64.encode(cipherData);
		//公钥解密过程
		res = RSAEncrypt.decrypt(publicKey, Base64.decode(cipher));
		restr = new String(res);
		System.out.println("原文：" + plainText);
		System.out.println("加密：" + cipher);
		System.out.println("解密：" + restr);
		System.out.println();

		System.out.println("---------------私钥签名过程------------------");
		String content="ihep_这是用于签名的原始数据";
		String signstr = RSASignature.sign(content,prikey1);
		System.out.println("签名原串：" + content);
		System.out.println("签名串：" + signstr);
		System.out.println();

		System.out.println("---------------公钥校验签名------------------");
		System.out.println("签名原串：" + content);
		System.out.println("签名串：" + signstr);

		System.out.println("验签结果：" + RSASignature.doCheck(content, signstr, publicKeyString));
	}

}
