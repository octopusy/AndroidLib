package com.octopusy.commonlib.file;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.octopusy.commonlib.des.ByteUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作类
 *
 * @author lijinniu
 * @since 2014年11月20日
 */
@SuppressLint("DefaultLocale")
public class FileOperation {

    private static byte[] data;
    private static List<FileBean> listFolder = new ArrayList();

    public FileOperation() {
    }


    /**
     * 获取文件大小，按字节算
     *
     * @param f 文件
     * @return 文件长度
     * @throws Exception
     */
    public static int getFileSizes(File f) throws Exception {
        int s = 0;
        Log.d("xgd", "文件路径" + f.getAbsolutePath());
        if (f.exists()) {
            FileInputStream fiss = new FileInputStream(f);
            s = fiss.available();
            data = new byte[4];
            data[3] = (byte) (s & 0xff);
            data[2] = (byte) ((s >> 8) & 0xff);
            data[1] = (byte) ((s >> 16) & 0xff);
            data[0] = (byte) ((s >> 24) & 0xff);

            fiss.close();
        } else {
            boolean bool = f.createNewFile();
            if(bool){
                return 0;
            }
        }
        return s;
    }

    /**
     * 文件夹中包含文件和文件夹的个数，及文件夹下文件的集合
     *
     * @param folder 文件夹
     * @return 返回文件的集合
     * @throws Exception
     */
    public static List<FileBean> getlist(File folder)
            throws Exception {

        List<FileBean> list = new ArrayList<FileBean>();

        if (!folder.exists()) {
            folder.mkdir();
        }
        byte fileCount = 0;
        int folderCount = 0;

        File[] flist = folder.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isFile()) {
                FileBean file = new FileBean();
                fileCount++;
                file.setFileName(flist[i].getName());
                file.setFilePath(folder + "/" + flist[i].getName());
                file.setFileLength(FileOperation.getFileSizes(new File(folder
                        + "/" + flist[i].getName())));
                file.setFileLengthByByte(data);
                list.add(file);
            } else if (flist[i].isDirectory()) {
                String filePath = folder + "/" + flist[i].getName();
                File sonFolder = new File(filePath);
                List<FileBean> sonFiles = getlist(sonFolder);
                int length = 0;
                if (sonFiles.size() > 0) {
                    for (FileBean ff : sonFiles) {
                        length += ff.getFileLength();
                    }
                }

                FileBean file = new FileBean();
                file.setFileName(flist[i].getName());
                file.setFilePath(filePath);
                file.setFileLength(length);
                file.setFileLengthByByte(data);
                folderCount++;
                listFolder.add(file);
            }
        }
        Log.i("xgdop2", "文件夹中文件个数fileCount=" + fileCount);
        Log.i("xgdop2", "文件夹中文件夹个数folderCount=" + folderCount);
        return list;
    }

    /**
     * 获取文件夹下子文件夹的详细信息
     * @param folder  父文件夹
     * @return 子文件夹集合
     * @throws Exception
     * @since 2014年11月27日
     */
    public static List<FileBean> getFolderList(File folder) throws Exception {
        if (listFolder != null) {
            listFolder.clear();
        }
        getlist(folder);
        return listFolder;
    }

    /**
     * 按指定长度读取文件
     *
     * @param file       实体类FileBean
     * @param curbagnum  当前包包序号，从1包开始读取文件
     * @param readLength 要读取的长度
     * @return 长度为readLength的byte数组
     */
    public static byte[] readFileByRandomAccess(FileBean file, int curbagnum, int readLength) {
        RandomAccessFile randomFile = null;
        int beginIndex = 0;
        // int byteread = 0;
        byte[] bytes = new byte[readLength];

        try {
            Log.i("xgdop2", "正在读取文件" + file.getFileName() + "  第" + curbagnum
                    + "包数据");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(file.getFilePath(), "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            Log.i("xgdop2", "RandomAccessFile读取文件长度:" + fileLength);
            Log.i("xgdop2", "FileBean中文件长度:" + file.getFileLength());
            // 读文件的起始位置
            if (curbagnum < file.getFileLength()) {
                // beginIndex = ((curbagnum - 1) * 1024);
                // FIXME: 2017/5/24 zhanghuan 修改读文件长度
                beginIndex = ((curbagnum - 1) * readLength);
            }
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            // 将一次读取的字节数赋给byteread

            randomFile.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                    Log.d("FileOperation","关闭RandomAccessFile错误");
                }
            }
        }
        return bytes;
    }

    /**
     * 按指定长度读取文件
     * @param file 实体类FileBean
     * @param curbagnum 当前包包序号，从1包开始读取文件
     * @param readLength 本次要读取的长度
     * @param maxReadLength 读取的最大固定长度，
     * @return 长度为readLength的byte数组
     */
    public static byte[] readFileByRandomAccess(FileBean file, int curbagnum,int readLength,int maxReadLength) {
        RandomAccessFile randomFile = null;
        int beginIndex = 0;
        byte[] bytes = new byte[readLength];

        try {
            Log.i("xgdop2", "正在读取文件" + file.getFileName() + "  第" + curbagnum
                    + "包数据");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(file.getFilePath(), "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            Log.i("xgdop2", "RandomAccessFile读取文件长度:" + fileLength);
            Log.i("xgdop2", "FileBean中文件长度:" + file.getFileLength());
            // 读文件的起始位置
            if (curbagnum < file.getFileLength()) {
                beginIndex = ((curbagnum - 1) * maxReadLength);
                //beginIndex = (curbagnum * readLength);
            }
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            // 将一次读取的字节数赋给byteread

            randomFile.read(bytes);

            //写入TXT文件
//            savedToTextA(bytes);
//            savedToTextB(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
        return bytes;
    }

    /**
     * 按字符读取文件
     *
     * @return String
     */
    public static String readFromFile(FileBean fileBean) {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            File targetFile = new File(fileBean.getFilePath());
            String readedStr = "";
            try {
                if (!targetFile.exists()) {
                    Log.i("lianghuiyuan", "路径不存在......");
                    targetFile.createNewFile();
                    return "No File error ";
                } else {
                    InputStream in = new BufferedInputStream(
                            new FileInputStream(targetFile));
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(in, "UTF-8"));
                    String tmp;
                    Log.i("lianghuiyuan", "读取文件....");
                    while ((tmp = br.readLine()) != null) {
                        readedStr += tmp;
                    }
                    br.close();
                    in.close();
                    Log.i("lianghuiyuan", "从文件中读出来的数据:" + readedStr);
                    return readedStr;
                }
            } catch (Exception e) {
                return e.toString();
            }
        } else {
            return "SD Card error";
        }
    }

    /**
     * 按字节读取文件
     *
     * @param fileBean
     * @return byte数组
     */
    public static byte[] readFileByByte(FileBean fileBean) {
        byte[] fileByte = new byte[fileBean.getFileLength()];
        Log.i("xgdop2", "按字节读取文件，文件长度" + fileByte.length);
        File file = new File(fileBean.getFilePath());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(fileByte);
//            savedToText_ls(fileByte);//按字节写入txt文件中
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        return fileByte;
    }

    /**
     * 写入文件
     * @param  bytes 需要写入文件的内容，byte数组
     */
    public static void savedToTextA(byte[] bytes) {
        Log.i("xgdop2", "正在写入byte数据到文件");
        File folder = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/bluetooth");
        if (folder == null || !folder.exists()) {
            folder.mkdir();
        }
        String fileName = "/ttbin.txt";
        File targetFile = new File(folder + fileName);

        String content = ByteUtils.byteArray2HexStringWithSpace(bytes);
        try {
            if (!targetFile.exists()) {
                Log.d("写文件","文件不存在，新建文件");
                targetFile.createNewFile();
            }

            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(targetFile, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        OutputStreamWriter osw;
//        try {
//            if (!targetFile.exists()) {
//                System.out.println("文件不存在，新建文件");
//                targetFile.createNewFile();
//            }
//            osw = new OutputStreamWriter(new FileOutputStream(targetFile),
//                    "utf-8");
//            for (int i = 0; i < bytes.length; i++) {
//                String hv = Integer.toHexString(bytes[i] & 0xFF);
//                if (hv.length() < 2) {
//                    hv = "0" + hv;
//                }
//
//                osw.write("0x" + hv.toUpperCase() + " ");
//            }
//
//            osw.flush();
//            osw.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void savedToTextB(byte[] bytes) {
        Log.i("xgdop2_ls_bin", "正在写入byte数据到文件ls_bin");
        File folder = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/bluetooth");
        if (folder == null || !folder.exists()) {
            folder.mkdir();
        }
        String fileName = "/ls_bin.bin";
        File targetFile = new File(folder + fileName);

        try {
            if (!targetFile.exists()) {
                Log.d("FileOperation","文件不存在，新建文件");
                targetFile.createNewFile();
            }

            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(targetFile, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.write(bytes);
            randomFile.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

//        OutputStreamWriter osw;
//        try {
//            if (!targetFile.exists()) {
//                System.out.println("文件不存在，新建文件");
//                targetFile.createNewFile();
//            }
//            osw = new OutputStreamWriter(new FileOutputStream(targetFile),
//                    "utf-8");
//            for (int i = 0; i < bytes.length; i++) {
//                String hv = Integer.toHexString(bytes[i] & 0xFF);
//                if (hv.length() < 2) {
//                    hv = "0" + hv;
//                }
//
//                osw.write("0x" + hv.toUpperCase() + " ");
//            }
//
//            osw.flush();
//            osw.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                Log.e("xgd", "选择下载文件错误");
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
