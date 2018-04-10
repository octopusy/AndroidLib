package com.octopusy.commonlib.des;

import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

/**
 * Created by maihaoqing on 16-1-26.
 */
public class PlainTrackDeal {

    static String DES_PKCS5  = "DES/ECB/PKCS5Padding";
    static String TDES_PKCS5  = "DESEDE/ECB/PKCS5Padding";

    public static byte[] des_crypt(byte key[], byte data[]) {
        if (data == null) return null;

        try {
            KeySpec ks = new DESKeySpec(key);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(DES_PKCS5);
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] des_decrypt(byte key[], byte data[]) {

        if (data == null) return null;
        try {
            KeySpec ks = new DESKeySpec(key);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(DES_PKCS5);
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] trides_crypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];

            if (data == null) return null;

            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }

            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(TDES_PKCS5);
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static byte[] trides_decrypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];

            if (data == null) return null;

            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }
            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(TDES_PKCS5);
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static byte[] ByteXor(byte[] data1, byte[] data2, int len) {
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = (byte) (data1[i] ^ data2[i]);
        }
        return result;
    }

    private static byte[] GenDesKey(byte algNo, byte[] random){

        byte[] tmp = new byte[8];
        byte[] outKey = null;

       if(random.length < 8) {
            return null;
        }

        Arrays.fill(tmp, algNo);
        switch(algNo)
        {
            case 0x01:
                outKey = new byte[8];
                System.arraycopy(ByteXor(tmp, random, 8), 0, outKey, 0, 8);
                break;

            case 0x02:
                outKey = new byte[16];
                System.arraycopy(ByteXor(tmp, random, 8), 0, outKey, 0, 8);
                Arrays.fill(tmp, (byte) 0xFF);
                System.arraycopy(ByteXor(tmp, random, 8), 0, outKey, 8, 8);
                break;

            case 0x03:
                outKey = new byte[24];
                System.arraycopy(ByteXor(tmp, random, 8), 0, outKey, 0, 8);
                Arrays.fill(tmp, (byte) 0xFF);
                System.arraycopy(ByteXor(tmp, random, 8), 0, outKey, 8, 8);
                Arrays.fill(tmp, (byte) 0x77);
                System.arraycopy(ByteXor(tmp, random, 8), 0, outKey, 16, 8);
                break;

            default:
                return null;
        }
        return outKey;
    }

    public static byte[] TrackDataDeal(byte[] trackData, byte[] random, byte algNo, boolean isEncrypt){
        byte[] key = null;
        byte[] outData = new byte[trackData.length];

        Arrays.fill(outData, (byte)0x00);
        key = GenDesKey(algNo, random);

        if (isEncrypt)
        {
            switch(algNo)
            {
                case 0x01:
                    outData = des_crypt(key, trackData);
                    break;

                case 0x02:
                case 0x03:
                    outData =  trides_crypt(key, trackData);
                    break;

                default:
                    return null;
            }
        } else {
            switch(algNo)
            {
                case 0x01:
                    outData = des_decrypt(key, trackData);
                    break;

                case 0x02:
                case 0x03:
                    outData =  trides_decrypt(key, trackData);
                    break;

                default:
                    return null;
            }

        }
        return outData;
    }

}
