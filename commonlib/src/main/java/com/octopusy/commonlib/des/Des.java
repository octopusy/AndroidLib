package com.octopusy.commonlib.des;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

public class Des {

    static String DES = "DES/ECB/NoPadding";
    static String TriDes = "DESede/ECB/NoPadding";

    public static byte[] des_crypt(byte key[], byte data[]) {
        if (data == null) return null;
        byte[] dstData = new byte[((data.length + 7) / 8) * 8];
        System.arraycopy(data, 0, dstData, 0, data.length);
        try {
            KeySpec ks = new DESKeySpec(key);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(DES);
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(dstData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] des_decrypt(byte key[], byte data[]) {
        if (data == null) return null;
        byte[] dstData = new byte[((data.length + 7) / 8) * 8];
        System.arraycopy(data, 0, dstData, 0, data.length);
        try {
            KeySpec ks = new DESKeySpec(key);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(DES);
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(dstData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] trides_crypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];

            if (data == null) return null;
            byte[] dstData = new byte[((data.length + 7) / 8) * 8];
            System.arraycopy(data, 0, dstData, 0, data.length);

            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }

            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(TriDes);
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(dstData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static byte[] trides_decrypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];

            if (data == null) return null;
            byte[] dstData = new byte[((data.length + 7) / 8) * 8];
            System.arraycopy(data, 0, dstData, 0, data.length);

            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }
            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(TriDes);
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(dstData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
