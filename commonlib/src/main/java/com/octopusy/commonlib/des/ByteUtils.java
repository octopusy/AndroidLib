package com.octopusy.commonlib.des;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具类   byte,int,short,BCD码,ASCII码等转换
 *
 * @author lijinniu
 * @since 2014年12月1日
 */
public class ByteUtils {

    public static byte[] asciiByteArray2BcdArray(byte[] data) {
        return hexString2ByteArray(asciiByteArray2String(data));
    }

    /**
     * ASCII码byte数组转为字符串
     * 方法名：byteArray2String
     * 描 述：aaa
     *
     * @param data
     * @return String 日 期：2014年10月13日 by lijinniu
     */
    public static String asciiByteArray2String(byte[] data) {
        StringBuffer tStringBuf = new StringBuffer();
        char[] tChars = new char[data.length];

        int end = 0;
        int i = 0;
        for (i = 0; i < data.length; i++) {
            if (data[i] == 0x00) break; //遇到0x00就结束
            tChars[i] = (char) data[i];
        }
        end = i;
        tStringBuf.append(tChars, 0, end);

        return tStringBuf.toString();
    }

    /**
     * ASCII码byte数组转为字符串（不去除空格)
     * 方法名：byteArray2String
     * 描 述：aaa
     *
     * @param data
     * @return String 日 期：2014年10月13日 by lijinniu
     */
    public static String asciiByteArray2String1(byte[] data) {
        StringBuffer tStringBuf = new StringBuffer();
        char[] tChars = new char[data.length];

        int end = 0;
        for (int i = 0; i < data.length; i++) {
            if(data[i] == 0x00) break; //遇到0x00就结束
            end = data.length;
            tChars[i] = (char) data[i];
        }

        tStringBuf.append(tChars, 0, end);

        return tStringBuf.toString();
    }

