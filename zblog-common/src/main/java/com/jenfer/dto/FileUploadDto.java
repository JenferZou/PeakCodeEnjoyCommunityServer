package com.jenfer.dto;

public class FileUploadDto {

    private String localPath;

    private String originalFileName;

    public FileUploadDto() {
    }
    public FileUploadDto(String localPath, String originalFileName) {
        this.localPath = localPath;
        this.originalFileName = originalFileName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
}
