package com.imscv.osssdk;

/**
 * Created by David Wong on 2016/1/14.
 */
public class UploadFileInfoInServer {
    private String fileName;
    private String fileUrl;
    private long fileSize;
    private int fileType;   // 1图片 2视频
    private String fileFormat;

    public UploadFileInfoInServer(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public UploadFileInfoInServer(String fileName, String fileUrl, long fileSize, int fileType, String fileFormat) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileFormat = fileFormat;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
}