    /**
     * 字符串转换为ASCII码表示的Byte数组
     * 方法名：string2ASCIIByteArray
     * 描 述：
     *
     * @param str
     * @return byte[] 日 期：2014年10月13日 by lijinniu
     */
    public static byte[] string2ASCIIByteArray(String str) {
        byte[] data = null;
        try {
            data = str.getBytes("gbk");
        } catch (UnsupportedEncodingException e) {

            Log.e("xgd", "字符串转换为ASCII码Byte数组错误");
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 最大9999的int值转为BCD码表示的两字节byte数组
     *
     * @param dd 需要转换的值
     * @return 两个字节的byte数组
     */
    public static byte[] int2BCDByteArray(int dd) {
        if (dd > 9999 || dd < 0) {
            return new byte[]{0x00, 0x00};
        }

        StringBuffer hexStr = new StringBuffer(dd + "");
        int strLen = hexStr.length();
        if (strLen != 4) {
            for (int i = 0; i < 4 - strLen; i++) {
                hexStr.insert(0, '0');
            }
        }

        return hexString2ByteArray(hexStr.toString());
    }

    /**
     * 16进制字符串转Byte数组
     * 方法名：hexString2ByteArray
     * 描 述：
     *
     * @param hexStr 需要转换的String，不是偶数后面自动补0
     * @return 日 期：2014年10月13日 by lijinniu
     */
    public static byte[] hexString2ByteArray(String hexStr) {
        if (hexStr == null || hexStr.equals(""))
            return null;
        if (hexStr.length() % 2 != 0) {
            hexStr = hexStr + "0";
        }
        byte[] data = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            char hc = hexStr.charAt(2 * i);
            char lc = hexStr.charAt(2 * i + 1);
            byte hb = hexChar2Byte(hc);
            byte lb = hexChar2Byte(lc);
            if (hb < 0 || lb < 0) {
                return null;
            }
            int n = hb << 4;
            data[i] = (byte) (n + lb);
        }
        return data;
    }

    /**
     * 16进制字符串转Byte
     * 方法名：hexString2ByteArray
     * 描 述：
     *
     * @param hexStr
     * @return 日 期：2015年4月13日 by liyang
     */
    public static byte hexString2Byte(String hexStr) {
        if (TextUtils.isEmpty(hexStr))
            return (byte) 0;
        byte data;
        char hc = hexStr.charAt(0);
        char lc = hexStr.charAt(1);
        byte hb = hexChar2Byte(hc);
        byte lb = hexChar2Byte(lc);
        if (hb < 0 || lb < 0) {
            return (byte) 0;
        }
        int n = hb << 4;
        data = (byte) (n + lb);
        return data;
    }

    public static short[] StringAndShorttoShortArray(short[] args1, String args2) {
        short[] result;
        int length;
        byte[] tmps = hexString2ByteArray(args2.replaceAll(" ", ""));
        if (args1 != null) {
            length = args1.length;
            result = new short[length + tmps.length];
            System.arraycopy(args1, 0, result, 0, length);
        } else {
            length = 0;
            result = new short[tmps.length];
        }
        for (int i = 0; i < tmps.length; i++) {
            result[length + i] = tmps[i];
        }
        return result;
    }

    public static short[] Bytes2ShortArray(byte[] data) {
        short[] result = null;

        if (data != null) {
            result = new short[data.length];
            for (int i = 0; i < data.length; i++) {
                result[i] = data[i];
            }
        }

        return result;
    }

    public static byte hexChar2Byte(char c) {
        if (c >= '0' && c <= '9')
            return (byte) (c - '0');
        if (c >= 'a' && c <= 'f')
            return (byte) (c - 'a' + 10);
        if (c >= 'A' && c <= 'F')
            return (byte) (c - 'A' + 10);
        return -1;
    }

    /**
     * byte[]数组转16进制字符串
     *
     * @param arr
     * @return
     */
    public static String byteArray2HexString(byte[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        StringBuilder sbd = new StringBuilder();
        for (byte b : arr) {
            String tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() < 2)
                tmp = "0" + tmp;
            sbd.append(tmp);
        }
        return sbd.toString();
    }


    /**
     * byte[]数组转16进制字符串
     * 当是 一位的时候 不再补零  liweibin 2016/05/09
     *
     * @param arr
     * @return
     */
    public static String byteArray2HexStringNotAppendZero(byte[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        StringBuilder sbd = new StringBuilder();
        for (byte b : arr) {
            String tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() < 2)
                sbd.append(tmp);
        }
        return sbd.toString();
    }

    /**
     * byte数组转换为带空格的16进制字符串
     * 方法名：byteArray2HexStringWithSpace
     * 描    述：
     *
     * @param arr
     * @return 日   期：2014年10月17日 by lijinniu
     */
    public static String byteArray2HexStringWithSpace(byte[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        StringBuilder sbd = new StringBuilder();
        for (byte b : arr) {
            String tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() < 2)
                tmp = "0" + tmp;
            sbd.append(tmp);
            sbd.append(" ");
        }
        return sbd.toString();
    }

    static public String getBCDString(byte[] data, int start, int end) {
        byte[] t = new byte[end - start + 1];
        System.arraycopy(data, start, t, 0, t.length);
        return byteArray2HexString(t);
    }

    static public String getBCDString(String str) {
        byte[] tmp = hexString2ByteArray(str);
        return getBCDString(tmp, 0, tmp.length);
    }

    static public String getHexString(byte[] data, int start, int end) {
        byte[] t = new byte[end - start + 1];
        System.arraycopy(data, start, t, 0, t.length);
        return byteArray2HexStringWithSpace(t);
    }

    // lxg added.
    public static String shortArray2HexStringWithSpace(short[] arr) {
        StringBuilder sbd = new StringBuilder();
        for (short b : arr) {
            String tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() < 2)
                tmp = "0" + tmp;
            sbd.append(tmp);
            // sbd.append(" ");
        }
        return sbd.toString();
    }

    public static byte[] shortArray2bytes(short[] arr) {
        byte[] sbd = new byte[arr.length];

        for (int i = 0; i < arr.length; i++) {
            sbd[i] = (byte) (0xFF & arr[i]);
        }

        return sbd;
    }

    public static byte[] shortArray2bytes2(short[] arr) {
        byte[] sbd = new byte[arr.length * 2];

        for (int i = 0; i < arr.length; i++) {
            sbd[i * 2 + 1] = (byte) (arr[i] / 256);
            sbd[i * 2] = (byte) (0xFF & arr[i]);
        }

        return sbd;
    }

