package com.imscv.osssdkexample;

/**
 * Created by David Wong on 2016/1/27.
 */
public class FileInfoFromServer {
    private int fileInfoId;
    private String fileName;
    private String fileFormat;
    private String fileUrl;
    private String thumb;

    public FileInfoFromServer(int fileInfoId, String fileName, String fileFormat, String fileUrl, String thumb) {
        this.fileInfoId = fileInfoId;
        this.fileName = fileName;
        this.fileFormat = fileFormat;
        this.fileUrl = fileUrl;
        this.thumb = thumb;
    }

    public int getFileInfoId() {
        return fileInfoId;
    }

    public void setFileInfoId(int fileInfoId) {
        this.fileInfoId = fileInfoId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "FileInfoFromServer{" +
                "fileInfoId=" + fileInfoId +
                ", fileName='" + fileName + '\'' +
                ", fileFormat='" + fileFormat + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(fileName.equals(((FileInfoFromServer)o).fileName)) {
            return true;
        } else {
            return false;
        }
    }
}
