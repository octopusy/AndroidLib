package com.octopusy.commonlib.file;

/**
 * 文件属性类，包含文件名称，文件长度，文件路径
 *
 * @author lijinniu
 */
public class FileBean {

    private String fileName;
    private int fileLength;
    private byte[] fileLengthByByte;
    private String filePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileLength() {
        return fileLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getFileLengthByByte() {
        return fileLengthByByte;
    }

    public void setFileLengthByByte(byte[] fileLengthByByte) {
        this.fileLengthByByte = fileLengthByByte;
    }


}