    /**
     * short类型转byte数组，低位在前
     * 方法名：short2bytes
     *
     * @param arr
     * @return 日   期：2014年10月28日 by lijinniu
     */
    public static byte[] short2ByteArrayLow(short arr) {
        byte[] sbd = new byte[2];
        sbd[1] = (byte) (arr / 256);
        sbd[0] = (byte) (0xFF & arr);
        return sbd;
    }

    /**
     * short转byte数组，高位在前
     * 方法名：short2ByteArrayHigh
     *
     * @param arr
     * @return 日   期：2014年10月28日 by lijinniu
     */
    public static byte[] short2ByteArrayHigh(short arr) {
        byte[] sbd = new byte[2];
        sbd[0] = (byte) (arr / 256);
        sbd[1] = (byte) (0xFF & arr);
        return sbd;
    }

    /**
     * short转换为两个字节的byte数组，使用BCD编码
     * 方法名：short2BcdByteArray
     *
     * @param arr
     */
    public static byte[] short2BcdByteArray(short arr) {
        byte m, n, len = 2;
        byte[] bcdByte = new byte[2];
        for (int i = 0; i < len; i++) {

            byte tmp = (byte) ((arr >> (8 * (len - i - 1))) & 0xff);
            m = (byte) (tmp / 10);
            n = (byte) (tmp % 10);
            //Log.i("xgd", "m=" + m + " n=" + n);
            bcdByte[i] = (byte) ((m << 4) + n);
            //Log.i("xgd", "bcd[" + i + "]=" + bcd[i]);
        }

        return bcdByte;
    }

    /**
     * 转换按BCD码表示的byte 为int
     * 方法名：bcd2Toint
     *
     * @param arg0 BCD码字节
     * @param arg1 BCD码字节
     * @return int值
     * 日   期：2014年10月16日 by lijinniu
     */
    public static int bcdByteArray2Int(byte arg0, byte arg1) {
        byte m, n;
        int tmp = 0;
        int data = 0;

        if ((arg0 & 0x80) == 0x80)
            tmp = arg0 + 256;
        else
            tmp = arg0;
        m = (byte) (tmp / 16);
        n = (byte) (tmp % 16);
        data += m * 1000 + n * 100;

        if ((arg1 & 0x80) == 0x80)
            tmp = arg1 + 256;
        else
            tmp = arg1;
        m = (byte) (tmp / 16);
        n = (byte) (tmp % 16);
        data += m * 10 + n;

        return data;

    }

    /**
     * 转换两字节的BCD码Byte数组 为int
     * 方法名：bcdByteArray2Int
     * 描    述：
     *
     * @param arr BCD码byte数组
     * @return int值
     * 日   期：2014年10月16日 by lijinniu
     */
    public static int bcdByteArray2Int(byte[] arr) {

        byte m, n;
        int tmp = 0;
        int data = 0;

        if ((arr[0] & 0x80) == 0x80)
            tmp = arr[0] + 256;
        else
            tmp = arr[0];
        m = (byte) (tmp / 16);
        n = (byte) (tmp % 16);
        data = m * 1000 + n * 100;
        //System.out.println("data = " + data);

        if ((arr[1] & 0x80) == 0x80)
            tmp = arr[1] + 256;
        else
            tmp = arr[1];
        m = (byte) (tmp / 16);
        n = (byte) (tmp % 16);
        data += m * 10 + n;

        return data;
    }


    /**
     * 两个字节的byte转int,高字节在前
     * 方法名：byte2int
     * 描    述：
     *
     * @param res
     * @return 日   期：2014年10月14日 by lijinniu
     */
    public static int byte2int(final byte[] res, int offset, int length) {
        // res = InversionByte(res);  
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000  
        return (res[1] & 0xff) | ((res[0] << 8) & 0xff00);
    }

    public static int byte2int(byte res0, byte res1) {
        return (res1 & 0xff) | ((res0 << 8) & 0xff00);
    }

