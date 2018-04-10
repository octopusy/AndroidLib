package com.octopusy.commonlib.des;


import java.util.ArrayList;
import java.util.List;


/**
 * 将字符串转换为TLV对象
 */
public class TlvUtils {


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

        byte[] tagData = ByteUtils.hexString2ByteArray(tag);

        System.arraycopy(tagData, 0, tmpData, len, tagData.length);
        len += tagData.length;

        /*、、----
         * 子域长度（即L本身）的属性也为bit，占1～3个字节长度。具体编码规则如下：
           a)  当 L 字段最左边字节的最左 bit 位（即 bit8）为 0，表示该 L 字段占一个字节，
           它的后续 7个 bit 位（即 bit7～bit1）表示子域取值的长度，采用二进制数表示子域取值长度的十进制数。
           例如，某个域取值占 3 个字节，那么其子域取值长度表示为“00000011”。
           所以，若子域取值的长度在 1～127 字节之间，那么该 L 字段本身仅占一个字节。

           b)  当 L 字段最左边字节的最左 bit 位（即 bit8）为 1，表示该 L 字段不止占一个字节，
           那么它到底占几个字节由该最左字节的后续 7 个 bit 位（即 bit7～bit1）的十进制取值表示。
           例如，若最左字节为 10000010，表示 L 字段除该字节外，后面还有两个字节。其后续字节的十进制
取值表示子域取值的长度。例如，若 L 字段为“1000 0001 1111 1111”，表示该子域取值占255 个字节。
所以，若子域取值的长度在 127～255 字节之间，那么该 L 字段本身需占两个字节。
10000010 00000001 00000000-10000010 11111111 11111111
    	  */

        if (length > 127 && length <= 255) {//10000001 10000000---10000001 11111111
            tmpData[len++] = (byte) 0x81;
        }

        if (length > 255 && length <= 65535) {//10000010 00000001 00000000--10000010 11111111 11111111
            tmpData[len++] = (byte) 0x82;
            byte[] tmpLen = ByteUtils.short2ByteArrayHigh((short) length);
            tmpData[len++] = tmpLen[0];
            tmpData[len++] = tmpLen[1];
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

    public static List<String> decodingTLV(String str, String tTag) {

        if (str == null || str.length() % 2 != 0) {
            throw new RuntimeException("Invalid tlv, null or odd length");
        }

//        List<String[]> ls = new ArrayList<String[]>();
        String vv = "";
        List<String> list = new ArrayList<String>();
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
//                System.out.println("tag:" + tag + " len:" + len + " value:" + value);
//                ls.add(new String[] {tag, len, value});
                if (tTag.equalsIgnoreCase(tag)) {
                    vv = value;
                    list.add(value);

                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("Error parsing number", e);
            } catch (IndexOutOfBoundsException e) {
                throw new RuntimeException("Error processing field", e);
            }
        }
        return list;
    }


}
