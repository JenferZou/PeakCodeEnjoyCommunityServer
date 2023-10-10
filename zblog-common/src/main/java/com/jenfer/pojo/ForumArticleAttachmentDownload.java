package com.jenfer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户附件下载
 * @TableName forum_article_attachment_download
 */
@TableName(value ="forum_article_attachment_download")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumArticleAttachmentDownload implements Serializable {
    /**
     * 文件ID
     */
    @TableId(value = "file_id")
    private String file_id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String user_id;

    /**
     * 文章ID
     */
    @TableField(value = "article_id")
    private String article_id;

    /**
     * 文件下载次数
     */
    @TableField(value = "download_count")
    private Integer download_count;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ForumArticleAttachmentDownload other = (ForumArticleAttachmentDownload) that;
        return (this.getFile_id() == null ? other.getFile_id() == null : this.getFile_id().equals(other.getFile_id()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getArticle_id() == null ? other.getArticle_id() == null : this.getArticle_id().equals(other.getArticle_id()))
            && (this.getDownload_count() == null ? other.getDownload_count() == null : this.getDownload_count().equals(other.getDownload_count()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFile_id() == null) ? 0 : getFile_id().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getArticle_id() == null) ? 0 : getArticle_id().hashCode());
        result = prime * result + ((getDownload_count() == null) ? 0 : getDownload_count().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", file_id=").append(file_id);
        sb.append(", user_id=").append(user_id);
        sb.append(", article_id=").append(article_id);
        sb.append(", download_count=").append(download_count);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}