    /**
     * int转为4个字节的byte数组，高字节在前
     *
     * @param d int值
     * @return
     * @author lijinniu
     * @since 2014年11月4日
     */
    public static byte[] int2ByteArray(int d) {
        byte[] data = new byte[4];
        data[0] = (byte) (((d) >> 24) & 0xff);
        data[1] = (byte) (((d) >> 16) & 0xff);
        data[2] = (byte) (((d) >> 8) & 0xff);
        data[3] = (byte) ((d) & 0xff);

        return data;
    }

    /**
     * byte值转换为二进制字符串
     *
     * @param bb
     * @return
     * @author lijinniu
     * @since 2014年10月29日
     */
    public static String byte2BinaryString(byte bb) {
        final String ZERO = "00000000";
        String s = Integer.toBinaryString(bb);
        if (s.length() > 8) {
            s = s.substring(s.length() - 8);
        } else if (s.length() < 8) {
            s = ZERO.substring(s.length()) + s;
        }

        return s;
    }

    /**
     * 计算LRC
     * 方法名：getCrc
     * 描    述：
     *
     * @param data  要计算的数据
     * @param start 开始位置，包含
     * @param end   结束位置，包含
     * @return 日   期：2014年10月17日 by lijinniu
     */
    public static byte getCrc(byte[] data, int start, int end) {
        byte crc = 0;
        for (int i = start; i <= end; i++)
            crc ^= data[i];

        return crc;
    }

    /**
     * 组TLV格式的数据
     *
     * @param tag    标签值
     * @param length 数据长度
     * @param data   数据
     * @return TLV格式的byte数组
     * @author lijinniu
     * @since 2015年4月13日
     */
    public static byte[] getTLVData(String tag, int length, byte[] data) {
        byte[] tmpData = new byte[1024];
        int len = 0;

        byte[] tagData = hexString2ByteArray(tag);

        System.arraycopy(tagData, 0, tmpData, len, tagData.length);
        len += tagData.length;

        /*、、----
         * 子域长度（即L本身）的属性也为bit，占1～3个字节长度。具体编码规则如下：
           a)  当 L 字段最左边字节的最左 bit 位（即 bit8）为 0，表示该 L 字段占一个字节，它的后续 7个 bit 位（即 bit7～bit1）表示子域取值的长度，采用二进制数表示子域取值长度的十进制数。例如，某个域取值占 3 个字节，那么其子域取值长度表示为“00000011”。所以，若子域取值的长度在 1～127 字节之间，那么该 L 字段本身仅占一个字节。
           b)  当 L 字段最左边字节的最左 bit 位（即 bit8）为 1，表示该 L 字段不止占一个字节，那么它到底占几个字节由该最左字节的后续 7 个 bit 位（即 bit7～bit1）的十进制取值表示。例如，若最左字节为 10000010，表示 L 字段除该字节外，后面还有两个字节。其后续字节的十进制
取值表示子域取值的长度。例如，若 L 字段为“1000 0001 1111 1111”，表示该子域取值占255 个字节。所以，若子域取值的长度在 127～255 字节之间，那么该 L 字段本身需占两个字节。

    	  */

        if (length > 127 && length < 255) {
            tmpData[len++] = (byte) 0x81;
        }
        tmpData[len++] = (byte) length;

        System.arraycopy(data, 0, tmpData, len, length);
        len += length;

        //返回的数据
        byte[] tlvData = new byte[len];
        System.arraycopy(tmpData, 0, tlvData, 0, len);

        return tlvData;
    }


    /**
     * 把两个数组合并成一个
     *
     * @param first
     * @param second
     * @author wanggaozhuo
     */
    public static byte[] mergeByteArray(byte[] first, byte[] second) {
        if (null == first && null != second) {
            return second;
        }
        if (null == first && null == second) {
            return new byte[0];
        }
        if (null != first && null == second) {
            return first;
        }
        byte[] data = new byte[first.length + second.length];
        System.arraycopy(first, 0, data, 0, first.length);
        System.arraycopy(second, 0, data, first.length, second.length);
        return data;
    }

    /**
     * 从源数据的第srcPost开始
     * 截取len长的数据，构建一个子数组
     *
     * @author wanggaozhuo
     */
    public static byte[] getSubByteArray(byte[] srcData, int srcPos, int len) {
        byte[] destData = new byte[len];
        try {
            System.arraycopy(srcData, srcPos, destData, 0, destData.length);
        } catch (Exception e) {
        }
        return destData;
    }

