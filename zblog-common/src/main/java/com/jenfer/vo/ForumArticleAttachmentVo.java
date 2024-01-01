package com.jenfer.vo;

import java.io.Serializable;

public class ForumArticleAttachmentVo implements Serializable {

    private String file_id;

    private Long file_size;


    private String file_name;

    private Integer download_count;

    private Integer file_type;

    private Integer integral;

    public ForumArticleAttachmentVo() {
    }

    public ForumArticleAttachmentVo(String file_id, Long file_size, String file_name, Integer download_count, Integer file_type, Integer integral) {
        this.file_id = file_id;
        this.file_size = file_size;
        this.file_name = file_name;
        this.download_count = download_count;
        this.file_type = file_type;
        this.integral = integral;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public Long getFile_size() {
        return file_size;
    }

    public void setFile_size(Long file_size) {
        this.file_size = file_size;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public Integer getDownload_count() {
        return download_count;
    }

    public void setDownload_count(Integer download_count) {
        this.download_count = download_count;
    }

    public Integer getFile_type() {
        return file_type;
    }

    public void setFile_type(Integer file_type) {
        this.file_type = file_type;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }
}
