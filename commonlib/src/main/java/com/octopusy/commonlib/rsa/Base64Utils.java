package com.octopusy.commonlib.rsa;
  
import java.io.ByteArrayInputStream;  
import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.InputStream;  
import java.io.OutputStream;  
  
import it.sauronsoftware.base64.Base64;  
  
/** *//** 
 * <p> 
 * BASE64缂栫爜瑙ｇ爜宸ュ叿鍖�? 
 * </p> 
 * <p> 
 * 渚濊禆javabase64-1.3.1.jar 
 * </p> 
 *  
 * @author jun
 * @date 2012-5-19 
 * @version 1.0 
 */  
public class Base64Utils {  
  
    /** *//** 
     * 鏂囦欢璇诲彇缂撳啿鍖哄ぇ灏� 
     */  
    private static final int CACHE_SIZE = 1024;  
      
    /** *//** 
     * <p> 
     * BASE64瀛楃涓茶В鐮佷负浜岃繘鍒舵暟鎹� 
     * </p> 
     *  
     * @param base64 
     * @return 
     * @throws Exception 
     */  
    public static byte[] decode(String base64) throws Exception {  
        return Base64.decode(base64.getBytes());  
    }  
      
    /** *//** 
     * <p> 
     * 浜岃繘鍒舵暟鎹紪鐮佷负BASE64瀛楃涓�? 
     * </p> 
     *  
     * @param bytes 
     * @return 
     * @throws Exception 
     */  
    public static String encode(byte[] bytes) throws Exception {  
        return new String(Base64.encode(bytes));  
    }  
      
    /** *//** 
     * <p> 
     * 灏嗘枃浠剁紪鐮佷负BASE64瀛楃涓�? 
     * </p> 
     * <p> 
     * 澶ф枃浠舵厧鐢紝鍙兘浼氬鑷村唴�?�樻孩鍑�? 
     * </p> 
     *  
     * @param filePath 鏂囦欢缁濆璺�? 
     * @return 
     * @throws Exception 
     */  
    public static String encodeFile(String filePath) throws Exception {  
        byte[] bytes = fileToByte(filePath);  
        return encode(bytes);  
    }  
      
    /** *//** 
     * <p> 
     * BASE64瀛楃涓茶浆鍥炴枃浠�? 
     * </p> 
     *  
     * @param filePath 鏂囦欢缁濆璺�? 
     * @param base64 缂栫爜�?�楃涓�? 
     * @throws Exception 
     */  
    public static void decodeToFile(String filePath, String base64) throws Exception {  
        byte[] bytes = decode(base64);  
        byteArrayToFile(bytes, filePath);  
    }  
      
    /** *//** 
     * <p> 
     * 鏂囦欢杞崲涓轰簩杩涘埗鏁扮�? 
     * </p> 
     *  
     * @param filePath 鏂囦欢璺緞 
     * @return 
     * @throws Exception 
     */  
    public static byte[] fileToByte(String filePath) throws Exception {  
        byte[] data = new byte[0];  
        File file = new File(filePath);  
        if (file.exists()) {  
            FileInputStream in = new FileInputStream(file);  
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);  
            byte[] cache = new byte[CACHE_SIZE];  
            int nRead = 0;  
            while ((nRead = in.read(cache)) != -1) {  
                out.write(cache, 0, nRead);  
                out.flush();  
            }  
            out.close();  
            in.close();  
            data = out.toByteArray();  
         }  
        return data;  
    }  
      
    /** *//** 
     * <p> 
     * 浜岃繘鍒舵暟鎹啓鏂囦欢 
     * </p> 
     *  
     * @param bytes 浜岃繘鍒舵暟鎹� 
     * @param filePath 鏂囦欢鐢熸垚鐩�? 
     */  
    public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {  
        InputStream in = new ByteArrayInputStream(bytes);     
        File destFile = new File(filePath);  
        if (!destFile.getParentFile().exists()) {  
            destFile.getParentFile().mkdirs();  
        }  
        destFile.createNewFile();  
        OutputStream out = new FileOutputStream(destFile);  
        byte[] cache = new byte[CACHE_SIZE];  
        int nRead = 0;  
        while ((nRead = in.read(cache)) != -1) {     
            out.write(cache, 0, nRead);  
            out.flush();  
        }  
        out.close();  
        in.close();  
    }  
      
      
}  