    /**
     * @fun 在byte[]加一个元素
     * @author wanggaozhuo
     * @since 20150224
     */
    public static byte[] byteAppendOne(byte[] byteArr, byte b) {
        byte[] bb = new byte[]{b};
        byte[] newArr = mergeByteArray(byteArr, bb);
        return newArr;
    }

    /**
     * @fun 把二维搞成一维
     * @author wanggaozhuo
     * @since 20150124
     */
    public static byte[] mergeByteArray(byte[][] twoDime) {
        byte[] temp = new byte[0];
        for (int i = 0; i < twoDime.length; i++) {
            temp = mergeByteArray(temp, twoDime[i]);
        }
        return temp;
    }

    /**
     * @value:普通的字符串，计算出tlv格式的数据
     * @author wanggaozhuo
     * @since 20150128
     */
    public static byte[] getTLVData(String tag, String value) {
        byte[] valueByteArr = string2ASCIIByteArray(value);
        byte[] data = null;
        try {
            data = getTLVData(tag, valueByteArr.length, valueByteArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @value:普通的字符串，计算出tlv格式的数据
     * @author wanggaozhuo
     * @since 20150128
     */
    public static byte[] getTLVData(String tag, byte[] value) {
        byte[] data = null;
        try {
            data = getTLVData(tag, value.length, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] addLL2ByteArr(byte[] arr) {
        if (null == arr || 0 == arr.length) {
            return new byte[0];
        }
        byte[] arrLen = int2BCDByteArray(arr.length);
        byte[] newArr = mergeByteArray(arrLen, arr);
        return newArr;
    }

    /**
     * 将List<Double>转为byte[]数组
     *
     * @author liyang
     * @since 20150325
     */
    public static byte[] DoubleList2ByteArray(ArrayList<Double> result) {
        if (result == null) return new byte[0];
        byte[] r = new byte[result.size()];
        for (int j = 0; j < result.size(); j++) {
            r[j] = (byte) ((new Double(result.get(j))).intValue());
        }
        return r;
    }

    /**
     * 将List<byte[]>转为byte[]数组
     *
     * @author liyang
     * @since 20150325
     */
    public static byte[] ByteArrayList2ByteArray(List<byte[]> list) {
        List<Byte> result = new ArrayList<Byte>();
        for (byte[] l : list) {
            for (int i = 0; i < l.length; i++) {
                result.add(l[i]);
            }
        }
        byte[] r = new byte[result.size()];
        for (int j = 0; j < result.size(); j++) {
            r[j] = (byte) result.get(j);
        }
        return r;

    }

    public static String decodingTLV(String str, String tTag) {

        if (str == null || str.length() % 2 != 0) {
            throw new RuntimeException("Invalid tlv, null or odd length");
        }

//        List<String[]> ls = new ArrayList<String[]>();
        String vv = "";
        for (int i = 0; i < str.length(); ) {
            try {

                String tag = str.substring(i, i = i + 2);
                // extra byte for TAG field
                if ((Integer.parseInt(tag, 16) & 0x1F) == 0x1F) {
                    tag += str.substring(i, i = i + 2);
                }

                String len = str.substring(i, i = i + 2);
                int length = Integer.parseInt(len, 16);
                // more than 1 byte for length
                if (length > 128) {//临界值，当是128即10000000时，长度还是一位，而不是两位
                    int bytesLength = length - 128;
                    len = str.substring(i, i = i + (bytesLength * 2));
                    length = Integer.parseInt(len, 16);
                }
                length *= 2;

                String value = str.substring(i, i = i + length);
                //System.out.println("tag:" + tag + " len:" + len + " value:" + value);
//                ls.add(new String[] {tag, len, value});
                if (tTag.equalsIgnoreCase(tag)) {
                    vv = value;
                    break;
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("Error parsing number", e);
            } catch (IndexOutOfBoundsException e) {
                throw new RuntimeException("Error processing field", e);
            }
        }
        return vv;
    }

    /**
     * 将需要转换成json的byte数组进行Base64编码
     *
     * @param bytes
     * @return
     * @author zengzitao
     */
    public static byte[] byteArrayBase64Encode(byte[] bytes) {
        return Base64.encode(bytes, 1);
    }

    /**
     * 将json里的byte数组进行Base64解码
     *
     * @return
     * @author zengzitao
     */
    public static byte[] byteArrayBase64Decode(byte[] bytes) {
        return Base64.decode(bytes, 1);
    }

    /**
     * bcd转ackii码
     *
     * @return
     * @author liyang 2015-5-28
     */
    public static byte[] bcd2Ascii(byte[] bytes) {

        byte[] temp = new byte[2];
        for (int i = 0; i < bytes.length; i++) {
            if (((bytes[i] & 0xF0) >> 4) <= 9) {
                temp[i * 2] = (byte) (((bytes[i] >> 4) & 0x0f) + 0x30);
            } else {
                temp[i * 2] = (byte) ((bytes[i] >> 4) & 0x0f + 0x37);
            }

            temp[i * 2 + 1] = (byte) ((bytes[i] & 0x0f) + 0x30);

        }
        return temp;
    }

    /**
     * 16进制Byte数组转为二进制字符串
     *
     * @param bArray 16进制数组
     * @return 转换为二进制字符串
     * @author liyang 2015-5-28
     */
    public static String hexByteArray2BinaryStr(byte[] bArray) {
        String[] binaryArray =
                {"0000", "0001", "0010", "0011",
                        "0100", "0101", "0110", "0111",
                        "1000", "1001", "1010", "1011",
                        "1100", "1101", "1110", "1111"};
        String outStr = "";
        int pos = 0;
        for (byte b : bArray) {
            //高四位
            pos = (b & 0xF0) >> 4;
            outStr += binaryArray[pos];
            //低四位
            pos = b & 0x0F;
            outStr += binaryArray[pos];
        }
        return outStr;
    }

    /**
     * 二进制转16进制字符串
     */
    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    /**
     * @函数功能: 10进制串转为BCD码
     * @输入参数: 10进制串
     * @输出结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }

        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;

        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF));
        return value;
    }

    /**
     * 判断一个byte数组最后一个字节是否是中文字符的单个字节，GBK编码落单的字节
     *
     * @param bytes 需要判断的数组
     * @return true有落单字节  false没有落单字节
     */
    public static boolean endOfBytesIsOneGBK(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return false;
        }

//        System.out.println("传入的byte数组长度是：" + bytes.length);
        boolean flag = true;
        if (bytes[bytes.length - 1] < -1) { //判断是否大于0x80 也就是小于-1

            int i = bytes.length - 1;
            for (; i >= 0; i--) {
//                System.out.print(bytes[i] + "\t");
                if (bytes[i] >= 0) {  //判断是否小于0x7f ,也就是大于0，属于英文字符
                    break;
                }
            }

//            System.out.println("ASCII字符位置是：" + i);
            if (i == 0) {

            }
            int cLen = bytes.length - i - 1;
            if (cLen % 2 == 0) {
                flag = false;
            }

        } else {
            flag = false;
        }
        return flag;
    }

    public static byte[] xor8(byte[] src1, byte[] src2) {
        byte[] dst = new byte[8];
        if (src1 == null || src2 == null || src1.length < 8 || src2.length < 8) return null;
        for (int i = 0; i < 8; i++) {
            dst[i] = (byte) (src1[i] ^ src2[i]);
        }
        return dst;
    }

    public static byte[] xor16(byte[] src1, byte[] src2) {
        byte[] dst = new byte[16];
        if (src1 == null || src2 == null || src1.length < 16 || src2.length < 16) return null;
        for (int i = 0; i < 16; i++) {
            dst[i] = (byte) (src1[i] ^ src2[i]);
        }
        return dst;
    }

    public static void main(String[] res) {
        byte[] b1 = {(byte) 0xb4, (byte) 0xf2, (byte) 0xd3, (byte) 0xa1, (byte) 0xd7, (byte) 0xd6, (byte) 0xb7, (byte) 0xfb,
                (byte) 0xb4, (byte) 0xae, (byte) 0xca, (byte) 0xb1, (byte) 0xa3, (byte) 0xba, (byte) 0xd4, (byte) 0xda,
                (byte) 0xd2, (byte) 0xaa, (byte) 0xd3, (byte) 0xd0, (byte) 0xd2, (byte) 0xbb, (byte) 0xb8, (byte) 0xf6,
                (byte) 0x35, (byte) 0xd7, (byte) 0xd6, (byte) 0xbd, (byte) 0xda, (byte) 0xb5, (byte) 0xc4, (byte) 0xca,
                (byte) 0xfd, (byte) 0xbe, (byte) 0xdd, (byte) 0xcd, (byte) 0xb7, (byte) 0xa3};

        byte[] b2 = {(byte) 0xb4, (byte) 0xf2, (byte) 0xd3, (byte) 0xa1, (byte) 0xd7, (byte) 0xd6, (byte) 0xb7, (byte) 0xfb,
                (byte) 0xb4, (byte) 0xae, (byte) 0xca, (byte) 0xb1, (byte) 0xa3, (byte) 0xba, (byte) 0xd4, (byte) 0xda,
                (byte) 0xd2, (byte) 0xaa, (byte) 0xd3, (byte) 0xd0, (byte) 0xd2, (byte) 0xbb, (byte) 0xb8, (byte) 0xf6,
                (byte) 0x35, (byte) 0xd7, (byte) 0xd6, (byte) 0xbd, (byte) 0xda, (byte) 0xb5, (byte) 0xc4, (byte) 0xca,
                (byte) 0xfd, (byte) 0xbe, (byte) 0xdd, (byte) 0xcd, (byte) 0xb7, (byte) 0xa3, (byte) 0xa8, (byte) 0xd7,
                (byte) 0xd6, (byte) 0xcc, (byte) 0xe5, (byte) 0xb4, (byte) 0xf3, (byte) 0xd0, (byte) 0xa1};

        byte[] b3 = {-76, -14, -45, -95, -41, -42, -73, -5, -76, -82, -54, -79, -93, -70, -44, -38, -46, -86, -45, -48, -46, -69, -72, -10};

        for (byte bb : b1) {
            System.out.print(bb + "\t");
        }
        System.out.println();
        for (byte bb : b2) {
            System.out.print(bb + "\t");
        }
        System.out.println();

        boolean bool = endOfBytesIsOneGBK(b1);
        System.out.println("bool = " + bool);
        boolean bool2 = endOfBytesIsOneGBK(b2);
        System.out.println("bool2 = " + bool2);

        boolean bool3 = endOfBytesIsOneGBK(b3);
        System.out.println("bool2 = " + bool3);
    }

    /**
     * 16进制字符串转普通字符串
     *
     * @param string
     * @return
     */
    public static String toStringHex(String string) {
        byte[] baKeyword = new byte[string.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(string.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            string = new String(baKeyword, "utf-8");//UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return string;
    }

    /**
     * 普通字符串转bcd码
     *
     * @param asc
     * @return
     */
    public static byte[] commonString2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * 16进制转二进制取反之后，转回16进制
     *
     * @param hexString
     * @return
     */
    public static String hexStringXOR(String hexString) {
        if (TextUtils.isEmpty(hexString) || hexString.length() == 0) return "";
        String xorString = hexByteArray2BinaryStr(hexString2ByteArray(hexString));
        char[] charArray = xorString.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '0') {
                charArray[i] = '1';
            } else if (charArray[i] == '1') {
                charArray[i] = '0';
            }
        }
        xorString = binaryString2hexString(new String().valueOf(charArray));
        return xorString;
    }

    static final char ascii[] = "0123456789ABCDEF".toCharArray();

    public static String bcd2AscStr(byte bytes[]) {
        return ascii2Str(bcd2Ascii(bytes));
    }

    public static String ascii2Str(byte ascii[]) {
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < ascii.length; i++)
            res.append(strValue(ascii[i]));
        return res.toString();
    }

    private static char strValue(byte asc) {
        if (asc < 0 || asc > 15)
            throw new InvalidParameterException();
        else
            return ascii[asc];
    }

}
